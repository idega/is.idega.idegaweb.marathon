package is.idega.idegaweb.marathon.presentation.user.runoverview;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;

import com.idega.presentation.wizard.Wizard;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/12/19 21:49:49 $ by $Author: civilis $
 *
 */
public class DistanceChangeWizard extends Wizard {

	public static final String distanceChangeWizardBeanExp = "#{distanceChangeWizardBean}";
	public static final String distanceChangeWizardBean_distanceIdExp = "#{distanceChangeWizardBean.distanceId}";
	public static final String distanceChangeWizardBean_wizardModeExp = "#{distanceChangeWizardBean.wizardMode}";
	
	/**
	 * @Override
	 */
	public List getWizardSteps() {
		
		List wizardSteps = new ArrayList();
		wizardSteps.add(new DistanceChangeStep());
		wizardSteps.add(new PaymentStep());
		return wizardSteps;
	}
	
	/**
	 * @Override
	 */
	protected UIComponent getContainer(FacesContext context) {
		return context.getApplication().createComponent(HtmlForm.COMPONENT_TYPE);
	}
}