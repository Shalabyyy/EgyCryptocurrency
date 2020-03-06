package network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;

import cryptography.CustomMath;
import cryptography.SHA256;
import blockchain.Block;
import transaction.Transaction;

public class Node{
	//User Settings
	private String networkID;
	private String public_address;
	private String private_address;
	private double balance;
	private ArrayList<Transaction>  transactionHistory;
	private Node nearest_node;
	private DecentralizedNetwork network;

	//Network Simulation Settings
	private double xCoor;
	private double yCoor;
	private String role;
	private ArrayList<Transaction> validated ;
	private ArrayList<Block> validatedBlock ;

	//Autonomous settings
	private ArrayList<Transaction> unvalidatedTransactionBuffer;
	private ArrayList<Transaction> validatedTransactionBuffer;

	private ArrayList<Block> unvalidatedBlockBuffer;
	private ArrayList<Block> blockVotingBuffer;

	public Node(double xCoor, double yCoor, String role,DecentralizedNetwork network ){
		this.setNetworkID("To Be Ammended Later");
		this.setPrivate_address(SHA256.generatePrivateAddress());
		this.setPublic_address(SHA256.hashValue(this.getPrivate_address()));
		this.setBalance(0);
		this.transactionHistory= new ArrayList<Transaction>();
		this.setxCoor(xCoor);
		this.setyCoor(yCoor);
		this.setRole(role);
		this.network= network;
		this.setValidated(new ArrayList<Transaction>());
		this.setValidatedBlock(new ArrayList<Block>());
		System.out.println("Node "+getPublic_address()+" was Created");
		locateNearestNode();
		//TODO Give a copy of the buffers to the new node
		if(getNetwork().getNodes().size()==1){}
		else{}
		
		setValidatedTransactionBuffer(new ArrayList<Transaction>());
		setUnvalidatedTransactionBuffer(new ArrayList<Transaction>());
		setBlockVotingBuffer(new ArrayList<Block>());
		setUnvalidatedBlockBuffer(new ArrayList<Block>());




	}
	public boolean validateTransaction(Transaction toBeValidated){
		//Check that the node does not validate itself
		if(toBeValidated.getSender().equals(getPublic_address()) || 
				toBeValidated.getRecepient().equals(getPublic_address())){
			System.out.println(getPublic_address().substring(0,6)+" Can't Validate it's own Transaction");
			return false;
		}
		//Check that the node did not validate this transaction before
		if(getValidated().contains(toBeValidated)){
			System.out.println(getPublic_address().substring(0,6)+" Already Validated This Transaction!");
			return false;	
		}
		ArrayList<Node> other_nodes = network.getNodes();
		boolean foundSender = false;
		boolean suffcientAmmount = false;
		boolean foundRecipient= false;
		for(int i=0;i<other_nodes.size();i++){
			if(toBeValidated.getSender().equals(other_nodes.get(i).getPublic_address())){
				//The User Has been Found
				foundSender= true;
				if(other_nodes.get(i).getBalance()>=toBeValidated.getAmount()){
					//The User has enough funds
					suffcientAmmount= true;
				}
			}
			if(toBeValidated.getRecepient().equals(other_nodes.get(i).getPublic_address())){
				//The Recipient has been found Has been Found
				foundRecipient=true;
			}

			//No need to keep iterating if all is found
			if(foundSender && foundRecipient)
				break;
		}

		//If all of the conditions are met, The Transaction is Validated
		if(foundSender && foundRecipient && suffcientAmmount){
			System.out.println("Node "+getPublic_address().substring(0,6) +" Validated Transaction "
					+toBeValidated.getHash().substring(0, 6));
			toBeValidated.setTimesValidated(toBeValidated.getTimesValidated()+1);
			getValidated().add(toBeValidated);
			//If the Transaction is confirmed Update Accounts
			if(toBeValidated.isConfirmed()){
				System.out.println(toBeValidated.getHash().substring(0, 6)+" Finished Validation 100%");
				network.updateBalances(toBeValidated);
			}
			return true;
		}
		else {
			//Increment the number of Validations Failures
			toBeValidated.setTimesError(toBeValidated.getTimesError()+1);
			getValidated().add(toBeValidated);
			if(!suffcientAmmount)
				System.out.println("Transaction "+toBeValidated.getHash().substring(0, 6)+
						" Verfication Failed by Node "+getPublic_address().substring(0,6)+" Due to Insuffcient Funds");
			if(!foundRecipient)
				System.out.println("Transaction "+toBeValidated.getHash().substring(0, 6)+
						" Verfication Failed by Node "+getPublic_address().substring(0,6) +" Invalid Recpient address");
			if(!foundSender)
				System.out.println("Transaction "+toBeValidated.getHash().substring(0, 6)+
						" Verfication Failed by Node "+getPublic_address().substring(0,6)+" Invalid Sender address");
			return false;
		}

	}
	public boolean validateBlock(Block toBeValidated){
		//Make sure that the Block was not Validated by this node before
		if(getValidatedBlock().contains(toBeValidated)){
			System.out.printf("Node %s Already Validated Block %s \n",
					getPublic_address().substring(0,6),toBeValidated.getHash().substring(0, 6));
			return false;
		}

		ArrayList<Transaction> transactions = toBeValidated.getTransaction();
		boolean confirmedTransactions = false;

		//Make Sure no Tampers Were Made 
		boolean legitMerkle = SHA256.validateMerkle(toBeValidated.getMerkle_root(),transactions);
		if(!legitMerkle){
			validatedBlock.add(toBeValidated);
			System.out.println("Merkle Root Mismatch by Node "+getPublic_address().substring(0, 6));
			return false;
		}

		//Make sure that all transactions are verified
		for(int i=0; i<transactions.size();i++){
			if(!transactions.get(i).isConfirmed()){
				System.out.println("Not All Transactions were Verified by Node "+getPublic_address().substring(0, 6));
				validatedBlock.add(toBeValidated);
				return false;
			}
		}

		confirmedTransactions=true;

		if(confirmedTransactions && legitMerkle){
			toBeValidated.setTimesValidated(toBeValidated.getTimesValidated()+1);
			getValidatedBlock().add(toBeValidated);
			System.out.println(getPublic_address().substring(0, 6)+" Validated Block "+toBeValidated.getHash().substring(0, 6));
		}
		if(toBeValidated.isConfirmed())
			System.out.println(toBeValidated.getHash().substring(0, 6)+" is 100% Valid");
		return (confirmedTransactions && legitMerkle);

	}

