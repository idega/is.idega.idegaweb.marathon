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
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/12/23 17:54:49 $ by $Author: civilis $
 *
 */
public class UIDistanceChangeWizard extends Wizard {

	public static final String COMPONENT_TYPE = "idega_DistanceChangeWizard";
	
	public static final String distanceChangeWizardBeanExp = "#{distanceChangeWizardBean}";
	public static final String distanceChangeWizardBean_newDistanceExp = "#{distanceChangeWizardBean.newDistanceId}";
	public static final String distanceChangeWizardBean_cardHolderNameExp = "#{distanceChangeWizardBean.cardHolderName}";
	public static final String distanceChangeWizardBean_cardHolderEmailExp = "#{distanceChangeWizardBean.cardHolderEmail}";
	public static final String distanceChangeWizardBean_creditCardNumberExp = "#{distanceChangeWizardBean.creditCardNumber}";
	public static final String distanceChangeWizardBean_ccvNumberExp = "#{distanceChangeWizardBean.ccvNumber}";
	public static final String distanceChangeWizardBean_cardExpirationDateExp = "#{distanceChangeWizardBean.cardExpirationDate}";
	
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
		wizardSteps.add(new UIDistanceChangeStep());
		wizardSteps.add(new UIPaymentStep());
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