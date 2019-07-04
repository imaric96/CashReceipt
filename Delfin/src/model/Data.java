package model;

public class Data {
	String naziv;
	int pib;
	String adresa;
	String grad;
	String telefon;
	int broj_isecka;
	int broj_gotovinskog;
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
	public String getTelefon() {
		return telefon;
	}
	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}
	public int getBroj_isecka() {
		return broj_isecka;
	}
	public void setBroj_isecka(int broj_isecka) {
		this.broj_isecka = broj_isecka;
	}
	public int getBroj_gotovinskog() {
		return broj_gotovinskog;
	}
	public void setBroj_gotovinskog(int broj_gotovinskog) {
		this.broj_gotovinskog = broj_gotovinskog;
	}
	public Data(String naziv, int pib, String adresa, String grad, String telefon, int broj_isecka,
			int broj_gotovinskog) {
		super();
		this.naziv = naziv;
		this.pib = pib;
		this.adresa = adresa;
		this.grad = grad;
		this.telefon = telefon;
		this.broj_isecka = broj_isecka;
		this.broj_gotovinskog = broj_gotovinskog;
	}
	public String toString() {
		return naziv + ";" + pib + ";" + adresa + ";" + grad + ";" + telefon + ";" + broj_isecka + ";" + broj_gotovinskog;
	}
	
}
