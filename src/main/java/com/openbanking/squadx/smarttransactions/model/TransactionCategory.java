package com.openbanking.squadx.smarttransactions.model;

import java.util.List;

public class TransactionCategory {
	public String name;
	public List<TransactionDetail> children;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<TransactionDetail> getChildren() {
		return children;
	}
	public void setChildren(List<TransactionDetail> children) {
		this.children = children;
	}
}
