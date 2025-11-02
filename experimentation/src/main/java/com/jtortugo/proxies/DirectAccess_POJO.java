package com.jtortugo.proxies;

public class DirectAccess_POJO {
    public Integer field1 = 0;
    public Integer field2 = 0;
    public Integer field3 = 0;
    public Integer field4 = 0;

    public DirectAccess_POJO(int field1) {
    	this.field1 = field1;
    }
    
	public DirectAccess_POJO(int field1, int field2) {
    	this.field1 = field1;
    	this.field2 = field2;
    }
    
	public DirectAccess_POJO(int field1, int field2, int field3) {
		this.field1 = field1;
		this.field2 = field2;
		this.field3 = field3;
    }
    
    public DirectAccess_POJO(int field1, int field2, int field3, int field4) {
		this.field1 = field1;
		this.field2 = field2;
		this.field3 = field3;
		this.field4 = field4;
    }
}