package com.jtortugo.reproxy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyObject;

import com.jtortugo.reproxy.proton.Struct;
import com.jtortugo.reproxy.proton.Term;

public class StructWrapperProxy implements ProxyObject {
	private final TypeScriptInterop typeInterop;
	private final Struct protonStruct;
	private final Map<String, Object> converted;

	public StructWrapperProxy(TypeScriptInterop typeInterop, Struct struct) {
		this.typeInterop = typeInterop;
		this.protonStruct = struct;
		this.converted = new HashMap<>();
	}

	@Override
	public Object getMember(String key) {
		return converted.computeIfAbsent(key, k -> {
			final Term value = protonStruct.get(k);
			return typeInterop.protonToTypeScript(value);
		});
	}

	@Override
	public Object getMemberKeys() {
		Set<String> memberKeys = new HashSet<>();
		memberKeys.addAll(protonStruct.getFields().keySet());
		return memberKeys.toArray();
	}

	@Override
	public boolean hasMember(String key) {
		return protonStruct.getFields().containsKey(key);
	}

	@Override
	public void putMember(String key, Value value) {
		throw new UnsupportedOperationException("Type cannot be modified");
	}

	public boolean removeMember(String key) {
		throw new UnsupportedOperationException("Type cannot be modified");
	}

	public Term getTerm() {
		return protonStruct;
	}
}