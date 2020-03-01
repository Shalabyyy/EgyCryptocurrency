package transaction;

import java.util.*;

import cryptography.SHA256;

public class MerkleTree {
	private List<Transaction> transactions;
	
	public MerkleTree(List<Transaction> transactions){
		this.transactions = transactions;
		formTree();
	}
	public List<Transaction> formTree(){
		if(transactions.size()<=1)
			return transactions;
		List<String> updated_list = new ArrayList();
		for(int i=0; i<transactions.size()-1;i+=2){
			updated_list.add(SHA256.mergeHash(transactions.get(i).getHash(), transactions.get(i+1).getHash()));
			
		}
		return null; //to clear error
	}
}	
