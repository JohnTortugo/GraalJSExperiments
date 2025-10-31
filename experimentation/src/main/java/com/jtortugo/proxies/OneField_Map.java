package com.jtortugo.proxies;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

public class OneField_Map implements Map<String, Object> {
        private final Map<String, Object> fields;

        public OneField_Map(int field1) {
        	this.fields = new HashMap<String, Object>(4); 
        	this.fields.put("field1", field1); 
		}

        public OneField_Map(int...fields) {
        	this.fields = new HashMap<String, Object>(fields.length); 
        	for (int i=0; i<fields.length; i++) {
				this.fields.put("field" + (i+1), fields[i]); 
        	}
		}       
        
		@Override
		public int size() {
			return this.fields.size();
		}

		@Override
		public boolean isEmpty() {
			return this.fields.isEmpty();
		}

		@Override
		public void putAll(Map m) {
			this.fields.putAll(m);
		}

		@Override
		public void clear() {
			this.fields.clear();
		}

		@Override
		public Set keySet() {
			return this.fields.keySet();
		}

		@Override
		public Collection values() {
			return this.fields.values();
		}

		@Override
		public Set entrySet() {
			return this.fields.entrySet();
		}

		@Override
		public boolean containsKey(java.lang.Object key) {
			return this.fields.containsKey(key);
		}

		@Override
		public boolean containsValue(java.lang.Object value) {
			return this.fields.containsValue(value);
		}

		@Override
		public Object get(java.lang.Object key) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object put(String key, Object value) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object remove(java.lang.Object key) {
			// TODO Auto-generated method stub
			return null;
		}
}