package cryptography;

import java.math.BigInteger; 
import java.nio.charset.StandardCharsets; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 
import java.util.List;
import java.util.Random;

import transaction.MerkleTree;
import transaction.Transaction;

// Java program to calculate SHA hash value 

public class SHA256 { 
	public static byte[] getSHA(String input) throws NoSuchAlgorithmException 
	{ 
		// Static getInstance method is called with hashing SHA 
		MessageDigest md = MessageDigest.getInstance("SHA-256"); 

		// digest() method called 
		// to calculate message digest of an input 
		// and return array of byte 
		return md.digest(input.getBytes(StandardCharsets.UTF_8)); 
	} 

	public static String toHexString(byte[] hash) 
	{ 
		// Convert byte array into signum representation 
		BigInteger number = new BigInteger(1, hash); 

		// Convert message digest into hex value 
		StringBuilder hexString = new StringBuilder(number.toString(16)); 

		// Pad with leading zeros 
		while (hexString.length() < 32) 
		{ 
			hexString.insert(0, '0'); 
		} 

		return hexString.toString(); 
	} 

	public static String hashValue(String val){
		try {
			return toHexString(getSHA(val));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "Error";
		}
	}

	public static String mergeHash(String hash1, String hash2){
		String mergedhash = hash1+hash2;
		try {
			return toHexString(getSHA(mergedhash));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "Error";
		}

	}

	public static boolean validateMerkle(String merkleroot, List<Transaction> transactions){
		MerkleTree merkle = new MerkleTree(transactions);
		if(merkleroot.equals(merkle.getMerkle_root())){
			return true;
		}
		else return false;

	}

	public static String generatePrivateAddress(){
		int leftLimit = 48; // letter '0'
		int rightLimit = 102; // letter 'z'
		int targetStringLength = 12;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) 
					(random.nextFloat() * (rightLimit - leftLimit + 1));
			if(randomLimitedInt>=58 && 96>=randomLimitedInt){
				i--;
			}
			else{
				buffer.append((char) randomLimitedInt);
			}

		}
		String generatedString = buffer.toString();
		return generatedString;
	}
	// Driver code 
	public static void main(String args[]) 
	{ 
		System.out.println(generatePrivateAddress());
	} 
} 
