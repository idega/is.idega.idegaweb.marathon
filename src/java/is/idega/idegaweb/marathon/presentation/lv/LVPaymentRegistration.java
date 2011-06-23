package is.idega.idegaweb.marathon.presentation.lv;

import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.presentation.PaymentReceivedPrintable;
import is.idega.idegaweb.marathon.presentation.Registration;
import is.idega.idegaweb.marathon.presentation.RunBlock;

import java.rmi.RemoteException;
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
import com.idega.data.IDOCreateException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.text.SocialSecurityNumber;

public class LVPaymentRegistration extends RunBlock {

	public static final String SESSION_ATTRIBUTE_PARTICIPANTS = Registration.SESSION_ATTRIBUTE_PARTICIPANTS;
	public static final String SESSION_ATTRIBUTE_AMOUNT = Registration.SESSION_ATTRIBUTE_AMOUNT;
	public static final String SESSION_ATTRIBUTE_CARD_NUMBER = Registration.SESSION_ATTRIBUTE_CARD_NUMBER;
	public static final String SESSION_ATTRIBUTE_PAYMENT_DATE = Registration.SESSION_ATTRIBUTE_PAYMENT_DATE;

	private static final String PARAMETER_PARTICIPANT_ID = "prm_participant_id";

	private static final String PARAMETER_AGREE = "prm_agree";

	private static final String PARAMETER_REFERENCE_NUMBER = "prm_reference_number";
	private static final String PARAMETER_NAME_ON_CARD = "prm_name_on_card";
	private static final String PARAMETER_CARD_NUMBER = "prm_card_number";
	private static final String PARAMETER_EXPIRES_MONTH = "prm_expires_month";
	private static final String PARAMETER_EXPIRES_YEAR = "prm_expires_year";
	private static final String PARAMETER_CCV = "prm_ccv";
	private static final String PARAMETER_AMOUNT = "prm_amount";
	private static final String PARAMETER_CARD_HOLDER_EMAIL = "prm_card_holder_email";

	private static final int ACTION_STEP_PARTICIPANTLOOKUP = 10;
	private static final int ACTION_STEP_DISCLAIMER = 20;
	private static final int ACTION_STEP_PAYMENT = 30;
	private static final int ACTION_STEP_RECEIPT = 40;
	private static final int ACTION_CANCEL = 50;

	private boolean isIcelandic = false;
	private boolean isIcelandicPersonalID = false;
	private Participant participant = null;
	private boolean disablePaymentAndOverviewSteps = false;
	private boolean showQuestionsError = false;

	private boolean errorNoParticipantFound = false;
	private boolean errorParticipantNotAllowed = false;
	private boolean errorParticipantAlreadyPayed = false;

	public void main(IWContext iwc) throws Exception {
		if (!iwc.isInEditMode()) {
			this.isIcelandic = iwc.getCurrentLocale().equals(
					LocaleUtil.getIcelandicLocale());

			if (getParticipant() != null
					&& getParticipant().getUser() != null
					&& getParticipant().getUser().getPersonalID() != null
					&& !"".equals(getParticipant().getUser().getPersonalID()
							.trim())) {
				if (SocialSecurityNumber
						.isValidIcelandicSocialSecurityNumber(getParticipant()
								.getUser().getPersonalID())) {
					this.isIcelandicPersonalID = true;
				}
			}

			loadCurrentStep(iwc, parseAction(iwc));
		}
	}

