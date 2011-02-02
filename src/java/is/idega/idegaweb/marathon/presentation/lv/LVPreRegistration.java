package is.idega.idegaweb.marathon.presentation.lv;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.business.Runner;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.presentation.RunBlock;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.FinderException;
import javax.faces.component.UIComponent;

import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOCreateException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.business.NoPhoneFoundException;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;

/**
 * Registration class for the Laugavegur Ultra Marathon.
 * 
 * @author palli
 * 
 */
public class LVPreRegistration extends RunBlock {

	public static final String SESSION_ATTRIBUTE_RUNNER_MAP = "sa_runner_map";
	public static final String SESSION_ATTRIBUTE_PARTICIPANTS = "sa_participants";

	private static final String PARAMETER_PERSONAL_ID = "prm_personal_id";
	private static final String PARAMETER_DATE_OF_BIRTH = "prm_date_of_birth";
	private static final String PARAMETER_NO_PERSONAL_ID = "prm_no_personal_id";
	private static final String PARAMETER_NAME = "prm_name";
	private static final String PARAMETER_ADDRESS = "prm_address";
	private static final String PARAMETER_POSTAL_CODE = "prm_postal_code";
	private static final String PARAMETER_CITY = "prm_city";
	private static final String PARAMETER_COUNTRY = "prm_country";
	private static final String PARAMETER_GENDER = "prm_gender";
	private static final String PARAMETER_NATIONALITY = "prm_nationality";
	private static final String PARAMETER_EMAIL = "prm_email";
	private static final String PARAMETER_EMAIL2 = "prm_email2";
	private static final String PARAMETER_HOME_PHONE = "prm_home_phone";
	private static final String PARAMETER_MOBILE_PHONE = "prm_mobile_phone";

	private static final String PARAMETER_QUESTION1_HOUR = "prm_q1_hour";
	private static final String PARAMETER_QUESTION1_MINUTE = "prm_q1_minute";
	private static final String PARAMETER_QUESTION1_YEAR = "prm_q1_year";
	private static final String PARAMETER_QUESTION1_NEVER = "prm_q1_never";
	private static final String PARAMETER_QUESTION3_HOUR = "prm_q3_hour";
	private static final String PARAMETER_QUESTION3_MINUTE = "prm_q3_minute";
	private static final String PARAMETER_QUESTION3_YEAR = "prm_q3_year";
	private static final String PARAMETER_QUESTION3_NEVER = "prm_q3_never";

	private static final int ACTION_STEP_PERSONLOOKUP = 10;
	private static final int ACTION_STEP_PERSONALDETAILS = 20;
	private static final int ACTION_STEP_QUESTIONS = 40;
	private static final int ACTION_STEP_OVERVIEW = 60;
	private static final int ACTION_STEP_REGISTER = 70;
	private static final int ACTION_CANCEL = 80;

	private static final double MILLISECONDS_IN_YEAR = 31557600000d;

	// private static final String ULTRA_MARATHON_GROUP_ID = "5";
	private static final String ULTRA_MARATHON_PREREGISTRATION_GROUP_ID = "425734";

	private boolean isIcelandicPersonalID = false;
	private Runner setRunner;
	boolean showQuestionsError = false;

	public void main(IWContext iwc) throws Exception {
		if (!iwc.isInEditMode()) {
			this.isIcelandicPersonalID = iwc.getCurrentLocale().equals(
					LocaleUtil.getIcelandicLocale());

			if (!this.isIcelandicPersonalID) {
				if (getRunner() != null) {
					if (getRunner().getPersonalID() != null) {
						this.isIcelandicPersonalID = true;
					}
				}
			}
			loadCurrentStep(iwc, parseAction(iwc));
		}
	}

	private void loadCurrentStep(IWContext iwc, int action)
			throws RemoteException {

		switch (action) {

		case ACTION_STEP_PERSONLOOKUP:
			stepPersonalLookup(iwc);
			break;
		case ACTION_STEP_PERSONALDETAILS:
			stepPersonalDetails(iwc);
			break;
		case ACTION_STEP_QUESTIONS:
			stepQuestions(iwc);
			break;
		case ACTION_STEP_OVERVIEW:
			stepOverview(iwc);
			break;
		case ACTION_STEP_REGISTER:
			stepRegister(iwc);
			break;
		case ACTION_CANCEL:
			cancel(iwc);
			break;
		}
	}

	private void stepPersonalLookup(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_DATE_OF_BIRTH);
		form.maintainParameter(PARAMETER_QUESTION1_NEVER);
		form.maintainParameter(PARAMETER_QUESTION3_NEVER);

