package ch.makery.address.view;

import java.io.File;

import ch.makery.address.MainApp;
import ch.makery.address.util.PdfMaker;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import javafx.stage.FileChooser.ExtensionFilter;

public class RootLayoutController {
	
	private MainApp mainApp;
	private boolean saveConfirmed;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	private boolean alertConfirmSave(){
		//returns true if continue (save done or not), false if cancelled
		
		Alert alert = new Alert(AlertType.CONFIRMATION, "Voulez-vous sauvegarder ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		
		alert.setTitle("Confirmation");
		
		alert.showAndWait().ifPresent(response -> {
			if(response == ButtonType.YES){
				handleSave();
				saveConfirmed = true;
			}
			else if(response == ButtonType.NO){
				saveConfirmed = true;
			}
			else if(response == ButtonType.CANCEL){
				saveConfirmed = false;
			}
		});
		return saveConfirmed;
		
	}
	
	@FXML
	private void handleNew(){
		
		boolean open = true;
		
		if(mainApp.isModified()){
			open = alertConfirmSave();
		}
		
		if(open){
			mainApp.getPersonData().clear();
			mainApp.setPersonFilePath(null);
		}
	}
	
	@FXML
	private void handleOpen(){
		
		boolean open = true;
		
		if(mainApp.isModified()){
			open = alertConfirmSave();
		}
		
		if(open){
			FileChooser fileChooser = new FileChooser();
			
			ExtensionFilter extFilter = new ExtensionFilter("XML files (*.xml)", ".xml");
			fileChooser.getExtensionFilters().add(extFilter);
			
			File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
			
			if(file != null){
				mainApp.loadPersonDataFromFile(file);
			}
		}
	}

	@FXML
	public void handleSave(){
		File personFile = mainApp.getPersonFilePath();
		if(personFile != null){
			mainApp.savePersonDataToFile(personFile);
		}
		else{
			handleSaveAs();
		}
	}
	
	@FXML
	private void handleSaveAs(){
		FileChooser fileChooser = new FileChooser();
		
		ExtensionFilter extFilter = new ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		
		File personFile = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
		
		if(personFile!=null){
			if(! personFile.getPath().endsWith(".xml")){
				personFile = new File(personFile.getPath()+".xml");
			}
			
			mainApp.savePersonDataToFile(personFile);
		}
	}
	
	@FXML
	private void handleExportPDF(){
		if(mainApp.getPersonFilePath() == null){
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Export");
			alert.setHeaderText("Enregistrement requis");
			alert.setContentText("Veuillez enregistrer avant d'exporter");
			
			alert.showAndWait();
			
		}else{
			
			PdfMaker.PersonListToPDF(mainApp.getPersonData(), mainApp.getPersonFilePath());
			
		}
	}
	
	@FXML
	private void handleAbout(){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Carnet d'adresses");
		alert.setHeaderText("A propos");
		alert.setContentText("Auteur: Guillaume Chopin");
		
		alert.showAndWait();
	}
	
	@FXML
	private void handleExit(){
		mainApp.getPrimaryStage().fireEvent(
                new WindowEvent(
                		mainApp.getPrimaryStage(),
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );
	}
	

}