	@SuppressWarnings("unused")
	private String proofOfWork(char leadingChar, Block block) {

		int leadingzeros= block.getNonce();
		String leading = CustomMath.numberToChars(leadingzeros,leadingChar);
		System.out.println(leading);
		String message=block.getMessage();
		String newMsg = "";
		String newHash = "";
		int nonceCounter = 1;
		boolean flag = false;
		while(!flag){
			newMsg = message+nonceCounter;
			newHash = SHA256.hashValue(newMsg);
			flag = newHash.substring(0, leadingzeros).equals(leading);
			nonceCounter++;
			//System.out.println(newMsg+" Has Produced "+newHash);	
		}
		//TODO Later to be Added in a Log File and Reward the Miner
		System.out.printf("Succcessfuy Mined ! Hash is %s",newHash);
		System.out.println("Your Reward Will be Added Shortly");
		return newHash;

	}

	//TODO Later on This Should be Private, Left as public for testing
	public Transaction sendFunds(double amount, String address){
		boolean addressFound = false;

		//Check Funds if they are enough
		if(amount>getBalance()){
			System.out.println("Insuffecient funds");
			return null;
		}
		for(int i=0; i<network.getNodes().size();i++ ){
			if(network.getNodes().get(i).getPublic_address().equals(address)){
				//If the Address is found it means that the Address Exists
				addressFound=true;
				break;
			}
		}
		if(addressFound)
		{
			System.out.println(getPublic_address().substring(0,6)+" ->("+amount+") -> "+
					address.substring(0,6)+" Is being Proccessed");
			Transaction transaction = new Transaction(getPublic_address(), address, amount);
			broadcastTransaction(transaction);
			return transaction;
		}
		else{
			System.out.println("Invalid Recepient Address");
			return null;
		}

	}
	public Node locateNearestNode(){
		//Check if it is only me
		if(network.getNodes().size()==1){
			System.out.println("You are the only Node in the Network");
			return this;
		}
		//Check Lowest Possible Distance Between this Node and some other node N
		String minNode = "";
		double min=Double.MAX_VALUE ;
		double computation = 0;
		Node nearest = null;
		for(int i=0; i<network.getNodes().size();i++){
			Node n = network.getNodes().get(i);
			if(n!=null){
				computation = CustomMath.calculateVector(getxCoor(), getyCoor(), n.getxCoor(), n.getyCoor());
				if(min>computation){
					min=computation;
					minNode=n.getPublic_address();	
					nearest=n;
				}	
			}

		}
		System.out.println(minNode+" Is you nearest Node with a distance of "+Math.round(min));

		//Set The Nearted Node
		if(nearest!=null)
			setNearest_node(nearest);

		return nearest;

	}
	public void updateLedger(Transaction transaction){
		//After The Transaction is Verfied and Validated This Method will run and funds will be updated
		if(!transaction.isConfirmed()){
			System.out.println("Can't add an Unverified transaction to the Ledger");
			return;
		}
		//Now we have to see if the user sent or received funds
		if(transaction.getSender().equals(getPublic_address())){
			//If The User is The Sender he is Debited (Negation)

			//However we need to terminate double spending
			if(balance<transaction.getAmount()){
				String id = transaction.getHash().substring(0, 6);
				System.out.println("Risk of Double Spending Transaction "+id+ "Will be Terminated");
				transaction.terminate();
				return;
			}
			balance = balance - transaction.getAmount();
			transactionHistory.add(transaction);

		}
		else if(transaction.getRecepient().equals(getPublic_address())){
			//If The User is The Sender he is Credited
			balance = balance + transaction.getAmount();
			transactionHistory.add(transaction);
		}
		else{
			System.out.println("Proccess Terminated");
		}

	}
	public void displayCredit(){
		//Display All Funds Received
		for(int i=0;i<transactionHistory.size();i++){
			if(transactionHistory.get(i).getRecepient().equals(getPublic_address())){
				transactionHistory.get(i).display();
			}
		}
	}
	public void displayDebit(){
		//Display All Funds Sent
		for(int i=0;i<transactionHistory.size();i++){
			if(transactionHistory.get(i).getSender().equals(getPublic_address())){
				transactionHistory.get(i).display();
			}
		}
	}

