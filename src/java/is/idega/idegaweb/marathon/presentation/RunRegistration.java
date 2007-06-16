/*
 * Created on Jun 30, 2004
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.block.text.business.TextFormatter;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.RadioButton;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * 
 * @author birna
 */
public class RunRegistration extends Block {

	private static final String PARAMETER_ACTION = "prm_action";
	
	private static final String PARAMETER_REFERENCE_NUMBER = "parameter_refererence_number";
	private static final String PARAMETER_REFERENCE_NUMBER_DEFAULT_VALUE = "rNum";

	private static final int ACTION_STEP_ONE = 1;
	private static final int ACTION_STEP_TWO = 2;
	private static final int ACTION_SAVE = 4;

	private static final String STYLENAME_HEADING = "headingText";
	private static final String STYLENAME_GREEN_TEXT = "greenText";
	private static final String STYLENAME_BLUE_TEXT = "blueText";
	private static final String STYLENAME_INTERFACE = "interface";
	private static final String STYLENAME_CHECKBOX = "checkbox";

	private Text redStar;
	private Text infoRedStarText;

	//texts step one
	private Text distanceText;
	private Text nameText;
	private Text nationalityText;
	private Text ssnText;
	private Text genderText;
	private Text addressText;
	private Text postalText;
	private Text cityText;
	private Text countryText;
	private Text telText;
	private Text mobileText;
	private Text emailText;
	private Text tShirtText;

	//texts step two
	private Text chipText;
	private Text ownChipText;
	private Text buyChipText;
	private Text rentChipText;

	private float buyPrice = 2200;
	private float rentPrice = 200;
	private float buyPriceEuro = 36;
	private float rentPriceEuro = 3;

	private Text groupCompetitionText;
	private Text groupNameText;
	private Text bestTimeText;
	private Text goalTimeText;
	private Text agreementText;
	private Text agreeText;
	private Text disagreeText;

	//fields step one
	private DropdownMenu runDropdown = null;
	private DropdownMenu distanceDropdown = null;
	private RemoteScriptHandler rsh = null;
	private TextInput nameField;
	private DropdownMenu nationalityField;
	private TextInput ssnISField;
	private DateInput ssnField;
	private DropdownMenu genderField;
	private TextInput addressField;
	private TextInput postalField;
	private TextInput cityField;
	private DropdownMenu countryField;
	private TextInput telField;
	private TextInput mobileField;
	private TextInput emailField;
	private DropdownMenu tShirtField;

	private SelectOption small;
	private SelectOption medium;
	private SelectOption large;
	private SelectOption xlarge;
	private SelectOption xxlarge;

	//fields step two
	private RadioButton ownChipField;
	private RadioButton buyChipField;
	private RadioButton rentChipField;
	private TextInput chipNumberField;
	private CheckBox groupCompetitionField;
	private TextInput groupNameField;
	private TextInput bestTimeField;
	private TextInput goalTimeField;

	private Link backGreen;
	private Link backBlue;

	private IWResourceBundle iwrb;

	private boolean showPayment = false;
	
	private RadioButton agreeField;
	private RadioButton disagreeField;
	
	boolean isIcelandic = false;
	
	public Map getStyleNames() {
		Map map = new HashMap();
		map.put(STYLENAME_HEADING, "");
		map.put(STYLENAME_GREEN_TEXT, "");
		map.put(STYLENAME_GREEN_TEXT + ":hover", "");
		map.put(STYLENAME_BLUE_TEXT, "");
		map.put(STYLENAME_BLUE_TEXT + ":hover", "");
		map.put(STYLENAME_INTERFACE, "");
		map.put(STYLENAME_CHECKBOX, "");

		return map;
	}

