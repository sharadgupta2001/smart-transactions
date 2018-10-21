package com.openbanking.squadx.smarttransactions.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.ParseContext;

public class TransactionsHistory {

	public String name;
	public List<TransactionCategory> children;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TransactionCategory> getChildren() {
		return children;
	}
	public void setChildren(List<TransactionCategory> children) {
		this.children = children;
	}
	
	public static TransactionsHistory from(Map<String,Object> response, ParseContext parseContext) {
		TransactionsHistory transactionsHistory = new TransactionsHistory();
		Data TransactionsData = parseContext.parse(response).read("$.Data",Data.class);
		Map<String, List<TransactionDetail>> debitTransactionDetails= new HashMap<>();

		for (Transaction transaction : TransactionsData.getTransaction()) {

			if ("Debit".equalsIgnoreCase(transaction.CreditDebitIndicator)) {
				if (debitTransactionDetails.containsKey(transaction.getTransactionInformation())) {
					List<TransactionDetail> transactionDetailsList = debitTransactionDetails.get(transaction.getTransactionInformation());
					transactionDetailsList.add(populateTransactionDetails(transaction));

				} else {
					List<TransactionDetail> transactionDetailsList = new ArrayList<>();
					transactionDetailsList.add(populateTransactionDetails(transaction));
					debitTransactionDetails.put(transaction.getTransactionInformation(), transactionDetailsList);
				}
			}
		}

		List<TransactionCategory> debitTransactionCategoryList = new ArrayList<>();
		
		for (Map.Entry<String,List<TransactionDetail>> entry : debitTransactionDetails.entrySet()) {
			TransactionCategory transactionCategory= new TransactionCategory();
			transactionCategory.setName(entry.getKey());
			transactionCategory.setChildren(entry.getValue());
			debitTransactionCategoryList.add(transactionCategory);
		} 

		transactionsHistory.setName("Expense Summary");
		transactionsHistory.setChildren(debitTransactionCategoryList);

		return transactionsHistory;
	}

	private static TransactionDetail populateTransactionDetails(Transaction transaction) {
		TransactionDetail transactionDetails = new TransactionDetail();
		transactionDetails.setAmount(transaction.getAmount().getAmount().replaceAll(",", ""));
		transactionDetails.setCurrency(transaction.getAmount().getCurrency());
		transactionDetails.setName(transaction.getMerchantDetails().getMerchantName());
		transactionDetails.setTransactionDate(transaction.getBookingDateTime().substring(0,10));
		return transactionDetails;
	}
}
