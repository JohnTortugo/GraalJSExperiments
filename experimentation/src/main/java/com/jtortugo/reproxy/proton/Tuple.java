package com.jtortugo.reproxy.proton;

public abstract class Tuple extends Term implements Iterable<Term> {
    public abstract Term[] getElements();

    public abstract Tuple walk(Object s);

    public abstract Tuple substitute(Object from, Term to);

    public abstract Tuple substitute(Object s);

    public abstract Tuple withAnnotations(String... annotations);

    public abstract Object ordering();

    public abstract int size();

	public abstract String[] annotations();
}