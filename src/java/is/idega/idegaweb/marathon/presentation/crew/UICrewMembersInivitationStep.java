package is.idega.idegaweb.marathon.presentation.crew;

import java.io.IOException;

import is.idega.idegaweb.marathon.IWBundleStarter;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.htmlTag.HtmlTag;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.wizard.Wizard;
import com.idega.presentation.wizard.WizardStep;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/01/08 19:20:24 $ by $Author: civilis $
 *
 */
public class UICrewMembersInivitationStep extends IWBaseComponent implements WizardStep {

	private static final long serialVersionUID = 7070707428550344301L;
	public static final String COMPONENT_TYPE = "idega_CrewMembersInivitationStep";
	static final String stepIdentifier = "CrewMembersInivitationStep";
	private static final String containerFacet = "container";
	
	private Wizard wizard;

	public void setWizard(Wizard wizard) {
		this.wizard = wizard;
	}

	public String getIdentifier() {
		return stepIdentifier;
	}

	public UIComponent getStepComponent(FacesContext context, Wizard wizard) {
		
		UICrewMembersInivitationStep step = (UICrewMembersInivitationStep)context.getApplication().createComponent(COMPONENT_TYPE);
		step.setId(context.getViewRoot().createUniqueId());
		step.setRendered(true);
		step.setWizard(wizard);
		return step;
	}
	
	protected void initializeComponent(FacesContext context) {
		
		Application application = context.getApplication();
		IWContext iwc = IWContext.getIWContext(context);
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		
		HtmlTag dcsDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		dcsDiv.setId(context.getViewRoot().createUniqueId());
		dcsDiv.setValue(divTag);
		
		HtmlOutputText text = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
		text.setId(context.getViewRoot().createUniqueId());
		text.setValue("members invitation");
		
		dcsDiv.getChildren().add(text);
		
		HtmlCommandButton prev = wizard.getPreviousButton(context, this);
		dcsDiv.getChildren().add(prev);
		
		getFacets().put(containerFacet, dcsDiv);
	}
	
	/**
	 * @Override
	 */
	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
		
		UIComponent container = getFacet(containerFacet);
		
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

	/**
	 * @Override
	 */
	public boolean isRendered() {
		
		return IWContext.getIWContext(FacesContext.getCurrentInstance()).isLoggedOn();
	}
}