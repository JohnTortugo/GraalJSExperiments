package com.jtortugo.reproxy;

import com.jtortugo.reproxy.proton.Term;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for Graal proxy objects representing containers (object/struct, array).
 */
public abstract class ContainerProxy extends ProxyTerm {

    @Getter(AccessLevel.PROTECTED)
    private boolean modified;

    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private ContainerProxy parent;

    protected ContainerProxy(TypeScriptIonInterop typeInterop) {
        super(typeInterop);
    }

    abstract Term getTerm();
    abstract String getIonType();

    protected void setModified() {
        if (modified) {
            return;
        }
        modified = true;
        //set modified for the parent if there is a parent
        if (parent != null) {
            parent.setModified();
        }
    }

    @Override
    public boolean hasMember(String key) {
        return switch (key) {
            case GET_ANNOTATIONS_METHOD, SET_ANNOTATIONS_METHOD, GET_ION_TYPE_METHOD, ION_EQUALS_METHOD -> true;
            default -> ionHasMember(key);
        };
    }

	public void setParent(ProtonReadOnlyStructWrapper protonReadOnlyStructWrapper) {
		// TODO Auto-generated method stub
		
	}
}