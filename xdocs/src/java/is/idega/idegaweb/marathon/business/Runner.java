/*
 * $Id: Runner.java,v 1.17 2009/01/13 09:38:28 palli Exp $ Created on May 16, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.data.RunCategory;
import is.idega.idegaweb.marathon.data.Year;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;
import com.idega.core.location.data.Country;
import com.idega.user.data.Gender;
import com.idega.user.data.User;
import com.idega.util.Age;

/**
 * A holder class for information about runners and their selection when
 * registering.
 * 
 * Last modified: $Date: 2009/01/13 09:38:28 $ by $Author: palli $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.17 $
 */
public class Runner {

	private User user;
	private Run run;
	private Distance distance;

	private boolean rentChip;
	private boolean ownChip;
	private boolean buyChip;
	private String chipNumber;
	private boolean transportOrdered;
	private boolean noTransportOrdered;
	private String shirtSize;
	private boolean allowsEmails;
	private boolean notAllowsEmails;

	private String name;
	private String personalID;
	private Date dateOfBirth;
	private String address;
	private String city;
	private String postalCode;
	private Country country;
	private Gender gender;
	private Country nationality;
	private String email;
	private String email2;
	private String homePhone;
	private String mobilePhone;
	private boolean agree;
	private float amount;
	private boolean participateInCharity=true;
	private boolean maySponsorContactRunner=false;
	private Charity charity;
	private RunCategory category;
	private boolean applyForDomesticTravelSupport=false;
	private boolean applyForInternationalTravelSupport=false;
	private boolean sponsoredRunner=false;

	private boolean checkQuestions =  false;
	
	private String question1Hour;
	private String question1Minute;
	private String question1Year;
	private boolean question1NeverRan = false;
	
	private String question2Hour;
	private String question2Minute;
	
	private String question3Hour;
	private String question3Minute;
	private String question3Year;
	private boolean question3NeverRan = false;
	
	private String relayPartner1SSN = "";
	private String relayPartner1Name = "";
	private String relayPartner1Email = "";
	private String relayPartner1ShirtSize = "";
	private String relayPartner1Leg = "";

	private String relayPartner2SSN = "";
	private String relayPartner2Name = "";
	private String relayPartner2Email = "";
	private String relayPartner2ShirtSize = "";
	private String relayPartner2Leg = "";

	private String relayPartner3SSN = "";
	private String relayPartner3Name = "";
	private String relayPartner3Email = "";
	private String relayPartner3ShirtSize = "";
	private String relayPartner3Leg = "";
	
	private String relayLeg = "";

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Run getRun() {
		return this.run;
	}

	public void setRun(Run run) {
		this.run = run;
	}

