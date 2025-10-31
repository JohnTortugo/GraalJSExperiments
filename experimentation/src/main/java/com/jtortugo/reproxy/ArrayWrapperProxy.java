package com.jtortugo.reproxy;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;

import com.jtortugo.reproxy.proton.Term;
import com.jtortugo.reproxy.proton.Array;

import java.util.HashMap;
import java.util.Map;

public class ArrayWrapperProxy implements ProxyArray {
    private final Array array;
    private final Map<Long, Object> converted;
	protected final TypeScriptInterop typeInterop;

    public ArrayWrapperProxy(TypeScriptInterop typeInterop, Array array) {
    	this.typeInterop = typeInterop;
        this.array = array;
        this.converted = new HashMap<>();
    }

    @Override
    public Object get(long index) {
        return converted.computeIfAbsent(index, i -> {
            Term value = array.getElements()[i.intValue()];
			return typeInterop.protonToTypeScript(value);
        });
    }

    @Override
    public void set(long index, Value value) {
        throw new UnsupportedOperationException("cannot be modified");
    }

    @Override
    public boolean remove(long index) {
        throw new UnsupportedOperationException("cannot be modified");
    }

    @Override
    public long getSize() {
        return array.size();
    }

    public Term getTerm() {
        return this.array;
    }
}