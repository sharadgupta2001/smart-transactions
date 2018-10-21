package com.openbanking.squadx.smarttransactions.config;

import javax.ws.rs.ext.ContextResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

	private final ObjectMapper mapper;

	public ObjectMapperProvider(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return mapper;
	}

}
