package com.jtortugo.reproxy.proton;

import java.util.Map;

public abstract class Term {
	protected Map<String, Term> fields;

	public Map<String, Term> getFields() {
		return this.fields;
	}

	public Term get(String field) {
		return this.fields.get(field);
	}

	public void put(String field, Term value) {
		this.fields.put(field, value);
	}
}
