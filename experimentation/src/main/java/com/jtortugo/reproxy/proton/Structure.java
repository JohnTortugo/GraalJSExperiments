package com.jtortugo.reproxy.proton;

import java.util.Map;

public class Structure extends Term {
	public Structure(final Map<String, Term> fields, final String...annotations) {
		this.fields = fields;
		this.annotations = annotations;
		this.type = Type.STRUCTURE;
	}
}
