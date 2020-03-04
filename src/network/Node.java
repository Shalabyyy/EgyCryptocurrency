package network;

import java.util.ArrayList;

import blockchain.Block;
import transaction.Transaction;

public class Node {
	//User Settings
	private String networkID;
	private String public_address;
	private String private_address;
	private double balance;
	private ArrayList<Transaction>  transactionHistory;

	//Network Simulation Settings
	public double xCoor;
	public double yCoor;
	private String role;

	public Node(){

	}
	public void validateTransaction(Transaction toBeValidated){

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
}
