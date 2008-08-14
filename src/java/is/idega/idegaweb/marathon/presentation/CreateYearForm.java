package is.idega.idegaweb.marathon.presentation;


import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.BooleanInput;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;

public class CreateYearForm extends Block {
	
	private static final String PARAMETER_ACTION = "marathon_prm_action";
	private static final String PARAMETER_MARATHON_PK = "prm_run_pk";
	private static final String PARAMETER_GROUP_ID = "ic_group_id";
	public static final String PARAMETER_SPONSORED_RUN = "sponsored_run";
	public static final String PARAMETER_CHARITY_ENABLED = "charity_enabled";
	public static final String PARAMETER_PLEDGED_BY_SPONSOR = "pledged_by_sponsor";
	public static final String PARAMETER_PLEDGED_BY_SPONSOR_GROUP = "pledged_by_sponsor_group";
	public static final String PARAMETER_PLEDGE_CURRENCY = "pledge_currency";
	public static final String PARAMETER_MINIMUM_AGE_FOR_RUN = "minimum_age_for_run";
	public static final String PARAMETER_MAXIMUM_AGE_FOR_RUN = "maximum_age_for_run";
	
	private static final int ACTION_SAVE = 4;

	private String runID = null;
	
	public CreateYearForm() {
		super();
	}
	public CreateYearForm (String runID) {
		super();
		this.runID = runID;
	}
	
	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		} else {
			return 0;
		}
	}
	
	public void main(IWContext iwc) throws Exception {
		
		switch (parseAction(iwc)) {
			case ACTION_SAVE:
				getRunBiz(iwc).createNewGroupYear(iwc, runID);
				//close();
		}
		
		IWResourceBundle iwrb = getResourceBundle(iwc);
		Group run = null;
		if (this.runID != null && !this.runID.equals("")) {
			int id = Integer.parseInt(this.runID);
			run = getGroupBiz(iwc).getGroupByGroupID(id);
		}

		Form form = new Form();
		form.maintainParameter(PARAMETER_MARATHON_PK);
		form.maintainParameter(PARAMETER_GROUP_ID);

		Table table = new Table();
		table.setCellpadding(3);
		int row = 1;
		int col = 2;
		
		table.add(new Text(iwrb.getLocalizedString("run_tab.year","Year")+": "), 1, row);
		table.mergeCells(2, 1, 9, 1);
		table.add(new TextInput("year"), 2, row++);
		table.setHeight(row++, 12);

		table.add(new Text(iwrb.getLocalizedString("run_tab.price_ISK", "Price (ISK)")), col++, row);
		table.add(new Text(iwrb.getLocalizedString("run_tab.price_EUR", "Price (EUR)")), col++, row);
		table.add(new Text(iwrb.getLocalizedString("run_tab.children_price_EUR", "Child Price (ISK)")), col++, row);
		table.add(new Text(iwrb.getLocalizedString("run_tab.children_price_EUR", "Child Price (EUR)")), col++, row);
		table.add(new Text(iwrb.getLocalizedString("run_tab.number_of_splits", "Number of splits")), col++, row);
		table.add(new Text(iwrb.getLocalizedString("run_tab.use_chip", "Use chip")), col++, row);
		table.add(new Text(iwrb.getLocalizedString("run_tab.family_discount", "Family discount")), col++, row);
		table.add(new Text(iwrb.getLocalizedString("run_tab.allows_groups", "Allows groups")), col++, row);
		table.add(new Text(iwrb.getLocalizedString("run_tab.transport_offered", "Bus trip offered")), col++, row++);
		String[] distances = getRunBiz(iwc).getDistancesForRun(run);
		if (distances != null) {
			for (int i = 0; i < distances.length; i++) {
				col = 1;
				String distance = distances[i];
				table.add(new Text(iwrb.getLocalizedString("distance." + distance, distance)), col++, row);
				
				TextInput text = new TextInput("price_isk");
				text.setLength(12);
				table.add(text, col++, row);
				
				text = new TextInput("price_eur");
				text.setLength(12);
				table.add(text, col++, row);
				
				text = new TextInput("price_children_isk");
				text.setLength(12);
				table.add(text, col++, row);
				
				text = new TextInput("price_children_eur");
				text.setLength(12);
				table.add(text, col++, row);

				DropdownMenu menu = new DropdownMenu("number_of_splits");
				menu.addMenuElement(0, "0");
				menu.addMenuElement(1, "1");
				menu.addMenuElement(2, "2");
				table.add(menu, col++, row);

				menu = new BooleanInput("use_chip");
				menu.setSelectedElement("Y");
				table.add(menu, col++, row);

				menu = new BooleanInput("family_discount");
				menu.setSelectedElement("N");
				table.add(menu, col++, row);

				menu = new BooleanInput("allows_groups");
				menu.setSelectedElement("N");
				table.add(menu, col++, row);
				
				menu = new BooleanInput("offers_transport");
				menu.setSelectedElement("N");
				table.add(menu, col++, row++);
			}
		}
		form.add(table);

		Layer charityDiv = new Layer(Layer.DIV);
		CheckBox charityEnabledCheck = new CheckBox(PARAMETER_CHARITY_ENABLED);
		Label charityEnabledLabel = new Label(iwrb.getLocalizedString("run_reg.charity_enabled", "Charity enabled"),charityEnabledCheck);
		charityDiv.add(charityEnabledCheck);
		charityDiv.add(charityEnabledLabel);
		
		table.setHeight(row++, 12);
		table.add(charityDiv, 1, row);
		
		Layer sponsorPledgeDiv = new Layer(Layer.DIV);
		TextInput sponsorPledgeInput = new TextInput(PARAMETER_PLEDGED_BY_SPONSOR);
		Label sponsorPledgeLabel = new Label(iwrb.getLocalizedString("run_reg.pledge_amount_from_sponsor", "Sponsor pledges per kilometer"),sponsorPledgeInput);
		sponsorPledgeDiv.add(sponsorPledgeLabel);
		sponsorPledgeDiv.add(sponsorPledgeInput);
		
		table.setHeight(row++, 12);
		table.add(sponsorPledgeDiv, 1, row);
		
		Layer sponsorPledgeGroupDiv = new Layer(Layer.DIV);
		TextInput sponsorPledgeGroupInput = new TextInput(PARAMETER_PLEDGED_BY_SPONSOR_GROUP);
		Label sponsorPledgeGroupLabel = new Label(iwrb.getLocalizedString("run_reg.pledge_amount_from_sponsor_group", "Sponsor pledges per kilometer for sponsor group"),sponsorPledgeGroupInput);
		sponsorPledgeGroupDiv.add(sponsorPledgeGroupLabel);
		sponsorPledgeGroupDiv.add(sponsorPledgeGroupInput);
		
		table.setHeight(row++, 12);
		table.add(sponsorPledgeGroupDiv, 1, row);
		
		SubmitButton create = new SubmitButton(iwrb.getLocalizedString("run_reg.save", "Save"), PARAMETER_ACTION, String.valueOf(ACTION_SAVE));
		table.setHeight(row++, 12);
		table.add(create, 1, row);
		
		add(form);
	}

	private GroupBusiness getGroupBiz(IWContext iwc) throws IBOLookupException {
		GroupBusiness business = (GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
		return business;
	}
	
	private RunBusiness getRunBiz(IWContext iwc) {
		RunBusiness business = null;
		try {
			business = (RunBusiness) IBOLookup.getServiceInstance(iwc, RunBusiness.class);
		} catch (IBOLookupException e) {
			business = null;
		}
		return business;
	}

	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}
}
