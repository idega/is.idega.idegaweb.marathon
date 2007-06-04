package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.CharityBusiness;
import is.idega.idegaweb.marathon.data.Charity;

import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;

public class CharitiesForRunDropDownMenu extends DropdownMenu {
	
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.marathon";
	private static final String PARAMETER_CHARITY_DROPDOWN = "prm_charity_dropdown";
	
	public CharitiesForRunDropDownMenu() {
		this(PARAMETER_CHARITY_DROPDOWN);
	}

	public CharitiesForRunDropDownMenu(String name) {
		super(name);
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		Collection charities = getCharityBusiness(iwc).getCharities();
		addMenuElement("-1", iwrb.getLocalizedString("run_charity_dd.select_charity", "Select Charity Organization..."));
		if (charities != null) {
			Iterator iter = charities.iterator();
			while (iter.hasNext()) {
				Charity charity = (Charity) iter.next();
				addMenuElement(charity.getOrganizationalID(), charity.getName());
			}
		}
	}
			
	protected CharityBusiness getCharityBusiness(IWApplicationContext iwac) {
		try {
			return (CharityBusiness) IBOLookup.getServiceInstance(iwac, CharityBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}