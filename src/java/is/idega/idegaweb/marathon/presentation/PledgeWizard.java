package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.business.PledgeHolder;
import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Pledge;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.ejb.FinderException;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.entity.business.EntityToPresentationObjectConverter;
import com.idega.block.entity.data.EntityPath;
import com.idega.block.entity.presentation.EntityBrowser;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWColor;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;

public class PledgeWizard extends RunBlock {
	
	private static final String PARAMETER_ACTION = "prm_action";
	private static final String PARAMETER_FROM_ACTION = "prm_from_action";
	private static final String PARAMETER_SEARCH = "prm_search";
	
	private static final String PARAMETER_PARTICIPANT_ID = "prm_participant_id";
	private static final String PARAMETER_PERSONAL_ID_FILTER = "prm_personal_id_filter";
	private static final String PARAMETER_FIRST_NAME_FILTER = "prm_first_name_filter";
	private static final String PARAMETER_MIDDLE_NAME_FILTER = "prm_middle_name_filter";
	private static final String PARAMETER_LAST_NAME_FILTER = "prm_last_name_filter";
	private static final String PARAMETER_CHARITY_FILTER = "prm_charity_filter";

	private static final String PARAMETER_PLEDGE_AMOUNT = "prm_pledge_amount";
	private static final String PARAMETER_CARDHOLDER_NAME = "prm_cardholder_name";
	private static final String PARAMETER_CARD_NUMBER = "prm_card_number";
	private static final String PARAMETER_EXPIRES_MONTH = "prm_expires_month";
	private static final String PARAMETER_EXPIRES_YEAR = "prm_expires_year";
	private static final String PARAMETER_CCV = "prm_ccv";

	private static final int ACTION_STEP_PERSON_LOOKUP = 1;
	private static final int ACTION_STEP_PERSONAL_DETAILS = 2;
	private static final int ACTION_STEP_PAYMENT = 3;
	private static final int ACTION_SAVE = 4;
	
	private static int NUMBER_OF_ROWS_IN_ENTITY_BROWSER = 10;
	
	private PledgeHolder pledgeHolder = new PledgeHolder();
	private boolean isIcelandic = false;
	private int runGroupID = -1;
	
	
	public void main(IWContext iwc) throws Exception {
		this.isIcelandic = iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale());

