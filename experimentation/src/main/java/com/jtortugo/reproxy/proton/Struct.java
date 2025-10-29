package com.jtortugo.reproxy.proton;

import java.util.Map;

public class Struct extends Term {
	public Struct(final Map<String, Term> fields) {
		this.fields = fields;
	}
}
