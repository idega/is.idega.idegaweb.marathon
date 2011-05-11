package is.idega.idegaweb.marathon.presentation.mr;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.business.Runner;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.presentation.ActiveRunDropDownMenu;
import is.idega.idegaweb.marathon.presentation.DistanceDropDownMenu;
import is.idega.idegaweb.marathon.presentation.Registration;
import is.idega.idegaweb.marathon.presentation.RegistrationReceivedPrintable;
import is.idega.idegaweb.marathon.presentation.RunBlock;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.sql.Date;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.ejb.FinderException;
import javax.faces.component.UIComponent;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOCreateException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
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
import com.idega.util.LocaleUtil;
import com.idega.util.text.SocialSecurityNumber;

/**
 * Registration class for the Midnight Run.
 * 
 * @author palli
 * 
 */
public class MRRegistration extends RunBlock {

	public static final String SESSION_ATTRIBUTE_RUNNER_MAP = "sa_runner_map";
	public static final String SESSION_ATTRIBUTE_ICELANDIC_PERSONAL_ID_RUNNERS = "sa_icelandic_runners";
	public static final String SESSION_ATTRIBUTE_PARTICIPANTS = Registration.SESSION_ATTRIBUTE_PARTICIPANTS;
	public static final String SESSION_ATTRIBUTE_AMOUNT = Registration.SESSION_ATTRIBUTE_AMOUNT;
	public static final String SESSION_ATTRIBUTE_CARD_NUMBER = Registration.SESSION_ATTRIBUTE_CARD_NUMBER;
	public static final String SESSION_ATTRIBUTE_PAYMENT_DATE = Registration.SESSION_ATTRIBUTE_PAYMENT_DATE;

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

	private static final String PARAMETER_DISTANCE = "prm_distance";

	private static final String PARAMETER_AGREE = "prm_agree";

	private static final String PARAMETER_REFERENCE_NUMBER = "prm_reference_number";
	private static final String PARAMETER_NAME_ON_CARD = "prm_name_on_card";
	private static final String PARAMETER_CARD_NUMBER = "prm_card_number";
	private static final String PARAMETER_EXPIRES_MONTH = "prm_expires_month";
	private static final String PARAMETER_EXPIRES_YEAR = "prm_expires_year";
	private static final String PARAMETER_CCV = "prm_ccv";
	private static final String PARAMETER_AMOUNT = "prm_amount";
	private static final String PARAMETER_CARD_HOLDER_EMAIL = "prm_card_holder_email";

	private static final int ACTION_STEP_PERSONLOOKUP = 10;
	private static final int ACTION_STEP_PERSONALDETAILS = 20;
	private static final int ACTION_STEP_RUNDETAILS = 30;
	private static final int ACTION_STEP_DISCLAIMER = 40;
	private static final int ACTION_STEP_OVERVIEW = 50;
	private static final int ACTION_STEP_PAYMENT = 60;
	private static final int ACTION_STEP_RECEIPT = 70;
	private static final int ACTION_CANCEL = 80;

	private static final double MILLISECONDS_IN_YEAR = 31557600000d;

	private static final String MIDNIGHT_RUN_GROUP_ID = "6";

	private boolean isIcelandicPersonalID = false;
	private Runner setRunner;
	private boolean disablePaymentAndOverviewSteps = false;

	public void main(IWContext iwc) throws Exception {
		if (!iwc.isInEditMode()) {
			this.isIcelandicPersonalID = iwc.getCurrentLocale().equals(
					LocaleUtil.getIcelandicLocale());

			if (!this.isIcelandicPersonalID) {
				if (getRunner() != null) {
					if (getRunner().getPersonalID() != null
							&& !"".equals(getRunner().getPersonalID().trim())) {
						if (SocialSecurityNumber
								.isValidIcelandicSocialSecurityNumber(getRunner()
										.getPersonalID())) {
							this.isIcelandicPersonalID = true;
						}
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
		case ACTION_STEP_RUNDETAILS:
			stepRunDetails(iwc);
			break;
		case ACTION_STEP_DISCLAIMER:
			stepDisclaimer(iwc);
			break;
		case ACTION_STEP_OVERVIEW:
			stepOverview(iwc);
			break;
		case ACTION_STEP_PAYMENT:
			stepPayment(iwc);
			break;
		case ACTION_STEP_RECEIPT:
			stepReceipt(iwc);
			break;
		case ACTION_CANCEL:
			cancel(iwc);
			break;
		}
	}

	private void stepPersonalLookup(IWContext iwc) {
		Form form = new Form();

		form.addParameter(PARAMETER_ACTION, ACTION_NEXT);
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_PERSONLOOKUP);

		form.add(getStepsHeader(iwc, ACTION_STEP_PERSONLOOKUP));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		boolean allowNoPersonalID = true;
		if (!isRunnerMapEmpty(iwc)) {
			Boolean previousIcelandic = (Boolean) iwc
					.getSessionAttribute(SESSION_ATTRIBUTE_ICELANDIC_PERSONAL_ID_RUNNERS);
			if (previousIcelandic != null && previousIcelandic.booleanValue()) {
				allowNoPersonalID = false;
			}
		}

		table.setHeight(row++, 12);

		table.add(
				getInformationTable(localize("mr_reg.information_text_step_1",
						"Information text 1...")), 1, row++);
		table.setHeight(row++, 6);

		// table.setCellpadding(1, row, 24);
		table.add(
				getHeader(localize("mr_reg.personal_id", "Personal ID") + ":"),
				1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);

		TextInput input = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_PERSONAL_ID));
		input.setAsIcelandicSSNumber(localize("mr_reg.not_valid_personal_id",
				"The personal ID you have entered is not valid"));
		input.setLength(10);
		input.setMaxlength(10);
		input.setInFocusOnPageLoad(true);
		if (this.isIcelandicPersonalID) {
			input.setAsNotEmpty(localize("mr_reg.not_valid_personal_id",
					"The personal ID you have entered is not valid"));
		} else if (!allowNoPersonalID) {
			input.setAsNotEmpty(localize("mr_reg.not_valid_personal_id",
					"The personal ID you have entered is not valid"));
		}
		table.add(input, 1, row++);

		if (!this.isIcelandicPersonalID && allowNoPersonalID) {
			// table.setCellpadding(1, row, 24);
			Layer noIcelandicSSNLayer = new Layer(Layer.DIV);

			CheckBox noIcelandicSSNCheck = getCheckBox(
					PARAMETER_NO_PERSONAL_ID, Boolean.TRUE.toString());
			noIcelandicSSNCheck.setToEnableWhenUnchecked(input);
			noIcelandicSSNCheck.setToDisableWhenChecked(input);

			Label noIcelandicSSNLabel = new Label(localize(
					"mr_reg.no_icelandic_ssn",
					"I do not have a Icelandic personal ID"),
					noIcelandicSSNCheck);
			noIcelandicSSNLayer.add(noIcelandicSSNCheck);
			noIcelandicSSNLayer.add(noIcelandicSSNLabel);

			table.add(noIcelandicSSNLayer, 1, row++);
		}

		UIComponent buttonsContainer = getButtonsFooter(iwc, false, true);
		form.add(buttonsContainer);

		if (!this.isIcelandicPersonalID) {
			Table advert = new Table();
			advert.setCellpadding(0);
			advert.setCellspacing(0);
			advert.setWidth(Table.HUNDRED_PERCENT);
			form.add(advert);

			advert.setHeight(1, 18);

			Link image = new Link(this.iwrb.getImage("icelandtotalcom.png"));
			image.setURL("http://www.icelandtotal.com/?utm_source=Rvk_Marathon&utm_medium=WebsiteBanner&utm_campaign=Reykjavikurmarathon");
			image.setTarget("_new");
			advert.add(image, 1, 2);
			advert.add(
					localize("mr_reg.iceland_total",
							"Reykjavik Marathon is proud of...."), 2, 2);
		}

		add(form);
	}

