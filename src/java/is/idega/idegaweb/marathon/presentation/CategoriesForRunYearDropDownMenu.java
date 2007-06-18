package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunCategoryBusiness;
import is.idega.idegaweb.marathon.data.RunCategory;

import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;

public class CategoriesForRunYearDropDownMenu extends DropdownMenu {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.marathon";
	private static final String PARAMETER_CATEGORY_DROPDOWN = "prm_category_dropdown";
	private Integer runYearID = null;
	
	public CategoriesForRunYearDropDownMenu() {
		this(PARAMETER_CATEGORY_DROPDOWN);
	}

	public CategoriesForRunYearDropDownMenu(String name) {
		this(name, null);
	}

	public CategoriesForRunYearDropDownMenu(String name, Integer runYearID) {
		super(name);
		this.runYearID = runYearID;
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		Collection charities;
		//if (runYearID == null) {
			charities = getRunCategoryBusiness(iwc).getAllCategories();
		//} else {
		//	charities = getRunCategoryBusiness(iwc).getCategoriesByRunYearID(this.runYearID);
		//}
		addMenuElement("-1", iwrb.getLocalizedString("run_category_dd.select_category", "Select department..."));
		if (charities != null) {
			Iterator iter = charities.iterator();
			while (iter.hasNext()) {
				RunCategory runCategory = (RunCategory) iter.next();
				addMenuElement(runCategory.getPrimaryKey().toString(), runCategory.getName());
			}
		}
	}
			
	protected RunCategoryBusiness getRunCategoryBusiness(IWApplicationContext iwac) {
		try {
			return (RunCategoryBusiness) IBOLookup.getServiceInstance(iwac, RunCategoryBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}
