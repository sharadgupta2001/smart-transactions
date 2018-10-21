package com.openbanking.squadx.smarttransactions.model;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.jayway.jsonpath.ParseContext;

public class TransactionHistory {

	public static TransactionAnalysis from(Map<String,Object> response, ParseContext parseContext) {
		Data TransactionsData= parseContext.parse(response).read("$.Data",Data.class);
		TransactionAnalysis TransactionAnalysis= new TransactionAnalysis();
		
		Map<String, AmountDetails> debitTransactionDetails= new HashMap<>();
		Map<String, AmountDetails> creditTransactionDetails= new HashMap<>();
		Double totalDebit= 0.00;
		Double totalCredit= 0.00;
		
		int sizeOfTransactions= TransactionsData.getTransaction().size();
		
		for(int i=0; i< sizeOfTransactions; i++) {
			Transaction transaction= TransactionsData.getTransaction().get(i);
			if(transaction.CreditDebitIndicator.equalsIgnoreCase("Debit")) {
				if(debitTransactionDetails.containsKey(transaction.getTransactionInformation())) {
					AmountDetails existingAmount= debitTransactionDetails.get(transaction.getTransactionInformation());
					Double netAmount= existingAmount.getAmount()+ Double.valueOf(transaction.getAmount().getAmount().replaceAll(",", ""));
					AmountDetails amountDetails= new AmountDetails();
					amountDetails.setAmount(netAmount);
					amountDetails.setCurrrency(transaction.getAmount().getCurrency());
					debitTransactionDetails.put(transaction.getTransactionInformation(), amountDetails);
					totalDebit= totalDebit+Double.valueOf(transaction.getAmount().getAmount().replaceAll(",", ""));;
				}else {
					AmountDetails amountDetails= new AmountDetails();
					amountDetails.setAmount(Double.valueOf(transaction.getAmount().getAmount().replaceAll(",", "")));
					amountDetails.setCurrrency(transaction.getAmount().getCurrency());
					debitTransactionDetails.put(transaction.getTransactionInformation(), amountDetails);
					totalDebit= totalDebit+Double.valueOf(transaction.getAmount().getAmount().replaceAll(",", ""));;
				}
			}else{
				if(creditTransactionDetails.containsKey(transaction.getTransactionInformation())) {
					AmountDetails existingAmount= creditTransactionDetails.get(transaction.getTransactionInformation());
					Double netAmount= existingAmount.getAmount()+ Double.valueOf(transaction.getAmount().getAmount().replaceAll(",", ""));
					AmountDetails amountDetails= new AmountDetails();
					amountDetails.setAmount(netAmount);
					amountDetails.setCurrrency(transaction.getAmount().getCurrency());
					creditTransactionDetails.put(transaction.getTransactionInformation(), amountDetails);
					totalCredit= totalCredit+Double.valueOf(transaction.getAmount().getAmount().replaceAll(",", ""));;
				}else {
					AmountDetails amountDetails= new AmountDetails();
					amountDetails.setAmount(Double.valueOf(transaction.getAmount().getAmount().replaceAll(",", "")));
					amountDetails.setCurrrency(transaction.getAmount().getCurrency());
					creditTransactionDetails.put(transaction.getTransactionInformation(), amountDetails);
					totalCredit= totalCredit+Double.valueOf(transaction.getAmount().getAmount().replaceAll(",", ""));;
				}
			}
		}
		
		if(sizeOfTransactions > 1) {
			String startBalance= TransactionsData.getTransaction().get(sizeOfTransactions-1).getBalance().getAmount().getAmount().replaceAll(",", "");
			String endBalance= TransactionsData.getTransaction().get(0).getBalance().getAmount().getAmount().replaceAll(",", "");
			Double totalSaving= Double.valueOf(endBalance)-Double.valueOf(startBalance);
			TransactionAnalysis.setTotalSaving(totalSaving);
		}else{
			TransactionAnalysis.setTotalSaving( Double.valueOf(TransactionsData.getTransaction().get(0).getBalance().getAmount().getAmount().replaceAll(",", "")));
		}
		
		debitTransactionDetails= debitTransactionDetails.entrySet().stream().sorted((e1,e2) -> e2.getValue().getAmount().compareTo(e1.getValue().getAmount()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
		
		creditTransactionDetails= debitTransactionDetails.entrySet().stream().sorted((e1,e2) -> e2.getValue().getAmount().compareTo(e1.getValue().getAmount()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
		
		TransactionAnalysis.setDebitTransactionDetails(debitTransactionDetails);
		TransactionAnalysis.setCreditTransactionDetails(creditTransactionDetails);
		TransactionAnalysis.setTotalCredit(totalCredit);
		TransactionAnalysis.setTotalDebit(totalDebit);
		return TransactionAnalysis;
	}
}
