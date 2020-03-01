package transaction;

public class Transcation {
	
private String sender;
private String recepient;
private double amount;
private String hash;

public Transcation(String sender, String recepient, double amount){	
	//Add Type Validations later on
	this.setSender(sender);
	this.setRecepient(recepient);
	this.setAmount(amount);
	this.setHash(""); //Later to be Hashed Based On Criteria
}

public String getSender() {
	return sender;
}

public void setSender(String sender) {
	this.sender = sender;
}

public String getRecepient() {
	return recepient;
}

public void setRecepient(String recepient) {
	this.recepient = recepient;
}

public double getAmount() {
	return amount;
}

public void setAmount(double amount) {
	this.amount = amount;
}


public String getHash() {
	return hash;
}


public void setHash(String hash) {
	this.hash = hash;
}

}
