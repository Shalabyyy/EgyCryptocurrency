package network;

import java.util.ArrayList;

import cryptography.CustomMath;
import transaction.Transaction;

@SuppressWarnings("unused")
public class DecentralizedNetwork {

	private ArrayList<Node>  nodes;
	public int number_of_nodes =0;

	public static double rateOfCommision = 0.01;
	public static double deployedAmmount =100;
	public static double reserveAmmount = 120000;
	public static int transactionValidationTries = 2; //to be modified later
	

	public DecentralizedNetwork(){
		setNodes(new ArrayList<Node>());
	}
	public void  addNode(Node n){
		//Make Sure That The Node Does not exist
		if(getNodes().contains(n)){
			System.out.println("Node Already Exists");
			return;
		}
		boolean add = getNodes().add(n);
		
	}
	private void removeNode(Node node){
		int ref = getNodes().indexOf(node);
		nodes.remove(ref);
	}
	public void getNetworkDetails(){
		System.out.println("Number of Nodes");
		System.out.println("Deployed Ammount");
		System.out.println("Ammount Left");
	}
	public double getNodeDistance(Node n1, Node n2){
		if(nodes.contains(n1)&& nodes.contains(n1)){
			return CustomMath.calculateVector(n1.getxCoor(), n1.getyCoor(), n2.getxCoor(), n2.getyCoor());
		}
		else return 0;
		//Check that both nodes exists in network first
	}
	public void updateBalances(Transaction transaction){
		if(transaction.isConfirmed()){
			Node s =  null;
			Node r = null;
			boolean foundS = false;
			boolean foundR = false;
			for(int i=0; i<getNodes().size();i++){
				if(getNodes().get(i).getPublic_address().equals(transaction.getSender())){
					s = getNodes().get(i);
					foundS = true;
				}
				if(getNodes().get(i).getPublic_address().equals(transaction.getRecepient())){
					r = getNodes().get(i);
					foundR=true;
				}
				if(foundS && foundR)
					break;

			}
			if(!(foundS && foundR)){
				System.out.println("Failed to Update Balances");
				return;
			}
			s.updateLedger(transaction);
			r.updateLedger(transaction);
		}
		else{
			System.out.println("The Transaction was not confirmed before");
		}
	}
	public void gossipProtocol(Node root,Object message){
		if(nodes.size()==1)
			return;
		boolean sentMessage[] = CustomMath.generateFalseArray(nodes.size());
		sentMessage[getNodes().indexOf(root)]= true;
		gossipHelper(root,message,sentMessage);
	}
	public void gossipHelper(Node root,Object message,boolean sentMessage []){
		if(CustomMath.isAllTrue(sentMessage)){
			System.out.println("Gossip Protocol Succussfull");
			return;
		}
		Node nearest =root.locateNearestNode();
		int index = getNodes().indexOf(nearest);
		if(!sentMessage[index]){
			//TODO Send Message Here
			sentMessage[index] = true;
			gossipHelper(root, message, sentMessage);
		}
		else{
			//Node got the message before so it can forward it again
			//TODO However this might lead to an infinte loop, a break case is needed
			gossipHelper(nearest.locateNearestNode(),message,sentMessage);
		}
	}

	//TODO
	public void forgeBlock(){
		
	}
	//TODO
	private void pureDeposit(){

	}
	//TODO
	public void AwardMiner(){

	}
	public static void TestNetwork(){
		System.out.println("Simulation Activated");
		System.out.println();
		
		DecentralizedNetwork network = new DecentralizedNetwork();
		
		Node node1 = new Node(0,0,"",network);
		Node node2 = new Node(2,3,"",network);
		Node node3 = new Node(3,0,"",network);
		Node node4 = new Node(3,-4,"",network);
		
		//TODO Make Sure That the Node is Safe and Clear to go
		//Assume all nodes are safe
		
		network.addNode(node1);
		network.addNode(node2);
		network.addNode(node3);
		network.addNode(node4);
		
		System.out.println();
		//Adding a Duplicate Node Test
		network.addNode(node1);
		System.out.println();
		//Add Funds to Simulate Transcations
		System.out.println("Simulating Transactions \n"); 
		node1.setBalance(10);
		node2.setBalance(3);
		node3.setBalance(3);
		node4.setBalance(7);
		
		//Simulate Transactions
		Transaction t13 = node1.sendFunds(4, node3.getPublic_address());	//Should be okay
		Transaction t24 = node2.sendFunds(2.5, node4.getPublic_address()); //Should be okay
		Transaction t43 = node4.sendFunds(2, node3.getPublic_address());	//should be okay
		Transaction t12 = node1.sendFunds(10, node2.getPublic_address());	//should be rejected later on
		
		Transaction t14 = node1.sendFunds(120, node4.getPublic_address()); //Should be Rejected
		
		System.out.println("Simulating Transaction Validation \n");
		//TODO This part will be hard coded now, will be changed later
		
		//At the time I was writng this code, the Validation Limit was 2
		//TODO set ValidationLimit Based on (n-2)/k 
		//TODO do not allow a node to validate the same transaction twice
		
		node1.validateTransaction(t24); //Should get Validated fine 1/2
		node1.validateTransaction(t43); //Should get Validated fine 1/2
		node1.validateTransaction(t13); //Should NOT get Validated 0/2
		node3.validateTransaction(t12); //Should get Validated fine 1/2
		node3.validateTransaction(t24); //Should be Validated 2/2 and Balance Should be Updated
		
		//TODO so far the System is not Realtime
		System.out.println("\nChecking that Balances Were Updated After t24");
		System.out.println("Previous Balance was: " +3.0+" The Balance Now is: "+node2.getBalance());
		System.out.println("Previous Balance was: " +7.0+" The Balance Now is: "+node4.getBalance()+"\n");	
		
		System.out.println("Checking for Double Spending Betweeen t13 and t12 \n");
		node2.validateTransaction(t13); //Should get Validated fine 1/2
		node4.validateTransaction(t13); //Should get Validated fine 2/2
		node4.validateTransaction(t12); //Should NOT get Validated fine, FAIL
		
		System.out.println("Validate Remaining Transactions \n");
		node2.validateTransaction(t43); //Should be Validated fine 2/2 
		
	
	}
	public static void main(String [] args){
		TestNetwork();
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
}
