/*
 * Created on Aug 19, 2004
 * 
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author birna
 * 
 */
public class UpdateRunInfoWindow extends StyledIWAdminWindow {

	private static final String PARAMETER_ACTION = "prm_action";
	private static final int ACTION_DISPLAY = 1;
	private static final int ACTION_SAVE = 2;

	// texts
	private Text userNameText;
	private Text runText;
	private Text distanceText;
	private Text groupText;
	private Text participantNumberText;
	private Text chipNumberText;
	private Text runTimeText;
	private Text chipTimeText;
	private Text tshirtText;
	private Text payMethodText;
	private Text payedAmountText;
	private Text teamNameText;

	// fields
	private Text userNameField;
	private Text runField;
	private Text distanceField;
	private Text groupField;
	private SubmitButton submitButton;
	private TextInput participantNumberField;
	private TextInput chipNumberField;
	private TextInput runTimeField;
	private TextInput chipTimeField;

	private DropdownMenu tShirtField;
	private SelectOption small;
	private SelectOption medium;
	private SelectOption large;
	private SelectOption xlarge;
	private SelectOption xxlarge;
	private SelectOption selectKids;
	private SelectOption smallKids;
	private SelectOption mediumKids;
	private SelectOption largeKids;
	private SelectOption xlargeKids;

	private DropdownMenu payMethodField;
	private SelectOption notPayed;
	private SelectOption credit;
	private SelectOption debet;
	private SelectOption cash;

	private TextInput payedAmountField;

	private TextInput teamNameField;

	private Form f;

