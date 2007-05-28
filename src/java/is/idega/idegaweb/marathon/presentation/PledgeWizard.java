package is.idega.idegaweb.marathon.presentation;

import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;

public class PledgeWizard extends RunBlock {
	
	private static final String PARAMETER_ACTION = "prm_action";
	//private static final String PARAMETER_FROM_ACTION = "prm_from_action";
	//private static final String PARAMETER_PERSONAL_ID = "prm_personal_id";
	
	private static final int ACTION_STEP_ONE = 1;
	private static final int ACTION_STEP_TWO = 2;
	private static final int ACTION_STEP_THREE = 3;
	private static final int ACTION_STEP_FOUR = 4;
	private static final int ACTION_SAVE = 5;
	private static final int ACTION_CANCEL = 6;
	
	private boolean isIcelandic = false;
	
	public void main(IWContext iwc) throws Exception {

		switch (parseAction(iwc)) {
			case ACTION_STEP_ONE:
				stepOne(iwc);
				break;
			case ACTION_STEP_TWO:
				stepTwo(iwc);
				break;
			case ACTION_STEP_THREE:
				stepThree(iwc);
				break;
			case ACTION_STEP_FOUR:
				stepFour(iwc);
				break;
			case ACTION_SAVE:
				save(iwc, true);
				break;
			case ACTION_CANCEL:
				cancel(iwc);
				break;
		}
	}
	
	private int parseAction(IWContext iwc) throws RemoteException {
		int action = this.isIcelandic ? ACTION_STEP_ONE : ACTION_STEP_TWO;
		
		if (iwc.isParameterSet(PARAMETER_ACTION)) {
			action = Integer.parseInt(iwc.getParameter(PARAMETER_ACTION));
		}
		return action;
	}
	
	private void stepOne(IWContext iwc) {
		Form form = new Form();
		
		add(form);
	}
	
	private void stepTwo(IWContext iwc) {
	}

	private void stepThree(IWContext iwc) {
	}

	private void stepFour(IWContext iwc) {
	}
	
	private void save(IWContext iwc, boolean doPayment) throws RemoteException {
		
	}
	
	private void cancel(IWContext iwc) {
		//iwc.removeSessionAttribute(SESSION_ATTRIBUTE_RUNNER_MAP);
	}
}