	public Distance getDistance() {
		return this.distance;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	public boolean isRentChip() {
		return this.rentChip;
	}

	public void setRentChip(boolean rentChip) {
		this.rentChip = rentChip;
		if (rentChip) {
			setOwnChip(false);
			setBuyChip(false);
		}
	}

	public boolean isOwnChip() {
		return this.ownChip;
	}

	public void setOwnChip(boolean ownChip) {
		this.ownChip = ownChip;
		if (ownChip) {
			setRentChip(false);
			setBuyChip(false);
		}
	}

	public boolean isBuyChip() {
		return this.buyChip;
	}

	public void setBuyChip(boolean buyChip) {
		this.buyChip = buyChip;
		if (buyChip) {
			setOwnChip(false);
			setRentChip(false);
		}
	}

	public String getChipNumber() {
		return this.chipNumber;
	}

	public void setChipNumber(String chipNumber) {
		this.chipNumber = chipNumber;
	}
	
	public boolean isTransportOrdered() {
		return this.transportOrdered;
	}

	public void setTransportOrdered(boolean transportOrdered) {
		this.noTransportOrdered = !transportOrdered;
		this.transportOrdered = transportOrdered;
	}

	public boolean isNoTransportOrdered() {
		return this.noTransportOrdered;
	}

	public void setNoTransportOrdered(boolean noTransportOrdered) {
		this.transportOrdered = !noTransportOrdered;
		this.noTransportOrdered = noTransportOrdered;
	}

	public String getShirtSize() {
		return this.shirtSize;
	}

	public void setShirtSize(String shirtSize) {
		this.shirtSize = shirtSize;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail2() {
		return this.email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getHomePhone() {
		return this.homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Country getNationality() {
		return this.nationality;
	}

	public void setNationality(Country nationality) {
		this.nationality = nationality;
	}

	public String getPostalCode() {
		return this.postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPersonalID() {
		return this.personalID;
	}

	public void setPersonalID(String personalID) {
		this.personalID = personalID;
	}
	
	public Gender getGender() {
		return this.gender;
	}
	
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	
	public boolean isAgree() {
		return this.agree;
	}

	
	public void setAgree(boolean agree) {
		this.agree = agree;
	}

	
	public float getAmount() {
		return this.amount;
	}

	
	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Charity getCharity() {
		return charity;
	}

	public void setCharity(Charity charity) {
		this.charity = charity;
	}

	public boolean isMaySponsorContactRunner() {
		return maySponsorContactRunner;
	}

	public void setMaySponsorContactRunner(boolean mayContactRunner) {
		this.maySponsorContactRunner = mayContactRunner;
	}

	public boolean isParticipateInCharity() {
		return participateInCharity;
	}

	public void setParticipateInCharity(boolean participateInCharity) {
		this.participateInCharity = participateInCharity;
	}

	public RunCategory getCategory() {
		return category;
	}

	public void setCategory(RunCategory category) {
		this.category = category;
	}
	
	public Year getYear(){
		Run run = getRun();
		if(run!=null){
			return run.getCurrentRegistrationYear();
		}
		return null;
	}

	public void setRunId(String runId) {
		try {
			setRun(ConverterUtility.getInstance().convertGroupToRun(new Integer(runId)));
		}
		catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public boolean isApplyForDomesticTravelSupport() {
		return applyForDomesticTravelSupport;
	}

	
	public void setApplyForDomesticTravelSupport(boolean applyForDomesticTravelSupport) {
		this.applyForDomesticTravelSupport = applyForDomesticTravelSupport;
	}

	
	public boolean isApplyForInternationalTravelSupport() {
		return applyForInternationalTravelSupport;
	}

	public void setApplyForInternationalTravelSupport(boolean applyForInternationalTravelSupport) {
		this.applyForInternationalTravelSupport = applyForInternationalTravelSupport;
	}

	public boolean isSponsoredRunner() {
		return this.sponsoredRunner;
	}

	public void setSponsoredRunner(boolean sponsoredRunner) {
		this.sponsoredRunner = sponsoredRunner;
	}

	public boolean getAllowsEmails() {
		return this.allowsEmails;
	}
	
	public void setAllowsEmails(boolean allowsEmails) {
		this.allowsEmails = allowsEmails;
		this.notAllowsEmails = !allowsEmails;
	}

	public boolean getNotAllowsEmails() {
		return this.notAllowsEmails;
	}
	
	public void setNotAllowsEmails(boolean notAllowsEmails) {
		this.notAllowsEmails = notAllowsEmails;
		this.allowsEmails = !notAllowsEmails;
	}

	public boolean getCheckQuestions() {
		return this.checkQuestions;
	}
	
	public void setCheckQuestions(boolean checkQuestions) {
		this.checkQuestions = checkQuestions;
	}
	
	public List getDisallowedDistancesPKs(List distances) {
		
		Date birthDate = getDateOfBirth() != null ? getDateOfBirth() : getUser().getDateOfBirth();
		int age = new Age(birthDate).getYears();
		List disallowed = new ArrayList();
		
		for (Iterator iterator = distances.iterator(); iterator.hasNext();) {
			
			Distance distance = (Distance)iterator.next();
			
			if((distance.getMinimumAgeForDistance() > 0 && age < distance.getMinimumAgeForDistance()) || (distance.getMaximumAgeForDistance() > 0 && age > distance.getMaximumAgeForDistance()))
				disallowed.add(distance.getPrimaryKey().toString());
		}
		
		return disallowed;
	}
	
	public String getQuestion1Hour() {
		return this.question1Hour;
	}
	
	public void setQuestion1Hour(String hour) {
		this.question1Hour = hour;
	}

	public String getQuestion1Minute() {
		return this.question1Minute;
	}
	
	public void setQuestion1Minute(String minute) {
		this.question1Minute = minute;
	}

	public String getQuestion1Year() {
		return this.question1Year;
	}
	
	public void setQuestion1Year(String year) {
		this.question1Year = year;
	}
	
	public boolean getQuestion1NeverRan() {
		return this.question1NeverRan;
	}
	
	public void setQuestion1NeverRan(boolean neverRan) {
		this.question1NeverRan = neverRan;
	}
	
	public String getQuestion2Hour() {
		return this.question2Hour;
	}
	
	public void setQuestion2Hour(String hour) {
		this.question2Hour = hour;
	}

	public String getQuestion2Minute() {
		return this.question2Minute;
	}
	
	public void setQuestion2Minute(String minute) {
		this.question2Minute = minute;
	}
	
	public String getQuestion3Hour() {
		return this.question3Hour;
	}
	
	public void setQuestion3Hour(String hour) {
		this.question3Hour = hour;
	}

	public String getQuestion3Minute() {
		return this.question3Minute;
	}
	
	public void setQuestion3Minute(String minute) {
		this.question3Minute = minute;
	}
	
	public String getQuestion3Year() {
		return this.question3Year;
	}
	
	public void setQuestion3Year(String year) {
		this.question3Year = year;
	}
	
	public boolean getQuestion3NeverRan() {
		return this.question3NeverRan;
	}
	
	public void setQuestion3NeverRan(boolean neverRan) {
		this.question3NeverRan = neverRan;
	}

	//Relay stuff
	public String getPartner1SSN() {
		return this.relayPartner1SSN;
	}
	
	public void setPartner1SSN(String ssn) {
		this.relayPartner1SSN = ssn;
	}
	
	public String getPartner1Name() {
		return this.relayPartner1Name;
	}
	
	public void setPartner1Name(String name) {
		this.relayPartner1Name = name;
	}

	public String getPartner1Email() {
		return this.relayPartner1Email;
	}
	
	public void setPartner1Email(String email) {
		this.relayPartner1Email = email;
	}

	public String getPartner1ShirtSize() {
		return this.relayPartner1ShirtSize;
	}
	
	public void setPartner1ShirtSize(String shirtSize) {
		this.relayPartner1ShirtSize = shirtSize;
	}

	public String getPartner1Leg() {
		return this.relayPartner1Leg;
	}
	
	public void setPartner1Leg(String leg) {
		this.relayPartner1Leg = leg;
	}
	
	public String getPartner2SSN() {
		return this.relayPartner2SSN;
	}
	
	public void setPartner2SSN(String ssn) {
		this.relayPartner2SSN = ssn;
	}
	
	public String getPartner2Name() {
		return this.relayPartner2Name;
	}
	
	public void setPartner2Name(String name) {
		this.relayPartner2Name = name;
	}

	public String getPartner2Email() {
		return this.relayPartner2Email;
	}
	
	public void setPartner2Email(String email) {
		this.relayPartner2Email = email;
	}

	public String getPartner2ShirtSize() {
		return this.relayPartner2ShirtSize;
	}
	
	public void setPartner2ShirtSize(String shirtSize) {
		this.relayPartner2ShirtSize = shirtSize;
	}

	public String getPartner2Leg() {
		return this.relayPartner2Leg;
	}
	
	public void setPartner2Leg(String leg) {
		this.relayPartner2Leg = leg;
	}

	public String getPartner3SSN() {
		return this.relayPartner3SSN;
	}
	
	public void setPartner3SSN(String ssn) {
		this.relayPartner3SSN = ssn;
	}
	
	public String getPartner3Name() {
		return this.relayPartner3Name;
	}
	
	public void setPartner3Name(String name) {
		this.relayPartner3Name = name;
	}

	public String getPartner3Email() {
		return this.relayPartner3Email;
	}
	
	public void setPartner3Email(String email) {
		this.relayPartner3Email = email;
	}

	public String getPartner3ShirtSize() {
		return this.relayPartner3ShirtSize;
	}
	
	public void setPartner3ShirtSize(String shirtSize) {
		this.relayPartner3ShirtSize = shirtSize;
	}

	public String getPartner3Leg() {
		return this.relayPartner3Leg;
	}
	
	public void setPartner3Leg(String leg) {
		this.relayPartner3Leg = leg;
	}
	
	public String getRelayLeg() {
		return this.relayLeg;
	}
	
	public void setRelayLeg(String leg) {
		this.relayLeg = leg;
	}
}