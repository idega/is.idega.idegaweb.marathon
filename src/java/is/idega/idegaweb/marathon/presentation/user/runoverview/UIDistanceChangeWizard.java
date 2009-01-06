package is.idega.idegaweb.marathon.presentation.user.runoverview;

import java.util.ArrayList;
import java.util.List;

import com.idega.presentation.wizard.Wizard;
import com.idega.presentation.wizard.WizardStep;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.9 $
 *
 * Last modified: $Date: 2009/01/06 09:41:37 $ by $Author: palli $
 *
 */
public class UIDistanceChangeWizard extends Wizard {

	public static final String COMPONENT_TYPE = "idega_DistanceChangeWizard";
	
	public static final String distanceChangeWizardBeanExp = 						"#{distanceChangeWizardBean}";
	public static final String distanceChangeWizardBean_newDistanceExp = 			"#{distanceChangeWizardBean.newDistanceId}";
	//public static final String distanceChangeWizardBean_newShirtSizeExp = 			"#{distanceChangeWizardBean.newTShirtId}";
	public static final String distanceChangeWizardBean_newDistanceNameExp = 		"#{distanceChangeWizardBean.newDistanceName}";
	public static final String distanceChangeWizardBean_cardHolderNameExp = 		"#{distanceChangeWizardBean.cardHolderName}";
	public static final String distanceChangeWizardBean_creditCardNumberExp = 		"#{distanceChangeWizardBean.creditCardNumber}";
	public static final String distanceChangeWizardBean_ccvNumberExp = 				"#{distanceChangeWizardBean.ccvNumber}";
	public static final String distanceChangeWizardBean_cardExpirationDateExp = 	"#{distanceChangeWizardBean.cardExpirationDate}";
	public static final String distanceChangeWizardBean_distanceChangePriceLabelExp = 	"#{distanceChangeWizardBean.distanceChangePrice.priceLabel}";
	public static final String distanceChangeWizardBean_submitDistanceChangeExp =	"#{distanceChangeWizardBean.submitDistanceChange}";
	
	public static final String distanceChangeStepBeanExp = 							"#{distanceChangeStepBean}";
	public static final String distanceChangeStepBean_wizardModeExp = 				"#{distanceChangeStepBean.wizardMode}";
	public static final String distanceChangeStepBean_runLabelExp = 				"#{distanceChangeStepBean.runLabel}";
	
	public static final String distanceChangeStepBean_validateDistanceChangeExp = 	"#{distanceChangeStepBean.validateDistanceChange}";
	//public static final String distanceChangeStepBean_validateShirtSizeChangeExp = 	"#{distanceChangeStepBean.validateShirtSizeChange}";
	public static final String distanceChangeStepBean_validateCCVNumberExp = 		"#{distanceChangeStepBean.validateCCVNumber}";
	public static final String distanceChangeStepBean_validateCardExpiresDateExp = 	"#{distanceChangeStepBean.validateCardExpiresDate}";
	
	
	public static final String distanceChangeStepBean_runDistancesExp = 			"#{distanceChangeStepBean.runDistances}";
	//public static final String distanceChangeStepBean_runShirtSizesExp = 			"#{distanceChangeStepBean.runShirtSizes}";
	public static final String distanceChangeStepBean_chosenDistanceNameExp = 		"#{distanceChangeStepBean.chosenDistanceName}";
	//public static final String distanceChangeStepBean_chosenShirtSizeNameExp = 		"#{distanceChangeStepBean.chosenTShirtName}";
	
	public static final String distanceChangeWizard_cssPrefix = 					"distChWiz_";
	
	/**
	 * @Override
	 */
	public List getWizardSteps() {
		
		List wizardSteps = new ArrayList(2);
		wizardSteps.add(new UIDistanceChangeStep());
		wizardSteps.add(new UIPaymentStep());
		return wizardSteps;
	}
	
	/**
	 * @Override
	 */
	public WizardStep getSubmissionSuccessStep() {
		
		return new UIDistanceChangeSuccess();
	}
}