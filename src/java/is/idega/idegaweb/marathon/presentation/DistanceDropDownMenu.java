package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.business.Runner;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.data.Year;
import java.util.Collection;
import java.util.Iterator;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.user.data.Group;

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



		addMenuElement("-1", iwrb.getLocalizedString("run_year_ddd.select_distance", "Select distance..."));
		Run run = null;
		if (this.runner != null && this.runner.getRun() != null) {
			run = this.runner.getRun();
		}
		else if (iwc.isParameterSet(PARAMETER_MARATHON_PK)) {
			Group gRun = getRunBusiness(iwc).getRunGroupByGroupId(Integer.valueOf(iwc.getParameter(PARAMETER_MARATHON_PK)));
			run = ConverterUtility.getInstance().convertGroupToRun(gRun);
		}
		if (run != null) {
			Year year = run.getCurrentRegistrationYear();
			String runnerYearString = year.getYearString();
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