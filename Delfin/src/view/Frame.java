package view;


import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import actions.Print;
import actions.Start;
import actions.Stop;
import actions.Task;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.Customer;
import model.Data;
import model.Options;
import model.Racun;

public class Frame extends Stage{

	private static Frame instance = null;	
	public static ArrayList<Options> dataOptions = new ArrayList<>();
	public static ArrayList<Customer> dataBase = new ArrayList<>();
	public static ArrayList<Data> dataPermament = new ArrayList<>();
	public static ArrayList<Racun> dataRacun = new ArrayList<>();
    public static PopupMenu popup = new PopupMenu();
	public static TrayIcon trayIcon; 
    public static SystemTray tray = SystemTray.getSystemTray();
    public static MenuItem itemOpen = new MenuItem("Otvori");
    public static Menu itemOptions = new Menu("Opcije");
    public static MenuItem itemStart = new MenuItem("Pokreni");
    public static MenuItem itemStop = new MenuItem("Zaustavi");
    public static MenuItem itemSettings = new MenuItem("Podešavanja");
    public static MenuItem itemGotovinski = new MenuItem("Gotovinski račun");
    public static MenuItem itemExit = new MenuItem("Izađi");

    public static ImageIcon icon;
    public DecimalFormat df = new DecimalFormat("0.00##");;
    public Timer time;
    public Button btnStart;
    public Button btnStop;
    public Button btnSettings;
    public Button btnGotovinski;
    public Alert alertExit = new Alert(AlertType.WARNING,
			 "Ukoliko ugasite aplikaciju nećete moći više da štampate fiskalne i gotovisnke račune.");
    public Alert alertGotovinski = new Alert(AlertType.INFORMATION);
    
    public static Path pathSettings = Paths.get(System.getProperty("user.dir")+"\\"+ "resources" + "\\" + "data" +"\\"+"options"+ ".txt");
    public static Path pathData = Paths.get(System.getProperty("user.dir")+"\\"+ "resources" + "\\" + "data" +"\\"+"permamentData"+ ".txt");
    public static Path pathDatabase  = Paths.get(System.getProperty("user.dir")+"\\"+ "resources" + "\\" + "data" +"\\"+"database"+ ".txt");
  
    public static String favicon = "/images/favicon.png";
    public String appName = "Delfin kasa 1.0";
    public GotovinskiRacun gr;
    public Settings sett;
    public boolean dialogGotovinski = false;
    public boolean dialogExit = false;
    
