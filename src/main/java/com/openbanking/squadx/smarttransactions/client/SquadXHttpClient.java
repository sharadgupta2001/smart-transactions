package com.openbanking.squadx.smarttransactions.client;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.openbanking.squadx.smarttransactions.model.TransactionsRequest;

import rx.Observable;

public interface SquadXHttpClient {
	
	Observable<Map<String, Object>> discoverTransactions(HttpServletRequest httpReq, TransactionsRequest transactionsRequest);

}
