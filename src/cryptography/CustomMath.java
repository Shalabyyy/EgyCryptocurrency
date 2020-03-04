package cryptography;
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
	public static void main(String [] args){
		System.out.println(calculateVector(5, 5, 7, 7));
		System.out.println(isAddressValid("ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb"));	}

}
