package network;

import java.util.ArrayList;

import transaction.Transaction;

public class DecentralizedNetwork {

	private ArrayList<Node>  nodes;
	public int number_of_nodes =0;
	public static double deployedAmmount =100;
	public static double reserveAmmount = 120000;

	public DecentralizedNetwork(){
		nodes= new ArrayList<Node>();
	}
	public void  addNode(Node n){
		@SuppressWarnings("unused")
		boolean add = nodes.add(n);

	}
	public void getNetworkDetails(){
		System.out.println("Number of Nodes");
		System.out.println("Deployed Ammount");
		System.out.println("Ammount Left");
	}
	public void getNodeDistance(Node n1, Node n2){
		//Check that both nodes exists in network first
	}
	public void updateBalances(Transaction transaction){

	}
	public void gossipProtocol(Object message){

	}
	public void forgeBlock(){
		
	}

	public static void TestNetwork(){

	}
	public static void main(String [] args){

	}
}
