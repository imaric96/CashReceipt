package actions;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import view.Frame;

public class Stop implements EventHandler<ActionEvent> {

	public void handle(ActionEvent arg0) {
		Frame.getInstance().getBtnStart().setDisable(false);
		Frame.getInstance().getBtnStop().setDisable(true);
		Frame.getInstance().getBtnSettings().setDisable(false);
		Frame.getInstance().getBtnGotovinski().setDisable(true);
		Frame.getInstance().stopTask();
		
//		Frame.getInstance().getItemStart().setEnabled(true);
//		Frame.getInstance().getItemSettings().setEnabled(true);
//		Frame.getInstance().getItemStop().setEnabled(false);
	}
}
