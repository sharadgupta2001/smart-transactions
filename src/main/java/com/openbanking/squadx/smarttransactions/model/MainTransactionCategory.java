package com.openbanking.squadx.smarttransactions.model;

import java.util.List;

public class MainTransactionCategory {
	public String name;
	public List<TransactionCategory> children;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TransactionCategory> getChildren() {
		return children;
	}
	public void setChildren(List<TransactionCategory> children) {
		this.children = children;
	}

}
