package actions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Data;
import view.Frame;


public class Task extends TimerTask{
	public void run() {
		
		File folderLokacija = new File(Frame.getInstance().getOptions().get(0).getPath().toAbsolutePath().toString());
		File[] listFajlovaLokacija= folderLokacija.listFiles();
		File t = new File(Frame.getDataOptions().get(0).getPath()+"\\"+"GotovinskiRacuni"); 
		File[] tlistFajlovaLokacija= t.listFiles();
		File displejFile = null;
		boolean postoje=false;
		boolean uvecaj=false;
		boolean displej = false;


        for(File fajlovi : tlistFajlovaLokacija) {
        	if(fajlovi.isFile()) {
		    	if(fajlovi.getName().startsWith("ART_") || fajlovi.getName().startsWith("RAC_"))
		    		postoje=true;
		    	else 
		    		postoje = false;
        	}
        }
        if(postoje==true)
        	Frame.getInstance().getBtnGotovinski().setDisable(false);
        else
        	Frame.getInstance().getBtnGotovinski().setDisable(true);
        
         for(File fajl : listFajlovaLokacija) {
        	 if(fajl.getName().startsWith("ART_") || fajl.getName().startsWith("RAC_"))
        		 uvecaj = true;
        	 else
        		 uvecaj = false;
         }
         if(uvecaj == true) {
        	 try {
     			File f = new File(Frame.getInstance().getPathData().toUri());
    		    BufferedWriter writer = new BufferedWriter(new FileWriter(f));
               for (Data d : Frame.getInstance().getDataPermament()) {
	             	d.setBroj_gotovinskog(d.getBroj_gotovinskog()+1);
	             	d.setBroj_isecka(d.getBroj_isecka()+1);
	             	String str = d.getNaziv()+";"+d.getPib()+";"+d.getAdresa()+";"+d.getGrad()+";"+d.getTelefon()+";"+d.getBroj_isecka()+ ";"+ d.getBroj_gotovinskog();
	                 writer.write(str);
	                 writer.newLine();
             }
               writer.close();
               Frame.getDataPermament().clear();
               Frame.getInstance().LoadPermamentData();
			} catch (Exception e) {
				// TODO: handle exception
			}
         }
		try {
			//UZMI LOKACIJU
			for (File file : listFajlovaLokacija) {
			    if (file.isFile()) {
			    	if(file.getName().startsWith("ART_")) {
			    		File trenutniFajl = new File(file.getAbsolutePath());
				         DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
				         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
				         Document doc = docBuilder.parse(trenutniFajl);
				         Node cars = doc.getFirstChild();
				         NodeList list = doc.getElementsByTagName("DATA");
				        
				         for(int i=0; i<list.getLength();i++) {
				        	  Node trenutni = list.item(i);
				        	  NamedNodeMap attr = trenutni.getAttributes();
				           	  Node nodeAttr = attr.getNamedItem("MES");
				           	  if(nodeAttr.getTextContent().isEmpty())
				           		  nodeAttr.setTextContent("0");
				         }
				         
				 		// UPISIVANJE U XML FAJL
				 		TransformerFactory transformerFactory = TransformerFactory.newInstance();
				 		Transformer transformer = transformerFactory.newTransformer();
				 		DOMSource source = new DOMSource(doc);
				 		StreamResult result = new StreamResult(trenutniFajl);
				 		transformer.transform(source, result);
			
				 		System.out.println("Uspesno upisan XML Fajl!");
			    	}
			    }
			}
			
			//KOPIRANJE FAJLOVA SA JEDNOG MESTA NA DRUGO

			    InputStream inStream = null;
			    OutputStream outStream = null;
			    OutputStream outStreamGotovinski= null;
			    try {
			        File folderGotovinski = new File(Frame.getDataOptions().get(0).getPath()+"\\"+"GotovinskiRacuni"); 
			        File fajloviGotivnski[] = folderGotovinski.listFiles();
			    
			        if (!folderGotovinski.exists()) 
			        	folderGotovinski.mkdir();
			        
			        int br=0;
			        for(File fajlovi : listFajlovaLokacija) {
			        	if(fajlovi.isFile()) {
					    	if(fajlovi.getName().startsWith("ART_") || fajlovi.getName().startsWith("RAC_"))
					    		br++;
			        	}
			        }
			        if(br==2) {
		
				        for (File l: fajloviGotivnski){
				        	if (l.isFile()) {
						        File trenutiL = new File(l.getAbsolutePath());
						        trenutiL.delete();
						    	System.out.println("Fajl " + trenutiL.getName() + " uspesno obrisan!");
						      }
				        	else {
				        		System.out.println("prazno");
				        	}
				        }
			        }
					for (File file : listFajlovaLokacija) {
					    if (file.isFile()) {
					        File lokacija = new File(file.getAbsolutePath());
					        File destinacija = new File(Frame.dataOptions.get(1).getPath()+"\\"+file.getName());
					        File destinacijaZaGotovinski = new File(Frame.dataOptions.get(0).getPath()+"\\"+"GotovinskiRacuni" + "\\" +file.getName());
				    	
					        inStream = new FileInputStream(lokacija);	        
					        outStream = new FileOutputStream(destinacija);
					        outStreamGotovinski = new FileOutputStream(destinacijaZaGotovinski);
  
					        
					        byte[] buffer = new byte[1024];

					        int length;

					        while ((length = inStream.read(buffer)) > 0) {
					            outStream.write(buffer, 0, length);
					            outStreamGotovinski.write(buffer, 0, length);
					        }

					        inStream.close();
					        outStream.close();
				            outStreamGotovinski.close();
						    System.out.println("Fajl " + file.getName() + " uspesno kopiran!");
					    }
				
					}


					for (File file : listFajlovaLokacija) {
					    if (file.isFile()) {
					        File lokacija = new File(file.getAbsolutePath());
					    	lokacija.delete();
					    	System.out.println("Fajl " + file.getName() + " uspesno obrisan!");
					    }
					}

			    } catch(IOException e) {
			        e.printStackTrace();
			    }
	 		
		} catch (Exception ex) {
			System.out.println("error running thread " + ex.getMessage());
		}
		
	}

}
