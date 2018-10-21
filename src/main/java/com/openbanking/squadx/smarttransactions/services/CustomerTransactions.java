package com.openbanking.squadx.smarttransactions.services;

import javax.servlet.http.HttpServletRequest;

import com.openbanking.squadx.smarttransactions.model.DrillDownTransactionsHistory;
import com.openbanking.squadx.smarttransactions.model.TransactionAnalysis;
import com.openbanking.squadx.smarttransactions.model.TransactionsRequest;

import rx.Single;

public interface CustomerTransactions {
	Single<TransactionAnalysis> discoverTransactions(HttpServletRequest httpReq, TransactionsRequest transactionsRequest);
	
	Single<DrillDownTransactionsHistory> drillDownTransactions(HttpServletRequest httpReq, TransactionsRequest transactionsRequest);
}