package is.idega.idegaweb.marathon.presentation.user.runoverview;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.apache.myfaces.custom.htmlTag.HtmlTag;

import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.wizard.Wizard;
import com.idega.presentation.wizard.WizardStep;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/12/19 21:49:49 $ by $Author: civilis $
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
		container.setValue("div");
		
		HtmlInputHidden distanceIdHidden = (HtmlInputHidden)application.createComponent(HtmlInputHidden.COMPONENT_TYPE);
		distanceIdHidden.setValueBinding("value", application.createValueBinding(DistanceChangeWizard.distanceChangeWizardBean_distanceIdExp));
		container.getChildren().add(distanceIdHidden);
		
		HtmlInputHidden wizzardModeHidden = (HtmlInputHidden)application.createComponent(HtmlInputHidden.COMPONENT_TYPE);
		wizzardModeHidden.setValueBinding("value", application.createValueBinding(DistanceChangeWizard.distanceChangeWizardBean_wizardModeExp));
		container.getChildren().add(wizzardModeHidden);
		
		HtmlOutputText text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setValue("some minor text");
		container.getChildren().add(text);
		
		HtmlCommandButton nextButton = wizard.getNextButton(context, this);
		container.getChildren().add(nextButton);
		HtmlCommandButton justButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		container.getChildren().add(justButton);
		
		getFacets().put(containerFacet, container);
	}

	/**
	 * @Override
	 */
	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
		
		UIComponent container = getFacet(containerFacet);
		
		ValueBinding vb = context.getApplication().createValueBinding(DistanceChangeWizard.distanceChangeWizardBeanExp);
		DistanceChangeWizardBean distanceChangeWizardBean = (DistanceChangeWizardBean)vb.getValue(context);
		
		System.out.println(":distance id: "+distanceChangeWizardBean.getDistanceId()+":");
		
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