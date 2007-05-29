package is.idega.idegaweb.marathon.presentation;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.business.Runner;
import is.idega.idegaweb.marathon.data.Year;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

public class DistanceDropDownMenu extends DropdownMenu {

	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.marathon";
	private static final String PARAMETER_DISTANCES = "prm_distances";
	private static final String PARAMETER_MARATHON_PK = "prm_run_pk";
	private Runner runner = null;

	public DistanceDropDownMenu() {
		this(PARAMETER_DISTANCES);
	}

	public DistanceDropDownMenu(String parameterName) {
		this(parameterName, null);
	}

	public DistanceDropDownMenu(String parameterName, Runner runner) {
		super(parameterName);
		this.runner = runner;
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		IWResourceBundle iwrb = this.getResourceBundle(iwc);

		IWTimestamp thisYearStamp = IWTimestamp.RightNow();
		String yearString = String.valueOf(thisYearStamp.getYear());
		IWTimestamp nextYearStamp = IWTimestamp.RightNow();
		nextYearStamp.addYears(1);
		String nextYearString = String.valueOf(nextYearStamp.getYear());

		addMenuElement("-1", iwrb.getLocalizedString("run_year_ddd.select_distance", "Select distance..."));
		Group run = null;
		if (this.runner != null && this.runner.getRun() != null) {
			run = this.runner.getRun();
		}
		else if (iwc.isParameterSet(PARAMETER_MARATHON_PK)) {
			run = getRunBusiness(iwc).getRunGroupByGroupId(Integer.valueOf(iwc.getParameter(PARAMETER_MARATHON_PK)));
		}
		if (run != null) {
			String runnerYearString = yearString;
			boolean finished = false;
			Map yearMap = getRunBusiness(iwc).getYearsMap(run);
			Year year = (Year) yearMap.get(yearString);
			if (year != null && year.getLastRegistrationDate() != null) {
				if (thisYearStamp.isLaterThanOrEquals(new IWTimestamp(year.getLastRegistrationDate()))) {
					finished = true;
				}
			}
			Year nextYear = (Year) yearMap.get(nextYearString);
			if (finished && nextYear != null) {
				runnerYearString = nextYearString;
			}

			Collection distances = getRunBusiness(iwc).getDistancesMap(run, runnerYearString);
			if (distances != null) {
				Iterator distanceIt = distances.iterator();
				while (distanceIt.hasNext()) {
					Group distance = (Group) distanceIt.next();
					addMenuElement(distance.getPrimaryKey().toString(), iwrb.getLocalizedString(distance.getName(), distance.getName()));
				}
			}
			if (this.runner != null && this.runner.getDistance() != null) {
				setSelectedElement(this.runner.getDistance().getPrimaryKey().toString());
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