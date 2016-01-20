package ch.makery.address;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import ch.makery.address.model.Person;
import ch.makery.address.model.PersonListWrapper;
import ch.makery.address.view.PersonEditDialogController;
import ch.makery.address.view.PersonOverviewController;
import ch.makery.address.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private ObservableList<Person> personData = FXCollections.observableArrayList();
	private RootLayoutController rootController;
	
	private boolean modified = false;
	
	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	public boolean isModified() {
		return modified;
	}
	
	public MainApp() {
	}
	
	public ObservableList<Person> getPersonData() {
		return personData;
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setOnCloseRequest(confirmCloseEventHandler);

		this.primaryStage.setTitle("Carnet d'adresses");
		
		this.primaryStage.getIcons().add(new Image("file:resources/images/address_icon.png"));

		initRootLayout();

		showPersonOverview();
	}
	
	private EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
		if(modified){

			Alert alert = new Alert(AlertType.CONFIRMATION, "Voulez-vous sauvegarder avant de quitter ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
			alert.setHeaderText("Confirmation pour quitter");
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(primaryStage);

			Button exitButton = (Button) alert.getDialogPane().lookupButton(ButtonType.NO);
			exitButton.setText("Quitter sans sauvegarder");

			Button saveButton = (Button) alert.getDialogPane().lookupButton(ButtonType.YES);
			saveButton.setText("Sauvegarder puis quitter");

			Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
			cancelButton.setText("Annuler");

			alert.showAndWait().ifPresent(response -> {
				if(response == ButtonType.YES){
					rootController.handleSave();
				}
				else if(response == ButtonType.NO){

				}
				else if(response == ButtonType.CANCEL){
					event.consume();
				}
			});

		}
	};
	
		

	private void showPersonOverview() {

		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
			AnchorPane personOverview;
			personOverview = (AnchorPane) loader.load();

			rootLayout.setCenter(personOverview);
			
			PersonOverviewController controller = loader.getController();
			controller.setMainApp(this);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean showPersonEditDialog(Person person){
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Editer une personne");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			PersonEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPerson(person);
			controller.setMainApp(this);
			
			dialogStage.showAndWait();
			
			return controller.isOkClicked();
			
		} catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}

	private void initRootLayout() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			this.rootController = controller;
			
			primaryStage.show();
			
			setPersonFilePath(null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Stage getPrimaryStage(){
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Returns the person file preference, i.e. the file that was last opened.
	 * The preference is read from the OS specific registry. If no such
	 * preference can be found, null is returned.
	 * 
	 * @return
	 */
	public File getPersonFilePath(){
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		String filePath = prefs.get("filepath", null);
		if(filePath != null){
			return new File(filePath);
		}
		else{
			return null;
		}
	}
	
	/**
	 * Sets the file path of the currently loaded file. The path is persisted in
	 * the OS specific registry.
	 * 
	 * @param file the file or null to remove the path
	 */
	public void setPersonFilePath(File file){
		Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		if(file != null){
			prefs.put("filepath", file.getPath());
			
			primaryStage.setTitle("Carnet d'adresses - "+ file.getName());
		}
		else{
			prefs.remove("filepath");
			
			primaryStage.setTitle("Carnet d'adresses");
		}
	}
	
	/**
	 * Loads person data from the specified file. The current person data will
	 * be replaced.
	 * 
	 * @param file
	 */
	public void loadPersonDataFromFile(File file){
		try {
			JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
			
			Unmarshaller um = context.createUnmarshaller();
			
			PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);
			
			personData.clear();
			if(wrapper.getPersons() != null){
				personData.addAll(wrapper.getPersons());
			}
			
			setPersonFilePath(file);
			
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Chargement impossible");
			alert.setContentText("Chargement impossible du fichier:\n"+file.getPath());
			
			alert.showAndWait();
		}
	}
	
	/**
	 * Saves the current person data to the specified file.
	 * 
	 * @param file
	 */
	public void savePersonDataToFile(File file){
		try {
			JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
			
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			PersonListWrapper wrapper = new PersonListWrapper();
			wrapper.setPersons(personData);
			
			m.marshal(wrapper, file);
			
			setPersonFilePath(file);
			setModified(false);
			
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur");
			alert.setHeaderText("Enregistrement impossible");
			alert.setContentText("Enregistrement impossible du fichier:\n"+file.getPath());
			
			alert.showAndWait();
		}
	}
	
}
