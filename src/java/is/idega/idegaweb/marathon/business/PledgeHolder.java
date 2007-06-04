package is.idega.idegaweb.marathon.business;

import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * A holder class for information about pledge in the PledgeWizard.
 * 
 * @author <a href="mailto:sigtryggur@idega.com">sigtryggur</a>
 */

public class PledgeHolder {
	private User runner;
	private Group run;
	private Group year;
	private Group distance;

	private String firstNameFilter;
	private String middleNameFilter;
	private String lastNameFilter;
	private String personalIDFilter;
	private String charityFilter;
	
	private float pledgeAmount;
	
	private String cardHolderName;
	private String cardholderEmail;
	private String cardholderPersonalID;
	
	private boolean agreeToTerms;
	
	

	public String getPersonalIDFilter() {
		return personalIDFilter;
	}

	public void setPersonalIDFilter(String personalIDFilter) {
		this.personalIDFilter = personalIDFilter;
	}

	public String getFirstNameFilter() {
		return firstNameFilter;
	}

	public void setFirstNameFilter(String firstNameFilter) {
		this.firstNameFilter = firstNameFilter;
	}

	public String getMiddleNameFilter() {
		return middleNameFilter;
	}

	public void setMiddleNameFilter(String middleNameFilter) {
		this.middleNameFilter = middleNameFilter;
	}
	
	public String getLastNameFilter() {
		return lastNameFilter;
	}

	public void setLastNameFilter(String lastNameFilter) {
		this.lastNameFilter = lastNameFilter;
	}

	public String getCharityFilter() {
		return charityFilter;
	}

	public void setCharityFilter(String charityFilter) {
		this.charityFilter = charityFilter;
	}
	
	public User getRunner() {
		return this.runner;
	}

	public void setRunner(User user) {
		this.runner = user;
	}

	public Group getRun() {
		return this.run;
	}

	public void setRun(Group run) {
		this.run = run;
	}

	public Group getYear() {
		return this.year;
	}

	public void setYear(Group year) {
		this.year = year;
	}

	public Group getDistance() {
		return this.distance;
	}


	public void setDistance(Group distance) {
		this.distance = distance;
	}

	public String getCardHolderName() {
		return this.cardHolderName;
	}

	public void setCardholderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public String getCardholderPersonalID() {
		return this.cardholderPersonalID;
	}

	public void setCardholderPersonalID(String cardholderPersonalID) {
		this.cardholderPersonalID = cardholderPersonalID;
	}

	public String getCardholderEmail() {
		return this.cardholderEmail;
	}

	public void setCardholderEmail(String cardholderEmail) {
		this.cardholderEmail = cardholderEmail;
	}

	public boolean isAgreedToTerms() {
		return this.agreeToTerms;
	}

	public void setAgreeToTerms(boolean agreeToTerms) {
		this.agreeToTerms = agreeToTerms;
	}
	
	public float getPledgeAmount() {
		return this.pledgeAmount;
	}
	
	public void setPledgeAmount(float pledgeAmount) {
		this.pledgeAmount = pledgeAmount;
	}
}