package com.jtortugo.reproxy.proton;

public class Integral extends Term {
	private final Integer value;
	
	public Integral(Integer integral) {
		this.fields = null;
		this.value = integral;
	}

	public Integer getValue() {
		return this.value;
	}
}
