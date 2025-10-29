package com.jtortugo.reproxy.proton;

public class Array extends Term {
    private final Term[] elements;

	public Array(final Term[] elements) {
		this.elements = elements;
	}
	
    public Term[] getElements() {
        return elements;
    }

    public int size() {
        return elements.length;
    }
}
