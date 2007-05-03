/*
 * Created on Jul 13, 2004
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * 
 * @author birna
 */
public class CreateYearWindow extends StyledIWAdminWindow {
	
	private static final String PARAMETER_GROUP_ID = "ic_group_id";
	private static final String PARAMETER_ACTION = "marathon_prm_action";
	private static final int ACTION_SAVE = 4; //The action has the value 4 so it is compatable with logic in RunYearEditor
	
	public CreateYearWindow() {
		super();
		setWidth(730);
		setHeight(500);
	}
	
	public void main(IWContext iwc) throws Exception {
		String runID = iwc.getParameter(PARAMETER_GROUP_ID);
		CreateYearForm form = new CreateYearForm(runID);
		add(form, iwc);

		switch (parseAction(iwc)) {
			case ACTION_SAVE:
				getRunBiz(iwc).createNewGroupYear(iwc, runID);
				close();
		}
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		} else {
			return 0;
		}
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