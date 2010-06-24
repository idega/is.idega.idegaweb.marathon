package is.idega.idegaweb.marathon.presentation.crew;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.idega.presentation.wizard.Wizard;
import com.idega.presentation.wizard.WizardStep;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.5 $
 *
 * Last modified: $Date: 2008/01/11 19:30:02 $ by $Author: civilis $
 *
 */
public class UICrewRegistrationWizard extends Wizard {

	public static final String COMPONENT_TYPE = 									"idega_CrewRegistrationWizard";
	private static final String wizardModeFacet = 									"wizardMode";
	
	/**
	 * @Override
	 */
	public WizardStep getSubmissionSuccessStep() {
		return null;
	}

	/**
	 * @Override
	 */
	public List getWizardSteps() {
		
		List wizardSteps = new ArrayList(2);
		wizardSteps.add(new UICrewManageStep());
		wizardSteps.add(new UICrewMembersInivitationStep());
		
		return wizardSteps;
	}
	
	protected void initializeComponent(FacesContext context) {
		super.initializeComponent(context);
		
		Application application = context.getApplication();
		
		HtmlInputHidden hidden = (HtmlInputHidden)application.createComponent(HtmlInputHidden.COMPONENT_TYPE);
		hidden.setId(context.getViewRoot().createUniqueId());
		hidden.setValueBinding(valueAtt, application.createValueBinding(UICrewsOverview.crewManageBean_wizardModeExp));
		getFacets().put(wizardModeFacet, hidden);
	}
	
	public void encodeChildren(FacesContext context) throws IOException {
		
		renderChild(context, getFacet(wizardModeFacet));
		super.encodeChildren(context);
	}
	
	/**
	 * @Override
	 */
	public void decode(FacesContext context) {
		super.decode(context);
		
		ValueBinding vb = context.getApplication().createValueBinding(UICrewsOverview.crewManageBean_wizardModeExp);
		vb.setValue(context, Boolean.valueOf((String)context.getExternalContext().getRequestParameterMap().get(getFacet(wizardModeFacet).getClientId(context))));
	}
}