package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.CharityBusiness;
import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.remotescripting.RemoteScriptCollection;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.remotescripting.RemoteScriptingResults;

public class YearMenuCharityMenuInputCollectionHandler extends PresentationObject implements RemoteScriptCollection {
	
	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}

	public RemoteScriptingResults getResults(IWContext iwc) {
		String sourceName = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME);
		String sourceID = iwc.getParameter(sourceName);
		return handleCharityUpdate(iwc, sourceName, sourceID);
	}
	
	private RemoteScriptingResults handleCharityUpdate(IWContext iwc, String sourceName, String yearIdString) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (yearIdString != null) {
		    CharityBusiness charityBiz = getCharityBiz(iwc);
		 
			try {
				Vector ids = new Vector();
			    Vector names = new Vector();
				
				Collection charities = charityBiz.getCharitiesByRunYearID(Integer.valueOf(yearIdString));
				
				Iterator charIt = charities.iterator();
				if (charIt.hasNext()) {
			    	ids.add("-1");
			    	names.add(iwrb.getLocalizedString("run_charity_dd.select_charity", "Select charity organization..."));
			    }
			    while (charIt.hasNext()) {
			    	Charity charity = (Charity) charIt.next();
				    ids.add(charity.getOrganizationalID());
				    names.add(charity.getName());
			    }
			    if (charities.isEmpty()) {
			    	ids.add("-1");
			    	names.add(iwrb.getLocalizedString("unavailable","Unavailable"));
				    } 
											
			    RemoteScriptingResults rsr = new RemoteScriptingResults(RemoteScriptHandler.getLayerName(sourceName, "id"), ids);
			    rsr.addLayer(RemoteScriptHandler.getLayerName(sourceName, "name"), names);
		
			    return rsr;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
			return null;
	}
	
//	private RunBusiness getRunBiz(IWContext iwc) {
//		RunBusiness business = null;
//		try {
//			business = (RunBusiness) IBOLookup.getServiceInstance(iwc, RunBusiness.class);
//		}
//		catch (IBOLookupException e) {
//			e.printStackTrace();
//		}
//		return business;
//	}
	
	private CharityBusiness getCharityBiz(IWContext iwc) {
		CharityBusiness business = null;
		try {
			business = (CharityBusiness) IBOLookup.getServiceInstance(iwc, CharityBusiness.class);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		return business;
	}
}
