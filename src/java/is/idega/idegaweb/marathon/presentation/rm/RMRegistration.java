package is.idega.idegaweb.marathon.presentation.rm;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.business.Runner;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.presentation.ActiveRunDropDownMenu;
import is.idega.idegaweb.marathon.presentation.DistanceDropDownMenu;
import is.idega.idegaweb.marathon.presentation.DistanceMenuShirtSizeMenuInputCollectionHandler;
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
import java.util.List;
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
import com.idega.presentation.remotescripting.RemoteScriptHandler;
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
import com.idega.util.ListUtil;
import com.idega.util.LocaleUtil;
import com.idega.util.text.SocialSecurityNumber;

/**
 * Registration class for the Reykjavik Marathon.
 * 
 * @author palli
 * 
 */
public class RMRegistration extends RunBlock {

	public static final String SESSION_ATTRIBUTE_RUNNER_MAP = "sa_runner_map";
	public static final String SESSION_ATTRIBUTE_ICELANDIC_PERSONAL_ID_RUNNERS = "sa_icelandic_runners";
	public static final String SESSION_ATTRIBUTE_PARTICIPANTS = Registration.SESSION_ATTRIBUTE_PARTICIPANTS;
	public static final String SESSION_ATTRIBUTE_AMOUNT = Registration.SESSION_ATTRIBUTE_AMOUNT;
	public static final String SESSION_ATTRIBUTE_CARD_NUMBER = Registration.SESSION_ATTRIBUTE_CARD_NUMBER;
	public static final String SESSION_ATTRIBUTE_PAYMENT_DATE = Registration.SESSION_ATTRIBUTE_PAYMENT_DATE;

	private static final String PROPERTY_CHILD_DISCOUNT_ISK = "child_discount_ISK";
	private static final String PROPERTY_CHILD_DISCOUNT_EUR = "child_discount_EUR";

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
	private static final String PARAMETER_SHIRT_SIZE = "prm_shirt_size";
	private static final String PARAMETER_SHIRT_SIZES_PER_RUN = "shirt_sizes_per_run";

	private static final String PARAMETER_RELAY_LEG = "prm_relay_leg";

	private static final String PARAMETER_RELAY_PARTNER_1_SSN = "prm_rel_prt_1_ssn";
	private static final String PARAMETER_RELAY_PARTNER_1_NAME = "prm_rel_prt_1_name";
	private static final String PARAMETER_RELAY_PARTNER_1_EMAIL = "prm_rel_prt_1_email";
	private static final String PARAMETER_RELAY_PARTNER_1_SHIRT_SIZE = "prm_rel_prt_1_shirt_size";
	private static final String PARAMETER_RELAY_PARTNER_1_LEG = "prm_rel_prt_1_leg";

	private static final String PARAMETER_RELAY_PARTNER_2_SSN = "prm_rel_prt_2_ssn";
	private static final String PARAMETER_RELAY_PARTNER_2_NAME = "prm_rel_prt_2_name";
	private static final String PARAMETER_RELAY_PARTNER_2_EMAIL = "prm_rel_prt_2_email";
	private static final String PARAMETER_RELAY_PARTNER_2_SHIRT_SIZE = "prm_rel_prt_2_shirt_size";
	private static final String PARAMETER_RELAY_PARTNER_2_LEG = "prm_rel_prt_2_leg";

	private static final String PARAMETER_RELAY_PARTNER_3_SSN = "prm_rel_prt_3_ssn";
	private static final String PARAMETER_RELAY_PARTNER_3_NAME = "prm_rel_prt_3_name";
	private static final String PARAMETER_RELAY_PARTNER_3_EMAIL = "prm_rel_prt_3_email";
	private static final String PARAMETER_RELAY_PARTNER_3_SHIRT_SIZE = "prm_rel_prt_3_shirt_size";
	private static final String PARAMETER_RELAY_PARTNER_3_LEG = "prm_rel_prt_3_leg";

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
	private static final int ACTION_STEP_RELAY = 40;
	private static final int ACTION_STEP_DISCLAIMER = 50;
	private static final int ACTION_STEP_OVERVIEW = 60;
	private static final int ACTION_STEP_PAYMENT = 70;
	private static final int ACTION_STEP_RECEIPT = 80;
	private static final int ACTION_CANCEL = 90;

	private static final double MILLISECONDS_IN_YEAR = 31557600000d;

	private static final String REYKJAVIK_MARATHON_GROUP_ID = "4";

	private boolean isIcelandicPersonalID = false;
	private Runner setRunner;
	private boolean showQuestionsError = false;
	private boolean showRelayError = false;
	private float childDiscount = 0;
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

			if (this.isIcelandicPersonalID) {
				this.childDiscount = Float.parseFloat(getBundle().getProperty(
						PROPERTY_CHILD_DISCOUNT_ISK, "400"));
			} else {
				this.childDiscount = Float.parseFloat(getBundle().getProperty(
						PROPERTY_CHILD_DISCOUNT_EUR, "4"));
			}

