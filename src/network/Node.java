package network;

import java.util.ArrayList;

import cryptography.SHA256;
import blockchain.Block;
import transaction.Transaction;

@SuppressWarnings("unused")
public class Node{
	//User Settings
	private String networkID;
	private String public_address;
	private String private_address;
	private double balance;
	private ArrayList<Transaction>  transactionHistory;
	private Node nearest_node;

	//Network Simulation Settings
	private double xCoor;
	private double yCoor;
	private String role;

	public Node(double xCoor, double yCoor, String role){
		//super();
		this.setNetworkID("To Be Ammended Later");
		this.setPrivate_address(SHA256.generatePrivateAddress());
		this.setPublic_address(SHA256.hashValue(this.getPrivate_address()));
		this.setBalance(0);
		this.transactionHistory= new ArrayList<Transaction>();
		this.setxCoor(xCoor);
		this.setyCoor(yCoor);
		this.setRole(role);
		//TODO get the nearest Node from super class or Network
	}
	public boolean validateTransaction(Transaction toBeValidated, DecentralizedNetwork network){
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
		}
		
		//If all of the conditions are met, The Transaction is Validated
		if(foundSender && foundRecipient && suffcientAmmount){
			toBeValidated.setConfirmed(true);
			return true;
		}
		else {
			//TODO Decrement the number of Validations
			return false;
		}
		
	}
	public void validateBlock(Block toBeValidated){

	}
	public void sendFunds(double amount, String address){

	}
	public void locateNearestNode(DecentralizedNetwork network){

	}
	public void updateLedger(){

	}
	public void getLedgerFrom(Node otherNode){

	}
	



	public String getPublic_address() {
		return public_address;
	}
	public void setPublic_address(String public_address) {
		this.public_address = public_address;
	}
	public String getPrivate_address() {
		return private_address;
	}
	public void setPrivate_address(String private_address) {
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
