package ch.makery.address.model;

import ch.makery.address.util.DayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Person {

	private final StringProperty lastName;
	private final StringProperty gender;
	private final StringProperty firstName;
	private final StringProperty street;
	private final StringProperty city;
	private final StringProperty postalCode;
	private final StringProperty moreInfo;
	private final StringProperty telNumber;
	private final ObjectProperty<DayList> dayList;
	
	public Person() {
		this("","");
	}
	
	public Person(String gender, String lastName){
		this.lastName = new SimpleStringProperty(lastName);
		this.gender = new SimpleStringProperty(gender);
		
		this.firstName = new SimpleStringProperty("");
		this.street = new SimpleStringProperty("");
		this.city = new SimpleStringProperty("");
		this.postalCode = new SimpleStringProperty("");
		this.moreInfo = new SimpleStringProperty("");
		this.telNumber = new SimpleStringProperty("");
		this.dayList = new SimpleObjectProperty<DayList>(new DayList());
	}
	
	public void setDayList(DayList dayList){
		this.dayList.set(dayList);
	}
	
	public DayList getDayList() {
		return dayList.get();
	}
	
	public ObjectProperty<DayList> dayListProperty() {
		return dayList;
	}
	
	public void setTelNumber(String tel){
		telNumber.set(tel);
	}
	
	public String getTelNumber() {
		return telNumber.get();
	}
	
	public StringProperty telNumberProperty() {
		return telNumber;
	}
	
	public String getMoreInfo() {
		return moreInfo.get();
	}
	
	public void setMoreInfo(String info){
		moreInfo.set(info);
	}
	
	public StringProperty moreInfoProperty() {
		return moreInfo;
	}
	
	public String getGender() {
		return gender.get();
	}
	
	public void setGender(String gender){
		this.gender.set(gender);
	}
	
	public StringProperty genderProperty(){
		return gender;
	}
	
	public String getGenderAndLastName(){
		if(gender.get() == "Homme"){
			return "M. "+getLastName();
		}
		else{
			return "Mme "+getLastName();
		}
	}
	
	public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public String getStreet() {
        return street.get();
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public StringProperty streetProperty() {
        return street;
    }

    public String getPostalCode() {
        return postalCode.get();
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public StringProperty postalCodeProperty() {
        return postalCode;
    }

    public String getCity() {
        return city.get();
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public StringProperty cityProperty() {
        return city;
    }

	public String getFullAddress() {
		String res = "";
		
		if(getStreet().length() > 0 && getCity().length() > 0){
			res += getStreet()+", "+getCity();
		}
		else{
			res += getStreet();
			res += getCity();
		}
		
		return res;
	}
	
	
	
}
