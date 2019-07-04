package model;

public class Customer {
	String naziv;
	int pib;
	String adresa;
	String grad;
	double suma;
	double pdv;
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public int getPib() {
		return pib;
	}
	public void setPib(int pib) {
		this.pib = pib;
	}
	public String getAdresa() {
		return adresa;
	}
	public void setAdresa(String adresa) {
		this.adresa = adresa;
	}
	public String getGrad() {
		return grad;
	}
	public void setGrad(String grad) {
		this.grad = grad;
	}
	public double getSuma() {
		return suma;
	}
	public void setSuma(double suma) {
		this.suma = suma;
	}
	public double getPdv() {
		return pdv;
	}
	public void setPdv(double pdv) {
		this.pdv = pdv;
	}
	public Customer(String naziv, int pib, String adresa, String grad, double suma, double pdv) {
		super();
		this.naziv = naziv;
		this.pib = pib;
		this.adresa = adresa;
		this.grad = grad;
		this.suma = suma;
		this.pdv = pdv;
	}
	public String toString() {
		return naziv + ";" + pib + ";" + adresa + ";" + grad + ";" + suma + ";" + pdv;
	}	
}