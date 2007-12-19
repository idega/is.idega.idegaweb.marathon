package is.idega.idegaweb.marathon.presentation.user.runoverview;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.htmlTag.HtmlTag;

import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.wizard.Wizard;
import com.idega.presentation.wizard.WizardStep;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/12/19 18:07:42 $ by $Author: civilis $
 *
 */
public class DistanceChangeStep extends IWBaseComponent implements WizardStep {
	
	private static final long serialVersionUID = 983517329599024600L;
	static final String stepIdentifier = "DistanceChangeStep";
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
		
		HtmlOutputText text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setValue("some minor text");
		
		HtmlCommandButton nextButton = wizard.getNextButton(context, this);
		HtmlCommandButton justButton = (HtmlCommandButton)application.createComponent(HtmlCommandButton.COMPONENT_TYPE);
		
		container.getChildren().add(text);
		container.getChildren().add(nextButton);
		container.getChildren().add(justButton);
		
		getFacets().put("container", container);
	}

	/**
	 * @Override
	 */
	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
		
		UIComponent container = getFacet("container");
		
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