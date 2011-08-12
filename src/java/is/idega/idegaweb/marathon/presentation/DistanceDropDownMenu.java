package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.business.Runner;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.data.Year;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.data.Group;
import com.idega.util.CoreConstants;

public class DistanceDropDownMenu extends DropdownMenu {

	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.marathon";
//	private static final String PARAMETER_DISTANCES = "prm_distances";
	private static final String PARAMETER_MARATHON_PK = "prm_run_pk";
	
	private Run run;
	private Group distance;
	private Runner runner;

	public DistanceDropDownMenu(String parameterName) {
		super(parameterName);
	}

	public DistanceDropDownMenu(String parameterName, Runner runner) {
		super(parameterName);
		this.runner = runner;
		this.run = runner.getRun();
		this.distance = runner.getDistance();
	}
	
	public DistanceDropDownMenu(String parameterName, Participant participant) {
		super(parameterName);
		try {
			this.run = ConverterUtility.getInstance().convertGroupToRun(participant.getRunTypeGroup());
			this.distance = participant.getRunDistanceGroup();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void main(IWContext iwc) throws Exception {		
		super.main(iwc);
		
		IWResourceBundle iwrb = this.getResourceBundle(iwc);

		addMenuElement("-1", iwrb.getLocalizedString("run_year_ddd.select_distance", "Select distance..."));
		
		if (this.run == null && iwc.isParameterSet(PARAMETER_MARATHON_PK)) {
			Group gRun = getRunBusiness(iwc).getRunGroupByGroupId(Integer.valueOf(iwc.getParameter(PARAMETER_MARATHON_PK)));
			run = ConverterUtility.getInstance().convertGroupToRun(gRun);
		}
		
		if (this.run != null) {
			
			Year year = this.runner != null ? this.runner.getYear() : null;;
			String runnerYearString = year != null ? year.getYearString() : null;
			Collection distancesGroups = runnerYearString != null ? getRunBusiness(iwc).getDistancesMap(run, runnerYearString) : null;
			
			if (distancesGroups != null) {
				
				List disallowedDistances;
				
				if(runner == null) {
					Logger.getLogger(this.getClassName()).log(Level.WARNING, "No runner resolved, therefore no filtering for distances drop down list");
					disallowedDistances = new ArrayList();
					
				} else {
					
					List distances = new ArrayList(distancesGroups.size());
					ConverterUtility converterUtility = ConverterUtility.getInstance();
					
					for (Iterator distancesIterator = distancesGroups.iterator(); distancesIterator.hasNext();)
						distances.add(converterUtility.convertGroupToDistance((Group) distancesIterator.next()));

					disallowedDistances = runner.getDisallowedDistancesPKs(distances);
				}
				
				for (Iterator iterator = distancesGroups.iterator(); iterator.hasNext();) {
					
					Group distanceGroup = (Group) iterator.next();
					
					if(disallowedDistances.contains(distanceGroup.getPrimaryKey().toString())) {
					
						addMenuElement("-1", 
							new StringBuilder(iwrb.getLocalizedString(distanceGroup.getName(), distanceGroup.getName()))
								.append(CoreConstants.EMPTY)
								.append(iwrb.getLocalizedString("runDistance.choiceNotAvailableBecauseOfAge", "(Not available for your age)"))
								.toString()
						);
					
					} else {
						
						addMenuElement(distanceGroup.getPrimaryKey().toString(), iwrb.getLocalizedString(distanceGroup.getName(), distanceGroup.getName()));
					}
				}
			}
			if (this.distance != null) {
				setSelectedElement(this.distance.getPrimaryKey().toString());
			}
		}
	}
	
	protected RunBusiness getRunBusiness(IWApplicationContext iwac) {
		try {
			return (RunBusiness) IBOLookup.getServiceInstance(iwac, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}