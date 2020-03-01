package blockchain;
import java.util.*;

public class BlockChain {

	public LinkedList<Block> chain;
	private Block genisis;
	private int current_block =0;

	public BlockChain(){
		chain= new LinkedList<Block>();
		genisis = new Block();
		addBlock(genisis);
		current_block++;
	}
	public void addBlock(Block block){
		this.chain.add(block);
		current_block++;

	}

}
