package com.mc;

public class Items{
	Double hours ;
	Double rate;
	Double tax_rate;
	Double brutto;
	Double tax;
	Double netto;
	
	public Double getBrutto() {
		return this.brutto;
	}
	public void setBrutto(Double brutto) {
		this.brutto = brutto;
	}
	public Double getHours() {
		return this.hours;
	}
	public void setHours(Double hours) {
		this.hours = hours;
	}
	public Double getNetto() {
		return this.netto;
	}
	public void setNetto(Double netto) {
		this.netto = netto;
	}
	public Double getRate() {
		return this.rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public Double getTax() {
		return this.tax;
	}
	public void setTax(Double tax) {
		this.tax = tax;
	}
	public Double getTax_rate() {
		return this.tax_rate;
	}
	public void setTax_rate(Double tax_rate) {
		this.tax_rate = tax_rate;
	}
	public void compute(){
		Double hours = this.hours;
		this.netto = hours * this.rate;
		this.brutto = this.netto * this.tax_rate;
		this.tax = this.brutto - this.netto ;
	}
}