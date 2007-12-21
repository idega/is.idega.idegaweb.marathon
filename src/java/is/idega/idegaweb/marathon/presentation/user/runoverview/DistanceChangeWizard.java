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
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2007/12/21 17:19:19 $ by $Author: civilis $
 *
 */
public class DistanceChangeWizard extends Wizard {

	public static final String distanceChangeWizardBeanExp = "#{distanceChangeWizardBean}";
	public static final String distanceChangeWizardBean_newDistanceExp = "#{distanceChangeWizardBean.newDistanceId}";
	
	public static final String distanceChangeStepBeanExp = "#{distanceChangeStepBean}";
	public static final String distanceChangeStepBean_wizardModeExp = "#{distanceChangeStepBean.wizardMode}";
	public static final String distanceChangeStepBean_runLabelExp = "#{distanceChangeStepBean.runLabel}";
	
	public static final String distanceChangeStepBean_runDistancesExp = "#{distanceChangeStepBean.runDistances}";
	public static final String distanceChangeStepBean_chosenDistanceNameExp = "#{distanceChangeStepBean.chosenDistanceName}";
	
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