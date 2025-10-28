package com.jtortugo.reproxy;

import com.jtortugo.reproxy.proton.Structure;
import com.jtortugo.reproxy.proton.Term;
import com.jtortugo.reproxy.proton.Types;
import com.jtortugo.reproxy.proton.TypeSchema;

import lombok.NonNull;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.graalvm.polyglot.proxy.ProxyObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;

public class ProtonReadOnlyStructWrapper extends ContainerProxy {
    /**
     * StructSchema field is unnecessary for ion.ReadOnlyStruct:
     * the input must be declared as ion.ReadOnlyStruct in function signature
     * to be interoped to ion.ReadOnlyStruct, hence interface cannot be used for this type
     */
    private final Structure protonStruct;
    private final Map<String, Object> converted;

    public ProtonReadOnlyStructWrapper(@NonNull TypeScriptIonInterop typeInterop, @NonNull Structure struct) {
        super(typeInterop);
        this.protonStruct = struct;
        this.converted = new HashMap<>();
    }

    @Override
    public String getIonType() {
        return "IonStruct";
    }

    public Object get(String key) {
        return converted.computeIfAbsent(key, k -> {
            final Term value = protonStruct.get(k);
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

    public ProxyObject toJSON() {
        // Use LinkedHashMap to maintain the order of elements
        Map<String, Object> map = new LinkedHashMap<>();
        //for (Map.Entry<String, Term> entry : ((Structure) getTerm()).getFields().entrySet()) {
        //    String key = entry.getKey();
        //    Object value = typeInterop.protonToTypeScript(entry.getValue());
        //    map.put(key, value);
        //}
        return ProxyObject.fromMap(map);
    }

    private Object __get(Value... args) {
        return get(args[0].asString());
    }

    private Object __toJSON(Value... args) {
        return toJSON();
    }

    @Override
    protected Object ionGetMember(String key) {
        return switch (key) {
            case "get" -> (ProxyExecutable) (this::__get);
            case "toJSON" -> (ProxyExecutable) (this::__toJSON);
            case "setAnnotations" ->
                    throw new UnsupportedOperationException("Annotations of ion.ReadOnlyStructWrapper" +
                            " cannot be modified");
            default -> {
                Object value = get(key);
                if (value instanceof ContainerProxy) {
                    ((ContainerProxy) value).setParent(this);
                }
                yield value;
            }
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
            case GET_ANNOTATIONS_METHOD, GET_ION_TYPE_METHOD, ION_EQUALS_METHOD -> true;
            default -> ionHasMember(key);
        };
    }

    @Override
    protected boolean ionHasMember(String key) {
        return protonStruct.getFields().containsKey(key) || key.equals("get") || key.equals("toJSON");
    }

    protected String[] ionAnnotations() {
        return protonStruct.annotations();
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