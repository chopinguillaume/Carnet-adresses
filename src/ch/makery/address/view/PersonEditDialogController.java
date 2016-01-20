package ch.makery.address.view;

import java.util.ArrayList;
import ch.makery.address.MainApp;
import ch.makery.address.model.Person;
import ch.makery.address.util.DayEnum;
import ch.makery.address.util.DayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PersonEditDialogController {

	@FXML
	private ChoiceBox<String> genderChoiceBox; 
	@FXML
	private TextField lastNameTextField;
	@FXML
	private TextField firstNameTextField;
	@FXML
	private TextField streetTextField;
	@FXML
	private TextField cityTextField;
	@FXML
	private TextField postalCodeTextField;
	@FXML
	private TextField telNumberTextField;
	@FXML
	private TextArea moreInfoTextArea;
	
	@FXML
	private CheckBox monCheckBox;
	@FXML
	private CheckBox tueCheckBox;
	@FXML
	private CheckBox wedCheckBox;
	@FXML
	private CheckBox thuCheckBox;
	@FXML
	private CheckBox friCheckBox;
	@FXML
	private CheckBox satCheckBox;
	@FXML
	private CheckBox sunCheckBox;
	
	private ArrayList<CheckBox> checkBoxes = new ArrayList<>();
	private DayEnum[] days = new DayEnum[7];

	private Stage dialogStage;
	private Person person;
	private boolean okClicked = false;
	private MainApp mainApp;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}


	@FXML
	private void initialize(){
		ObservableList<String> genderList = FXCollections.observableArrayList("Homme","Femme");
		genderChoiceBox.setItems(genderList);
		
		checkBoxes.add(monCheckBox); days[0] = DayEnum.Monday;
		checkBoxes.add(tueCheckBox); days[1] = DayEnum.Tuesday;
		checkBoxes.add(wedCheckBox); days[2] = DayEnum.Wednesday;
		checkBoxes.add(thuCheckBox); days[3] = DayEnum.Thurdsay;
		checkBoxes.add(friCheckBox); days[4] = DayEnum.Friday;
		checkBoxes.add(satCheckBox); days[5] = DayEnum.Saturday;
		checkBoxes.add(sunCheckBox); days[6] = DayEnum.Sunday;
		
	}

	public void setDialogStage(Stage dialogStage){
		this.dialogStage = dialogStage;
	}

	public void setPerson(Person person){
		this.person = person;

		genderChoiceBox.setValue(person.getGender());
		lastNameTextField.setText(person.getLastName());
		firstNameTextField.setText(person.getFirstName());
		streetTextField.setText(person.getStreet());
		cityTextField.setText(person.getCity());
		postalCodeTextField.setText(person.getPostalCode());
		telNumberTextField.setText(person.getTelNumber());
		moreInfoTextArea.setText(person.getMoreInfo());
		
		for (int i = 0; i<7; i++){
			if(person.getDayList().contains(days[i])){
				checkBoxes.get(i).setSelected(true);
			}
		}
	}

	public boolean isOkClicked(){
		return okClicked;
	}

	@FXML
	private void handleOk(){
		if(isValidInput()){

			person.setGender(genderChoiceBox.getSelectionModel().getSelectedItem().toString());
			person.setLastName(lastNameTextField.getText());
			person.setFirstName(firstNameTextField.getText());
			person.setStreet(streetTextField.getText());
			person.setCity(cityTextField.getText());
			person.setPostalCode(postalCodeTextField.getText());
			person.setTelNumber(telNumberTextField.getText());
			person.setMoreInfo(moreInfoTextArea.getText());
			
			DayList newDays = new DayList();
			for(int i=0; i<7; i++){
				if(checkBoxes.get(i).isSelected()){
					newDays.add(days[i]);
				}
			}
			person.setDayList(newDays);
			
			okClicked=true;
			mainApp.setModified(true);
			
			dialogStage.close();
		}
	}

	@FXML
	private void handleCancel(){
		dialogStage.close();
	}

	private boolean isValidInput(){
		String errorMessage = "";

		if (genderChoiceBox.getSelectionModel().getSelectedItem() == null || 
				genderChoiceBox.getSelectionModel().getSelectedItem().toString().length() == 0){
			errorMessage += "Le sexe est invalide !\n";
		}
		
		if (lastNameTextField.getText() == null || lastNameTextField.getText().length() == 0) {
			errorMessage += "Le nom est invalide !\n";
		}
		
		// try to parse the postal code into an int.
		try {
			if(postalCodeTextField.getText() != null && postalCodeTextField.getText().length() != 0){
				Integer.parseInt(postalCodeTextField.getText());
			}
		} catch (NumberFormatException e) {
			errorMessage += "Le code postal est invalide (doit être un nombre) !\n";
		}
		
		try {
			if(telNumberTextField.getText() != null && telNumberTextField.getText().length() != 0){
				Integer.parseInt(telNumberTextField.getText());
			}
		} catch (NumberFormatException e) {
			errorMessage += "Le numéro de téléphone est invalide (doit être un nombre) !\n";
		}
		
		if(errorMessage.length() == 0){
			return true;
		}
		else{
			Alert alert = new Alert(AlertType.ERROR);
			
			alert.initOwner(dialogStage);
			alert.setTitle("Champs invalides");
			alert.setContentText("Veuillez corriger les champs invalides");
			alert.setHeaderText(errorMessage);
			
			alert.showAndWait();
			
			return false;
		}
		
	}

}
