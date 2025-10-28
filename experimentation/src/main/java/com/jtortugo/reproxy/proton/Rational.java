package com.jtortugo.reproxy.proton;

public class Rational extends Term {
	private final Double value;
	
	public Rational(Double d) {
		this.fields = null;
		this.value = d;
	}
	
	public Double getValue() {
		return this.value;
	}
}
