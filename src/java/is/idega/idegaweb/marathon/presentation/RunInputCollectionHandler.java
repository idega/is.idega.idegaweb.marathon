/*
 * $Id: RunInputCollectionHandler.java,v 1.9 2007/01/17 12:53:37 idegaweb Exp $
 * Created on Feb 14, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
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
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * 
 *  Last modified: $Date: 2007/01/17 12:53:37 $ by $Author: idegaweb $
 * 
 * @author <a href="mailto:birna@idega.com">birna</a>
 * @version $Revision: 1.9 $
 */
public class RunInputCollectionHandler extends PresentationObject implements RemoteScriptCollection {

	//TODO parameter check member ID
	public static final String PARAMETER_USER_ID = "rich_uid";
	
	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}

	public RemoteScriptingResults getResults(IWContext iwc) {
		String sourceName = iwc.getParameter(RemoteScriptHandler.PARAMETER_SOURCE_PARAMETER_NAME);

		String sourceID = iwc.getParameter(sourceName);

	  return handleDistanceUpdate(iwc, sourceName, sourceID);
	}
	
	private RemoteScriptingResults handleDistanceUpdate(IWContext iwc, String sourceName, String runIdString) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		if (runIdString != null) {
			Integer runId = Integer.valueOf(runIdString);
		    RunBusiness runBiz = getRunBiz(iwc);
				IWTimestamp ts = IWTimestamp.RightNow();
		    Integer y = new Integer(ts.getYear());
		    String yearString = y.toString();
		    IWTimestamp stamp = new IWTimestamp();
		    stamp.addYears(1);
		    String nextYearString = String.valueOf(stamp.getYear());
		 
				try {
					Group run = runBiz.getRunGroupByGroupId(runId);

					String runnerYearString = yearString;
					boolean finished = false;
					Map yearMap = runBiz.getYearsMap(run);
					Year year = (Year) yearMap.get(yearString);
					if (year != null && year.getLastRegistrationDate() != null) {
						if (ts.isLaterThanOrEquals(stamp)) {
							finished = true;
						}
					}
					Year nextYear = (Year) yearMap.get(nextYearString);
					if (finished && nextYear != null) {
						runnerYearString = nextYearString;
					}
					Collection distances = runBiz.getDistancesMap(run, runnerYearString);
		
			    Vector ids = new Vector();
			    Vector names = new Vector();
			    
			    User user = null;
			    if (iwc.isParameterSet(PARAMETER_USER_ID)) {
			    	user = getRunBiz(iwc).getUserBiz().getUser(new Integer(iwc.getParameter(PARAMETER_USER_ID)));
			    }
			    if (distances != null) {
				    Iterator disIter = distances.iterator();
				    if (disIter.hasNext()) {
					    	ids.add("");
					    	names.add(iwrb.getLocalizedString("run_year_ddd.select_distance","Select distance..."));
				    }
				    while (disIter.hasNext()) {
				    		Group distance = (Group) disIter.next();
				    		boolean add = true;
				    		if (user != null && distance.getName().equals(IWMarathonConstants.DISTANCE_1_5)) {
				    			int age = getRunBiz(iwc).getAgeFromPersonalID(user.getPersonalID());
				    			if (age > 11) {
				    				add = false;
				    			}
				    		}
				    		if (add) {
	 				    		String s = iwrb.getLocalizedString(distance.getName(),distance.getName());
					    		ids.add(distance.getPrimaryKey().toString());
					    		names.add(s);
				    		}
				    }
				    if (distances.isEmpty()) {
				    		ids.add("");
				    		names.add(iwrb.getLocalizedString("unavailable","Unavailable"));
				    } 
			    }
			    else {
			    		ids.add("");
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
