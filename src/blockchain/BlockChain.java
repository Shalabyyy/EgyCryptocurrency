package blockchain;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

import transaction.Transaction;

public class BlockChain {

	public LinkedList<Block> chain;
	private Block genisis;
	private int current_block =0;

	//Creating a Blockchain from scathch with a Genisis Block
	public BlockChain(){
		chain= new LinkedList<Block>();
		genisis = new Block();
		chain.add(genisis);
		current_block++;
		try {
			writeBlockchainFile(genisis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addBlock(Block block){
		//Add Previous Block Address
		String prev = chain.get(chain.size()-1).getHash();
		block.setPrevious_hash(prev);
		this.chain.add(block);
		current_block++;
		
		try {
			writeBlockchainFile(block);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void writeBlockchainFile(Block block) throws Exception{
	    BufferedWriter writer = new BufferedWriter(new FileWriter("blockchain.txt",true));
	    block.displayWrite();
		writer.close();
		System.out.printf("Added Block %s Log. \n",block.getHash());

	}
	public void display(){
		for(int i=0; i< chain.size();i++){
			System.out.println("Block Number "+i);
			chain.get(i).display();
		}
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
		Block block = new Block(1, "prev hash", 1024,transactions);

		Transaction t5 = new Transaction(
				"a4a4ce5fa6340a35aa5db0b4b6d31a6fbaa6052356460dbb0537657d803f5be2",
				"b9058ab198f6908f702111b0c0fb5b36f99d00554521886c40e2891b349dc7a1",
				11.2
				);
		Transaction t6 = new Transaction(
				"c488ce5fa6340a35aa5db0b4b6d31a6fbaa6052356460dbb0537657d803f5be2",
				"d9058ab198f69090102111abc3b5b36f99d00554521886c40e2891b349dc7a1",
				0.6
				);
		Transaction t7 = new Transaction(
				"e488ce5fa6340a35aa5db0b4b6d31a6fbaa6052356460dbb0537657d803f5be2",
				"k9058ab198f6908f702111abc3b5b36f99d00554521886c40e2891b349dc7a1",
				22.0
				);
		Transaction t8 = new Transaction(
				"z7yyce5fa6340a35aa5db0b4b6d31a6fbaa6052356460dbb0537657d803f5be2",
				"asgh8ab198f6908f702111abc3b5b36f99d00554521886c40e2891b349dc7a1",
				7.0
				);
		ArrayList<Transaction> transactions1 =  new ArrayList<Transaction>();
		transactions1.add(t5);
		transactions1.add(t6);
		transactions1.add(t7);
		transactions1.add(t8);
		Block block1 = new Block(2, "prev hash", 1024,transactions1);

		BlockChain chain = new BlockChain();
		chain.addBlock(block);
		chain.addBlock(block1);
		chain.display();
		System.out.println("");
		
	}
}
