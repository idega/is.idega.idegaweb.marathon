/*
 * $Id: Registration.java,v 1.134 2009/07/13 10:26:27 palli Exp $
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
import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.CharityHome;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.data.RunCategory;
import is.idega.idegaweb.marathon.data.RunCategoryHome;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.ejb.FinderException;
import javax.faces.component.UIComponent;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.Script;
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
 * Last modified: $Date: 2009/07/13 10:26:27 $ by $Author: palli $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.134 $
 */
public class Registration extends RunBlock {

	public static final String SESSION_ATTRIBUTE_RUNNER_MAP = "sa_runner_map";
	public static final String SESSION_ATTRIBUTE_PARTICIPANTS = "sa_participants";
	public static final String SESSION_ATTRIBUTE_AMOUNT = "sa_amount";
	public static final String SESSION_ATTRIBUTE_CARD_NUMBER = "sa_card_number";
	public static final String SESSION_ATTRIBUTE_PAYMENT_DATE = "sa_payment_date";

	private static final String PROPERTY_CHIP_PRICE_ISK = "chip_price_ISK";
	private static final String PROPERTY_CHIP_PRICE_EUR = "chip_price_EUR";
	private static final String PROPERTY_CHIP_DISCOUNT_ISK = "chip_discount_ISK";
	private static final String PROPERTY_CHIP_DISCOUNT_EUR = "chip_discount_EUR";
	private static final String PROPERTY_CHILD_DISCOUNT_ISK = "child_discount_ISK";

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
	private static final String PARAMETER_EMAIL2 = "prm_email2";
	private static final String PARAMETER_HOME_PHONE = "prm_home_phone";
	private static final String PARAMETER_MOBILE_PHONE = "prm_mobile_phone";
	private static final String PARAMETER_SHIRT_SIZE = "prm_shirt_size";
	private static final String PARAMETER_CHIP = "prm_chip";
	private static final String PARAMETER_CHIP_NUMBER = "prm_chip_number";
	// private static final String PARAMETER_TRANSPORT = "prm_transport";
	private static final String PARAMETER_AGREE = "prm_agree";
	private static final String PARAMETER_QUESTION1_HOUR = "prm_q1_hour";
	private static final String PARAMETER_QUESTION1_MINUTE = "prm_q1_minute";
	private static final String PARAMETER_QUESTION1_YEAR = "prm_q1_year";
	private static final String PARAMETER_QUESTION1_NEVER = "prm_q1_never";
	private static final String PARAMETER_QUESTION2_HOUR = "prm_q2_hour";
	private static final String PARAMETER_QUESTION2_MINUTE = "prm_q2_minute";
	private static final String PARAMETER_QUESTION3_HOUR = "prm_q3_hour";
	private static final String PARAMETER_QUESTION3_MINUTE = "prm_q3_minute";
	private static final String PARAMETER_QUESTION3_YEAR = "prm_q3_year";
	private static final String PARAMETER_QUESTION3_NEVER = "prm_q3_never";

	private static final String PARAMETER_NAME_ON_CARD = "prm_name_on_card";
	private static final String PARAMETER_CARD_NUMBER = "prm_card_number";
	private static final String PARAMETER_EXPIRES_MONTH = "prm_expires_month";
	private static final String PARAMETER_EXPIRES_YEAR = "prm_expires_year";
	private static final String PARAMETER_CCV = "prm_ccv";
	private static final String PARAMETER_AMOUNT = "prm_amount";
	private static final String PARAMETER_CARD_HOLDER_EMAIL = "prm_card_holder_email";
	private static final String PARAMETER_REFERENCE_NUMBER = "prm_reference_number";
	private static final String PARAMETER_SHIRT_SIZES_PER_RUN = "shirt_sizes_per_run";
	private static final String PARAMETER_CHARITY_ID = "prm_charity_id";
	private static final String PARAMETER_ACCEPT_CHARITY = "prm_accept_charity";
	private static final String PARAMETER_NOT_ACCEPT_CHARITY = "prm_not_accept_charity";
	private static final String PARAMETER_ALLOW_CONTACT = "prm_allow_contact";
	private static final String PARAMETER_ALLOW_EMAILS = "prm_allow_emails";
	private static final String PARAMETER_CATEGORY_ID = "prm_category_id";
	private static final String PARAMETER_APPLY_DOMESTIC_TRAVEL_SUPPORT = "prm_apply_domestic_travel_support";
	private static final String PARAMETER_APPLY_INTERNATIONAL_TRAVEL_SUPPORT = "prm_apply_international_travel_support";
	
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

	private static final String PARAMETER_LIMIT_RUN_IDS = "run_ids";

	private static final int ACTION_STEP_PERSONLOOKUP = 10;
	private static final int ACTION_STEP_PERSONALDETAILS = 20;
	private static final int ACTION_STEP_RUNDETAILS = 21;
	private static final int ACTION_STEP_CHIP = 30;
	// private static final int ACTION_STEP_TRANSPORT = 31;
	private static final int ACTION_STEP_QUESTIONS = 31;
	private static final int ACTION_STEP_CHARITY = 32;
	private static final int ACTION_STEP_TRAVELSUPPORT = 33;
	private static final int ACTION_STEP_RELAY = 29;
	private static final int ACTION_STEP_DISCLAIMER = 40;
	private static final int ACTION_STEP_OVERVIEW = 50;
	private static final int ACTION_STEP_PAYMENT = 60;
	private static final int ACTION_STEP_RECEIPT = 70;
	private static final int ACTION_CANCEL = 80;

	private static final double MILLISECONDS_IN_YEAR = 31557600000d;

	private boolean isIcelandic = false;
	private float chipPrice;
	private float chipDiscount;
	private float childDiscount = 0;
	private Runner setRunner;
	private boolean disablePaymentAndOverviewSteps = false;
	private String runIds;
	private String constrainedToOneRun;
	private String presetCountries;
	private boolean charityStepEnabledForForeignLocale = false;
	private boolean hideCharityCheckbox = false;
	private boolean disableSponsorContactCheck = false;
	private boolean showCategories = false;
	private boolean disableChipBuy = false;
	private boolean enableTravelSupport = false;
	private boolean sponsoredRegistration = false;
	private boolean hideShirtSize = false;
	private boolean hidePrintviewLink = false;
	private boolean hideRaceNumberColumn = false;
	private boolean showAllThisYear = false;
	private boolean showQuestionsError = false;
	private boolean showRelayError = false;

	public void main(IWContext iwc) throws Exception {
		if (!iwc.isInEditMode()) {
			this.isIcelandic = iwc.getCurrentLocale().equals(
					LocaleUtil.getIcelandicLocale());
			if (this.isIcelandic) {
				this.chipPrice = Float.parseFloat(getBundle().getProperty(
						PROPERTY_CHIP_PRICE_ISK, "2700"));
				this.chipDiscount = Float.parseFloat(getBundle().getProperty(
						PROPERTY_CHIP_DISCOUNT_ISK, "300"));
				this.childDiscount = Float.parseFloat(getBundle()
						.getProperty(PROPERTY_CHILD_DISCOUNT_ISK, "600"));
			} else {
				this.chipPrice = Float.parseFloat(getBundle().getProperty(
						PROPERTY_CHIP_PRICE_EUR, "33"));
				this.chipDiscount = Float.parseFloat(getBundle().getProperty(
						PROPERTY_CHIP_DISCOUNT_EUR, "3"));
			}
			loadCurrentStep(iwc, parseAction(iwc));
		}
	}

