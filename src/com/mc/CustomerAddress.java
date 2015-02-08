package com.mc;


public class CustomerAddress {
	String CustName;
	String CustAdress;
	String CustZip;
	String CustTown;
	
	/* constructor empty for now */
    public CustomerAddress() { // empty be defaukt
	}
    
    public CustomerAddress(String CustName, String CustAddress, String CustZip, String CustTown){
    	this.CustName = CustName;
    	this.CustAdress = CustAddress;
    	this.CustZip = CustZip;
    	this.CustTown = CustTown;
    }
    
	public String getCustAdress() {
		return this.CustAdress;
	}
	public void setCustAdress(String custAdress) {
		this.CustAdress = custAdress;
	}
	public String getCustName() {
		return this.CustName;
	}
	public void setCustName(String custName) {
		this.CustName = custName;
	}
	public String getCustTown() {
		return "Düsseldorf";
	}
	public void setCustTown(String custTown) {
		this.CustTown = custTown;
	}
	public String getCustZip() {
		return this.CustZip;
	}
	public void setCustZip(String custZip) {
		this.CustZip = custZip;
	}
}
