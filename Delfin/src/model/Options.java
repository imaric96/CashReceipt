package model;

import java.nio.file.Path;

public class Options {
	
	String naziv;
	Path path;
	long vreme;

	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	public Path getPath() {
		return path;
	}
	public void setPath(Path path) {
		this.path = path;
	}
	public long getVreme() {
		return vreme;
	}
	public void setVreme(long vreme) {
		this.vreme = vreme;
	}
	public Options(String naziv, Path path, long vreme) {
		super();
		this.naziv = naziv;
		this.path = path;
		this.vreme = vreme;
	}
	public String toString() {
		return naziv + ";" + path + ";" + vreme;
	}

}