		switch (parseAction(iwc)) {
			case ACTION_STEP_PERSON_LOOKUP:
				stepPersonLookup(iwc);
				break;
			case ACTION_STEP_PERSONAL_DETAILS:
				stepPersonalDetails(iwc);
				break;
			case ACTION_STEP_PAYMENT:
				stepPayment(iwc);
				break;
			case ACTION_SAVE:
				save(iwc, true);
				break;
		}
	}
	
	protected int parseAction(IWContext iwc) {
		collectValues(iwc);
		
		int action;
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		} else {
			action = ACTION_STEP_PERSON_LOOKUP;
		}
		return action;
	}
	
	private void stepPersonLookup(IWContext iwc) {
		Form form = new Form();
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;
		table.add(getPhasesTable(1, 4, "pledgewizard.make_pledge", "Find runner to make a pledge for"));
		table.setHeight(row++, 18);

		TextInput firstNameInput = new TextInput(PARAMETER_FIRST_NAME_FILTER);
		Layer firstNameLayer = new Layer(Layer.DIV);
		firstNameLayer.setStyleClass(STYLENAME_FORM_ELEMENT);
		Label firstnameLabel = new Label(localize("pledgewizard.first_name", "First name") + ":", firstNameInput);
		firstNameLayer.add(firstnameLabel);
		firstNameLayer.add(firstNameInput);
		//form.add(firstNameLayer);
		//form.add(new Break());
		
		TextInput middleNameInput = new TextInput(PARAMETER_MIDDLE_NAME_FILTER);
		Layer middleNameLayer = new Layer(Layer.DIV);
		middleNameLayer.setStyleClass(STYLENAME_FORM_ELEMENT);
		Label middleNameLabel = new Label(localize("pledgewizard.middle_name", "Middle name") + ":", middleNameInput);
		middleNameLayer.add(middleNameLabel);
		middleNameLayer.add(middleNameInput);
		//form.add(middleNameLayer);
		//form.add(new Break());
		
		TextInput lastNameInput = new TextInput(PARAMETER_LAST_NAME_FILTER);
		Layer lastNameLayer = new Layer(Layer.DIV);
		lastNameLayer.setStyleClass(STYLENAME_FORM_ELEMENT);
		Label lastNameLabel = new Label(localize("pledgewizard.last_name", "Last name") + ":", lastNameInput);
		lastNameLayer.add(lastNameLabel);
		lastNameLayer.add(lastNameInput);
		//form.add(lastNameLayer);
		//form.add(new Break());

		TextInput personalIDInput = new TextInput(PARAMETER_PERSONAL_ID_FILTER);
		personalIDInput.setLength(10);
		personalIDInput.setMaxlength(10);
		Layer personalIDLayer = new Layer(Layer.DIV);
		personalIDLayer.setStyleClass(STYLENAME_FORM_ELEMENT);
		Label personalIDLabel = new Label(localize("pledgewizard.personal_id", "Personal ID") + ":", personalIDInput);
		personalIDLayer.add(personalIDLabel);
		personalIDLayer.add(personalIDInput);
		//form.add(personalIDLayer);
		//form.add(new Break());

		DropdownMenu charityDropDown = (new CharitiesForRunDropDownMenu(PARAMETER_CHARITY_FILTER,new Integer(runGroupID)));
		charityDropDown.setWidth("200");
		Layer charityDropDownLayer = new Layer(Layer.DIV);
		charityDropDownLayer.setStyleClass(STYLENAME_FORM_ELEMENT);
		Label charityDropDownLabel = new Label(localize("pledgewizard.charity_organization", "Charity Organization") + ":", charityDropDown);
		charityDropDownLayer.add(charityDropDownLabel);
		charityDropDownLayer.add(charityDropDown);
		//form.add(charityDropDownLayer);
		//form.add(new Break());
		
		table.add(getText(localize("pledgewizard.pledge_information_text_step_1", "Information text 1...")), 1, row++);
		table.setHeight(row++, 6);
		table.add(firstNameLayer, 1, row);
		table.setHeight(row++, 6);
		table.add(middleNameLayer, 1, row);
		table.setHeight(row++, 6);
		table.add(lastNameLayer, 1, row);
		table.setHeight(row++, 6);
		table.add(personalIDLayer, 1, row);
		table.setHeight(row++, 6);
		table.add(charityDropDownLayer, 1, row);
		table.setHeight(row++, 6);
		
		SubmitButton search;
		if (pledgeHolder.getFirstNameFilter() != null){
			firstNameInput.setValue(pledgeHolder.getFirstNameFilter());
		}
		if (pledgeHolder.getMiddleNameFilter() != null){
			middleNameInput.setValue(pledgeHolder.getMiddleNameFilter());
		}
		if (pledgeHolder.getLastNameFilter() != null){
			lastNameInput.setValue(pledgeHolder.getLastNameFilter());
		}
		if (pledgeHolder.getPersonalIDFilter() != null){
			personalIDInput.setValue(pledgeHolder.getPersonalIDFilter());
		}
		if (pledgeHolder.getCharityFilter() != null){
			charityDropDown.setSelectedElement(pledgeHolder.getCharityFilter());

		}
		if (iwc.isParameterSet(PARAMETER_SEARCH) || iwc.isParameterSet(EntityBrowser.BOTTOM_FORM_KEY + EntityBrowser.SHOW_ALL_KEY)) {
			search = new SubmitButton(localize("search_again", "Search again"),PARAMETER_SEARCH, PARAMETER_SEARCH);
			table.add(search, 1, row);
			table.setAlignment(1, row++, Table.HORIZONTAL_ALIGN_RIGHT);
			table.setHeight(1, row++, 8);
			
			if (this.runGroupID != -1) {
				try {
					Group runYear = getGroupBusiness(iwc).getGroupByGroupID(this.runGroupID);
					Group run = getRunBusiness(iwc).getRunGroupOfTypeForGroup(runYear, IWMarathonConstants.GROUP_TYPE_RUN);
					Collection groupTypesFilter = new ArrayList();
					groupTypesFilter.add(IWMarathonConstants.GROUP_TYPE_RUN_GROUP);
					Collection runnerGroups = getGroupBusiness(iwc).getChildGroupsRecursiveResultFiltered(runYear, groupTypesFilter, true, true, true);
					String[] group_ids = new String[runnerGroups.size()];
					Iterator grIt = runnerGroups.iterator();
					for (int i=0; grIt.hasNext(); i++) {
						Group group = (Group)grIt.next();
						group_ids[i] = group.getPrimaryKey().toString();
					}
					UserHome userHome = (UserHome) IDOLookup.getHome(User.class);
					Collection usersFound = userHome.findUsersByConditions(pledgeHolder.getFirstNameFilter(), pledgeHolder.getMiddleNameFilter(), pledgeHolder.getLastNameFilter(), pledgeHolder.getPersonalIDFilter(), null, null, -1, -1, -1, -1, group_ids, null, true, false);
					Collection runRegistrations = new ArrayList();
					Iterator userIt = usersFound.iterator();
					while (userIt.hasNext()) {
						User user = (User)userIt.next();
						Participant runRegistration = getRunBusiness(iwc).getParticipantByRunAndYear(user, run, runYear);
						
						if (pledgeHolder.getCharityFilter().equals("-1") || (runRegistration.getCharityId() != null && runRegistration.getCharityId().equals(pledgeHolder.getCharityFilter()))) {
							if (runRegistration.getCharityId() != null && runRegistration.getCharityId() != "-1") {
								runRegistrations.add(runRegistration);
							}
						}
					}
					if (runRegistrations.isEmpty()) {
						table.add(getText(localize("pledgewizard.no_runners_found", "No runners were found from your search criteria")), 1, row++);
						table.setHeight(1, row, 20);
						
						
					} else {
						EntityBrowser browser = getRunnersBrowser(runRegistrations, iwc);
						table.add(browser, 1, row);
					}
				} catch (FinderException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} 
		} else {
			search = new SubmitButton(localize("search", "Search"),PARAMETER_SEARCH, PARAMETER_SEARCH);
			search.setValueOnClick(PARAMETER_SEARCH, PARAMETER_SEARCH);
			table.add(search, 1, row);
			table.setAlignment(1, row++, Table.HORIZONTAL_ALIGN_RIGHT);
			table.setHeight(1, row++, 8);
		}
		add(form);
	} 
	
	private void stepPersonalDetails(IWContext iwc) {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, "-1");
		form.addParameter(PARAMETER_FROM_ACTION, ACTION_STEP_PERSON_LOOKUP);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(2, 4, "pledgewizard.specify_pledge_amount", "Specify pledge amount"), 1, row++);
		table.setHeight(row++, 18);

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("next", "Next")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_PAYMENT));

		Participant participant = null;
		try {
			if (iwc.isParameterSet(PARAMETER_PARTICIPANT_ID)) {
				participant = getRunBusiness(iwc).getParticipantByPrimaryKey(Integer.parseInt(iwc.getParameter(PARAMETER_PARTICIPANT_ID)));
				pledgeHolder.setParticipant(participant);
			} else {
				participant = pledgeHolder.getParticipant();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Text nameText = new Text();
		Layer nameLayer = new Layer(Layer.DIV);
		nameLayer.setStyleClass(STYLENAME_FORM_ELEMENT);
		Text labelText = getText(localize("pledgewizard.runner_name", "Runner name") + ": ");
		labelText.setBold();
		nameLayer.add(labelText);
		if (participant != null) {
			nameText = getText(participant.getUser().getName());
		}
		nameLayer.add(nameText);
		//form.add(nameLayer);
		//form.add(new Break());
		
		Collection runs = new ArrayList();
		runs.add(participant);
		EntityBrowser browser = getRunsBrowser(runs, iwc);
		
		table.add(getText(localize("pledgewizard.pledge_information_text_step_2", "Information text 2...")), 1, row++);
		table.setHeight(row++, 6);
		table.add(nameLayer, 1, row++);
		table.setHeight(row++, 6);
		table.add(browser, 1, row++);
	    
		
		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_PERSON_LOOKUP));

		table.setHeight(row++, 18);
		table.add(previous, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(next, 1, row);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}

	private void stepPayment(IWContext iwc) throws RemoteException {
		Form form = new Form();
		form.addParameter(PARAMETER_ACTION, "-1");
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		form.add(table);
		int row = 1;

		table.add(getPhasesTable(3, 4, "run_reg.payment_info", "Payment info"), 1, row++);
		table.setHeight(row++, 12);

		table.add(getInformationTable(localize("pledgewizard.pledge_information_text_step_3", "Information text 3...")), 1, row++);
		table.setHeight(row++, 18);
		
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
		
		float totalAmount = 0;

		if (pledgeHolder.getParticipant() != null && pledgeHolder.getParticipant().getUser() != null) {
			runnerTable.add(getText(pledgeHolder.getParticipant().getUser().getName()), 1, runRow);
			runnerTable.add(getText(localize(pledgeHolder.getParticipant().getRunTypeGroup().getName(), pledgeHolder.getParticipant().getRunTypeGroup().getName())+ " " + localize(pledgeHolder.getParticipant().getRunYearGroup().getName(), pledgeHolder.getParticipant().getRunYearGroup().getName())), 2, runRow);
			runnerTable.add(getText(localize(pledgeHolder.getParticipant().getRunDistanceGroup().getName()+"_short_name", pledgeHolder.getParticipant().getRunDistanceGroup().getName())), 3, runRow);
		}
		float pledgeAmount = pledgeHolder.getPledgeAmount();
		totalAmount += pledgeAmount;
		runnerTable.add(getText(formatAmount(iwc.getCurrentLocale(), pledgeAmount)), 4, runRow++);
		
		pledgeHolder.setPledgeAmount(pledgeAmount);
		
		if (totalAmount == 0) {
			//save(iwc, false);
			//return;
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
				//image.setToolTip(getResourceBundle().getLocalizedString("image_tooltip."+image.getName(),image.getName()));
				creditCardTable.add(image, 3, creditRow);
				if (iterator.hasNext()) {
					creditCardTable.add(Text.getNonBrakingSpace(), 3, creditRow);
				}
			}
		}
		creditCardTable.setAlignment(3, creditRow++, Table.HORIZONTAL_ALIGN_RIGHT);
		creditCardTable.setHeight(creditRow++, 12);

		TextInput nameField = (TextInput) getStyledInterface(new TextInput(PARAMETER_CARDHOLDER_NAME));
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
				cardNumber.setMaxlength(4);
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
		
		creditCardTable.setHeight(creditRow++, 3);
		creditCardTable.mergeCells(3, creditRow, 3, creditRow+1);
		creditCardTable.add(getText(localize("run_reg.ccv_explanation_text","A CCV number is a three digit number located on the back of all major credit cards.")), 3, creditRow);
		creditCardTable.add(new HiddenInput(PARAMETER_PLEDGE_AMOUNT, String.valueOf(totalAmount)));
		
		//creditCardTable.setHeight(creditRow++, 18);
		//creditCardTable.mergeCells(1, creditRow, creditCardTable.getColumns(), creditRow);
		//creditCardTable.add(getText(localize("run_reg.read_conditions", "Please read before you finish your payment") + ": "), 1, creditRow);
		
		//Help help = new Help();
		//help.setHelpTextBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER);
		//help.setHelpTextKey("terms_and_conditions");
		//help.setShowAsText(true);
		//help.setLinkText(localize("run_reg.terms_and_conditions", "Terms and conditions"));
		//creditCardTable.add(help, 1, creditRow++);

		SubmitButton next = (SubmitButton) getButton(new SubmitButton(localize("run_reg.pay", "Pay fee")));
		next.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		//next.setDisabled(true);

		//CheckBox agree = getCheckBox(PARAMETER_AGREE, Boolean.TRUE.toString());
		//agree.setToEnableWhenChecked(next);
		//agree.setToDisableWhenUnchecked(next);
		
		//creditCardTable.setHeight(creditRow++, 12);
		//creditCardTable.mergeCells(1, creditRow, creditCardTable.getColumns(), creditRow);
		//creditCardTable.add(agree, 1, creditRow);
		//creditCardTable.add(Text.getNonBrakingSpace(), 1, creditRow);
		//creditCardTable.add(getHeader(localize("run_reg.agree_terms_and_conditions", "I agree to the terms and conditions")), 1, creditRow++);

		SubmitButton previous = (SubmitButton) getButton(new SubmitButton(localize("previous", "Previous")));
		previous.setValueOnClick(PARAMETER_ACTION, String.valueOf(ACTION_STEP_PERSONAL_DETAILS));
		table.setHeight(row++, 18);
		table.add(previous, 1, row);
		table.add(Text.getNonBrakingSpace(), 1, row);
		table.add(next, 1, row);
		form.setToDisableOnSubmit(next, true);
		table.setAlignment(1, row, Table.HORIZONTAL_ALIGN_RIGHT);

		add(form);
	}

	private void showReceipt(IWContext iwc, Collection runners, double amount, String cardNumber, IWTimestamp paymentStamp, boolean doPayment) {
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;

		table.add(getPhasesTable(4, 4,  "pledgewizard.receipt", "Receipt"), 1, row++);
		table.setHeight(row++, 18);
		
		table.add(getHeader(localize("pledgewizard.hello_participant", "Thank you for making a pledge")), 1, row++);
		table.setHeight(row++, 16);

		table.add(getText(localize("pledgewizard.payment_received", "We have received payment for the following:")), 1, row++);
		table.setHeight(row++, 8);

		Table runnerTable = new Table(5, runners.size() + 3);
		runnerTable.setWidth(Table.HUNDRED_PERCENT);
		runnerTable.add(getHeader(localize("run_reg.runner_name", "Runner name")), 1, 1);
		runnerTable.add(getHeader(localize("run_reg.run", "Run")), 2, 1);
		runnerTable.add(getHeader(localize("run_reg.distance", "Distance")), 3, 1);
		runnerTable.add(getHeader(localize("pledgewizard.charity_organization", "Charity Organization")), 4, 1);
		runnerTable.add(getHeader(localize("run_reg.amount", "Amount")), 5, 1);
		table.add(runnerTable, 1, row++);
		int runRow = 2;
		Iterator iter = runners.iterator();
		Group run = null;
		while (iter.hasNext()) {
			Pledge pledge = (Pledge) iter.next();
			Participant participant = pledge.getParticipant();
			run = participant.getRunTypeGroup();
			Group year = participant.getRunYearGroup();
			Group distance = participant.getRunDistanceGroup();
			String charityString = "";
			try {
				Charity charity = getCharityBusiness(iwc).getCharityByOrganisationalID(pledge.getOrganizationalID());
				charityString = charity.getName();
			} catch (Exception e) {
				//charity not found
				System.err.println(e.getMessage());
			}
			
			runnerTable.add(getText(participant.getUser().getName()), 1, runRow);
			runnerTable.add(getText(localize(run.getName(), run.getName()) + " " + localize(year.getName(), year.getName())), 2, runRow);
			runnerTable.add(getText(localize(distance.getName()+"_short_name", distance.getName())), 3, runRow);
			runnerTable.add(getText(charityString), 4, runRow);
			runnerTable.add(getText(pledge.getAmountPayed()), 5, runRow++);
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
		table.add(getHeader(localize("run_reg.receipt_info_headline", "Receipt - Please print it out")), 1, row++);
		table.add(getText(localize("run_reg.receipt_info_headline_body", "This document is your receipt, please print it out and bring it with you when you collect your race material.")), 1, row++);

		table.setHeight(row++, 16);
		table.add(getText(localize("run_reg.best_regards", "Best regards,")), 1, row++);
		Run selectedRun = null;
		try {
			selectedRun = ConverterUtility.getInstance().convertGroupToRun(run);
		} catch (FinderException e) {
			//Run not found
		}
		if (selectedRun != null) {
			table.add(getText(localize(selectedRun.getName(), selectedRun.getName())), 1, row++);
			table.add(getText(selectedRun.getRunHomePage()), 1, row++);
		}
		
		table.setHeight(row++, 16);
		
		//Link print = new Link(localize("print", "Print"));
		//print.setPublicWindowToOpen(RegistrationReceivedPrintable.class);
		//table.add(print, 1, row);
		
		add(table);
	}
	
	private void save(IWContext iwc, boolean doPayment) throws RemoteException {
		try {
			Collection pledgeHolders = new ArrayList();
			pledgeHolders.add(pledgeHolder);

			String nameOnCard = null;
			String cardNumber = null;
			String hiddenCardNumber = "XXXX-XXXX-XXXX-XXXX";
			String expiresMonth = null;
			String expiresYear = null;
			String ccVerifyNumber = null;
			String referenceNumber = IWTimestamp.RightNow().toString();
			double amount = 0;
			IWTimestamp paymentStamp = new IWTimestamp();

			IWBundle iwb = getBundle(iwc);
			boolean disablePaymentProcess = "true".equalsIgnoreCase(iwc.getApplicationSettings().getProperty("MARAHTON_DISABLE_PAYMENT_AUTH","false"));
			if (doPayment && disablePaymentProcess) {
				doPayment = false;
			}

			if (doPayment) {
				nameOnCard = iwc.getParameter(PARAMETER_CARDHOLDER_NAME);
				cardNumber = "";
				for (int i = 1; i <= 4; i++) {
					cardNumber += iwc.getParameter(PARAMETER_CARD_NUMBER + "_" + i);
				}
				hiddenCardNumber = "XXXX-XXXX-XXXX-" + iwc.getParameter(PARAMETER_CARD_NUMBER + "_" + 4);
				expiresMonth = iwc.getParameter(PARAMETER_EXPIRES_MONTH);
				expiresYear = iwc.getParameter(PARAMETER_EXPIRES_YEAR);
				ccVerifyNumber = iwc.getParameter(PARAMETER_CCV);
				amount = Double.parseDouble(iwc.getParameter(PARAMETER_PLEDGE_AMOUNT));
				referenceNumber = IWTimestamp.RightNow().toString();
			}
			
			String properties = null;
			if (doPayment) {
				properties = getRunBusiness(iwc).authorizePayment(nameOnCard, cardNumber, expiresMonth, expiresYear, ccVerifyNumber, amount, this.isIcelandic ? "ISK" : "EUR", referenceNumber);
			}
			Collection pledges = getPledgeBusiness(iwc).saveParticipants(pledgeHolders);
			if (doPayment) {
				getRunBusiness(iwc).finishPayment(properties);
			}			
			showReceipt(iwc, pledges, amount, hiddenCardNumber, paymentStamp, doPayment);
		}
		catch (IDOCreateException ice) {
			getParentPage().setAlertOnLoad(localize("run_reg.save_failed", "There was an error when trying to finish registration."));
			ice.printStackTrace();
			stepPayment(iwc);
		}
		catch (CreditCardAuthorizationException ccae) {
			IWResourceBundle creditCardBundle = iwc.getIWMainApplication().getBundle("com.idega.block.creditcard").getResourceBundle(iwc.getCurrentLocale());
			getParentPage().setAlertOnLoad(ccae.getLocalizedMessage(creditCardBundle));
			ccae.printStackTrace();
			stepPayment(iwc);
		}
	}
	
	private EntityBrowser getRunnersBrowser(Collection entities, IWContext iwc)  {
		// define checkbox button converter class
		EntityToPresentationObjectConverter converterToChooseButton = new EntityToPresentationObjectConverter() {
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				Participant participant = (Participant) entity;
				IWBundle marathonBundle = iwc.getIWMainApplication().getBundle("is.idega.idegaweb.marathon");
				Image forwardImage = marathonBundle.getImage("shared/forward.gif", 12, 12);
				forwardImage.setToolTip(marathonBundle.getResourceBundle(iwc).getLocalizedString("choose","Choose"));
				Link forwardLink = new Link(forwardImage);
				forwardLink.addParameter(PARAMETER_PARTICIPANT_ID, participant.getPrimaryKey().toString());
				forwardLink.addParameter(PARAMETER_ACTION, ACTION_STEP_PERSONAL_DETAILS);
				return forwardLink;
			}

		};
		
		EntityToPresentationObjectConverter converterToCharityOrganization = new EntityToPresentationObjectConverter() {
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				Participant participant = (Participant) entity;
				String charityString = "";
				try {
					Charity charity = getCharityBusiness(iwc).getCharityByOrganisationalID(participant.getCharityId());
					charityString = charity.getName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Text charityOrganisation = new Text(charityString);
				return charityOrganisation;
			}
		};

		EntityToPresentationObjectConverter converterToUserName = new EntityToPresentationObjectConverter() {
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				Participant participant = (Participant) entity;
				return  new Text(participant.getUser().getName());
			}
		};
		
		EntityToPresentationObjectConverter converterToDistanceName = new EntityToPresentationObjectConverter() {
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				Participant participant = (Participant) entity;
				return  new Text(localize(participant.getRunDistanceGroup().getName()+ "_short_name",participant.getRunDistanceGroup().getName()));
			}
		};
		
		EntityToPresentationObjectConverter converterToUserPersonalID = new EntityToPresentationObjectConverter() {
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				Participant participant = (Participant) entity;
				Date dob = participant.getUser().getDateOfBirth();
				IWTimestamp dobStamp = new IWTimestamp(dob);
				return  new Text(dobStamp.getDateString("dd. MMM yyyy", iwc.getCurrentLocale()));
			}
		};

	    // set default columns
		String nameKey = User.class.getName()+".FIRST_NAME:" + User.class.getName()+".MIDDLE_NAME:"+User.class.getName()+".LAST_NAME";
	    String pinKey = User.class.getName()+".DATE_OF_BIRTH_SHORT";
	    String distanceKey = Group.class.getName()+".DISTANCE";
	    String charityKey = Participant.class.getName()+".CHARITY_ORGANIZATIONAL_ID";
	    EntityBrowser browser = EntityBrowser.getInstance();
	    browser.setAcceptUserSettingsShowUserSettingsButton(false, false);
	    browser.setDefaultNumberOfRows(NUMBER_OF_ROWS_IN_ENTITY_BROWSER);
	    browser.setUseExternalForm(true);
	    browser.setEntities("pledge_wizard", entities);
	    browser.setWidth(Table.HUNDRED_PERCENT);
	    //fonts
	    Text column = new Text();
	    column.setBold();
	    browser.setColumnTextProxy(column);
	    //   set color of rows
	    browser.setColorForEvenRows(IWColor.getHexColorString(246, 246, 247));
	    browser.setColorForOddRows("#FFFFFF");
	      
	    browser.setDefaultColumn(1, nameKey);
	    browser.setDefaultColumn(2, pinKey);
	    browser.setDefaultColumn(3, distanceKey);
	    browser.setDefaultColumn(4, charityKey);
	    browser.setMandatoryColumn(1, "Choose");
        // set foreign entities
        browser.addEntity(User.class.getName());
        browser.addEntity(Group.class.getName());
	    // set special converters
	    browser.setEntityToPresentationConverter("Choose", converterToChooseButton);
	    browser.setEntityToPresentationConverter(nameKey, converterToUserName);
	    browser.setEntityToPresentationConverter(pinKey, converterToUserPersonalID);
	    browser.setEntityToPresentationConverter(distanceKey, converterToDistanceName);
	    browser.setEntityToPresentationConverter(charityKey, converterToCharityOrganization);
	    browser.setShowNavigation(false,true);
	    return browser;
	}
	
	private EntityBrowser getRunsBrowser(Collection entities, IWContext iwc)  {
		// define checkbox button converter class
		EntityToPresentationObjectConverter converterPledgeAmountTextInput = new EntityToPresentationObjectConverter() {
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				TextInput pledgeAmountInput = (TextInput)getStyledInterface(new TextInput(PARAMETER_PLEDGE_AMOUNT));
				pledgeAmountInput.setMaxlength(9);
				pledgeAmountInput.setWidth("50");
				pledgeAmountInput.setInFocusOnPageLoad(true);
				pledgeAmountInput.setAsNotEmpty(localize("pledgewizard.you_must_put_amount", "You must type in amount"));
				pledgeAmountInput.setAsIntegers(localize("pledgewizard.only_put_digits_in_amount_field","Please, only type in digits into the amount field"));
				return pledgeAmountInput;
			}
		};
		
		EntityToPresentationObjectConverter converterToRunYearName = new EntityToPresentationObjectConverter() {
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				Participant participant = (Participant) entity;
				return  new Text(localize(participant.getRunTypeGroup().getName(),participant.getRunTypeGroup().getName())+ " " +localize(participant.getRunYearGroup().getName(),participant.getRunYearGroup().getName()));
			}
		};
		
		EntityToPresentationObjectConverter converterToDistanceName = new EntityToPresentationObjectConverter() {
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				Participant participant = (Participant) entity;
				return  new Text(localize(participant.getRunDistanceGroup().getName()+ "_short_name",participant.getRunDistanceGroup().getName()));
			}
		};
		
		EntityToPresentationObjectConverter converterToCharityOrganization = new EntityToPresentationObjectConverter() {
			public PresentationObject getHeaderPresentationObject(EntityPath entityPath, EntityBrowser browser, IWContext iwc) {
				return browser.getDefaultConverter().getHeaderPresentationObject(entityPath, browser, iwc);
			}

			public PresentationObject getPresentationObject(Object entity, EntityPath path, EntityBrowser browser, IWContext iwc) {
				Participant participant = (Participant) entity;
				String charityString = "";
				try {
					Charity charity = getCharityBusiness(iwc).getCharityByOrganisationalID(participant.getCharityId());
					charityString = charity.getName();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Text charityOrganisation = new Text(charityString);
				return charityOrganisation;
			}
		};

	    // set default columns
		String runYearKey = Participant.class.getName()+".RUN|YEAR";
		String distanceKey = Group.class.getName()+".DISTANCE";
	    String charityKey = Participant.class.getName()+".CHARITY_ORGANIZATIONAL_ID";
	    EntityBrowser browser = EntityBrowser.getInstance();
	    browser.setAcceptUserSettingsShowUserSettingsButton(false, false);
	    browser.setShowNavigation(false,false);
	    browser.setDefaultNumberOfRows(NUMBER_OF_ROWS_IN_ENTITY_BROWSER);
	    browser.setUseExternalForm(true);
	    browser.setEntities("pledge_wizard_runs", entities);
	    browser.setWidth(Table.HUNDRED_PERCENT);
	    //fonts
	    Text column = new Text();
	    column.setBold();
	    browser.setColumnTextProxy(column);
	    //   set color of rows
	    browser.setColorForEvenRows(IWColor.getHexColorString(246, 246, 247));
	    browser.setColorForOddRows("#FFFFFF");
	      
	    browser.setDefaultColumn(1, runYearKey);
	    browser.setDefaultColumn(2, distanceKey);
	    browser.setDefaultColumn(3, charityKey);
	    browser.setDefaultColumn(4, "Amount");
	    
	    // set foreign entities
        browser.addEntity(User.class.getName());
        browser.addEntity(Group.class.getName());
	    // set special converters
	    browser.setEntityToPresentationConverter(runYearKey, converterToRunYearName);
	    browser.setEntityToPresentationConverter(distanceKey, converterToDistanceName);
	    browser.setEntityToPresentationConverter(charityKey, converterToCharityOrganization);
	    browser.setEntityToPresentationConverter("Amount", converterPledgeAmountTextInput);
	    return browser;
	}

	
	public void setRunYearGroup(Group group) {
		setRunYearGroup(new Integer(group.getPrimaryKey().toString()).intValue());
	}
	
	public void setRunYearGroup(int groupID) {
		if (groupID != -1) {
			this.runGroupID = groupID;
		}
	}
	
	private String formatAmount(Locale locale, float amount) {
		return NumberFormat.getInstance(locale).format(amount) + " " + (this.isIcelandic ? "ISK" : "EUR");
	}
	
	private void collectValues(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_PERSONAL_ID_FILTER)) {
			pledgeHolder.setPersonalIDFilter(iwc.getParameter(PARAMETER_PERSONAL_ID_FILTER));
		} else if (iwc.isParameterSetAsEmpty(PARAMETER_PERSONAL_ID_FILTER)) {
			pledgeHolder.setPersonalIDFilter(null);
		}
		if (iwc.isParameterSet(PARAMETER_FIRST_NAME_FILTER)) {
			pledgeHolder.setFirstNameFilter(iwc.getParameter(PARAMETER_FIRST_NAME_FILTER));
		} else if (iwc.isParameterSetAsEmpty(PARAMETER_FIRST_NAME_FILTER)) {
			pledgeHolder.setFirstNameFilter(null);
		}
		if (iwc.isParameterSet(PARAMETER_MIDDLE_NAME_FILTER)) {
			pledgeHolder.setMiddleNameFilter(iwc.getParameter(PARAMETER_MIDDLE_NAME_FILTER));
		} else if (iwc.isParameterSetAsEmpty(PARAMETER_MIDDLE_NAME_FILTER)) {
			pledgeHolder.setMiddleNameFilter(null);
		} 
		if (iwc.isParameterSet(PARAMETER_LAST_NAME_FILTER)) {
			pledgeHolder.setLastNameFilter(iwc.getParameter(PARAMETER_LAST_NAME_FILTER));
		} else if (iwc.isParameterSetAsEmpty(PARAMETER_LAST_NAME_FILTER)) {
			pledgeHolder.setLastNameFilter(null);
		} 
		if (iwc.isParameterSet(PARAMETER_CHARITY_FILTER)) {
			pledgeHolder.setCharityFilter(iwc.getParameter(PARAMETER_CHARITY_FILTER));
		} else if (iwc.isParameterSetAsEmpty(PARAMETER_CHARITY_FILTER)) {
			pledgeHolder.setCharityFilter(null);
		} 

		if (iwc.isParameterSet(PARAMETER_PLEDGE_AMOUNT)) {
			pledgeHolder.setPledgeAmount(Float.parseFloat(iwc.getParameter(PARAMETER_PLEDGE_AMOUNT)));
		}
		if (iwc.isParameterSet(PARAMETER_CARDHOLDER_NAME)) {
			pledgeHolder.setCardholderName(iwc.getParameter(PARAMETER_CARDHOLDER_NAME));
		}
	}
}