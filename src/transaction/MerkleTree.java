package transaction;

import java.util.*;

import cryptography.SHA256;

public class MerkleTree {
	private List<Transaction> transactions;
	private String merkle_root;

	public MerkleTree(List<Transaction> transactions){
		this.transactions = transactions;
		List<Transaction> updtransactions=formTree(this.transactions);
		this.merkle_root = updtransactions.get(0).getHash();
		//System.out.println("The Merkle Root is "+merkle_root);
	}
	public String getMerkle_root() {
		return merkle_root;
	}
	public void setMerkle_root(String merkle_root) {
		this.merkle_root = merkle_root;
	}
	private List<Transaction> formTree(List<Transaction> transactions){
		if(transactions==null){
			System.out.println("Error Forming Merkle Tree");
			return null;
		}
		if(transactions.size()==0){
			System.out.println("Cant Form Merkle Tree from Empty List");
			return null;
		}
		//Base Case If the List only has One Transaction
		if(transactions.size()==1)
			return transactions;
		List<Transaction> updated_list = new ArrayList<Transaction>();
		
		//Combine the Both elememts together tp form an updated list
		for(int i=0; i<transactions.size()-1;i+=2){
			String hash = SHA256.mergeHash(transactions.get(i).getHash(), transactions.get(i+1).getHash());
			updated_list.add(new Transaction(hash));
		}
		
		//if the length is odd hash the last value with itself
		if(transactions.size()%2==1){
			int ref = transactions.size()-1;
			String hash2 = SHA256.mergeHash(transactions.get(ref).getHash(), transactions.get(ref).getHash());
			updated_list.add(new Transaction(hash2));
		}
		return formTree(updated_list); 
	}
	
	public static void main (String args[]){
		List<Transaction> transactions = new ArrayList<Transaction>();
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
		transactions.add(t1);
		transactions.add(t2);
		transactions.add(t3);
		transactions.add(t4);

		@SuppressWarnings("unused")
		MerkleTree merkletree = new MerkleTree(transactions);



	}
	
}	
