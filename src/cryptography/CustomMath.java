package cryptography;
public class CustomMath {

	public static double calculateVector(double x1,double y1, double x2, double y2){
		double xVector2 = Math.pow(x2-x1, 2);
		double yVector2 = Math.pow(y2-y1, 2);
		return Math.sqrt(xVector2+yVector2);
	}
	public static void main(String [] args){
		System.out.println(calculateVector(5, 5, 7, 7));
	}
}