		form.addParameter(PARAMETER_ACTION, ACTION_NEXT);
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_PERSONLOOKUP);

		form.add(getStepsHeader(iwc, ACTION_STEP_PERSONLOOKUP));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.setHeight(row++, 12);

		table.add(
				getInformationTable(localize("lv_reg.information_text_step_1",
						"Information text 1...")), 1, row++);
		table.setHeight(row++, 6);

		// table.setCellpadding(1, row, 24);
		table.add(
				getHeader(localize("lv_reg.personal_id", "Personal ID") + ":"),
				1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);

		TextInput input = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_PERSONAL_ID));
		input.setAsIcelandicSSNumber(localize("lv_reg.not_valid_personal_id",
				"The personal ID you have entered is not valid"));
		input.setLength(10);
		input.setMaxlength(10);
		input.setInFocusOnPageLoad(true);
		if (this.isIcelandicPersonalID) {
			input.setAsNotEmpty(localize("lv_reg.not_valid_personal_id",
					"The personal ID you have entered is not valid"));
		}
		table.add(input, 1, row++);

		if (!this.isIcelandicPersonalID) {
			// table.setCellpadding(1, row, 24);
			Layer noIcelandicSSNLayer = new Layer(Layer.DIV);

			CheckBox noIcelandicSSNCheck = getCheckBox(
					PARAMETER_NO_PERSONAL_ID, Boolean.TRUE.toString());
			noIcelandicSSNCheck.setToEnableWhenUnchecked(input);
			noIcelandicSSNCheck.setToDisableWhenChecked(input);

			Label noIcelandicSSNLabel = new Label(localize(
					"lv_reg.no_icelandic_ssn",
					"I do not have a Icelandic personal ID"),
					noIcelandicSSNCheck);
			noIcelandicSSNLayer.add(noIcelandicSSNCheck);
			noIcelandicSSNLayer.add(noIcelandicSSNLabel);

			table.add(noIcelandicSSNLayer, 1, row++);
		}

		UIComponent buttonsContainer = getButtonsFooter(iwc, false, true);
		form.add(buttonsContainer);

		add(form);
	}

	private void stepPersonalDetails(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_DATE_OF_BIRTH);
		form.maintainParameter(PARAMETER_QUESTION1_NEVER);
		form.maintainParameter(PARAMETER_QUESTION3_NEVER);

		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_PERSONALDETAILS);

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);

		form.add(getStepsHeader(iwc, ACTION_STEP_PERSONALDETAILS));

		form.add(table);
		int row = 1;

		table.setHeight(row++, 12);

		table.add(
				getInformationTable(localize("lv_reg.information_text_step_2",
						"Information text 2...")), 1, row++);
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

		Text redStar = getHeader(" *");
		redStar.setFontColor("#ff0000");

		int iRow = 1;
		Runner runner = getRunner();

		TextInput nameField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_NAME));
		if (getRunner().getName() != null) {
			nameField.setContent(getRunner().getName());
		}
		if (this.isIcelandicPersonalID) {
			nameField.setDisabled(true);
			if (getRunner().getUser() != null) {
				nameField.setContent(getRunner().getUser().getName());
			}
		} else {
			nameField.setAsAlphabeticText(localize("lv_reg.name_err_msg",
					"Your name may only contain alphabetic characters"));
			nameField.setAsNotEmpty(localize("lv_reg.name_not_empty",
					"Name field can not be empty"));
		}

		DropdownMenu genderField = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_GENDER));
		Collection genders = getGenderBusiness(iwc).getAllGenders();
		genderField.addMenuElement("-1",
				localize("lv_reg.select_gender", "Select gender..."));
		if (genders != null) {
			Iterator iter = genders.iterator();
			while (iter.hasNext()) {
				Gender gender = (Gender) iter.next();
				genderField
						.addMenuElement(
								gender.getPrimaryKey().toString(),
								localize("gender." + gender.getName(),
										gender.getName()));
			}
		}
		if (getRunner().getGender() != null) {
			genderField.setSelectedElement(getRunner().getGender()
					.getPrimaryKey().toString());
		}
		if (this.isIcelandicPersonalID) {
			genderField.setDisabled(true);
			if (getRunner().getUser() != null) {
				genderField.setSelectedElement(getRunner().getUser()
						.getGenderID());
			}
		} else {
			genderField.setAsNotEmpty(localize("lv_reg.gender_not_empty",
					"Gender can not be empty"));
		}

		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_NAME, "Name")), 1,
				iRow);
		if (!this.isIcelandicPersonalID) {
			choiceTable.add(redStar, 1, iRow);
		}
		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_GENDER, "Gender")),
				3, iRow);
		if (!this.isIcelandicPersonalID) {
			choiceTable.add(redStar, 3, iRow++);
		} else {
			iRow++;
		}
		choiceTable.add(nameField, 1, iRow);
		choiceTable.add(genderField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		TextInput ssnISField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_PERSONAL_ID));
		ssnISField.setLength(10);
		if (this.isIcelandicPersonalID) {
			ssnISField.setDisabled(true);
			if (getRunner().getUser() != null) {
				ssnISField.setContent(getRunner().getUser().getPersonalID());
			}
		}

		DateInput ssnField = (DateInput) getStyledInterface(new DateInput(
				PARAMETER_DATE_OF_BIRTH));
		ssnField.setAsNotEmpty("Date of birth can not be empty");

		IWTimestamp maximumAgeStamp = new IWTimestamp();
		IWTimestamp earliestYearStamp = new IWTimestamp();
		IWTimestamp minimumAgeStamp = new IWTimestamp();
		IWTimestamp newestYearStamp = new IWTimestamp();
		int maximumAgeForRun = -1;
		if (getRunner().getYear() != null) {
			maximumAgeForRun = getRunner().getYear().getMaximumAgeForRun();
		}
		if (maximumAgeForRun == -1) {
			maximumAgeForRun = 100;
		}
		earliestYearStamp.addYears(-maximumAgeForRun);
		maximumAgeStamp.addYears(-maximumAgeForRun);
		int minimumAgeForRun = -1;
		if (getRunner().getYear() != null) {
			minimumAgeForRun = getRunner().getYear().getMinimumAgeForRun();
		}
		if (minimumAgeForRun == -1) {
			minimumAgeForRun = 3;
		}
		newestYearStamp.addYears(-minimumAgeForRun);
		minimumAgeStamp.addYears(-minimumAgeForRun);

		ssnField.setYearRange(newestYearStamp.getYear(),
				earliestYearStamp.getYear());

		if (getRunner().getDateOfBirth() != null) {
			ssnField.setDate(getRunner().getDateOfBirth());
		}

		TextInput emailField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_EMAIL));
		emailField.setAsEmail(localize("lv_reg.email_err_msg",
				"Not a valid email address"));
		emailField.setAsNotEmpty(localize("lv_reg.continue_without_email",
				"You can not continue without entering an e-mail"));
		if (getRunner().getEmail() != null) {
			emailField.setContent(getRunner().getEmail());
		} else if (getRunner().getUser() != null) {
			try {
				Email mail = getUserBusiness(iwc).getUsersMainEmail(
						getRunner().getUser());
				emailField.setContent(mail.getEmailAddress());
			} catch (NoEmailFoundException nefe) {
				// No email registered...
			}
		}

		Collection countries = getRunBusiness(iwc).getCountries(null);
		DropdownMenu nationalityField = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_NATIONALITY));
		DropdownMenu countryField = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_COUNTRY));
		nationalityField.addMenuElement("-1",
				localize("lv_reg.select_nationality", "Select nationality..."));
		countryField.addMenuElement("-1",
				localize("lv_reg.select_country", "Select country..."));
		SelectorUtility util = new SelectorUtility();
		if (countries != null && !countries.isEmpty()) {
			nationalityField = (DropdownMenu) util.getSelectorFromIDOEntities(
					nationalityField, countries, "getName");
			countryField = (DropdownMenu) util.getSelectorFromIDOEntities(
					countryField, countries, "getName");
		}
		if (this.isIcelandicPersonalID) {
			countryField.setDisabled(true);
			Country icelandicNationality = null;
			try {
				icelandicNationality = getAddressBusiness(iwc).getCountryHome()
						.findByIsoAbbreviation("IS");
				nationalityField.setSelectedElement(icelandicNationality
						.getPrimaryKey().toString());
			} catch (FinderException e) {
				// icelandicNationality not found
			}
			if (getRunner().getUser() != null) {
				Address address = getUserBusiness(iwc).getUsersMainAddress(
						getRunner().getUser());
				if (address != null && address.getCountry() != null) {
					countryField.setSelectedElement(address.getCountry()
							.getPrimaryKey().toString());
				}
			}
		}
		nationalityField.setAsNotEmpty(localize(
				"lv_reg.must_select_nationality",
				"You must select your nationality"));
		if (!this.isIcelandicPersonalID) {
			countryField.setAsNotEmpty(localize("lv_reg.must_select_country",
					"You must select your country"));
		}
		if (getRunner().getCountry() != null) {
			countryField.setSelectedElement(getRunner().getCountry()
					.getPrimaryKey().toString());
		}
		if (getRunner().getNationality() != null) {
			nationalityField.setSelectedElement(getRunner().getNationality()
					.getPrimaryKey().toString());
		}

		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_SSN, "SSN")),
				1, iRow);
		if (!this.isIcelandicPersonalID) {
			choiceTable.add(redStar, 1, iRow);
		}
		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_EMAIL, "Email")), 3,
				iRow);
		choiceTable.add(redStar, 3, iRow++);
		if (this.isIcelandicPersonalID) {
			choiceTable.add(ssnISField, 1, iRow);
		} else {
			choiceTable.add(ssnField, 1, iRow);
		}
		choiceTable.add(emailField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		TextInput addressField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_ADDRESS));
		if (!this.isIcelandicPersonalID) {
			addressField.setAsNotEmpty(localize("lv_reg.must_provide_address",
					"You must enter your address."));
		}
		if (getRunner().getAddress() != null) {
			addressField.setContent(getRunner().getAddress());
		}
		if (this.isIcelandicPersonalID) {
			addressField.setDisabled(true);
			if (getRunner().getUser() != null) {
				Address address = getUserBusiness(iwc).getUsersMainAddress(
						getRunner().getUser());
				if (address != null) {
					addressField.setContent(address.getStreetAddress());
				}
			}
		}

		TextInput emailField2 = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_EMAIL2));
		emailField2.setAsEmail(localize("lv_reg.email_err_msg",
				"Not a valid email address"));
		emailField2.setAsNotEmpty(localize("lv_reg.continue_without_email2",
				"You can not continue without repeating the e-mail"));
		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_ADDRESS, "Address")),
				1, iRow);
		if (!this.isIcelandicPersonalID) {
			choiceTable.add(redStar, 1, iRow);
		}
		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_EMAIL2,
						"Email repeated")), 3, iRow);
		choiceTable.add(redStar, 3, iRow++);
		choiceTable.add(addressField, 1, iRow);
		choiceTable.add(emailField2, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		TextInput cityField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_CITY));
		if (!this.isIcelandicPersonalID) {
			cityField.setAsNotEmpty(localize("lv_reg.must_provide_city",
					"You must enter your city of living."));
		}
		if (getRunner().getCity() != null) {
			cityField.setContent(getRunner().getCity());
		}
		if (this.isIcelandicPersonalID) {
			cityField.setDisabled(true);
			if (getRunner().getUser() != null) {
				Address address = getUserBusiness(iwc).getUsersMainAddress(
						getRunner().getUser());
				if (address != null) {
					cityField.setContent(address.getCity());
				}
			}
		}

		TextInput telField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_HOME_PHONE));
		// telField.setWidth(Table.HUNDRED_PERCENT);
		if (getRunner().getHomePhone() != null) {
			telField.setContent(getRunner().getHomePhone());
		} else if (getRunner().getUser() != null) {
			try {
				Phone phone = getUserBusiness(iwc).getUsersHomePhone(
						getRunner().getUser());
				telField.setContent(phone.getNumber());
			} catch (NoPhoneFoundException nefe) {
				// No phone registered...
			}
		}

		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_CITY, "City")), 1,
				iRow);
		if (!this.isIcelandicPersonalID) {
			choiceTable.add(redStar, 1, iRow);
		}
		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_TEL, "Telephone")),
				3, iRow++);
		choiceTable.add(cityField, 1, iRow);
		choiceTable.add(telField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		TextInput postalField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_POSTAL_CODE));
		if (!this.isIcelandicPersonalID) {
			postalField.setAsNotEmpty(localize("lv_reg.must_provide_postal",
					"You must enter your postal address."));
		}
		postalField.setMaxlength(10);
		postalField.setLength(10);
		if (getRunner().getPostalCode() != null) {
			postalField.setContent(getRunner().getPostalCode());
		}
		if (this.isIcelandicPersonalID) {
			postalField.setDisabled(true);
			if (getRunner().getUser() != null) {
				Address address = getUserBusiness(iwc).getUsersMainAddress(
						getRunner().getUser());
				if (address != null) {
					PostalCode postal = address.getPostalCode();
					if (postal != null) {
						postalField.setContent(postal.getPostalCode());
					}
				}
			}
		}

		TextInput mobileField = (TextInput) getStyleObject(new TextInput(
				PARAMETER_MOBILE_PHONE), STYLENAME_INTERFACE);
		// mobileField.setWidth(Table.HUNDRED_PERCENT);
		if (getRunner().getMobilePhone() != null) {
			mobileField.setContent(getRunner().getMobilePhone());
		} else if (getRunner().getUser() != null) {
			try {
				Phone phone = getUserBusiness(iwc).getUsersMobilePhone(
						getRunner().getUser());
				mobileField.setContent(phone.getNumber());
			} catch (NoPhoneFoundException nefe) {
				// No phone registered...
			}
		}

		choiceTable
				.add(getHeader(localize(IWMarathonConstants.RR_POSTAL,
						"Postal Code")), 1, iRow);
		if (!this.isIcelandicPersonalID) {
			choiceTable.add(redStar, 1, iRow);
		}
		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_MOBILE,
						"Mobile Phone")), 3, iRow++);
		choiceTable.add(postalField, 1, iRow);
		choiceTable.add(mobileField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_COUNTRY, "Country")),
				1, iRow);
		if (!this.isIcelandicPersonalID) {
			choiceTable.add(redStar, 1, iRow);
		}
		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_NATIONALITY,
						"Nationality")), 3, iRow);
		choiceTable.add(redStar, 3, iRow++);

		choiceTable.add(countryField, 1, iRow);
		choiceTable.add(nationalityField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		UIComponent buttonsContainer = getButtonsFooter(iwc, false, true);
		form.add(buttonsContainer);

		add(form);
	}

	private void stepQuestions(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_DATE_OF_BIRTH);
		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_QUESTIONS);

		form.add(getStepsHeader(iwc, ACTION_STEP_QUESTIONS));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.setHeight(row++, 12);

		table.add(
				getInformationTable(localize(
						"lv_reg.information_text_step_questions",
						"Please answer the following questions.")), 1, row++);
		table.setHeight(row++, 18);

		if (showQuestionsError) {
			Text errorText = getHeader(localize("question_error",
					"You have to answer all the question to continue."));
			errorText.setFontColor("#ff0000");
			table.add(errorText, 1, row++);
			table.setHeight(row++, 18);
			showQuestionsError = false;
		}

		Text redStar = getHeader(" *");
		redStar.setFontColor("#ff0000");

		IWTimestamp currentYear = new IWTimestamp();
		
		table.add(
				getText((localize(
						"lv_reg.question1",
						"What is your personal best, if you have run the Laugavegur Ultra Marathon before? (please choose time and year)"))),
				1, row);
		table.add(redStar, 1, row++);
		table.add(
				createHourDropDown(PARAMETER_QUESTION1_HOUR, getRunner()
						.getQuestion1Hour()), 1, row);
		table.add(
				createMinuteDropDown(PARAMETER_QUESTION1_MINUTE, getRunner()
						.getQuestion1Minute()), 1, row);
		table.add(
				createYearDropDown(PARAMETER_QUESTION1_YEAR, 1996, currentYear.getYear(),
						getRunner().getQuestion1Year()), 1, row++);
		CheckBox neverBefore = getCheckBox(PARAMETER_QUESTION1_NEVER,
				Boolean.TRUE.toString());
		neverBefore.setChecked(getRunner().getQuestion1NeverRan());
		table.add(neverBefore, 1, row);
		table.add(
				getText((localize("lv_reg.question1_never_before",
						"I haven't done the Laugavegur Ultra Marathon before"))),
				1, row++);

		table.setHeight(row++, 18);
		table.add(
				getText((localize("lv_reg.question3",
						"What is your personal best in a Marathon race? (please choose time and year)"))),
				1, row);
		table.add(redStar, 1, row++);
		table.add(
				createHourDropDown(PARAMETER_QUESTION3_HOUR, getRunner()
						.getQuestion3Hour()), 1, row);
		table.add(
				createMinuteDropDown(PARAMETER_QUESTION3_MINUTE, getRunner()
						.getQuestion3Minute()), 1, row);
		table.add(
				createYearDropDown(PARAMETER_QUESTION3_YEAR, 1990, currentYear.getYear(),
						getRunner().getQuestion3Year()), 1, row++);
		CheckBox neverBefore2 = getCheckBox(PARAMETER_QUESTION3_NEVER,
				Boolean.TRUE.toString());
		neverBefore2.setChecked(getRunner().getQuestion3NeverRan());
		table.add(neverBefore2, 1, row);
		table.add(
				getText((localize("lv_reg.question3_never_before",
						"I haven't done a marathon before"))), 1, row++);

		form.add(getButtonsFooter(iwc));

		add(form);
	}

	private void stepOverview(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_DATE_OF_BIRTH);
		form.maintainParameter(PARAMETER_QUESTION1_NEVER);
		form.maintainParameter(PARAMETER_QUESTION3_NEVER);

		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_OVERVIEW);

		form.add(getStepsHeader(iwc, ACTION_STEP_OVERVIEW));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.setHeight(row++, 12);

		table.add(
				getInformationTable(localize("lv_reg.information_text_step_5",
						"Information text 5...")), 1, row++);
		table.setHeight(row++, 18);

		Map runners = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		Table runnerTable = new Table();
		// runnerTable.setColumns(columns)
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		runnerTable.add(
				getHeader(localize("lv_reg.runner_name", "Runner name")), 1, 1);
		runnerTable.add(getHeader(localize("lv_reg.run", "Run")), 2, 1);
		runnerTable.add(getHeader(localize("lv_reg.distance", "Distance")), 3,
				1);
		table.add(runnerTable, 1, row++);
		int runRow = 2;

		boolean addedLegHeader = false;

		Iterator iter = runners.values().iterator();
		while (iter.hasNext()) {
			Runner runner = (Runner) iter.next();
			if (runner.getRun() != null) {
				if (runner.getUser() != null) {
					runnerTable.add(getText(runner.getUser().getName()), 1,
							runRow);
				} else {
					runnerTable.add(getText(runner.getName()), 1, runRow);
				}

				runnerTable.add(
						getText(localize(runner.getRun().getName(), runner
								.getRun().getName())
								+ " "
								+ runner.getDistance().getYear().getName()), 2,
						runRow);
				runnerTable.add(
						getText(localize(runner.getDistance().getName(), runner
								.getDistance().getName())), 3, runRow++);
			} else {
				removeRunner(iwc, runner.getPersonalID());
			}
		}

		UIComponent buttonsContainer = getButtonsFooter(iwc, false, false);

		SubmitButton previous = getPreviousButton();
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize(
				"lv_reg.finish_registration", "Register")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_NEXT));

		buttonsContainer.getChildren().add(previous);
		buttonsContainer.getChildren().add(next);

		form.add(buttonsContainer);

		add(form);
	}

	private void stepRegister(IWContext iwc) throws RemoteException {
		try {
			if (getRunner().getRun() == null) {
				getParentPage()
						.setAlertOnLoad(
								localize(
										"lv_reg.session_has_expired_payment",
										"Session has expired and information from earlier steps is lost. \\nYou will have to enter the information again."));
				stepPersonalDetails(iwc);
				return;
			}

			String email = getRunner().getEmail();

			IWTimestamp paymentStamp = new IWTimestamp();

			Participant participant = getRunBusiness(iwc)
					.storeParticipantRegistration(getRunner(),
							iwc.getCurrentLocale(), "lv_reg.");
			iwc.removeSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);

			showRegistration(iwc, participant);
		} catch (IDOCreateException ice) {
			getParentPage()
					.setAlertOnLoad(
							localize("lv_reg.save_failed",
									"There was an error when trying to finish registration."));
			ice.printStackTrace();
			loadPreviousStep(iwc);
		}
	}

	private void showRegistration(IWContext iwc, Participant participant) {
		add(getStepsHeader(iwc, ACTION_STEP_REGISTER));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS, participant);

		Group run = null;
		Run selectedRun = null;
		run = participant.getRunTypeGroup();
		try {
			selectedRun = ConverterUtility.getInstance().convertGroupToRun(run);
		} catch (FinderException e) {
		}

		table.setHeight(row++, 18);

		String greeting = localize("lv_reg.hello_participant",
				"Dear participant");
		if (selectedRun != null) {
			if (iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale())) {
				greeting = selectedRun.getRunRegistrationReceiptGreeting();
			} else {
				greeting = selectedRun
						.getRunRegistrationReceiptGreetingEnglish();
			}
		}

		table.add(getHeader(greeting), 1, row++);
		table.setHeight(row++, 16);

		table.add(
				getText(localize("lv_reg.payment_received",
						"You have registered for the following:")), 1, row++);
		table.setHeight(row++, 8);

		Table runnerTable = new Table(5, 5);
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		int col = 1;
		runnerTable.add(
				getHeader(localize("lv_reg.runner_name", "Runner name")),
				col++, 1);
		runnerTable.add(getHeader(localize("lv_reg.run", "Run")), col++, 1);
		runnerTable.add(getHeader(localize("lv_reg.distance", "Distance")),
				col++, 1);

		table.add(runnerTable, 1, row++);
		int runRow = 2;
		run = participant.getRunTypeGroup();
		Group distance = participant.getRunDistanceGroup();
		col = 1;
		runnerTable
				.add(getText(participant.getUser().getName()), col++, runRow);
		runnerTable.add(getText(localize(run.getName(), run.getName()) + " "
				+ participant.getRunYearGroup().getName()), col++, runRow);
		runnerTable.add(
				getText(localize(distance.getName(), distance.getName())),
				col++, runRow);

		table.setHeight(row++, 16);

		if (selectedRun != null) {
			String informationText = "";

			if (iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale())) {
				informationText = selectedRun.getRunRegistrationReceiptInfo();
			} else {
				informationText = selectedRun
						.getRunRegistrationReceiptInfoEnglish();
			}
			table.add(getText(informationText), 1, row++);
		}

		table.setHeight(row++, 16);
		table.add(getText(localize("lv_reg.best_regards", "Best regards,")), 1,
				row++);

		if (selectedRun != null) {
			table.add(getText(selectedRun.getRunHomePage()), 1, row++);
		}

		add(table);
	}

	private void cancel(IWContext iwc) {
		iwc.removeSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
	}

	protected Runner getRunner() {
		if (this.setRunner == null) {
			IWContext iwc = IWContext.getInstance();
			try {
				this.setRunner = initializeRunner(iwc);
			} catch (FinderException e) {
				throw new RuntimeException(e);
			}
		}
		return this.setRunner;
	}

	private Runner initializeRunner(IWContext iwc) throws FinderException {
		if (!iwc.isParameterSet(PARAMETER_PERSONAL_ID)
				&& !iwc.isParameterSet(PARAMETER_DATE_OF_BIRTH)) {
			Runner runner = new Runner();
			runner.setRunId(LVPreRegistration.ULTRA_MARATHON_PREREGISTRATION_GROUP_ID);
			Year year = runner.getYear();
			String runnerYearString = year.getYearString();

			try {
				Collection distancesGroups = getRunBusiness(iwc)
						.getDistancesMap(runner.getRun(), runnerYearString);
				if (distancesGroups != null) {
					Iterator it = distancesGroups.iterator();
					if (it.hasNext()) {
						runner.setDistance(ConverterUtility.getInstance()
								.convertGroupToDistance((Group) it.next()));
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			return runner;
		}

		Runner runner = null;
		String personalID = iwc.getParameter(PARAMETER_PERSONAL_ID);
		String dateOfBirth = iwc.getParameter(PARAMETER_DATE_OF_BIRTH);
		if (personalID != null && personalID.length() > 0) {
			runner = getRunner(iwc, personalID);
		} else if (dateOfBirth != null && dateOfBirth.length() > 0) {
			runner = getRunner(iwc, dateOfBirth);
		}

		if (runner == null) {
			runner = new Runner();
			User user = null;
			if (personalID != null && personalID.length() > 0) {
				runner.setPersonalID(personalID);
				try {
					user = getUserBusiness(iwc).getUser(personalID);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else if (dateOfBirth != null && dateOfBirth.length() > 0) {
				IWTimestamp dob = new IWTimestamp(dateOfBirth);
				runner.setDateOfBirth(dob.getDate());
				if (iwc.isParameterSet(PARAMETER_NAME)) {
					runner.setName(iwc.getParameter(PARAMETER_NAME));
					/*try {
						user = getUserBusiness(iwc).getUserHome()
								.findByDateOfBirthAndName(dob.getSQLDate(),
										runner.getName());
					} catch (Exception fe) {
						System.out
								.println("User not found by name and date_of_birth");
					}*/
				}
			}

			runner.setUser(user);
		}

		if (runner.getRun() == null) {
			runner.setRunId(LVPreRegistration.ULTRA_MARATHON_PREREGISTRATION_GROUP_ID);
			Year year = runner.getYear();
			String runnerYearString = year.getYearString();

			try {
				Collection distancesGroups = getRunBusiness(iwc)
						.getDistancesMap(runner.getRun(), runnerYearString);
				if (distancesGroups != null) {
					Iterator it = distancesGroups.iterator();
					if (it.hasNext()) {
						runner.setDistance(ConverterUtility.getInstance()
								.convertGroupToDistance((Group) it.next()));
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
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
			try {
				runner.setCountry(getUserBusiness(iwc)
						.getAddressBusiness()
						.getCountryHome()
						.findByPrimaryKey(
								new Integer(iwc.getParameter(PARAMETER_COUNTRY))));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		if (iwc.isParameterSet(PARAMETER_GENDER)) {
			try {
				runner.setGender(getGenderBusiness(iwc).getGender(
						new Integer(iwc.getParameter(PARAMETER_GENDER))));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		if (iwc.isParameterSet(PARAMETER_NATIONALITY)) {
			try {
				runner.setNationality(getUserBusiness(iwc)
						.getAddressBusiness()
						.getCountryHome()
						.findByPrimaryKey(
								new Integer(iwc
										.getParameter(PARAMETER_NATIONALITY))));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		if (iwc.isParameterSet(PARAMETER_EMAIL)) {
			runner.setEmail(iwc.getParameter(PARAMETER_EMAIL));
		}
		if (iwc.isParameterSet(PARAMETER_EMAIL2)) {
			runner.setEmail2(iwc.getParameter(PARAMETER_EMAIL2));
		}
		if (iwc.isParameterSet(PARAMETER_HOME_PHONE)) {
			runner.setHomePhone(iwc.getParameter(PARAMETER_HOME_PHONE));
		}
		if (iwc.isParameterSet(PARAMETER_MOBILE_PHONE)) {
			runner.setMobilePhone(iwc.getParameter(PARAMETER_MOBILE_PHONE));
		}

		if (iwc.isParameterSet(PARAMETER_QUESTION1_HOUR)) {
			runner.setQuestion1Hour(iwc.getParameter(PARAMETER_QUESTION1_HOUR));
		}
		if (iwc.isParameterSet(PARAMETER_QUESTION1_MINUTE)) {
			runner.setQuestion1Minute(iwc
					.getParameter(PARAMETER_QUESTION1_MINUTE));
		}
		if (iwc.isParameterSet(PARAMETER_QUESTION3_HOUR)) {
			runner.setQuestion3Hour(iwc.getParameter(PARAMETER_QUESTION3_HOUR));
		}
		if (iwc.isParameterSet(PARAMETER_QUESTION3_MINUTE)) {
			runner.setQuestion3Minute(iwc
					.getParameter(PARAMETER_QUESTION3_MINUTE));
		}
		if (iwc.isParameterSet(PARAMETER_QUESTION1_YEAR)) {
			runner.setQuestion1Year(iwc.getParameter(PARAMETER_QUESTION1_YEAR));
		}
		if (iwc.isParameterSet(PARAMETER_QUESTION3_YEAR)) {
			runner.setQuestion3Year(iwc.getParameter(PARAMETER_QUESTION3_YEAR));
		}
		if (iwc.isParameterSet(PARAMETER_QUESTION1_NEVER)) {
			runner.setQuestion1NeverRan(true);
		} else {
			runner.setQuestion1NeverRan(false);
		}
		if (iwc.isParameterSet(PARAMETER_QUESTION3_NEVER)) {
			runner.setQuestion3NeverRan(true);
		} else {
			runner.setQuestion3NeverRan(false);
		}

		if (personalID != null) {
			addRunner(iwc, personalID, runner);
		} else if (dateOfBirth != null) {
			addRunner(iwc, dateOfBirth, runner);
		}

		return runner;
	}

	private Runner getRunner(IWContext iwc, String key) {
		Map runnerMap = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		if (runnerMap != null) {
			return (Runner) runnerMap.get(key);
		}
		return null;
	}

	private void addRunner(IWContext iwc, String key, Runner runner) {
		Map runnerMap = new HashMap();
		runnerMap.put(key, runner);
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP, runnerMap);
	}

	private void removeRunner(IWContext iwc, String key) {
		Map runnerMap = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		if (runnerMap == null) {
			runnerMap = new HashMap();
		}
		runnerMap.remove(key);
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP, runnerMap);
	}

	protected int parseAction(IWContext iwc) throws RemoteException {
		Runner runner = null;
		try {
			runner = getRunner();
		} catch (RuntimeException fe) {
			getParentPage().setAlertOnLoad(
					localize("lv_reg.user_not_found_for_personal_id",
							"No user found with personal ID."));
			initializeSteps(iwc);
			return ACTION_STEP_PERSONLOOKUP;
		}

		if (runner != null && runner.getUser() != null) {
			if (this.getRunBusiness(iwc).isRegisteredInRun(
					runner.getYear().getYearString(), runner.getRun(),
					runner.getUser())) {
				getParentPage().setAlertOnLoad(
						localize("lv_reg.already_registered",
								"You are already registered in this run."));
				if (this.isIcelandicPersonalID) {
					initializeSteps(iwc);
					return ACTION_STEP_PERSONLOOKUP;
				} else {
					return ACTION_STEP_PERSONALDETAILS;
				}

			}
		}

		if ((runner != null && runner.getDateOfBirth() != null && isIcelandicPersonalID)
				|| (runner != null && runner.getUser() != null && runner
						.getUser().getDateOfBirth() != null && isIcelandicPersonalID)) {
			Date dateOfBirth;
			if (runner.getDateOfBirth() != null) {
				dateOfBirth = runner.getDateOfBirth();
			} else {
				dateOfBirth = runner.getUser().getDateOfBirth();
			}
			
			IWTimestamp dob = new IWTimestamp(dateOfBirth);
			
			if (runner.getYear() != null) {
				int year = Integer.parseInt(runner.getYear().getYearString());
				int maximumAgeForRun = runner.getYear().getMaximumAgeForRun();
				
				if (maximumAgeForRun != -1 && year - dob.getYear() > maximumAgeForRun) {
					Object[] args = { String.valueOf(maximumAgeForRun) };
					getParentPage()
							.setAlertOnLoad(
									MessageFormat
											.format(localize(
													"lv_reg.invalid_date_of_birth_exeeding",
													"Invalid date of birth.  You have to be {0} or younger to register"),
													args));
						initializeSteps(iwc);
						return ACTION_STEP_PERSONLOOKUP;
				}

				int minimumAgeForRun = runner.getYear().getMinimumAgeForRun();
				if (minimumAgeForRun != -1 && year - dob.getYear() < minimumAgeForRun) {
					Object[] args = { String.valueOf(minimumAgeForRun) };
					getParentPage()
							.setAlertOnLoad(
									MessageFormat
											.format(localize(
													"lv_reg.invalid_date_of_birth",
													"Invalid date of birth.  You have to be {0} or older to register"),
													args));
						initializeSteps(iwc);
						return ACTION_STEP_PERSONLOOKUP;
				}
			}
		}

		if (runner != null && runner.getEmail() != null
				&& runner.getEmail2() != null) {
			if (!runner.getEmail().equals(runner.getEmail2())) {
				getParentPage()
						.setAlertOnLoad(
								localize("lv_reg.email_dont_match",
										"Emails do not match. Please type the same email in both email inputs"));
				return ACTION_STEP_PERSONALDETAILS;
			}
		}

		int nextStep = super.parseAction(iwc);

		if (nextStep == ACTION_STEP_OVERVIEW) {
			if (runner.getQuestion1NeverRan() == false
					&& (runner.getQuestion1Hour().equals("-1")
							|| runner.getQuestion1Minute().equals("-1") || runner
							.getQuestion1Year().equals("-1"))) {
				showQuestionsError = true;
			}

			if (runner.getQuestion3NeverRan() == false
					&& (runner.getQuestion3Hour().equals("-1")
							|| runner.getQuestion3Minute().equals("-1") || runner
							.getQuestion3Year().equals("-1"))) {
				showQuestionsError = true;
			}

			if (showQuestionsError) {
				return ACTION_STEP_QUESTIONS;
			}
		}

		return nextStep;

	}

	private DropdownMenu createYearDropDown(String name, int yearFrom,
			int yearTo, String selected) {
		DropdownMenu theYear = new DropdownMenu(name);
		theYear.addMenuElement("-1", localize("year", "Year"));
		for (int i = yearFrom; i <= yearTo; i++) {
			theYear.addMenuElement(i, Integer.toString(i));
		}

		if (selected != null && !"".equals(selected)) {
			theYear.setSelectedElement(selected);
		}

		return theYear;
	}

	private DropdownMenu createHourDropDown(String name, String selected) {
		DropdownMenu theHour = new DropdownMenu(name);
		theHour.addMenuElement("-1", localize("hour", "Hour"));
		theHour.addMenuElement("00", "00");
		theHour.addMenuElement("01", "01");
		theHour.addMenuElement("02", "02");
		theHour.addMenuElement("03", "03");
		theHour.addMenuElement("04", "04");
		theHour.addMenuElement("05", "05");
		theHour.addMenuElement("06", "06");
		theHour.addMenuElement("07", "07");
		theHour.addMenuElement("08", "08");
		theHour.addMenuElement("09", "09");
		theHour.addMenuElement("10", "10");
		theHour.addMenuElement("11", "11");
		theHour.addMenuElement("12", "12");
		theHour.addMenuElement("13", "13");
		theHour.addMenuElement("14", "14");
		theHour.addMenuElement("15", "15");
		theHour.addMenuElement("16", "16");
		theHour.addMenuElement("17", "17");
		theHour.addMenuElement("18", "18");
		theHour.addMenuElement("19", "19");
		theHour.addMenuElement("20", "20");
		theHour.addMenuElement("21", "21");
		theHour.addMenuElement("22", "22");
		theHour.addMenuElement("23", "23");

		if (selected != null && !"".equals(selected)) {
			theHour.setSelectedElement(selected);
		}
		return theHour;
	}

	private DropdownMenu createMinuteDropDown(String name, String selected) {
		DropdownMenu theMinute = new DropdownMenu(name);
		theMinute.addMenuElement("-1", localize("minute", "Minute"));

		theMinute.addMenuElement("00", "00");
		theMinute.addMenuElement("01", "01");
		theMinute.addMenuElement("02", "02");
		theMinute.addMenuElement("03", "03");
		theMinute.addMenuElement("04", "04");
		theMinute.addMenuElement("05", "05");
		theMinute.addMenuElement("06", "06");
		theMinute.addMenuElement("07", "07");
		theMinute.addMenuElement("08", "08");
		theMinute.addMenuElement("09", "09");
		theMinute.addMenuElement("10", "10");
		theMinute.addMenuElement("11", "11");
		theMinute.addMenuElement("12", "12");
		theMinute.addMenuElement("13", "13");
		theMinute.addMenuElement("14", "14");
		theMinute.addMenuElement("15", "15");
		theMinute.addMenuElement("16", "16");
		theMinute.addMenuElement("17", "17");
		theMinute.addMenuElement("18", "18");
		theMinute.addMenuElement("19", "19");
		theMinute.addMenuElement("20", "20");
		theMinute.addMenuElement("21", "21");
		theMinute.addMenuElement("22", "22");
		theMinute.addMenuElement("23", "23");
		theMinute.addMenuElement("24", "24");
		theMinute.addMenuElement("25", "25");
		theMinute.addMenuElement("26", "26");
		theMinute.addMenuElement("27", "27");
		theMinute.addMenuElement("28", "28");
		theMinute.addMenuElement("29", "29");
		theMinute.addMenuElement("30", "30");
		theMinute.addMenuElement("31", "31");
		theMinute.addMenuElement("32", "32");
		theMinute.addMenuElement("33", "33");
		theMinute.addMenuElement("34", "34");
		theMinute.addMenuElement("35", "35");
		theMinute.addMenuElement("36", "36");
		theMinute.addMenuElement("37", "37");
		theMinute.addMenuElement("38", "38");
		theMinute.addMenuElement("39", "39");
		theMinute.addMenuElement("40", "40");
		theMinute.addMenuElement("41", "41");
		theMinute.addMenuElement("42", "42");
		theMinute.addMenuElement("43", "43");
		theMinute.addMenuElement("44", "44");
		theMinute.addMenuElement("45", "45");
		theMinute.addMenuElement("46", "46");
		theMinute.addMenuElement("47", "47");
		theMinute.addMenuElement("48", "48");
		theMinute.addMenuElement("49", "49");
		theMinute.addMenuElement("50", "50");
		theMinute.addMenuElement("51", "51");
		theMinute.addMenuElement("52", "52");
		theMinute.addMenuElement("53", "53");
		theMinute.addMenuElement("54", "54");
		theMinute.addMenuElement("55", "55");
		theMinute.addMenuElement("56", "56");
		theMinute.addMenuElement("57", "57");
		theMinute.addMenuElement("58", "58");
		theMinute.addMenuElement("59", "59");

		if (selected != null && !"".equals(selected)) {
			theMinute.setSelectedElement(selected);
		}

		return theMinute;
	}

	private void loadPreviousStep(IWContext iwc) throws RemoteException {
		loadCurrentStep(iwc,
				Integer.parseInt(iwc.getParameter(PARAMETER_FROM_ACTION)));
	}

	/**
	 * Called by StepsBlock
	 */
	protected void initializeSteps(IWContext iwc) {
		addStep(iwc, ACTION_STEP_PERSONLOOKUP,
				localize("lv_reg.registration", "Registration"));

		addStep(iwc, ACTION_STEP_PERSONALDETAILS,
				localize("lv_reg.registration", "Registration details"));

		addStep(iwc, ACTION_STEP_QUESTIONS,
				localize("lv_reg.questions", "Answer the following questions"));

		addStep(iwc, ACTION_STEP_OVERVIEW,
				localize("lv_reg.overview", "Overview"));

		addStep(iwc, ACTION_STEP_REGISTER,
				localize("lv_reg.receipt", "Registration saved"));
	}
}