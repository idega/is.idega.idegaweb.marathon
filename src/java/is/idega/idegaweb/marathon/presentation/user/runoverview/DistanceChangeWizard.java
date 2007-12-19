package is.idega.idegaweb.marathon.presentation.user.runoverview;

import java.util.ArrayList;
import java.util.List;

import com.idega.presentation.wizard.Wizard;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/12/19 18:07:42 $ by $Author: civilis $
 *
 */
public class DistanceChangeWizard extends Wizard {
	
	/**
	 * @Override
	 */
	public List getWizardSteps() {
		
		List wizardSteps = new ArrayList();
		wizardSteps.add(new DistanceChangeStep());
		wizardSteps.add(new PaymentStep());
		return wizardSteps;
	}
}