package com.jtortugo.proxies;

import java.util.HashMap;
import java.util.Map;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

public class MapAccess_OneField_ProxyObject implements ProxyObject {
        private final Map<String, Object> fields;

        public MapAccess_OneField_ProxyObject(int field1) {
        	this.fields = new HashMap<>(4); 
        	this.fields.put("field1", field1); 
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
			return new String[] {"field1"};
		}

		@Override
		public boolean hasMember(String key) {
			 return key.equals("field1");
		}
}