	public void prepareGUI(){
	
	    setTitle(appName); 
		setResizable(false); 
		initStyle(StageStyle.DECORATED);
	    getIcons().add(new Image(getClass().getResourceAsStream(favicon)));     
	    Platform.setImplicitExit(false);
	    
		icon = new ImageIcon(getClass().getResource(favicon));
		trayIcon = new java.awt.TrayIcon(icon.getImage(), appName);
		btnStart = new Button("Pokreni");
		btnStart.setPrefSize(100, 50);
		btnStop = new Button("Zaustavi");
		btnStop.setPrefSize(100, 50);
		btnStop.setDisable(true);
		btnSettings = new Button("Podešavanje");
		btnSettings.setPrefSize(150, 105);
		btnGotovinski = new Button("Gotovinski račun");
		btnGotovinski.setPrefSize(150, 105);
	 	btnGotovinski.setDisable(true);
		
		//AKCIJE
	    btnStart.setOnAction(p->{
	    	Start();
	    	System.out.println("da");
	    });
	    btnStop.setOnAction(new Stop());
	    btnSettings.setOnAction(p->sett = new Settings());
	    btnGotovinski.setOnAction(p->gr = new GotovinskiRacun());
		itemStart.setEnabled(false);
		itemSettings.setEnabled(false);
		itemStop.setEnabled(false);
		itemGotovinski.setEnabled(false);
		

		if (Files.exists(pathSettings)) {
			LoadPaths();
			System.out.println("Frame, 103: Procitao je putanje");
		}
		else {
			System.out.println("Frame, 107: Nema putanja");
			btnStart.setDisable(true);
			btnStop.setDisable(true);
			btnGotovinski.setDisable(true);		
			btnSettings.setDisable(false);
		}		
	

		if(Files.exists(pathData)) {
			LoadPermamentData();
			System.out.println("Frame, 117: Ucitao podatke o Delfinu");
			
		}
		else
			System.out.println("Frame, 119: Nema podataka o Delfinu");
		
		if(Files.exists(pathDatabase)){
		    loadDataBase();
			System.out.println("Frame, 123: Ucitao bazu korisnika");
		}
		else {
			System.out.println("Frame, 126: Putanja baze nije tacna ili baza ne postoji");
		}
		
	    setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
        		if(SystemTray.isSupported()) initSysTray();
            	   hide();
            }
        });
	    
	    itemOpen.addActionListener(p->{    	
	    	Platform.runLater(this::showStage);
	    	tray.remove(trayIcon);
	    });
	    itemStart.addActionListener(p->{
	    	   Platform.runLater(this::Start);
	    });
	    itemStop.addActionListener(p->{
	    	   Platform.runLater(this::Stop);
	    });
	    itemSettings.addActionListener(p->{
	    	   Platform.runLater(this::Settings);
	    });
	    itemExit.addActionListener(p ->{ 
//	    	Platform.runLater(new Runnable() {
//				public void run() {
//					if(btnStart.isDisabled())
//						stopTask();
			    	Platform.exit();
			    	System.exit(0);				
//				}
//			});
	    	
	    });
	    trayIcon.addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 1) {
	            	Platform.runLater(() -> showStage());
	            	tray.remove(trayIcon);
	            }
	        }
	    });
	    
		addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
	        if (KeyCode.ESCAPE == event.getCode()) {	        	
	        	 if(btnStart.isDisabled()) 
	        		  Stop();
	        	 else{
	        		 if(dialogExit == true) {
	        			 dialogExit = false;
	        		 }
	        		 else {
	        		 ButtonType da = new ButtonType("Da", ButtonBar.ButtonData.OK_DONE);
	        		 ButtonType ne = new ButtonType("Ne", ButtonBar.ButtonData.CANCEL_CLOSE);
	        		 
	        		    
	        		    alertExit.getButtonTypes().clear();
	        		    alertExit.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
	        		    Button yesButton = (Button) alertExit.getDialogPane().lookupButton( ButtonType.YES );
	        		    yesButton.setDefaultButton( false );
	        		    yesButton.setText("Da");
	        		    //Activate Defaultbehavior for no-Button:
	        		    Button noButton = (Button) alertExit.getDialogPane().lookupButton( ButtonType.NO );
	        		    noButton.setDefaultButton( true );
	        		    noButton.setText("Ne");
	        		    
	        		 alertExit.setTitle("Preventivni dialog");
	        		 alertExit.setHeaderText("Da li želite da naustite aplikaciju?");
	        		 Image image = new Image(getClass().getResource("/images/shutdown.png").toExternalForm());
	        		 ImageView imageView = new ImageView(image);
	        		 alertExit.setGraphic(imageView);
	        		 Optional<ButtonType> result = alertExit.showAndWait();
	        		 dialogExit = true;
	        		 if (result.get() == ButtonType.YES) {
		 	     	    	Platform.exit();
		 	    	    	System.exit(0);		
		 	    	    	 dialogExit = false;
	        		 }
	        		 if (result.get() == ButtonType.NO) {
		 	     	    	alertExit.hide();
	
	        		 }
	        	 }
	        	 }
	        }
		});
		
		addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
	        if (KeyCode.ENTER == event.getCode()) {
	        	if(dialogGotovinski == false && dialogExit == false){
			        if (btnGotovinski.isDisabled() == true) {
							alertGotovinski.setTitle("Obaveštenje");
							alertGotovinski.setHeaderText("Odštampajte fiskalni račun!");
							alertGotovinski.setContentText("Neophodno je da se prvo odštampa fiskalni račun. Štampanje gotovinskog "
									+ "računa je omogućeno isključivo za zadnji odštampan fiskalni isečak.");
							Optional<ButtonType> result = alertGotovinski.showAndWait();
							dialogGotovinski = true;
							if(result.get() == ButtonType.OK)
			        			alertGotovinski.hide();
			        }
			        else        	
			        	gr = new GotovinskiRacun();
	        	}
		        else 
		        	dialogGotovinski = false;
	        	if(dialogExit == true){
	        		dialogExit = false;
	        	}
		    }
	 });
		//AKCIJE
	    GridPane gp = new GridPane();
	    gp.add(btnStart, 0, 0);
	    gp.add(btnStop, 0, 1);
	    gp.setVgap(5);
	    gp.setAlignment(Pos.CENTER);
		HBox hb = new HBox(20);
		hb.setAlignment(Pos.CENTER);
		
		hb.getChildren().addAll(gp, btnSettings, btnGotovinski);
		Scene sc = new Scene(hb,500,150);
		setScene(sc);		
		show();	
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        setX((primScreenBounds.getWidth() - getWidth()) / 2);
        setY((primScreenBounds.getHeight() - getHeight()) / 2);
	}
	public static Frame getInstance(){
		if(instance == null){
			instance = new Frame();
			
		}
		return instance;
	}
	//UVOZ INFORMACIJA ZA DELFIN
	public void LoadPermamentData() { 
		try {
		       File file = new File(pathData.toUri());
			   Scanner sc = new Scanner(file);
			   while(sc.hasNextLine()){
			    String s[] = sc.nextLine().split(";");
			    Data d = new Data(s[0],Integer.parseInt(s[1]), s[2], s[3], s[4],Integer.parseInt(s[5]), Integer.parseInt(s[6]));
			    dataPermament.add(d);
			   }		
		} catch (Exception e) {
		}
	}
	// Uzmi podatke iz options.txt
	public void LoadPaths() { 
		try {
	           File file = new File(pathSettings.toUri());
			   Scanner sc = new Scanner(file);
			   while(sc.hasNextLine()){
				   String s[] = sc.nextLine().split(";");			   
				   Path path = FileSystems.getDefault().getPath(s[1]);
				   Options o = new Options(s[0], path, Long.parseLong(s[2]));
				   dataOptions.add(o); 
			   }
			
		} catch (Exception e) {
		}
	}
    public void loadDataBase() {
		try {
	        File file = new File(pathDatabase.toUri());
			   Scanner sc = new Scanner(file);
			   while(sc.hasNextLine()){
			    String s[] = sc.nextLine().split(";");
			    String suma = df.format(Double.parseDouble(s[4]));
			    String pdv = df.format(Double.parseDouble(s[5]));

			    Customer k = new Customer(s[0],Integer.parseInt(s[1]),s[2],s[3],Double.parseDouble(suma),Double.parseDouble(pdv));		    
			    dataBase.add(k);
			   }
			
		} catch (Exception e) {
		}
    }
	public void showStage() {
	        if (this != null) {
	            show();
	            toFront();
	        }
	}
	public void Start() {
       // if (this != null) {
    		btnStart.setDisable(true);
    		btnSettings.setDisable(true);
    		btnStop.setDisable(false);
    		btnGotovinski.setDisable(false);
    	
    		itemStart.setEnabled(false);
    		itemSettings.setEnabled(false);
    		itemStop.setEnabled(true);
    		time = new Timer();
    		runTask();
    	
    //    }
	}
	public void Stop() {
        if (this != null) {
    		itemStart.setEnabled(true);
    		itemSettings.setEnabled(true);
    		itemStop.setEnabled(false);
    		
    		btnStart.setDisable(false);
    		btnSettings.setDisable(false);
    		btnStop.setDisable(true);
    		btnGotovinski.setDisable(true);
    		stopTask();
       }
	}
	public void Settings() {
      //  if (this != null) {
        	sett = new Settings();
      //  }
	}
	public void runTask(){
		time.schedule(new Task(),dataOptions.get(2).getVreme(),dataOptions.get(2).getVreme());
	}
	public void stopTask(){
        time.cancel();
        time.purge();
	}
    public void initSysTray() {
	itemOptions.add(itemStart);
	itemOptions.add(itemStop);
	itemOptions.add(itemSettings);
	itemOptions.add(itemGotovinski);
	popup.add(itemOpen);
	popup.add(itemOptions);
    popup.add(itemExit);
	 
	 trayIcon.setPopupMenu(popup);
	 try {
		tray.add(trayIcon);
		 trayIcon.setToolTip(appName);
		 trayIcon.displayMessage(appName, "Aplikacija omogućava štampanje fiskalnih i gotovinskih računa. Obavezno je da putanje budu tačne i da bude pokrenuta u pozadini!", MessageType.INFO);
	} catch (Exception e) {
	}
	}

	public static ArrayList<Options> getOptions() {
		return dataOptions;
	}
	public static void setOptions(ArrayList<Options> options) {
		Frame.dataOptions = options;
	}
	public static Path getPathData() {
		return pathData;
	}
	public static void setPathData(Path pathData) {
		Frame.pathData = pathData;
	}
	public static Path getPathDatabase() {
		return pathDatabase;
	}
	public static void setPathDatabase(Path pathDatabase) {
		Frame.pathDatabase = pathDatabase;
	}
	public static Path getPathSettings() {
		return pathSettings;
	}
	public static void setPathSettings(Path pathSettings) {
		Frame.pathSettings = pathSettings;
	}
	public Timer getTime() {
		return time;
	}
	public void setTime(Timer time) {
		this.time = time;
	}
	public static PopupMenu getPopup() {
		return popup;
	}
	public static void setPopup(PopupMenu popup) {
		Frame.popup = popup;
	}
	public static TrayIcon getTrayIcon() {
		return trayIcon;
	}
	public static void setTrayIcon(TrayIcon trayIcon) {
		Frame.trayIcon = trayIcon;
	}
	public static SystemTray getTray() {
		return tray;
	}
	public static void setTray(SystemTray tray) {
		Frame.tray = tray;
	}
	public static MenuItem getItemExit() {
		return itemExit;
	}
	public static void setItemExit(MenuItem itemExit) {
		Frame.itemExit = itemExit;
	}
	public static String getFavicon() {
		return favicon;
	}
	public static void setFavicon(String favicon) {
		Frame.favicon = favicon;
	}
	public static ImageIcon getIcon() {
		return icon;
	}
	public static void setIcon(ImageIcon icon) {
		Frame.icon = icon;
	}
	public static ArrayList<Options> getDataOptions() {
		return dataOptions;
	}
	public static void setDataOptions(ArrayList<Options> dataOptions) {
		Frame.dataOptions = dataOptions;
	}
	public static ArrayList<Customer> getDataBase() {
		return dataBase;
	}
	public static void setDataBase(ArrayList<Customer> dataBase) {
		Frame.dataBase = dataBase;
	}
	public static ArrayList<Data> getDataPermament() {
		return dataPermament;
	}
	public static void setDataPermament(ArrayList<Data> dataPermament) {
		Frame.dataPermament = dataPermament;
	}
	public static ArrayList<Racun> getDataRacun() {
		return dataRacun;
	}
	public static void setDataRacun(ArrayList<Racun> dataRacun) {
		Frame.dataRacun = dataRacun;
	}
	public DecimalFormat getDf() {
		return df;
	}
	public void setDf(DecimalFormat df) {
		this.df = df;
	}
	public Button getBtnStart() {
		return btnStart;
	}
	public void setBtnStart(Button btnStart) {
		this.btnStart = btnStart;
	}
	public Button getBtnStop() {
		return btnStop;
	}
	public void setBtnStop(Button btnStop) {
		this.btnStop = btnStop;
	}
	public Button getBtnSettings() {
		return btnSettings;
	}
	public void setBtnSettings(Button btnSettings) {
		this.btnSettings = btnSettings;
	}
	public Button getBtnGotovinski() {
		return btnGotovinski;
	}
	public void setBtnGotovinski(Button btnGotovinski) {
		this.btnGotovinski = btnGotovinski;
	}
	public GotovinskiRacun getGr() {
		return gr;
	}
	public void setGr(GotovinskiRacun gr) {
		this.gr = gr;
	}
	public Settings getSett() {
		return sett;
	}
	public void setSett(Settings sett) {
		this.sett = sett;
	}
	public static MenuItem getItemOpen() {
		return itemOpen;
	}
	public static void setItemOpen(MenuItem itemOpen) {
		Frame.itemOpen = itemOpen;
	}
	public static Menu getItemOptions() {
		return itemOptions;
	}
	public static void setItemOptions(Menu itemOptions) {
		Frame.itemOptions = itemOptions;
	}
	public static MenuItem getItemStart() {
		return itemStart;
	}
	public static void setItemStart(MenuItem itemStart) {
		Frame.itemStart = itemStart;
	}
	public static MenuItem getItemStop() {
		return itemStop;
	}
	public static void setItemStop(MenuItem itemStop) {
		Frame.itemStop = itemStop;
	}
	public static MenuItem getItemSettings() {
		return itemSettings;
	}
	public static void setItemSettings(MenuItem itemSettings) {
		Frame.itemSettings = itemSettings;
	}	
	
}