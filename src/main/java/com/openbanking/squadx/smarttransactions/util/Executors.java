package com.openbanking.squadx.smarttransactions.util;

import java.time.Duration;

import org.springframework.web.context.request.async.DeferredResult;

import rx.Single;

public class Executors {

	private static final Duration DEFAULT_TIMEOUT= Duration.ofMinutes(5);
	
	public static <T> DeferredResult<T> toDeferred(Single<T> single){
		DeferredResult<T> deferred = new DeferredResult<>(DEFAULT_TIMEOUT.toMillis());
		single.subscribe(deferred::setResult, deferred::setErrorResult);
		return deferred;
	}
}
