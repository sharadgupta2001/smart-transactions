package com.openbanking.squadx.smarttransactions.config;

import static com.jayway.jsonpath.Option.DEFAULT_PATH_LEAF_TO_NULL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.openbanking.squadx.smarttransactions.client.SquadXClient;
import com.openbanking.squadx.smarttransactions.services.CustomerTransactionsService;


@Configuration
public class AppConfig {

	@Bean
	public CustomerTransactionsService getCustTranService(@Autowired SquadXClient sqClient,
			@Autowired ParseContext parseContext) {
		
		return new CustomerTransactionsService(sqClient, parseContext); 
	}
	
	@Bean
	public ParseContext jsonPath(ObjectMapper mapper) {
		return JsonPath.using(new com.jayway.jsonpath.Configuration.ConfigurationBuilder()
				.jsonProvider(new JacksonJsonProvider(mapper))
				.mappingProvider(new JacksonMappingProvider(mapper))
				.options(DEFAULT_PATH_LEAF_TO_NULL).build());
	}

}
