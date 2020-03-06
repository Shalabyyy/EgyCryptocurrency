package cryptography;

import java.util.Arrays;
import java.util.Random;

public class CustomMath {

	public static double calculateVector(double x1,double y1, double x2, double y2){
		double xVector2 = Math.pow(x2-x1, 2);
		double yVector2 = Math.pow(y2-y1, 2);
		return Math.sqrt(xVector2+yVector2);
	}
	public static boolean isAddressValid(String adr){
		return (adr.length()==64);
	}
	public static String numberToChars(int nonce, char ltr){
		String message ="";
		if( nonce<=0)
			return "Erorr";
		while(nonce!=0){
			message=message+ltr;
			nonce--;
		}
		return message;
	}
	public static boolean [] generateFalseArray(int size){
		boolean sentMessage[] = new boolean[size];

		for(int i=0; i<sentMessage.length;i++)
			sentMessage[i]=false;
		return sentMessage;
	}
	public static boolean isAllTrue(boolean [] array){
		boolean checker = true;
		for(int i=0; i<array.length;i++)
			checker = array[i] && checker;
		return checker;
	}
	public static int randomIntExclude(int start, int end, int... excludes){
		//Sort List exludes first 
		Arrays.sort(excludes);
	    int rangeLength = end - start - excludes.length;
	    int randomInt = new Random().nextInt(rangeLength) + start;

	    for(int i = 0; i < excludes.length; i++) {
	        if(excludes[i] > randomInt) {
	            return randomInt;
	        }

	        randomInt++;
	    }

	    return randomInt;
	}
	public static void main(String [] args){
		System.out.println(calculateVector(5, 5, 7, 7));
		System.out.println(isAddressValid("ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb"));	
		System.out.println();
		int []x={6,3,4};
		
		for(int i=0;i<10;i++)
			System.out.println(randomIntExclude(1,10,x));
	}
		
}
