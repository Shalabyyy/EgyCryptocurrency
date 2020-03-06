package blockchain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

import network.DecentralizedNetwork;
import transaction.Transaction;

@SuppressWarnings("unused")
public class BlockChain {
	
	private DecentralizedNetwork network;
	public LinkedList<Block> chain;
	private Block genisis;
	private int current_block =0;

	//Creating a Blockchain from scathch with a Genisis Block
	public BlockChain(DecentralizedNetwork network){
		chain= new LinkedList<Block>();
		genisis = new Block();
		chain.add(genisis);
		this.setNetwork(network);
		setCurrent_block(getCurrent_block() + 1);
		try {
			writeBlockchainFile(genisis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//Testing Constructor
	private BlockChain(){
		chain= new LinkedList<Block>();
		genisis = new Block();
		chain.add(genisis);
		setCurrent_block(getCurrent_block() + 1);
		try {
			writeBlockchainFile(genisis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addBlock(Block block){
		if(block.isConfirmed()){
			String prev = chain.get(chain.size()-1).getHash();
			block.setPrevious_hash(prev);
			this.chain.add(block);
			setCurrent_block(getCurrent_block() + 1);
			try {
				writeBlockchainFile(block);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(block.getHash().substring(0, 6)+" Was Added to The BlockChain");
		}
		else{
			System.out.println(block.getHash().substring(0, 6)+" is Invalid");
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
		
	}

	public int getCurrent_block() {
		return current_block;
	}

	public void setCurrent_block(int current_block) {
		this.current_block = current_block;
	}

	public DecentralizedNetwork getNetwork() {
		return network;
	}

	public void setNetwork(DecentralizedNetwork network) {
		this.network = network;
	}
}
