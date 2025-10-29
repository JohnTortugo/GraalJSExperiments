package com.jtortugo.reproxy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.graalvm.polyglot.proxy.ProxyObject;

import com.jtortugo.reproxy.proton.Struct;
import com.jtortugo.reproxy.proton.Term;

import lombok.NonNull;

public class StructWrapperProxy implements ProxyObject {
	protected static final String GET_ION_TYPE_METHOD = "getIonType";
	protected static final String ION_EQUALS_METHOD = "ionEquals";

	protected final TypeScriptInterop typeInterop;
	private final Struct protonStruct;
	private final Map<String, Object> converted;

	public StructWrapperProxy(@NonNull TypeScriptInterop typeInterop, @NonNull Struct struct) {
		this.typeInterop = typeInterop;
		this.protonStruct = struct;
		this.converted = new HashMap<>();
	}

	private Object __ionEquals(Value... args) {
		return getTerm().equals(typeInterop.typescriptToProton(args[0]));
	}

	@Override
	public Object getMember(String key) {
		return switch (key) {
		case ION_EQUALS_METHOD -> (ProxyExecutable) (this::__ionEquals);
		default -> ionGetMember(key);
		};
	}

	public Object get(String key) {
		return converted.computeIfAbsent(key, k -> {
			final Term value = protonStruct.get(k);
			if (value == null) {
				return typeInterop.newUndefined();
			} else {
				return typeInterop.protonToTypeScript(value);
			}
		});
	}

	private Object __get(Value... args) {
		return get(args[0].asString());
	}

	protected Object ionGetMember(String key) {
		return switch (key) {
		case "get" -> (ProxyExecutable) (this::__get);
		default -> get(key);
		};
	}

	@Override
	public Object getMemberKeys() {
		Set<String> memberKeys = new HashSet<>();
		memberKeys.addAll(protonStruct.getFields().keySet());
		return memberKeys.toArray();
	}

	@Override
	public boolean hasMember(String key) {
		return switch (key) {
		case GET_ION_TYPE_METHOD, ION_EQUALS_METHOD -> true;
		default -> ionHasMember(key);
		};
	}

	protected boolean ionHasMember(String key) {
		return protonStruct.getFields().containsKey(key) || key.equals("get") || key.equals("toJSON");
	}

	@Override
	public void putMember(String key, Value value) {
		throw new UnsupportedOperationException("ion.ReadOnlyStructWrapper cannot be modified");
	}

	public boolean removeMember(String key) {
		throw new UnsupportedOperationException("ion.ReadOnlyStructWrapper cannot be modified");
	}

	public Term getTerm() {
		return protonStruct;
	}
}