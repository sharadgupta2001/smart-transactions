package com.openbanking.squadx.smarttransactions.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jayway.jsonpath.ParseContext;

public class DrillDownTransactionsHistory {

	public String name;
	public List<MainTransactionCategory> children;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<MainTransactionCategory> getChildren() {
		return children;
	}
	public void setChildren(List<MainTransactionCategory> children) {
		this.children = children;
	}
	
	public static DrillDownTransactionsHistory from(Map<String,Object> response, ParseContext parseContext) {
		DrillDownTransactionsHistory drillDownTransactionsHistory= new DrillDownTransactionsHistory();
		
		Data TransactionsData= parseContext.parse(response).read("$.Data",Data.class);
		
		Map<String, List<TransactionDetail>> debitTransactionDetails= new HashMap<>();
		
		int sizeOfTransactions= TransactionsData.getTransaction().size();
		
		for(int i=0; i< sizeOfTransactions; i++) {
			Transaction transaction= TransactionsData.getTransaction().get(i);
			if("Debit".equalsIgnoreCase(transaction.CreditDebitIndicator)) {
				if(debitTransactionDetails.containsKey(transaction.getTransactionInformation())) {
					List<TransactionDetail> transactionDetailsList= debitTransactionDetails.get(transaction.getTransactionInformation());
					TransactionDetail transactionDetails= new TransactionDetail();
					transactionDetails.setAmount(transaction.getAmount().getAmount().replaceAll(",", ""));
					transactionDetails.setCurrency(transaction.getAmount().getCurrency());
					transactionDetails.setName(transaction.getMerchantDetails().getMerchantName());
					transactionDetails.setTransactionDate(transaction.getBookingDateTime().substring(0,10));
					transactionDetailsList.add(transactionDetails);
				}else {
					List<TransactionDetail> transactionDetailsList = new ArrayList<>();
					TransactionDetail transactionDetails= new TransactionDetail();
					transactionDetails.setAmount(transaction.getAmount().getAmount().replaceAll(",", ""));
					transactionDetails.setCurrency(transaction.getAmount().getCurrency());
					transactionDetails.setName(transaction.getMerchantDetails().getMerchantName());
					transactionDetails.setTransactionDate(transaction.getBookingDateTime().substring(0,10));
					transactionDetailsList.add(transactionDetails);
					debitTransactionDetails.put(transaction.getTransactionInformation(), transactionDetailsList);
				}
			}
		}
		
		MainTransactionCategory debitMainTransactionCategory = new MainTransactionCategory();
		List<TransactionCategory> debitTransactionCategoryList = new ArrayList<>();
		
		for (Map.Entry<String,List<TransactionDetail>> entry : debitTransactionDetails.entrySet()) {
			TransactionCategory transactionCategory= new TransactionCategory();
			transactionCategory.setName(entry.getKey());
			transactionCategory.setChildren(entry.getValue());
			debitTransactionCategoryList.add(transactionCategory);
		} 
		debitMainTransactionCategory.setName("Expenses");
		debitMainTransactionCategory.setChildren(debitTransactionCategoryList);
		
		List<MainTransactionCategory> mainTransactionCategoryList = new ArrayList<>();
		mainTransactionCategoryList.add(debitMainTransactionCategory);
		
		drillDownTransactionsHistory.setName("Account Transaction");
		drillDownTransactionsHistory.setChildren(mainTransactionCategoryList);

		return drillDownTransactionsHistory;
	}
}
