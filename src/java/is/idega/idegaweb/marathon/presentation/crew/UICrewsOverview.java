package is.idega.idegaweb.marathon.presentation.crew;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/01/08 19:20:25 $ by $Author: civilis $
 *
 */
public class UICrewsOverview extends IWBaseComponent {

	public static final String COMPONENT_TYPE = "CrewsOverview";
	private static final String CrewsOverviewListFacet = "CrewsOverviewList";
	private static final String CrewRegistrationWizardFormFacet = "CrewRegistrationWizardForm";
	
	/**
	 * @Override
	 */
	protected void initializeComponent(FacesContext context) {
	
		UICrewsOverviewList crewsOverviewList = (UICrewsOverviewList)context.getApplication().createComponent(UICrewsOverviewList.COMPONENT_TYPE);
		crewsOverviewList.setId(context.getViewRoot().createUniqueId());
		getFacets().put(CrewsOverviewListFacet, crewsOverviewList);
		
		HtmlForm form = (HtmlForm)context.getApplication().createComponent(HtmlForm.COMPONENT_TYPE);
		form.setId(context.getViewRoot().createUniqueId());
		form.getChildren().add(context.getApplication().createComponent(UICrewRegistrationWizard.COMPONENT_TYPE));
		
		getFacets().put(CrewRegistrationWizardFormFacet, form);
	}
	
	/**
	 * @Override
	 */
	public void encodeChildren(FacesContext context) throws IOException {
		
		IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
		if (!iwc.isLoggedOn()) {
			renderChild(context, new Text("No user logged on..."));
			return;
		}
	
		UIComponent stepComponent;
		String editCrewId = (String)context.getExternalContext().getRequestParameterMap().get(UICrewsOverviewList.EDIT_CREW_ID);
		
		ValueBinding vb = context.getApplication().createValueBinding(UICrewRegistrationWizard.crewManageBeanExp);
		CrewManageBean crewViewBean = (CrewManageBean)vb.getValue(context);
		
		if(editCrewId != null) {
		
			vb = context.getApplication().createValueBinding(UICrewRegistrationWizard.crewEditWizardBeanExp);
			CrewEditWizardBean crewEditWizardBean = (CrewEditWizardBean)vb.getValue(context);
			crewEditWizardBean.setCrewEditId(new Integer(editCrewId));
			crewViewBean.setWizardMode(true);
		}
		
		if(crewViewBean.isWizardMode())
			stepComponent = getFacet(CrewRegistrationWizardFormFacet);
		else 
			stepComponent = getFacet(CrewsOverviewListFacet);
		
		if(stepComponent != null) {
			
			stepComponent.setRendered(true);
			renderChild(context, stepComponent);
			
		} else {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "No component resolved for UICrewsOverview");
		}
	}
	
	/**
	 * @Override
	 */
	public boolean getRendersChildren() {
		return true;
	}
}