	public UpdateRunInfoWindow() {
		super();
		setResizable(true);
	}

	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		this.userNameText = new Text(iwrb.getLocalizedString("run_tab.user_name", "User Name"));
		this.runText = new Text(iwrb.getLocalizedString("run_tab.run", "Run"));
		this.distanceText = new Text(iwrb.getLocalizedString("run_tab.distance", "Distance"));
		this.groupText = new Text(iwrb.getLocalizedString("run_tab.group", "Group"));
		this.participantNumberText = new Text(iwrb.getLocalizedString("run_tab.participant_nr", "Participant number"));
		this.chipNumberText = new Text(iwrb.getLocalizedString("run_tab.chip_nr", "Chip number"));
		this.runTimeText = new Text(iwrb.getLocalizedString("run_tab.run_time", "Run time"));
		this.chipTimeText = new Text(iwrb.getLocalizedString("run_tab.chip_time", "Chip time"));
		this.tshirtText = new Text(iwrb.getLocalizedString("run_tab.tshirt", "Tshirt size"));
		this.payMethodText = new Text(iwrb.getLocalizedString("run_tab.pay_method", "Pay method"));
		this.payedAmountText = new Text(iwrb.getLocalizedString("run_tab.payed_amount", "Payed amount"));
		this.teamNameText = new Text(iwrb.getLocalizedString("run_tab.team_name", "Team name"));
	}

	public void initializeFields() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		String userID = iwc.getParameter("ic_user_id");
		String selectedGroupID = iwc.getParameter("selected_ic_group_id");
		String runGroupID = iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN);
		User user = null;
		if (userID != null) {
			try {
				user = getUserBiz().getUser(Integer.parseInt(userID));
				if (user != null) {
					this.userNameField = new Text(user.getName());
				}
			}
			catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		if (runGroupID != null && !runGroupID.equals("")) {
			Group runGroup = null;
			try {
				runGroup = getGroupBiz().getGroupByGroupID(Integer.parseInt(runGroupID));
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			this.runField = new Text(iwrb.getLocalizedString(runGroup.getName(), runGroup.getName()));
		}
		Participant run = null;
		if (userID != null && selectedGroupID != null) {
			try {
				run = getRunBiz(iwc).getRunObjByUserAndGroup(Integer.parseInt(userID), Integer.parseInt(selectedGroupID));
				Group dis = getRunBiz(iwc).getDistanceByUserID(Integer.parseInt(userID));
				this.f.addParameter(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE, dis.getPrimaryKey().toString());
				if (userID != null && runGroupID != null) {
					run = getRunBiz(iwc).getRunObjByUserIDandDistanceID(Integer.parseInt(userID), Integer.parseInt(dis.getPrimaryKey().toString()));
				}
			}
			catch (Exception re) {
				log(re);
			}
		}

		this.participantNumberField = new TextInput(IWMarathonConstants.PARAMETER_PARTICIPANT_NUMBER);
		this.chipNumberField = new TextInput(IWMarathonConstants.PARAMETER_CHIP_NUMBER);
		this.tShirtField = new DropdownMenu(IWMarathonConstants.PARAMETER_TSHIRT);
		SelectOption empty = new SelectOption(iwrb.getLocalizedString("run_reg.select_tee_shirt_size", "Select tee-shirt size"), "-1");
		SelectOption selectAdult = new SelectOption(iwrb.getLocalizedString("run_reg.adult_sized", "Adult sizes"), "-1");
		this.small = new SelectOption("- " + iwrb.getLocalizedString("run_reg.small", "Small"), IWMarathonConstants.PARAMETER_TSHIRT_S);
		this.medium = new SelectOption("- " + iwrb.getLocalizedString("run_reg.medium", "Medium"), IWMarathonConstants.PARAMETER_TSHIRT_M);
		this.large = new SelectOption("- " + iwrb.getLocalizedString("run_reg.large", "Large"), IWMarathonConstants.PARAMETER_TSHIRT_L);
		this.xlarge = new SelectOption("- " + iwrb.getLocalizedString("run_reg.xlarge", "Larger"), IWMarathonConstants.PARAMETER_TSHIRT_XL);
		this.xxlarge = new SelectOption("- " + iwrb.getLocalizedString("run_reg.xxlarge", "Largest"), IWMarathonConstants.PARAMETER_TSHIRT_XXL);

		this.tShirtField.addOption(empty);
		this.tShirtField.addOption(selectAdult);
		this.tShirtField.addOption(this.small);
		this.tShirtField.addOption(this.medium);
		this.tShirtField.addOption(this.large);
		this.tShirtField.addOption(this.xlarge);
		this.tShirtField.addOption(this.xxlarge);

		this.selectKids = new SelectOption(iwrb.getLocalizedString("run_reg.kids_sized", "Kids sizes"), "-1");
		this.smallKids = new SelectOption("- " + iwrb.getLocalizedString("run_reg.small_kids", "Small"), IWMarathonConstants.PARAMETER_TSHIRT_S + "_kids");
		this.mediumKids = new SelectOption("- " + iwrb.getLocalizedString("run_reg.medium_kids", "Medium"), IWMarathonConstants.PARAMETER_TSHIRT_M + "_kids");
		this.largeKids = new SelectOption("- " + iwrb.getLocalizedString("run_reg.large_kids", "Large"), IWMarathonConstants.PARAMETER_TSHIRT_L + "_kids");
		this.xlargeKids = new SelectOption("- " + iwrb.getLocalizedString("run_reg.xlarge_kids", "Larger"), IWMarathonConstants.PARAMETER_TSHIRT_XL + "_kids");

		this.tShirtField.addOption(this.selectKids);
		this.tShirtField.addOption(this.smallKids);
		this.tShirtField.addOption(this.mediumKids);
		this.tShirtField.addOption(this.largeKids);
		this.tShirtField.addOption(this.xlargeKids);

		this.payMethodField = new DropdownMenu(IWMarathonConstants.PARAMETER_PAY_METHOD);
		this.notPayed = new SelectOption(iwrb.getLocalizedString("run_tab.not_payed", "Not payed"), "not_payed");
		this.credit = new SelectOption(iwrb.getLocalizedString("run_tab.credit", "Credit"), "credit");
		this.debet = new SelectOption(iwrb.getLocalizedString("run_tab.debet", "Debet"), "debet");
		this.cash = new SelectOption(iwrb.getLocalizedString("run_tab.cash", "Cash"), "cash");

		this.payMethodField.addOption(this.notPayed);
		this.payMethodField.addOption(this.credit);
		this.payMethodField.addOption(this.debet);
		this.payMethodField.addOption(this.cash);

		this.payedAmountField = new TextInput(IWMarathonConstants.PARAMETER_AMOUNT);

		this.teamNameField = new TextInput(IWMarathonConstants.PARAMETER_GROUP_NAME);

		this.runTimeField = new TextInput(IWMarathonConstants.PARAMETER_RUN_TIME);
		this.chipTimeField = new TextInput(IWMarathonConstants.PARAMETER_CHIP_TIME);

		if (run != null) {
			int disID = run.getRunDistanceGroupID();
			int grID = run.getRunGroupGroupID();
			Group dis = null;
			Group gr = null;
			try {
				if (disID != -1) {
					dis = getGroupBiz().getGroupByGroupID(disID);
				}
				if (grID != -1) {
					gr = getGroupBiz().getGroupByGroupID(grID);
				}
			}
			catch (IBOLookupException e) {
				e.printStackTrace();
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
			if (dis != null) {
				this.distanceField = new Text(iwrb.getLocalizedString(dis.getName(), dis.getName()));
			}
			if (gr != null) {
				this.groupField = new Text(iwrb.getLocalizedString(gr.getName(), gr.getName()));
			}

			this.participantNumberField.setContent(String.valueOf(run.getParticipantNumber()));
			this.chipNumberField.setContent(String.valueOf(run.getChipNumber()));
			this.tShirtField.setSelectedElement(run.getTShirtSize());
			this.payMethodField.setSelectedElement(run.getPayMethod());
			this.payedAmountField.setContent(run.getPayedAmount());
			if (run.getPayedAmount() != null && run.getPayMethod() == null) {
				this.payMethodField.setSelectedElement("credit");
			}
			this.teamNameField.setContent(run.getRunGroupName());
			this.runTimeField.setContent(String.valueOf(run.getRunTime()));
			this.chipTimeField.setContent(String.valueOf(run.getChipTime()));

		}
		this.submitButton = new SubmitButton(iwrb.getLocalizedString("run_tab.save", "Save"), PARAMETER_ACTION, Integer.toString(ACTION_SAVE));
		this.submitButton.setAsImageButton(true);

	}

	public boolean store(IWContext iwc) {
		String payMethod = iwc.getParameter(IWMarathonConstants.PARAMETER_PAY_METHOD);
		String payedAmount = iwc.getParameter(IWMarathonConstants.PARAMETER_AMOUNT);
		String participantNr = iwc.getParameter(IWMarathonConstants.PARAMETER_PARTICIPANT_NUMBER);
		String chipNr = iwc.getParameter(IWMarathonConstants.PARAMETER_CHIP_NUMBER);
		String teamName = iwc.getParameter(IWMarathonConstants.PARAMETER_GROUP_NAME);
		String runTime = iwc.getParameter(IWMarathonConstants.PARAMETER_RUN_TIME);
		String chipTime = iwc.getParameter(IWMarathonConstants.PARAMETER_CHIP_TIME);
		String userIDString = iwc.getParameter("ic_user_id");
		String groupIDString = iwc.getParameter("selected_ic_group_id");
		User user = null;
		if (userIDString != null && !userIDString.equals("")) {
			try {
				user = getUserBiz().getUser(Integer.parseInt(userIDString));
			}
			catch (NumberFormatException e1) {
				e1.printStackTrace();
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}

		int userID = -1;
		if (user != null) {
			userID = Integer.parseInt(user.getPrimaryKey().toString());
		}
		int groupID = -1;
		if (groupIDString != null && !groupIDString.equals("")) {
			groupID = Integer.parseInt(groupIDString);
		}
		try {
			getRunBiz(iwc).savePaymentByUserID(userID, payMethod, payedAmount);
			getRunBiz(iwc).updateParticipantAndChip(userID, participantNr, chipNr);
			getRunBiz(iwc).updateTeamName(userID, groupID, teamName);
			getRunBiz(iwc).updateRunAndChipTimes(userID, groupID, runTime, chipTime);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Table t = new Table();
		t.add(getResourceBundle(iwc).getLocalizedString("update.successful", "Update successful"));
		this.f.add(t);

		return true;
	}

	public void main(IWContext iwc) throws Exception {
		this.f = new Form();
		this.f.maintainAllParameters();
		initializeTexts();
		initializeFields();

		switch (parseAction(iwc)) {
			case ACTION_DISPLAY:
				lineUp(iwc);
				break;
			case ACTION_SAVE:
				store(iwc);
				break;
		}
		add(this.f, iwc);
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		else {
			return ACTION_DISPLAY;
		}
	}

	public void lineUp(IWContext iwc) {
		Table t = new Table();
		t.setCellpadding(0);
		t.setCellspacing(0);
		t.setStyleClass("main");
		t.add(this.userNameText + ": ", 1, 1);
		t.add(this.userNameField, 1, 2);
		t.add(this.runText + ": ", 2, 1);
		t.add(this.runField, 2, 2);
		t.setHeight(1, 3, 8);
		t.setHeight(2, 3, 8);
		t.add(this.distanceText + ": ", 1, 4);
		t.add(this.distanceField, 1, 5);
		t.add(this.groupText + ": ", 2, 4);
		t.add(this.groupField, 2, 5);
		t.setHeight(1, 6, 8);
		t.setHeight(2, 6, 8);
		t.add(this.payMethodText + ": ", 1, 7);
		t.add(this.payMethodField, 1, 8);
		t.add(this.payedAmountText + ": ", 2, 7);
		t.add(this.payedAmountField, 2, 8);
		t.setHeight(1, 9, 8);
		t.setHeight(2, 9, 8);
		t.add(this.participantNumberText + ": ", 1, 10);
		t.add(this.participantNumberField, 1, 11);
		t.add(this.chipNumberText + ": ", 2, 10);
		t.add(this.chipNumberField, 2, 11);
		t.setHeight(1, 12, 8);
		t.setHeight(2, 12, 8);
		t.add(this.tshirtText + ": ", 1, 13);
		t.add(this.tShirtField, 1, 14);
		t.add(this.teamNameText + ": ", 2, 13);
		t.add(this.teamNameField, 2, 14);
		t.setHeight(1, 15, 8);
		t.setHeight(2, 15, 8);
		t.add(this.runTimeText + ": ", 1, 16);
		t.add(this.runTimeField, 1, 17);
		t.add(this.chipTimeText + ": ", 2, 16);
		t.add(this.chipTimeField, 2, 17);
		t.setAlignment(2, 18, Table.HORIZONTAL_ALIGN_RIGHT);
		t.add(this.submitButton, 2, 18);
		this.f.add(t);
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

	private UserBusiness getUserBiz() throws IBOLookupException {
		UserBusiness business = (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		return business;
	}

	private GroupBusiness getGroupBiz() throws IBOLookupException {
		GroupBusiness business = (GroupBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), GroupBusiness.class);
		return business;
	}
}