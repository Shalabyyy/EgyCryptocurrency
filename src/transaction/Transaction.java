package transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;

import network.DecentralizedNetwork;
import cryptography.*;
public class Transaction {

	private int timesValidated =0;
	
	//Asumtion Made here to check the number of validations needed
	private int validationsNeeded =DecentralizedNetwork.transactionValidationTries;
	
	private String sender;
	private String recepient;
	private double amount;
	private long timestamp;
	private String hash;

	private boolean confirmed= false;

	public Transaction(String hash){
		setHash(hash);
		setSender("Combination");
		setRecepient("Combination");
		setAmount(0);
		Date d = new Date();
		setTimestamp(d.getTime());
	}
	public Transaction(String sender, String recepient, double amount){	
		if(!(CustomMath.isAddressValid(sender) && CustomMath.isAddressValid(recepient))){
			System.out.println("Error Can Not Create Transaction");
			return;
		}
		//Add Type Validations later on
		this.setSender(sender);
		this.setRecepient(recepient);
		this.setAmount(amount);
		Date d = new Date();
		this.setTimestamp(d.getTime());
		String combination = sender+recepient+amount+timestamp;
		this.setHash(SHA256.hashValue(combination)); //Later to be Hashed Based On Criteria
	}

	public void terminate(){
		setTimesValidated(-Integer.MAX_VALUE);
		setAmount(0);
		setConfirmed(false);
	}
	public void confirmTransaction(){
		//TODO Check Accounts and funds if they exits
		//TODO Make sure that the transaction was not tampered
		//TODO Forward the transactions to another node
	}
	public void displayWrite() throws Exception{
		BufferedWriter myWriter = new BufferedWriter(new FileWriter("blockchain.txt",true));
		myWriter.append("Transaction Number: "+getHash()+"\n");
		myWriter.append("Sender: "+getSender()+"\n");
		myWriter.append("Receipeint: "+getRecepient()+"\n");
		myWriter.append("Ammount: "+getAmount()+"\n");
		myWriter.append("Timestamp: " + getTimestamp()+"\n");
		myWriter.close();
	}
	public void display(){
		System.out.println("Transaction Number: "+getHash());
		System.out.println("Sender: "+getSender());
		System.out.println("Receipeint: "+getRecepient());
		System.out.println("Ammount: "+getAmount());
		System.out.println("Timestamp: " + getTimestamp());
	}

	public static void main(String [] args){
		@SuppressWarnings("unused")
		Transaction test = new Transaction(
				"f4a4ce5fa6340a35aa5db0b4b6d31a6fbaa6052356460dbb0537657d803f5be2",
				"e9058ab198f6908f702111b0c0fb5b36f99d00554521886c40e2891b349dc7a1",
				15.0
				);
		Transaction test2 = new Transaction("e9058ab198f6908f702111b0c0fb5b36f99d00554521886c40e2891b349dc7a1");
		test2.display();	
	}
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecepient() {
		return recepient;
	}

	public void setRecepient(String recepient) {
		this.recepient = recepient;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public boolean isConfirmed() {
		return confirmed;
	}
	private void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}
	public int getTimesValidated() {
		return timesValidated;
	}
	public void setTimesValidated(int timesValidated) {
		this.timesValidated = timesValidated;
		if(timesValidated==getValidationsNeeded()){
			setConfirmed(true);
		}
	}
	public int getValidationsNeeded() {
		return validationsNeeded;
	}
	public void setValidationsNeeded(int validationsNeeded) {
		this.validationsNeeded = validationsNeeded;
	}



}
