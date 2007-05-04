package is.idega.idegaweb.marathon.presentation;

import java.rmi.RemoteException;

import com.idega.presentation.IWContext;

public class RunDistanceEditor extends RunBlock {
	
	private static final String PARAMETER_ACTION = "marathon_prm_action";
	private static final String PARAMETER_MARATHON_DISTANCE_PK = "prm_run_year_pk";

	private static final int ACTION_VIEW = 1;
	private static final int ACTION_EDIT = 2;
	private static final int ACTION_NEW = 3;
	private static final int ACTION_SAVE = 4;
	
	public void main(IWContext iwc) throws Exception {
		init(iwc);
	}
	
	protected void init(IWContext iwc) throws Exception {
		switch (parseAction(iwc)) {
			case ACTION_VIEW:
				showList(iwc);
				break;

			case ACTION_EDIT:
				String distanceID = iwc.getParameter(PARAMETER_MARATHON_DISTANCE_PK);
				showEditor(iwc, distanceID);
				break;

			case ACTION_NEW:
				showEditor(iwc, null);
				break;

			case ACTION_SAVE:
				save(iwc);
				showList(iwc);
				break;
		}
	}

	public void showList(IWContext iwc) throws RemoteException {
		
	}
	
	public void showEditor(IWContext iwc, String distanceID) throws java.rmi.RemoteException {
		
	}

	public void save(IWContext iwc) throws java.rmi.RemoteException {
		
	}

	private int parseAction(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			return Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return ACTION_VIEW;
	}
}