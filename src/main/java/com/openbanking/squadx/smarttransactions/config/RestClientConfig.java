package com.openbanking.squadx.smarttransactions.config;


import static org.glassfish.jersey.client.ClientProperties.CONNECT_TIMEOUT;
import static org.glassfish.jersey.client.ClientProperties.READ_TIMEOUT;

import javax.ws.rs.client.ClientBuilder;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.rxjava.RxObservable;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvokerProvider;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class RestClientConfig {

	private final ObjectMapper mapper;
	
	public RestClientConfig(ObjectMapper mapper) {
		this.mapper= mapper;
	}

	@Bean
	public RxClient<RxObservableInvoker> restClient(){
		return RxObservable.from(ClientBuilder.newClient(
					new ClientConfig()
						.property(CONNECT_TIMEOUT, 60000)
						.property(READ_TIMEOUT, 60000)
						.register(RxObservableInvokerProvider.class)
						.register(new JacksonFeature())
						.register(new ObjectMapperProvider(mapper))
				));
	}
}
