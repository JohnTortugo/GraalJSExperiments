package com.jtortugo.reproxy;

import org.graalvm.polyglot.Value;

import com.jtortugo.reproxy.proton.Term;
import com.jtortugo.reproxy.proton.TypeSchema;

import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Context;

public class TypeScriptIonInterop {
	private static final String UNDEFINED = "undefined";
	
	private final Value undefined;

    public TypeScriptIonInterop(Context context, String viewName) {
        Source undef = Source.newBuilder("js", UNDEFINED, "undefined.js").buildLiteral();
        this.undefined = context.eval(undef);
    }
	
	public Object protonAnnotationsToTypeScript(String[] ionAnnotations) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object typeScriptToProton(Value value) {
		// TODO Auto-generated method stub
		return null;
	}

	public Value newUndefined() {
		// TODO Auto-generated method stub
		return undefined;
	}

	public Object protonToTypeScript(Term value, TypeSchema ionReadonlystruct) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object protonToTypeScript(Term value) {
		// TODO Auto-generated method stub
		return null;
	}

}
