package com.openbanking.squadx.smarttransactions.model;

public class TransactionsRequest {
	private String transactionFromDateTime;
	private String transactionToDateTime;
	
	public String getTransactionFromDateTime() {
		return transactionFromDateTime;
	}
	public void setTransactionFromDateTime(String transactionFromDateTime) {
		this.transactionFromDateTime = transactionFromDateTime;
	}
	public String getTransactionToDateTime() {
		return transactionToDateTime;
	}
	public void setTransactionToDateTime(String transactionToDateTime) {
		this.transactionToDateTime = transactionToDateTime;
	}
}
