package com.jtortugo.proxies;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

public class DirectAccess_OneField_ProxyObject implements ProxyObject {
    Integer field1 = 0;

    public DirectAccess_OneField_ProxyObject(int field1) {
    	this.field1 = field1;
    }

    @Override
    public Object getMember(final String key) {
		return field1;
    }

    @Override
    public Object getMemberKeys() {
        return new String[] {"field1"};
    }

    @Override
    public boolean hasMember(final String key) {
        return key.equals("field1");
    }
    

    @Override
    public void putMember(final String key, final Value value) {
        throw new UnsupportedOperationException("SingleFieldProxy is immutable");
    }
}