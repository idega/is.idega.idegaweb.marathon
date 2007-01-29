package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.data.ShirtSize;
import is.idega.idegaweb.marathon.data.ShirtSizeHome;

import java.util.Collection;
import java.util.Iterator;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;

public class ShirtSizeDropdownMenu extends DropdownMenu {
	
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.marathon";
	private static final String PARAMETER_SHIRT_SIZE = "prm_shirt_size";
	
	public ShirtSizeDropdownMenu(){
		super(PARAMETER_SHIRT_SIZE); 
	}
	
	public ShirtSizeDropdownMenu(String parameterName){
		super(parameterName); 
	}

	public void main(IWContext iwc) throws Exception{
		super.main(iwc);
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		ShirtSizeHome shirtSizeHome = (ShirtSizeHome) IDOLookup.getHome(ShirtSize.class);

		Collection shirtSizes = shirtSizeHome.findAll();
		
		if (shirtSizes != null) {
			setAsNotEmpty(iwrb.getLocalizedString("run_reg.must_select_shirt_size", "You must select tee-shirt size"), "-1");
			Iterator iter = shirtSizes.iterator();
			while (iter.hasNext()) {
				ShirtSize shirtSize = (ShirtSize) iter.next();
				String name = shirtSize.getName();
				if(name!=null) {
					addMenuElement(name, iwrb.getLocalizedString(name,name));
				}
			}
		}
	}
	
		public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}
