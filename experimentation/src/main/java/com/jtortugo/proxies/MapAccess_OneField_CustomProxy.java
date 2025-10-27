package com.jtortugo.proxies;

import java.util.HashMap;
import java.util.Map;

import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.interop.InteropLibrary;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnknownIdentifierException;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.library.ExportLibrary;
import com.oracle.truffle.api.library.ExportMessage;

@ExportLibrary(InteropLibrary.class)
public class MapAccess_OneField_CustomProxy implements TruffleObject {
    private final String term;
    private final String[] annotations;
    private final Map<String, Object> fields;

    public MapAccess_OneField_CustomProxy(int field1) {
    	this.fields = new HashMap<>(4); 
    	this.fields.put("field1", field1);
        this.term = "CesarTerms";
        this.annotations = new String[] { "Annot1", "Annot2", "Annot3" };
	}
    
    @ExportMessage
    Object readMember(String member) throws UnsupportedMessageException, UnknownIdentifierException {
        if (member.equals("term")) {
            return this.term;
        } else if (member.equals("annotations")) {
            return this.annotations;
        } else {
            return read(member);
        }
    }

    @ExportMessage
    void writeMember(String member, Object value) throws UnsupportedMessageException {
    	write(member, value);
    }

    public String[] getAnnotations() {
        return this.annotations;
    }

    @TruffleBoundary(allowInlining = true)
    Object read(String member) {
    	return this.fields.get(member);
    }
    
    @TruffleBoundary(allowInlining = true)
    void write(String member, Object value) {
    	this.fields.put(member, value);
    }
    
    @ExportMessage final Object getMembers(boolean includeInternal) throws UnsupportedMessageException { return this.fields.keySet(); }
    @ExportMessage final boolean hasMembers() { return true; }
    @ExportMessage final boolean isMemberReadable(String member) { return true; }
    @ExportMessage final boolean isMemberModifiable(String member) { return true; }
    @ExportMessage final boolean isMemberInsertable(String member) { return true; }
}