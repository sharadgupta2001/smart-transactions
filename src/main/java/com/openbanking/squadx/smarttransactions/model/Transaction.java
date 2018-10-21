package com.openbanking.squadx.smarttransactions.model;

public class Transaction {

	public String AccountId;
	public String CreditDebitIndicator;
	public Amount Amount;
	public Balance Balance;
	public String TransactionInformation;
	public MerchantDetails MerchantDetails;
	public String BookingDateTime;
	
	public MerchantDetails getMerchantDetails() {
		return MerchantDetails;
	}
	public void setMerchantDetails(MerchantDetails merchantDetails) {
		MerchantDetails = merchantDetails;
	}
	
	public String getBookingDateTime() {
		return BookingDateTime;
	}
	public void setBookingDateTime(String bookingDateTime) {
		BookingDateTime = bookingDateTime;
	}
	
	public Balance getBalance() {
		return Balance;
	}
	public void setBalance(Balance balance) {
		Balance = balance;
	}
	public String getTransactionInformation() {
		return TransactionInformation;
	}
	public void setTransactionInformation(String transactionInformation) {
		TransactionInformation = transactionInformation;
	}
	public String getAccountId() {
		return AccountId;
	}
	public void setAccountId(String accountId) {
		AccountId = accountId;
	}
	public String getCreditDebitIndicator() {
		return CreditDebitIndicator;
	}
	public void setCreditDebitIndicator(String creditDebitIndicator) {
		CreditDebitIndicator = creditDebitIndicator;
	}
	public Amount getAmount() {
		return Amount;
	}
	public void setAmount(Amount amount) {
		Amount = amount;
	}
	
}
