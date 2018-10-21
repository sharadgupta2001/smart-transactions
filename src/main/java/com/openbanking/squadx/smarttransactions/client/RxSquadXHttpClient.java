package com.openbanking.squadx.smarttransactions.client;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.springframework.stereotype.Service;

import com.openbanking.squadx.smarttransactions.model.TransactionsRequest;

import rx.Observable;

@Service
public class RxSquadXHttpClient implements SquadXHttpClient {

	private final RxClient<RxObservableInvoker> client;
	private final String transactionURL;

	public RxSquadXHttpClient(RxClient<RxObservableInvoker> client) {
		this.client = client;
		this.transactionURL = "https://api.hsbc.qa.xlabs.one/invais/open-banking/v1.1/transactions";
	}

	@Override
	public Observable<Map<String, Object>> discoverTransactions(HttpServletRequest httpReq,
			TransactionsRequest transactionsRequest) {

		UriBuilder URL = UriBuilder.fromUri(transactionURL);
		// 2018-01-31T00:00:00+00:00
		if (StringUtils.isNotBlank(transactionsRequest.getTransactionFromDateTime())) {
			URL.queryParam("fromBookingDateTime", transactionsRequest.getTransactionFromDateTime() + "T00:00:00+00:00");
		}
		if (StringUtils.isNotBlank(transactionsRequest.getTransactionToDateTime())) {
			URL.queryParam("toBookingDateTime", transactionsRequest.getTransactionToDateTime()+"T00:00:00+00:00");
		}

		RxObservableInvoker rxObservableInvoker = createRequest(URL, httpReq);
		return rxObservableInvoker.get().map(response -> parseAsMap(response));
	}

	public Map<String, Object> parseAsMap(Response response) {
		GenericType<Map<String, Object>> asMap = new GenericType<Map<String, Object>>() {
		};
		try {
			Map<String, Object> json = response.hasEntity() ? response.readEntity(asMap) : null;

			if (response.getStatus() == 200) {
				return json;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private RxObservableInvoker createRequest(UriBuilder url, HttpServletRequest httpReq) {

		MultivaluedHashMap<String, Object> headerMap = new MultivaluedHashMap<>();
		headerMap.add("Content-Type", httpReq.getHeader("Content-Type"));
		headerMap.add("Accept", httpReq.getHeader("Accept"));
		headerMap.add("x-fapi-customer-ip-address", httpReq.getHeader("x-fapi-customer-ip-address"));
		headerMap.add("x-fapi-customer-last-logged-time", httpReq.getHeader("x-fapi-customer-last-logged-time"));
		headerMap.add("Authorization", "bearer " + httpReq.getHeader("Authorization"));
		headerMap.add("x-fapi-interaction-id", "2c96efd2-6566-490a-81d7-24dd51340196");
		headerMap.add("x-fapi-financial-id", "OB/2017/001");

		return client.target(url).request(APPLICATION_JSON_TYPE).headers(headerMap).accept(APPLICATION_JSON_TYPE).rx();
	}

}
