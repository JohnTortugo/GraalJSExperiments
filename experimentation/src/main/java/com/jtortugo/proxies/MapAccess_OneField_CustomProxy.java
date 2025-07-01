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
    private final Map<String, Object> fields;

    public MapAccess_OneField_CustomProxy(int field1) {
    	this.fields = new HashMap<>(4); 
    	this.fields.put("field1", field1); 
	}
    
    @ExportMessage
    Object readMember(String member) throws UnsupportedMessageException, UnknownIdentifierException {
    	return read(member);
    }

    @ExportMessage
    void writeMember(String member, Object value) throws UnsupportedMessageException {
    	write(member, value);
    }
    
    @TruffleBoundary
    Object read(String member) {
    	return this.fields.get(member);
    }
    
    @TruffleBoundary
    void write(String member, Object value) {
    	this.fields.put(member, value);
    }
    
    @ExportMessage final Object getMembers(boolean includeInternal) throws UnsupportedMessageException { return this.fields.keySet(); }
    @ExportMessage final boolean hasMembers() { return true; }
    @ExportMessage final boolean isMemberReadable(String member) { return true; }
    @ExportMessage final boolean isMemberModifiable(String member) { return true; }
    @ExportMessage final boolean isMemberInsertable(String member) { return true; }
}