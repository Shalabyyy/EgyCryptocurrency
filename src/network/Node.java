package network;

import java.util.ArrayList;

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


	public Node(double xCoor, double yCoor, String role,DecentralizedNetwork network ){
		//super();
		this.setNetworkID("To Be Ammended Later");
		this.setPrivate_address(SHA256.generatePrivateAddress());
		this.setPublic_address(SHA256.hashValue(this.getPrivate_address()));
		this.setBalance(0);
		this.transactionHistory= new ArrayList<Transaction>();
		this.setxCoor(xCoor);
		this.setyCoor(yCoor);
		this.setRole(role);
		this.network= network;

		System.out.println("Node "+getPublic_address()+" was Created");
		//TODO get the nearest Node from super class or Network
	}
	public boolean validateTransaction(Transaction toBeValidated){
		//Check that the node does not validate itself
		if(toBeValidated.getSender().equals(getPublic_address()) || 
				toBeValidated.getRecepient().equals(getPublic_address())){
			System.out.println(getPublic_address().substring(0,6)+" Can't Validate it's own Transaction");
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
			//If the Transaction is confirmed Update Accounts
			if(toBeValidated.isConfirmed()){
				System.out.println(toBeValidated.getHash().substring(0, 6)+" Finished Validation 100%");
				network.updateBalances(toBeValidated);
			}
			return true;
		}
		else {
			//TODO Decrement the number of Validations Failures
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
		//Make Sure no Tampers Were Made 
		ArrayList<Transaction> transactions = toBeValidated.getTransaction();
		boolean confirmedTransactions = false;
		boolean legitMerkle = SHA256.validateMerkle(toBeValidated.getMerkle_root(),transactions);
		if(!legitMerkle){
			System.out.println("Merkle Root Mismatch by Node "+getPublic_address().substring(0, 6));
			return false;
		}

		//Make sure that all transactions are verified
		for(int i=0; i<transactions.size();i++){
			if(!transactions.get(i).isConfirmed()){
				System.out.println("Not All Transactions were Verified by Node "+getPublic_address().substring(0, 6));
				return false;
			}
		}

		confirmedTransactions=true;
		toBeValidated.setTimesValidated(toBeValidated.getTimesValidated()+1);
		if(confirmedTransactions && legitMerkle){
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
			return new Transaction(getPublic_address(), address, amount);
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
			computation = CustomMath.calculateVector(getxCoor(), getyCoor(), n.getxCoor(), n.getyCoor());
			if(min>computation){
				min=computation;
				minNode=n.getPublic_address();	
				nearest=n;
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
}
