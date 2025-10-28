package com.jtortugo.reproxy;


import lombok.AccessLevel;
import lombok.Getter;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyExecutable;
import org.graalvm.polyglot.proxy.ProxyObject;

import com.jtortugo.reproxy.proton.Term;

/**
 * Base class for Graal proxy objects representing ion objects.
 */
public abstract class ProxyTerm implements ProxyObject {

    @Getter(AccessLevel.PROTECTED)
    private Value newAnnotations;

    protected static final String GET_ANNOTATIONS_METHOD = "getAnnotations";
    protected static final String SET_ANNOTATIONS_METHOD = "setAnnotations";
    protected static final String GET_ION_TYPE_METHOD = "getIonType";
    protected static final String ION_EQUALS_METHOD = "ionEquals";

    protected final TypeScriptIonInterop typeInterop;

    protected ProxyTerm(TypeScriptIonInterop typeInterop) {
        this.typeInterop = typeInterop;
    }
    abstract Term getTerm();
    abstract String getIonType();

    protected String[] getAnnotationsForProton() {
        String[] newAnnotations = null;
        if (this.newAnnotations != null) {
            newAnnotations = new String[(int) this.newAnnotations.getArraySize()];
            for (int i = 0; i < newAnnotations.length; i++) {
                newAnnotations[i] = this.newAnnotations.getArrayElement(i).asString();
            }
            return newAnnotations;
        } else {
            return ionAnnotations();
        }
    }

    @Override
    public boolean hasMember(String key) {
        return switch (key) {
            case GET_ANNOTATIONS_METHOD, SET_ANNOTATIONS_METHOD, GET_ION_TYPE_METHOD, ION_EQUALS_METHOD -> true;
            default -> ionHasMember(key);
        };
    }
    protected abstract boolean ionHasMember(String key);

    private Object __getAnnotations(Value... args) {
        return newAnnotations == null
                ? typeInterop.protonAnnotationsToTypeScript(ionAnnotations())
                : newAnnotations;
    }

    private Object __setAnnotations(Value... args) {
        if (args[0] != null && args[0].hasArrayElements()) {
            newAnnotations = args[0];
        }
        return null;
    }

    private Object __getIonType(Value... args) {
        return getIonType();
    }

    private Object __ionEquals(Value... args) {
        return getTerm().equals(typeInterop.typeScriptToProton(args[0]));
    }

    @Override
    public Object getMember(String key) {
        return switch (key) {
            case GET_ANNOTATIONS_METHOD -> (ProxyExecutable) (this::__getAnnotations);
            case SET_ANNOTATIONS_METHOD -> (ProxyExecutable) (this::__setAnnotations);
            case GET_ION_TYPE_METHOD -> (ProxyExecutable) (this::__getIonType);
            case ION_EQUALS_METHOD -> (ProxyExecutable) (this::__ionEquals);
            default -> ionGetMember(key);
        };
    }

    protected abstract String[] ionAnnotations();
    protected abstract Object ionGetMember(String key);
}
