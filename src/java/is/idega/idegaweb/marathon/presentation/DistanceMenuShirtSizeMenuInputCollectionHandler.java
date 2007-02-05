package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.util.Iterator;
import java.util.List;
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
import com.idega.util.ListUtil;

public class DistanceMenuShirtSizeMenuInputCollectionHandler extends PresentationObject implements RemoteScriptCollection {

	private static final String PARAMETER_SHIRT_SIZES_PER_RUN = "shirt_sizes_per_run";
	
	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}

	public RemoteScriptingResults getResults(IWContext iwc) {
		String sourceName = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME);
		String sourceID = iwc.getParameter(sourceName);
		return handleShirtSizeUpdate(iwc, sourceName, sourceID);
	}
	
	private RemoteScriptingResults handleShirtSizeUpdate(IWContext iwc, String sourceName, String distanceIdString) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (distanceIdString != null) {
			Integer distanceID = Integer.valueOf(distanceIdString);
		    RunBusiness runBiz = getRunBiz(iwc);
		 
			try {
				Vector ids = new Vector();
			    Vector names = new Vector();
				
				Group runDistance = runBiz.getRunGroupByGroupId(distanceID);
				String shirtSizeMetadata = runDistance.getMetaData(PARAMETER_SHIRT_SIZES_PER_RUN);
				List shirtSizes = null;
				if (shirtSizeMetadata != null) {
					shirtSizes = ListUtil. convertCommaSeparatedStringToList(shirtSizeMetadata);
					Iterator shirtIt = shirtSizes.iterator();
					//ShirtSizeHome shirtSizeHome = (ShirtSizeHome) IDOLookup.getHome(ShirtSize.class);

					if (shirtIt.hasNext()) {
				    	ids.add("-1");
				    	names.add(iwrb.getLocalizedString("run_distance_dd.select_shirt_size","Select shirt size..."));
				    }
				    while (shirtIt.hasNext()) {
				    	String shirtSizeKey = (String) shirtIt.next();
				    	//ShirtSize shirtSize = shirtSizeHome.findByPrimaryKey(shirtSizeKey);
					    //String s = iwrb.getLocalizedString(shirtSize.getName(),shirtSize.getName());
					    ids.add(shirtSizeKey);
					    names.add(iwrb.getLocalizedString("shirt_size."+shirtSizeKey,shirtSizeKey));
				    }
				    if (shirtSizes.isEmpty()) {
				    	ids.add("-1");
				    	names.add(iwrb.getLocalizedString("unavailable","Unavailable"));
				    } 
				} else {
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
			business = null;
		}
		return business;
	}
}