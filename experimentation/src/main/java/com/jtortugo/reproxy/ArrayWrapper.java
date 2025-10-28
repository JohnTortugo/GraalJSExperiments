package com.jtortugo.reproxy;

import org.apache.commons.lang3.math.NumberUtils;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;

import com.jtortugo.reproxy.proton.Term;
import com.jtortugo.reproxy.proton.Array;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArrayWrapper implements ProxyArray {
    private final Array tuple;
    private final Map<Long, Object> converted;
	protected final TypeScriptIonInterop typeInterop;

    public ArrayWrapper(TypeScriptIonInterop typeInterop, Array tuple) {
    	this.typeInterop = typeInterop;
        this.tuple = tuple;
        this.converted = new HashMap<>();
    }

    @Override
    public Object get(long index) {
        return converted.computeIfAbsent(index, i -> {
            Term value = tuple.getElements()[i.intValue()];
            if (value == null) {
                return typeInterop.newUndefined();
            } else {
                return typeInterop.protonToTypeScript(value);
            }
        });
    }

    @Override
    public void set(long index, Value value) {
        throw new UnsupportedOperationException("ion.ReadOnlyList cannot be modified");
    }

    @Override
    public boolean remove(long index) {
        throw new UnsupportedOperationException("ion.ReadOnlyList cannot be modified");
    }

    @Override
    public long getSize() {
        return tuple.getElements().length;
    }

    public Term getTerm() {
        return this.tuple;
    }

    protected Object ionGetMember(String key) {
        long index =  Long.parseLong(key);
        long size = getSize();
        // ProxyArray returns undefined if index >= size
        if (index >= size) {
            return null;
        } else {
            return get(index);
        }
    }

    public Object getMemberKeys() {
        return List.of();
    }

    public boolean hasMember(String key) {
        return switch (key) {
            case "getAnnotations", "getIonType", "ionEquals" -> true;
            default -> ionHasMember(key);
        };
    }

    protected boolean ionHasMember(String key) {
        try {
            if (!NumberUtils.isParsable(key)) {
                return false;
            }
            long index = Long.parseLong(key);
            long size = getSize();
            // ProxyArray returns undefined if index >= size
            if (index >= size) {
                return false;
            }
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public void putMember(String key, Value value) {
        throw new UnsupportedOperationException("ion.ReadOnlyList cannot be modified");
    }
}