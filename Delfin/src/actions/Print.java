package actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.Customer;
import model.Data;
import model.Racun;
import view.Frame;

public class Print {
	
	public double suma;
	public double pdv;
	public Customer k;
	public DateTimeFormatter dtf;
	public LocalDate localDate;
    public Calendar cal;
    public SimpleDateFormat sdf;
    public File folder;
    
	public Print(Customer k) {
		this.k = k;
		dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		localDate = LocalDate.now();
        cal = Calendar.getInstance();
        sdf = new SimpleDateFormat("HH:mm");
		try {
	        folder = new File(Frame.dataOptions.get(0).getPath()+"\\"+"GotovinskiRacuni");
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
			    if (file.isFile()) {
			    	if(file.getName().startsWith("ART_")) {
			    		
			    	   	 File trenutniFajl = new File(file.getAbsolutePath());
				         DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				         Document doc = docBuilder.parse(trenutniFajl);
				         Node cars = doc.getFirstChild();
				         NodeList list = doc.getElementsByTagName("DATA");
			           	  String PAY = null;
				         for(int i=0; i<list.getLength();i++) {
				        	  Node trenutni = list.item(i);
				        	  NamedNodeMap attr = trenutni.getAttributes();
				           	  Node nodePLU = attr.getNamedItem("PLU");
				           	  Node nodeDECS = attr.getNamedItem("DESC");
				           	  String PLU = nodePLU.getTextContent().trim();
				           	  String DESC = nodeDECS.getTextContent().trim();

				  			for (File racun : listOfFiles) {
							    if (racun.isFile()) {
							    	if(racun.getName().startsWith("RAC_")) {
							    	   	 File trenutniRacun = new File(racun.getAbsolutePath());
								         DocumentBuilderFactory racFactory = DocumentBuilderFactory.newInstance();
								         DocumentBuilder racBuilder = racFactory.newDocumentBuilder();
								         Document rac = racBuilder.parse(trenutniRacun);
								         Node cars1 = rac.getFirstChild();
								         NodeList elementi = rac.getElementsByTagName("DATA");
								   
								         for(int j=0; j<elementi.getLength();j++) {
								        	  Node trenutniR = elementi.item(j);
								        	  NamedNodeMap atribut = trenutniR.getAttributes();
								           	  Node nodeAMN = null;
								           	  Node nodePLU1 = null;
								           	  Node nodePRC = null;
								           	  Node nodePAY = null;
								           	  String PLU1;
								           	  String AMN;
								           	  String PRC;
	           	
								           	  if(j<elementi.getLength()-1){
	     								       	nodePLU1 = atribut.getNamedItem("BCR");
								           		nodePRC = atribut.getNamedItem("PRC");
									           	nodeAMN = atribut.getNamedItem("AMN");
									           	
								           		PLU1 = nodePLU1.getTextContent().trim();	
								           		PRC = nodePRC.getTextContent().trim();			           		
								           		AMN = nodeAMN.getTextContent().trim();
								           		PAY = "";
								           	  }
								           	  else {
										        nodePAY = atribut.getNamedItem("PAY");
										      	nodeAMN = atribut.getNamedItem("AMN");
		
									           	PLU1 = "";
									           	PRC = "";
									           	AMN = nodeAMN.getTextContent().trim();
									           	PAY = nodePAY.getTextContent().trim();
								           	  }
								         
								           	 if(PLU.equals(PLU1)){
								           		DESC = DESC.replace("\"", "");
								           		Racun r = new Racun(DESC, Double.parseDouble(AMN),Double.parseDouble(PRC), 20);	
								           		Frame.getInstance().getDataRacun().add(r);								  
								           	 } 		
								         }

							    	}
							    }
				  			}      
				         }
				           sredi(PAY);
						}
			    	}			    
			}
		} catch (Exception ex) {
				System.out.println("error running thread " + ex.getMessage());
			}
	}
	public void sredi(String PAY) {
		DecimalFormat df = new DecimalFormat("##.00");
		String verzija = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
		String nacinPlacanja = null;
		String oznakePoc = "<NEFISKALNI_TEKST>\n"
				+ "	<DATA TXT=\"\n";
		String oznakeKraj = "\"/>\n</NEFISKALNI_TEKST>";
		String artikli = "";
		double cenaRacuna = 0;
		String razmak=" ";
		Data d = Frame.getInstance().getDataPermament().get(0);
		ArrayList<Racun> racun = Frame.getInstance().getDataRacun();

		for(Racun r: racun) {
			int duzinaRedaNaziva = r.getNaziv().length();
			artikli+=r.getNaziv();
			int duzinaNaziva= 32 - duzinaRedaNaziva;
			for(int i=0;i<duzinaNaziva-1;i++) {
				artikli+=razmak;
			}
			artikli+="\n";
			double ukupnaCenaArtikla = 0.00;	
			ukupnaCenaArtikla = r.getKolicina() * r.getCena();
			cenaRacuna+=ukupnaCenaArtikla;
			String artikal = df.format(r.getKolicina())+"   X   " + df.format(r.getCena()) + "     " + df.format(ukupnaCenaArtikla);
			int duzinaRedaCene = artikal.length();
			int duzinaCene = 32-duzinaRedaCene;
			for(int i=0;i<duzinaCene-1;i++){
				artikal+=razmak;
			}
			if(artikal.length()>32) {
				int duzina = artikal.length()-33;
				for(int i=0;i<duzina;i++) {
					artikal = artikal.replace(artikal.substring(artikal.length()-1), "");
				}
			}
			artikli+=artikal+"\n";
		}
		double osnovica = cenaRacuna/1.2;
		double pdv = cenaRacuna-osnovica;

		String zvezdice ="****************************";
		String nefiskalni = "      NEFISKALNI TEKST";
		String firma = "        "+d.getNaziv();
		String firmaAdresa = "    "+ d.getAdresa();
		String firmaGrad = "       11090 "+ d.getGrad();
		String firmaTelefon = "       "+d.getTelefon();
		String firmaPib = "	   PIB/" + d.getPib();
		
		String gotovinski = " Gotov. racun: 17-320-0"+ d.getBroj_gotovinskog();
		
		String kupac = "Kupac / "+k.getNaziv();
		String adresa ="Adresa / "+k.getAdresa();
		String grad = "Grad / "+k.getGrad();
		String pib = "PIB / "+k.getPib();
		String datumIzdavanja = "Datum izdavanja / "+ dtf.format(localDate);
		String brojFiskalnogIsecka = "Broj fiskalnog isecka / " + d.getBroj_isecka();					
		String mestoIzdavanja = "Mesto izdavanja / "+ d.getGrad();

		String tabelaArtikal = "Artikal";
		String tabelaVrednosti = "Kolicina    Cena    Vrednost";
		
		String ukupnoSaPdv = "Ukupno sa PDV / " + df.format(cenaRacuna);
		String osnovicaPodatak = "Osnovica / " + df.format(osnovica);
		String ukupnoPDV ="Ukupno PDV / " + df.format(pdv);
		
		String tabelaRekapitulacija = "Rekapitulacija poreza";
		String tabelaPorez = "Stopa     Osnovica      PDV";
		String tabelaPorezPodaci = df.format(racun.get(0).getPdv()) +"      "+ df.format(osnovica) + "    " + df.format(pdv);
				
		String robuIzdao = "Robu izdala / Dragana Krstic";
		String prazanRed = "";
		String MP ="            M.P";
		String tackice="............................";
		String crtice="----------------------------";
		
		String vreme = "     "+ dtf.format(localDate)+ " - " + sdf.format(cal.getTime());
		
		String[] podaci = {zvezdice,nefiskalni,firma,firmaAdresa,firmaGrad,
				           firmaTelefon,firmaPib,gotovinski,kupac,adresa,grad,
				           pib,datumIzdavanja,brojFiskalnogIsecka,mestoIzdavanja,tabelaArtikal,
				           tabelaVrednosti,ukupnoSaPdv,osnovicaPodatak, ukupnoPDV,tabelaRekapitulacija,
				           tabelaPorez,tabelaPorezPodaci,robuIzdao,prazanRed,MP,
				           tackice,crtice,vreme};
		
		
		for(int i=0;i<podaci.length;i++) {
			int duzinaRedaInformacija = podaci[i].length();
			int duzinaInformacije = 32-duzinaRedaInformacija;
			for(int j=0;j<duzinaInformacije-1;j++)
				podaci[i]+=razmak;
			if(podaci[i].length()>32) {
				int duzina = podaci[i].length()-33;
				for(int k=0;k<duzina;k++) {
					podaci[i] = podaci[i].replace(podaci[i].substring(podaci[i].length()-1), "");
				}
			}
			podaci[i]+="\n";
		};
		;
		zvezdice = podaci[0];
		nefiskalni = podaci[1];
		firma = podaci[2];
		firmaAdresa = podaci[3];
		firmaGrad = podaci[4];
		firmaTelefon = podaci[5];
		firmaPib = podaci[6].replace(podaci[6].substring(podaci[6].length()-4), "")+"\n";
//		firmaPib="	   PIB/" + d.getPib();
		gotovinski = podaci[7];
		kupac = podaci[8];
		adresa = podaci[9];
		grad = podaci[10];
		pib = podaci[11];
		datumIzdavanja = podaci[12];
		brojFiskalnogIsecka = podaci[13];
		mestoIzdavanja = podaci[14];
		tabelaArtikal = podaci[15];
		tabelaVrednosti = podaci[16];
		ukupnoSaPdv = podaci[17];
		osnovicaPodatak = podaci[18];
		ukupnoPDV = podaci[19];
		tabelaRekapitulacija = podaci[20];
		tabelaPorez = podaci[21];
		tabelaPorezPodaci = podaci[22];;
		robuIzdao = podaci[23];
		prazanRed = podaci[24];
		MP = podaci[25];
		tackice = podaci[26];
		crtice = podaci[27];
		vreme = podaci[28];

		String data = verzija
				+ oznakePoc
				+ zvezdice
				+ nefiskalni
				+ zvezdice
				+ firma
				+ firmaAdresa
				+ firmaGrad
			    + firmaTelefon
			    + firmaPib
				+ crtice
				+ gotovinski
				+ crtice
				+ kupac
				+ adresa	
				+ grad	
			    + pib				
				+ crtice
				+ datumIzdavanja
				+ brojFiskalnogIsecka
				+ mestoIzdavanja
				+ crtice
			    + tabelaArtikal
			    + tabelaVrednosti
				+ tackice
			    + artikli
				+ crtice
			    + ukupnoSaPdv
			    + osnovicaPodatak
				+ ukupnoPDV
				+ crtice
			    + tabelaRekapitulacija
			    + tabelaPorez
				+ tackice
			    + tabelaPorezPodaci
				+ crtice
			    + robuIzdao
				+ crtice
			    + prazanRed	    
			    + MP
			    + prazanRed		    
				+ crtice
				+ zvezdice
				+ nefiskalni
				+ vreme
				+ zvezdice
				+ oznakeKraj;	
			try {
				NapraviFajl(data);
				data="";
				
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	public void NapraviFajl(String data) throws IOException {
		String content = data;
		String path = folder + "\\TXT_GOTOVINSKI.XML";
		File file = new File(path);   
	      if (file.createNewFile()){
		        System.out.println("Gotovinski racun je uspesno napravljen!");
			      
				Files.write(Paths.get(path), content.getBytes(), StandardOpenOption.CREATE);
				PosaljiNaStampu();
		      }else{
		          System.out.println("Fajl vec postoji!");
		    		if(file.delete()){
		    			System.out.println(file.getName() + " je obrisan!");
		    			file.createNewFile();
		    			Files.write(Paths.get(path), content.getBytes(), StandardOpenOption.CREATE);
		    			PosaljiNaStampu();
		    		}else{
		    			System.out.println("Delete operation is failed.");
		    		}		
		      }

	}
	public void PosaljiNaStampu() {
	//KOPIRANJE FAJLOVA SA JEDNOG MESTA NA DRUGO
			
			    InputStream inStream = null;
			    OutputStream outStream = null;
			    try {
			    	File folderLokacija = new File(Frame.dataOptions.get(0).getPath() + "\\" + "GotovinskiRacuni");
					File[] FajloviUnutra = folderLokacija.listFiles();
					for (File file : FajloviUnutra) {
					    if (file.isFile()) {
					    	if(file.getName().equals("TXT_GOTOVINSKI.XML")) {
						        File lokacija = new File(file.getAbsolutePath());
						        File destinacija = new File(Frame.dataOptions.get(1).getPath() + "\\" + file.getName());
						        System.out.println(lokacija.getAbsolutePath());
						        System.out.println(destinacija.getAbsolutePath());
						        inStream = new FileInputStream(lokacija);				        
						        outStream = new FileOutputStream(destinacija);
	
						        byte[] buffer = new byte[1024];
	
						        int length;
						        while ((length = inStream.read(buffer)) > 0) {
						            outStream.write(buffer, 0, length);
						        }
	
						        inStream.close();
						        outStream.close();	
							    System.out.println("Fajl " + file.getName() + " uspesno kopiran!");
					    	}
					    }			
					}
					for (File file : FajloviUnutra) {
					    if (file.isFile()) {
					        File lokacija = new File(file.getAbsolutePath());
					    	lokacija.delete();
					    	System.out.println("Fajl " + file.getName() + " uspesno obrisan!");
					    }
					}
			    } catch(IOException e) {
			        e.printStackTrace();
			    }
	}
}