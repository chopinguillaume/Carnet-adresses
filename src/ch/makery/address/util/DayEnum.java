package ch.makery.address.util;

public enum DayEnum {
	Monday("Lundi","L"),
	Tuesday("Mardi","Ma"),
	Wednesday("Mercredi","Me"),
	Thurdsay("Jeudi","J"),
	Friday("Vendredi","V"),
	Saturday("Samedi","S"),
	Sunday("Dimanche","D");
	
	private String trad = "";
	private String cut = "";
	
	DayEnum(String trad, String cut){
		this.trad = trad;
		this.cut = cut;
	}
	
	public String getTrad() {
		return trad;
	}
	
	public String getCut() {
		return cut;
	}
	
	@Override
	public String toString() {
		return trad;
	}
	
}
