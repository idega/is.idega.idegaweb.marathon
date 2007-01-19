package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.data.ShirtSize;
import is.idega.idegaweb.marathon.data.ShirtSizeHome;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.SelectionBox;

public class ShirtSizeSelectionBox extends SelectionBox {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.marathon";
	
	public ShirtSizeSelectionBox() {
		super();
	}
	
	public ShirtSizeSelectionBox(String name) {
		super(name);
	}
	
	public void main(IWContext iwc) {
        initialize(iwc);
        super.main(iwc);
	}
	
	private void initialize(IWContext iwc) {
		try {
			IWResourceBundle iwrb = this.getResourceBundle(iwc);
			ShirtSizeHome shirtSizeHome = (ShirtSizeHome) IDOLookup.getHome(ShirtSize.class);

			Collection shirtSizes = shirtSizeHome.findAll();

			if (shirtSizes != null) {
				Iterator iter = shirtSizes.iterator();
				while (iter.hasNext()) {
					ShirtSize shirtSize = (ShirtSize) iter.next();
					String name = shirtSize.getName();
					if(name!=null) {
						addMenuElement(name, iwrb.getLocalizedString(name,name));
					}
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}