package com.jtortugo.proxies;

import java.util.HashMap;
import java.util.Map;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

public class MapAccess_FourFields_ProxyObject implements ProxyObject {
        private final Map<String, Object> fields;

        public MapAccess_FourFields_ProxyObject(int field1, int field2, int field3, int field4) {
        	this.fields = new HashMap<>(4); 
        	this.fields.put("field1", field1); 
        	this.fields.put("field2", field2); 
        	this.fields.put("field3", field3); 
        	this.fields.put("field4", field4); 
		}
        
        @Override
        public Object getMember(final String member) {
        	return this.fields.get(member);
        }

        @Override
        public void putMember(final String key, final Value value) {
            throw new UnsupportedOperationException("SingleFieldProxy is immutable");
        }

		@Override
		public Object getMemberKeys() {
			return new String[] {"field1", "field2", "field3", "field4"};
		}

		@Override
		public boolean hasMember(String key) {
			 return key.equals("field1") ||
	            		key.equals("field2") ||
	            		key.equals("field3") ||
	            		key.equals("field4");
		}
}