	private void loadCurrentStep(IWContext iwc, int action)
			throws RemoteException {

		if (action == ACTION_STEP_DISCLAIMER) {
			Participant participant = getParticipant();
			if (participant == null) {
				errorNoParticipantFound = true;
				action = ACTION_STEP_PARTICIPANTLOOKUP;
			} else {
				if (!participant.getIsAllowedToRun()) {
					errorParticipantNotAllowed = true;
					action = ACTION_STEP_PARTICIPANTLOOKUP;
				} else if (!participant.getHasPayedConfirmation()) {
					errorParticipantNotAllowed = true;
					action = ACTION_STEP_PARTICIPANTLOOKUP;
				} else if (participant.getHasPayedFee()) {
					errorParticipantAlreadyPayed = true;
					action = ACTION_STEP_PARTICIPANTLOOKUP;
				}
			}
		}

		switch (action) {

		case ACTION_STEP_PARTICIPANTLOOKUP:
			stepParticipantLookup(iwc);
			break;
		case ACTION_STEP_DISCLAIMER:
			stepDisclaimer(iwc);
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

	private void stepParticipantLookup(IWContext iwc) throws RemoteException {
		iwc.removeSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS);

		Form form = new Form();

		form.addParameter(PARAMETER_ACTION, ACTION_NEXT);
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_PARTICIPANTLOOKUP);

		form.add(getStepsHeader(iwc, ACTION_STEP_PARTICIPANTLOOKUP));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.setHeight(row++, 12);

		table.add(
				getInformationTable(localize("lv_paym.information_text_step_1",
						"Information text 1...")), 1, row++);
		table.setHeight(row++, 6);

		if (this.errorNoParticipantFound) {
			Text errorText = getHeader(localize(
					"lv_paym.participant_not_found",
					"This participant is not registered in this run."));
			errorText.setFontColor("#ff0000");
			table.add(errorText, 1, row++);
			table.setHeight(row++, 18);
			showQuestionsError = false;
		}

		if (this.errorParticipantNotAllowed) {
			Text errorText = getHeader(localize(
					"lv_paym.participant_not_allowed",
					"You were not picked to participant in the run."));
			errorText.setFontColor("#ff0000");
			table.add(errorText, 1, row++);
			table.setHeight(row++, 18);
			showQuestionsError = false;
		}

		if (this.errorParticipantAlreadyPayed) {
			Text errorText = getHeader(localize("lv_paym.participant_payed",
					"You have already confirmed your participation and payed the fee."));
			errorText.setFontColor("#ff0000");
			table.add(errorText, 1, row++);
			table.setHeight(row++, 18);
			showQuestionsError = false;
		}

		// table.setCellpadding(1, row, 24);
		table.add(
				getHeader(localize("lv_paym.participant_id", "Participant id")
						+ ":"), 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);

		TextInput input = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_PARTICIPANT_ID));
		input.setLength(5);
		input.setMaxlength(5);
		input.setInFocusOnPageLoad(true);
		table.add(input, 1, row++);

		UIComponent buttonsContainer = getButtonsFooter(iwc, false, true);
		form.add(buttonsContainer);

		add(form);
	}

	private void stepDisclaimer(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PARTICIPANT_ID);

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
		//next.setDisabled(true);

		// table.add(getText(localize(key, "Information text 4...")), 1, row++);
		Object[] args = { participant.getUser().getName() };

		table.add(getInformationTable(MessageFormat.format(
				localize("lv_paym.information_text_step_4",
						"Information text 4..."), args)), 1, row++);


		UIComponent buttonsContainer = getButtonsFooter(iwc, true, false);
		buttonsContainer.getChildren().add(next);
		form.add(buttonsContainer);

		add(form);
	}

	private void stepPayment(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.maintainParameter(PARAMETER_PARTICIPANT_ID);
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
				getInformationTable(localize("lv_paym.information_text_step_6",
						"Information text 6...")), 1, row++);
		table.setHeight(row++, 18);

		Map participants = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS);
		Table participantTable = new Table();
		participantTable.setWidth(Table.HUNDRED_PERCENT);
		participantTable.setCellspacing(0);
		participantTable
				.add(getHeader(localize("lv_paym.runner_name", "Runner name")),
						1, 1);
		participantTable.add(getHeader(localize("lv_paym.run", "Run")), 2, 1);
		participantTable.add(
				getHeader(localize("lv_paym.distance", "Distance")), 3, 1);
		participantTable.add(getHeader(localize("lv_paym.price", "Price")), 4,
				1);
		table.add(participantTable, 1, row++);
		table.setHeight(row++, 18);
		int runRow = 2;

		float totalAmount = 0;
		Iterator iter = participants.values().iterator();
		boolean first = true;
		while (iter.hasNext()) {
			Participant participant = (Participant) iter.next();
			participantTable.add(getText(participant.getUser().getName()), 1,
					runRow);

			participantTable.add(
					getText(localize(participant.getRunTypeGroup().getName(),
							participant.getRunTypeGroup().getName())), 2,
					runRow);
			participantTable.add(
					getText(localize(participant.getRunDistanceGroup()
							.getName(), participant.getRunDistanceGroup()
							.getName())), 3, runRow);
			float runPrice = 0.0f;
			if (this.isIcelandicPersonalID) {
				runPrice = 33400.0f; // TODO Fix
			} else {
				runPrice = 270;
			}
			totalAmount += runPrice;
			if (this.isIcelandicPersonalID) {
				participantTable.add(
						getText(formatAmount(LocaleUtil.getIcelandicLocale(),
								runPrice)), 4, runRow++);
			} else {
				participantTable
						.add(getText(formatAmount(iwc.getCurrentLocale(),
								runPrice)), 4, runRow++);
			}

			participant.setPayedAmount(String.valueOf(runPrice));
			participant.setFeeAmount((int) runPrice);
			addParticipant(iwc, (Integer) participant.getPrimaryKey(),
					participant);

			if (first) {
				if (this.isIcelandicPersonalID) {
					participantTable.add(new HiddenInput(
							PARAMETER_REFERENCE_NUMBER, participant.getUser()
									.getPersonalID().replaceAll("-", "")));
				} else {
					participantTable.add(new HiddenInput(
							PARAMETER_REFERENCE_NUMBER, participant.getUser()
									.getDateOfBirth().toString()
									.replaceAll("-", "")));
				}
				first = false;
			}
		}

		if (totalAmount == 0) {
			stepReceipt(iwc, false);
			return;
		}

		participantTable.setHeight(runRow++, 12);
		participantTable.add(
				getHeader(localize("lv_paym.total_amount", "Total amount")), 1,
				runRow);
		participantTable.add(
				getHeader(formatAmount(iwc.getCurrentLocale(), totalAmount)),
				4, runRow);
		participantTable.setColumnAlignment(4, Table.HORIZONTAL_ALIGN_RIGHT);

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
				getHeader(localize("lv_paym.credit_card_information",
						"Credit card information")), 1, creditRow);
		Collection images = getRunBusiness(iwc).getCreditCardImages(this.isIcelandicPersonalID ? "ISK" : "EUR");
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
		nameField.setAsNotEmpty(localize(
				"lv_paym.must_supply_card_holder_name",
				"You must supply card holder name"));
		nameField.keepStatusOnAction(true);
		nameField.setContent(getParticipant().getUser().getName());

		TextInput ccv = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_CCV));
		ccv.setLength(3);
		ccv.setMaxlength(3);
		ccv.setAsIntegers(localize("lv_paym.not_valid_ccv",
				"Not a valid CCV number"));
		ccv.setAsNotEmpty(localize("lv_paym.must_supply_ccv",
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
				getHeader(localize("lv_paym.card_holder", "Card holder")), 1,
				creditRow);
		creditCardTable.add(
				getHeader(localize("lv_paym.card_number", "Card number")), 3,
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
					localize("lv_paym.not_valid_card_number",
							"Not a valid card number"));
			cardNumber.setAsIntegers(localize("lv_paym.not_valid_card_number",
					"Not a valid card number"));
			cardNumber.setAsNotEmpty(localize(
					"lv_paym.must_supply_card_number",
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
				getHeader(localize("lv_paym.card_expires", "Card expires")), 1,
				creditRow);
		creditCardTable.add(
				getHeader(localize("lv_paym.ccv_number", "CCV number")), 3,
				creditRow++);
		creditCardTable.add(month, 1, creditRow);
		creditCardTable.add(getText("/"), 1, creditRow);
		creditCardTable.add(year, 1, creditRow);
		creditCardTable.add(ccv, 3, creditRow++);

		TextInput emailField = (TextInput) getStyledInterface(new TextInput(
				PARAMETER_CARD_HOLDER_EMAIL));
		emailField.setAsEmail(localize("lv_paym.email_err_msg",
				"Not a valid email address"));
		emailField.setLength(30);
		emailField.keepStatusOnAction(true);
		Email email = null;
		try {
			email = getUserBusiness(iwc).getUsersMainEmail(
					getParticipant().getUser());
		} catch (NoEmailFoundException nefe) {
		} catch (RemoteException e) {
		}

		if (email != null) {
			emailField.setContent(email.getEmailAddress());
		}

		creditCardTable.setHeight(creditRow++, 3);
		creditCardTable.mergeCells(3, creditRow, 3, creditRow + 1);
		creditCardTable
				.add(getText(localize(
						"lv_paym.ccv_explanation_text",
						"A CCV number is a three digit number located on the back of all major credit cards.")),
						3, creditRow);
		creditCardTable.add(
				getHeader(localize("lv_paym.card_holder_email",
						"Cardholder email")), 1, creditRow++);
		creditCardTable.add(emailField, 1, creditRow++);
		creditCardTable.add(new HiddenInput(PARAMETER_AMOUNT, String
				.valueOf(totalAmount)));
		creditCardTable.setHeight(creditRow++, 18);
		creditCardTable.mergeCells(1, creditRow, creditCardTable.getColumns(),
				creditRow);
		creditCardTable.add(
				getText(localize("lv_paym.read_conditions",
						"Please read before you finish your payment")), 1,
				creditRow);

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize(
				"lv_paym.pay", "Pay fee")));
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
				.add(getHeader(localize("lv_paym.agree_terms_and_conditions",
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
			Collection participants = ((Map) iwc
					.getSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS))
					.values();

			String nameOnCard = null;
			String cardNumber = null;
			String hiddenCardNumber = "XXXX-XXXX-XXXX-XXXX";
			Email emailEntry = null;
			try {
				emailEntry = getUserBusiness(iwc).getUsersMainEmail(
						getParticipant().getUser());
			} catch (NoEmailFoundException nefe) {
			} catch (RemoteException e) {
			}

			String email = emailEntry != null ? emailEntry.getEmailAddress()
					: "";
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

			getRunBusiness(iwc).markParticipantAsPaid(participants, email,
					hiddenCardNumber, amount, paymentStamp,
					iwc.getCurrentLocale(), isDisablePaymentAndOverviewSteps(),
					"lv_paym.");

			if (doPayment) {
				getRunBusiness(iwc).finishPayment(properties, this.isIcelandicPersonalID ? "ISK" : "EUR");
			}

			iwc.removeSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS);

			showReceipt(iwc, participants, amount, hiddenCardNumber,
					paymentStamp, doPayment);
		} catch (IDOCreateException e) {
			getParentPage()
					.setAlertOnLoad(
							localize("lv_paym.save_failed",
									"There was an error when trying to finish registration."));
			e.printStackTrace();
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

	private void cancel(IWContext iwc) {
		iwc.removeSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS);
	}

	private void showReceipt(IWContext iwc, Collection participants,
			double amount, String cardNumber, IWTimestamp paymentStamp,
			boolean doPayment) {

		add(getStepsHeader(iwc, ACTION_STEP_RECEIPT));

		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS, participants);
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_AMOUNT, new Double(amount));
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_CARD_NUMBER, cardNumber);
		iwc.setSessionAttribute(SESSION_ATTRIBUTE_PAYMENT_DATE, paymentStamp);

		Group run = null;

		table.setHeight(row++, 18);

		String greeting = localize("lv_paym.hello_participant",
				"Dear participant");

		table.add(getHeader(greeting), 1, row++);
		table.setHeight(row++, 16);

		table.add(
				getText(localize("lv_paym.payment_received",
						"You have registered for the following:")), 1, row++);
		table.setHeight(row++, 8);

		Table participantTable = new Table(5, participants.size() + 4);
		participantTable.setWidth(Table.HUNDRED_PERCENT);
		int col = 1;
		participantTable.add(
				getHeader(localize("lv_paym.runner_name", "Runner name")),
				col++, 1);
		participantTable.add(getHeader(localize("lv_paym.run", "Run")), col++,
				1);
		participantTable.add(
				getHeader(localize("lv_paym.shirt_size", "Shirt size")), col++,
				1);
		table.add(participantTable, 1, row++);
		int runRow = 2;
		Iterator iter = participants.iterator();
		while (iter.hasNext()) {
			Participant participant = (Participant) iter.next();
			run = participant.getRunTypeGroup();
			Group distance = participant.getRunDistanceGroup();
			col = 1;
			participantTable.add(getText(participant.getUser().getName()),
					col++, runRow);
			participantTable.add(getText(localize(run.getName(), run.getName())
					+ " " + participant.getRunYearGroup().getName()), col++,
					runRow);
			participantTable.add(
					getText(localize(
							"shirt_size." + participant.getShirtSize(),
							participant.getShirtSize())), col++, runRow++);
		}

		if (doPayment) {
			Table creditCardTable = new Table(2, 3);
			creditCardTable.add(
					getHeader(localize("lv_paym.payment_received_timestamp",
							"Payment received") + ":"), 1, 1);
			creditCardTable.add(getText(paymentStamp.getLocaleDateAndTime(
					iwc.getCurrentLocale(), IWTimestamp.SHORT,
					IWTimestamp.SHORT)), 2, 1);
			creditCardTable.add(
					getHeader(localize("lv_paym.card_number", "Card number")
							+ ":"), 1, 2);
			creditCardTable.add(getText(cardNumber), 2, 2);
			creditCardTable.add(getHeader(localize("lv_paym.amount", "Amount")
					+ ":"), 1, 3);
			creditCardTable
					.add(getText(formatAmount(iwc.getCurrentLocale(),
							(float) amount)), 2, 3);
			table.setHeight(row++, 16);
			table.add(creditCardTable, 1, row++);
		}

		table.setHeight(row++, 16);
		table.add(
				getHeader(localize("lv_paym.receipt_info_headline",
						"Receipt - Please print it out")), 1, row++);
		table.add(
				getText(localize(
						"lv_paym.receipt_info_headline_body",
						"This document is your receipt, please print it out and bring it with you when you collect your race material.")),
				1, row++);

		table.setHeight(row++, 16);
		table.add(getText(localize("lv_paym.best_regards", "Best regards,")),
				1, row++);

		add(table);

		Link print = new Link(localize("print", "Print receipt"));
		print.setPublicWindowToOpen(PaymentReceivedPrintable.class);

		UIComponent buttonsContainer = getButtonsFooter(iwc, false, false);
		buttonsContainer.getChildren().add(print);
		add(buttonsContainer);

	}

	protected void initializeSteps(IWContext iwc) {
		addStep(iwc, ACTION_STEP_PARTICIPANTLOOKUP,
				localize("lv_paym.lookup", "Lookup"));

		addStep(iwc, ACTION_STEP_DISCLAIMER,
				localize("lv_paym.disclaimer", "Disclaimer"));

		addStep(iwc, ACTION_STEP_PAYMENT,
				localize("lv_paym.payment", "Payment"));

		addStep(iwc, ACTION_STEP_RECEIPT,
				localize("lv_paym.receipt", "Receipt"));
	}

	protected Participant getParticipant() {
		if (this.participant == null) {
			IWContext iwc = IWContext.getInstance();
			try {
				this.participant = getAndUpdateParticipant(iwc);
			} catch (FinderException e) {
				throw new RuntimeException(e);
			}
		}
		return this.participant;
	}

	private Participant getAndUpdateParticipant(IWContext iwc)
			throws FinderException {
		Participant participant = null;

		if (!iwc.isParameterSet(PARAMETER_PARTICIPANT_ID)) {
			return participant;
		}

		Integer partId = new Integer(iwc.getParameter(PARAMETER_PARTICIPANT_ID));
		participant = getParticipant(iwc, partId);
		if (participant == null) {
			try {
				participant = getRunBusiness(iwc).getParticipantByPrimaryKey(
						partId.intValue());
			} catch (RemoteException e) {
			}
		}

		if (participant == null) {
			return participant;
		}

		if (partId != null) {
			addParticipant(iwc, partId, participant);
		}

		return participant;
	}

	private Participant getParticipant(IWContext iwc, Integer key) {
		Map participantMap = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS);
		if (participantMap != null) {
			return (Participant) participantMap.get(key);
		}
		return null;
	}

	private void addParticipant(IWContext iwc, Integer key,
			Participant participant) {
		Map participantMap = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS);
		if (participantMap == null) {
			participantMap = new HashMap();
		}
		participantMap.put(key, participant);

		iwc.setSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS, participantMap);
	}

	private void removeParticipant(IWContext iwc, Integer key) {
		Map participantMap = (Map) iwc
				.getSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS);
		if (participantMap == null) {
			participantMap = new HashMap();
		}
		participantMap.remove(key);

		iwc.setSessionAttribute(SESSION_ATTRIBUTE_PARTICIPANTS, participantMap);
	}

	private String formatAmount(Locale locale, float amount) {
		return NumberFormat.getInstance(locale).format(amount) + " "
				+ (this.isIcelandicPersonalID ? "ISK" : "EUR");
	}

	public boolean isDisablePaymentAndOverviewSteps() {
		return disablePaymentAndOverviewSteps;
	}

	public void setDisablePaymentAndOverviewSteps(
			boolean disablePaymentAndOverviewSteps) {
		this.disablePaymentAndOverviewSteps = disablePaymentAndOverviewSteps;
	}

	private void loadPreviousStep(IWContext iwc) throws RemoteException {
		// Convenient to use when Exception is caught in one step, and user is
		// sent to the previous step
		loadCurrentStep(iwc,
				Integer.parseInt(iwc.getParameter(PARAMETER_FROM_ACTION)));
	}
}