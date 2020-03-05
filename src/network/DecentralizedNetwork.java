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
	public static int transactionValidationTries= 5; //to be modified later

	public DecentralizedNetwork(){
		setNodes(new ArrayList<Node>());
	}
	public void  addNode(Node n){
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
	public void forgeBlock(){
		
	}

	private void pureDeposit(){

	}
	public void AwardMiner(){

	}
	public static void TestNetwork(){
		System.out.println("Simulation Activated");
		DecentralizedNetwork network = new DecentralizedNetwork();
		
	}
	public static void main(String [] args){

	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
}
