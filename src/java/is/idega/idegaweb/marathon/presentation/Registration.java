/*
 * $Id: Registration.java,v 1.51 2007/03/09 12:37:14 idegaweb Exp $
 * Created on May 16, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.business.Runner;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOCreateException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.Age;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
import com.idega.util.LocaleUtil;


/**
 * Last modified: $Date: 2007/03/09 12:37:14 $ by $Author: idegaweb $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.51 $
 */
public class Registration extends RunBlock {
	
	private static final String SESSION_ATTRIBUTE_RUNNER_MAP = "sa_runner_map";
	public static final String SESSION_ATTRIBUTE_PARTICIPANTS = "sa_participants";
	public static final String SESSION_ATTRIBUTE_AMOUNT = "sa_amount";
	public static final String SESSION_ATTRIBUTE_CARD_NUMBER = "sa_card_number";
	public static final String SESSION_ATTRIBUTE_PAYMENT_DATE = "sa_payment_date";

	private static final String PROPERTY_CHIP_PRICE_ISK = "chip_price_ISK";
	private static final String PROPERTY_CHIP_PRICE_EUR = "chip_price_EUR";
	private static final String PROPERTY_CHIP_DISCOUNT_ISK = "chip_discount_ISK";
	private static final String PROPERTY_CHIP_DISCOUNT_EUR = "chip_discount_EUR";
	private static final String PROPERTY_CHILD_DISCOUNT_ISK = "chip_discount_ISK";

	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_FROM_ACTION = "prm_from_action";
	
	private static final String PARAMETER_RUN = "prm_run";
	private static final String PARAMETER_DISTANCE = "prm_distance";
	private static final String PARAMETER_PERSONAL_ID = "prm_personal_id";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_ADDRESS = "prm_address";
	private static final String PARAMETER_POSTAL_CODE = "prm_postal_code";
	private static final String PARAMETER_CITY = "prm_city";
	private static final String PARAMETER_COUNTRY = "prm_country";
	private static final String PARAMETER_GENDER = "prm_gender";
	private static final String PARAMETER_NATIONALITY = "prm_nationality";
	private static final String PARAMETER_EMAIL = "prm_email";
	private static final String PARAMETER_HOME_PHONE = "prm_home_phone";
	private static final String PARAMETER_MOBILE_PHONE = "prm_mobile_phone";
	private static final String PARAMETER_SHIRT_SIZE = "prm_shirt_size";
	private static final String PARAMETER_CHIP = "prm_chip";
	private static final String PARAMETER_CHIP_NUMBER = "prm_chip_number";
	private static final String PARAMETER_TRANSPORT = "prm_transport";
	private static final String PARAMETER_AGREE = "prm_agree";
	
	private static final String PARAMETER_NAME_ON_CARD = "prm_name_on_card";
	private static final String PARAMETER_CARD_NUMBER = "prm_card_number";
	private static final String PARAMETER_EXPIRES_MONTH = "prm_expires_month";
	private static final String PARAMETER_EXPIRES_YEAR = "prm_expires_year";
	private static final String PARAMETER_CCV = "prm_ccv";
	private static final String PARAMETER_AMOUNT = "prm_amount";
	private static final String PARAMETER_CARD_HOLDER_EMAIL = "prm_card_holder_email";
	private static final String PARAMETER_REFERENCE_NUMBER = "prm_reference_number";
	private static final String PARAMETER_SHIRT_SIZES_PER_RUN = "shirt_sizes_per_run";

	private static final int ACTION_STEP_ONE = 1;
	private static final int ACTION_STEP_TWO = 2;
	private static final int ACTION_STEP_THREE = 3;
	private static final int ACTION_STEP_THREE_TRANSPORT = 30;
	private static final int ACTION_STEP_FOUR = 4;
	private static final int ACTION_STEP_FIVE = 5;
	private static final int ACTION_STEP_SIX = 6;
	private static final int ACTION_SAVE = 7;
	private static final int ACTION_CANCEL = 8;

	private boolean isIcelandic = false;
	private float chipPrice;
	private float chipDiscount;
	private float childDiscount = 0;
	private Runner runner;
	private boolean disableTransportStep = false;
	
	public void main(IWContext iwc) throws Exception {
		this.isIcelandic = iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale());
		if (this.isIcelandic) {
			this.chipPrice = Float.parseFloat(getBundle().getProperty(PROPERTY_CHIP_PRICE_ISK, "2700"));
			this.chipDiscount = Float.parseFloat(getBundle().getProperty(PROPERTY_CHIP_DISCOUNT_ISK, "300"));
			this.childDiscount = Float.parseFloat(getBundle().getProperty(PROPERTY_CHILD_DISCOUNT_ISK, "300"));
		}
		else {
			this.chipPrice = Float.parseFloat(getBundle().getProperty(PROPERTY_CHIP_PRICE_EUR, "33"));
			this.chipDiscount = Float.parseFloat(getBundle().getProperty(PROPERTY_CHIP_DISCOUNT_EUR, "3"));
		}

