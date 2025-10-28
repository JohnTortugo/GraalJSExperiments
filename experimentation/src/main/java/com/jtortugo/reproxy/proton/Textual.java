package com.jtortugo.reproxy.proton;

public class Textual extends Term {
	private final String value;
	
	public Textual(String value) {
		this.fields = null;
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
}
