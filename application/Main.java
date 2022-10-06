package application;
	
import java.io.IOException;

import gui.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;


public class Main extends Application {

/* instanciamos um fxmlloader p/ manipular tela antes de carregar
 * loader carrega a view informada (parametro)
 * cena principal com obj da view
 * dps do titulo, mostra o palco	
 	  scene esta no Main - janela principal
	  referencia da janela principal
	  guardamos a tela principal
	  e um metodo para pegar a referencia getMainScene
*/
	
	private static Scene mainScene; 
	private static Integer numEmp = 2;

	@Override
	public void start(Stage primaryStage) { 
	  try { 
			//  carrega os dados da MainView			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollpane = loader.load();
 			MainViewController controller = loader.getController();
			controller.numEmp = numEmp;

// isso cola a barra no limite da tela (menubar)
  		scrollpane.setFitToHeight(true);
		scrollpane.setFitToWidth(true);
 
		mainScene = new Scene(scrollpane); 
	    primaryStage.setScene(mainScene); 
	    primaryStage.setTitle ("WS Direções                                                 " +
	    "                  Sistema Gerencial de Oficinas"); 
	    primaryStage.show(); 
	  } catch (IOException e) { 
	    e.printStackTrace(); 
	  } 
	}
	
	public static Scene getMainScene() {
		return mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
