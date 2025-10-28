package com.jtortugo.reproxy.proton;

public class Bool extends Term {
	private final Boolean value;
	
	public Bool(Boolean value) {
		this.fields = null;
		this.value = value;
	}
	
	public Boolean getValue() {
		return this.value;
	}
}
