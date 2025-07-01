package com.jtortugo.proxies;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

public class DirectAccess_FourFields_ProxyObject implements ProxyObject {
    Integer field1 = 0;
    Integer field2 = 0;
    Integer field3 = 0;
    Integer field4 = 0;

    public DirectAccess_FourFields_ProxyObject(int field1, int field2, int field3, int field4) {
    	this.field1 = field1;
    	this.field2 = field2;
    	this.field3 = field3;
    	this.field4 = field4;
    }

    @Override
    public Object getMember(final String key) {
    	switch (key) {
    		case "field1": return field1;
    		case "field2": return field2;
    		case "field3": return field3;
    		case "field4": return field4;
    		default: return -1;
    	}
    }

    @Override
    public Object getMemberKeys() {
        return new String[] {"field1", "field2", "field3", "field4"};
    }

    @Override
    public boolean hasMember(final String key) {
        return key.equals("field1") ||
        		key.equals("field2") ||
        		key.equals("field3") ||
        		key.equals("field4");
    }
    
    @Override
    public void putMember(final String key, final Value value) {
        throw new UnsupportedOperationException("SingleFieldProxy is immutable");
    }
}