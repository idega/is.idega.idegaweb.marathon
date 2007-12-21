package is.idega.idegaweb.marathon.presentation.user.runoverview;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.htmlTag.HtmlTag;

import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.wizard.Wizard;
import com.idega.presentation.wizard.WizardStep;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/12/21 15:05:02 $ by $Author: civilis $
 *
 */
public class DistanceChangeStep extends IWBaseComponent implements WizardStep {
	
	private static final long serialVersionUID = 983517329599024600L;
	static final String stepIdentifier = "DistanceChangeStep";
	
	private static final String containerFacet = "container";
	
	private Wizard wizard;
	
	public String getIdentifier() {
		
		return stepIdentifier;
	}

	public UIComponent getStepComponent(FacesContext context, Wizard wizard) {
		
		DistanceChangeStep step = new DistanceChangeStep();
		step.setWizard(wizard);
		return step;
	}
	
	public void setWizard(Wizard wizard) {
		this.wizard = wizard;
	}
	
	/**
	 * @Override
	 */
	protected void initializeComponent(FacesContext context) {
		
		Application application = context.getApplication();
		
		
		HtmlTag container = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		container.setId(context.getViewRoot().createUniqueId());
		container.setValue("div");
		
//		HtmlInputHidden hidden = (HtmlInputHidden)application.createComponent(HtmlInputHidden.COMPONENT_TYPE);
//		hidden.setId("xxxxxxxxxxxx");
//		hidden.setValueBinding("value", application.createValueBinding(DistanceChangeWizard.distanceChangeWizardBean_participantIdExp));
		
//		container.getChildren().add(hidden);
		
		HtmlInputHidden hidden = (HtmlInputHidden)application.createComponent(HtmlInputHidden.COMPONENT_TYPE);
		hidden.setId(context.getViewRoot().createUniqueId());
		hidden.setValueBinding("value", application.createValueBinding(DistanceChangeWizard.distanceChangeStepBean_wizardModeExp));
		container.getChildren().add(hidden);
		
		HtmlOutputText text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValueBinding("value", application.createValueBinding(DistanceChangeWizard.distanceChangeStepBean_runLabelExp));
		
		HtmlTag div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue("div");
		div.getChildren().add(text);
		div.setStyleClass("runDistanceChangeHeader");
		container.getChildren().add(div);
		
		text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValue("You will need to pay to change your chosen distance.");
		
		div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue("div");
		div.getChildren().add(text);
		div.setStyleClass("runDistanceChangeWarning");
		container.getChildren().add(div);
		
		HtmlTag distanceChangeArea = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		distanceChangeArea.setId(context.getViewRoot().createUniqueId());
		distanceChangeArea.setValue("div");
		distanceChangeArea.getChildren().add(text);
		distanceChangeArea.setStyleClass("distanceChangeArea");
		container.getChildren().add(distanceChangeArea);
		
		text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValue("Currently chosen distance: ");
		distanceChangeArea.getChildren().add(text);
		
		div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue("div");
		div.getChildren().add(text);
		distanceChangeArea.getChildren().add(div);
		
		text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValue("Super great distance");
		div.getChildren().add(text);
		
		text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValue("Choose new distance: ");
		distanceChangeArea.getChildren().add(text);
		
		div = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		div.setId(context.getViewRoot().createUniqueId());
		div.setValue("div");
		div.getChildren().add(text);
		distanceChangeArea.getChildren().add(div);
		
		HtmlSelectOneMenu distanceChooser = (HtmlSelectOneMenu)application.createComponent(HtmlSelectOneMenu.COMPONENT_TYPE);
		distanceChooser.setId(context.getViewRoot().createUniqueId());
		distanceChooser.setValueBinding("value", application.createValueBinding(DistanceChangeWizard.distanceChangeStepBean_newDistanceExp));
		UISelectItems selectItems = (UISelectItems)application.createComponent(UISelectItems.COMPONENT_TYPE);
		selectItems.setId(context.getViewRoot().createUniqueId());
		selectItems.setValueBinding("value", application.createValueBinding(DistanceChangeWizard.distanceChangeStepBean_runDistancesExp));
		distanceChooser.getChildren().add(selectItems);
		
		div.getChildren().add(distanceChooser);
		
		
		
		HtmlCommandButton nextButton = wizard.getNextButton(context, this);
		container.getChildren().add(nextButton);
		HtmlCommandButton justButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		justButton.setId(context.getViewRoot().createUniqueId());
		container.getChildren().add(justButton);
		
		getFacets().put(containerFacet, container);
	}

	/**
	 * @Override
	 */
	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
		
		UIComponent container = getFacet(containerFacet);
		
//		ValueBinding vb = context.getApplication().createValueBinding(DistanceChangeWizard.distanceChangeStepBeanExp);
//		DistanceChangeWizardBean distanceChangeWizardBean = (DistanceChangeWizardBean)vb.getValue(context);
		
		if(container != null) {
			
			container.setRendered(true);
			renderChild(context, container);
		}
	}
	
	/**
	 * @Override
	 */
	public boolean getRendersChildren() {
		return true;
	}
}