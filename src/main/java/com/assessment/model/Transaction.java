package com.assessment.model;

/**
 * 
 * @author Aman
 *
 *A POJO for transactions
 */

public class Transaction {

	double amount;

	long timestamp;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}