		switch (parseAction(iwc)) {
			case ACTION_STEP_ONE:
				stepOne(iwc);
				break;
			case ACTION_STEP_TWO:
				stepTwo(iwc);
				break;
			case ACTION_STEP_THREE:
				stepThree(iwc);
				break;
			case ACTION_STEP_THREE_TRANSPORT:
				stepThreeTransport(iwc);
				break;
			case ACTION_STEP_FOUR:
				stepFour(iwc);
				break;
			case ACTION_STEP_FIVE:
				stepFive(iwc);
				break;
			case ACTION_STEP_SIX:
				stepSix(iwc);
				break;
			case ACTION_SAVE:
				save(iwc, true);
				break;
			case ACTION_CANCEL:
				cancel(iwc);
				break;
		}
	}
	
	private void stepOne(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.addParameter(PARAMETER_ACTION, ACTION_STEP_TWO);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(1, 7, "run_reg.registration", "Registration"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize("run_reg.information_text_step_1", "Information text 1...")), 1, row++);
		table.setHeight(row++, 6);
		
		table.setCellpadding(1, row, 24);
		table.add(getHeader(localize("run_reg.personal_id", "Personal ID") + ":"), 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		
		TextInput input = (TextInput) getStyledInterface(new TextInput(PARAMETER_PERSONAL_ID));
		input.setAsIcelandicSSNumber(localize("run_reg.not_valid_personal_id", "The personal ID you've entered is not valid"));
		input.setLength(10);
		input.setMaxlength(10);
		input.setInFocusOnPageLoad(true);
		table.add(input, 1, row++);
		
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}

	private void stepTwo(IWContext iwc) throws RemoteException {
		Form form = new Form();
		if (this.isIcelandic) {
			form.maintainParameter(PARAMETER_PERSONAL_ID);
		}
		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_TWO);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(this.isIcelandic ? 2 : 1, this.isIcelandic ? 7 : 6, "run_reg.registration", "Registration"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize("run_reg.information_text_step_2", "Information text 2...")), 1, row++);
		table.setHeight(row++, 18);
		
		Table choiceTable = new Table();
		choiceTable.setColumns(3);
		choiceTable.setCellpadding(2);
		choiceTable.setCellspacing(0);
		choiceTable.setWidth(1, "50%");
		choiceTable.setWidth(2, 12);
		choiceTable.setWidth(3, "50%");
		choiceTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(choiceTable, 1, row++);
		int iRow = 1;
		
		IWTimestamp ts = IWTimestamp.RightNow();
    Integer y = new Integer(ts.getYear());
    String yearString = y.toString();
    IWTimestamp stamp = new IWTimestamp();
    stamp.addYears(1);
    String nextYearString = String.valueOf(stamp.getYear());
    
		boolean hasRuns = false;
		DropdownMenu runDropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_RUN));
		Collection runs = getRunBusiness(iwc).getRuns();
		runDropdown.addMenuElement("-1", localize("run_year_ddd.select_run","Select run..."));
		if(runs != null) {
			Iterator iter = runs.iterator();
			while (iter.hasNext()) {
				Group run = (Group) iter.next();
				String runnerYearString = yearString;
				
				boolean show = false;
				boolean finished = true;
				Map yearMap = getRunBusiness(iwc).getYearsMap(run);
				Year year = (Year) yearMap.get(yearString);
				if (year != null && year.getLastRegistrationDate() != null) {
					IWTimestamp runDate = new IWTimestamp(year.getLastRegistrationDate());
					if (ts.isEarlierThan(runDate)) {
						finished = false;
						show = true;
					}
				}
				Year nextYear = (Year) yearMap.get(nextYearString);
				if (finished && nextYear != null) {
					runnerYearString = nextYearString;
					show = true;
				}
				// this.runner.getUser() != null 
				// adda parameter member_ID
				

				if (show) {
					if (this.runner.getUser() != null) {
						if (!getRunBusiness(iwc).isRegisteredInRun(runnerYearString, run, this.runner.getUser())) {
							runDropdown.addMenuElement(run.getPrimaryKey().toString(), localize(run.getName(), run.getName()) + " - " + runnerYearString);
							hasRuns = true;
						}
					}
					else {
						runDropdown.addMenuElement(run.getPrimaryKey().toString(), localize(run.getName(), run.getName()) + " - " + runnerYearString);
						hasRuns = true;
					}
				}
			}
		}
		if (this.runner.getRun() != null) {
			runDropdown.setSelectedElement(this.runner.getRun().getPrimaryKey().toString());
		}
		runDropdown.setAsNotEmpty(localize("run_reg.must_select_run", "You have to select a run"));
		if (!hasRuns) {
			getParentPage().setAlertOnLoad(localize("run_reg.no_runs_available", "There are no runs you can register for."));
			if (this.isIcelandic) {
				removeRunner(iwc, this.runner.getPersonalID());
				stepOne(iwc);
				return;
			}
		}
		
		DropdownMenu distanceDropdown = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_DISTANCE));
		distanceDropdown.addMenuElement("-1", localize("run_year_ddd.select_distance","Select distance..."));
		if(this.runner.getRun() != null) {
			String runnerYearString = yearString;
			boolean finished = false;
			Map yearMap = getRunBusiness(iwc).getYearsMap(this.runner.getRun());
			Year year = (Year) yearMap.get(yearString);
			if (year != null && year.getLastRegistrationDate() != null) {
				if (ts.isLaterThanOrEquals(stamp)) {
					finished = true;
				}
			}
			Year nextYear = (Year) yearMap.get(nextYearString);
			if (finished && nextYear != null) {
				runnerYearString = nextYearString;
			}

			Collection distances = getRunBusiness(iwc).getDistancesMap(this.runner.getRun(), runnerYearString);
			if(distances != null) {
				Iterator distanceIt = distances.iterator();
				while (distanceIt.hasNext()) {
					Group distance = (Group)distanceIt.next();
					distanceDropdown.addMenuElement(distance.getPrimaryKey().toString(), localize(distance.getName(),distance.getName()));
			    }
			}
			if (this.runner.getDistance() != null) {
				distanceDropdown.setSelectedElement(this.runner.getDistance().getPrimaryKey().toString());
			}
		}
		distanceDropdown.setAsNotEmpty(localize("run_reg.must_select_distance", "You have to select a distance"));

		Text redStar = getHeader("*");
		redStar.setFontColor("#ff0000");

		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_PRIMARY_DD, "Run") + "/" + localize(IWMarathonConstants.RR_SECONDARY_DD, "Distance")), 1, iRow);
		choiceTable.add(redStar, 1, iRow++);
		choiceTable.mergeCells(1, iRow, choiceTable.getColumns(), iRow);
		choiceTable.add(runDropdown, 1, iRow);
		choiceTable.add(distanceDropdown, 1, iRow++);
		
		RemoteScriptHandler rsh = new RemoteScriptHandler(runDropdown, distanceDropdown);
		try {
			rsh.setRemoteScriptCollectionClass(RunInputCollectionHandler.class);
			if (this.runner.getUser() != null) {
				rsh.addParameter(RunInputCollectionHandler.PARAMETER_USER_ID, this.runner.getUser().getPrimaryKey().toString());
			}
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		add(rsh);

		choiceTable.setHeight(iRow++, 12);
		
		TextInput nameField = (TextInput) getStyledInterface(new TextInput(PARAMETER_NAME));
		nameField.setWidth(Table.HUNDRED_PERCENT);
		if (this.runner.getName() != null) {
			nameField.setContent(this.runner.getName());
		}
		if (this.isIcelandic) {
			nameField.setDisabled(true);
			if (this.runner.getUser() != null) {
				nameField.setContent(this.runner.getUser().getName());
			}
		}
		else {
			nameField.setAsAlphabeticText(localize("run_reg.name_err_msg", "Your name may only contain alphabetic characters"));
			nameField.setAsNotEmpty(localize("run_reg.name_not_empty", "Name field can not be empty"));
		}

		DropdownMenu genderField = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_GENDER));
		Collection genders = getGenderBusiness(iwc).getAllGenders();
		genderField.addMenuElement("-1", localize("run_reg.select_gender","Select gender..."));
		if(genders != null) {
			Iterator iter = genders.iterator();
			while (iter.hasNext()) {
				Gender gender = (Gender) iter.next();
				genderField.addMenuElement(gender.getPrimaryKey().toString(), localize("gender." + gender.getName(), gender.getName()));
			}
		}
		if (this.runner.getGender() != null) {
			genderField.setSelectedElement(this.runner.getGender().getPrimaryKey().toString());
		}
		if (this.isIcelandic) {
			genderField.setDisabled(true);
			if (this.runner.getUser() != null) {
				genderField.setSelectedElement(this.runner.getUser().getGenderID());
			}
		}
		else {
			genderField.setAsNotEmpty(localize("run_reg.gender_not_empty", "Gender can not be empty"));
		}

		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_NAME, "Name")), 1, iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_GENDER, "Gender")), 3, iRow);
		choiceTable.add(redStar, 3, iRow++);
		choiceTable.add(nameField, 1, iRow);
		choiceTable.add(genderField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		TextInput ssnISField = (TextInput) getStyledInterface(new TextInput(PARAMETER_PERSONAL_ID));
		ssnISField.setLength(10);
		if (this.isIcelandic) {
			ssnISField.setDisabled(true);
			if (this.runner.getUser() != null) {
				ssnISField.setContent(this.runner.getUser().getPersonalID());
			}
		}
		
		IWTimestamp stampNow = new IWTimestamp();
		stampNow.addYears(-3);

		IWTimestamp birthStamp = new IWTimestamp();
		DateInput ssnField = (DateInput) getStyledInterface(new DateInput(PARAMETER_PERSONAL_ID));
		ssnField.setAsNotEmpty("Date of birth can not be empty");
		ssnField.setYearRange(birthStamp.getYear(), birthStamp.getYear() - 100);
		ssnField.setLatestPossibleDate(stampNow.getDate(), "Invalid date of birth.  Please check the date you have selected and try again");
		if (this.runner.getDateOfBirth() != null) {
			ssnField.setDate(this.runner.getDateOfBirth());
		}

		Collection countries = getRunBusiness(iwc).getCountries();
		DropdownMenu nationalityField = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_NATIONALITY));
		DropdownMenu countryField = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_COUNTRY));
		nationalityField.addMenuElement("-1", localize("run_reg.select_nationality", "Select nationality..."));
		countryField.addMenuElement("-1", localize("run_reg.select_country", "Select country..."));
		SelectorUtility util = new SelectorUtility();
		if (countries != null && !countries.isEmpty()) {
			nationalityField = (DropdownMenu) util.getSelectorFromIDOEntities(nationalityField, countries, "getName");
			countryField = (DropdownMenu) util.getSelectorFromIDOEntities(countryField, countries, "getName");
		}
		if (this.isIcelandic) {
			countryField.setDisabled(true);
			nationalityField.setSelectedElement("104");
			if (this.runner.getUser() != null) {
				Address address = getUserBusiness(iwc).getUsersMainAddress(this.runner.getUser());
				if (address != null && address.getCountry() != null) {
					countryField.setSelectedElement(address.getCountry().getPrimaryKey().toString());
				}
			}
		}
		nationalityField.setWidth(Table.HUNDRED_PERCENT);
		nationalityField.setAsNotEmpty(localize("run_reg.must_select_nationality", "You must select your nationality"));
		countryField.setWidth(Table.HUNDRED_PERCENT);
		if (!this.isIcelandic) {
			countryField.setAsNotEmpty(localize("run_reg.must_select_country", "You must select your country"));
		}
		if (this.runner.getCountry() != null) {
			countryField.setSelectedElement(this.runner.getCountry().getPrimaryKey().toString());
		}
		if (this.runner.getNationality() != null) {
			nationalityField.setSelectedElement(this.runner.getNationality().getPrimaryKey().toString());
		}
		
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_SSN, "SSN")), 1, iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_NATIONALITY, "Nationality")), 3, iRow);
		choiceTable.add(redStar, 3, iRow++);
		if (this.isIcelandic) {
			choiceTable.add(ssnISField, 1, iRow);
		}
		else {
			choiceTable.add(ssnField, 1, iRow);
		}
		choiceTable.add(nationalityField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);
		
		TextInput addressField = (TextInput) getStyledInterface(new TextInput(PARAMETER_ADDRESS));
		addressField.setWidth(Table.HUNDRED_PERCENT);
		if (!this.isIcelandic) {
			addressField.setAsNotEmpty(localize("run_reg.must_provide_address", "You must enter your address."));
		}
		if (this.runner.getAddress() != null) {
			addressField.setContent(this.runner.getAddress());
		}
		if (this.isIcelandic) {
			addressField.setDisabled(true);
			if (this.runner.getUser() != null) {
				Address address = getUserBusiness(iwc).getUsersMainAddress(this.runner.getUser());
				if (address != null) {
					addressField.setContent(address.getStreetAddress());
				}
			}
		}

		TextInput emailField = (TextInput) getStyledInterface(new TextInput(PARAMETER_EMAIL));
		emailField.setAsEmail(localize("run_reg.email_err_msg", "Not a valid email address"));
		emailField.setEmptyConfirm(localize("run_reg.continue_without_email", "Are you sure you want to continue without entering an e-mail?"));
		emailField.setWidth(Table.HUNDRED_PERCENT);
		if (this.runner.getEmail() != null) {
			emailField.setContent(this.runner.getEmail());
		}
		else if (this.runner.getUser() != null) {
			try {
				Email mail = getUserBusiness(iwc).getUsersMainEmail(this.runner.getUser());
				emailField.setContent(mail.getEmailAddress());
			}
			catch (NoEmailFoundException nefe) {
				//No email registered...
			}
		}
		
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_ADDRESS, "Address")), 1, iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_EMAIL, "Email")), 3, iRow++);
		choiceTable.add(addressField, 1, iRow);
		choiceTable.add(emailField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		TextInput cityField = (TextInput) getStyledInterface(new TextInput(PARAMETER_CITY));
		cityField.setWidth(Table.HUNDRED_PERCENT);
		if (!this.isIcelandic) {
			cityField.setAsNotEmpty(localize("run_reg.must_provide_city", "You must enter your city of living."));
		}
		if (this.runner.getCity() != null) {
			cityField.setContent(this.runner.getCity());
		}
		if (this.isIcelandic) {
			cityField.setDisabled(true);
			if (this.runner.getUser() != null) {
				Address address = getUserBusiness(iwc).getUsersMainAddress(this.runner.getUser());
				if (address != null) {
					cityField.setContent(address.getCity());
				}
			}
		}

		TextInput telField = (TextInput) getStyledInterface(new TextInput(PARAMETER_HOME_PHONE));
		telField.setWidth(Table.HUNDRED_PERCENT);
		if (this.runner.getHomePhone() != null) {
			telField.setContent(this.runner.getHomePhone());
		}
		else if (this.runner.getUser() != null) {
			try {
				Phone phone = getUserBusiness(iwc).getUsersHomePhone(this.runner.getUser());
				telField.setContent(phone.getNumber());
			}
			catch (NoPhoneFoundException nefe) {
				//No phone registered...
			}
		}

		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_CITY, "City")), 1, iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_TEL, "Telephone")), 3, iRow++);
		choiceTable.add(cityField, 1, iRow);
		choiceTable.add(telField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		TextInput postalField = (TextInput) getStyledInterface(new TextInput(PARAMETER_POSTAL_CODE));
		if (!this.isIcelandic) {
			postalField.setAsNotEmpty(localize("run_reg.must_provide_postal", "You must enter your postal address."));
		}
		postalField.setMaxlength(10);
		postalField.setLength(10);
		if (this.runner.getPostalCode() != null) {
			postalField.setContent(this.runner.getPostalCode());
		}
		if (this.isIcelandic) {
			postalField.setDisabled(true);
			if (this.runner.getUser() != null) {
				Address address = getUserBusiness(iwc).getUsersMainAddress(this.runner.getUser());
				if (address != null) {
					PostalCode postal = address.getPostalCode();
					if (postal != null) {
						postalField.setContent(postal.getPostalCode());
					}
				}
			}
		}

		TextInput mobileField = (TextInput) getStyleObject(new TextInput(PARAMETER_MOBILE_PHONE), STYLENAME_INTERFACE);
		mobileField.setWidth(Table.HUNDRED_PERCENT);
		if (this.runner.getMobilePhone() != null) {
			mobileField.setContent(this.runner.getMobilePhone());
		}
		else if (this.runner.getUser() != null) {
			try {
				Phone phone = getUserBusiness(iwc).getUsersMobilePhone(this.runner.getUser());
				mobileField.setContent(phone.getNumber());
			}
			catch (NoPhoneFoundException nefe) {
				//No phone registered...
			}
		}

		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_POSTAL, "Postal Code")), 1, iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_MOBILE, "Mobile Phone")), 3, iRow++);
		choiceTable.add(postalField, 1, iRow);
		choiceTable.add(mobileField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		DropdownMenu tShirtField = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_SHIRT_SIZE));
		tShirtField.addMenuElement("-1", localize("run_reg.select_tee_shirt_size","Select tee-shirt size"));
		if(this.runner.getDistance() != null) {
			String shirtSizeMetadata = this.runner.getDistance().getMetaData(PARAMETER_SHIRT_SIZES_PER_RUN);
			List shirtSizes = null;
			if (shirtSizeMetadata != null) {
				shirtSizes = ListUtil. convertCommaSeparatedStringToList(shirtSizeMetadata);
			}
			if(shirtSizes != null) {
				Iterator shirtIt = shirtSizes.iterator();
				while (shirtIt.hasNext()) {
					String shirtSizeKey = (String) shirtIt.next();
					tShirtField.addMenuElement(shirtSizeKey, localize("shirt_size."+shirtSizeKey,shirtSizeKey));
			    }
			}
			if (this.runner.getDistance() != null) {
				tShirtField.setSelectedElement(this.runner.getShirtSize());
			}
		}
		tShirtField.setAsNotEmpty(localize("run_reg.must_select_shirt_size", "You must select tee-shirt size"));

		RemoteScriptHandler rshShirts = new RemoteScriptHandler(distanceDropdown, tShirtField);
		try {
			rshShirts.setRemoteScriptCollectionClass(DistanceMenuShirtSizeMenuInputCollectionHandler.class);
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		add(rshShirts);

		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_COUNTRY, "Country")), 1, iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_TSHIRT, "T-Shirt")), 3, iRow);
		choiceTable.add(redStar, 3, iRow++);
		choiceTable.add(countryField, 1, iRow);
		choiceTable.add(tShirtField, 3, iRow++);

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_THREE));
		
		table.setHeight(row++, 18);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}

	private void stepThree(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_THREE);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(this.isIcelandic ? 3 : 2, this.isIcelandic ? 7 : 6, "run_reg.time_registration_chip", "Time registration chip"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize("run_reg.information_text_step_3", "Information text 3...")), 1, row++);
		table.setHeight(row++, 18);
		
		RadioButton rentChip = getRadioButton(PARAMETER_CHIP, IWMarathonConstants.CHIP_RENT);
		rentChip.setSelected(this.runner.isRentChip());
		rentChip.setMustBeSelected(localize("run_reg.must_select_chip_option", "You have to select a chip option"));
		
		table.add(rentChip, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getHeader(localize("run_reg.rent_chip", "I don't own a chip")), 1, row++);
		table.setHeight(row++, 6);
		table.add(getText(localize("run_reg.rent_chip_information", "A one use chip is included in the entry fee and will be delivered with other tournament stuff.")), 1, row);
		table.setBottomCellBorder(1, row, 1, "#D7D7D7", "solid");
		table.setCellpaddingBottom(1, row++, 6);
		
		RadioButton ownChip = getRadioButton(PARAMETER_CHIP, IWMarathonConstants.CHIP_OWN);
		ownChip.setSelected(this.runner.isOwnChip());
		TextInput chipNumber = (TextInput) getStyledInterface(new TextInput(PARAMETER_CHIP_NUMBER));
		chipNumber.setLength(7);
		chipNumber.setMaxlength(7);
		if (this.runner.getChipNumber() != null) {
			chipNumber.setContent(this.runner.getChipNumber());
		}

		table.setHeight(row++, 12);
		table.add(ownChip, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getHeader(localize("run_reg.own_chip", "I own a chip, number")), 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(chipNumber, 1, row);
		table.setBottomCellBorder(1, row, 1, "#D7D7D7", "solid");
		table.setCellpaddingBottom(1, row++, 6);
		
		RadioButton buyChip = getRadioButton(PARAMETER_CHIP, IWMarathonConstants.CHIP_BUY);
		buyChip.setSelected(this.runner.isBuyChip());
		
		table.setHeight(row++, 12);
		table.add(buyChip, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getHeader(localize("run_reg.buy_chip", "I want to buy a multi use chip")), 1, row++);
		table.setHeight(row++, 6);
		String priceText = formatAmount(iwc.getCurrentLocale(), this.chipPrice);
		table.add(getText(localize("run_reg.buy_chip_information", "You can buy a multi use chip that you can use in future tournaments.  The price of the chip is ") + priceText), 1, row++);
		
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_TWO));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_THREE_TRANSPORT));

		table.setHeight(row++, 18);
		table.add(previous, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}
	
	private void stepThreeTransport(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_THREE_TRANSPORT);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(this.isIcelandic ? 3 : 2, this.isIcelandic ? 7 : 6, "run_reg.order_transport", "Order transport"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize("run_reg.information_text_step_3_transport", "Bus trip to race starting point and back to Reykjavik after the race is organized by Reykjavik Marathon. Please select if you like to order a seat or not.")), 1, row++);
		table.setHeight(row++, 18);
		
		RadioButton orderTransport = getRadioButton(PARAMETER_TRANSPORT, Boolean.TRUE.toString());
		orderTransport.setSelected(this.runner.isTransportOrdered());
		orderTransport.setMustBeSelected(localize("run_reg.must_select_transport_option", "You must select bus trip option."));
		
		RadioButton notOrderTransport = getRadioButton(PARAMETER_TRANSPORT, Boolean.FALSE.toString());
		notOrderTransport.setSelected(this.runner.isNoTransportOrdered());

		table.add(orderTransport, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		Distance distance = runner.getDistance();
		String distancePriceString = "";
		if (distance != null) {
			distancePriceString = formatAmount(iwc.getCurrentLocale(), distance.getPriceForTransport(iwc.getCurrentLocale()));
		}
		Object[] args = { distancePriceString };
		table.add(getHeader(MessageFormat.format(localize("run_reg.order_transport_text", "I want to order a bus trip. The price is: {0}"), args)), 1, row);
		table.setHeight(row++, 6);
		
		table.add(getText((localize("run_reg.order_tranport_information_"+runner.getRun().getName().replace(' ', '_'), "Info about transport order..."))), 1, row);
		table.setBottomCellBorder(1, row, 1, "#D7D7D7", "solid");
		table.setCellpaddingBottom(1, row++, 6);
		table.add(notOrderTransport, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getHeader(localize("run_reg.not_order_transport_text", "I don't want to order bus trip.")), 1, row);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_THREE));
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_FOUR));

		table.setHeight(row++, 18);
		table.add(previous, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}

	private void stepFour(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_FOUR);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(this.isIcelandic ? 4 : 3, this.isIcelandic ? 7 : 6, "run_reg.consent", "Consent"), 1, row++);
		table.setHeight(row++, 18);

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_FIVE));
		if (!this.runner.isAgree()) {
			next.setDisabled(true);
		}

		CheckBox agree = getCheckBox(PARAMETER_AGREE, Boolean.TRUE.toString());
		agree.setToEnableWhenChecked(next);
		agree.setToDisableWhenUnchecked(next);
		agree.setChecked(this.runner.isAgree());
		
		table.add(getText(localize("run_reg.information_text_step_4", "Information text 4...")), 1, row++);
		table.setHeight(row++, 6);
		table.add(agree, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getHeader(localize("run_reg.agree_terms", "Yes, I agree")), 1, row++);
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_THREE_TRANSPORT));

		table.setHeight(row++, 18);
		table.add(previous, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}

	private void stepFive(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.addParameter(PARAMETER_ACTION, "-1");
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(this.isIcelandic ? 5 : 4, this.isIcelandic ? 7 : 6, "run_reg.overview", "Overview"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize("run_reg.information_text_step_5", "Information text 5...")), 1, row++);
		table.setHeight(row++, 18);
		
		Map runners = (Map) iwc.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		Table runnerTable = new Table(3, runners.size() + 1);
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		runnerTable.add(getHeader(localize("run_reg.runner_name", "Runner name")), 1, 1);
		runnerTable.add(getHeader(localize("run_reg.run", "Run")), 2, 1);
		runnerTable.add(getHeader(localize("run_reg.distance", "Distance")), 3, 1);
		table.add(runnerTable, 1, row++);
		int runRow = 2;
		
		Iterator iter = runners.values().iterator();
		while (iter.hasNext()) {
			Runner runner = (Runner) iter.next();
			if (runner.getUser() != null) {
				runnerTable.add(getText(runner.getUser().getName()), 1, runRow);
			}
			else {
				runnerTable.add(getText(runner.getName()), 1, runRow);
			}
			if (runner.getRun() != null) {
				runnerTable.add(getText(localize(runner.getRun().getName(), runner.getRun().getName())), 2, runRow);
				runnerTable.add(getText(localize(runner.getDistance().getName(), runner.getDistance().getName())), 3, runRow++);
			}
			else {
				removeRunner(iwc, runner.getPersonalID());
			}
		}
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_FOUR));
		SubmitButton registerOther = (SubmitButton) getButton(new SubmitButton(localize("run_reg.register_other", "Register other")));
		registerOther.setValueOnClick(PARAMETER_ACTION, this.isIcelandic ? String.valueOf(ACTION_STEP_ONE) : String.valueOf(String.valueOf(ACTION_STEP_TWO)));
		registerOther.setValueOnClick(PARAMETER_PERSONAL_ID, "");
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("run_reg.pay", "Pay")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_SIX));

		table.setHeight(row++, 18);
		table.add(previous, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(registerOther, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}

	private void stepSix(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.addParameter(PARAMETER_ACTION, "-1");
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(this.isIcelandic ? 6 : 5, this.isIcelandic ? 7 : 6, "run_reg.payment_info", "Payment info"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize("run_reg.information_text_step_6", "Information text 6...")), 1, row++);
		table.setHeight(row++, 18);
		
		Map runners = (Map) iwc.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		Table runnerTable = new Table();
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		runnerTable.setCellspacing(0);
		runnerTable.add(getHeader(localize("run_reg.runner_name", "Runner name")), 1, 1);
		runnerTable.add(getHeader(localize("run_reg.run", "Run")), 2, 1);
		runnerTable.add(getHeader(localize("run_reg.distance", "Distance")), 3, 1);
		runnerTable.add(getHeader(localize("run_reg.price", "Price")), 4, 1);
		table.add(runnerTable, 1, row++);
		table.setHeight(row++, 18);
		int runRow = 2;
		
		int numberOfChildren = this.isIcelandic ? getRunBusiness(iwc).getNumberOfChildren(runners.values()) : 0;
		int numberOfSiblings = this.isIcelandic ? getRunBusiness(iwc).getNumberOfSiblings(runners.values()) : 0;
		boolean useSiblingDiscount = false;
		int childNumber = 0;
		float totalAmount = 0;
		int chipsToBuy = 0;
		int transportToBuy = 0;
		Iterator iter = runners.values().iterator();
		boolean first = true;
		while (iter.hasNext()) {
			Runner runner = (Runner) iter.next();
			if (runner.getUser() != null) {
				runnerTable.add(getText(runner.getUser().getName()), 1, runRow);
			}
			else {
				runnerTable.add(getText(runner.getName()), 1, runRow);
			}
			if (isChild(runner)) {
				childNumber++;
			}
			runnerTable.add(getText(localize(runner.getRun().getName(), runner.getRun().getName())), 2, runRow);
			runnerTable.add(getText(localize(runner.getDistance().getName(), runner.getDistance().getName())), 3, runRow);
			float runPrice = getRunBusiness(iwc).getPriceForRunner(runner, iwc.getCurrentLocale(), this.chipDiscount, 0);
			totalAmount += runPrice;
			runnerTable.add(getText(formatAmount(iwc.getCurrentLocale(), runPrice)), 4, runRow++);
			if (numberOfChildren > 1 && childNumber > 1 && runPrice > 0) {
				runPrice -= this.childDiscount;
			}
			if (runner.getDistance().getName().equals(IWMarathonConstants.DISTANCE_1_5)) {
				useSiblingDiscount = true;
				if (numberOfSiblings > 2 && childNumber > 2 && runPrice > 0) {
					runPrice -= this.childDiscount;
				}
			}
			
			if (runner.isBuyChip()) {
				chipsToBuy++;
			}
			
			if (runner.isTransportOrdered()) {
				transportToBuy++;
			}
			
			runner.setAmount(runPrice);
			addRunner(iwc, runner.getPersonalID(), runner);
			
			if (first) {
				runnerTable.add(new HiddenInput(PARAMETER_REFERENCE_NUMBER, runner.getPersonalID().replaceAll("-", "")));
				first = false;
			}
		}
		
		if (totalAmount == 0) {
			save(iwc, false);
			return;
		}
		
		if (this.isIcelandic) {
			if (numberOfChildren > 1) {
				float childrenDiscount = -((numberOfChildren - 1) * this.childDiscount);
				totalAmount += childrenDiscount;
				
				runnerTable.setHeight(runRow++, 12);
				runnerTable.add(getText(localize("run_reg.family_discount", "Family discount")), 1, runRow);
				runnerTable.add(getText(formatAmount(iwc.getCurrentLocale(), childrenDiscount)), 4, runRow++);
			}
			if (useSiblingDiscount && numberOfSiblings > 2) {
				float childrenDiscount = -((numberOfSiblings - 2) * this.childDiscount);
				totalAmount += childrenDiscount;
				
				runnerTable.setHeight(runRow++, 12);
				runnerTable.add(getText(localize("run_reg.family_discount", "Family discount")), 1, runRow);
				runnerTable.add(getText(formatAmount(iwc.getCurrentLocale(), childrenDiscount)), 4, runRow++);
			}
		}
		
		if (chipsToBuy > 0) {
			float totalChips = chipsToBuy * this.chipPrice;
			totalAmount += totalChips;
			
			runnerTable.setHeight(runRow++, 12);
			runnerTable.add(getText(chipsToBuy + " x " + localize("run_reg.multi_use_chips", "Multi use chips")), 1, runRow);
			runnerTable.add(getText(formatAmount(iwc.getCurrentLocale(), totalChips)), 4, runRow++);
		}
		
		if (transportToBuy > 0) {
			float totalTransport = transportToBuy * runner.getDistance().getPriceForTransport(iwc.getCurrentLocale());
			totalAmount += totalTransport;
			
			runnerTable.setHeight(runRow++, 12);
			runnerTable.add(getText(transportToBuy + " x " + localize("run_reg.transport_to_race_starting_point", "Bus trip to race starting point")), 1, runRow);
			runnerTable.add(getText(formatAmount(iwc.getCurrentLocale(), totalTransport)), 4, runRow++);
		}
		
		runnerTable.setHeight(runRow++, 12);
		runnerTable.add(getHeader(localize("run_reg.total_amount", "Total amount")), 1, runRow);
		runnerTable.add(getHeader(formatAmount(iwc.getCurrentLocale(), totalAmount)), 4, runRow);
		runnerTable.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_RIGHT);

		Table creditCardTable = new Table();
		creditCardTable.setWidth(Table.HUNDRED_PERCENT);
		creditCardTable.setWidth(1, "50%");
		creditCardTable.setWidth(3, "50%");
		creditCardTable.setWidth(2, 12);
		creditCardTable.setColumns(3);
		creditCardTable.setCellspacing(0);
		creditCardTable.setCellpadding(0);
		table.setTopCellBorder(1, row, 1, "#D7D7D7", "solid");
		table.setCellpaddingBottom(1, row++, 6);
		table.add(creditCardTable, 1, row++);
		int creditRow = 1;
		
		creditCardTable.add(getHeader(localize("run_reg.credit_card_information", "Credit card information")), 1, creditRow);
		Collection images = getRunBusiness(iwc).getCreditCardImages();
		if (images != null) {
			Iterator iterator = images.iterator();
			while (iterator.hasNext()) {
				Image image = (Image) iterator.next();
				creditCardTable.add(image, 3, creditRow);
				if (iterator.hasNext()) {
					creditCardTable.add(Text.getNonBrakingSpace(), 3, creditRow);
				}
			}
		}
		creditCardTable.setAlignment(3, creditRow++, Table.HORIZONTAL_ALIGN_RIGHT);
		creditCardTable.setHeight(creditRow++, 12);

		TextInput nameField = (TextInput) getStyledInterface(new TextInput(PARAMETER_NAME_ON_CARD));
		nameField.setAsNotEmpty(localize("run_reg.must_supply_card_holder_name", "You must supply card holder name"));
		nameField.keepStatusOnAction(true);
		
		TextInput ccv = (TextInput) getStyledInterface(new TextInput(PARAMETER_CCV));
		ccv.setLength(3);
		ccv.setMaxlength(3);
		ccv.setMininumLength(3, localize("run_reg.not_valid_ccv", "Not a valid CCV number"));
		ccv.setAsIntegers(localize("run_reg.not_valid_ccv", "Not a valid CCV number"));
		ccv.setAsNotEmpty(localize("run_reg.must_supply_ccv", "You must enter the CCV number"));
		ccv.keepStatusOnAction(true);
		
		IWTimestamp stamp = new IWTimestamp();
		DropdownMenu month = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_EXPIRES_MONTH));
		for (int a = 1; a <= 12; a++) {
			month.addMenuElement(a < 10 ? "0" + a : String.valueOf(a), a < 10 ? "0" + a : String.valueOf(a));
		}
		month.keepStatusOnAction(true);
		DropdownMenu year = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_EXPIRES_YEAR));
		for (int a = stamp.getYear(); a <= stamp.getYear() + 8; a++) {
			year.addMenuElement(String.valueOf(a).substring(2), String.valueOf(a));
		}
		year.keepStatusOnAction(true);
		
		creditCardTable.add(getHeader(localize("run_reg.card_holder", "Card holder")), 1, creditRow);
		creditCardTable.add(getHeader(localize("run_reg.card_number", "Card number")), 3, creditRow++);
		creditCardTable.add(nameField, 1, creditRow);
		for (int a = 1; a <= 4; a++) {
			TextInput cardNumber = (TextInput) getStyledInterface(new TextInput(PARAMETER_CARD_NUMBER + "_" + a));
			if (a < 4) {
				cardNumber.setLength(4);
				cardNumber.setMaxlength(4);
			}
			else {
				cardNumber.setLength(4);
				cardNumber.setMaxlength(7);
			}
			cardNumber.setMininumLength(4, localize("run_reg.not_valid_card_number", "Not a valid card number"));
			cardNumber.setAsIntegers(localize("run_reg.not_valid_card_number", "Not a valid card number"));
			cardNumber.setAsNotEmpty(localize("run_reg.must_supply_card_number", "You must enter the credit card number"));
			cardNumber.keepStatusOnAction(true);

			creditCardTable.add(cardNumber, 3, creditRow);
			if (a != 4) {
				creditCardTable.add(Text.getNonBrakingSpace(), 3, creditRow);
			}
		}
		creditRow++;
		creditCardTable.setHeight(creditRow++, 3);

		creditCardTable.add(getHeader(localize("run_reg.card_expires", "Card expires")), 1, creditRow);
		creditCardTable.add(getHeader(localize("run_reg.ccv_number", "CCV number")), 3, creditRow++);
		creditCardTable.add(month, 1, creditRow);
		creditCardTable.add(getText("/"), 1, creditRow);
		creditCardTable.add(year, 1, creditRow);
		creditCardTable.add(ccv, 3, creditRow++);
		
		TextInput emailField = (TextInput) getStyledInterface(new TextInput(PARAMETER_CARD_HOLDER_EMAIL));
		emailField.setAsEmail(localize("run_reg.email_err_msg", "Not a valid email address"));
		emailField.setWidth(Table.HUNDRED_PERCENT);
		emailField.keepStatusOnAction(true);
		
		creditCardTable.setHeight(creditRow++, 3);
		creditCardTable.mergeCells(3, creditRow, 3, creditRow+1);
		creditCardTable.add(getText(localize("run_reg.ccv_explanation_text","A CCV number is a three digit number located on the back of all major credit cards.")), 3, creditRow);
		creditCardTable.add(getHeader(localize("run_reg.card_holder_email", "Cardholder email")), 1, creditRow++);
		creditCardTable.add(emailField, 1, creditRow++);
		creditCardTable.add(new HiddenInput(PARAMETER_AMOUNT, String.valueOf(totalAmount)));
		creditCardTable.setHeight(creditRow++, 18);
		creditCardTable.mergeCells(1, creditRow, creditCardTable.getColumns(), creditRow);
		creditCardTable.add(getText(localize("run_reg.read_conditions", "Please read before you finish your payment") + ": "), 1, creditRow);
		
		Help help = new Help();
		help.setHelpTextBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER);
		help.setHelpTextKey("terms_and_conditions");
		help.setShowAsText(true);
		help.setLinkText(localize("run_reg.terms_and_conditions", "Terms and conditions"));
		creditCardTable.add(help, 1, creditRow++);

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("run_reg.pay", "Pay")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		next.setDisabled(true);

		CheckBox agree = getCheckBox(PARAMETER_AGREE + "_terms", Boolean.TRUE.toString());
		agree.setToEnableWhenChecked(next);
		agree.setToDisableWhenUnchecked(next);
		
		creditCardTable.setHeight(creditRow++, 12);
		creditCardTable.mergeCells(1, creditRow, creditCardTable.getColumns(), creditRow);
		creditCardTable.add(agree, 1, creditRow);
		creditCardTable.add(Text.getNonBrakingSpace(), 1, creditRow);
		creditCardTable.add(getHeader(localize("run_reg.agree_terms_and_conditions", "I agree to the terms and conditions")), 1, creditRow++);

		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_FIVE));
		table.setHeight(row++, 18);
		table.add(previous, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(next, 1, row);
		form.setToDisableOnSubmit(next, true);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}
	
	private String formatAmount(Locale locale, float amount) {
		return NumberFormat.getInstance(locale).format(amount) + " " + (this.isIcelandic ? "ISK" : "EUR");
	}
	
	private void save(IWContext iwc, boolean doPayment) throws RemoteException {
		try {
			Collection runners = ((Map) iwc.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP)).values();

			String nameOnCard = null;
			String cardNumber = null;
			String hiddenCardNumber = "XXXX-XXXX-XXXX-XXXX";
			String email = ((Runner) runners.iterator().next()).getEmail();
			String expiresMonth = null;
			String expiresYear = null;
			String ccVerifyNumber = null;
			String referenceNumber = null;
			double amount = 0;
			IWTimestamp paymentStamp = new IWTimestamp();

			IWBundle iwb = getBundle(iwc);
			boolean disablePaymentProcess = "true".equalsIgnoreCase(iwb.getProperty("disable_payment_authorization_process","false"));
			if (doPayment && disablePaymentProcess) {
				doPayment = false;
			}

			if (doPayment) {
				nameOnCard = iwc.getParameter(PARAMETER_NAME_ON_CARD);
				cardNumber = "";
				for (int i = 1; i <= 4; i++) {
					cardNumber += iwc.getParameter(PARAMETER_CARD_NUMBER + "_" + i);
				}
				hiddenCardNumber = "XXXX-XXXX-XXXX-" + iwc.getParameter(PARAMETER_CARD_NUMBER + "_" + 4);
				expiresMonth = iwc.getParameter(PARAMETER_EXPIRES_MONTH);
				expiresYear = iwc.getParameter(PARAMETER_EXPIRES_YEAR);
				ccVerifyNumber = iwc.getParameter(PARAMETER_CCV);
				email = iwc.getParameter(PARAMETER_CARD_HOLDER_EMAIL);
				amount = Double.parseDouble(iwc.getParameter(PARAMETER_AMOUNT));
				referenceNumber = iwc.getParameter(PARAMETER_REFERENCE_NUMBER);
			}
			
			String properties = null;
			if (doPayment) {
				properties = getRunBusiness(iwc).authorizePayment(nameOnCard, cardNumber, expiresMonth, expiresYear, ccVerifyNumber, amount, this.isIcelandic ? "ISK" : "EUR", referenceNumber);
			}
			Collection participants = getRunBusiness(iwc).saveParticipants(runners, email, hiddenCardNumber, amount, paymentStamp, iwc.getCurrentLocale());
			if (doPayment) {
				getRunBusiness(iwc).finishPayment(properties);
			}
			iwc.removeSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
			
			showReceipt(iwc, participants, amount, hiddenCardNumber, paymentStamp, doPayment);
		}
		catch (IDOCreateException ice) {
			getParentPage().setAlertOnLoad(localize("run_reg.save_failed", "There was an error when trying to finish registration.  Please contact the marathon.is office."));
			ice.printStackTrace();
			stepSix(iwc);
		}
		catch (CreditCardAuthorizationException ccae) {
			IWResourceBundle creditCardBundle = iwc.getIWMainApplication().getBundle("com.idega.block.creditcard").getResourceBundle(iwc.getCurrentLocale());
			getParentPage().setAlertOnLoad(ccae.getLocalizedMessage(creditCardBundle));
			ccae.printStackTrace();
			stepSix(iwc);
		}
	}
	
	private void showReceipt(IWContext iwc, Collection runners, double amount, String cardNumber, IWTimestamp paymentStamp, boolean doPayment) {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS, runners);
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_AMOUNT, new Double(amount));
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_CARD_NUMBER, cardNumber);
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_PAYMENT_DATE, paymentStamp);

		table.add(getPhasesTable(this.isIcelandic ? 7 : 6, this.isIcelandic ? 7 : 6, "run_reg.receipt", "Receipt"), 1, row++);
		table.setHeight(row++, 18);
		
		table.add(getHeader(localize("run_reg.hello_participant", "Hello participant(s)")), 1, row++);
		table.setHeight(row++, 16);

		table.add(getText(localize("run_reg.payment_received", "We have received payment for the following:")), 1, row++);
		table.setHeight(row++, 8);

		Table runnerTable = new Table(5, runners.size() + 3);
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		runnerTable.add(getHeader(localize("run_reg.runner_name", "Runner name")), 1, 1);
		runnerTable.add(getHeader(localize("run_reg.run", "Run")), 2, 1);
		runnerTable.add(getHeader(localize("run_reg.distance", "Distance")), 3, 1);
		runnerTable.add(getHeader(localize("run_reg.race_number", "Race number")), 4, 1);
		runnerTable.add(getHeader(localize("run_reg.shirt_size", "Shirt size")), 5, 1);
		table.add(runnerTable, 1, row++);
		int runRow = 2;
		int transportToBuy = 0;
		Iterator iter = runners.iterator();
		while (iter.hasNext()) {
			Participant participant = (Participant) iter.next();
			Group run = participant.getRunTypeGroup();
			Group distance = participant.getRunDistanceGroup();
			
			runnerTable.add(getText(participant.getUser().getName()), 1, runRow);
			runnerTable.add(getText(localize(run.getName(), run.getName())), 2, runRow);
			runnerTable.add(getText(localize(distance.getName(), distance.getName())), 3, runRow);
			runnerTable.add(getText(String.valueOf(participant.getParticipantNumber())), 4, runRow);
			runnerTable.add(getText(localize("shirt_size." + participant.getShirtSize(), participant.getShirtSize())), 5, runRow++);
			if (participant.getTransportOrdered().equalsIgnoreCase(Boolean.TRUE.toString())) {
				transportToBuy++;
			}
		}
		if (transportToBuy > 0) {
			runRow++;
			runnerTable.mergeCells(1, runRow, 5, runRow);
			runnerTable.add(getText(transportToBuy + " x " + localize("run_reg.transport_to_race_starting_point", "Bus trip to race starting point")), 1, runRow);
		}

		
		if (doPayment) {
			Table creditCardTable = new Table(2, 3);
			creditCardTable.add(getHeader(localize("run_reg.payment_received_timestamp", "Payment received") + ":"), 1, 1);
			creditCardTable.add(getText(paymentStamp.getLocaleDateAndTime(iwc.getCurrentLocale(), IWTimestamp.SHORT, IWTimestamp.SHORT)), 2, 1);
			creditCardTable.add(getHeader(localize("run_reg.card_number", "Card number") + ":"), 1, 2);
			creditCardTable.add(getText(cardNumber), 2, 2);
			creditCardTable.add(getHeader(localize("run_reg.amount", "Amount") + ":"), 1, 3);
			creditCardTable.add(getText(formatAmount(iwc.getCurrentLocale(), (float) amount)), 2, 3);
			table.setHeight(row++, 16);
			table.add(creditCardTable, 1, row++);
		}
		
		table.setHeight(row++, 16);
		table.add(getHeader(localize("run_reg.delivery_of_race_material_headline", "Race material and T-shirt/sweatshirt")), 1, row++);
		table.add(getText(localize("run_reg.delivery_of_race_material_body", "Participants can collect their race number and the t-shirt/sweatshirt here.")), 1, row++);

		table.setHeight(row++, 16);
		table.add(getHeader(localize("run_reg.receipt_info_headline", "Receipt - Please Print It Out")), 1, row++);
		table.add(getText(localize("run_reg.receipt_info_headline_body", "This document is your receipt, please print this out and bring it with you when you get your race number and T-shirt/sweatshirt.")), 1, row++);

		table.setHeight(row++, 16);
		table.add(getText(localize("run_reg.best_regards", "Best regards,")), 1, row++);
		table.add(getText(localize("run_reg.reykjavik_marathon", "Reykjavik Marathon")), 1, row++);
		table.add(getText("www.marathon.is"), 1, row++);
		
		table.setHeight(row++, 16);
		
		Link print = new Link(localize("print", "Print"));
		print.setPublicWindowToOpen(RegistrationReceivedPrintable.class);
		table.add(print, 1, row);
		
		add(table);
	}
	
	private void cancel(IWContext iwc) {
		iwc.removeSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
	}
	
	private boolean isChild(Runner theRunner) {
		Age age = null;
		if (theRunner.getUser() != null) {
			age = new Age(theRunner.getUser().getDateOfBirth());
		}
		else {
			age = new Age(theRunner.getDateOfBirth());
		}
		return age.getYears() <= 12;
	}
	
	private Runner collectValues(IWContext iwc) throws FinderException, RemoteException {
		String personalID = iwc.getParameter(PARAMETER_PERSONAL_ID);
		if (personalID != null && personalID.length() > 0) {
			Runner runner = getRunner(iwc, personalID);
			if (runner == null) {
				runner = new Runner();
				runner.setPersonalID(personalID);
				if (this.isIcelandic) {
					User user = getUserBusiness(iwc).getUser(personalID);
					runner.setUser(user);
				}
			}
			
			if (!this.isIcelandic) {
				IWTimestamp dateOfBirth = new IWTimestamp(personalID);
				runner.setDateOfBirth(dateOfBirth.getDate());
			}
			if (iwc.isParameterSet(PARAMETER_RUN)) {
				runner.setRun(ConverterUtility.getInstance().convertGroupToRun(new Integer(iwc.getParameter(PARAMETER_RUN))));
			}
			if (iwc.isParameterSet(PARAMETER_DISTANCE)) {
				runner.setDistance(ConverterUtility.getInstance().convertGroupToDistance(new Integer(iwc.getParameter(PARAMETER_DISTANCE))));
			}
			if (iwc.isParameterSet(PARAMETER_NAME)) {
				runner.setName(iwc.getParameter(PARAMETER_NAME));
			}
			if (iwc.isParameterSet(PARAMETER_ADDRESS)) {
				runner.setAddress(iwc.getParameter(PARAMETER_ADDRESS));
			}
			if (iwc.isParameterSet(PARAMETER_POSTAL_CODE)) {
				runner.setPostalCode(iwc.getParameter(PARAMETER_POSTAL_CODE));
			}
			if (iwc.isParameterSet(PARAMETER_CITY)) {
				runner.setCity(iwc.getParameter(PARAMETER_CITY));
			}
			if (iwc.isParameterSet(PARAMETER_COUNTRY)) {
				runner.setCountry(getUserBusiness(iwc).getAddressBusiness().getCountryHome().findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_COUNTRY))));
			}
			if (iwc.isParameterSet(PARAMETER_GENDER)) {
				runner.setGender(getGenderBusiness(iwc).getGender(new Integer(iwc.getParameter(PARAMETER_GENDER))));
			}
			if (iwc.isParameterSet(PARAMETER_NATIONALITY)) {
				runner.setNationality(getUserBusiness(iwc).getAddressBusiness().getCountryHome().findByPrimaryKey(new Integer(iwc.getParameter(PARAMETER_NATIONALITY))));
			}
			if (iwc.isParameterSet(PARAMETER_EMAIL)) {
				runner.setEmail(iwc.getParameter(PARAMETER_EMAIL));
			}
			if (iwc.isParameterSet(PARAMETER_HOME_PHONE)) {
				runner.setHomePhone(iwc.getParameter(PARAMETER_HOME_PHONE));
			}
			if (iwc.isParameterSet(PARAMETER_MOBILE_PHONE)) {
				runner.setMobilePhone(iwc.getParameter(PARAMETER_MOBILE_PHONE));
			}
			if (iwc.isParameterSet(PARAMETER_SHIRT_SIZE)) {
				runner.setShirtSize(iwc.getParameter(PARAMETER_SHIRT_SIZE));
			}
			if (iwc.isParameterSet(PARAMETER_CHIP)) {
				String chip = iwc.getParameter(PARAMETER_CHIP);
				if (chip.equals(IWMarathonConstants.CHIP_RENT)) {
					runner.setRentChip(true);
				}
				else if (chip.equals(IWMarathonConstants.CHIP_BUY)) {
					runner.setBuyChip(true);
				}
				else if (chip.equals(IWMarathonConstants.CHIP_OWN)) {
					runner.setOwnChip(true);
				}
			}
			if (iwc.isParameterSet(PARAMETER_CHIP_NUMBER)) {
				runner.setChipNumber(iwc.getParameter(PARAMETER_CHIP_NUMBER));
			}
			if (iwc.isParameterSet(PARAMETER_TRANSPORT)) {
				String transport = iwc.getParameter(PARAMETER_TRANSPORT);
				if (transport.equals(Boolean.TRUE.toString())) {
					runner.setTransportOrdered(true);
				}
				else if (transport.equals(Boolean.FALSE.toString())){
					runner.setNoTransportOrdered(true);
				}
			}
			if (iwc.isParameterSet(PARAMETER_AGREE)) {
				runner.setAgree(true);
			}

			addRunner(iwc, personalID, runner);
			return runner;
		}
		return new Runner();
	}
	
	private int parseAction(IWContext iwc) throws RemoteException {
		int action = this.isIcelandic ? ACTION_STEP_ONE : ACTION_STEP_TWO;
		
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}

		try {
			this.runner = collectValues(iwc);
		}
		catch (FinderException fe) {
			getParentPage().setAlertOnLoad(localize("run_reg.user_not_found_for_personal_id", "No user found with personal ID."));
			action = ACTION_STEP_ONE;
		}

		if (action == ACTION_STEP_THREE) {
			if (this.runner != null && !this.runner.getDistance().isUseChip()) {
				int fromAction = Integer.parseInt(iwc.getParameter(PARAMETER_FROM_ACTION));
				if (fromAction == ACTION_STEP_FOUR || fromAction == ACTION_STEP_THREE_TRANSPORT) {
					action = ACTION_STEP_TWO;
				}
				else if (fromAction == ACTION_STEP_TWO) {
					if (this.runner != null && this.runner.getDistance().isTransportOffered() && !disableTransportStep) {
						action= ACTION_STEP_THREE_TRANSPORT;
					}
					else {
						action= ACTION_STEP_FOUR;
					}
				}
			}
		}
		if (action == ACTION_STEP_THREE_TRANSPORT) {
			if (this.runner != null && !(this.runner.getDistance().isTransportOffered() && !disableTransportStep)) {
				int fromAction = Integer.parseInt(iwc.getParameter(PARAMETER_FROM_ACTION));
				if (fromAction == ACTION_STEP_FOUR) {
					if (this.runner != null && this.runner.getDistance().isUseChip()) {
						action = ACTION_STEP_THREE;
					}
					else {
						action = ACTION_STEP_TWO;
					}
				}
				else if (fromAction == ACTION_STEP_TWO || fromAction == ACTION_STEP_THREE) {
					action= ACTION_STEP_FOUR;
				}
			}
		}
		if (action == ACTION_STEP_FOUR) {
			if (this.runner != null && this.runner.isOwnChip() && (this.runner.getChipNumber() == null || !checkChipNumber(this.runner.getChipNumber()).equals(""))) {
				getParentPage().setAlertOnLoad(localize("run_reg.must_fill_in_chip_number", "You have to fill in a valid chip number (seven characters)."));
				action = ACTION_STEP_THREE;
			}
		}

		return action;
	}
	
	private Runner getRunner(IWContext iwc, String key) {
		Map runnerMap = (Map) iwc.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		if (runnerMap != null) {
			return (Runner) runnerMap.get(key);
		}
		return null;
	}
	
	private void addRunner(IWContext iwc, String key, Runner runner) {
		Map runnerMap = (Map) iwc.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		if (runnerMap == null) {
			runnerMap = new HashMap();
		}
		runnerMap.put(key, runner);
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP, runnerMap);
	}
	
	private void removeRunner(IWContext iwc, String key) {
		Map runnerMap = (Map) iwc.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		if (runnerMap == null) {
			runnerMap = new HashMap();
		}
		runnerMap.remove(key);
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP, runnerMap);
	}
	
	private static String checkChipNumber(String chipNumber) {
		if (chipNumber == null) {
			return "null value";
		}
		chipNumber = chipNumber.trim().toUpperCase();
		if (chipNumber.length() != 7) {
			return "invalid length (" + chipNumber.length() + ")";
		}
		if ("ABC".indexOf(chipNumber.substring(0, 1))!= -1) {
			return checkOldBlock(chipNumber);
		} else {
			return checkBigBlock(chipNumber);
		}
	}

	private static String checkOldBlock(String chipNumber) {
		if (chipNumber == null) {
			return "null value";
		}
		chipNumber = chipNumber.trim().toUpperCase();
		if (chipNumber.length() != 7) {
			return "invalid length";
		}
		String[] pos = new String[8];
		pos[1] = "ABC";
		pos[2] = "ABCDEFGH";
		pos[3] = "0123456789";
		pos[4] = "0123456789";
		pos[5] = "0123456789";
		pos[6] = "0123456789";
		pos[7] = "0123456789";
		for (int i = 1; i < 8; i++) {
			if (pos[i].indexOf(chipNumber.substring((i - 1), i)) == -1) {
				return "invalid character on position: " + i;
			}
		}
		return "";
	}

	private static String checkBigBlock(String chipNumber) {
		if (chipNumber == null) {
			return "null value";
		}
		chipNumber = chipNumber.trim().toUpperCase();
		if (chipNumber.length() != 7) {
			return "invalid length";
		}
		String[] pos = new String[7];
		pos[1] = "DEFGHKMNPRSTVWYZ";
		pos[2] = "ABCDEFGHKMNPRSTVWXYZ";
		pos[3] = "0123456789";
		pos[4] = "0123456789ABCDEFGHKMNPRSTVWXYZ";
		pos[5] = "0123456789ABCDEFGHKMNPRSTVWXYZ";
		pos[6] = "0123456789ABCDEFGHKMNPRSTVWXYZ";
		String poscnt = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String poschk = "Z97BN4XSVE56AWYM1TPK8H2GDCF3R";
		int checksum = 0;
		for (int i = 1; i < 7; i++) {
			if (pos[i].indexOf(chipNumber.substring((i - 1), i)) == -1) {
				return "invalid character on position: " + i;
			}
			checksum = checksum + poscnt.indexOf(chipNumber.substring(i - 1, i));
		}
		checksum = checksum % 29;
		if (checksum == poschk.indexOf(chipNumber.substring(6, 7))) {
			return "";
		} else {
			return "invalid chipcode.";
		}
	}

	public void setDisableTransportStep(boolean disableTransportStep) {
		this.disableTransportStep = disableTransportStep;
	}
}