	private void stepPersonalDetails(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);

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
				getInformationTable(localize("mr_reg.information_text_step_2",
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
			nameField.setAsAlphabeticText(localize("mr_reg.name_err_msg",
					"Your name may only contain alphabetic characters"));
			nameField.setAsNotEmpty(localize("mr_reg.name_not_empty",
					"Name field can not be empty"));
		}

		DropdownMenu genderField = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_GENDER));
		Collection genders = getGenderBusiness(iwc).getAllGenders();
		genderField.addMenuElement("-1",
				localize("mr_reg.select_gender", "Select gender..."));
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
		/*if (this.isIcelandicPersonalID) {
			genderField.setDisabled(true);
			if (getRunner().getUser() != null) {
				genderField.setSelectedElement(getRunner().getUser()
						.getGenderID());
			}
		} else {*/
			genderField.setAsNotEmpty(localize("mr_reg.gender_not_empty",
					"Gender can not be empty"));
		//}

		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_NAME, "Name")), 1,
				iRow);
		if (!this.isIcelandicPersonalID) {
			choiceTable.add(redStar, 1, iRow);
		}
		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_GENDER, "Gender")),
				3, iRow);
		//if (!this.isIcelandicPersonalID) {
			choiceTable.add(redStar, 3, iRow++);
		/*} else {
			iRow++;
		}*/
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
		emailField.setAsEmail(localize("mr_reg.email_err_msg",
				"Not a valid email address"));
		emailField.setAsNotEmpty(localize("mr_reg.continue_without_email",
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
				localize("mr_reg.select_nationality", "Select nationality..."));
		countryField.addMenuElement("-1",
				localize("mr_reg.select_country", "Select country..."));
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
				"mr_reg.must_select_nationality",
				"You must select your nationality"));
		if (!this.isIcelandicPersonalID) {
			countryField.setAsNotEmpty(localize("mr_reg.must_select_country",
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

		if (this.isIcelandicPersonalID) {
			TextInput emailField2 = (TextInput) getStyledInterface(new TextInput(
					PARAMETER_EMAIL2));
			emailField2.setAsEmail(localize("mr_reg.email_err_msg",
					"Not a valid email address"));
			emailField2.setAsNotEmpty(localize("mr_reg.continue_without_email2",
					"You can not continue without repeating the e-mail"));
			choiceTable.add(
					getHeader(localize(IWMarathonConstants.RR_NATIONALITY,
							"Nationality")), 1, iRow);

			choiceTable.add(redStar, 1, iRow);
			choiceTable.add(
					getHeader(localize(IWMarathonConstants.RR_EMAIL2,
							"Email repeated")), 3, iRow);
			choiceTable.add(redStar, 3, iRow++);
			choiceTable.add(nationalityField, 1, iRow);
			choiceTable.add(emailField2, 3, iRow++);
			choiceTable.setHeight(iRow++, 3);


			TextInput telField = (TextInput) getStyledInterface(new TextInput(
					PARAMETER_HOME_PHONE));
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

			TextInput mobileField = (TextInput) getStyleObject(new TextInput(
					PARAMETER_MOBILE_PHONE), STYLENAME_INTERFACE);
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

			choiceTable.add(
					getHeader(localize(IWMarathonConstants.RR_TEL, "Telephone")),
					1, iRow);
			choiceTable.add(
					getHeader(localize(IWMarathonConstants.RR_MOBILE,
							"Mobile Phone")), 3, iRow++);
			choiceTable.add(telField, 1, iRow);
			choiceTable.add(mobileField, 3, iRow++);
			choiceTable.setHeight(iRow++, 3);
		} else {
			TextInput addressField = (TextInput) getStyledInterface(new TextInput(
					PARAMETER_ADDRESS));
			addressField.setAsNotEmpty(localize("mr_reg.must_provide_address",
					"You must enter your address."));
			if (getRunner().getAddress() != null) {
				addressField.setContent(getRunner().getAddress());
			}

			TextInput emailField2 = (TextInput) getStyledInterface(new TextInput(
					PARAMETER_EMAIL2));
			emailField2.setAsEmail(localize("mr_reg.email_err_msg",
					"Not a valid email address"));
			emailField2.setAsNotEmpty(localize("mr_reg.continue_without_email2",
					"You can not continue without repeating the e-mail"));
			choiceTable.add(
					getHeader(localize(IWMarathonConstants.RR_ADDRESS, "Address")),
					1, iRow);
			choiceTable.add(redStar, 1, iRow);
			choiceTable.add(
					getHeader(localize(IWMarathonConstants.RR_EMAIL2,
							"Email repeated")), 3, iRow);
			choiceTable.add(redStar, 3, iRow++);
			choiceTable.add(addressField, 1, iRow);
			choiceTable.add(emailField2, 3, iRow++);
			choiceTable.setHeight(iRow++, 3);

			TextInput cityField = (TextInput) getStyledInterface(new TextInput(
					PARAMETER_CITY));
			cityField.setAsNotEmpty(localize("mr_reg.must_provide_city",
					"You must enter your city of living."));
			if (getRunner().getCity() != null) {
				cityField.setContent(getRunner().getCity());
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
			choiceTable.add(redStar, 1, iRow);
			choiceTable.add(
					getHeader(localize(IWMarathonConstants.RR_TEL, "Telephone")),
					3, iRow++);
			choiceTable.add(cityField, 1, iRow);
			choiceTable.add(telField, 3, iRow++);
			choiceTable.setHeight(iRow++, 3);

			TextInput postalField = (TextInput) getStyledInterface(new TextInput(
					PARAMETER_POSTAL_CODE));
			postalField.setAsNotEmpty(localize("mr_reg.must_provide_postal",
					"You must enter your postal address."));
			postalField.setMaxlength(10);
			postalField.setLength(10);
			if (getRunner().getPostalCode() != null) {
				postalField.setContent(getRunner().getPostalCode());
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
			choiceTable.add(redStar, 1, iRow);
			choiceTable.add(
					getHeader(localize(IWMarathonConstants.RR_MOBILE,
							"Mobile Phone")), 3, iRow++);
			choiceTable.add(postalField, 1, iRow);
			choiceTable.add(mobileField, 3, iRow++);
			choiceTable.setHeight(iRow++, 3);

			choiceTable.add(
					getHeader(localize(IWMarathonConstants.RR_COUNTRY, "Country")),
					1, iRow);
			choiceTable.add(redStar, 1, iRow);
			choiceTable.add(
					getHeader(localize(IWMarathonConstants.RR_NATIONALITY,
							"Nationality")), 3, iRow);
			choiceTable.add(redStar, 3, iRow++);

			choiceTable.add(countryField, 1, iRow);
			choiceTable.add(nationalityField, 3, iRow++);
			choiceTable.setHeight(iRow++, 3);			
		}

		UIComponent buttonsContainer = getButtonsFooter(iwc, false, true);
		form.add(buttonsContainer);

		add(form);

	}
	private void stepOverview(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_DATE_OF_BIRTH);

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
				getInformationTable(localize("mr_reg.information_text_step_5",
						"Information text 5...")), 1, row++);
		table.setHeight(row++, 18);

		Map runners = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		Table runnerTable = new Table();
		// runnerTable.setColumns(columns)
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		runnerTable.add(
				getHeader(localize("mr_reg.runner_name", "Runner name")), 1, 1);
		runnerTable.add(getHeader(localize("mr_reg.run", "Run")), 2, 1);
		runnerTable.add(getHeader(localize("mr_reg.distance", "Distance")), 3,
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
								.getDistance().getName())), 3, runRow);
				if (runner.getPartner1SSN() != null
						&& !"".equals(runner.getPartner1SSN())) {
					if (!addedLegHeader) {
						runnerTable.add(
								getHeader(localize("mr_reg.relay_leg", "Leg")),
								4, 1);
						addedLegHeader = true;
					}

					runnerTable.add(getText(runner.getRelayLeg()), 4, runRow++);

					runnerTable.add(getText(runner.getPartner1Name()), 1,
							runRow);
					runnerTable
							.add(getText(localize(runner.getRun().getName(),
									runner.getRun().getName())
									+ " "
									+ runner.getDistance().getYear().getName()),
									2, runRow);
					runnerTable
							.add(getText(localize(runner.getDistance()
									.getName(), runner.getDistance().getName())),
									3, runRow);
					runnerTable.add(getText(runner.getPartner1Leg()), 4,
							runRow++);

					if (runner.getPartner2SSN() != null
							&& !"".equals(runner.getPartner2SSN())) {
						runnerTable.add(getText(runner.getPartner2Name()), 1,
								runRow);
						runnerTable.add(
								getText(localize(runner.getRun().getName(),
										runner.getRun().getName())
										+ " "
										+ runner.getDistance().getYear()
												.getName()), 2, runRow);
						runnerTable.add(
								getText(localize(
										runner.getDistance().getName(), runner
												.getDistance().getName())), 3,
								runRow);
						runnerTable.add(getText(runner.getPartner2Leg()), 4,
								runRow++);

						if (runner.getPartner3SSN() != null
								&& !"".equals(runner.getPartner3SSN())) {
							runnerTable.add(getText(runner.getPartner3Name()),
									1, runRow);
							runnerTable.add(
									getText(localize(runner.getRun().getName(),
											runner.getRun().getName())
											+ " "
											+ runner.getDistance().getYear()
													.getName()), 2, runRow);
							runnerTable.add(
									getText(localize(runner.getDistance()
											.getName(), runner.getDistance()
											.getName())), 3, runRow);
							runnerTable.add(getText(runner.getPartner3Leg()),
									4, runRow++);
						}
					}
				} else {
					runRow++;
				}

			} else {
				if (this.isIcelandicPersonalID) {
					removeRunner(iwc, runner.getPersonalID());
				} else {
					removeRunner(iwc, runner.getDateOfBirth().toString());
				}
			}
		}

		UIComponent buttonsContainer = getButtonsFooter(iwc, false, false);

		SubmitButton previous = getPreviousButton();
		SubmitButton registerOther = (SubmitButton) getButton(new SubmitButton(
				localize("run_reg.register_other", "Register other")));
		registerOther.setValueOnClick(PARAMETER_ACTION,
				String.valueOf(ACTION_START));
		if (this.isIcelandicPersonalID) {
			registerOther.setValueOnClick(PARAMETER_PERSONAL_ID, "");
		} else {
			registerOther.setValueOnClick(PARAMETER_DATE_OF_BIRTH, "");
		}

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize(
				"mr_reg.finish_registration", "Register")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_NEXT));

		buttonsContainer.getChildren().add(previous);
		buttonsContainer.getChildren().add(registerOther);
		buttonsContainer.getChildren().add(next);

		form.add(buttonsContainer);

		add(form);
	}

	private void stepRunDetails(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_DATE_OF_BIRTH);

		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_RUNDETAILS);

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);

		form.add(getStepsHeader(iwc, ACTION_STEP_RUNDETAILS));

		form.add(table);
		int row = 1;

		table.setHeight(row++, 12);

		table.add(
				getInformationTable(localize("mr_reg.information_text_step_20",
						"Information text run details...")), 1, row++);
		table.setHeight(row++, 18);

		Table choiceTable = new Table();
		choiceTable.setColumns(3);
		choiceTable.setCellpadding(2);
		choiceTable.setCellspacing(0);
		choiceTable.setWidth(1, "20%");
		choiceTable.setWidth(2, 12);
		choiceTable.setWidth(3, "80%");
		choiceTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(choiceTable, 1, row++);

		Text redStar = getHeader(" *");
		redStar.setFontColor("#ff0000");

		int iRow = 1;
		Runner runner = getRunner();

		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_PRIMARY_DD, "Run")),
				1, iRow);
		choiceTable.add(
				getHeader(localize(runner.getRun().getName(), runner.getRun()
						.getName())
						+ " " + runner.getYear().getName()), 3, iRow++);

		choiceTable.setHeight(iRow++, 5);

		DistanceDropDownMenu distanceDropdown = (DistanceDropDownMenu) getStyledInterface(new DistanceDropDownMenu(
				PARAMETER_DISTANCE, runner));
		distanceDropdown.setAsNotEmpty(localize("mr_reg.must_select_distance",
				"You have to select a distance"));

		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_SECONDARY_DD,
						"Distance")), 1, iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(distanceDropdown, 3, iRow++);

		choiceTable.setHeight(iRow++, 10);

		UIComponent buttonsContainer = getButtonsFooter(iwc, true, true);
		form.add(buttonsContainer);

		add(form);
	}

	private void stepDisclaimer(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_DATE_OF_BIRTH);

		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_DISCLAIMER);

		form.add(getStepsHeader(iwc, ACTION_STEP_DISCLAIMER));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.setHeight(row++, 18);

		SubmitButton next = getNextButton();
		if (isDisablePaymentAndOverviewSteps()) {
			next = (SubmitButton) getButton(new SubmitButton(localize(
					"register", "Register")));
		} else {
			next = (SubmitButton) getButton(new SubmitButton(localize("next",
					"Next")));
		}
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_NEXT));
		if (!getRunner().isAgree()) {
			next.setDisabled(true);
		}

		table.add(
				getInformationTable(localize("mr_reg.information_text_step_4",
						"Information text 4...")), 1, row++);

		Layer disclaimerLayer = new Layer(Layer.DIV);
		CheckBox agreeCheck = getCheckBox(PARAMETER_AGREE,
				Boolean.TRUE.toString());
		agreeCheck.setToEnableWhenChecked(next);
		agreeCheck.setToDisableWhenUnchecked(next);
		agreeCheck.setChecked(getRunner().isAgree());

		Label disclaimerLabel = new Label(localize("mr_reg.agree_terms",
				"Yes, I agree"), agreeCheck);
		disclaimerLayer.add(agreeCheck);
		disclaimerLayer.add(disclaimerLabel);

		table.setHeight(row++, 18);

		table.add(disclaimerLayer, 1, row++);

		UIComponent buttonsContainer = getButtonsFooter(iwc, true, false);
		buttonsContainer.getChildren().add(next);
		form.add(buttonsContainer);

		add(form);
	}

	private void stepPayment(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_DATE_OF_BIRTH);
		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_PAYMENT);

		form.add(getStepsHeader(iwc, ACTION_STEP_PAYMENT));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.setHeight(row++, 12);

		table.add(
				getInformationTable(localize("mr_reg.information_text_step_6",
						"Information text 6...")), 1, row++);
		table.setHeight(row++, 18);

		Map runners = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		Table runnerTable = new Table();
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		runnerTable.setCellspacing(0);
		runnerTable.add(
				getHeader(localize("mr_reg.runner_name", "Runner name")), 1, 1);
		runnerTable.add(getHeader(localize("mr_reg.run", "Run")), 2, 1);
		runnerTable.add(getHeader(localize("mr_reg.distance", "Distance")), 3,
				1);
		runnerTable.add(getHeader(localize("mr_reg.price", "Price")), 4, 1);
		table.add(runnerTable, 1, row++);
		table.setHeight(row++, 18);
		int runRow = 2;

		float totalAmount = 0;
		Iterator iter = runners.values().iterator();
		boolean first = true;
		while (iter.hasNext()) {
			Runner runner = (Runner) iter.next();
			if (runner.getUser() != null) {
				runnerTable.add(getText(runner.getUser().getName()), 1, runRow);
			} else {
				runnerTable.add(getText(runner.getName()), 1, runRow);
			}

			runnerTable.add(
					getText(localize(runner.getRun().getName(), runner.getRun()
							.getName())), 2, runRow);
			runnerTable.add(
					getText(localize(runner.getDistance().getName(), runner
							.getDistance().getName())), 3, runRow);
			float runPrice = 0.0f;
			if (this.isIcelandicPersonalID) {
				runPrice = getRunBusiness(iwc).getPriceForRunner(runner,
						LocaleUtil.getIcelandicLocale());
			} else {
				runPrice = getRunBusiness(iwc).getPriceForRunner(runner,
						iwc.getCurrentLocale());
			}
			totalAmount += runPrice;
			if (this.isIcelandicPersonalID) {
				runnerTable.add(
						getText(formatAmount(LocaleUtil.getIcelandicLocale(),
								runPrice)), 4, runRow++);
			} else {
				runnerTable
						.add(getText(formatAmount(iwc.getCurrentLocale(),
								runPrice)), 4, runRow++);
			}

			runner.setAmount(runPrice);

			if (this.isIcelandicPersonalID) {
				addRunner(iwc, runner.getPersonalID(), runner);
			} else {
				addRunner(iwc, runner.getDateOfBirth().toString(), runner);
			}

			if (first) {
				if (this.isIcelandicPersonalID) {
					runnerTable.add(new HiddenInput(PARAMETER_REFERENCE_NUMBER,
							runner.getPersonalID().replaceAll("-", "")));
				} else {
					runnerTable.add(new HiddenInput(PARAMETER_REFERENCE_NUMBER,
							runner.getDateOfBirth().toString()
									.replaceAll("-", "")));
				}
				first = false;
			}
		}

		if (totalAmount == 0) {
			stepReceipt(iwc, false);
			return;
		}

		runnerTable.setHeight(runRow++, 12);
		runnerTable.add(
				getHeader(localize("mr_reg.total_amount", "Total amount")), 1,
				runRow);
		runnerTable.add(
				getHeader(formatAmount(iwc.getCurrentLocale(), totalAmount)),
				4, runRow);
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

		creditCardTable.add(
				getHeader(localize("mr_reg.credit_card_information",
						"Credit card information")), 1, creditRow);
		Collection images = getRunBusiness(iwc).getCreditCardImages();
		if (images != null) {
			Iterator iterator = images.iterator();
			while (iterator.hasNext()) {
				Image image = (Image) iterator.next();
				if (image != null) {
					String imageURL = image.getURL();
					if (imageURL != null && !imageURL.equals("")) {
						image.setToolTip(imageURL.substring(
								imageURL.lastIndexOf('/') + 1,
								imageURL.lastIndexOf('.')));
					}
				}
				creditCardTable.add(image, 3, creditRow);
				if (iterator.hasNext()) {
					creditCardTable
							.add(Text.getNonBrakingSpace(), 3, creditRow);
				}
			}
		}
		creditCardTable.setAlignment(3, creditRow++,
				Table.HORIZONTAL_ALIGN_RIGHT);
		creditCardTable.setHeight(creditRow++, 12);

		TextInput nameField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_NAME_ON_CARD));
		nameField.setAsNotEmpty(localize("mr_reg.must_supply_card_holder_name",
				"You must supply card holder name"));
		nameField.keepStatusOnAction(true);
		if (getRunner().getUser() != null) {
			nameField.setContent(getRunner().getUser().getName());
		} else {
			nameField.setContent(getRunner().getName());
		}

		TextInput ccv = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_CCV));
		ccv.setLength(3);
		ccv.setMaxlength(3);
		ccv.setAsIntegers(localize("mr_reg.not_valid_ccv",
				"Not a valid CCV number"));
		ccv.setAsNotEmpty(localize("mr_reg.must_supply_ccv",
				"You must enter the CCV number"));
		ccv.keepStatusOnAction(true);

		IWTimestamp stamp = new IWTimestamp();
		DropdownMenu month = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_EXPIRES_MONTH));
		for (int a = 1; a <= 12; a++) {
			month.addMenuElement(a < 10 ? "0" + a : String.valueOf(a),
					a < 10 ? "0" + a : String.valueOf(a));
		}
		month.keepStatusOnAction(true);
		DropdownMenu year = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_EXPIRES_YEAR));
		for (int a = stamp.getYear(); a <= stamp.getYear() + 8; a++) {
			year.addMenuElement(String.valueOf(a).substring(2),
					String.valueOf(a));
		}
		year.keepStatusOnAction(true);

		creditCardTable.add(
				getHeader(localize("mr_reg.card_holder", "Card holder")), 1,
				creditRow);
		creditCardTable.add(
				getHeader(localize("mr_reg.card_number", "Card number")), 3,
				creditRow++);
		creditCardTable.add(nameField, 1, creditRow);
		for (int a = 1; a <= 4; a++) {
			TextInput cardNumber = (TextInput) getStyledInterfaceCreditcard(new TextInput(
					PARAMETER_CARD_NUMBER + "_" + a));
			if (a < 4) {
				cardNumber.setLength(4);
				cardNumber.setMaxlength(4);
				cardNumber.setNextInput(PARAMETER_CARD_NUMBER + "_" + (a + 1));
			} else {
				cardNumber.setLength(4);
				cardNumber.setMaxlength(7);
			}
			cardNumber.setMininumLength(
					4,
					localize("mr_reg.not_valid_card_number",
							"Not a valid card number"));
			cardNumber.setAsIntegers(localize("mr_reg.not_valid_card_number",
					"Not a valid card number"));
			cardNumber.setAsNotEmpty(localize("mr_reg.must_supply_card_number",
					"You must enter the credit card number"));
			cardNumber.keepStatusOnAction(true);

			creditCardTable.add(cardNumber, 3, creditRow);
			if (a != 4) {
				creditCardTable.add(Text.getNonBrakingSpace(), 3, creditRow);
			}
		}
		creditRow++;
		creditCardTable.setHeight(creditRow++, 3);

		creditCardTable.add(
				getHeader(localize("mr_reg.card_expires", "Card expires")), 1,
				creditRow);
		creditCardTable.add(
				getHeader(localize("mr_reg.ccv_number", "CCV number")), 3,
				creditRow++);
		creditCardTable.add(month, 1, creditRow);
		creditCardTable.add(getText("/"), 1, creditRow);
		creditCardTable.add(year, 1, creditRow);
		creditCardTable.add(ccv, 3, creditRow++);

		TextInput emailField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_CARD_HOLDER_EMAIL));
		emailField.setAsEmail(localize("mr_reg.email_err_msg",
				"Not a valid email address"));
		emailField.setLength(30);
		emailField.keepStatusOnAction(true);
		emailField.setContent(getRunner().getEmail());

		creditCardTable.setHeight(creditRow++, 3);
		creditCardTable.mergeCells(3, creditRow, 3, creditRow + 1);
		creditCardTable
				.add(getText(localize(
						"mr_reg.ccv_explanation_text",
						"A CCV number is a three digit number located on the back of all major credit cards.")),
						3, creditRow);
		creditCardTable.add(
				getHeader(localize("mr_reg.card_holder_email",
						"Cardholder email")), 1, creditRow++);
		creditCardTable.add(emailField, 1, creditRow++);
		creditCardTable.add(new HiddenInput(PARAMETER_AMOUNT, String
				.valueOf(totalAmount)));
		creditCardTable.setHeight(creditRow++, 18);
		creditCardTable.mergeCells(1, creditRow, creditCardTable.getColumns(),
				creditRow);
		creditCardTable.add(
				getText(localize("mr_reg.read_conditions",
						"Please read before you finish your payment")), 1,
				creditRow);

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize(
				"mr_reg.pay", "Pay fee")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_NEXT));
		next.setDisabled(true);

		CheckBox agree = getCheckBox(PARAMETER_AGREE + "_terms",
				Boolean.TRUE.toString());
		agree.setToEnableWhenChecked(next);
		agree.setToDisableWhenUnchecked(next);

		creditCardTable.setHeight(creditRow++, 12);
		creditCardTable.mergeCells(1, creditRow, creditCardTable.getColumns(),
				creditRow);
		creditCardTable.add(agree, 1, creditRow);
		creditCardTable.add(Text.getNonBrakingSpace(), 1, creditRow);
		creditCardTable
				.add(getHeader(localize("mr_reg.agree_terms_and_conditions",
						"I agree to the terms and conditions")), 1, creditRow++);

		UIComponent buttonsContainer = getButtonsFooter(iwc, true, false);
		buttonsContainer.getChildren().add(next);
		form.add(buttonsContainer);

		form.setToDisableOnSubmit(next, true);

		add(form);
	}

	private void stepReceipt(IWContext iwc) throws RemoteException {
		boolean doPayment = true;
		if (isDisablePaymentAndOverviewSteps()) {
			doPayment = false;
		}
		boolean disablePaymentProcess = "true".equalsIgnoreCase(iwc
				.getApplicationSettings().getProperty(
						"MARATHON_DISABLE_PAYMENT_AUTH", "false"));
		if (doPayment && disablePaymentProcess) {
			doPayment = false;
		}
		stepReceipt(iwc, doPayment);
	}

	private void stepReceipt(IWContext iwc, boolean doPayment)
			throws RemoteException {
		try {
			if (getRunner().getDistance() == null) {
				getParentPage()
						.setAlertOnLoad(
								localize(
										"mr_reg.session_has_expired_payment",
										"Session has expired and information from earlier steps is lost. \\nYou will have to enter the information again. \\nYour credit card has not been charged."));
				stepPersonalDetails(iwc);
				return;
			}
			Collection runners = ((Map) iwc
					.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP))
					.values();

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

			if (doPayment) {
				nameOnCard = iwc.getParameter(PARAMETER_NAME_ON_CARD);
				cardNumber = "";
				for (int i = 1; i <= 4; i++) {
					cardNumber += iwc.getParameter(PARAMETER_CARD_NUMBER + "_"
							+ i);
				}
				hiddenCardNumber = "XXXX-XXXX-XXXX-"
						+ iwc.getParameter(PARAMETER_CARD_NUMBER + "_" + 4);
				expiresMonth = iwc.getParameter(PARAMETER_EXPIRES_MONTH);
				expiresYear = iwc.getParameter(PARAMETER_EXPIRES_YEAR);
				ccVerifyNumber = iwc.getParameter(PARAMETER_CCV);
				email = iwc.getParameter(PARAMETER_CARD_HOLDER_EMAIL);
				amount = Double.parseDouble(iwc.getParameter(PARAMETER_AMOUNT));
				referenceNumber = iwc.getParameter(PARAMETER_REFERENCE_NUMBER);
			}

			String properties = null;

			if (doPayment) {
				properties = getRunBusiness(iwc).authorizePayment(nameOnCard,
						cardNumber, expiresMonth, expiresYear, ccVerifyNumber,
						amount, this.isIcelandicPersonalID ? "ISK" : "EUR",
						referenceNumber);
			}

			Collection participants = getRunBusiness(iwc).saveParticipants(
					runners, email, hiddenCardNumber, amount, paymentStamp,
					iwc.getCurrentLocale(), isDisablePaymentAndOverviewSteps(),
					"mr_reg.");

			if (doPayment) {
				getRunBusiness(iwc).finishPayment(properties);
			}

			iwc.removeSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
			iwc.removeApplicationAttribute(SESSION_ATTRIBUTE_ICELANDIC_PERSONAL_ID_RUNNERS);

			showReceipt(iwc, participants, amount, hiddenCardNumber,
					paymentStamp, doPayment);
		} catch (IDOCreateException ice) {
			getParentPage()
					.setAlertOnLoad(
							localize("mr_reg.save_failed",
									"There was an error when trying to finish registration."));
			ice.printStackTrace();
			loadPreviousStep(iwc);
		} catch (CreditCardAuthorizationException ccae) {
			IWResourceBundle creditCardBundle = iwc.getIWMainApplication()
					.getBundle("com.idega.block.creditcard")
					.getResourceBundle(iwc.getCurrentLocale());
			getParentPage().setAlertOnLoad(
					ccae.getLocalizedMessage(creditCardBundle));
			loadPreviousStep(iwc);
		}

	}

	private void showReceipt(IWContext iwc, Collection runners, double amount,
			String cardNumber, IWTimestamp paymentStamp, boolean doPayment) {

		add(getStepsHeader(iwc, ACTION_STEP_RECEIPT));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS, runners);
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_AMOUNT, new Double(amount));
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_CARD_NUMBER, cardNumber);
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_PAYMENT_DATE, paymentStamp);

		Group run = null;
		Run selectedRun = null;
		Iterator it = runners.iterator();
		if (it.hasNext()) {
			Participant participant = (Participant) it.next();
			run = participant.getRunTypeGroup();
			try {
				selectedRun = ConverterUtility.getInstance().convertGroupToRun(
						run);
			} catch (FinderException e) {
			}
		}

		table.setHeight(row++, 18);

		String greeting = localize("mr_reg.hello_participant",
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
				getText(localize("mr_reg.payment_received",
						"You have registered for the following:")), 1, row++);
		table.setHeight(row++, 8);

		Table runnerTable = new Table(5, runners.size() + 4);
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		int col = 1;
		runnerTable.add(
				getHeader(localize("mr_reg.runner_name", "Runner name")),
				col++, 1);
		runnerTable.add(getHeader(localize("mr_reg.run", "Run")), col++, 1);
		runnerTable.add(getHeader(localize("mr_reg.distance", "Distance")),
				col++, 1);
		runnerTable.add(
				getHeader(localize("mr_reg.race_number", "Race number")),
				col++, 1);
		table.add(runnerTable, 1, row++);
		int runRow = 2;
		Iterator iter = runners.iterator();
		while (iter.hasNext()) {
			Participant participant = (Participant) iter.next();
			run = participant.getRunTypeGroup();
			Group distance = participant.getRunDistanceGroup();
			col = 1;
			runnerTable.add(getText(participant.getUser().getName()), col++,
					runRow);
			runnerTable.add(getText(localize(run.getName(), run.getName())
					+ " " + participant.getRunYearGroup().getName()), col++,
					runRow);
			runnerTable.add(
					getText(localize(distance.getName(), distance.getName())),
					col++, runRow);
			runnerTable
					.add(getText(String.valueOf(participant
							.getParticipantNumber())), col++, runRow);
		}

		if (doPayment) {
			Table creditCardTable = new Table(2, 3);
			creditCardTable.add(
					getHeader(localize("mr_reg.payment_received_timestamp",
							"Payment received") + ":"), 1, 1);
			creditCardTable.add(getText(paymentStamp.getLocaleDateAndTime(
					iwc.getCurrentLocale(), IWTimestamp.SHORT,
					IWTimestamp.SHORT)), 2, 1);
			creditCardTable.add(
					getHeader(localize("mr_reg.card_number", "Card number")
							+ ":"), 1, 2);
			creditCardTable.add(getText(cardNumber), 2, 2);
			creditCardTable.add(getHeader(localize("mr_reg.amount", "Amount")
					+ ":"), 1, 3);
			creditCardTable
					.add(getText(formatAmount(iwc.getCurrentLocale(),
							(float) amount)), 2, 3);
			table.setHeight(row++, 16);
			table.add(creditCardTable, 1, row++);
		}

		table.setHeight(row++, 16);
		table.add(
				getHeader(localize("mr_reg.receipt_info_headline",
						"Receipt - Please print it out")), 1, row++);
		table.add(
				getText(localize(
						"mr_reg.receipt_info_headline_body",
						"This document is your receipt, please print it out and bring it with you when you collect your race material.")),
				1, row++);

		if (selectedRun != null) {
			table.setHeight(row++, 16);
			table.add(
					getHeader(localize(
							"mr_reg.delivery_of_race_material_headline",
							"Further information about the run is available on:")),
					1, row++);
			String informationText;
			if (iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale())) {
				informationText = selectedRun.getRunRegistrationReceiptInfo();
			} else {
				informationText = selectedRun
						.getRunRegistrationReceiptInfoEnglish();
			}
			table.add(getText(informationText), 1, row++);
		}

		table.setHeight(row++, 16);
		table.add(getText(localize("mr_reg.best_regards", "Best regards,")), 1,
				row++);

		if (selectedRun != null) {
			// table.add(getText(localize(selectedRun.getName(), selectedRun
			// .getName())), 1, row++);
			if (iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale())) {
				table.add(getText(selectedRun.getRunInformationPage()), 1,
						row++);
			} else {
				table.add(getText(selectedRun.getEnglishRunInformationPage()),
						1, row++);
			}
		}

		add(table);

		Link print = new Link(localize("print", "Print receipt"));
		print.setPublicWindowToOpen(RegistrationReceivedPrintable.class);
		print.setParameter(RegistrationReceivedPrintable.PARAM_HIDE_TSHIRT, "hide_it");

		UIComponent buttonsContainer = getButtonsFooter(iwc, false, false);
		buttonsContainer.getChildren().add(print);
		add(buttonsContainer);

	}

	private String formatAmount(Locale locale, float amount) {
		return NumberFormat.getInstance(locale).format(amount) + " "
				+ (this.isIcelandicPersonalID ? "ISK" : "EUR");
	}

	private boolean isChild(Runner theRunner) {
		Age age = null;
		if (theRunner.getUser() != null) {
			age = new Age(theRunner.getUser().getDateOfBirth());
		} else {
			age = new Age(theRunner.getDateOfBirth());
		}
		return age.getYears() <= 16;
	}

	protected ActiveRunDropDownMenu getRunDropdown(IWContext iwc, Runner runner) {
		ActiveRunDropDownMenu runDropdown = null;
		runDropdown = (ActiveRunDropDownMenu) getStyledInterface(new ActiveRunDropDownMenu(
				"TMP", runner, null, false));

		runDropdown.setAsNotEmpty(localize("mr_reg.must_select_run",
				"You have to select a run"));
		try {
			// main is run to check if any runs exists. Otherwise
			// isConstrainedToOneRun() will not work
			runDropdown.main(iwc);
		} catch (Exception e) {
			e.printStackTrace();
		}

		runDropdown.setSelectedElement(runner.getRun().getPrimaryKey()
				.toString());
		runDropdown.setDisabled(true);

		return runDropdown;
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
			runner.setRunId(MRRegistration.MIDNIGHT_RUN_GROUP_ID);
			Year year = runner.getYear();
			String runnerYearString = year.getYearString();

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
					if (runner.getDateOfBirth() != null
							&& runner.getName() != null
							&& runner.getName().trim().length() > 0) {
						try {
							user = getUserBusiness(iwc).getUserHome()
									.findByDateOfBirthAndName(dob.getSQLDate(),
											runner.getName());
						} catch (Exception fe) {
							System.out
									.println("User not found by name and date_of_birth");
						}

					}
				}
			}

			runner.setUser(user);
		}

		if (runner.getRun() == null) {
			runner.setRunId(MRRegistration.MIDNIGHT_RUN_GROUP_ID);
		}

		if (iwc.isParameterSet(PARAMETER_DISTANCE)) {
			runner.setDistance(ConverterUtility.getInstance()
					.convertGroupToDistance(
							new Integer(iwc.getParameter(PARAMETER_DISTANCE))));
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

		if (iwc.isParameterSet(PARAMETER_AGREE)) {
			runner.setAgree(true);
		}

		if (personalID != null && !"".equals(personalID.trim())) {
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
		Map runnerMap = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		if (runnerMap == null) {
			runnerMap = new HashMap();
		}
		runnerMap.put(key, runner);

		if (iwc.getSessionAttribute(SESSION_ATTRIBUTE_ICELANDIC_PERSONAL_ID_RUNNERS) == null) {
			if (runner.getPersonalID() != null
					|| runner.getDateOfBirth() != null) {
				if (runner.getPersonalID() != null
						&& !"".equals(runner.getPersonalID().trim())) {
					iwc.setSessionAttribute(
							SESSION_ATTRIBUTE_ICELANDIC_PERSONAL_ID_RUNNERS,
							new Boolean(true));
				} else {
					iwc.setSessionAttribute(
							SESSION_ATTRIBUTE_ICELANDIC_PERSONAL_ID_RUNNERS,
							new Boolean(false));
				}
			}
		}

		iwc.setSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP, runnerMap);
	}

	private void removeRunner(IWContext iwc, String key) {
		Map runnerMap = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		if (runnerMap == null) {
			runnerMap = new HashMap();
		}
		runnerMap.remove(key);
		if (runnerMap.isEmpty()) {
			iwc.removeApplicationAttribute(SESSION_ATTRIBUTE_ICELANDIC_PERSONAL_ID_RUNNERS);
		}
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP, runnerMap);
	}

	private boolean isRunnerMapEmpty(IWContext iwc) {
		Map runnerMap = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);

		if (runnerMap == null || runnerMap.isEmpty()) {
			return true;
		}

		return false;
	}

	protected int parseAction(IWContext iwc) throws RemoteException {
		Runner runner = null;
		try {
			runner = getRunner();
		} catch (RuntimeException fe) {
			getParentPage().setAlertOnLoad(
					localize("mr_reg.user_not_found_for_personal_id",
							"No user found with personal ID."));
			initializeSteps(iwc);
			return ACTION_STEP_PERSONLOOKUP;
		}

		if (runner != null && runner.getUser() != null) {
			if (this.getRunBusiness(iwc).isRegisteredInRun(
					runner.getYear().getYearString(), runner.getRun(),
					runner.getUser())) {
				getParentPage().setAlertOnLoad(
						localize("mr_reg.already_registered",
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
				|| (runner != null && runner.getUser() != null
						&& runner.getUser().getDateOfBirth() != null && !isIcelandicPersonalID)) {
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

				if (maximumAgeForRun != -1
						&& year - dob.getYear() > maximumAgeForRun) {
					Object[] args = { String.valueOf(maximumAgeForRun) };
					getParentPage()
							.setAlertOnLoad(
									MessageFormat
											.format(localize(
													"mr_reg.invalid_date_of_birth_exeeding",
													"Invalid date of birth.  You have to be {0} or younger to register"),
													args));
					initializeSteps(iwc);
					return ACTION_STEP_PERSONLOOKUP;
				}

				int minimumAgeForRun = runner.getYear().getMinimumAgeForRun();
				if (minimumAgeForRun != -1
						&& year - dob.getYear() < minimumAgeForRun) {
					Object[] args = { String.valueOf(minimumAgeForRun) };
					getParentPage()
							.setAlertOnLoad(
									MessageFormat
											.format(localize(
													"mr_reg.invalid_date_of_birth",
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
								localize("mr_reg.email_dont_match",
										"Emails do not match. Please type the same email in both email inputs"));
				return ACTION_STEP_PERSONALDETAILS;
			}
		}

		int nextStep = super.parseAction(iwc);

		return nextStep;
	}

	private void loadPreviousStep(IWContext iwc) throws RemoteException {
		loadCurrentStep(iwc,
				Integer.parseInt(iwc.getParameter(PARAMETER_FROM_ACTION)));
	}

	/**
	 * Called by StepsBlock
	 */
	protected void initializeSteps(IWContext iwc) {
		boolean showPersonalLookupStep = true;
		if (!this.isIcelandicPersonalID) {
			if (!isRunnerMapEmpty(iwc)) {
				Boolean previousIcelandic = (Boolean) iwc
						.getSessionAttribute(SESSION_ATTRIBUTE_ICELANDIC_PERSONAL_ID_RUNNERS);
				if (previousIcelandic != null
						&& !previousIcelandic.booleanValue()) {
					showPersonalLookupStep = false;
				}
			}
		}

		if (showPersonalLookupStep) {
			addStep(iwc, ACTION_STEP_PERSONLOOKUP,
					localize("mr_reg.registration", "Registration"));
		}

		addStep(iwc, ACTION_STEP_PERSONALDETAILS,
				localize("mr_reg.registration", "Registration details"));

		addStep(iwc, ACTION_STEP_RUNDETAILS,
				localize("mr_reg.run_details", "Run details"));

		addStep(iwc, ACTION_STEP_DISCLAIMER,
				localize("mr_reg.disclaimer", "Disclaimer"));

		if (!isDisablePaymentAndOverviewSteps()) {
			addStep(iwc, ACTION_STEP_OVERVIEW,
					localize("mr_reg.overview", "Overview"));

			addStep(iwc, ACTION_STEP_PAYMENT,
					localize("mr_reg.payment", "Payment"));
		}

		addStep(iwc, ACTION_STEP_RECEIPT,
				localize("mr_reg.receipt", "Registration saved"));
	}

	public boolean isDisablePaymentAndOverviewSteps() {
		return disablePaymentAndOverviewSteps;
	}

	public void setDisablePaymentAndOverviewSteps(
			boolean disablePaymentAndOverviewSteps) {
		this.disablePaymentAndOverviewSteps = disablePaymentAndOverviewSteps;
	}
}
