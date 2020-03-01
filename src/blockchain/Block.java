package blockchain;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

import transaction.Transcation;
import cryptography.*;


public class Block {

	private int id;	
	private String hash;
	private String previous_hash;
	private int nonce;
	private long timestamp;
	private Transcation transaction;

	public Block(){
		//Constructor for genisis block
		try {
			this.id=0;
			this.hash = SHA256.toHexString(SHA256.getSHA("Genisis"));
			this.nonce=0;
			this.previous_hash = "";
			Date d = new Date();
			this.timestamp = d.getTime();
			this.transaction= null;
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	public Block(int id, String hash, String previous_hash, int nonce,Transcation transaction){
		this.id=id;
		this.hash=hash;
		this.previous_hash= previous_hash;
		this.nonce=nonce;
		this.transaction = transaction;

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
}
