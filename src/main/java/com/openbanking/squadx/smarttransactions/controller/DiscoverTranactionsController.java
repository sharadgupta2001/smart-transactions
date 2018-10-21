package com.openbanking.squadx.smarttransactions.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.openbanking.squadx.smarttransactions.model.TransactionsHistory;
import com.openbanking.squadx.smarttransactions.model.TransactionAnalysis;
import com.openbanking.squadx.smarttransactions.model.TransactionsRequest;
import com.openbanking.squadx.smarttransactions.services.CustomerTransactions;
import com.openbanking.squadx.smarttransactions.util.Executors;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping({ "/api" })
public class DiscoverTranactionsController {

	private final CustomerTransactions customerTransactions;

	public DiscoverTranactionsController(CustomerTransactions customerTransactions) {
		this.customerTransactions = customerTransactions;
	}

	@PostMapping(value = "/discover-transactions")
	public DeferredResult<TransactionAnalysis> getData(HttpServletRequest request,
			@RequestBody TransactionsRequest transactionsRequest) {
		return Executors.toDeferred(customerTransactions.discoverTransactions(request, transactionsRequest));
	}

	@PostMapping(value = "/drill-down-transactions", consumes = "application/json")
	public DeferredResult<TransactionsHistory> drillDownTransactions(HttpServletRequest request,
			@RequestBody TransactionsRequest transactionsRequest) {
		return Executors.toDeferred(customerTransactions.drillDownTransactions(request, transactionsRequest));
	}
}