	//Automation Methods
	public void broadcastTransaction(Transaction transaction){
		if(getNetwork().getNodes().size()<=2){
			System.out.println("Can't Broadcast when you are the only node");
			return;
		}
		int me = getNetwork().getNodes().indexOf(this); //get my reference on the network
		int receiver=-1;

		//getting reference of the recipent
		for(int i=0; i<network.getNodes().size();i++ ){
			if(network.getNodes().get(i).getPublic_address().equals(transaction.getRecepient())){
				receiver = i;
				break;
			}
		}

		int audience = DecentralizedNetwork.transactionValidationTries;
		int [] exclude = {me,receiver};
		ArrayList<Integer> used = new ArrayList<Integer>();
		while(audience!=0){
			int ref =  CustomMath.randomIntExclude(0, getNetwork().getNodes().size(), exclude);
			boolean alreadyExists = used.contains(ref);
			//System.out.println(alreadyExists);
			if(!alreadyExists){
				//add value
				getNetwork().getNodes().get(ref).getUnvalidatedTransactionBuffer().add(transaction);
				System.out.printf("Node %s chose Node %s to Validate \n",getPublic_address().substring(0, 6),
						getNetwork().getNodes().get(ref).getPublic_address().substring(0, 6));
				used.add(ref);
				audience--;
		
			}
		}



	}
	public void broadcastConfirmedTransaction(){

	}
	public void formBlockFromBuffer(){

	}
	public void binFaultyBlock(){

	}
	public void binFaultyTransaction(){

	}
	public void signalTransactionFlush(){

	}

	public String getPublic_address() {
		return public_address;
	}
	public void setPublic_address(String public_address) {
		this.public_address = public_address;
	}
	private String getPrivate_address() {
		return private_address;
	}
	private void setPrivate_address(String private_address) {
		this.private_address = private_address;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public ArrayList<Transaction> getTransactionHistory() {
		return transactionHistory;
	}
	public void setTransactionHistory(ArrayList<Transaction> transactionHistory) {
		this.transactionHistory = transactionHistory;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getNetworkID() {
		return networkID;
	}
	public void setNetworkID(String networkID) {
		this.networkID = networkID;
	}
	public double getxCoor() {
		return xCoor;
	}
	public void setxCoor(double xCoor) {
		this.xCoor = xCoor;
	}
	public double getyCoor() {
		return yCoor;
	}
	public void setyCoor(double yCoor) {
		this.yCoor = yCoor;
	}
	public Node getNearest_node() {
		return nearest_node;
	}
	public void setNearest_node(Node nearest_node) {
		this.nearest_node = nearest_node;
	}
	public ArrayList<Transaction> getValidated() {
		return validated;
	}
	public void setValidated(ArrayList<Transaction> validated) {
		this.validated = validated;
	}
	public ArrayList<Block> getValidatedBlock() {
		return validatedBlock;
	}
	public void setValidatedBlock(ArrayList<Block> validatedBlock) {
		this.validatedBlock = validatedBlock;
	}

	public DecentralizedNetwork getNetwork() {
		return network;
	}
	public void setNetwork(DecentralizedNetwork network) {
		this.network = network;
	}
	public ArrayList<Transaction> getUnvalidatedTransactionBuffer() {
		return unvalidatedTransactionBuffer;
	}
	public void setUnvalidatedTransactionBuffer(
			ArrayList<Transaction> unvalidatedTransactionBuffer) {
		this.unvalidatedTransactionBuffer = unvalidatedTransactionBuffer;
	}
	public ArrayList<Transaction> getValidatedTransactionBuffer() {
		return validatedTransactionBuffer;
	}
	public void setValidatedTransactionBuffer(
			ArrayList<Transaction> validatedTransactionBuffer) {
		this.validatedTransactionBuffer = validatedTransactionBuffer;
	}
	public ArrayList<Block> getUnvalidatedBlockBuffer() {
		return unvalidatedBlockBuffer;
	}
	public void setUnvalidatedBlockBuffer(ArrayList<Block> unvalidatedBlockBuffer) {
		this.unvalidatedBlockBuffer = unvalidatedBlockBuffer;
	}
	public ArrayList<Block> getBlockVotingBuffer() {
		return blockVotingBuffer;
	}
	public void setBlockVotingBuffer(ArrayList<Block> blockVotingBuffer) {
		this.blockVotingBuffer = blockVotingBuffer;
	}

}
