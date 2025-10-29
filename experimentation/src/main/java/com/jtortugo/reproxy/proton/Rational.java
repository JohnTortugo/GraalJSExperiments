package com.jtortugo.reproxy.proton;

public class Rational extends Term {
	private final Double value;
	
	public Rational(Double value) {
		this.fields = null;
		this.value = value;
	}
	
	public Double getValue() {
		return this.value;
	}
}