			loadCurrentStep(iwc, parseAction(iwc));
		}
	}

	private void loadCurrentStep(IWContext iwc, int action)
			throws RemoteException {

		/*
		 * if (action == ACTION_STEP_PERSONALDETAILS) { if
		 * (!iwc.isParameterSet(PARAMETER_NO_PERSONAL_ID)) { String personalID =
		 * iwc.getpara } }
		 */

		if (action == ACTION_STEP_DISCLAIMER) {
			Runner runner = getRunner();

			if (runner.getDistance().isRelayDistance()) {
				boolean leg1 = false;
				boolean leg2 = false;
				boolean leg3 = false;
				boolean leg4 = false;

				if (runner.getRelayLeg() == null
						|| "".equals(runner.getRelayLeg())) {
					action = ACTION_STEP_RELAY;
					showRelayError = true;
				} else {
					String leg = runner.getRelayLeg();

					if (leg.indexOf("1") > -1) {
						leg1 = true;
					}

					if (leg.indexOf("2") > -1) {
						leg2 = true;
					}

					if (leg.indexOf("3") > -1) {
						leg3 = true;
					}

					if (leg.indexOf("4") > -1) {
						leg4 = true;
					}

				}

				if (runner.getPartner1SSN() != null
						&& !"".equals(runner.getPartner1SSN())) {
					if (runner.getPartner1Name() == null
							|| "".equals(runner.getPartner1Name())) {
						action = ACTION_STEP_RELAY;
						showRelayError = true;
					}

					if (runner.getPartner1Email() == null
							|| "".equals(runner.getPartner1Email())) {
						action = ACTION_STEP_RELAY;
						showRelayError = true;
					}

					if (runner.getPartner1ShirtSize() == null
							|| "".equals(runner.getPartner1ShirtSize())
							|| "-1".equals(runner.getPartner1ShirtSize())) {
						action = ACTION_STEP_RELAY;
						showRelayError = true;
					}

					if (runner.getPartner1Leg() == null
							|| "".equals(runner.getPartner1Leg())) {
						action = ACTION_STEP_RELAY;
						showRelayError = true;
					} else {
						String leg = runner.getPartner1Leg();

						if (leg.indexOf("1") > -1) {
							if (leg1) {
								action = ACTION_STEP_RELAY;
								showRelayError = true;
							}

							leg1 = true;
						}

						if (leg.indexOf("2") > -1) {
							if (leg2) {
								action = ACTION_STEP_RELAY;
								showRelayError = true;
							}

							leg2 = true;
						}

						if (leg.indexOf("3") > -1) {
							if (leg3) {
								action = ACTION_STEP_RELAY;
								showRelayError = true;
							}

							leg3 = true;
						}

						if (leg.indexOf("4") > -1) {
							if (leg4) {
								action = ACTION_STEP_RELAY;
								showRelayError = true;
							}

							leg4 = true;
						}
					}

					if (runner.getPartner2SSN() != null
							&& !"".equals(runner.getPartner2SSN())) {
						if (runner.getPartner2Name() == null
								|| "".equals(runner.getPartner2Name())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;
						}

						if (runner.getPartner2Email() == null
								|| "".equals(runner.getPartner2Email())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;
						}

						if (runner.getPartner2ShirtSize() == null
								|| "".equals(runner.getPartner2ShirtSize())
								|| "-1".equals(runner.getPartner2ShirtSize())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;
						}

						if (runner.getPartner2Leg() == null
								|| "".equals(runner.getPartner2Leg())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;
						} else {
							String leg = runner.getPartner2Leg();

							if (leg.indexOf("1") > -1) {
								if (leg1) {
									action = ACTION_STEP_RELAY;
									showRelayError = true;
								}

								leg1 = true;
							}

							if (leg.indexOf("2") > -1) {
								if (leg2) {
									action = ACTION_STEP_RELAY;
									showRelayError = true;
								}

								leg2 = true;
							}

							if (leg.indexOf("3") > -1) {
								if (leg3) {
									action = ACTION_STEP_RELAY;
									showRelayError = true;
								}

								leg3 = true;
							}

							if (leg.indexOf("4") > -1) {
								if (leg4) {
									action = ACTION_STEP_RELAY;
									showRelayError = true;
								}

								leg4 = true;
							}
						}
					}

					if (runner.getPartner3SSN() != null
							&& !"".equals(runner.getPartner3SSN())) {
						if (runner.getPartner3Name() == null
								|| "".equals(runner.getPartner3Name())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;
						}

						if (runner.getPartner3Email() == null
								|| "".equals(runner.getPartner3Email())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;
						}

						if (runner.getPartner3ShirtSize() == null
								|| "".equals(runner.getPartner3ShirtSize())
								|| "-1".equals(runner.getPartner3ShirtSize())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;
						}

						if (runner.getPartner3Leg() == null
								|| "".equals(runner.getPartner3Leg())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;
						} else {
							String leg = runner.getPartner3Leg();

							if (leg.indexOf("1") > -1) {
								if (leg1) {
									action = ACTION_STEP_RELAY;
									showRelayError = true;
								}

								leg1 = true;
							}

							if (leg.indexOf("2") > -1) {
								if (leg2) {
									action = ACTION_STEP_RELAY;
									showRelayError = true;
								}

								leg2 = true;
							}

							if (leg.indexOf("3") > -1) {
								if (leg3) {
									action = ACTION_STEP_RELAY;
									showRelayError = true;
								}

								leg3 = true;
							}

							if (leg.indexOf("4") > -1) {
								if (leg4) {
									action = ACTION_STEP_RELAY;
									showRelayError = true;
								}

								leg4 = true;
							}
						}
					}
				}

				if (!leg1 || !leg2 || !leg3 || !leg4) {
					action = ACTION_STEP_RELAY;
					showRelayError = true;
				}
			}
		}

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
		case ACTION_STEP_RELAY:
			stepRelay(iwc);
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
				getInformationTable(localize("rm_reg.information_text_step_1",
						"Information text 1...")), 1, row++);
		table.setHeight(row++, 6);

		// table.setCellpadding(1, row, 24);
		table.add(
				getHeader(localize("rm_reg.personal_id", "Personal ID") + ":"),
				1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);

		TextInput input = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_PERSONAL_ID));
		input.setAsIcelandicSSNumber(localize("rm_reg.not_valid_personal_id",
				"The personal ID you have entered is not valid"));
		input.setLength(10);
		input.setMaxlength(10);
		input.setInFocusOnPageLoad(true);
		if (this.isIcelandicPersonalID) {
			input.setAsNotEmpty(localize("rm_reg.not_valid_personal_id",
					"The personal ID you have entered is not valid"));
		} else if (!allowNoPersonalID) {
			input.setAsNotEmpty(localize("rm_reg.not_valid_personal_id",
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
					"rm_reg.no_icelandic_ssn",
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
					localize("rm_reg.iceland_total",
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
				getInformationTable(localize("rm_reg.information_text_step_2",
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
			nameField.setAsAlphabeticText(localize("rm_reg.name_err_msg",
					"Your name may only contain alphabetic characters"));
			nameField.setAsNotEmpty(localize("rm_reg.name_not_empty",
					"Name field can not be empty"));
		}

		DropdownMenu genderField = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_GENDER));
		Collection genders = getGenderBusiness(iwc).getAllGenders();
		genderField.addMenuElement("-1",
				localize("rm_reg.select_gender", "Select gender..."));
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
			genderField.setAsNotEmpty(localize("rm_reg.gender_not_empty",
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
		emailField.setAsEmail(localize("rm_reg.email_err_msg",
				"Not a valid email address"));
		emailField.setAsNotEmpty(localize("rm_reg.continue_without_email",
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
				localize("rm_reg.select_nationality", "Select nationality..."));
		countryField.addMenuElement("-1",
				localize("rm_reg.select_country", "Select country..."));
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
				"rm_reg.must_select_nationality",
				"You must select your nationality"));
		if (!this.isIcelandicPersonalID) {
			countryField.setAsNotEmpty(localize("rm_reg.must_select_country",
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
			addressField.setAsNotEmpty(localize("rm_reg.must_provide_address",
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
		emailField2.setAsEmail(localize("rm_reg.email_err_msg",
				"Not a valid email address"));
		emailField2.setAsNotEmpty(localize("rm_reg.continue_without_email2",
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
			cityField.setAsNotEmpty(localize("rm_reg.must_provide_city",
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
			postalField.setAsNotEmpty(localize("rm_reg.must_provide_postal",
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
				getInformationTable(localize("rm_reg.information_text_step_5",
						"Information text 5...")), 1, row++);
		table.setHeight(row++, 18);

		Map runners = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		Table runnerTable = new Table();
		// runnerTable.setColumns(columns)
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		runnerTable.add(
				getHeader(localize("rm_reg.runner_name", "Runner name")), 1, 1);
		runnerTable.add(getHeader(localize("rm_reg.run", "Run")), 2, 1);
		runnerTable.add(getHeader(localize("rm_reg.distance", "Distance")), 3,
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
								getHeader(localize("rm_reg.relay_leg", "Leg")),
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
				"rm_reg.finish_registration", "Register")));
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
				getInformationTable(localize("rm_reg.information_text_step_20",
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
		distanceDropdown.setAsNotEmpty(localize("rm_reg.must_select_distance",
				"You have to select a distance"));

		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_SECONDARY_DD,
						"Distance")), 1, iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(distanceDropdown, 3, iRow++);

		choiceTable.setHeight(iRow++, 5);

		DropdownMenu tShirtField = null;
		tShirtField = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_SHIRT_SIZE));
		tShirtField
				.addMenuElement(
						"-1",
						localize("rm_reg.select_tee_shirt_size",
								"Select shirt size..."));
		if (getRunner().getDistance() != null) {
			String shirtSizeMetadata = getRunner().getDistance().getMetaData(
					PARAMETER_SHIRT_SIZES_PER_RUN);
			List shirtSizes = null;
			if (shirtSizeMetadata != null) {
				shirtSizes = ListUtil
						.convertCommaSeparatedStringToList(shirtSizeMetadata);
			}
			if (shirtSizes != null) {
				Iterator shirtIt = shirtSizes.iterator();
				while (shirtIt.hasNext()) {
					String shirtSizeKey = (String) shirtIt.next();
					tShirtField
							.addMenuElement(
									shirtSizeKey,
									localize("shirt_size." + shirtSizeKey,
											shirtSizeKey));
				}
			}
			if (getRunner().getDistance() != null) {
				tShirtField.setSelectedElement(getRunner().getShirtSize());
			}
		}
		tShirtField.setAsNotEmpty(localize("rm_reg.must_select_shirt_size",
				"You must select shirt size"));

		RemoteScriptHandler rshShirts = new RemoteScriptHandler(
				distanceDropdown, tShirtField);
		try {
			rshShirts
					.setRemoteScriptCollectionClass(DistanceMenuShirtSizeMenuInputCollectionHandler.class);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		add(rshShirts);

		choiceTable
				.add(getHeader(localize(IWMarathonConstants.RR_TSHIRT,
						"Shirt size")), 1, iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(tShirtField, 3, iRow++);

		choiceTable.setHeight(iRow++, 10);

		UIComponent buttonsContainer = getButtonsFooter(iwc, true, true);
		form.add(buttonsContainer);

		add(form);
	}

	private void stepRelay(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_DATE_OF_BIRTH);
		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_RELAY);

		form.add(getStepsHeader(iwc, ACTION_STEP_RELAY));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.setHeight(row++, 12);

		table.add(
				getInformationTable(localize(
						"rm_reg.information_text_step_relay",
						"Enter information about your relay partners.")), 1,
				row++);
		table.setHeight(row++, 18);

		if (showRelayError) {
			Text errorText = getHeader(localize("relay_error",
					"You have to fill in all fields for each relay partner."));
			errorText.setFontColor("#ff0000");
			table.add(errorText, 1, row++);
			table.setHeight(row++, 18);
			showRelayError = false;
		}

		TextInput relayLeg = new TextInput(PARAMETER_RELAY_LEG);
		if (getRunner().getRelayLeg() != null) {
			relayLeg.setValue(getRunner().getRelayLeg());
		}

		TextInput relPart1SSN = new TextInput(PARAMETER_RELAY_PARTNER_1_SSN);
		if (getRunner().getPartner1SSN() != null) {
			relPart1SSN.setValue(getRunner().getPartner1SSN());
		}
		TextInput relPart1Name = new TextInput(PARAMETER_RELAY_PARTNER_1_NAME);
		if (getRunner().getPartner1Name() != null) {
			relPart1Name.setValue(getRunner().getPartner1Name());
		}
		TextInput relPart1Email = new TextInput(PARAMETER_RELAY_PARTNER_1_EMAIL);
		if (getRunner().getPartner1Email() != null) {
			relPart1Email.setValue(getRunner().getPartner1Email());
		}
		DropdownMenu relPart1ShirtSize = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_RELAY_PARTNER_1_SHIRT_SIZE));
		TextInput relPart1Leg = new TextInput(PARAMETER_RELAY_PARTNER_1_LEG);
		if (getRunner().getPartner1Leg() != null) {
			relPart1Leg.setValue(getRunner().getPartner1Leg());
		}

		TextInput relPart2SSN = new TextInput(PARAMETER_RELAY_PARTNER_2_SSN);
		if (getRunner().getPartner2SSN() != null) {
			relPart2SSN.setValue(getRunner().getPartner2SSN());
		}
		TextInput relPart2Name = new TextInput(PARAMETER_RELAY_PARTNER_2_NAME);
		if (getRunner().getPartner2Name() != null) {
			relPart2Name.setValue(getRunner().getPartner2Name());
		}
		TextInput relPart2Email = new TextInput(PARAMETER_RELAY_PARTNER_2_EMAIL);
		if (getRunner().getPartner2Email() != null) {
			relPart2Email.setValue(getRunner().getPartner2Email());
		}
		DropdownMenu relPart2ShirtSize = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_RELAY_PARTNER_2_SHIRT_SIZE));
		TextInput relPart2Leg = new TextInput(PARAMETER_RELAY_PARTNER_2_LEG);
		if (getRunner().getPartner2Leg() != null) {
			relPart2Leg.setValue(getRunner().getPartner2Leg());
		}

		TextInput relPart3SSN = new TextInput(PARAMETER_RELAY_PARTNER_3_SSN);
		if (getRunner().getPartner3SSN() != null) {
			relPart3SSN.setValue(getRunner().getPartner3SSN());
		}
		TextInput relPart3Name = new TextInput(PARAMETER_RELAY_PARTNER_3_NAME);
		if (getRunner().getPartner3Name() != null) {
			relPart3Name.setValue(getRunner().getPartner3Name());
		}
		TextInput relPart3Email = new TextInput(PARAMETER_RELAY_PARTNER_3_EMAIL);
		if (getRunner().getPartner3Email() != null) {
			relPart3Email.setValue(getRunner().getPartner3Email());
		}
		DropdownMenu relPart3ShirtSize = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_RELAY_PARTNER_3_SHIRT_SIZE));
		TextInput relPart3Leg = new TextInput(PARAMETER_RELAY_PARTNER_3_LEG);
		if (getRunner().getPartner3Leg() != null) {
			relPart3Leg.setValue(getRunner().getPartner3Leg());
		}

		relPart1ShirtSize
				.addMenuElement(
						"-1",
						localize("rm_reg.select_tee_shirt_size",
								"Select shirt size..."));
		relPart2ShirtSize
				.addMenuElement(
						"-1",
						localize("rm_reg.select_tee_shirt_size",
								"Select shirt size..."));
		relPart3ShirtSize
				.addMenuElement(
						"-1",
						localize("rm_reg.select_tee_shirt_size",
								"Select shirt size..."));
		if (getRunner().getDistance() != null) {
			String shirtSizeMetadata = getRunner().getDistance().getMetaData(
					PARAMETER_SHIRT_SIZES_PER_RUN);
			List shirtSizes = null;
			if (shirtSizeMetadata != null) {
				shirtSizes = ListUtil
						.convertCommaSeparatedStringToList(shirtSizeMetadata);
			}
			if (shirtSizes != null) {
				Iterator shirtIt = shirtSizes.iterator();
				while (shirtIt.hasNext()) {
					String shirtSizeKey = (String) shirtIt.next();
					relPart1ShirtSize
							.addMenuElement(
									shirtSizeKey,
									localize("shirt_size." + shirtSizeKey,
											shirtSizeKey));
					relPart2ShirtSize
							.addMenuElement(
									shirtSizeKey,
									localize("shirt_size." + shirtSizeKey,
											shirtSizeKey));
					relPart3ShirtSize
							.addMenuElement(
									shirtSizeKey,
									localize("shirt_size." + shirtSizeKey,
											shirtSizeKey));
				}
			}
			if (getRunner().getPartner1ShirtSize() != null) {
				relPart1ShirtSize.setSelectedElement(getRunner()
						.getPartner1ShirtSize());
			}
			if (getRunner().getPartner2ShirtSize() != null) {
				relPart2ShirtSize.setSelectedElement(getRunner()
						.getPartner2ShirtSize());
			}
			if (getRunner().getPartner3ShirtSize() != null) {
				relPart3ShirtSize.setSelectedElement(getRunner()
						.getPartner3ShirtSize());
			}
		}

		Table choiceTable = new Table();
		choiceTable.setColumns(6);
		choiceTable.setCellpadding(2);
		choiceTable.setCellspacing(0);
		choiceTable.setWidth(1, "10%");
		choiceTable.setWidth(2, "18%");
		choiceTable.setWidth(3, "18%");
		choiceTable.setWidth(4, "18%");
		choiceTable.setWidth(5, "18%");
		choiceTable.setWidth(6, "18%");
		choiceTable.setWidth(Table.HUNDRED_PERCENT);
		table.add(choiceTable, 1, row++);

		int choiceRow = 1;

		choiceTable.add("#", 1, choiceRow);
		choiceTable.add(localize("rm_reg.relay_ssn", "SSN"), 2, choiceRow);
		choiceTable.add(localize("rm_reg.relay_name", "Name"), 3, choiceRow);
		choiceTable.add(localize("rm_reg.relay_email", "E-mail"), 4, choiceRow);
		choiceTable.add(localize("rm_reg.relay_shirt_size", "Shirt size"), 5,
				choiceRow);
		choiceTable.add(localize("rm_reg.relay_leg", "Leg"), 6, choiceRow++);

		choiceTable.add("1", 1, choiceRow);
		choiceTable.add(getRunner().getPersonalID(), 2, choiceRow);
		if (this.isIcelandicPersonalID) {
			if (getRunner().getUser() != null) {
				choiceTable.add(getRunner().getUser().getName(), 3, choiceRow);
			} else {
				choiceTable.add(getRunner().getName(), 3, choiceRow);
			}
		} else {
			choiceTable.add(getRunner().getName(), 3, choiceRow);
		}

		choiceTable.add(getRunner().getEmail(), 4, choiceRow);
		choiceTable.add(
				localize("shirt_size." + getRunner().getShirtSize(),
						getRunner().getShirtSize()), 5, choiceRow);
		choiceTable.add(relayLeg, 6, choiceRow++);

		choiceTable.add("2", 1, choiceRow);
		choiceTable.add(relPart1SSN, 2, choiceRow);
		choiceTable.add(relPart1Name, 3, choiceRow);
		choiceTable.add(relPart1Email, 4, choiceRow);
		choiceTable.add(relPart1ShirtSize, 5, choiceRow);
		choiceTable.add(relPart1Leg, 6, choiceRow++);

		choiceTable.add("3", 1, choiceRow);
		choiceTable.add(relPart2SSN, 2, choiceRow);
		choiceTable.add(relPart2Name, 3, choiceRow);
		choiceTable.add(relPart2Email, 4, choiceRow);
		choiceTable.add(relPart2ShirtSize, 5, choiceRow);
		choiceTable.add(relPart2Leg, 6, choiceRow++);

		choiceTable.add("4", 1, choiceRow);
		choiceTable.add(relPart3SSN, 2, choiceRow);
		choiceTable.add(relPart3Name, 3, choiceRow);
		choiceTable.add(relPart3Email, 4, choiceRow);
		choiceTable.add(relPart3ShirtSize, 5, choiceRow);
		choiceTable.add(relPart3Leg, 6, choiceRow);

		form.add(getButtonsFooter(iwc));

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
				getInformationTable(localize("rm_reg.information_text_step_4",
						"Information text 4...")), 1, row++);

		Layer disclaimerLayer = new Layer(Layer.DIV);
		CheckBox agreeCheck = getCheckBox(PARAMETER_AGREE,
				Boolean.TRUE.toString());
		agreeCheck.setToEnableWhenChecked(next);
		agreeCheck.setToDisableWhenUnchecked(next);
		agreeCheck.setChecked(getRunner().isAgree());

		Label disclaimerLabel = new Label(localize("rm_reg.agree_terms",
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
				getInformationTable(localize("rm_reg.information_text_step_6",
						"Information text 6...")), 1, row++);
		table.setHeight(row++, 18);

		Map runners = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		Table runnerTable = new Table();
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		runnerTable.setCellspacing(0);
		runnerTable.add(
				getHeader(localize("rm_reg.runner_name", "Runner name")), 1, 1);
		runnerTable.add(getHeader(localize("rm_reg.run", "Run")), 2, 1);
		runnerTable.add(getHeader(localize("rm_reg.distance", "Distance")), 3,
				1);
		runnerTable.add(getHeader(localize("rm_reg.price", "Price")), 4, 1);
		table.add(runnerTable, 1, row++);
		table.setHeight(row++, 18);
		int runRow = 2;

		int numberOfChildren = getRunBusiness(iwc).getNumberOfChildren(
				runners.values());
		boolean useChildDiscount = false;
		if (numberOfChildren > 1 && numberOfChildren != runners.size()) {
			useChildDiscount = true;
		}
		int childNumber = 0;
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

			if (isChild(runner) && runner.getDistance().isFamilyDiscount()) {
				childNumber++;
				if (useChildDiscount && childNumber > 1 && runPrice > 0) {
					runPrice -= this.childDiscount;
				}
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

		// if (this.isIcelandicPersonalID) {
		if (useChildDiscount) {
			float childrenDiscount = -((numberOfChildren - 1) * this.childDiscount);
			totalAmount += childrenDiscount;

			runnerTable.setHeight(runRow++, 12);
			runnerTable.add(
					getText(localize("rm_reg.family_discount",
							"Family discount")), 1, runRow);
			runnerTable.add(
					getText(formatAmount(iwc.getCurrentLocale(),
							childrenDiscount)), 4, runRow++);
		}
		// }

		runnerTable.setHeight(runRow++, 12);
		runnerTable.add(
				getHeader(localize("rm_reg.total_amount", "Total amount")), 1,
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
				getHeader(localize("rm_reg.credit_card_information",
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
		nameField.setAsNotEmpty(localize("rm_reg.must_supply_card_holder_name",
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
		ccv.setAsIntegers(localize("rm_reg.not_valid_ccv",
				"Not a valid CCV number"));
		ccv.setAsNotEmpty(localize("rm_reg.must_supply_ccv",
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
				getHeader(localize("rm_reg.card_holder", "Card holder")), 1,
				creditRow);
		creditCardTable.add(
				getHeader(localize("rm_reg.card_number", "Card number")), 3,
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
					localize("rm_reg.not_valid_card_number",
							"Not a valid card number"));
			cardNumber.setAsIntegers(localize("rm_reg.not_valid_card_number",
					"Not a valid card number"));
			cardNumber.setAsNotEmpty(localize("rm_reg.must_supply_card_number",
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
				getHeader(localize("rm_reg.card_expires", "Card expires")), 1,
				creditRow);
		creditCardTable.add(
				getHeader(localize("rm_reg.ccv_number", "CCV number")), 3,
				creditRow++);
		creditCardTable.add(month, 1, creditRow);
		creditCardTable.add(getText("/"), 1, creditRow);
		creditCardTable.add(year, 1, creditRow);
		creditCardTable.add(ccv, 3, creditRow++);

		TextInput emailField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_CARD_HOLDER_EMAIL));
		emailField.setAsEmail(localize("rm_reg.email_err_msg",
				"Not a valid email address"));
		emailField.setLength(30);
		emailField.keepStatusOnAction(true);
		emailField.setContent(getRunner().getEmail());

		creditCardTable.setHeight(creditRow++, 3);
		creditCardTable.mergeCells(3, creditRow, 3, creditRow + 1);
		creditCardTable
				.add(getText(localize(
						"rm_reg.ccv_explanation_text",
						"A CCV number is a three digit number located on the back of all major credit cards.")),
						3, creditRow);
		creditCardTable.add(
				getHeader(localize("rm_reg.card_holder_email",
						"Cardholder email")), 1, creditRow++);
		creditCardTable.add(emailField, 1, creditRow++);
		creditCardTable.add(new HiddenInput(PARAMETER_AMOUNT, String
				.valueOf(totalAmount)));
		creditCardTable.setHeight(creditRow++, 18);
		creditCardTable.mergeCells(1, creditRow, creditCardTable.getColumns(),
				creditRow);
		creditCardTable.add(
				getText(localize("rm_reg.read_conditions",
						"Please read before you finish your payment")), 1,
				creditRow);

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize(
				"rm_reg.pay", "Pay fee")));
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
				.add(getHeader(localize("rm_reg.agree_terms_and_conditions",
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
										"rm_reg.session_has_expired_payment",
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
					"rm_reg.");

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
							localize("rm_reg.save_failed",
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

		String greeting = localize("rm_reg.hello_participant",
				"Dear participant");
		if (selectedRun != null) {
			if (this.isIcelandicPersonalID) {
				greeting = selectedRun.getRunRegistrationReceiptGreeting();
			} else {
				greeting = selectedRun
						.getRunRegistrationReceiptGreetingEnglish();
			}
		}

		table.add(getHeader(greeting), 1, row++);
		table.setHeight(row++, 16);

		table.add(
				getText(localize("rm_reg.payment_received",
						"You have registered for the following:")), 1, row++);
		table.setHeight(row++, 8);

		Table runnerTable = new Table(5, runners.size() + 4);
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		int col = 1;
		runnerTable.add(
				getHeader(localize("rm_reg.runner_name", "Runner name")),
				col++, 1);
		runnerTable.add(getHeader(localize("rm_reg.run", "Run")), col++, 1);
		runnerTable.add(getHeader(localize("rm_reg.distance", "Distance")),
				col++, 1);
		runnerTable.add(
				getHeader(localize("rm_reg.race_number", "Race number")),
				col++, 1);
		runnerTable.add(getHeader(localize("rm_reg.shirt_size", "Shirt size")),
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
			runnerTable.add(
					getText(localize(
							"shirt_size." + participant.getShirtSize(),
							participant.getShirtSize())), col++, runRow++);

			if (participant.getRelayPartner1SSN() != null
					&& !"".equals(participant.getRelayPartner1SSN())) {
				col = 1;
				runnerTable.add(getText(participant.getRelayPartner1Name()),
						col++, runRow);
				runnerTable.add(getText(localize(run.getName(), run.getName())
						+ " " + participant.getRunYearGroup().getName()),
						col++, runRow);
				runnerTable
						.add(getText(localize(distance.getName(),
								distance.getName())), col++, runRow);
				runnerTable.add(getText(String.valueOf(participant
						.getParticipantNumber())), col++, runRow);
				runnerTable.add(
						getText(localize(
								"shirt_size."
										+ participant
												.getRelayPartner1ShirtSize(),
								participant.getRelayPartner1ShirtSize())),
						col++, runRow++);

				if (participant.getRelayPartner2SSN() != null
						&& !"".equals(participant.getRelayPartner2SSN())) {
					col = 1;
					runnerTable.add(
							getText(participant.getRelayPartner2Name()), col++,
							runRow);
					runnerTable.add(
							getText(localize(run.getName(), run.getName())
									+ " "
									+ participant.getRunYearGroup().getName()),
							col++, runRow);
					runnerTable.add(
							getText(localize(distance.getName(),
									distance.getName())), col++, runRow);
					runnerTable.add(getText(String.valueOf(participant
							.getParticipantNumber())), col++, runRow);
					runnerTable
							.add(getText(localize(
									"shirt_size."
											+ participant
													.getRelayPartner2ShirtSize(),
									participant.getRelayPartner2ShirtSize())),
									col++, runRow++);

					if (participant.getRelayPartner3SSN() != null
							&& !"".equals(participant.getRelayPartner3SSN())) {
						col = 1;
						runnerTable.add(
								getText(participant.getRelayPartner3Name()),
								col++, runRow);
						runnerTable.add(
								getText(localize(run.getName(), run.getName())
										+ " "
										+ participant.getRunYearGroup()
												.getName()), col++, runRow);
						runnerTable.add(
								getText(localize(distance.getName(),
										distance.getName())), col++, runRow);
						runnerTable.add(getText(String.valueOf(participant
								.getParticipantNumber())), col++, runRow);
						runnerTable
								.add(getText(localize(
										"shirt_size."
												+ participant
														.getRelayPartner3ShirtSize(),
										participant.getRelayPartner3ShirtSize())),
										col++, runRow++);
					}
				}
			}
		}

		if (doPayment) {
			Table creditCardTable = new Table(2, 3);
			creditCardTable.add(
					getHeader(localize("rm_reg.payment_received_timestamp",
							"Payment received") + ":"), 1, 1);
			creditCardTable.add(getText(paymentStamp.getLocaleDateAndTime(
					iwc.getCurrentLocale(), IWTimestamp.SHORT,
					IWTimestamp.SHORT)), 2, 1);
			creditCardTable.add(
					getHeader(localize("rm_reg.card_number", "Card number")
							+ ":"), 1, 2);
			creditCardTable.add(getText(cardNumber), 2, 2);
			creditCardTable.add(getHeader(localize("rm_reg.amount", "Amount")
					+ ":"), 1, 3);
			creditCardTable
					.add(getText(formatAmount(iwc.getCurrentLocale(),
							(float) amount)), 2, 3);
			table.setHeight(row++, 16);
			table.add(creditCardTable, 1, row++);
		}

		table.setHeight(row++, 16);
		table.add(
				getHeader(localize("rm_reg.receipt_info_headline",
						"Receipt - Please print it out")), 1, row++);
		table.add(
				getText(localize(
						"rm_reg.receipt_info_headline_body",
						"This document is your receipt, please print it out and bring it with you when you collect your race material.")),
				1, row++);

		if (selectedRun != null) {
			table.setHeight(row++, 16);
			table.add(
					getHeader(localize(
							"rm_reg.delivery_of_race_material_headline",
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
		table.add(getText(localize("rm_reg.best_regards", "Best regards,")), 1,
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

		runDropdown.setAsNotEmpty(localize("rm_reg.must_select_run",
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
			runner.setRunId(RMRegistration.REYKJAVIK_MARATHON_GROUP_ID);
			Year year = runner.getYear();
			String runnerYearString = year.getYearString();

			/*
			 * try { Collection distancesGroups = getRunBusiness(iwc)
			 * .getDistancesMap(runner.getRun(), runnerYearString); if
			 * (distancesGroups != null) { Iterator it =
			 * distancesGroups.iterator(); if (it.hasNext()) {
			 * runner.setDistance(ConverterUtility.getInstance()
			 * .convertGroupToDistance((Group) it.next())); } } } catch
			 * (RemoteException e) { e.printStackTrace(); }
			 */

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
			runner.setRunId(RMRegistration.REYKJAVIK_MARATHON_GROUP_ID);
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
		if (iwc.isParameterSet(PARAMETER_SHIRT_SIZE)) {
			runner.setShirtSize(iwc.getParameter(PARAMETER_SHIRT_SIZE));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_LEG)) {
			runner.setRelayLeg(iwc.getParameter(PARAMETER_RELAY_LEG));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_1_SSN)) {
			runner.setPartner1SSN(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_1_SSN));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_1_NAME)) {
			runner.setPartner1Name(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_1_NAME));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_1_EMAIL)) {
			runner.setPartner1Email(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_1_EMAIL));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_1_SHIRT_SIZE)) {
			runner.setPartner1ShirtSize(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_1_SHIRT_SIZE));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_1_LEG)) {
			runner.setPartner1Leg(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_1_LEG));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_2_SSN)) {
			runner.setPartner2SSN(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_2_SSN));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_2_NAME)) {
			runner.setPartner2Name(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_2_NAME));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_2_EMAIL)) {
			runner.setPartner2Email(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_2_EMAIL));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_2_SHIRT_SIZE)) {
			runner.setPartner2ShirtSize(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_2_SHIRT_SIZE));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_2_LEG)) {
			runner.setPartner2Leg(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_2_LEG));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_3_SSN)) {
			runner.setPartner3SSN(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_3_SSN));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_3_NAME)) {
			runner.setPartner3Name(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_3_NAME));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_3_EMAIL)) {
			runner.setPartner3Email(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_3_EMAIL));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_3_SHIRT_SIZE)) {
			runner.setPartner3ShirtSize(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_3_SHIRT_SIZE));
		}

		if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_3_LEG)) {
			runner.setPartner3Leg(iwc
					.getParameter(PARAMETER_RELAY_PARTNER_3_LEG));
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
					localize("rm_reg.user_not_found_for_personal_id",
							"No user found with personal ID."));
			initializeSteps(iwc);
			return ACTION_STEP_PERSONLOOKUP;
		}

		if (runner != null && runner.getUser() != null) {
			if (this.getRunBusiness(iwc).isRegisteredInRun(
					runner.getYear().getYearString(), runner.getRun(),
					runner.getUser())) {
				getParentPage().setAlertOnLoad(
						localize("rm_reg.already_registered",
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
													"rm_reg.invalid_date_of_birth_exeeding",
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
													"rm_reg.invalid_date_of_birth",
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
								localize("rm_reg.email_dont_match",
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
					localize("rm_reg.registration", "Registration"));
		}

		addStep(iwc, ACTION_STEP_PERSONALDETAILS,
				localize("rm_reg.registration", "Registration details"));

		addStep(iwc, ACTION_STEP_RUNDETAILS,
				localize("rm_reg.run_details", "Run details"));
		Runner runner = null;
		try {
			runner = getRunner();
		} catch (RuntimeException e) {
		}

		Distance dist = null;
		if (runner != null) {
			dist = runner.getDistance();
			if (dist != null) {
				if (dist != null) {
					if (dist.isRelayDistance()) {
						addStep(iwc,
								ACTION_STEP_RELAY,
								localize("rm_reg.relay_setup",
										"Setup relay team"));
					}
				}
			}
		}

		addStep(iwc, ACTION_STEP_DISCLAIMER,
				localize("rm_reg.disclaimer", "Disclaimer"));

		if (!isDisablePaymentAndOverviewSteps()) {
			addStep(iwc, ACTION_STEP_OVERVIEW,
					localize("rm_reg.overview", "Overview"));

			addStep(iwc, ACTION_STEP_PAYMENT,
					localize("rm_reg.receipt", "Registration saved"));
		}
		
		addStep(iwc, ACTION_STEP_RECEIPT,
				localize("rm_reg.receipt", "Registration saved"));
	}

	public boolean isDisablePaymentAndOverviewSteps() {
		return disablePaymentAndOverviewSteps;
	}

	public void setDisablePaymentAndOverviewSteps(
			boolean disablePaymentAndOverviewSteps) {
		this.disablePaymentAndOverviewSteps = disablePaymentAndOverviewSteps;
	}
}