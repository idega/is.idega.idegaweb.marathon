package is.idega.idegaweb.marathon.webservice;

public class Registration {

	private String personalId;
	private int distance;
	private String charityId;
	public String getCharityId() {
		return charityId;
	}
	public void setCharityId(String charityId) {
		this.charityId = charityId;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public String getPersonalId() {
		return personalId;
	}
	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}
}