	private void loadCurrentStep(IWContext iwc, int action)
			throws RemoteException {
		if (action == ACTION_STEP_DISCLAIMER) {
			Runner runner = getRunner();
			if (runner.getDistance().isAskQuestions()) {
				if (runner.getQuestion1NeverRan() == false
						&& (runner.getQuestion1Hour().equals("-1")
								|| runner.getQuestion1Minute().equals("-1") || runner
								.getQuestion1Year().equals("-1"))) {
					action = ACTION_STEP_QUESTIONS;
					showQuestionsError = true;
				}

				if (runner.getQuestion2Hour().equals("-1")
						|| runner.getQuestion2Minute().equals("-1")) {
					action = ACTION_STEP_QUESTIONS;
					showQuestionsError = true;
				}

				if (runner.getQuestion3NeverRan() == false
						&& (runner.getQuestion3Hour().equals("-1")
								|| runner.getQuestion3Minute().equals("-1") || runner
								.getQuestion3Year().equals("-1"))) {
					action = ACTION_STEP_QUESTIONS;
					showQuestionsError = true;
				}
			}
		}
		
		if (action == ACTION_STEP_CHARITY) {
			Runner runner = getRunner();
			
			if (runner.getDistance().isRelayDistance()) {
				boolean leg1 = false;
				boolean leg2 = false;
				boolean leg3 = false;
				boolean leg4 = false;
				
				if (runner.getRelayLeg() == null || "".equals(runner.getRelayLeg())) {
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
				
				if (runner.getPartner1SSN() != null && !"".equals(runner.getPartner1SSN())) {
					if (runner.getPartner1Name() == null || "".equals(runner.getPartner1Name())) {
						action = ACTION_STEP_RELAY;
						showRelayError = true;						
					}

					if (runner.getPartner1Email() == null || "".equals(runner.getPartner1Email())) {
						action = ACTION_STEP_RELAY;
						showRelayError = true;						
					}

					if (runner.getPartner1ShirtSize() == null || "".equals(runner.getPartner1ShirtSize()) || "-1".equals(runner.getPartner1ShirtSize())) {
						action = ACTION_STEP_RELAY;
						showRelayError = true;						
					}

					if (runner.getPartner1Leg() == null || "".equals(runner.getPartner1Leg())) {
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

					if (runner.getPartner2SSN() != null && !"".equals(runner.getPartner2SSN())) {
						if (runner.getPartner2Name() == null || "".equals(runner.getPartner2Name())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;						
						}

						if (runner.getPartner2Email() == null || "".equals(runner.getPartner2Email())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;						
						}

						if (runner.getPartner2ShirtSize() == null || "".equals(runner.getPartner2ShirtSize()) || "-1".equals(runner.getPartner2ShirtSize())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;						
						}

						if (runner.getPartner2Leg() == null || "".equals(runner.getPartner2Leg())) {
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

					if (runner.getPartner3SSN() != null && !"".equals(runner.getPartner3SSN())) {
						if (runner.getPartner3Name() == null || "".equals(runner.getPartner3Name())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;						
						}

						if (runner.getPartner3Email() == null || "".equals(runner.getPartner3Email())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;						
						}

						if (runner.getPartner3ShirtSize() == null || "".equals(runner.getPartner3ShirtSize()) || "-1".equals(runner.getPartner3ShirtSize())) {
							action = ACTION_STEP_RELAY;
							showRelayError = true;						
						}

						if (runner.getPartner3Leg() == null || "".equals(runner.getPartner3Leg())) {
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
		case ACTION_STEP_CHIP:
			stepChip(iwc);
			break;
		case ACTION_STEP_QUESTIONS:
			stepQuestions(iwc);
			break;
		case ACTION_STEP_RELAY:
			stepRelay(iwc);
			break;
		case ACTION_STEP_CHARITY:
			stepCharity(iwc);
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
		case ACTION_STEP_TRAVELSUPPORT:
			stepTravelsupport(iwc);
			break;
		}
	}

	private void loadPreviousStep(IWContext iwc) throws RemoteException {
		// Convenient to use when Exception is caught in one step, and user is
		// sent to the previous step
		loadCurrentStep(iwc, Integer.parseInt(iwc
				.getParameter(PARAMETER_FROM_ACTION)));
	}

	// private void loadPreviousStep(IWContext iwc, int action) throws
	// RemoteException {
	// Convenient to use when Exception is caught in one step, and user is sent
	// to the previous step
	// loadCurrentStep(iwc,getPreviousStep(iwc, action));
	// }

	private void stepPersonalLookup(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_LIMIT_RUN_IDS);
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

		if (isConstrainedToOneRun()) {
			table.add(
					getInformationTable(localizeForRun(
							"run_reg.information_text_step_1",
							"Information text 1...")), 1, row++);
		} else {
			table.add(
					getInformationTable(localize(
							"run_reg.information_text_step_1",
							"Information text 1...")), 1, row++);
		}
		table.setHeight(row++, 6);

		table.setCellpadding(1, row, 24);
		table.add(getHeader(localize("run_reg.personal_id", "Personal ID")
				+ ":"), 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);

		TextInput input = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_PERSONAL_ID));
		input.setAsIcelandicSSNumber(localize("run_reg.not_valid_personal_id",
				"The personal ID you have entered is not valid"));
		input.setLength(10);
		input.setMaxlength(10);
		input.setInFocusOnPageLoad(true);
		input.setAsNotEmpty(localize("run_reg.not_valid_personal_id",
				"The personal ID you have entered is not valid"));
		table.add(input, 1, row++);

		UIComponent buttonsContainer = getButtonsFooter(iwc, false, true);
		form.add(buttonsContainer);

		add(form);
	}

	private void stepPersonalDetails(IWContext iwc) throws RemoteException {
		Form form = new Form();
		if (this.isIcelandic) {
			form.maintainParameter(PARAMETER_PERSONAL_ID);
		}
		form.maintainParameter(PARAMETER_LIMIT_RUN_IDS);
		form.maintainParameter(PARAMETER_QUESTION1_NEVER);
		form.maintainParameter(PARAMETER_QUESTION3_NEVER);

		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_PERSONALDETAILS);

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);

		// table.add(getPhasesTable(this.isIcelandic ? 2 : 1, this.isIcelandic ?
		// 8 : 6, "run_reg.registration", "Registration"), 1, row++);
		// table.add(getStepsHeader(iwc, ACTION_STEP_PERSONALDETAILS),1,row++);
		form.add(getStepsHeader(iwc, ACTION_STEP_PERSONALDETAILS));

		form.add(table);
		int row = 1;

		table.setHeight(row++, 12);

		table.add(getInformationTable(localize(
				"run_reg.information_text_step_2", "Information text 2...")),
				1, row++);
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
		// nameField.setWidth(Table.HUNDRED_PERCENT);
		if (getRunner().getName() != null) {
			nameField.setContent(getRunner().getName());
		}
		if (this.isIcelandic) {
			nameField.setDisabled(true);
			if (getRunner().getUser() != null) {
				nameField.setContent(getRunner().getUser().getName());
			}
		} else {
			nameField.setAsAlphabeticText(localize("run_reg.name_err_msg",
					"Your name may only contain alphabetic characters"));
			nameField.setAsNotEmpty(localize("run_reg.name_not_empty",
					"Name field can not be empty"));
		}

		DropdownMenu genderField = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_GENDER));
		Collection genders = getGenderBusiness(iwc).getAllGenders();
		genderField.addMenuElement("-1", localize("run_reg.select_gender",
				"Select gender..."));
		if (genders != null) {
			Iterator iter = genders.iterator();
			while (iter.hasNext()) {
				Gender gender = (Gender) iter.next();
				genderField
						.addMenuElement(gender.getPrimaryKey().toString(),
								localize("gender." + gender.getName(), gender
										.getName()));
			}
		}
		if (getRunner().getGender() != null) {
			genderField.setSelectedElement(getRunner().getGender()
					.getPrimaryKey().toString());
		}
		if (this.isIcelandic) {
			genderField.setDisabled(true);
			if (getRunner().getUser() != null) {
				genderField.setSelectedElement(getRunner().getUser()
						.getGenderID());
			}
		} else {
			genderField.setAsNotEmpty(localize("run_reg.gender_not_empty",
					"Gender can not be empty"));
		}

		choiceTable.add(
				getHeader(localize(IWMarathonConstants.RR_NAME, "Name")), 1,
				iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_GENDER,
				"Gender")), 3, iRow);
		choiceTable.add(redStar, 3, iRow++);
		choiceTable.add(nameField, 1, iRow);
		choiceTable.add(genderField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		TextInput ssnISField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_PERSONAL_ID));
		ssnISField.setLength(10);
		if (this.isIcelandic) {
			ssnISField.setDisabled(true);
			if (getRunner().getUser() != null) {
				ssnISField.setContent(getRunner().getUser().getPersonalID());
			}
		}

		DateInput ssnField = (DateInput) getStyledInterface(new DateInput(
				PARAMETER_PERSONAL_ID));
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

		ssnField.setYearRange(newestYearStamp.getYear(), earliestYearStamp
				.getYear());
		Object[] maximumArgs = { String.valueOf(maximumAgeForRun) };
		ssnField
				.setEarliestPossibleDate(
						maximumAgeStamp.getDate(),
						MessageFormat
								.format(
										localize(
												"run_reg.invalid_date_of_birth_exeeding",
												"Invalid date of birth.  You have to be {0} or younger to register"),
										maximumArgs));
		Object[] minimumArgs = { String.valueOf(minimumAgeForRun) };
		ssnField
				.setLatestPossibleDate(
						minimumAgeStamp.getDate(),
						MessageFormat
								.format(
										localize(
												"run_reg.invalid_date_of_birth",
												"Invalid date of birth.  You have to be {0} years old to register"),
										minimumArgs));

		if (getRunner().getDateOfBirth() != null) {
			ssnField.setDate(getRunner().getDateOfBirth());
		}

		TextInput emailField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_EMAIL));
		emailField.setAsEmail(localize("run_reg.email_err_msg",
				"Not a valid email address"));
		emailField.setAsNotEmpty(localize("run_reg.continue_without_email",
				"You can not continue without entering an e-mail"));
		// emailField.setWidth(Table.HUNDRED_PERCENT);
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

		Collection countries = getRunBusiness(iwc).getCountries(
				getPresetCountriesArray());
		DropdownMenu nationalityField = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_NATIONALITY));
		DropdownMenu countryField = (DropdownMenu) getStyledInterface(new DropdownMenu(
				PARAMETER_COUNTRY));
		nationalityField.addMenuElement("-1", localize(
				"run_reg.select_nationality", "Select nationality..."));
		countryField.addMenuElement("-1", localize("run_reg.select_country",
				"Select country..."));
		SelectorUtility util = new SelectorUtility();
		if (countries != null && !countries.isEmpty()) {
			nationalityField = (DropdownMenu) util.getSelectorFromIDOEntities(
					nationalityField, countries, "getName");
			countryField = (DropdownMenu) util.getSelectorFromIDOEntities(
					countryField, countries, "getName");
		}
		if (this.isIcelandic) {
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
		// nationalityField.setWidth(Table.HUNDRED_PERCENT);
		nationalityField.setAsNotEmpty(localize(
				"run_reg.must_select_nationality",
				"You must select your nationality"));
		// countryField.setWidth(Table.HUNDRED_PERCENT);
		if (!this.isIcelandic) {
			countryField.setAsNotEmpty(localize("run_reg.must_select_country",
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
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_EMAIL,
				"Email")), 3, iRow);
		choiceTable.add(redStar, 3, iRow++);
		if (this.isIcelandic) {
			choiceTable.add(ssnISField, 1, iRow);
		} else {
			choiceTable.add(ssnField, 1, iRow);
		}
		choiceTable.add(emailField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		TextInput addressField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_ADDRESS));
		// addressField.setWidth(Table.HUNDRED_PERCENT);
		if (!this.isIcelandic) {
			addressField.setAsNotEmpty(localize("run_reg.must_provide_address",
					"You must enter your address."));
		}
		if (getRunner().getAddress() != null) {
			addressField.setContent(getRunner().getAddress());
		}
		if (this.isIcelandic) {
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
		emailField2.setAsEmail(localize("run_reg.email_err_msg",
				"Not a valid email address"));
		emailField2.setAsNotEmpty(localize("run_reg.continue_without_email2",
				"You can not continue without repeating the e-mail"));
		// emailField.setWidth(Table.HUNDRED_PERCENT);
		/*if (getRunner().getEmail2() != null) {
			emailField2.setContent(getRunner().getEmail2());
		} else if (getRunner().getUser() != null) {
			try {
				Email mail = getUserBusiness(iwc).getUsersMainEmail(
						getRunner().getUser());
				emailField2.setContent(mail.getEmailAddress());
			} catch (NoEmailFoundException nefe) {
				// No email registered...
			}
		}*/

		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_ADDRESS,
				"Address")), 1, iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_EMAIL2,
				"Email repeated")), 3, iRow);
		choiceTable.add(redStar, 3, iRow++);
		choiceTable.add(addressField, 1, iRow);
		choiceTable.add(emailField2, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		TextInput cityField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_CITY));
		// cityField.setWidth(Table.HUNDRED_PERCENT);
		if (!this.isIcelandic) {
			cityField.setAsNotEmpty(localize("run_reg.must_provide_city",
					"You must enter your city of living."));
		}
		if (getRunner().getCity() != null) {
			cityField.setContent(getRunner().getCity());
		}
		if (this.isIcelandic) {
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
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_TEL,
				"Telephone")), 3, iRow++);
		choiceTable.add(cityField, 1, iRow);
		choiceTable.add(telField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		TextInput postalField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_POSTAL_CODE));
		if (!this.isIcelandic) {
			postalField.setAsNotEmpty(localize("run_reg.must_provide_postal",
					"You must enter your postal address."));
		}
		postalField.setMaxlength(10);
		postalField.setLength(10);
		if (getRunner().getPostalCode() != null) {
			postalField.setContent(getRunner().getPostalCode());
		}
		if (this.isIcelandic) {
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

		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_POSTAL,
				"Postal Code")), 1, iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_MOBILE,
				"Mobile Phone")), 3, iRow++);
		choiceTable.add(postalField, 1, iRow);
		choiceTable.add(mobileField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_COUNTRY,
				"Country")), 1, iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_NATIONALITY,
				"Nationality")), 3, iRow);
		choiceTable.add(redStar, 3, iRow++);

		choiceTable.add(countryField, 1, iRow);
		choiceTable.add(nationalityField, 3, iRow++);
		choiceTable.setHeight(iRow++, 3);

		Integer runYearID = null;
		if (runner.getYear() != null) {
			runYearID = (Integer) runner.getYear().getPrimaryKey();
		}

		if (this.showCategories) {
			DropdownMenu categoriesDropdown = (CategoriesForRunYearDropDownMenu) (getStyledInterface(new CategoriesForRunYearDropDownMenu(
					PARAMETER_CATEGORY_ID, runYearID)));
			categoriesDropdown.setAsNotEmpty(localize(
					"run_reg.must_select_category",
					"You must select department"));
			if (getRunner().getCategory() != null) {
				categoriesDropdown.setSelectedElement(getRunner().getCategory()
						.getPrimaryKey().toString());
			}
			int iCol = 1;

			choiceTable.add(
					getHeader(localize("run_reg.category", "Department")),
					iCol, iRow);
			choiceTable.add(redStar, iCol, iRow++);
			choiceTable.add(categoriesDropdown, iCol, iRow);

			// RemoteScriptHandler rshCategories = new
			// RemoteScriptHandler(distanceDropdown, categoriesDropdown);
			// try {
			// rshCategories.setRemoteScriptCollectionClass(DistanceMenuCategoriesMenuInputCollectionHandler.class);
			// } catch (InstantiationException e) {
			// e.printStackTrace();
			// } catch (IllegalAccessException e) {
			// e.printStackTrace();
			// }
			// add(rshCategories);
		}

		UIComponent buttonsContainer = getButtonsFooter(iwc, false, true);
		form.add(buttonsContainer);

		add(form);
	}

	private void stepRunDetails(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_QUESTION1_NEVER);
		form.maintainParameter(PARAMETER_QUESTION3_NEVER);

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

		table.add(getInformationTable(localize(
				"run_reg.information_text_step_20",
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

		ActiveRunDropDownMenu runDropdown = getRunDropdown(iwc, runner);
		if (runDropdown.getChildCount() == 1) {
			getParentPage().setAlertOnLoad(
					localize("run_reg.no_runs_available",
							"There are no runs you can register for."));
			if (this.isIcelandic) {
				removeRunner(iwc, getRunner().getPersonalID());
				stepPersonalLookup(iwc);
				return;
			} else {
				stepPersonalDetails(iwc);
				return;
			}
		}
		runDropdown.clearChildren();

		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_PRIMARY_DD,
				"Run")), 1, iRow);
		if (!isConstrainedToOneRun()) {
			choiceTable.add(redStar, 1, iRow);
			choiceTable.add(runDropdown, 3, iRow++);
		} else {
			choiceTable.add(getHeader(localize(runner.getRun().getName(),
					runner.getRun().getName())
					+ " " + runner.getYear().getName()), 3, iRow++);
			choiceTable.add(runDropdown, 1, 0);
			runDropdown.setVisible(false);
		}

		choiceTable.setHeight(iRow++, 5);

		DistanceDropDownMenu distanceDropdown = (DistanceDropDownMenu) getStyledInterface(new DistanceDropDownMenu(
				PARAMETER_DISTANCE, runner));
		distanceDropdown.setAsNotEmpty(localize("run_reg.must_select_distance",
				"You have to select a distance"));

		choiceTable.add(getHeader(localize(IWMarathonConstants.RR_SECONDARY_DD,
				"Distance")), 1, iRow);
		choiceTable.add(redStar, 1, iRow);
		choiceTable.add(distanceDropdown, 3, iRow++);

		RemoteScriptHandler rsh = new RemoteScriptHandler(runDropdown,
				distanceDropdown);
		try {
			rsh.setRemoteScriptCollectionClass(RunInputCollectionHandler.class);
			rsh.addParameter(RunInputCollectionHandler.RUNNER_PERSONAL_ID,
					getRunner().getPersonalID());

			if (getRunner().getUser() != null) {
				rsh.addParameter(RunInputCollectionHandler.PARAMETER_USER_ID,
						getRunner().getUser().getPrimaryKey().toString());
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		add(rsh);

		choiceTable.setHeight(iRow++, 5);

		DropdownMenu tShirtField = null;
		if (!isHideShirtSize()) {
			tShirtField = (DropdownMenu) getStyledInterface(new DropdownMenu(
					PARAMETER_SHIRT_SIZE));
			tShirtField.addMenuElement("-1", localize(
					"run_reg.select_tee_shirt_size", "Select shirt size..."));
			if (getRunner().getDistance() != null) {
				String shirtSizeMetadata = getRunner().getDistance()
						.getMetaData(PARAMETER_SHIRT_SIZES_PER_RUN);
				List shirtSizes = null;
				if (shirtSizeMetadata != null) {
					shirtSizes = ListUtil
							.convertCommaSeparatedStringToList(shirtSizeMetadata);
				}
				if (shirtSizes != null) {
					Iterator shirtIt = shirtSizes.iterator();
					while (shirtIt.hasNext()) {
						String shirtSizeKey = (String) shirtIt.next();
						tShirtField.addMenuElement(shirtSizeKey, localize(
								"shirt_size." + shirtSizeKey, shirtSizeKey));
					}
				}
				if (getRunner().getDistance() != null) {
					tShirtField.setSelectedElement(getRunner().getShirtSize());
				}
			}
			tShirtField.setAsNotEmpty(localize(
					"run_reg.must_select_shirt_size",
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

			// Link tShirtInfo = new Link();
		}

		if (!isHideShirtSize()) {
			choiceTable.add(getHeader(localize(IWMarathonConstants.RR_TSHIRT,
					"Shirt size")), 1, iRow);
			choiceTable.add(redStar, 1, iRow);
			choiceTable.add(tShirtField, 3, iRow++);
		}

		choiceTable.setHeight(iRow++, 10);

		UIComponent buttonsContainer = getButtonsFooter(iwc, true, true);
		form.add(buttonsContainer);

		add(form);
	}

	protected ActiveRunDropDownMenu getRunDropdown(IWContext iwc, Runner runner) {
		ActiveRunDropDownMenu runDropdown = null;
		String[] constrainedRunIds = getRunIdsArray();
		if (constrainedRunIds == null) {
			runDropdown = (ActiveRunDropDownMenu) getStyledInterface(new ActiveRunDropDownMenu(
					PARAMETER_RUN, runner, null, this.showAllThisYear));
		} else {
			runDropdown = (ActiveRunDropDownMenu) getStyledInterface(new ActiveRunDropDownMenu(
					PARAMETER_RUN, runner, constrainedRunIds,
					this.showAllThisYear));
		}

		runDropdown.setAsNotEmpty(localize("run_reg.must_select_run",
				"You have to select a run"));
		try {
			// main is run to check if any runs exists. Otherwise
			// isConstrainedToOneRun() will not work
			runDropdown.main(iwc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (isConstrainedToOneRun()) {
			String runId = getRunIds();
			runDropdown.setSelectedElement(runId);
			runDropdown.setDisabled(true);
		}

		return runDropdown;
	}

	private void stepChip(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_QUESTION1_NEVER);
		form.maintainParameter(PARAMETER_QUESTION3_NEVER);

		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_CHIP);

		form.add(getStepsHeader(iwc, ACTION_STEP_CHIP));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		// table.add(getPhasesTable(this.isIcelandic ? 3 : 2, this.isIcelandic ?
		// 8 : 6, "run_reg.time_registration_chip", "Time registration chip"),
		// 1, row++);
		// table.add(getStepsHeader(iwc, ACTION_STEP_CHIP),1,row++);
		table.setHeight(row++, 12);

		// table.add(getText(localize(key, "Information text 4...")), 1, row++);
		String[] attributes = { localize(getRunner().getRun().getName(),
				getRunner().getRun().getName()) };
		table.add(getText(MessageFormat.format(localizeForRun(
				"run_reg.information_text_step_3", "Information text 3..."),
				attributes)), 1, row++);
		table.setHeight(row++, 18);

		RadioButton rentChip = getRadioButton(PARAMETER_CHIP,
				IWMarathonConstants.CHIP_RENT);
		rentChip.setSelected(getRunner().isRentChip());
		rentChip.setMustBeSelected(localize("run_reg.must_select_chip_option",
				"You have to select a chip option"));

		table.add(rentChip, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getHeader(localize("run_reg.rent_chip", "Rent a chip")), 1,
				row++);
		table.setHeight(row++, 6);
		table
				.add(
						getText(localize(
								"run_reg.rent_chip_information",
								"A disposable chip is included in the entry fee and will be included in your race material.")),
						1, row);
		table.setBottomCellBorder(1, row, 1, "#D7D7D7", "solid");
		table.setCellpaddingBottom(1, row++, 6);

		/*
		 * RadioButton ownChip = getRadioButton(PARAMETER_CHIP,
		 * IWMarathonConstants.CHIP_OWN);
		 * ownChip.setSelected(getRunner().isOwnChip()); TextInput chipNumber =
		 * (TextInput) getStyledInterface(new TextInput(PARAMETER_CHIP_NUMBER));
		 * chipNumber.setLength(7); chipNumber.setMaxlength(7); if
		 * (getRunner().getChipNumber() != null) {
		 * chipNumber.setContent(getRunner().getChipNumber()); }
		 * 
		 * table.setHeight(row++, 12); table.add(ownChip, 1, row);
		 * table.add(Text.getNonBrakingSpace(), 1, row);
		 * table.add(getHeader(localize("run_reg.own_chip",
		 * "I own a chip - chipnumber")), 1, row);
		 * table.add(Text.getNonBrakingSpace(), 1, row); table.add(chipNumber,
		 * 1, row); table.setBottomCellBorder(1, row, 1, "#D7D7D7", "solid");
		 * table.setCellpaddingBottom(1, row++, 6);
		 */

		if (!isDisableChipBuy()) {

			RadioButton buyChip = getRadioButton(PARAMETER_CHIP,
					IWMarathonConstants.CHIP_BUY);
			buyChip.setSelected(getRunner().isBuyChip());

			table.setHeight(row++, 12);
			table.add(buyChip, 1, row);
			table.add(Text.getNonBrakingSpace(), 1, row);
			table.add(getHeader(localize("run_reg.buy_chip", "Buy chip")), 1,
					row++);
			table.setHeight(row++, 6);
			String priceText = formatAmount(iwc.getCurrentLocale(),
					this.chipPrice);
			table
					.add(
							getText(localize(
									"run_reg.buy_chip_information",
									"You can buy a multi use chip that you can use in future competitions, at a price of ")
									+ priceText), 1, row++);
		}

		form.add(getButtonsFooter(iwc));

		add(form);
	}

	/*
	 * private void stepTransport(IWContext iwc) { Form form = new Form();
	 * form.maintainParameter(PARAMETER_PERSONAL_ID);
	 * form.addParameter(PARAMETER_ACTION, "-1");
	 * form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_TRANSPORT);
	 * 
	 * form.add(getStepsHeader(iwc, ACTION_STEP_TRANSPORT));
	 * 
	 * Table table = new Table(); table.setCellpadding(0);
	 * table.setCellspacing(0); table.setWidth(Table.HUNDRED_PERCENT);
	 * form.add(table); int row = 1;
	 * 
	 * //table.add(getPhasesTable(this.isIcelandic ? 3 : 2, this.isIcelandic ? 8
	 * : 6, "run_reg.order_transport", "Order transport"), 1, row++);
	 * //table.add(getStepsHeader(iwc, ACTION_STEP_TRANSPORT),1,row++);
	 * table.setHeight(row++, 12);
	 * 
	 * table.add(getInformationTable(localize(
	 * "run_reg.information_text_step_3_transport",
	 * "Bus trip to race starting point and back to Reykjavik after the race is organized by Reykjavik Marathon. Please select if you like to order a seat or not."
	 * )), 1, row++); table.setHeight(row++, 18);
	 * 
	 * RadioButton orderTransport = getRadioButton(PARAMETER_TRANSPORT,
	 * Boolean.TRUE.toString());
	 * orderTransport.setSelected(getRunner().isTransportOrdered());
	 * orderTransport
	 * .setMustBeSelected(localize("run_reg.must_select_transport_option",
	 * "You must select bus trip option."));
	 * 
	 * RadioButton notOrderTransport = getRadioButton(PARAMETER_TRANSPORT,
	 * Boolean.FALSE.toString());
	 * notOrderTransport.setSelected(getRunner().isNoTransportOrdered());
	 * 
	 * table.add(orderTransport, 1, row); table.add(Text.getNonBrakingSpace(),
	 * 1, row); Distance distance = getRunner().getDistance(); String
	 * distancePriceString = ""; if (distance != null) { distancePriceString =
	 * formatAmount(iwc.getCurrentLocale(),
	 * distance.getPriceForTransport(iwc.getCurrentLocale())); } Object[] args =
	 * { distancePriceString };
	 * table.add(getHeader(MessageFormat.format(localize
	 * ("run_reg.order_transport_text",
	 * "I want to order a bus trip. The price is: {0}"), args)), 1, row);
	 * table.setHeight(row++, 6);
	 * 
	 * 
	 * 
	 * table.add(getText((localize("run_reg.order_tranport_information_"+getRunner
	 * ().getRun().getName().replace(' ', '_'),
	 * "Info about transport order..."))), 1, row); table.setBottomCellBorder(1,
	 * row, 1, "#D7D7D7", "solid"); table.setCellpaddingBottom(1, row++, 6);
	 * table.add(notOrderTransport, 1, row);
	 * table.add(Text.getNonBrakingSpace(), 1, row);
	 * table.add(getHeader(localize("run_reg.not_order_transport_text",
	 * "I don't want to order a bus trip.")), 1, row);
	 * 
	 * form.add(getButtonsFooter(iwc));
	 * 
	 * add(form); }
	 */

	private void stepQuestions(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
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

		table.add(getInformationTable(localize(
				"run_reg.information_text_step_questions",
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

		table
				.add(
						getText((localize(
								"run_reg.question1",
								"What is your personal best, if you have run the Laugavegur Ultra Marathon before? (please choose time and year)"))),
						1, row);
		table.add(redStar, 1, row++);
		table.add(createHourDropDown(PARAMETER_QUESTION1_HOUR, getRunner()
				.getQuestion1Hour()), 1, row);
		table.add(createMinuteDropDown(PARAMETER_QUESTION1_MINUTE, getRunner()
				.getQuestion1Minute()), 1, row);
		table.add(createYearDropDown(PARAMETER_QUESTION1_YEAR, 1996, 2009,
				getRunner().getQuestion1Year()), 1, row++);
		CheckBox neverBefore = getCheckBox(PARAMETER_QUESTION1_NEVER,
				Boolean.TRUE.toString());
		neverBefore.setChecked(getRunner().getQuestion1NeverRan());
		table.add(neverBefore, 1, row);
		table.add(getText((localize("run_reg.question1_never_before",
				"I haven't done the Laugavegur Ultra Marathon before"))), 1,
				row++);

		table.setHeight(row++, 18);
		table
				.add(
						getText((localize(
								"run_reg.question2",
								"What is your goal time for this year Laugavegur Ultra Marathon? (please choose time)"))),
						1, row);
		table.add(redStar, 1, row++);
		table.add(createHourDropDown(PARAMETER_QUESTION2_HOUR, getRunner()
				.getQuestion2Hour()), 1, row);
		table.add(createMinuteDropDown(PARAMETER_QUESTION2_MINUTE, getRunner()
				.getQuestion2Minute()), 1, row++);

		table.setHeight(row++, 18);
		table
				.add(
						getText((localize("run_reg.question3",
								"What is your personal best in a Marathon race? (please choose time and year)"))),
						1, row);
		table.add(redStar, 1, row++);
		table.add(createHourDropDown(PARAMETER_QUESTION3_HOUR, getRunner()
				.getQuestion3Hour()), 1, row);
		table.add(createMinuteDropDown(PARAMETER_QUESTION3_MINUTE, getRunner()
				.getQuestion3Minute()), 1, row);
		table.add(createYearDropDown(PARAMETER_QUESTION3_YEAR, 1990, 2010,
				getRunner().getQuestion3Year()), 1, row++);
		CheckBox neverBefore2 = getCheckBox(PARAMETER_QUESTION3_NEVER,
				Boolean.TRUE.toString());
		neverBefore2.setChecked(getRunner().getQuestion3NeverRan());
		table.add(neverBefore2, 1, row);
		table.add(getText((localize("run_reg.question3_never_before",
				"I haven't done a marathon before"))), 1, row++);

		form.add(getButtonsFooter(iwc));

		add(form);
	}

	private void stepRelay(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
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

		table.add(getInformationTable(localize(
				"run_reg.information_text_step_relay",
				"Enter information about your relay partners.")), 1, row++);
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
		DropdownMenu relPart1ShirtSize = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_RELAY_PARTNER_1_SHIRT_SIZE));
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
		DropdownMenu relPart2ShirtSize = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_RELAY_PARTNER_2_SHIRT_SIZE));
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
		DropdownMenu relPart3ShirtSize = (DropdownMenu) getStyledInterface(new DropdownMenu(PARAMETER_RELAY_PARTNER_3_SHIRT_SIZE));
		TextInput relPart3Leg = new TextInput(PARAMETER_RELAY_PARTNER_3_LEG);
		if (getRunner().getPartner3Leg() != null) {
			relPart3Leg.setValue(getRunner().getPartner3Leg());
		}
		
		relPart1ShirtSize.addMenuElement("-1", localize("run_reg.select_tee_shirt_size", "Select shirt size..."));
		relPart2ShirtSize.addMenuElement("-1", localize("run_reg.select_tee_shirt_size", "Select shirt size..."));
		relPart3ShirtSize.addMenuElement("-1", localize("run_reg.select_tee_shirt_size", "Select shirt size..."));
		if (getRunner().getDistance() != null) {
			String shirtSizeMetadata = getRunner().getDistance()
					.getMetaData(PARAMETER_SHIRT_SIZES_PER_RUN);
			List shirtSizes = null;
			if (shirtSizeMetadata != null) {
				shirtSizes = ListUtil
						.convertCommaSeparatedStringToList(shirtSizeMetadata);
			}
			if (shirtSizes != null) {
				Iterator shirtIt = shirtSizes.iterator();
				while (shirtIt.hasNext()) {
					String shirtSizeKey = (String) shirtIt.next();
					relPart1ShirtSize.addMenuElement(shirtSizeKey, localize(
							"shirt_size." + shirtSizeKey, shirtSizeKey));
					relPart2ShirtSize.addMenuElement(shirtSizeKey, localize(
							"shirt_size." + shirtSizeKey, shirtSizeKey));
					relPart3ShirtSize.addMenuElement(shirtSizeKey, localize(
							"shirt_size." + shirtSizeKey, shirtSizeKey));
				}
			}
			if (getRunner().getPartner1ShirtSize() != null) {
				relPart1ShirtSize.setSelectedElement(getRunner().getPartner1ShirtSize());
			}
			if (getRunner().getPartner2ShirtSize() != null) {
				relPart2ShirtSize.setSelectedElement(getRunner().getPartner2ShirtSize());
			}
			if (getRunner().getPartner3ShirtSize() != null) {
				relPart3ShirtSize.setSelectedElement(getRunner().getPartner3ShirtSize());
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
		choiceTable.add(localize("run_reg.relay_ssn","SSN"), 2, choiceRow);
		choiceTable.add(localize("run_reg.relay_name","Name"), 3, choiceRow);
		choiceTable.add(localize("run_reg.relay_email","E-mail"), 4, choiceRow);
		choiceTable.add(localize("run_reg.relay_shirt_size","Shirt size"), 5, choiceRow);
		choiceTable.add(localize("run_reg.relay_leg","Leg"), 6, choiceRow++);

		choiceTable.add("1", 1, choiceRow);
		choiceTable.add(getRunner().getPersonalID(), 2, choiceRow);
		if (this.isIcelandic) {
			if (getRunner().getUser() != null) {
				choiceTable.add(getRunner().getUser().getName(), 3, choiceRow);
			} else {
				choiceTable.add(getRunner().getName(), 3, choiceRow);
			}
		} else {
			choiceTable.add(getRunner().getName(), 3, choiceRow);
		}

		choiceTable.add(getRunner().getEmail(), 4, choiceRow);
		choiceTable.add(localize(
				"shirt_size." + getRunner().getShirtSize(), getRunner().getShirtSize()), 5, choiceRow);
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

	private void stepDisclaimer(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_QUESTION1_NEVER);
		form.maintainParameter(PARAMETER_QUESTION3_NEVER);

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

		// table.add(getText(localize(key, "Information text 4...")), 1, row++);
		String[] attributes = { localize(getRunner().getRun().getName(),
				getRunner().getRun().getName()) };
		table.add(getText(MessageFormat.format(localizeForRun(
				"run_reg.information_text_step_4", "Information text 4..."),
				attributes)), 1, row++);

		Layer disclaimerLayer = new Layer(Layer.DIV);
		CheckBox agreeCheck = getCheckBox(PARAMETER_AGREE, Boolean.TRUE
				.toString());
		agreeCheck.setToEnableWhenChecked(next);
		agreeCheck.setToDisableWhenUnchecked(next);
		agreeCheck.setChecked(getRunner().isAgree());

		Label disclaimerLabel = new Label(localize("run_reg.agree_terms",
				"Yes, I agree"), agreeCheck);
		disclaimerLayer.add(agreeCheck);
		disclaimerLayer.add(disclaimerLabel);

		table.setHeight(row++, 18);

		table.add(getText(MessageFormat.format(localizeForRun(
				"run_reg.allow_email_step_4",
				"Information about emails in step 4..."), attributes)), 1,
				row++);
		table.add(disclaimerLayer, 1, row++);
		table.setHeight(row++, 18);

		RadioButton allowEmails = getRadioButton(PARAMETER_ALLOW_EMAILS,
				Boolean.TRUE.toString());
		allowEmails.setSelected(getRunner().getAllowsEmails());
		allowEmails.setMustBeSelected(localize(
				"run_reg.must_select_email_option",
				"You must select email option."));

		RadioButton notAllowEmails = getRadioButton(PARAMETER_ALLOW_EMAILS,
				Boolean.FALSE.toString());
		notAllowEmails.setSelected(getRunner().getNotAllowsEmails());

		table.add(allowEmails, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getHeader(localize("run_reg.allow_emails_text",
				"I allow emails")), 1, row);
		table.setHeight(row++, 10);

		table.add(notAllowEmails, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(getHeader(localize("run_reg.not_allow_emails_text",
				"I don't allow emails.")), 1, row);

		UIComponent buttonsContainer = getButtonsFooter(iwc, true, false);
		buttonsContainer.getChildren().add(next);
		form.add(buttonsContainer);

		add(form);
	}

	private void stepOverview(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
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

		// table.add(getPhasesTable(this.isIcelandic ? 6 : 5, this.isIcelandic ?
		// 8 : 6, "run_reg.overview", "Overview"), 1, row++);
		// table.add(getStepsHeader(iwc, ACTION_STEP_OVERVIEW),1,row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize(
				"run_reg.information_text_step_5", "Information text 5...")),
				1, row++);
		table.setHeight(row++, 18);

		Map runners = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
		Table runnerTable = new Table();
		//runnerTable.setColumns(columns)
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		runnerTable
				.add(getHeader(localize("run_reg.runner_name", "Runner name")),
						1, 1);
		runnerTable.add(getHeader(localize("run_reg.run", "Run")), 2, 1);
		runnerTable.add(getHeader(localize("run_reg.distance", "Distance")), 3,
				1);
		table.add(runnerTable, 1, row++);
		int runRow = 2;

		boolean addedLegHeader = false;
		
		Iterator iter = runners.values().iterator();
		while (iter.hasNext()) {
			Runner runner = (Runner) iter.next();
			if (runner.getRun() != null) {
				if (runner.getUser() != null) {
					runnerTable.add(getText(runner.getUser().getName()), 1, runRow);
				} else {
					runnerTable.add(getText(runner.getName()), 1, runRow);
				}

				runnerTable.add(getText(localize(runner.getRun().getName(),
						runner.getRun().getName())
						+ " " + runner.getDistance().getYear().getName()), 2,
						runRow);
				runnerTable.add(getText(localize(
						runner.getDistance().getName(), runner.getDistance()
								.getName())), 3, runRow);

				if (runner.getPartner1SSN() != null && !"".equals(runner.getPartner1SSN())) {
					if (!addedLegHeader) {
						runnerTable.add(getHeader(localize("run_reg.relay_leg","Leg")), 4, 1);
						addedLegHeader = true;
					}
					
					runnerTable.add(getText(
							runner.getRelayLeg()), 4, runRow++);
					
					runnerTable.add(getText(runner.getPartner1Name()), 1, runRow);
					runnerTable.add(getText(localize(runner.getRun().getName(),
							runner.getRun().getName())
							+ " " + runner.getDistance().getYear().getName()), 2, runRow);
					runnerTable.add(getText(localize(
							runner.getDistance().getName(), runner.getDistance()
							.getName())), 3, runRow);
					runnerTable.add(getText(runner.getPartner1Leg()), 4, runRow++);
					
					if (runner.getPartner2SSN() != null && !"".equals(runner.getPartner2SSN())) {
						runnerTable.add(getText(runner.getPartner2Name()), 1, runRow);
						runnerTable.add(getText(localize(runner.getRun().getName(),
								runner.getRun().getName())
								+ " " + runner.getDistance().getYear().getName()), 2, runRow);
						runnerTable.add(getText(localize(
								runner.getDistance().getName(), runner.getDistance()
								.getName())), 3, runRow);
						runnerTable.add(getText(runner.getPartner2Leg()), 4, runRow++);
						
						if (runner.getPartner3SSN() != null && !"".equals(runner.getPartner3SSN())) {
							runnerTable.add(getText(runner.getPartner3Name()), 1, runRow);
							runnerTable.add(getText(localize(runner.getRun().getName(),
									runner.getRun().getName())
									+ " " + runner.getDistance().getYear().getName()), 2, runRow);
							runnerTable.add(getText(localize(
									runner.getDistance().getName(), runner.getDistance()
									.getName())), 3, runRow);
							runnerTable.add(getText(runner.getPartner3Leg()), 4, runRow++);
						}
					}
				} else {
					runRow++;
				}
			} else {
				removeRunner(iwc, runner.getPersonalID());
			}
		}

		UIComponent buttonsContainer = getButtonsFooter(iwc, false, false);

		SubmitButton previous = getPreviousButton();
		SubmitButton registerOther = (SubmitButton) getButton(new SubmitButton(
				localize("run_reg.register_other", "Register other")));
		registerOther.setValueOnClick(PARAMETER_ACTION, String
				.valueOf(ACTION_START));
		registerOther.setValueOnClick(PARAMETER_PERSONAL_ID, "");
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize(
				"run_reg.pay", "Pay fee")));
		// next.setValueOnClick(PARAMETER_ACTION,
		// String.valueOf(ACTION_STEP_RECEIPT));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_NEXT));

		buttonsContainer.getChildren().add(previous);
		buttonsContainer.getChildren().add(registerOther);
		buttonsContainer.getChildren().add(next);

		form.add(buttonsContainer);

		add(form);
	}


	private void stepPayment(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_PAYMENT);
		
		form.add(getStepsHeader(iwc, ACTION_STEP_PAYMENT));
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		//table.add(getPhasesTable(this.isIcelandic ? 7 : 6, this.isIcelandic ? 8 : 6, "run_reg.payment_info", "Payment info"), 1, row++);
		//table.add(getStepsHeader(iwc, ACTION_STEP_PAYMENT),1,row++);
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
		
		System.out.println("Getting siblings, children and prices");
		
		int numberOfChildren = this.isIcelandic ? getRunBusiness(iwc).getNumberOfChildren(runners.values()) : 0;
		System.out.println("children = " + numberOfChildren);
		boolean useChildDiscount = false;
		if (numberOfChildren > 1 && numberOfChildren != runners.size()) {
			useChildDiscount = true;
		}
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
			
			System.out.println("childnumber = " + childNumber);
			runnerTable.add(getText(localize(runner.getRun().getName(), runner.getRun().getName())), 2, runRow);
			runnerTable.add(getText(localize(runner.getDistance().getName(), runner.getDistance().getName())), 3, runRow);
			float runPrice = getRunBusiness(iwc).getPriceForRunner(runner, iwc.getCurrentLocale(), this.chipDiscount, 0);
			totalAmount += runPrice;
			runnerTable.add(getText(formatAmount(iwc.getCurrentLocale(), runPrice)), 4, runRow++);
			if (useChildDiscount &&  childNumber > 1 && runPrice > 0) {
				runPrice -= this.childDiscount;
			}
			
			System.out.println("totalAmount = " + totalAmount);
			System.out.println("runPrice = " + runPrice);
			
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
			stepReceipt(iwc, false);
			return;
		}
		
		if (this.isIcelandic) {
			if (useChildDiscount) {
				float childrenDiscount = -((numberOfChildren - 1) * this.childDiscount);
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
			float totalTransport = transportToBuy * getRunner().getDistance().getPriceForTransport(iwc.getCurrentLocale());
			totalAmount += totalTransport;
			
			runnerTable.setHeight(runRow++, 12);
			runnerTable.mergeCells(1, runRow, 3, runRow);
			runnerTable.add(getText(transportToBuy + " x " + localize("run_reg.transport_to_race_starting_point", "Bus trip to race starting point and back again")), 1, runRow);
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
				if (image != null) {
					String imageURL = image.getURL();
					if (imageURL != null && !imageURL.equals("")) {
						image.setToolTip(imageURL.substring(imageURL.lastIndexOf('/')+1,imageURL.lastIndexOf('.')));
					}
				}
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
		if (getRunner().getUser() != null) {
			nameField.setContent(getRunner().getUser().getName());
		} else {
			nameField.setContent(getRunner().getName());
		}
		
		TextInput ccv = (TextInput) getStyledInterface(new TextInput(PARAMETER_CCV));
		ccv.setLength(3);
		ccv.setMaxlength(3);
		//ccv.setMininumLength(3, localize("run_reg.not_valid_ccv", "Not a valid CCV number"));
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
			TextInput cardNumber = (TextInput) getStyledInterfaceCreditcard(new TextInput(PARAMETER_CARD_NUMBER + "_" + a));
			if (a < 4) {
				cardNumber.setLength(4);
				cardNumber.setMaxlength(4);
				cardNumber.setNextInput(PARAMETER_CARD_NUMBER + "_" + (a+1));
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
		emailField.setContent(getRunner().getEmail());
		
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

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("run_reg.pay", "Pay fee")));
		//next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_RECEIPT));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_NEXT));
		next.setDisabled(true);

		CheckBox agree = getCheckBox(PARAMETER_AGREE + "_terms", Boolean.TRUE.toString());
		agree.setToEnableWhenChecked(next);
		agree.setToDisableWhenUnchecked(next);
		
		creditCardTable.setHeight(creditRow++, 12);
		creditCardTable.mergeCells(1, creditRow, creditCardTable.getColumns(), creditRow);
		creditCardTable.add(agree, 1, creditRow);
		creditCardTable.add(Text.getNonBrakingSpace(), 1, creditRow);
		creditCardTable.add(getHeader(localize("run_reg.agree_terms_and_conditions", "I agree to the terms and conditions")), 1, creditRow++);

//		SubmitButton previous = getPreviousButton();
		
		UIComponent buttonsContainer = getButtonsFooter(iwc,true,false);
//		buttonsContainer.getChildren().add(previous);
		buttonsContainer.getChildren().add(next);
		form.add(buttonsContainer);

		form.setToDisableOnSubmit(next, true);

		add(form);
	}
	

	private String formatAmount(IWContext iwc, float amount) {
		return NumberFormat.getInstance(iwc.getCurrentLocale()).format(amount);
	}

	private String formatAmount(Locale locale, float amount) {
		return NumberFormat.getInstance(locale).format(amount) + " "
				+ (this.isIcelandic ? "ISK" : "EUR");
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
			if (getRunner().getRun() == null) {
				getParentPage()
						.setAlertOnLoad(
								localize(
										"run_reg.session_has_expired_payment",
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
						amount, this.isIcelandic ? "ISK" : "EUR",
						referenceNumber);
			}
			Collection participants = getRunBusiness(iwc).saveParticipants(
					runners, email, hiddenCardNumber, amount, paymentStamp,
					iwc.getCurrentLocale(), isDisablePaymentAndOverviewSteps());
			if (doPayment) {
				getRunBusiness(iwc).finishPayment(properties);
			}
			iwc.removeSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);

			showReceipt(iwc, participants, amount, hiddenCardNumber,
					paymentStamp, doPayment);
		} catch (IDOCreateException ice) {
			getParentPage()
					.setAlertOnLoad(
							localize("run_reg.save_failed",
									"There was an error when trying to finish registration."));
			ice.printStackTrace();
			loadPreviousStep(iwc);
		} catch (CreditCardAuthorizationException ccae) {
			IWResourceBundle creditCardBundle = iwc.getIWMainApplication()
					.getBundle("com.idega.block.creditcard").getResourceBundle(
							iwc.getCurrentLocale());
			getParentPage().setAlertOnLoad(
					ccae.getLocalizedMessage(creditCardBundle));
			// ccae.printStackTrace();
			System.out
					.println("CreditCardAuthorizationException in Registration.stepReceipt");
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

		String greeting = localize("run_reg.hello_participant",
				"Dear participant");
		if (selectedRun != null) {
			if (this.isIcelandic) {
				greeting = selectedRun.getRunRegistrationReceiptGreeting();
			} else {
				greeting = selectedRun
						.getRunRegistrationReceiptGreetingEnglish();
			}
		}

		table.add(getHeader(greeting), 1, row++);
		table.setHeight(row++, 16);

		table.add(getText(localize("run_reg.payment_received",
				"You have registered for the following:")), 1, row++);
		table.setHeight(row++, 8);

		Table runnerTable = new Table(5, runners.size() + 3);
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		int col = 1;
		runnerTable.add(
				getHeader(localize("run_reg.runner_name", "Runner name")),
				col++, 1);
		runnerTable.add(getHeader(localize("run_reg.run", "Run")), col++, 1);
		runnerTable.add(getHeader(localize("run_reg.distance", "Distance")),
				col++, 1);
		if (!isHideRaceNumberColumn()) {
			runnerTable.add(getHeader(localize("run_reg.race_number",
					"Race number")), col++, 1);
		}
		if (!isHideShirtSize()) {
			runnerTable.add(getHeader(localize("run_reg.shirt_size",
					"Shirt size")), col++, 1);
		}
		table.add(runnerTable, 1, row++);
		int runRow = 2;
		int transportToBuy = 0;
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
			runnerTable.add(getText(localize(distance.getName(), distance
					.getName())), col++, runRow);
			if (!isHideRaceNumberColumn()) {
				runnerTable.add(getText(String.valueOf(participant
						.getParticipantNumber())), col++, runRow);
			}
			if (!isHideShirtSize()) {
				runnerTable.add(getText(localize("shirt_size."
						+ participant.getShirtSize(), participant
						.getShirtSize())), col++, runRow++);
			} else {
				runRow++;
			}
			if (participant.getTransportOrdered().equalsIgnoreCase(
					Boolean.TRUE.toString())) {
				transportToBuy++;
			}
			
			if (participant.getRelayPartner1SSN() != null && !"".equals(participant.getRelayPartner1SSN())) {
				col = 1;
				runnerTable.add(getText(participant.getRelayPartner1Name()), col++, runRow);
				runnerTable.add(getText(localize(run.getName(), run.getName())
						+ " " + participant.getRunYearGroup().getName()), col++,
						runRow);
				runnerTable.add(getText(localize(distance.getName(), distance
						.getName())), col++, runRow);
				if (!isHideRaceNumberColumn()) {
					runnerTable.add(getText(String.valueOf(participant
							.getParticipantNumber())), col++, runRow);
				}
				if (!isHideShirtSize()) {
					runnerTable.add(getText(localize("shirt_size."
							+ participant.getRelayPartner1ShirtSize(), participant.getRelayPartner1ShirtSize())), col++, runRow++);
				} else {
					runRow++;
				}

				if (participant.getRelayPartner2SSN() != null && !"".equals(participant.getRelayPartner2SSN())) {
					col = 1;
					runnerTable.add(getText(participant.getRelayPartner2Name()), col++, runRow);
					runnerTable.add(getText(localize(run.getName(), run.getName())
							+ " " + participant.getRunYearGroup().getName()), col++,
							runRow);
					runnerTable.add(getText(localize(distance.getName(), distance
							.getName())), col++, runRow);
					if (!isHideRaceNumberColumn()) {
						runnerTable.add(getText(String.valueOf(participant
								.getParticipantNumber())), col++, runRow);
					}
					if (!isHideShirtSize()) {
						runnerTable.add(getText(localize("shirt_size."
								+ participant.getRelayPartner2ShirtSize(), participant.getRelayPartner2ShirtSize())), col++, runRow++);
					} else {
						runRow++;
					}
					
					if (participant.getRelayPartner3SSN() != null && !"".equals(participant.getRelayPartner3SSN())) {
						col = 1;
						runnerTable.add(getText(participant.getRelayPartner3Name()), col++, runRow);
						runnerTable.add(getText(localize(run.getName(), run.getName())
								+ " " + participant.getRunYearGroup().getName()), col++,
								runRow);
						runnerTable.add(getText(localize(distance.getName(), distance
								.getName())), col++, runRow);
						if (!isHideRaceNumberColumn()) {
							runnerTable.add(getText(String.valueOf(participant
									.getParticipantNumber())), col++, runRow);
						}
						if (!isHideShirtSize()) {
							runnerTable.add(getText(localize("shirt_size."
									+ participant.getRelayPartner3ShirtSize(), participant.getRelayPartner3ShirtSize())), col++, runRow++);
						} else {
							runRow++;
						}
					}
				}
			}
		}
		if (transportToBuy > 0) {
			runRow++;
			runnerTable.mergeCells(1, runRow, 5, runRow);
			runnerTable.add(getText(transportToBuy
					+ " x "
					+ localize("run_reg.transport_to_race_starting_point",
							"Bus trip to race starting point and back again")),
					1, runRow);
		}

		if (doPayment) {
			Table creditCardTable = new Table(2, 3);
			creditCardTable.add(getHeader(localize(
					"run_reg.payment_received_timestamp", "Payment received")
					+ ":"), 1, 1);
			creditCardTable.add(
					getText(paymentStamp.getLocaleDateAndTime(iwc
							.getCurrentLocale(), IWTimestamp.SHORT,
							IWTimestamp.SHORT)), 2, 1);
			creditCardTable.add(getHeader(localize("run_reg.card_number",
					"Card number")
					+ ":"), 1, 2);
			creditCardTable.add(getText(cardNumber), 2, 2);
			creditCardTable.add(getHeader(localize("run_reg.amount", "Amount")
					+ ":"), 1, 3);
			creditCardTable.add(getText(formatAmount(iwc.getCurrentLocale(),
					(float) amount)), 2, 3);
			table.setHeight(row++, 16);
			table.add(creditCardTable, 1, row++);
		}

		table.setHeight(row++, 16);
		table.add(getHeader(localize("run_reg.receipt_info_headline",
				"Receipt - Please print it out")), 1, row++);
		table
				.add(
						getText(localizeForRun(
								"run_reg.receipt_info_headline_body",
								"This document is your receipt, please print it out and bring it with you when you collect your race material.")),
						1, row++);

		if (selectedRun != null) {
			table.setHeight(row++, 16);
			table.add(getHeader(localizeForRun(
					"run_reg.delivery_of_race_material_headline",
					"Further information about the run is available on:")), 1,
					row++);
			String informationText;
			if (this.isIcelandic) {
				informationText = selectedRun.getRunRegistrationReceiptInfo();
			} else {
				informationText = selectedRun
						.getRunRegistrationReceiptInfoEnglish();
			}
			table.add(getText(informationText), 1, row++);
		}

		table.setHeight(row++, 16);
		table.add(getText(localize("run_reg.best_regards", "Best regards,")),
				1, row++);

		if (selectedRun != null) {
			// table.add(getText(localize(selectedRun.getName(), selectedRun
			// .getName())), 1, row++);
			table.add(getText(selectedRun.getRunHomePage()), 1, row++);
		}

		add(table);

		if (!isHidePrintviewLink()) {
			Link print = new Link(localize("print", "Print receipt"));
			print.setPublicWindowToOpen(RegistrationReceivedPrintable.class);

			UIComponent buttonsContainer = getButtonsFooter(iwc, false, false);
			buttonsContainer.getChildren().add(print);
			add(buttonsContainer);

		}

	}

	private void cancel(IWContext iwc) {
		iwc.removeSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
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
		String personalID = iwc.getParameter(PARAMETER_PERSONAL_ID);
		if (personalID != null && personalID.length() > 0) {
			Runner runner = getRunner(iwc, personalID);
			if (runner == null) {
				runner = new Runner();
				runner.setPersonalID(personalID);
				if (this.isIcelandic) {
					User user = null;
					try {
						user = getUserBusiness(iwc).getUser(personalID);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					runner.setUser(user);
				}
			}

			if (!this.isIcelandic) {
				IWTimestamp dateOfBirth = new IWTimestamp(personalID);
				runner.setDateOfBirth(dateOfBirth.getDate());
			}
			if (iwc.isParameterSet(PARAMETER_RUN)) {
				String runId = iwc.getParameter(PARAMETER_RUN);
				runner.setRunId(runId);
			}
			if (iwc.isParameterSet(PARAMETER_DISTANCE)) {
				runner.setDistance(ConverterUtility.getInstance()
						.convertGroupToDistance(
								new Integer(iwc
										.getParameter(PARAMETER_DISTANCE))));
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
					runner.setCountry(getUserBusiness(iwc).getAddressBusiness()
							.getCountryHome().findByPrimaryKey(
									new Integer(iwc
											.getParameter(PARAMETER_COUNTRY))));
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
					runner
							.setNationality(getUserBusiness(iwc)
									.getAddressBusiness()
									.getCountryHome()
									.findByPrimaryKey(
											new Integer(
													iwc
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
			if (iwc.isParameterSet(PARAMETER_CHIP)) {
				String chip = iwc.getParameter(PARAMETER_CHIP);
				if (chip.equals(IWMarathonConstants.CHIP_RENT)) {
					runner.setRentChip(true);
				} else if (chip.equals(IWMarathonConstants.CHIP_BUY)) {
					runner.setBuyChip(true);
				} else if (chip.equals(IWMarathonConstants.CHIP_OWN)) {
					runner.setOwnChip(true);
				}
			}
			if (iwc.isParameterSet(PARAMETER_CHIP_NUMBER)) {
				runner.setChipNumber(iwc.getParameter(PARAMETER_CHIP_NUMBER));
			}
			/*
			 * if (iwc.isParameterSet(PARAMETER_TRANSPORT)) { String transport =
			 * iwc.getParameter(PARAMETER_TRANSPORT); if
			 * (transport.equals(Boolean.TRUE.toString())) {
			 * runner.setTransportOrdered(true); } else if
			 * (transport.equals(Boolean.FALSE.toString())){
			 * runner.setNoTransportOrdered(true); } }
			 */
			if (iwc.isParameterSet(PARAMETER_QUESTION1_HOUR)) {
				runner.setQuestion1Hour(iwc
						.getParameter(PARAMETER_QUESTION1_HOUR));
			}
			if (iwc.isParameterSet(PARAMETER_QUESTION1_MINUTE)) {
				runner.setQuestion1Minute(iwc
						.getParameter(PARAMETER_QUESTION1_MINUTE));
			}
			if (iwc.isParameterSet(PARAMETER_QUESTION2_HOUR)) {
				runner.setQuestion2Hour(iwc
						.getParameter(PARAMETER_QUESTION2_HOUR));
			}
			if (iwc.isParameterSet(PARAMETER_QUESTION2_MINUTE)) {
				runner.setQuestion2Minute(iwc
						.getParameter(PARAMETER_QUESTION2_MINUTE));
			}
			if (iwc.isParameterSet(PARAMETER_QUESTION3_HOUR)) {
				runner.setQuestion3Hour(iwc
						.getParameter(PARAMETER_QUESTION3_HOUR));
			}
			if (iwc.isParameterSet(PARAMETER_QUESTION3_MINUTE)) {
				runner.setQuestion3Minute(iwc
						.getParameter(PARAMETER_QUESTION3_MINUTE));
			}
			if (iwc.isParameterSet(PARAMETER_QUESTION1_YEAR)) {
				runner.setQuestion1Year(iwc
						.getParameter(PARAMETER_QUESTION1_YEAR));
			}
			if (iwc.isParameterSet(PARAMETER_QUESTION3_YEAR)) {
				runner.setQuestion3Year(iwc
						.getParameter(PARAMETER_QUESTION3_YEAR));
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

			if (iwc.isParameterSet(PARAMETER_ALLOW_EMAILS)) {
				String allowEmails = iwc.getParameter(PARAMETER_ALLOW_EMAILS);
				if (allowEmails.equals(Boolean.TRUE.toString())) {
					runner.setAllowsEmails(true);
				} else if (allowEmails.equals(Boolean.FALSE.toString())) {
					runner.setNotAllowsEmails(true);
				}
			}
			if (iwc.isParameterSet(PARAMETER_AGREE)) {
				runner.setAgree(true);
			}
			if (iwc.isParameterSet(PARAMETER_ACCEPT_CHARITY)) {
				String participate = iwc.getParameter(PARAMETER_ACCEPT_CHARITY);
				if (participate.equals(Boolean.TRUE.toString())) {
					runner.setParticipateInCharity(true);
				} else if (participate.equals(Boolean.FALSE.toString())) {
					runner.setParticipateInCharity(false);
				}
			} else {
				if (iwc.isParameterSet(PARAMETER_NOT_ACCEPT_CHARITY)) {
					String notParticipate = iwc
							.getParameter(PARAMETER_NOT_ACCEPT_CHARITY);
					if (notParticipate.equals(Boolean.TRUE.toString())) {
						runner.setParticipateInCharity(false);
					}
				}
			}
			/*
			 * else{ runner.setParticipateInCharity(false); }
			 */
			if (iwc.isParameterSet(PARAMETER_CHARITY_ID)) {
				String organizationalId = iwc
						.getParameter(PARAMETER_CHARITY_ID);
				try {
					CharityHome cHome = (CharityHome) IDOLookup
							.getHome(Charity.class);
					if ((organizationalId != null)
							&& (!organizationalId.equals("-1"))) {
						Charity charity = cHome
								.findCharityByOrganizationalId(organizationalId);
						runner.setCharity(charity);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (iwc.isParameterSet(PARAMETER_ALLOW_CONTACT)) {
				String allowContact = iwc.getParameter(PARAMETER_ALLOW_CONTACT);
				if (allowContact.equals(Boolean.TRUE.toString())) {
					runner.setMaySponsorContactRunner(true);
				} else if (allowContact.equals(Boolean.FALSE.toString())) {
					runner.setMaySponsorContactRunner(false);
				}
			}
			if (iwc.isParameterSet(PARAMETER_CATEGORY_ID)) {
				String runCategorylId = iwc.getParameter(PARAMETER_CATEGORY_ID);
				try {
					RunCategoryHome catHome = (RunCategoryHome) IDOLookup
							.getHome(RunCategory.class);
					if ((runCategorylId != null)
							&& (!runCategorylId.equals("-1"))) {
						RunCategory category = catHome
								.findByPrimaryKey(new Integer(runCategorylId));
						runner.setCategory(category);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (iwc.isParameterSet(PARAMETER_APPLY_DOMESTIC_TRAVEL_SUPPORT)) {
				String apply = iwc
						.getParameter(PARAMETER_APPLY_DOMESTIC_TRAVEL_SUPPORT);
				if (apply.equals(Boolean.TRUE.toString())) {
					runner.setApplyForDomesticTravelSupport(true);
				} else if (apply.equals(Boolean.FALSE.toString())) {
					runner.setApplyForDomesticTravelSupport(false);
				}
			}
			if (iwc
					.isParameterSet(PARAMETER_APPLY_INTERNATIONAL_TRAVEL_SUPPORT)) {
				String apply = iwc
						.getParameter(PARAMETER_APPLY_INTERNATIONAL_TRAVEL_SUPPORT);
				if (apply.equals(Boolean.TRUE.toString())) {
					runner.setApplyForInternationalTravelSupport(true);
				} else if (apply.equals(Boolean.FALSE.toString())) {
					runner.setApplyForInternationalTravelSupport(false);
				}
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_LEG)) {
				runner.setRelayLeg(iwc.getParameter(PARAMETER_RELAY_LEG));
			}
			
			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_1_SSN)) {
				runner.setPartner1SSN(iwc.getParameter(PARAMETER_RELAY_PARTNER_1_SSN));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_1_NAME)) {
				runner.setPartner1Name(iwc.getParameter(PARAMETER_RELAY_PARTNER_1_NAME));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_1_EMAIL)) {
				runner.setPartner1Email(iwc.getParameter(PARAMETER_RELAY_PARTNER_1_EMAIL));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_1_SHIRT_SIZE)) {
				runner.setPartner1ShirtSize(iwc.getParameter(PARAMETER_RELAY_PARTNER_1_SHIRT_SIZE));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_1_LEG)) {
				runner.setPartner1Leg(iwc.getParameter(PARAMETER_RELAY_PARTNER_1_LEG));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_2_SSN)) {
				runner.setPartner2SSN(iwc.getParameter(PARAMETER_RELAY_PARTNER_2_SSN));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_2_NAME)) {
				runner.setPartner2Name(iwc.getParameter(PARAMETER_RELAY_PARTNER_2_NAME));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_2_EMAIL)) {
				runner.setPartner2Email(iwc.getParameter(PARAMETER_RELAY_PARTNER_2_EMAIL));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_2_SHIRT_SIZE)) {
				runner.setPartner2ShirtSize(iwc.getParameter(PARAMETER_RELAY_PARTNER_2_SHIRT_SIZE));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_2_LEG)) {
				runner.setPartner2Leg(iwc.getParameter(PARAMETER_RELAY_PARTNER_2_LEG));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_3_SSN)) {
				runner.setPartner3SSN(iwc.getParameter(PARAMETER_RELAY_PARTNER_3_SSN));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_3_NAME)) {
				runner.setPartner3Name(iwc.getParameter(PARAMETER_RELAY_PARTNER_3_NAME));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_3_EMAIL)) {
				runner.setPartner3Email(iwc.getParameter(PARAMETER_RELAY_PARTNER_3_EMAIL));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_3_SHIRT_SIZE)) {
				runner.setPartner3ShirtSize(iwc.getParameter(PARAMETER_RELAY_PARTNER_3_SHIRT_SIZE));
			}

			if (iwc.isParameterSet(PARAMETER_RELAY_PARTNER_3_LEG)) {
				runner.setPartner3Leg(iwc.getParameter(PARAMETER_RELAY_PARTNER_3_LEG));
			}
			
			addRunner(iwc, personalID, runner);
			return runner;
		}
		Runner runner = new Runner();
		return runner;
	}

	/**
	 * Called by StepsBlock
	 */
	protected void initializeSteps(IWContext iwc) {

		if (isIcelandic) {
			addStep(iwc, ACTION_STEP_PERSONLOOKUP, localize(
					"run_reg.registration", "Registration"));
		}
		addStep(iwc, ACTION_STEP_PERSONALDETAILS, localize(
				"run_reg.registration", "Registration"));
		addStep(iwc, ACTION_STEP_RUNDETAILS, localize("run_reg.run_details",
				"Run details"));
		Runner runner = null;
		try {
			runner = getRunner();
		} catch (RuntimeException e) {
			// runner not found by Personal ID
		}
		
		Distance dist = null;
		if (runner != null) {
			dist = runner.getDistance();
			if (dist != null) {
				if (dist.isUseChip()) {
					addStep(iwc, ACTION_STEP_CHIP, localize(
							"run_reg.time_registration_chip",
							"Time registration chip"));
				}
				// if(dist.isTransportOffered()){
				// addStep(iwc,ACTION_STEP_TRANSPORT,localize("run_reg.order_transport",
				// "Order a bus trip"));
				// }
				if (dist.isAskQuestions()) {
					addStep(iwc, ACTION_STEP_QUESTIONS, localize(
							"run_reg.questions",
							"Answer the following questions."));
				}

				if (dist != null) {
					if (dist.isRelayDistance()) {
						addStep(iwc, ACTION_STEP_RELAY, localize("run_reg.relay_setup", "Setup relay team"));
					}			
				}
			}
		}

		if (isCharityStepEnabled(iwc.getLocale())) {
			// if(isIcelandic){
			if (runner != null) {
				Year year = runner.getYear();
				if (year != null) {
					if (year.isCharityEnabled()) {
						addStep(iwc, ACTION_STEP_CHARITY, localize(
								"run_reg.charity", "Select charity"));
					}
				}
			}
			// }
		}
		
		
		if (this.isEnableTravelSupport()) {
			addStep(iwc, ACTION_STEP_TRAVELSUPPORT, localize(
					"run_reg.travelsupport", "Travel support"));
		}
		addStep(iwc, ACTION_STEP_DISCLAIMER, localize("run_reg.consent",
				"Consent"));
		if (!isDisablePaymentAndOverviewSteps()) {
			addStep(iwc, ACTION_STEP_OVERVIEW, localize("run_reg.overview",
					"Overview"));
		}
		if (!isDisablePaymentAndOverviewSteps()) {
			addStep(iwc, ACTION_STEP_PAYMENT, localize("run_reg.payment_info",
					"Payment info"));
		}
		addStep(iwc, ACTION_STEP_RECEIPT,
				localize("run_reg.receipt", "Receipt"));
	}

	protected void initializeSetRuns(IWContext iwc) {

		if (iwc.isParameterSet(PARAMETER_LIMIT_RUN_IDS)) {
			String runIds = iwc.getParameter(PARAMETER_LIMIT_RUN_IDS);
			setRunIds(runIds);
		}
		if (isConstrainedToOneRun()) {
			String runId = getRunIds();
			getRunner().setRunId(runId);
		}
	}

	protected int parseAction(IWContext iwc) throws RemoteException {
		Runner runner = null;
		try {
			runner = getRunner();
		} catch (RuntimeException fe) {
			getParentPage().setAlertOnLoad(
					localize("run_reg.user_not_found_for_personal_id",
							"No user found with personal ID."));
			// action = ACTION_STEP_PERSONLOOKUP;
			initializeSteps(iwc);
			return ACTION_STEP_PERSONLOOKUP;
		}

		if (runner != null && runner.getDateOfBirth() != null || runner != null
				&& runner.getUser() != null
				&& runner.getUser().getDateOfBirth() != null) {
			Date dateOfBirth;
			if (runner.getDateOfBirth() != null) {
				dateOfBirth = runner.getDateOfBirth();
			} else {
				dateOfBirth = runner.getUser().getDateOfBirth();
			}
			long ageInMillisecs = IWTimestamp.getMilliSecondsBetween(
					new IWTimestamp(dateOfBirth), new IWTimestamp());
			BigDecimal ageObject = new BigDecimal(ageInMillisecs
					/ MILLISECONDS_IN_YEAR);
			int age = ageObject.intValue();
			if (runner.getYear() != null) {
				int maximumAgeForRun = runner.getYear().getMaximumAgeForRun();
				if (maximumAgeForRun != -1 && age > maximumAgeForRun) {
					Object[] args = { String.valueOf(maximumAgeForRun) };
					getParentPage()
							.setAlertOnLoad(
									MessageFormat
											.format(
													localize(
															"run_reg.invalid_date_of_birth_exeeding",
															"Invalid date of birth.  You have to be {0} or younger to register"),
													args));
					// initializeSteps(iwc);
					return ACTION_STEP_PERSONALDETAILS;
				}
			}
		}

		if (runner != null && runner.getEmail() != null
				&& runner.getEmail2() != null) {
			if (!runner.getEmail().equals(runner.getEmail2())) {
				getParentPage()
						.setAlertOnLoad(
								localize("run_reg.email_dont_match",
										"Emails do not match. Please type the same email in both email inputs"));
				return ACTION_STEP_PERSONALDETAILS;
			}
		}

		initializeSetRuns(iwc);

		return super.parseAction(iwc);
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

	private void stepCharity(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_QUESTION1_NEVER);
		form.maintainParameter(PARAMETER_QUESTION3_NEVER);

		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_CHARITY);

		form.add(getStepsHeader(iwc, ACTION_STEP_CHARITY));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		Script script = new Script();
		add(script);
		script
				.addFunction(
						"toggleCharitySelection",
						"function toggleCharitySelection(){ var checkbox = findObj('"
								+ PARAMETER_ACCEPT_CHARITY
								+ "');  var hiddencheck = findObj('"
								+ PARAMETER_NOT_ACCEPT_CHARITY
								+ "'); if(checkbox.checked){ hiddencheck.value='false';}else if(!checkbox.checked){ hiddencheck.value='true';}  }");

		table.setHeight(row++, 12);

		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(
				localize("previous", "Previous")));

		String previousActionValue = String.valueOf(ACTION_PREVIOUS);
		previous.setValueOnClick(PARAMETER_ACTION, previousActionValue);
		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize(
				"next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_NEXT));

		table
				.add(
						new Text(
								localize(
										"run_reg.charity_headertext",
										"Now every runner can run for a good cause for a charity of his/her choice. It is now possible to search among all the runners that have registered and make a pledge.")),
						1, row++);

		Runner runner = getRunner();
		DropdownMenu charities = (CharitiesForRunDropDownMenu) (getStyledInterface(new CharitiesForRunDropDownMenu(
				PARAMETER_CHARITY_ID, (Integer) runner.getYear()
						.getPrimaryKey())));
		charities.setWidth("300");

		Layer acceptCharityDiv = new Layer(Layer.DIV);
		CheckBox acceptCharityCheck = getCheckBox(PARAMETER_ACCEPT_CHARITY,
				Boolean.TRUE.toString());
		acceptCharityCheck.setChecked(true);
		acceptCharityCheck.setToEnableWhenChecked(charities);
		acceptCharityCheck.setToDisableWhenUnchecked(charities);

		HiddenInput notAcceptCharityCheck = new HiddenInput(
				PARAMETER_NOT_ACCEPT_CHARITY);

		acceptCharityCheck.setOnClick("toggleCharitySelection();");
		// acceptCharityCheck.setOnChange(action)
		Label accepCharityLabel = new Label(
				localize(
						"run_reg.agree_charity_participation",
						"I agree to participate in running for a charity and searchable by others in a pledge form"),
				acceptCharityCheck);
		acceptCharityDiv.add(acceptCharityCheck);
		acceptCharityDiv.add(accepCharityLabel);
		acceptCharityDiv.add(notAcceptCharityCheck);
		table.setHeight(row++, 18);
		table.add(acceptCharityDiv, 1, row++);

		acceptCharityCheck.setChecked(getRunner().isParticipateInCharity());
		notAcceptCharityCheck.setValue(new Boolean(!getRunner()
				.isParticipateInCharity()).toString());

		if (isHideCharityCheckbox()) {
			acceptCharityDiv.setVisible(false);
		}

		table.setHeight(row++, 18);
		table.add(charities, 1, row++);
		table.setHeight(row++, 18);

		Distance distance = runner.getDistance();
		Year year = distance.getYear();

		if (year.isSponsoredRun()) {
			Layer charityEnquiryDiv = new Layer(Layer.DIV);
			Text charityEnquiryText = new Text(
					localize(
							"run_reg.charity_enquiry",
							"If you charity organization is not on the list, please send enquiry to godgerdarmal@glitnir.is"));
			charityEnquiryDiv.add(charityEnquiryText);
			table.add(charityEnquiryDiv, 1, row++);
		}

		int pledgePerKilometerISK;
		if (isSponsoredRegistration()) {
			runner.setSponsoredRunner(true);
			pledgePerKilometerISK = year.getPledgedBySponsorGroupPerKilometer();
		} else {
			pledgePerKilometerISK = year.getPledgedBySponsorPerKilometer();
		}
		int kilometersRun = getRunner().getDistance().getDistanceInKms();
		int totalPledgedISK = pledgePerKilometerISK * kilometersRun;

		String localizedString;
		if (isSponsoredRegistration()) {
			localizedString = localize(
					"run_reg.charity_sponsortext_staff",
					"The sponsor will pay {0} {2} to the selected charity organization for each kilometer run. The sponsor will pay total of {1} {2} to the selected charity organization for your participation.");
		} else {
			localizedString = localizeForRun(
					"run_reg.charity_sponsortext_general",
					"The sponsor will pay {0} {2} to the selected charity organization for each kilometer run. The sponsor will pay total of {1} {2} to the selected charity organization for your participation.");
		}
		String[] attributes = { formatAmount(iwc, pledgePerKilometerISK),
				formatAmount(iwc, totalPledgedISK), year.getPledgeCurrency() };
		table.setHeight(row++, 12);
		table.add(new Text(MessageFormat.format(localizedString, attributes)),
				1, row++);

		table.add(new Text(localize("run_reg.charity_footer_info",
									"You can select a charity later on your pages.")), 1, row++);
		table.setHeight(row++, 12);

		Layer infoLayer = new Layer(Layer.DIV);
		Text infoText = new Text(localize("", ""));

		UIComponent buttonsContainer = getButtonsFooter(iwc);
		form.add(buttonsContainer);

		add(form);

		String selectCharitiesMessage = localize("run_reg.must_select_charity",
				"Please select a valid charity");
		charities
				.setOnSubmitFunction(
						"checkCharities",
						"function checkCharities(){ var checkbox = findObj('"
								+ PARAMETER_ACCEPT_CHARITY
								+ "'); var charities = findObj('"
								+ PARAMETER_CHARITY_ID
								+ "');  if(checkbox.checked){if(charities.options[charities.selectedIndex].value=='-1'){ alert('"
								+ selectCharitiesMessage
								+ "'); return false;} } return true;}");

		if (getRunner().isParticipateInCharity()) {
			Charity charity = getRunner().getCharity();
			if (charity != null) {
				charities.setSelectedElement(getRunner().getCharity()
						.getOrganizationalID());
			}
		} else {
			charities.setDisabled(true);
		}

	}

	private String localizeForRun(String key, String value) {
		Runner runner = null;
		try {
			runner = getRunner();
		} catch (Exception e) {
			// unable to initialize runner, using default value
		}
		if (runner != null && runner.getRun() != null) {
			String runKey = key + "_runid_" + getRunner().getRun().getId();
			String localizedString = getResourceBundle().getLocalizedString(
					runKey);
			if (localizedString == null) {
				localizedString = getResourceBundle().getLocalizedString(key,
						value);
			}
			return localizedString;
		} else {
			return getResourceBundle().getLocalizedString(key, value);
		}

	}

	public boolean isDisablePaymentAndOverviewSteps() {
		return disablePaymentAndOverviewSteps;
	}

	public void setDisablePaymentAndOverviewSteps(
			boolean disablePaymentAndOverviewSteps) {
		this.disablePaymentAndOverviewSteps = disablePaymentAndOverviewSteps;
	}

	public String getRunIds() {
		return runIds;
	}

	public void setRunIds(String runIds) {
		this.runIds = runIds;
		if (runIds.indexOf(",") != -1) {

		} else {
			setConstrainedToOneRun(runIds);
		}
	}

	public String[] getRunIdsArray() {
		String runIds = getRunIds();
		if (runIds != null) {
			if (runIds.indexOf(",") != -1) {
				StringTokenizer tokenizer = new StringTokenizer(runIds, ",");
				List list = new ArrayList();
				while (tokenizer.hasMoreElements()) {
					list.add(tokenizer.nextElement());
				}
				return (String[]) list.toArray(new String[0]);
			} else {
				String[] array = { runIds };
				return array;
			}
		}

		return null;
	}

	protected String getConstrainedToOneRun() {
		return this.constrainedToOneRun;
	}

	protected void setConstrainedToOneRun(String runId) {
		// getRunner().setRunId(runId);
		this.constrainedToOneRun = runId;
	}

	protected boolean isConstrainedToOneRun() {
		return (this.constrainedToOneRun != null);
	}

	public void setPresetCountries(String countryList) {
		this.presetCountries = countryList;
	}

	public String getPresetCountries() {
		return this.presetCountries;
	}

	public String[] getPresetCountriesArray() {
		String ids = getPresetCountries();

		if (ids != null) {
			if (ids.indexOf(",") != -1) {
				StringTokenizer tokenizer = new StringTokenizer(ids, ",");
				List list = new ArrayList();
				while (tokenizer.hasMoreElements()) {
					list.add(tokenizer.nextElement());
				}
				return (String[]) list.toArray(new String[0]);
			} else {
				String[] array = { ids };
				return array;
			}
		}

		return null;
	}

	public void setCharityStepEnabledForForeignLocale(boolean enable) {
		this.charityStepEnabledForForeignLocale = enable;
	}

	public boolean isCharityStepEnabledForForeignLocale() {
		return this.charityStepEnabledForForeignLocale;
	}

	protected boolean isCharityStepEnabled(Locale locale) {
		if (LocaleUtil.getIcelandicLocale().equals(locale)) {
			return true;
		} else {
			return isCharityStepEnabledForForeignLocale();
		}
	}

	public boolean isHideCharityCheckbox() {
		return hideCharityCheckbox;
	}

	public void setHideCharityCheckbox(boolean hideCharityCheckbox) {
		this.hideCharityCheckbox = hideCharityCheckbox;
	}

	public boolean isDisableSponsorContactCheck() {
		return disableSponsorContactCheck;
	}

	public void setDisableSponsorContactCheck(boolean disableSponsorCheck) {
		this.disableSponsorContactCheck = disableSponsorCheck;
	}

	public boolean isShowCategories() {
		return this.showCategories;
	}

	public void setShowCategories(boolean showCategories) {
		this.showCategories = showCategories;
	}

	public boolean isDisableChipBuy() {
		return disableChipBuy;
	}

	public void setDisableChipBuy(boolean disableChipBuy) {
		this.disableChipBuy = disableChipBuy;
	}

	protected void stepTravelsupport(IWContext iwc) {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PERSONAL_ID);
		form.maintainParameter(PARAMETER_QUESTION1_NEVER);
		form.maintainParameter(PARAMETER_QUESTION3_NEVER);

		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_TRAVELSUPPORT);

		form.add(getStepsHeader(iwc, ACTION_STEP_TRAVELSUPPORT));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		// Script script = new Script();
		// add(script);
		// script.addFunction("toggleCharitySelection",
		// "function toggleCharitySelection(){ var checkbox = findObj('"+PARAMETER_ACCEPT_CHARITY+"');  var hiddencheck = findObj('"+PARAMETER_NOT_ACCEPT_CHARITY+"'); if(checkbox.checked){ hiddencheck.value='false';}else if(!checkbox.checked){ hiddencheck.value='true';}  }");

		// table.add(getPhasesTable(this.isIcelandic ? 4 : 3, this.isIcelandic ?
		// 8 : 6, "run_reg.charity", "Charity"), 1, row++);
		// table.add(getStepsHeader(iwc, ACTION_STEP_TRAVELSUPPORT),1,row++);
		table.setHeight(row++, 12);

		table.add(getText(localizeForRun(
				"run_reg.travelsupport_informationtext",
				"The sponsor will grant two types of travel support")), 1,
				row++);

		Runner runner = getRunner();

		Layer applyDomesticDiv = new Layer(Layer.DIV);
		CheckBox applyDomesticCheck = getCheckBox(
				PARAMETER_APPLY_DOMESTIC_TRAVEL_SUPPORT, Boolean.TRUE
						.toString());
		Label applyDomesticLabel = new Label(localize(
				"run_reg.travelsupport_apply_domestic",
				"Apply for domestic travel support"), applyDomesticCheck);
		applyDomesticDiv.add(applyDomesticCheck);
		applyDomesticDiv.add(applyDomesticLabel);
		table.add(applyDomesticDiv, 1, row++);
		applyDomesticCheck.setChecked(runner.isApplyForDomesticTravelSupport());

		Layer applyInternationalDiv = new Layer(Layer.DIV);
		CheckBox applyInternationalCheck = getCheckBox(
				PARAMETER_APPLY_INTERNATIONAL_TRAVEL_SUPPORT, Boolean.TRUE
						.toString());
		Label applyInternationalLabel = new Label(localize(
				"run_reg.travelsupport_apply_international",
				"Apply for international travel support"), applyDomesticCheck);
		applyInternationalDiv.add(applyInternationalCheck);
		applyInternationalDiv.add(applyInternationalLabel);
		table.add(applyInternationalDiv, 1, row++);
		applyInternationalCheck.setChecked(runner
				.isApplyForInternationalTravelSupport());

		UIComponent buttonsContainer = getButtonsFooter(iwc);
		form.add(buttonsContainer);

		add(form);

	}

	public void setEnableTravelSupport(boolean enableTravelSupport) {
		this.enableTravelSupport = enableTravelSupport;
	}

	public boolean isEnableTravelSupport() {
		return enableTravelSupport;
	}

	public void setSponsoredRegistration(boolean sponsoredRegistration) {
		this.sponsoredRegistration = sponsoredRegistration;
	}

	public boolean isSponsoredRegistration() {
		return sponsoredRegistration;
	}

	public void setHideShirtSize(boolean hideShirtSize) {
		this.hideShirtSize = hideShirtSize;
	}

	public boolean isHideShirtSize() {
		return hideShirtSize;
	}

	public void setHideRaceNumberColumn(boolean hideRaceNumberColumn) {
		this.hideRaceNumberColumn = hideRaceNumberColumn;
	}

	public boolean isHideRaceNumberColumn() {
		return hideRaceNumberColumn;
	}

	public void setHidePrintviewLink(boolean hidePrintviewLink) {
		this.hidePrintviewLink = hidePrintviewLink;
	}

	public boolean isHidePrintviewLink() {
		return hidePrintviewLink;
	}

	public void setShowAllThisYear(boolean showAllThisYear) {
		this.showAllThisYear = showAllThisYear;
	}

	public boolean getShowAllThisYear() {
		return this.showAllThisYear;
	}
}