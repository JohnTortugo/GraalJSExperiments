package com.jtortugo.reproxy;

import com.jtortugo.reproxy.proton.Term;
import com.jtortugo.reproxy.proton.Tuple;
import com.jtortugo.reproxy.proton.TypeSchema;
import com.jtortugo.reproxy.proton.Types;

import org.apache.commons.lang3.math.NumberUtils;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProtonReadOnlyTupleWrapper extends ContainerProxy implements ProxyArray {
    private final Tuple tuple;
    private final Map<Long, Object> converted;

    public ProtonReadOnlyTupleWrapper(TypeScriptIonInterop typeInterop, Tuple tuple) {
        super(typeInterop);
        this.tuple = tuple;
        this.converted = new HashMap<>();
    }

    @Override
    public Object get(long index) {
        return converted.computeIfAbsent(index, i -> {
            Term value = tuple.getElements()[i.intValue()];
            if (value == null) {
                return typeInterop.newUndefined();
            } else if (value.type() == Types.structure()) {
                return typeInterop.protonToTypeScript(value, TypeSchema.ION_READONLYSTRUCT);
            } else if (value.type() == Types.tuple()) {
                return typeInterop.protonToTypeScript(value, TypeSchema.ION_READONLYLIST);
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

    @Override
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

    @Override
    public Object getMemberKeys() {
        // `enumerateOwnPropertyNamesForeign:188` actually enumerates the keys from the array automatically.
        // This method is probably only used for member keys other than indices, which is none for tuples.
        return List.of();
    }

    @Override
    public boolean hasMember(String key) {
        return switch (key) {
            case GET_ANNOTATIONS_METHOD, GET_ION_TYPE_METHOD, ION_EQUALS_METHOD -> true;
            default -> ionHasMember(key);
        };
    }

    @Override
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

    protected String[] ionAnnotations() {
        return tuple.annotations();
    }

    @Override
    public void putMember(String key, Value value) {
        throw new UnsupportedOperationException("ion.ReadOnlyList cannot be modified");
    }

    @Override
    public String getIonType() {
        return "IonList";
    }
}