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
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/12/21 15:05:02 $ by $Author: civilis $
 *
 */
public class DistanceChangeWizard extends Wizard {

	public static final String distanceChangeWizardBeanExp = "#{distanceChangeWizardBean}";
	//public static final String distanceChangeWizardBean_participantIdExp = "#{distanceChangeWizardBean.participantId}";
	
	public static final String distanceChangeStepBeanExp = "#{distanceChangeStepBean}";
	public static final String distanceChangeStepBean_wizardModeExp = "#{distanceChangeStepBean.wizardMode}";
	public static final String distanceChangeStepBean_runLabelExp = "#{distanceChangeStepBean.runLabel}";
	public static final String distanceChangeStepBean_newDistanceExp = "#{distanceChangeStepBean.newDistanceId}";
	public static final String distanceChangeStepBean_runDistancesExp = "#{distanceChangeStepBean.runDistances}";
	
	
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
		
		HtmlForm form = (HtmlForm)context.getApplication().createComponent(HtmlForm.COMPONENT_TYPE);
		form.setId(context.getViewRoot().createUniqueId());
		
		return form;
	}
}