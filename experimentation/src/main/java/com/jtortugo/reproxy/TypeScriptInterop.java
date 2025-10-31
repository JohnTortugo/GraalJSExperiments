package com.jtortugo.reproxy;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.Proxy;

import com.jtortugo.reproxy.proton.Array;
import com.jtortugo.reproxy.proton.Bool;
import com.jtortugo.reproxy.proton.Integral;
import com.jtortugo.reproxy.proton.Rational;
import com.jtortugo.reproxy.proton.Struct;
import com.jtortugo.reproxy.proton.Term;
import com.jtortugo.reproxy.proton.Textual;

import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Context;

public class TypeScriptInterop {
	private final Value undefined;

	public TypeScriptInterop(Context context, String viewName) {
		Source undef = Source.newBuilder("js", "undefined", "undefined.js").buildLiteral();
		this.undefined = context.eval(undef);
	}

	public Value newUndefined() {
		return undefined;
	}

	public Object protonToTypeScript(Term term) {
		if (term == null) {
			return newUndefined();
		} else if (term instanceof Struct) {
			return new StructWrapperProxy(this, (Struct) term);
		} else if (term instanceof Array) {
			return new ArrayWrapperProxy(this, (Array) term);
		} else if (term instanceof Bool bl) {
			return bl.getValue();
		} else if (term instanceof Integral intg) {
			return intg.getValue();
		} else if (term instanceof Rational rat) {
			return rat.getValue();
		} else if (term instanceof Textual txt) {
			return txt.getValue();
		} else {
			System.err.println("Unknown Term type!");
			System.exit(1);
			return null;
		}
	}

	public Term typescriptToProton(Value v) {
		boolean isProxy = v.isProxyObject();

		if (isProxy) {
			Proxy termProxy = v.asProxyObject();
			if (termProxy instanceof StructWrapperProxy) {
				return ((StructWrapperProxy) termProxy).getTerm();
			} else if (termProxy instanceof ArrayWrapperProxy) {
				return ((ArrayWrapperProxy) termProxy).getTerm();
			} else {
				throw new IllegalArgumentException("Illegal cast of value " + v + " in view ");
			}
		} else {
			if (v.isBoolean()) {
				return new Bool(v.asBoolean());
			} else if (v.isNumber()) {
				if (v.fitsInInt()) {
					return new Integral(v.asInt());
				} else if (v.fitsInDouble()) {
					return new Rational(v.asDouble());
				} else {
					throw new IllegalArgumentException("Illegal cast of value " + v + " in view ");
				}
			} else if (v.isString()) {
				return new Textual(v.asString());
			} else {
				throw new IllegalArgumentException("Illegal cast of value " + v + " in view ");
			}
		}
	}
}
