package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.business.RunCategoryBusiness;
import is.idega.idegaweb.marathon.data.RunCategory;
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
import com.idega.user.data.Group;

public class DistanceMenuCategoriesMenuInputCollectionHandler extends PresentationObject implements RemoteScriptCollection {
	
	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}

	public RemoteScriptingResults getResults(IWContext iwc) {
		String sourceName = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME+"_1");
		String sourceID = iwc.getParameter(sourceName);
		return handleCategoriesUpdate(iwc, sourceName, sourceID);
	}
	
	private RemoteScriptingResults handleCategoriesUpdate(IWContext iwc, String sourceName, String distanceIdString) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (distanceIdString != null) {
			Integer distanceID = Integer.valueOf(distanceIdString);
		    RunBusiness runBiz = getRunBiz(iwc);
		    RunCategoryBusiness runCategoryBiz = getRunCategoryBiz(iwc);
		 
			try {
				Vector ids = new Vector();
			    Vector names = new Vector();
				
				Group runDistance = runBiz.getRunGroupByGroupId(distanceID);
				Group runYear = (Group)runDistance.getParentGroups().iterator().next();
			    //Collection categories = runCategoryBiz.getCategoriesByRunYearID((Integer)runYear.getPrimaryKey());
				Collection categories = runCategoryBiz.getAllCategories();
				
				Iterator catIt = categories.iterator();
				if (catIt.hasNext()) {
			    	ids.add("-1");
			    	names.add(iwrb.getLocalizedString("run_category_dd.select_category", "Select category..."));
			    }
			    while (catIt.hasNext()) {
			    	RunCategory category = (RunCategory) catIt.next();
				    ids.add(category.getPrimaryKey());
				    names.add(category.getName());
			    }
			    if (categories.isEmpty()) {
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
	
	private RunBusiness getRunBiz(IWContext iwc) {
		RunBusiness business = null;
		try {
			business = (RunBusiness) IBOLookup.getServiceInstance(iwc, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		return business;
	}
	
	private RunCategoryBusiness getRunCategoryBiz(IWContext iwc) {
		RunCategoryBusiness business = null;
		try {
			business = (RunCategoryBusiness) IBOLookup.getServiceInstance(iwc, RunCategoryBusiness.class);
		}
		catch (IBOLookupException e) {
			e.printStackTrace();
		}
		return business;
	}
}