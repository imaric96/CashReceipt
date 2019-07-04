package view;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import actions.Print;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Customer;
import model.Racun;

public class GotovinskiRacun extends Stage {
	public ObservableList<Customer> databaseTemp = FXCollections.observableList(Frame.dataBase);
	public FilteredList<Customer> filterList = new FilteredList<>(databaseTemp);
	public TextField tfNaziv;
	public TextField tfPIB;
	public TextField tfAdresa;
	public TextField tfGrad;
	public TextField tfSuma;
	public TextField tfPDV;
	public TableView<Customer> tb;
	public Button btnStampaj;
	public Button btnOcisti;
	public Button btnObrisi;
	public boolean dialogGotovinski = false;
    public static String favicon = "/images/bill.png";
    
	public GotovinskiRacun() {
		getIcons().add(new Image(getClass().getResourceAsStream(favicon)));
		setResizable(false);
		btnStampaj = new Button("Štampaj");
		btnOcisti = new Button("Očisti");
		btnObrisi = new Button("Obriši");
		tfNaziv = new TextField();
		tfNaziv.setMaxSize(300, 40);
		tfNaziv.setMinHeight(40);
		tfNaziv.setPromptText("Naziv");
		tfPIB = new TextField();
		tfPIB.setMaxSize(300, 40);
		tfPIB.setMinHeight(40);
		tfPIB.setPromptText("PIB");
	    tfAdresa = new TextField();
		tfAdresa.setMaxSize(300, 40);
		tfAdresa.setMinHeight(40);
		tfAdresa.setPromptText("Adresa");
		tfGrad = new TextField();
		tfGrad.setMaxSize(300, 40);
		tfGrad.setMinHeight(40);
		tfGrad.setPromptText("Grad");
		tfSuma = new TextField();
		tfSuma.setMaxSize(300, 40);
		tfSuma.setMinHeight(40);
		tfSuma.setPromptText("Suma");
		tfSuma.setEditable(false);
		tfPDV = new TextField();
		tfPDV.setMaxSize(300, 40);
		tfPDV.setMinHeight(40);
		tfPDV.setPromptText("PDV");
		tfPDV.setEditable(false);
		tb = new TableView<>();
		btnStampaj.setMinSize(100, 40);
		btnOcisti.setMinSize(100, 40);
		btnObrisi.setMinSize(100, 40);
		TableColumn<Customer, String> cNaziv = new TableColumn<>("Naziv");
		cNaziv.setCellValueFactory(new PropertyValueFactory<>("naziv"));
		
		TableColumn<Customer, Integer> cPib = new TableColumn<>("PIB");
		cPib.setCellValueFactory(new PropertyValueFactory<>("pib"));
		
		TableColumn<Customer, String> cAdresa = new TableColumn<>("Adresa");
		cAdresa.setCellValueFactory(new PropertyValueFactory<>("adresa"));
		
		TableColumn<Customer, String> cGrad = new TableColumn<>("Grad");
		cGrad.setCellValueFactory(new PropertyValueFactory<>("grad"));
		
		TableColumn<Customer, Double> cSuma = new TableColumn<>("Suma (RSD)");
		cSuma.setCellValueFactory(new PropertyValueFactory<>("suma"));
		
		TableColumn<Customer, Double> cPdv = new TableColumn<>("PDV 20% (RSD)");
		cPdv.setCellValueFactory(new PropertyValueFactory<>("pdv"));
		tb.getColumns().addAll(cNaziv,cPib,cAdresa,cGrad,cSuma,cPdv);
		tb.setItems(filterList);
		tb.setColumnResizePolicy((param) -> true);
		Platform.runLater(() -> customResize(tb));

		//AKCIJE	
		
		btnObrisi.setOnAction(p->{
	
			if(filterList.isEmpty() == true || tfNaziv.getText().trim().equals("") || tfPIB.getText().trim().equals("") || tfAdresa.getText().trim().equals("") || tfGrad.getText().trim().equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Greška");
				alert.setHeaderText("Izaberite kupca iz liste!");
				alert.setContentText("Neophodno je da se izabere kupac iz liste da bi mogli da ga obrišete. Ako je korisnik izabran, popuniće vam se sva polja iznad i tako ćete znati da se dobro odabrali korisnika.");
				alert.showAndWait();
			}
			else {
			  if(filterList.size()==1) {
				  	Alert alertDelete = new Alert(AlertType.WARNING, "Ukoliko obrišete kupca, izgubićete SUMU i PDV. Nakon brisanja moguće je napraviti nakonadno istog kupca.");
				  	ButtonType da = new ButtonType("Da", ButtonBar.ButtonData.OK_DONE);
				  	ButtonType ne = new ButtonType("Ne", ButtonBar.ButtonData.CANCEL_CLOSE);
	     		 
	     		    
	     		    alertDelete.getButtonTypes().clear();
	     		    alertDelete.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
	     		    Button yesButton = (Button) alertDelete.getDialogPane().lookupButton( ButtonType.YES );
	     		    yesButton.setDefaultButton( false );
	     		    yesButton.setText("Da");
	     		    //Activate Defaultbehavior for no-Button:
	     		    Button noButton = (Button) alertDelete.getDialogPane().lookupButton( ButtonType.NO );
	     		    noButton.setDefaultButton( true );
	     		    noButton.setText("Ne");
	     		    
	     		    alertDelete.setTitle("Preventivni dialog");
	     		 	alertDelete.setHeaderText("Da li želite da obrišete izabranog kupca?");
	     		 	Image image = new Image(getClass().getResource("/images/delete.png").toExternalForm());
	     		 	ImageView imageView = new ImageView(image);
	     		 	alertDelete.setGraphic(imageView);
	     		 	Optional<ButtonType> result = alertDelete.showAndWait();
	     			 if (result.get() == ButtonType.YES) {

	 					for(Customer c: Frame.getInstance().getDataBase()) {
	 						if(c.getPib() == filterList.get(0).getPib()) {
	 							Frame.getInstance().getDataBase().remove(c);
	 							break;
	 						}						
	 					}
	 					try {
	 						File f = new File(Frame.getInstance().getPathDatabase().toUri());
	 						BufferedWriter writer = new BufferedWriter(new FileWriter(f));
	 				        for (Customer c : Frame.getInstance().getDataBase()) {
	 				        	String str = c.getNaziv()+";"+c.getPib()+";"+c.getAdresa()+";"+c.getGrad()+";"+c.getSuma()+";"+c.getPdv();
	 				            writer.write(str);
	 				            writer.newLine();
	 				        }
	 				        writer.close();
	 					} catch (Exception e) {
	 						// TODO: handle exception
	 					}
	        		 }
	        		 if (result.get() == ButtonType.NO) {
		 	     	    	alertDelete.hide();
	        		 }
	        		 hide();
				}
			}
		});
		tfPIB.textProperty().addListener((observable, oldValue, newValue) -> {
		    if(!newValue.matches("[0-9]*")){
		        tfPIB.setText(oldValue);
		    }
		});	  
		
		tb.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	Customer k = tb.getSelectionModel().getSelectedItem();
		    	
    	Platform.runLater(new Runnable() {
    	     public void run() {
				tb.getSelectionModel().clearSelection();
				tfNaziv.setText(k.getNaziv());
		    	tfGrad.setText(k.getGrad());
		    	tfPIB.setText(String.valueOf(k.getPib()));			
		    	tfAdresa.setText(k.getAdresa());
		    	tfGrad.setText(k.getGrad());
		    	tfSuma.setText(String.valueOf(k.getSuma()));
		    	tfPDV.setText(String.valueOf(k.getPdv()));	
				tb.setColumnResizePolicy((param) -> true );
				Platform.runLater(() -> customResize(tb));			
    	}});
		    }
		});

		btnStampaj.setOnAction(p->{
				if(filterList.isEmpty() == true || tfNaziv.getText().trim().equals("") || tfPIB.getText().trim().equals("") || tfAdresa.getText().trim().equals("") || tfGrad.getText().trim().equals("")) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Upozorenje");
					alert.setHeaderText("Popunite sva polja!");
					alert.setContentText("Neophodno je da se popune sva polja za štampanje gotovinskog računa. U slučaju da korisnik postoji već u bazi, izaberite iz tabele željenog kupca. Ako korisnik ne postoji, biće automatski unet u bazu nakon popunjavanja svih polja. SUMA I PDV se popunjavaju automatski pri pravljenju svakog gotovinskog racuna.");
					alert.showAndWait();
				}
				else {
					if(tfSuma.getText().trim().equals(""))
						tfSuma.setText("0");
					if(tfPDV.getText().trim().equals(""))
						tfPDV.setText("0");
					boolean postoji = false;					
					for(Customer k : databaseTemp){
						if(tfPIB.getText().trim().equals(String.valueOf(k.getPib()).trim()))
							postoji = true;
					}
					String naziv = tfNaziv.getText();
					String adresa = tfAdresa.getText();
					String grad = tfGrad.getText();
					naziv = naziv.substring(0, 1).toUpperCase() + naziv.substring(1);
					adresa = adresa.substring(0, 1).toUpperCase() + adresa.substring(1);
					grad = grad.substring(0, 1).toUpperCase() + grad.substring(1);
			        Customer customer = new Customer(naziv, Integer.parseInt(tfPIB.getText()), adresa, grad,Double.parseDouble( tfSuma.getText()), Double.parseDouble(tfPDV.getText()));
					
			        if(postoji == false) {
						   File f = new File(Frame.getInstance().getPathDatabase().toUri());
					        try {
					        		new Print(customer);	
					        		double suma=0.00;
					        		
					        		for(Racun r : Frame.getInstance().getDataRacun()) {
					        			suma+=r.getKolicina()*r.getCena();
					        		}
					        		double osnovica = suma/1.2;
					        	    double pdv = suma-osnovica;
					        	    customer.setSuma(suma);
					        	    customer.setPdv(pdv);
					        	    
							        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
									Frame.getInstance().getDataBase().add(customer);
							        for (Customer c : Frame.getInstance().getDataBase()) {
							        	String str = c.getNaziv()+";"+c.getPib()+";"+c.getAdresa()+";"+c.getGrad()+";"+c.getSuma()+";"+c.getPdv();
							            writer.write(str);
							            writer.newLine();
							        }
					
							        writer.close();
							} catch (Exception e) {

							}
					    
					}
					else {
						new Print(customer);
						
						   File f = new File(Frame.getInstance().getPathDatabase().toUri());
					        try {
					        		new Print(customer);	
					        		double suma=0.00;					        		
					        		for(Racun r : Frame.getInstance().getDataRacun()) {
					        			suma+=r.getKolicina()*r.getCena();
					        		}
					        		double osnovica = suma/1.2;
					        	    double pdv = suma-osnovica;
					        	    
							        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
							        for (Customer c : Frame.getInstance().getDataBase()) {
							        	if(tfPIB.getText().trim().equals(String.valueOf(c.getPib()).trim())) {
							        	    c.setNaziv(customer.getNaziv());
							        	    c.setAdresa(customer.getAdresa());
							        	    c.setGrad(customer.getGrad());
							        		c.setSuma(suma+customer.getSuma());
							        	    c.setPdv(pdv+customer.getPdv());
							        	}
							        	String str = c.getNaziv()+";"+c.getPib()+";"+c.getAdresa()+";"+c.getGrad()+";"+c.getSuma()+";"+c.getPdv();
							            writer.write(str);
							            writer.newLine();
							        }
									tb.setItems(databaseTemp);
									tb.refresh();
							        writer.close();
							} catch (Exception e) {

							}
					}
			        Frame.getInstance().getDataRacun().clear();
					hide();	
				}
		});
		filterList.predicateProperty().bind(Bindings.createObjectBinding(() ->
	    kupac -> kupac.getNaziv().toLowerCase().contains(tfNaziv.getText().toLowerCase()) &&
	    String.valueOf(kupac.getPib()).toLowerCase().contains(tfPIB.getText().toLowerCase()) &&
	    kupac.getAdresa().toLowerCase().contains(tfAdresa.getText().toLowerCase()) &&
	    kupac.getGrad().toLowerCase().contains(tfGrad.getText().toLowerCase()) &&
	    String.valueOf(kupac.getSuma()).toLowerCase().contains(tfSuma.getText().toLowerCase()) &&
	    String.valueOf(kupac.getPdv()).toLowerCase().contains(tfPDV.getText().toLowerCase()),
	    
	    tfNaziv.textProperty(),
	    tfPIB.textProperty(),
	    tfAdresa.textProperty(),
	    tfGrad.textProperty(),
	    tfSuma.textProperty(),
	    tfPDV.textProperty()
		));
		btnOcisti.setOnAction(p->{
		    tfNaziv.clear();
		    tfPIB.clear();
		    tfAdresa.clear();
		    tfGrad.clear();
		    tfSuma.clear();
		    tfPDV.clear();

		});
		filterList.addListener((ListChangeListener)(c -> {
			
			if(filterList.isEmpty()) {
				boolean unikat = true;
				for(Customer k : databaseTemp){
					if(tfPIB.getText().trim().equals(String.valueOf(k.getPib()).trim()))
						unikat = false;	
				}
				if(unikat == true)
				    tfPIB.setEditable(true);
				else
				    tfPIB.setEditable(false);
			}
			else
			    tfPIB.setEditable(false);
			if(tfNaziv.getText().trim().equals("") || tfPIB.getText().trim().equals("") || tfAdresa.getText().trim().equals("") || tfGrad.getText().trim().equals("") || tfSuma.getText().trim().equals("") || tfPDV.getText().trim().equals(""))
				 tfPIB.setEditable(true);
			
		}));
		
		addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
		        if (KeyCode.ESCAPE == event.getCode() || KeyCode.DELETE == event.getCode()) {	        	
						if(tfNaziv.getText().trim().equals("") == false || tfPIB.getText().trim().equals("") == false || tfAdresa.getText().trim().equals("") == false || tfGrad.getText().trim().equals("") == false || tfSuma.getText().trim().equals("")  == false || tfPDV.getText().trim().equals("")  == false ) {			
				    	    tfNaziv.clear();
				    	    tfPIB.clear();
				    	    tfAdresa.clear();
				    	    tfGrad.clear();
				    	    tfSuma.clear();
				    	    tfPDV.clear();
						}
						else {
							hide();
						}
		        }
		 });
		addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
			if (KeyCode.ENTER == event.getCode()) {
				if(dialogGotovinski == false){
					
					if(filterList.isEmpty() == true || tfNaziv.getText().trim().equals("") || tfPIB.getText().trim().equals("") || tfAdresa.getText().trim().equals("") || tfGrad.getText().trim().equals("")) {
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Upozorenje");
						alert.setHeaderText("Popunite sva polja!");
						alert.setContentText("Neophodno je da se popune sva polja za štampanje gotovinskog računa. U slučaju da korisnik postoji već u bazi, izaberite iz tabele željenog kupca. Ako korisnik ne postoji, biće automatski unet u bazu nakon popunjavanja svih polja. SUMA I PDV se popunjavaju automatski pri pravljenju svakog gotovinskog racuna.");
						alert.showAndWait();
					}
					else {
						if(tfSuma.getText().trim().equals(""))
							tfSuma.setText("0");
						if(tfPDV.getText().trim().equals(""))
							tfPDV.setText("0");
						boolean postoji = false;					
						for(Customer k : databaseTemp){
							if(tfPIB.getText().trim().equals(String.valueOf(k.getPib()).trim()))
								postoji = true;
						}
						String naziv = tfNaziv.getText();
						String adresa = tfAdresa.getText();
						String grad = tfGrad.getText();
						naziv = naziv.substring(0, 1).toUpperCase() + naziv.substring(1);
						adresa = adresa.substring(0, 1).toUpperCase() + adresa.substring(1);
						grad = grad.substring(0, 1).toUpperCase() + grad.substring(1);
				        Customer customer = new Customer(naziv, Integer.parseInt(tfPIB.getText()), adresa, grad,Double.parseDouble( tfSuma.getText()), Double.parseDouble(tfPDV.getText()));
						
				        if(postoji == false) {
							   File f = new File(Frame.getInstance().getPathDatabase().toUri());
						        try {
						        		new Print(customer);	
						        		double suma=0.00;
						        		
						        		for(Racun r : Frame.getInstance().getDataRacun()) {
						        			suma+=r.getKolicina()*r.getCena();
						        		}
						        		double osnovica = suma/1.2;
						        	    double pdv = suma-osnovica;
						        	    customer.setSuma(suma);
						        	    customer.setPdv(pdv);
						        	    
								        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
										Frame.getInstance().getDataBase().add(customer);
								        for (Customer c : Frame.getInstance().getDataBase()) {
								        	String str = c.getNaziv()+";"+c.getPib()+";"+c.getAdresa()+";"+c.getGrad()+";"+c.getSuma()+";"+c.getPdv();
								            writer.write(str);
								            writer.newLine();
								        }
						
								        writer.close();
								} catch (Exception e) {

								}
						    
						}
						else {
							new Print(customer);
							
							   File f = new File(Frame.getInstance().getPathDatabase().toUri());
						        try {
						        		new Print(customer);	
						        		double suma=0.00;					        		
						        		for(Racun r : Frame.getInstance().getDataRacun()) {
						        			suma+=r.getKolicina()*r.getCena();
						        		}
						        		double osnovica = suma/1.2;
						        	    double pdv = suma-osnovica;
						        	    
								        BufferedWriter writer = new BufferedWriter(new FileWriter(f));
								        for (Customer c : Frame.getInstance().getDataBase()) {
								        	if(tfPIB.getText().trim().equals(String.valueOf(c.getPib()).trim())) {
								        	    c.setNaziv(customer.getNaziv());
								        	    c.setAdresa(customer.getAdresa());
								        	    c.setGrad(customer.getGrad());
								        		c.setSuma(suma+customer.getSuma());
								        	    c.setPdv(pdv+customer.getPdv());
								        	}
								        	String str = c.getNaziv()+";"+c.getPib()+";"+c.getAdresa()+";"+c.getGrad()+";"+c.getSuma()+";"+c.getPdv();
								            writer.write(str);
								            writer.newLine();
								        }
										tb.setItems(databaseTemp);
										tb.refresh();
								        writer.close();
								} catch (Exception e) {

								}
						}
				        Frame.getInstance().getDataRacun().clear();
						hide();	
					}	
					dialogGotovinski = true;
				}
		        else 
		        	dialogGotovinski = false;
		}
	 });
	
		//AKCIJE
		HBox hbb = new HBox(10);
		hbb.setAlignment(Pos.CENTER);
		hbb.getChildren().addAll(tfAdresa,tfGrad,tfSuma,tfPDV);
		
		HBox hba = new HBox(10);
		hba.setAlignment(Pos.CENTER);
		hba.getChildren().addAll(tfNaziv,tfPIB);
		
		HBox hbc = new HBox(10);
		hbc.setAlignment(Pos.BOTTOM_CENTER);
		hbc.getChildren().addAll(btnStampaj,btnOcisti,btnObrisi);
		
		VBox vb = new VBox(10);
		vb.setAlignment(Pos.CENTER);
		vb.getChildren().addAll(hba,hbb,tb,hbc);

		Scene sc = new Scene(vb,900,565);
		setScene(sc);		
		show();	
	}
	
	public void customResize(TableView<?> view) {	
        AtomicLong width = new AtomicLong();
        view.getColumns().forEach(col -> {
            width.addAndGet((long) col.getWidth());
        });
        double tableWidth = view.getWidth();

        if (tableWidth > width.get()) {
            view.getColumns().forEach(col -> {
                col.setPrefWidth(col.getWidth()+((tableWidth-width.get())/view.getColumns().size()));
            });
        }
    }

	public TextField getTfNaziv() {
		return tfNaziv;
	}

	public void setTfNaziv(TextField tfNaziv) {
		this.tfNaziv = tfNaziv;
	}

	public ObservableList<Customer> getDatabaseTemp() {
		return databaseTemp;
	}

	public void setDatabaseTemp(ObservableList<Customer> databaseTemp) {
		this.databaseTemp = databaseTemp;
	}

	public TextField getTfPIB() {
		return tfPIB;
	}

	public void setTfPIB(TextField tfPIB) {
		this.tfPIB = tfPIB;
	}

	public TextField getTfAdresa() {
		return tfAdresa;
	}

	public void setTfAdresa(TextField tfAdresa) {
		this.tfAdresa = tfAdresa;
	}

	public TextField getTfGrad() {
		return tfGrad;
	}

	public void setTfGrad(TextField tfGrad) {
		this.tfGrad = tfGrad;
	}

	public TableView<Customer> getTb() {
		return tb;
	}

	public void setTb(TableView<Customer> tb) {
		this.tb = tb;
	}
}