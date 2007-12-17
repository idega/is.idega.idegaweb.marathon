/*
 * $Id: Runner.java,v 1.13 2007/12/17 13:39:17 civilis Exp $ Created on May 16, 2005
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
import com.idega.util.IWTimestamp;

/**
 * A holder class for information about runners and their selection when
 * registering.
 * 
 * Last modified: $Date: 2007/12/17 13:39:17 $ by $Author: civilis $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.13 $
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

	public List getDisallowedDistancesPKs(List distances) {
		
		List disallowed = new ArrayList();
		
		int currentYear = new IWTimestamp().getYear();
		int userBirthYear = new IWTimestamp(getDateOfBirth()).getYear();
		
		if(currentYear < userBirthYear)
			throw new IllegalArgumentException("Current year lower than birth year. User birth year: "+userBirthYear+", current year: "+currentYear);
		
		int age = currentYear - userBirthYear;
		
		for (Iterator iterator = distances.iterator(); iterator.hasNext();) {
			
			Distance distance = (Distance)iterator.next();
			
			if(age < distance.getMinimumAgeForDistance() || age > distance.getMaximumAgeForDistance())
				disallowed.add(distance.getPrimaryKey().toString());
		}
		
		return disallowed;
	}
}