	private void initializeTexts(IWContext iwc) {
		this.iwrb = getResourceBundle(iwc);
		this.redStar = new Text("*");
		this.redStar.setFontColor("#ff0000");
		this.infoRedStarText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_INFO_RED_STAR, "These fields must be filled out"));
		//step one texts begin
		this.distanceText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_PRIMARY_DD, "Run") + "/" + this.iwrb.getLocalizedString(IWMarathonConstants.RR_SECONDARY_DD, "Distance"));
		this.nameText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_NAME, "Name "));
		this.nationalityText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_NATIONALITY, "Nationality "));
		this.ssnText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_SSN, "SSN "));
		this.genderText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_GENDER, "Gender "));
		this.addressText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_ADDRESS, "Address"));
		this.postalText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_POSTAL, "Postal Code"));
		this.cityText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_CITY, "City"));
		this.countryText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_COUNTRY, "Country"));
		this.telText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_TEL, "Telephone"));
		this.mobileText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_MOBILE, "Mobile Phone"));
		this.emailText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_EMAIL, "Email "));
		this.tShirtText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_TSHIRT, "T-Shirt"));
		//step one texts end

		//step two texts begin
		this.chipText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_CHIP_TIME, "Championchip timing: "));
		this.ownChipText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_OWN_CHIP, "Own Chip"));
		this.buyChipText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_BUY_CHIP, "Buy Chip"));
		this.rentChipText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_RENT_CHIP, "Rent Chip"));
		this.groupCompetitionText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_GROUP_COMP, "Group competition"));
		this.groupNameText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_GROUP_NAME, "Group Name"));
		this.bestTimeText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_BEST_TIME, "Your best time running this distance"));
		this.goalTimeText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_GOAL_TIME, "Your goal in running this distance now"));
		this.agreementText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_AGREEMENT, "Agreement"));
		this.agreeText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_AGREE, "I agree"));
		this.disagreeText = new Text(this.iwrb.getLocalizedString(IWMarathonConstants.RR_DISAGREE, "I disagree"));
		//step two texts end
	}

	private void initializeFields(IWContext iwc) throws RemoteException {
		this.iwrb = getResourceBundle(iwc);

		//step one fields begin
		
		this.runDropdown = new DropdownMenu(IWMarathonConstants.GROUP_TYPE_RUN);
		RunBusiness runBiz = getRunBiz(iwc);
		Collection runs = runBiz.getRuns();
		this.runDropdown.addMenuElement("-1", this.iwrb.getLocalizedString("run_year_ddd.select_run","Select run..."));
		if(runs != null) {
			this.runDropdown.addMenuElements(runs);
		}
		IWTimestamp ts = IWTimestamp.RightNow();
    Integer y = new Integer(ts.getYear());
    String yearString = y.toString();
    
    this.distanceDropdown = new DropdownMenu(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
		if(IWMarathonConstants.GROUP_TYPE_RUN != null) {
			String runIdString = iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN);
			this.runDropdown.setSelectedElement(runIdString);
			if(runs != null) {
				if(runIdString != null && runIdString != "") {
					Group run = runBiz.getRunGroupByGroupId(Integer.valueOf(runIdString));
					if(run != null) {
						Collection distances = runBiz.getDistancesMap(run,yearString);
						if(distances != null) {
							this.distanceDropdown.addMenuElement("-1", this.iwrb.getLocalizedString("run_year_ddd.select_distance","Select distance..."));
							this.distanceDropdown.addMenuElements(distances);
						}
					}
				}
				
				
			}
		}
		this.rsh = new RemoteScriptHandler(this.runDropdown, this.distanceDropdown);
		try {
			this.rsh.setRemoteScriptCollectionClass(RunInputCollectionHandler.class);
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		this.nameField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_NAME), STYLENAME_INTERFACE);
		this.nameField.setAsAlphabeticText(this.iwrb.getLocalizedString("run_reg.name_err_msg", "Your name may only contain alphabetic characters"));
		this.nameField.setAsNotEmpty(this.iwrb.getLocalizedString("run_reg.name_not_empty", "Name field cannot be empty"));
		this.nameField.setInFocusOnPageLoad(true);
		this.nameField.setWidth(Table.HUNDRED_PERCENT);

		Collection countries = getRunBiz(iwc).getCountries();

		this.nationalityField = (DropdownMenu) getStyleObject(new DropdownMenu(IWMarathonConstants.PARAMETER_NATIONALITY), STYLENAME_INTERFACE);
		this.countryField = (DropdownMenu) getStyleObject(new DropdownMenu(IWMarathonConstants.PARAMETER_COUNTRY), STYLENAME_INTERFACE);
		SelectorUtility util = new SelectorUtility();
		if (countries != null && !countries.isEmpty()) {
			this.nationalityField = (DropdownMenu) util.getSelectorFromIDOEntities(this.nationalityField, countries, "getName");
			this.countryField = (DropdownMenu) util.getSelectorFromIDOEntities(this.countryField, countries, "getName");
		}
		if (this.isIcelandic) {
			this.nationalityField.setSelectedElement("104");
			this.countryField.setSelectedElement("104");
		}
		this.nationalityField.setWidth(Table.HUNDRED_PERCENT);
		this.nationalityField.addMenuElementFirst("-1", this.iwrb.getLocalizedString("run_reg.select_country", "Select country"));
		this.nationalityField.setAsNotEmpty(this.iwrb.getLocalizedString("run_reg.must_select_nationality", "You must select your nationality"));
		this.countryField.setWidth(Table.HUNDRED_PERCENT);
		this.countryField.addMenuElementFirst("-1", this.iwrb.getLocalizedString("run_reg.select_nationality", "Select ationality"));
		this.countryField.setAsNotEmpty(this.iwrb.getLocalizedString("run_reg.must_select_country", "You must select your country"));

		this.ssnISField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_SSN_IS), STYLENAME_INTERFACE);
		this.ssnISField.setAsIcelandicSSNumber(this.iwrb.getLocalizedString("run_reg.ssn_is_err_msg", "Your ssn is not a valid Icelandic ssn"));
		this.ssnISField.setAsNotEmpty(this.iwrb.getLocalizedString("run_reg.ssnIS_not_empty", "ssnIS may not be empty"));
		this.ssnISField.setLength(10);

		IWTimestamp stamp = new IWTimestamp();
		this.ssnField = (DateInput) getStyleObject(new DateInput(IWMarathonConstants.PARAMETER_SSN), STYLENAME_INTERFACE);
		//TODO: set the ssnField as either dateInput or set a error check on
		// the TextInput...
		this.ssnField.setAsNotEmpty("Date of birth can not be empty");
		this.ssnField.setYearRange(stamp.getYear(), stamp.getYear() - 100);

		this.genderField = (DropdownMenu) getStyleObject(new DropdownMenu(IWMarathonConstants.PARAMETER_GENDER), STYLENAME_INTERFACE);
		this.genderField.addMenuElement(IWMarathonConstants.PARAMETER_FEMALE, this.iwrb.getLocalizedString(IWMarathonConstants.RR_FEMALE, "Female"));
		this.genderField.addMenuElement(IWMarathonConstants.PARAMETER_MALE, this.iwrb.getLocalizedString(IWMarathonConstants.RR_MALE, "Male"));

		this.addressField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_ADDRESS), STYLENAME_INTERFACE);
		this.addressField.setWidth(Table.HUNDRED_PERCENT);
		if (!this.isIcelandic) {
			this.addressField.setAsNotEmpty(this.iwrb.getLocalizedString("run_reg.must_provide_address", "You must enter your address."));
		}

		this.postalField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_POSTAL), STYLENAME_INTERFACE);
		this.postalField.setMaxlength(7);
		this.postalField.setLength(7);
		if (!this.isIcelandic) {
			this.postalField.setAsNotEmpty(this.iwrb.getLocalizedString("run_reg.must_provide_postal", "You must enter your postal address."));
		}

		this.cityField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_CITY), STYLENAME_INTERFACE);
		this.cityField.setWidth(Table.HUNDRED_PERCENT);
		if (!this.isIcelandic) {
			this.cityField.setAsNotEmpty(this.iwrb.getLocalizedString("run_reg.must_provide_city", "You must enter your city of living."));
		}

		this.telField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_TEL), STYLENAME_INTERFACE);
		this.telField.setAsIntegers(this.iwrb.getLocalizedString("run_reg.tel_err_msg", "Phonenumber must be integers"));
		this.telField.setWidth(Table.HUNDRED_PERCENT);

		this.mobileField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_MOBILE), STYLENAME_INTERFACE);
		this.mobileField.setAsIntegers(this.iwrb.getLocalizedString("run_reg.mob_err_msg", "Mobilephonenumber must be integers"));
		this.mobileField.setWidth(Table.HUNDRED_PERCENT);

		this.emailField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_EMAIL), STYLENAME_INTERFACE);
		this.emailField.setAsEmail(this.iwrb.getLocalizedString("run_reg.email_err_msg", "Not a valid email address"));
		this.emailField.setWidth(Table.HUNDRED_PERCENT);
		if (!this.isIcelandic) {
			this.emailField.setAsNotEmpty(this.iwrb.getLocalizedString("run_reg.must_provide_email", "You must enter your e-mail address."));
		}
		
		this.tShirtField = (DropdownMenu) getStyleObject(new DropdownMenu(IWMarathonConstants.PARAMETER_TSHIRT), STYLENAME_INTERFACE);
		SelectOption empty = new SelectOption(this.iwrb.getLocalizedString("run_reg.select_tee_shirt_size", "Select shirt size..."), "-1");
		SelectOption selectAdult = new SelectOption(this.iwrb.getLocalizedString("run_reg.adult_sized", "Adult sizes"), "-1");
		this.small = new SelectOption("- " + this.iwrb.getLocalizedString("run_reg.small", "Small"), IWMarathonConstants.PARAMETER_TSHIRT_S);
		this.medium = new SelectOption("- " + this.iwrb.getLocalizedString("run_reg.medium", "Medium"), IWMarathonConstants.PARAMETER_TSHIRT_M);
		this.large = new SelectOption("- " + this.iwrb.getLocalizedString("run_reg.large", "Large"), IWMarathonConstants.PARAMETER_TSHIRT_L);
		this.xlarge = new SelectOption("- " + this.iwrb.getLocalizedString("run_reg.xlarge", "Larger"), IWMarathonConstants.PARAMETER_TSHIRT_XL);
		this.xxlarge = new SelectOption("- " + this.iwrb.getLocalizedString("run_reg.xxlarge", "Largest"), IWMarathonConstants.PARAMETER_TSHIRT_XXL);

		this.tShirtField.addOption(empty);
		this.tShirtField.addOption(selectAdult);
		this.tShirtField.addOption(this.small);
		this.tShirtField.addOption(this.medium);
		this.tShirtField.addOption(this.large);
		this.tShirtField.addOption(this.xlarge);
		this.tShirtField.addOption(this.xxlarge);

		SelectOption selectKids = new SelectOption(this.iwrb.getLocalizedString("run_reg.kids_sized", "Kids sizes"), "-1");
		SelectOption smallKids = new SelectOption("- " + this.iwrb.getLocalizedString("run_reg.small_kids", "Small"), IWMarathonConstants.PARAMETER_TSHIRT_S + "_kids");
		SelectOption mediumKids = new SelectOption("- " + this.iwrb.getLocalizedString("run_reg.medium_kids", "Medium"), IWMarathonConstants.PARAMETER_TSHIRT_M + "_kids");
		SelectOption largeKids = new SelectOption("- " + this.iwrb.getLocalizedString("run_reg.large_kids", "Large"), IWMarathonConstants.PARAMETER_TSHIRT_L + "_kids");
		SelectOption xlargeKids = new SelectOption("- " + this.iwrb.getLocalizedString("run_reg.xlarge_kids", "Larger"), IWMarathonConstants.PARAMETER_TSHIRT_XL + "_kids");

		this.tShirtField.addOption(selectKids);
		this.tShirtField.addOption(smallKids);
		this.tShirtField.addOption(mediumKids);
		this.tShirtField.addOption(largeKids);
		this.tShirtField.addOption(xlargeKids);

		this.tShirtField.setAsNotEmpty(this.iwrb.getLocalizedString("run_reg.must_select_shirt_size", "You must select shirt size"), "-1");

		//step one fields end

		//step two fields begin
		this.ownChipField = (RadioButton) getStyleObject(new RadioButton(IWMarathonConstants.PARAMETER_CHIP, IWMarathonConstants.PARAMETER_OWN_CHIP), STYLENAME_CHECKBOX);
		this.ownChipField.setMustBeSelected(this.iwrb.getLocalizedString("run_reg.must_select_chip_option", "You have to select a chip option."));
		
		this.buyChipField = (RadioButton) getStyleObject(new RadioButton(IWMarathonConstants.PARAMETER_CHIP, IWMarathonConstants.PARAMETER_BUY_CHIP), STYLENAME_CHECKBOX);

		this.rentChipField = (RadioButton) getStyleObject(new RadioButton(IWMarathonConstants.PARAMETER_CHIP, IWMarathonConstants.PARAMETER_RENT_CHIP), STYLENAME_CHECKBOX);

		this.chipNumberField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_CHIP_NUMBER), STYLENAME_INTERFACE);

		this.groupCompetitionField = (CheckBox) getStyleObject(new CheckBox(IWMarathonConstants.PARAMETER_GROUP_COMP), STYLENAME_CHECKBOX);

		this.groupNameField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_GROUP_NAME), STYLENAME_INTERFACE);
		this.groupNameField.setWidth("50%");

		this.bestTimeField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_BEST_TIME), STYLENAME_INTERFACE);
		this.bestTimeField.setWidth("50%");

		this.goalTimeField = (TextInput) getStyleObject(new TextInput(IWMarathonConstants.PARAMETER_GOAL_TIME), STYLENAME_INTERFACE);
		this.goalTimeField.setWidth("50%");
		
		this.agreeField = (RadioButton) getStyleObject(new RadioButton(IWMarathonConstants.PARAMETER_AGREEMENT, IWMarathonConstants.PARAMETER_AGREE), STYLENAME_CHECKBOX);
		
		this.disagreeField = (RadioButton) getStyleObject(new RadioButton(IWMarathonConstants.PARAMETER_AGREEMENT, IWMarathonConstants.PARAMETER_DISAGREE), STYLENAME_CHECKBOX);

		//step two fields end

		this.backGreen = getStyleLink(new Link(this.iwrb.getLocalizedString("run_reg.back", "Back")), STYLENAME_BLUE_TEXT);
		this.backGreen.setAsBackLink();
		this.backBlue = getStyleLink(new Link("&gt;&gt;"), STYLENAME_GREEN_TEXT);
		this.backBlue.setAsBackLink();
	}

	private void stepOne(IWContext iwc, Form f) {
		Table t = new Table();
		f.add(t);
		t.setColumns(2);
		t.setCellpadding(0);
		t.setCellspacing(0);
		t.setWidth(Table.HUNDRED_PERCENT);
		
		int row = 1;
		int column = 1;
		int formRow = -1;

		t.add(this.iwrb.getLocalizedString("step", "Step") + " 1 " + this.iwrb.getLocalizedString("of", "of") + " 2", column, row++);
		t.setHeight(row++, 12);

		t.mergeCells(column, row, t.getColumns(), row);
		t.add(this.redStar, column, row);
		t.add(Text.getNonBrakingSpace(), column, row);
		t.add(this.infoRedStarText, column, row++);

		t.setHeight(row++, 8);

		t.add(this.distanceText, column, row);
		t.add(this.redStar, column, row++);
		t.setHeight(row++, 3);
		t.mergeCells(column, row, t.getColumns(), row);
		t.add(this.runDropdown,column,row);
		t.add(this.distanceDropdown,column,row);
		add(this.rsh);

		t.setHeight(row++, 12);

		formRow = row;
		t.add(this.nameText, column, row);
		t.add(this.redStar, column, row++);
		t.setHeight(row++, 3);
		t.add(this.nameField, column, row++);

		t.setHeight(row++, 8);

		t.add(this.ssnText, column, row);
		t.add(this.redStar, column, row++);
		t.setHeight(row++, 3);
		if (iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale())) {
			t.add(this.ssnISField, column, row++);
		}
		else {
			t.add(this.ssnField, column, row++);
		}

		t.setHeight(row++, 8);

		t.add(this.addressText, column, row);
		if (!this.isIcelandic) {
			t.add(this.redStar, column, row);
		}
		row++;
		t.setHeight(row++, 3);
		t.add(this.addressField, column, row++);

		t.setHeight(row++, 8);

		t.add(this.cityText, column, row);
		if (!this.isIcelandic) {
			t.add(this.redStar, column, row);
		}
		row++;
		t.setHeight(row++, 3);
		t.add(this.cityField, column, row++);

		t.setHeight(row++, 8);

		t.add(this.postalText, column, row);
		if (!this.isIcelandic) {
			t.add(this.redStar, column, row);
		}
		row++;
		t.setHeight(row++, 3);
		t.add(this.postalField, column, row++);

		t.setHeight(row++, 8);

		t.add(this.countryText, column, row);
		if (!this.isIcelandic) {
			t.add(this.redStar, column, row);
		}
		row++;
		t.setHeight(row++, 3);
		t.add(this.countryField, column, row++);

		row = formRow;
		column = 2;

		t.add(this.genderText, column, row);
		t.add(this.redStar, column, row++);
		t.setHeight(row++, 3);
		t.add(this.genderField, column, row++);

		t.setHeight(row++, 8);

		t.add(this.nationalityText, column, row);
		t.add(this.redStar, column, row++);
		t.setHeight(row++, 3);
		t.add(this.nationalityField, column, row++);

		t.setHeight(row++, 8);

		t.add(this.emailText, column, row);
		if (!this.isIcelandic) {
			t.add(this.redStar, column, row);
		}
		row++;
		t.setHeight(row++, 3);
		t.add(this.emailField, column, row++);

		StringBuffer emailValidation = new StringBuffer();
		emailValidation.append("function isEmailEntered() {").append("\n\t");
		emailValidation.append("var email = findObj('"+IWMarathonConstants.PARAMETER_EMAIL+"');").append("\n\t");
		emailValidation.append("if (email.value == '') {").append("\n\t\t");
		emailValidation.append("return confirm('"+this.iwrb.getLocalizedString("run_reg.continue_without_email", "You can not continue without entering an e-mail")+"');").append("\n\t");
		emailValidation.append("}").append("\n");
		emailValidation.append("}");
		this.emailField.setOnSubmitFunction("isEmailEntered", emailValidation.toString());

		t.setHeight(row++, 8);

		t.add(this.telText, column, row++);
		t.setHeight(row++, 3);
		t.add(this.telField, column, row++);

		t.setHeight(row++, 8);

		t.add(this.mobileText, column, row++);
		t.setHeight(row++, 3);
		t.add(this.mobileField, column, row++);

		t.setHeight(row++, 8);

		t.add(this.tShirtText, column, row);
		t.add(this.redStar, column, row++);
		t.setHeight(row++, 3);
		t.add(this.tShirtField, column, row++);

		t.setHeight(row++, 18);

		Link stepTwoGreen = getStyleLink(new Link(this.iwrb.getLocalizedString("run_reg.submit_step_one", "Next step")), STYLENAME_BLUE_TEXT);
		stepTwoGreen.setFormToSubmit(f, true);
		Link stepTwoBlue = getStyleLink(new Link("&gt;&gt;"), STYLENAME_GREEN_TEXT);
		stepTwoBlue.setFormToSubmit(f, true);

		t.add(stepTwoGreen, 1, row);
		t.add(Text.getNonBrakingSpace(), 1, row);
		t.add(stepTwoBlue, 1, row);

		t.setWidth(1, "50%");
		t.setWidth(2, "50%");
		t.setColumnPaddingRight(1, 8);
		t.setColumnPaddingLeft(2, 8);

		f.addParameter(PARAMETER_ACTION, ACTION_STEP_TWO);
		f.keepStatusOnAction();
	}

	private void stepTwo(IWContext iwc, Form f) {
		Table t = new Table();
		t.setCellpadding(0);
		t.setCellspacing(0);
		int column = 1;
		int row = 1;
		
		t.add(this.iwrb.getLocalizedString("step", "Step") + " 2 " + this.iwrb.getLocalizedString("of", "of") + " 2", column, row++);
		t.setHeight(row++, 12);
		
		int threeKM = Integer.parseInt(this.iwrb.getIWBundleParent().getProperty("3_km_id", "126"));
		int distanceID = Integer.parseInt(iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE));
		
		if (threeKM != distanceID) {
			Currency ISK = Currency.getInstance("ISK");
			Currency EUR = Currency.getInstance("EUR");
	
			t.add(this.chipText, column, row);
			t.add(this.redStar, column, row++);
			t.setHeight(row++, 3);
			t.add(this.ownChipField, column, row);
			t.add(this.ownChipText, column, row);
			t.add(" ", column, row);
			t.add(this.chipNumberField, column, row++);
			t.setHeight(row++, 3);
			t.add(this.buyChipField, column, row);
			t.add(this.buyChipText, column, row);
			t.add(" - ", column, row);
			if (iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale())) {
				t.add(String.valueOf((int) this.buyPrice) + " " + ISK.getSymbol() + " " + this.iwrb.getLocalizedString("run_reg.special_buy_chip_offer", "(special price)"), column, row++);
			}
			else {
				t.add(String.valueOf((int) this.buyPriceEuro) + " " + EUR.getSymbol(), column, row++);
			}
			t.setHeight(row++, 3);
			t.add(this.rentChipField, column, row);
			t.add(this.rentChipText, column, row);
			t.add(" - ", column, row);
			if (iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale())) {
				t.add(String.valueOf((int) this.rentPrice) + " " + ISK.getSymbol(), column, row++);
			}
			else {
				t.add(String.valueOf((int) this.rentPriceEuro) + " " + EUR.getSymbol(), column, row++);
			}
			
			t.setHeight(row++, 8);
			
			String message = this.iwrb.getLocalizedString("run_reg.group_competition_info", "Information about group competition.");
			Text groupCompetitionInfo = new Text(TextFormatter.formatText(message));

			t.add(groupCompetitionInfo, column, row++);
			t.setHeight(row++, 3);
			t.add(this.groupCompetitionField, column, row);
			t.add(Text.getNonBrakingSpace(), column, row);
			t.add(this.groupCompetitionText, column, row++);

			t.setHeight(row++, 8);

			t.add(this.groupNameText, column, row++);
			t.setHeight(row++, 3);
			t.add(this.groupNameField, column, row++);

			t.setHeight(row++, 8);

			t.add(this.bestTimeText, column, row++);
			t.setHeight(row++, 3);
			t.add(this.bestTimeField, column, row++);

			t.setHeight(row++, 8);

			t.add(this.goalTimeText, column, row++);
			t.setHeight(row++, 3);
			t.add(this.goalTimeField, column, row++);
			
			t.setHeight(row++, 8);
		}

		this.agreeField.setMustBeSelected(this.iwrb.getLocalizedString("run_reg.must_agree_registration", "You have to agree/disagree on being fit to participate."));
		t.add(this.agreementText,column,row++);
		t.setHeight(row++, 3);
		t.add(this.agreeField,column,row);
		t.add(this.agreeText,column,row);
		t.add(Text.NON_BREAKING_SPACE + Text.NON_BREAKING_SPACE, column, row);
		t.add(this.disagreeField,column,row);
		t.add(this.disagreeText,column,row++);

		t.setHeight(row++, 18);

		Table buttonTable = new Table(3, 1);
		buttonTable.setCellpadding(0);
		buttonTable.setCellspacing(0);
		buttonTable.setWidth(2, 1, 12);
		Link finishGreen = getStyleLink(new Link(this.iwrb.getLocalizedString("run_reg.submit_step_two", "Next step")), STYLENAME_BLUE_TEXT);
		finishGreen.setFormToSubmit(f, true);
		Link finishBlue = getStyleLink(new Link("&gt;&gt;"), STYLENAME_GREEN_TEXT);
		finishBlue.setFormToSubmit(f, true);

		buttonTable.add(this.backGreen, 1, 1);
		buttonTable.add(Text.getNonBrakingSpace(), 1, 1);
		buttonTable.add(this.backBlue, 1, 1);
		buttonTable.add(finishGreen, 3, 1);
		buttonTable.add(Text.getNonBrakingSpace(), 3, 1);
		buttonTable.add(finishBlue, 3, 1);

		t.add(buttonTable, 1, row);

		f.addParameter(PARAMETER_ACTION, ACTION_SAVE);
		f.keepStatusOnAction();
		f.maintainAllParameters();
		f.add(t);
	}

	private void commitRegistration(IWContext iwc, Form f) throws RemoteException {
		RunBusiness runBiz = getRunBiz(iwc);

		//user info
		String name = iwc.getParameter(IWMarathonConstants.PARAMETER_NAME);
		String nationality = iwc.getParameter(IWMarathonConstants.PARAMETER_NATIONALITY);
		String ssnIS = iwc.getParameter(IWMarathonConstants.PARAMETER_SSN_IS);
		String ssn = iwc.getParameter(IWMarathonConstants.PARAMETER_SSN);
		IWTimestamp dateOfBirth = null;
		if (ssn != null) {
			dateOfBirth = new IWTimestamp(ssn);
			ssn = dateOfBirth.getDateString("ddMMyy");
		}

		//run info
		String run = iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN);
		String distance = iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
		IWTimestamp now = IWTimestamp.RightNow();
		Integer y = new Integer(now.getYear());
		String year = y.toString();

		String tshirt = iwc.getParameter(IWMarathonConstants.PARAMETER_TSHIRT);
		String chip = iwc.getParameter(IWMarathonConstants.PARAMETER_CHIP);
		String chipNumber = iwc.getParameter(IWMarathonConstants.PARAMETER_CHIP_NUMBER);
		String groupName = iwc.getParameter(IWMarathonConstants.PARAMETER_GROUP_NAME);
		String bestTime = iwc.getParameter(IWMarathonConstants.PARAMETER_BEST_TIME);
		String goalTime = iwc.getParameter(IWMarathonConstants.PARAMETER_GOAL_TIME);
		String agreement = iwc.getParameter(IWMarathonConstants.PARAMETER_AGREEMENT);
		boolean agrees = false;
		if (agreement != null) {
			agrees = new Boolean(agreement).booleanValue();
		}

		int sevenKM = Integer.parseInt(this.iwrb.getIWBundleParent().getProperty("7_km_id", "113"));
		int threeKM = Integer.parseInt(this.iwrb.getIWBundleParent().getProperty("3_km_id", "126"));

		int userID = -1;
		
		Table t = new Table();
		add(t);
		t.setCellpadding(0);
		t.setCellspacing(0);
		t.setWidth(Table.HUNDRED_PERCENT);
		int row = 1;

		Link backBlue = getStyleLink(new Link(this.iwrb.getLocalizedString("run_reg.back", "Back")), STYLENAME_BLUE_TEXT);
		Link backGreen = getStyleLink(new Link("&gt;&gt;"), STYLENAME_GREEN_TEXT);
		backBlue.setAsBackLink();
		backGreen.setAsBackLink();
		String message = "";
		String refNum = "";
		boolean isAlreadyRegistered = getRunBiz(iwc).isRegisteredInRun(Integer.parseInt(run), iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale()) ? ssnIS : ssn);
		if (!isAlreadyRegistered) {
			
			if (agrees) {
				if (ssnIS != null && !ssnIS.equals("")) {
					//userID = runBiz.saveUser(name, ssnIS, dateOfBirth, gender, address, postal, city, country, tel, mobile, email);
					refNum = ssnIS;
				}
				else if (ssn != null && !ssn.equals("")) {
					//userID = runBiz.saveUser(name, ssn, dateOfBirth, gender, address, postal, city, country, tel, mobile, email);
					refNum = ssn;
				}
		
				if (userID > 0) {
					runBiz.saveRun(userID, run, distance, year, nationality, tshirt, chip, chipNumber, groupName, bestTime, goalTime, iwc.getCurrentLocale());
				}
		
				message = this.iwrb.getLocalizedString("registration_received", "Your registration has been received.");
				Group runGroup = null;
				Group yearGroup = null;
				Group distanceGroup = null;
				try {
					runGroup = getGroupBusiness(iwc).getGroupByGroupID(Integer.parseInt(run));
					yearGroup = getGroupBusiness(iwc).getGroupByGroupID(Integer.parseInt(year));
					distanceGroup = getGroupBusiness(iwc).getGroupByGroupID(Integer.parseInt(distance));
					String participantNumber = null;
					
					try {
						User user = getGroupBusiness(iwc).getUserByID(userID);
						Participant participant = runBiz.getParticipantByRunAndYear(user, runGroup, yearGroup);
						participantNumber = String.valueOf(participant.getParticipantNumber());
					}
					catch (FinderException e) {
						log (e);
					}
					Object[] args = { name, this.iwrb.getLocalizedString(runGroup.getName(),runGroup.getName()), this.iwrb.getLocalizedString(distanceGroup.getName(),distanceGroup.getName()), this.iwrb.getLocalizedString(tshirt, tshirt), participantNumber };
					message = MessageFormat.format(this.iwrb.getLocalizedString("registration_received", "Your registration has been received."), args);
				}
				catch (RemoteException re) {
					log(re);
				}
				catch (FinderException fe) {
					log(fe);
				}
				
				Table buttonTable = new Table(3, 1);
				buttonTable.setCellpadding(0);
				buttonTable.setCellspacing(0);
				buttonTable.setWidth(2, 1, 12);
		
				Link payBlue = getStyleLink(new Link(this.iwrb.getLocalizedString("run_reg.pay", "Pay fee")), STYLENAME_BLUE_TEXT);
				Link payGreen = getStyleLink(new Link("&gt;&gt;"), STYLENAME_GREEN_TEXT);
				IWBundle bundle = this.iwrb.getIWBundleParent();
				if (runGroup != null && distanceGroup != null) {
					String travelURL = "travelURL_"+runGroup.getName()+"_"+distanceGroup.getName()+"_"+year+"_"+iwc.getCurrentLocale().toString();
					if (chip != null) {
						if (chip.equals(IWMarathonConstants.PARAMETER_BUY_CHIP)) {
							travelURL += "_buy";
						}
						else if (chip.equals(IWMarathonConstants.PARAMETER_RENT_CHIP)) {
							travelURL += "_rent";
						}
					}
					if ( (Integer.parseInt(distance) == sevenKM || Integer.parseInt(distance) == threeKM)&& ssnIS != null) {
						int age = getRunBiz(iwc).getAgeFromPersonalID(ssnIS);
						if (age > 0 && age <= 12) {
							travelURL += "_12";
						}
					}
					String URL = bundle.getProperty(travelURL, "#");
					if (URL.equals("#")) {
						this.showPayment = false;
					}
					payBlue.setURL(URL);
					payBlue.setTarget(Link.TARGET_NEW_WINDOW);
					payGreen.setURL(URL);
					payGreen.setTarget(Link.TARGET_NEW_WINDOW);
				}
				String referenceNumberParameter = bundle.getProperty(PARAMETER_REFERENCE_NUMBER, PARAMETER_REFERENCE_NUMBER_DEFAULT_VALUE );
				payBlue.setLocale(iwc.getCurrentLocale());
				payBlue.addParameter(referenceNumberParameter, refNum);
				payGreen.setLocale(iwc.getCurrentLocale());
				payGreen.addParameter(referenceNumberParameter, refNum);
		
				buttonTable.add(backBlue, 1, 1);
				buttonTable.add(Text.getNonBrakingSpace(), 1, 1);
				buttonTable.add(backGreen, 1, 1);
				if (this.showPayment) {
					buttonTable.add(payBlue, 3, 1);
					buttonTable.add(Text.getNonBrakingSpace(), 3, 1);
					buttonTable.add(payGreen, 3, 1);
				}
	
				t.setHeight(row++, 12);
				if (this.showPayment) {
					Text payText = new Text(this.iwrb.getLocalizedString("run_reg.registration_finished_pay", "Registration is now finished. You can pay the fee by clicking on the link below."));
					payText.setStyleAttribute("font-size", "12px");
					
					t.add(payText, 1, row++);
					t.add(payBlue, 1, row);
					t.add(Text.getNonBrakingSpace(), 1, row);
					t.add(payGreen, 1, row++);
					t.setHeight(row++, 12);
				}
				t.add(TextFormatter.formatText(message), 1, row++);
				t.setHeight(row++, 8);
				Link l = new Link(this.iwrb.getLocalizedString("run_reg.printable","Printable"));
				l.addParameter(IWMarathonConstants.PARAMETER_NAME,name);
				l.addParameter(IWMarathonConstants.GROUP_TYPE_RUN,run);
				l.addParameter(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE,distance);
				l.addParameter(IWMarathonConstants.PARAMETER_TSHIRT,tshirt);
				l.setWindowToOpen(RegistrationReceivedPrintable.class);
				t.add(l,1,row++);
		
				t.add(buttonTable, 1, row);
			}
			else {
				message = this.iwrb.getLocalizedString("run_reg.must_accept_conditions", "You must accept the conditions before you can register.");

				t.setHeight(row++, 12);
				t.add(TextFormatter.formatText(message), 1, row++);
				t.setHeight(row++, 8);
				t.add(backBlue, 1, row);
				t.add(Text.getNonBrakingSpace(), 1, row);
				t.add(backGreen, 1, row++);
			}
		}
		else {
			message = this.iwrb.getLocalizedString("run_reg.already_registered", "You have already registered for this run.");

			t.setHeight(row++, 12);
			t.add(TextFormatter.formatText(message), 1, row++);
			t.setHeight(row++, 8);
			t.add(backBlue, 1, row);
			t.add(Text.getNonBrakingSpace(), 1, row);
			t.add(backGreen, 1, row++);
		}
		
	}
	
	public void main(IWContext iwc) throws Exception {
		Form f = new Form();
		add(f);
		this.isIcelandic = iwc.getCurrentLocale().equals(LocaleUtil.getIcelandicLocale());
		initializeTexts(iwc);
		initializeFields(iwc);

		switch (parseAction(iwc)) {
			case ACTION_STEP_ONE:
				stepOne(iwc,f);
				break;
			case ACTION_STEP_TWO:
				stepTwo(iwc,f);
				break;
			case ACTION_SAVE:
				commitRegistration(iwc,f);
				break;
		}
		
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		else {
			return ACTION_STEP_ONE;
		}
	}

	public void setRentPrice(float price) {
		this.rentPrice = price;
	}

	public void setBuyPrice(float price) {
		this.buyPrice = price;
	}

	public void setRentPriceEuro(float price) {
		this.rentPriceEuro = price;
	}

	public void setBuyPriceEuro(float price) {
		this.buyPriceEuro = price;
	}

	public float getRentPrice() {
		return this.rentPrice;
	}

	public float getBuyPrice() {
		return this.buyPrice;
	}

	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}

	private RunBusiness getRunBiz(IWContext iwc) {
		RunBusiness business = null;
		try {
			business = (RunBusiness) IBOLookup.getServiceInstance(iwc, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			business = null;
		}
		return business;
	}

	private GroupBusiness getGroupBusiness(IWContext iwc) {
		try {
			return (GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	/**
	 * @param showPayment
	 *            The showPayment to set.
	 */
	public void setShowPayment(boolean showPayment) {
		this.showPayment = showPayment;
	}
}