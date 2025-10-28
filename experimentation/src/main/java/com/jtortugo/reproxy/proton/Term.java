package com.jtortugo.reproxy.proton;

import java.util.Map;

public abstract class Term {
	protected Map<String, Term> fields;
	protected String[] annotations;
	protected Type type;

	public Map<String, Term> getFields() {
		return this.fields;
	}

	public Term get(String field) {
		return this.fields.get(field);
	}

	public String[] annotations() {
		return this.annotations;
	}
}
