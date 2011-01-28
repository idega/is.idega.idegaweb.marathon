/*
 * $Id: RunInputCollectionHandler.java,v 1.14 2007/12/17 13:48:13 civilis Exp $
 * Created on Feb 14, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.business.Runner;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.remotescripting.RemoteScriptCollection;
import com.idega.presentation.remotescripting.RemoteScriptHandler;
import com.idega.presentation.remotescripting.RemoteScriptingResults;
import com.idega.user.data.Group;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;


/**
 * 
 *  Last modified: $Date: 2007/12/17 13:48:13 $ by $Author: civilis $
 * 
 * @author <a href="mailto:birna@idega.com">birna</a>
 * @version $Revision: 1.14 $
 */
public class RunInputCollectionHandler extends PresentationObject implements RemoteScriptCollection {

	//TODO parameter check member ID
	public static final String PARAMETER_USER_ID = "rich_uid";
	public static final String RUNNER_PERSONAL_ID = "rich_runner_pid";
	
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
				Collection distancesGroups = null;
				
				if (runId.intValue() != -1) {
					Group run = runBiz.getRunGroupByGroupId(runId);

					String runnerYearString = yearString;
					boolean finished = false;
					boolean foundYear = false;
					Map yearMap = runBiz.getYearsMap(run);
					Year year = (Year) yearMap.get(yearString);
					
					if (year != null && year.getLastRegistrationDate() != null && ts.isLaterThanOrEquals(new IWTimestamp(year.getLastRegistrationDate()))) {
						finished = true;
					} else { 
						foundYear = true;
					}
					Year nextYear = (Year) yearMap.get(nextYearString);
					
					if (finished && nextYear != null && nextYear.getLastRegistrationDate() != null && ts.isEarlierThan(new IWTimestamp(nextYear.getLastRegistrationDate()))) {
						runnerYearString = nextYearString;
						foundYear = true;
					}
					if (foundYear) {
						distancesGroups = runBiz.getDistancesMap(run, runnerYearString);
					}
				}
		
			    Vector ids = new Vector();
			    Vector names = new Vector();
			    
			    if (distancesGroups != null) {			    	
				    if (!distancesGroups.isEmpty()) {
				    	
				    	ids.add("-1");
				    	names.add(iwrb.getLocalizedString("run_year_ddd.select_distance","Select distance..."));
				    	
				    	Map runners = (Map) iwc.getSessionAttribute(Registration.SESSION_ATTRIBUTE_RUNNER_MAP);
					    String runnerPersonalId = iwc.getParameter(RUNNER_PERSONAL_ID);
					    
						Runner runner = runners == null || runnerPersonalId == null || CoreConstants.EMPTY.equals(runnerPersonalId) ? null :
										(Runner)runners.get(runnerPersonalId);
						
				    	List disallowedDistances;
						
						if(runner == null) {
							Logger.getLogger(this.getClassName()).log(Level.WARNING, "No runner resolved, therefore no filtering for distances drop down list");
							disallowedDistances = new ArrayList();							
						} else {
							List distances = new ArrayList(distancesGroups.size());
							ConverterUtility converterUtility = ConverterUtility.getInstance();
							
							for (Iterator distancesGroupsIterator = distancesGroups.iterator(); distancesGroupsIterator.hasNext();)
								distances.add(converterUtility.convertGroupToDistance((Group) distancesGroupsIterator.next()));

							disallowedDistances = runner.getDisallowedDistancesPKs(distances);
						}
						
						for (Iterator iterator = distancesGroups.iterator(); iterator.hasNext();) {							
							Group distanceGroup = (Group) iterator.next();
							
							if(disallowedDistances.contains(distanceGroup.getPrimaryKey().toString())) {							
						    	ids.add("-1");
						    	names.add(
						    			new StringBuffer(iwrb.getLocalizedString(distanceGroup.getName(), distanceGroup.getName()))
						    			.append(" ")
						    			.append(iwrb.getLocalizedString("runDistance.choiceNotAvailableBecauseOfAge", "(Not available for your age)"))
						    			.toString()
						    	);
							} else {
						    	ids.add(distanceGroup.getPrimaryKey().toString());
						    	names.add(iwrb.getLocalizedString(distanceGroup.getName(), distanceGroup.getName()));
							}
						}					    	
				    } else {
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
			} catch (Exception e) {
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
