package view;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Data;
import model.Options;

public class Settings extends Stage {
    public static String favicon = "/images/file.png";
    public TextField tfLokacija;
	public TextField tfProvera;
	public TextField tfOdrediste;
	public TextField tfNaziv;
	public TextField tfPib;
	public TextField tfAdresa;
	public TextField tfGrad;
	public TextField tfTelefon;
	public TextField tfBrojIsecka;
	public TextField tfBrojGotovinskog;
	
	public Settings() {
		getIcons().add(new Image(getClass().getResourceAsStream(favicon)));
		Image imgFile = new Image(getClass().getResourceAsStream("/images/file.png"));
		Label lblLokacija = new Label("Čitaj sa lokacije:");
		tfLokacija = new TextField();
		tfLokacija.setPromptText("Lokacija gde Pantheon stvara fajlove");
		Button btnLokacija = new Button();
		btnLokacija.setGraphic(new ImageView(imgFile));
		tfLokacija.setEditable(false);
		
		Label lblOdrediste = new Label("Piši na lokaciju:");
		tfOdrediste = new TextField();
		tfOdrediste.setPromptText("Lokacija odakle štampač čita fajlove");
		Button btnOdrediste = new Button();
		btnOdrediste.setGraphic(new ImageView(imgFile));
		tfOdrediste.setEditable(false);
		
		Label lblProvera = new Label("Proveravaj folder na:");
		tfProvera = new TextField();
		tfProvera.setPromptText("Preporuka je 10 ms");
		Label lblMs = new Label("ms");
		Button btnSacuvaj = new Button("Sačuvaj");
		
		Label lblNaziv = new Label("Naziv firme:");
		tfNaziv = new TextField();
		tfNaziv.setPromptText("Naziv fime za gotovinski:");
		
		Label lblPib = new Label("PIB firme:");
		tfPib = new TextField();
		tfPib.setPromptText("PIB firme koja izdaje gotovinski.");
		
		Label lblAdresa = new Label("Adresa firme:");
		tfAdresa = new TextField();
		tfAdresa.setPromptText("Lokacija firme");
		
		Label lblGrad = new Label("Grad firme:");
		tfGrad = new TextField();
		tfGrad.setPromptText("Grad u kome se nalazi firma");
		
		Label lblTelefon= new Label("Telefon firme:");
		tfTelefon = new TextField();
		tfTelefon.setPromptText("Kontakt telefon firme");
		
		Label lblBrojIsecka= new Label("Broj isečka:");
		tfBrojIsecka = new TextField();
		tfBrojIsecka.setPromptText("Broj isečka zadnjeg računa");
		
		Label lblBrojGotovinskog = new Label("Broj gotovinskog računa:");
		tfBrojGotovinskog = new TextField();
		tfBrojGotovinskog.setPromptText("Broj gotovin. računa u Pantheonu");
		
		tfProvera.textProperty().addListener((observable, oldValue, newValue) -> {
		    if(!newValue.matches("[0-9]*")){
		        tfProvera.setText(oldValue);
		    }
		});	    
		tfPib.textProperty().addListener((observable, oldValue, newValue) -> {
		    if(!newValue.matches("[0-9]*")){
		        tfPib.setText(oldValue);
		    }
		});	  
		tfBrojGotovinskog.textProperty().addListener((observable, oldValue, newValue) -> {
		    if(!newValue.matches("[0-9]*")){
		        tfBrojGotovinskog.setText(oldValue);
		    }
		});	  
		tfBrojIsecka.textProperty().addListener((observable, oldValue, newValue) -> {
		    if(!newValue.matches("[0-9]*")){
		        tfBrojIsecka.setText(oldValue);
		    }
		});	  
		if(Frame.dataOptions.isEmpty()==false) {	
		    tfLokacija.setText(Frame.getInstance().getOptions().get(0).getPath().toString());
		    tfOdrediste.setText(Frame.getInstance().getOptions().get(1).getPath().toString());
		    tfProvera.setText(String.valueOf(Frame.getInstance().getOptions().get(2).getVreme()));
		}
		else {
			tfLokacija.setText("");
			tfOdrediste.setText("");
			tfProvera.setText("");
		}
		if(Frame.dataPermament.isEmpty()==false) {	
		    tfNaziv.setText(Frame.getInstance().getDataPermament().get(0).getNaziv());
		    tfPib.setText(String.valueOf(Frame.getInstance().getDataPermament().get(0).getPib()));
		    tfAdresa.setText(Frame.getInstance().getDataPermament().get(0).getAdresa());
		    tfGrad.setText(Frame.getInstance().getDataPermament().get(0).getGrad());
		    tfTelefon.setText(Frame.getInstance().getDataPermament().get(0).getTelefon());		    
		    tfBrojIsecka.setText(String.valueOf(Frame.getInstance().getDataPermament().get(0).getBroj_isecka()));
		    tfBrojGotovinskog.setText(String.valueOf(Frame.getInstance().getDataPermament().get(0).getBroj_gotovinskog()));
		}
		
		btnLokacija.setOnAction(p->{
			DirectoryChooser chooser = new DirectoryChooser();
			if(tfLokacija.getText().isEmpty()==false)
				chooser.setInitialDirectory(new File(tfLokacija.getText()));
			File selectedFolder = chooser.showDialog(null);	

			if (selectedFolder != null) {
			    tfLokacija.setText(selectedFolder.getAbsolutePath());
			}	
		});
		btnOdrediste.setOnAction(p->{
			DirectoryChooser chooser = new DirectoryChooser();
			if(tfOdrediste.getText().isEmpty()==false)
				chooser.setInitialDirectory(new File(tfOdrediste.getText()));
			File selectedFolder = chooser.showDialog(null);

			if (selectedFolder != null) {
			    tfOdrediste.setText(selectedFolder.getAbsolutePath());
			}	
		});
		
		btnSacuvaj.setOnAction(p->{
			ArrayList<String> settings = new ArrayList<>();
			settings.add(lblLokacija.getText() + tfLokacija.getText());
			settings.add(lblOdrediste.getText() + tfOdrediste.getText());
			settings.add(lblProvera.getText() + tfProvera.getText());
			if(tfLokacija.getText().trim().equals("") || tfOdrediste.getText().trim().equals("") || tfProvera.getText().trim().equals("") ||
			   tfNaziv.getText().trim().equals("") || tfPib.getText().trim().equals("") || tfAdresa.getText().trim().equals("") || 
			   tfGrad.getText().trim().equals("") || tfTelefon.getText().trim().equals("") || tfBrojIsecka.getText().trim().equals("") ||
			   tfBrojGotovinskog.getText().trim().equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Upozorenje");
				alert.setHeaderText("Popunite sva polja!");
				alert.setContentText("Neophodno je da se popune sva polja za normalan rad programa. Molimo vas popunite sva polja ispravno.");
				alert.showAndWait();
			}
			else {
				try {
					File f = new File(Frame.getInstance().getPathSettings().toUri());
					Frame.getInstance().getDataOptions().clear();
					Options lokacija = new Options(lblLokacija.getText(), Paths.get(tfLokacija.getText()), 0);
					Options odrediste = new Options(lblOdrediste.getText(), Paths.get(tfOdrediste.getText()), 0);
					Options vreme = new Options(lblProvera.getText(), Paths.get(tfLokacija.getText()), Long.parseLong(tfProvera.getText()));
					Frame.getInstance().getDataOptions().add(lokacija);
					Frame.getInstance().getDataOptions().add(odrediste);
					Frame.getInstance().getDataOptions().add(vreme);
				    BufferedWriter writer = new BufferedWriter(new FileWriter(f));
			        for (Options c : Frame.getInstance().getOptions()) {
			        	String str = c.getNaziv()+";"+c.getPath()+";"+c.getVreme();
			            writer.write(str);
			            writer.newLine();
			        }
			        writer.close();

			        File data = new File(Frame.getInstance().getPathData().toUri());
			        BufferedWriter writerData = new BufferedWriter(new FileWriter(data));
			        Frame.getInstance().getDataPermament().clear();
			        Data d = new Data(tfNaziv.getText(), Integer.parseInt(tfPib.getText()), tfAdresa.getText(), tfGrad.getText(), tfTelefon.getText(), Integer.parseInt(tfBrojIsecka.getText()), Integer.parseInt(tfBrojGotovinskog.getText()));
			        Frame.getInstance().getDataPermament().add(d);
			        String dat = d.getNaziv()+";"+d.getPib()+";"+d.getAdresa()+";"+d.getGrad()+";"+d.getTelefon()+";"+d.getBroj_isecka()+";"+d.getBroj_gotovinskog();
			        writerData.write(dat);
			        writerData.newLine();
			        writerData.close();
				} catch (Exception e) {
				}
		        Frame.getInstance().LoadPaths();
		        Frame.getInstance().LoadPermamentData();
		        hide();		
			}
		});			
		GridPane gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
		gp.setHgap(20);
		gp.setVgap(20);
		gp.add(lblLokacija, 0, 0);
		gp.add(tfLokacija, 1, 0);
		gp.add(btnLokacija, 2, 0);
		gp.add(lblOdrediste, 0, 1);
		gp.add(tfOdrediste, 1, 1);
		gp.add(btnOdrediste, 2, 1);
		gp.add(lblProvera, 0, 2);
		gp.add(tfProvera, 1, 2);
		gp.add(lblMs, 2, 2);
		
		gp.add(lblNaziv, 0, 3);
		gp.add(tfNaziv, 1, 3);
		gp.add(lblPib, 0, 4);
		gp.add(tfPib, 1, 4);
		gp.add(lblAdresa, 0, 5);
		gp.add(tfAdresa, 1, 5);
		gp.add(lblGrad, 0, 6);	
		gp.add(tfGrad, 1, 6);
		gp.add(lblBrojIsecka, 0, 7);
		gp.add(tfBrojIsecka, 1, 7);
		gp.add(lblBrojGotovinskog, 0, 8);
		gp.add(tfBrojGotovinskog, 1, 8);
		gp.add(btnSacuvaj, 2, 8);
		ToggleGroup group = new ToggleGroup();
		Scene sc = new Scene(gp,490,480);
		setScene(sc);
		show();;
		setResizable(false);
	    setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
            	hide();
            }
        });
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        setX((primScreenBounds.getWidth() - getWidth()) / 2);
        setY((primScreenBounds.getHeight() - getHeight()) / 2);
	}

}
