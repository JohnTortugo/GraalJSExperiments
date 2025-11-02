package com.jtortugo.proxies;

import java.util.Map;
import java.util.HashMap;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

public class Map_ProxyObject implements ProxyObject {
        private final Map<String, Object> fields;

        public Map_ProxyObject(Map<String, Object> fields) {
        	this.fields = fields;
		}

        public Map_ProxyObject(Object...values) {
        	this.fields = new HashMap<String, Object>();
        	for (int i=0; i<values.length; i++) {
				this.fields.put("field"+(i+1), values[i]);
        	}
		}

        @Override
        public Object getMember(final String member) {
        	return this.fields.get(member);
        }

        @Override
        public void putMember(final String key, final Value value) {
            this.fields.put(key, value);
        }

		@Override
		public Object getMemberKeys() {
			return this.fields.keySet().toArray();
		}

		@Override
		public boolean hasMember(String key) {
			 return this.fields.containsKey(key);
		}
}