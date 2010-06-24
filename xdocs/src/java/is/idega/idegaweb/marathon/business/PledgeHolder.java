package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Participant;

/**
 * A holder class for information about pledge in the PledgeWizard.
 * 
 * @author <a href="mailto:sigtryggur@idega.com">sigtryggur</a>
 */

public class PledgeHolder {
	private Participant participant;
	private String firstNameFilter;
	private String middleNameFilter;
	private String lastNameFilter;
	private String personalIDFilter;
	private String charityFilter;
	private float pledgeAmount;
	private String cardHolderName;

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
	
	public Participant getParticipant() {
		return this.participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public String getCardHolderName() {
		return this.cardHolderName;
	}

	public void setCardholderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}

	public float getPledgeAmount() {
		return this.pledgeAmount;
	}
	
	public void setPledgeAmount(float pledgeAmount) {
		this.pledgeAmount = pledgeAmount;
	}
}