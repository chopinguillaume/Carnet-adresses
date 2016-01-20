package ch.makery.address.view;

import ch.makery.address.MainApp;
import ch.makery.address.model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;

public class PersonOverviewController {

	@FXML
	private TableView<Person> personTable;
	@FXML
	private TableColumn<Person, String> genderColumn;
	@FXML
	private TableColumn<Person, String> lastNameColumn;
	
	@FXML
	private Label genderLabel;
	@FXML
	private Label lastNameLabel;
	@FXML
    private Label firstNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label telNumberLabel;
    @FXML
    private Label daysLabel;
    @FXML
    private TextArea moreInfoTextArea;
    
    private MainApp mainApp;
    
    public PersonOverviewController() {
	}
    
    @FXML
    private void initialize(){
    	genderColumn.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
    	lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
    	
    	showPersonDetails(null);
    	
    	personTable.getSelectionModel().selectedItemProperty().addListener(
    			(observable, oldValue, newValue) -> showPersonDetails(newValue));
    }
	
    public void setMainApp(MainApp mainApp){
    	this.mainApp = mainApp;
    	
    	personTable.setItems(mainApp.getPersonData());
    }
    
    private void showPersonDetails(Person person){
    	if(person != null){
    		genderLabel.setText(person.getGender());
    		lastNameLabel.setText(person.getLastName());
    		firstNameLabel.setText(person.getFirstName());
    		streetLabel.setText(person.getStreet());
    		cityLabel.setText(person.getCity());
    		postalCodeLabel.setText(person.getPostalCode());
    		telNumberLabel.setText(person.getTelNumber());
    		daysLabel.setText(person.getDayList().toString());
    		moreInfoTextArea.setText(person.getMoreInfo());
    	}
    	else{
    		genderLabel.setText("");
    		lastNameLabel.setText("");
    		firstNameLabel.setText("");
    		streetLabel.setText("");
    		cityLabel.setText("");
    		postalCodeLabel.setText("");
    		telNumberLabel.setText("");
    		daysLabel.setText("");
    		moreInfoTextArea.setText("");
    	}
    }
    
    @FXML
    private void handleDeletePerson(){
    	int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
    	if(selectedIndex >= 0){
    		Alert alertConfirm = new Alert(AlertType.CONFIRMATION,"Voulez-vous vraiment supprimer cette personne de la liste ?" ,ButtonType.YES, ButtonType.NO);
    		alertConfirm.initOwner(mainApp.getPrimaryStage());
    		alertConfirm.setTitle("Suppression");
    		alertConfirm.setHeaderText("Confirmation de suppression");
    		alertConfirm.initModality(Modality.WINDOW_MODAL);
    		
    		alertConfirm.showAndWait().ifPresent(response -> {
    			if(response == ButtonType.YES){
    				personTable.getItems().remove(selectedIndex);
    				mainApp.setModified(true);
    			}
    			else if(response == ButtonType.NO){
    				
    			}
    		});
    	}
    	else{
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.initOwner(mainApp.getPrimaryStage());
    		alert.setTitle("Aucune sélection");
    		alert.setHeaderText("Aucune personne n'est sélectionnée");
    		alert.setContentText("Veuillez sélectionner une personne dans la liste");
    		
    		alert.showAndWait();
    	}
    }
    
    @FXML
    private void handleNewPerson(){
    	Person tempPerson = new Person();
    	boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
    	
    	if(okClicked){
    		mainApp.getPersonData().add(tempPerson);
    	}
    }
    
    @FXML
    private void handleEditPerson(){
    	Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
    	if(selectedPerson != null){
    		boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
    		if(okClicked){
    			showPersonDetails(selectedPerson);
    		}
    	}
    	else{
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.initOwner(mainApp.getPrimaryStage());
    		alert.setTitle("Aucune sélection");
    		alert.setHeaderText("Aucune personne n'est sélectionnée");
    		alert.setContentText("Veuillez sélectionner une personne dans la liste");

            alert.showAndWait();
    	}
    }
    
    
}
