package com.jtortugo.proxies;

import java.util.Collection;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class Map_POJO implements Map<String, Object> {
        private final Map<String, Object> fields;

        public Map_POJO(Map<String, Object> fields) {
        	this.fields = fields;
		}

        public Map_POJO(Object...values) {
        	this.fields = new HashMap<String, Object>();
        	for (int i=0; i<values.length; i++) {
				this.fields.put("field"+(i+1), values[i]);
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
		public boolean containsKey(Object key) {
			return this.fields.containsKey(key);
		}

		@Override
		public boolean containsValue(Object value) {
			return this.fields.containsValue(value);
		}

		@Override
		public Object get(Object key) {
			return this.fields.get(key);
		}

		@Override
		public Object put(String key, Object value) {
			return this.fields.put(key, value);
		}

		@Override
		public Object remove(Object key) {
			return this.fields.remove(key);
		}
}