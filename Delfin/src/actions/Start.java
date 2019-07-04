package actions;

import java.util.Timer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import view.Frame;

public class Start implements EventHandler<ActionEvent> {

	public void handle(ActionEvent arg0) {
		Frame.getInstance().getBtnStart().setDisable(true);
		Frame.getInstance().getBtnStop().setDisable(false);
		Frame.getInstance().getBtnSettings().setDisable(true);
		Frame.getInstance().getBtnGotovinski().setDisable(false);
//		Frame.getInstance().time = new Timer();
		Frame.getInstance().runTask();
		
//		Frame.getInstance().getItemStart().setEnabled(false);
//		Frame.getInstance().getItemSettings().setEnabled(false);
//		Frame.getInstance().getItemStop().setEnabled(true);

	}

}
