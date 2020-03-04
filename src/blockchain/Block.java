package blockchain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import transaction.MerkleTree;
import transaction.Transaction;
import cryptography.*;


public class Block {

	public static int diffculty = 5; // From 1 to 420k

	private int id;	
	private String hash;
	private String previous_hash;
	private int nonce;
	private long timestamp;
	private ArrayList<Transaction>  transaction;
	private String merkle_root;

	//Constructor for genisis block
	public Block(){
		try {
			this.id=0;
			this.hash = SHA256.toHexString(SHA256.getSHA("Genisis"));
			this.nonce=0;
			this.previous_hash = "0";
			Date d = new Date();
			this.timestamp = d.getTime();
			this.setTransaction(new ArrayList<Transaction>());;
			this.merkle_root= "";

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	//Constructor for i+1 Blocks
	public Block(int id, String previous_hash, int nonce,ArrayList<Transaction> transaction){
		//TODO Make sure that all transactions were confirmed
		this.setTransaction(transaction);
		
		//TODO Discard block if the transactions were not confirmed
		this.id=id;
		this.previous_hash= previous_hash; // to be amended later
		this.nonce=nonce;
		Date d = new Date();
		this.timestamp = d.getTime();



		MerkleTree merkle = new MerkleTree(transaction);
		this.merkle_root = merkle.getMerkle_root();

		this.hash= SHA256.hashValue(""+getNonce()+getTimestamp()+getMerkle_root());
		//this.proofOfWork();

		//this.mineBlock(diffculty);
		//proof of work goes here
		this.proofOfWork('1');
	}

	//Class Getters and Setters
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
	public String getMerkle_root() {
		return merkle_root;
	}
	public void setMerkle_root(String merkle_root) {
		this.merkle_root = merkle_root;
	}
	public void displayWrite() throws Exception{
		BufferedWriter myWriter = new BufferedWriter(new FileWriter("blockchain.txt",true));
		myWriter.append("**********START OF BLOCK**********\n");
		myWriter.append("Block ID: "+getId()+"\n");
		myWriter.append("Block Hash: "+getHash()+"\n");
		myWriter.append("Previous Block Hash: "+getPrevious_hash()+"\n");
		myWriter.append("Nonce: "+getNonce()+"\n");
		myWriter.append("Timestamp: "+getTimestamp()+"\n");
		myWriter.append("Merkle Root: "+getMerkle_root()+"\n");
		myWriter.append("Transactions: "+"\n");
		myWriter.close();
		for(int i=0; i<transaction.size();i++){
			transaction.get(i).displayWrite();
		}
		BufferedWriter myWriter2 = new BufferedWriter(new FileWriter("blockchain.txt",true));
		myWriter2.append("**********END OF BLOCK**********\n \n");
		myWriter2.close();
	}
	public void display(){
		System.out.println("Block ID: "+getId());
		System.out.println("Block Hash: "+getHash());
		System.out.println("Previous Block Hash: "+getPrevious_hash());
		System.out.println("Nonce: "+getNonce());
		System.out.println("Timestamp: "+getTimestamp());
		System.out.println("Merkle Root: "+getMerkle_root());
		System.out.println("Transactions: ");
		for(int i=0; i<transaction.size();i++){
			transaction.get(i).display();
			System.out.println("--------------------------------------------------------------------------");
		}
	}
	private static String numberToChars(int nonce, char ltr){
		String message ="";
		if( nonce<=0)
			return "Erorr";
		while(nonce!=0){
			message=message+ltr;
			nonce--;
		}
		return message;
	}
	private String proofOfWork(char leadingChar) {
		 
        int leadingzeros= getNonce();
        String leading = numberToChars(leadingzeros,leadingChar);
        System.out.println(leading);
        String message=""+getNonce()+getTimestamp()+getMerkle_root();
        String newMsg = "";
        String newHash = "";
        int nonceCounter = 1;
        boolean flag = false;
        while(!flag){
        	newMsg = message+nonceCounter;
        	newHash = SHA256.hashValue(newMsg);
        	flag = newHash.substring(0, leadingzeros).equals(leading);
        	nonceCounter++;
        	System.out.println(newMsg+" Has Produced "+newHash);
        	

        	
        }
        System.out.printf("Succcessfuy Mined ! Hash is %s",newHash);
        return newHash;
 
    }
	public static void main(String[] args){


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
		Block block = new Block(1, "prev hash", 1,transactions);
		block.display();
		//block.proofOfWork();
		System.out.println("");

	}
}
