package blockchain;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

import transaction.Transaction;
import cryptography.*;


public class Block {

	private int id;	
	private String hash;
	private String previous_hash;
	private int nonce;
	private long timestamp;
	private ArrayList<Transaction>  transaction;

	public Block(){
		//Constructor for genisis block
		try {
			this.id=0;
			this.hash = SHA256.toHexString(SHA256.getSHA("Genisis"));
			this.nonce=0;
			this.previous_hash = "";
			Date d = new Date();
			this.timestamp = d.getTime();
			this.setTransaction(new ArrayList<Transaction>());;
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	public Block(int id, String hash, String previous_hash, int nonce,ArrayList<Transaction> transaction){
		this.id=id;
		this.hash=hash;
		this.previous_hash= previous_hash;
		this.nonce=nonce;
		Date d = new Date();
		this.timestamp = d.getTime();
		this.setTransaction(transaction);

	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHash() {
		return hash;
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public String getPrevious_hash() {
		return previous_hash;
	}
	public void setPrevious_hash(String previous_hash) {
		this.previous_hash = previous_hash;
	}
	public int getNonce() {
		return nonce;
	}
	public void setNonce(int nonce) {
		this.nonce = nonce;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public ArrayList<Transaction> getTransaction() {
		return transaction;
	}
	public void setTransaction(ArrayList<Transaction> transaction) {
		this.transaction = transaction;
	}
	
	public void display(){
		System.out.println("Block ID: "+getId());
		System.out.println("Block Hash: "+getHash());
		System.out.println("Previous Block Hash: "+getPrevious_hash());
		System.out.println("Nonce: "+getNonce());
		System.out.println("Timestamp: "+getTimestamp());
		System.out.println("Transactions: ");
		for(int i=0; i<transaction.size();i++){
			transaction.get(i).display();
			System.out.println("--------------------------------------------------------------------------");
		}
	}
	public static void main(String[] args){
		System.out.println("Genisis block");
		Block genisis = new Block();
		genisis.display();
		System.out.println("");
		System.out.println("");
		System.out.println("");
		
		Transaction t1 = new Transaction(
				"f4a4ce5fa6340a35aa5db0b4b6d31a6fbaa6052356460dbb0537657d803f5be2",
				"e9058ab198f6908f702111b0c0fb5b36f99d00554521886c40e2891b349dc7a1",
				15.0
				);
		Transaction t2 = new Transaction(
				"f488ce5fa6340a35aa5db0b4b6d31a6fbaa6052356460dbb0537657d803f5be2",
				"e9058ab198f6908f702111abc3b5b36f99d00554521886c40e2891b349dc7a1",
				3.0
				);
		Transaction t3 = new Transaction(
				"f488ce5fa6340a35aa5db0b4b6d31a6fbaa6052356460dbb0537657d803f5be2",
				"e9058ab198f6908f702111abc3b5b36f99d00554521886c40e2891b349dc7a1",
				6.0
				);
		Transaction t4 = new Transaction(
				"a7yyce5fa6340a35aa5db0b4b6d31a6fbaa6052356460dbb0537657d803f5be2",
				"x9gh8ab198f6908f702111abc3b5b36f99d00554521886c40e2891b349dc7a1",
				5.5
				);
		ArrayList<Transaction> transactions =  new ArrayList<Transaction>();
		transactions.add(t1);
		transactions.add(t2);
		transactions.add(t3);
		transactions.add(t4);
		Block block = new Block(1, "my hash", "prev hash", 1024,transactions);
		block.display();
		
		System.out.println("");
		
	}
}
