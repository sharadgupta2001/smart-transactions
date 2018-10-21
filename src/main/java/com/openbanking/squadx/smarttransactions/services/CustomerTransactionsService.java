package com.openbanking.squadx.smarttransactions.services;

import javax.servlet.http.HttpServletRequest;

import com.jayway.jsonpath.ParseContext;
import com.openbanking.squadx.smarttransactions.client.SquadXClient;
import com.openbanking.squadx.smarttransactions.model.DrillDownTransactionsHistory;
import com.openbanking.squadx.smarttransactions.model.TransactionAnalysis;
import com.openbanking.squadx.smarttransactions.model.TransactionHistory;
import com.openbanking.squadx.smarttransactions.model.TransactionsRequest;
import com.openbanking.squadx.smarttransactions.services.CustomerTransactions;

import rx.Single;

public class CustomerTransactionsService implements CustomerTransactions {

	private final SquadXClient squadXClient;
	
	private final ParseContext parseContext;
	
	public CustomerTransactionsService(SquadXClient squadXClient,
			ParseContext parseContext) {
		this.squadXClient= squadXClient;
		this.parseContext= parseContext;
	}
	
	@Override
	public Single<TransactionAnalysis> discoverTransactions(HttpServletRequest httpReq, TransactionsRequest transactionsRequest) {
		return squadXClient.discoverTransactions(httpReq, transactionsRequest).map(it-> TransactionHistory.from(it,parseContext));
	}
	
	@Override
	public Single<DrillDownTransactionsHistory> drillDownTransactions(HttpServletRequest httpReq, TransactionsRequest transactionsRequest) {
		return squadXClient.discoverTransactions(httpReq, transactionsRequest).map(it-> DrillDownTransactionsHistory.from(it,parseContext));
	}
}
