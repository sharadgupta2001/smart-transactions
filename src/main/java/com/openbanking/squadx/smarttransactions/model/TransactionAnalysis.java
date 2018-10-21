package com.openbanking.squadx.smarttransactions.model;

import java.util.Map;

public class TransactionAnalysis {

	public Map<String, AmountDetails> debitTransactionDetails;
	public Map<String, AmountDetails> creditTransactionDetails;
	public Double totalDebit;
	public Double totalCredit;
	public Double totalSaving;
	
	public Map<String, AmountDetails> getDebitTransactionDetails() {
		return debitTransactionDetails;
	}
	public void setDebitTransactionDetails(Map<String, AmountDetails> debitTransactionDetails) {
		this.debitTransactionDetails = debitTransactionDetails;
	}
	public Map<String, AmountDetails> getCreditTransactionDetails() {
		return creditTransactionDetails;
	}
	public void setCreditTransactionDetails(Map<String, AmountDetails> creditTransactionDetails) {
		this.creditTransactionDetails = creditTransactionDetails;
	}
	public Double getTotalDebit() {
		return totalDebit;
	}
	public void setTotalDebit(Double totalDebit) {
		this.totalDebit = totalDebit;
	}
	public Double getTotalCredit() {
		return totalCredit;
	}
	public void setTotalCredit(Double totalCredit) {
		this.totalCredit = totalCredit;
	}
	public Double getTotalSaving() {
		return totalSaving;
	}
	public void setTotalSaving(Double totalSaving) {
		this.totalSaving = totalSaving;
	}
	
}
