package model;

public class Racun {
	String naziv;
	double kolicina;
	double cena;
	int pdv;
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public double getKolicina() {
		return kolicina;
	}
	public void setKolicina(double kolicina) {
		this.kolicina = kolicina;
	}
	public double getCena() {
		return cena;
	}
	public void setCena(double cena) {
		this.cena = cena;
	}
	public int getPdv() {
		return pdv;
	}
	public void setPdv(int pdv) {
		this.pdv = pdv;
	}
	public Racun(String naziv, double kolicina, double cena, int pdv) {
		super();
		this.naziv = naziv;
		this.kolicina = kolicina;
		this.cena = cena;
		this.pdv = pdv;
	}
	public String toString() {
		return naziv + ";" + kolicina + ";" + cena + ";" + pdv;
	}
	
}
