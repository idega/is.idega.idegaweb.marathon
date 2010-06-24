package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.presentation.user.runoverview.DistanceChangeStepBean;
import is.idega.idegaweb.marathon.presentation.user.runoverview.UIDistanceChangeWizard;
import is.idega.idegaweb.marathon.presentation.user.runoverview.DistanceChangeWizardBean;
import is.idega.idegaweb.marathon.presentation.user.runoverview.UIUserRunOverviewList;

import java.io.IOException;

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
 * @version $Revision: 1.11 $
 *
 * Last modified: $Date: 2008/01/08 19:20:25 $ by $Author: civilis $
 *
 */
public class UserRunOverview extends IWBaseComponent {

	private static final String UserRunOverviewListFacet = "UserRunOverviewList";
	private static final String DistanceChangeWizardFormFacet = "DistanceChangeWizardForm";
	
	/**
	 * @Override
	 */
	protected void initializeComponent(FacesContext context) {
	
		getFacets().put(UserRunOverviewListFacet, new UIUserRunOverviewList());
		
		HtmlForm form = (HtmlForm)context.getApplication().createComponent(HtmlForm.COMPONENT_TYPE);
		form.setId(context.getViewRoot().createUniqueId());
		form.getChildren().add(context.getApplication().createComponent(UIDistanceChangeWizard.COMPONENT_TYPE));
		
		getFacets().put(DistanceChangeWizardFormFacet, form);
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
	
		String participantId = (String)context.getExternalContext().getRequestParameterMap().get(UIUserRunOverviewList.PARTICIPANT_PARAM);
		UIComponent stepComponent;
		
		ValueBinding vb = context.getApplication().createValueBinding(UIDistanceChangeWizard.distanceChangeStepBeanExp);
		DistanceChangeStepBean distanceChangeStepBean = (DistanceChangeStepBean)vb.getValue(context);
		
		if(participantId != null) {
		
			vb = context.getApplication().createValueBinding(UIDistanceChangeWizard.distanceChangeWizardBeanExp);
			DistanceChangeWizardBean distanceChangeWizardBean = (DistanceChangeWizardBean)vb.getValue(context);
			distanceChangeWizardBean.setParticipantId(participantId);
			distanceChangeStepBean.setWizardMode(true);
		}
		
		if(distanceChangeStepBean.isWizardMode())
			stepComponent = getFacet(DistanceChangeWizardFormFacet);
		else 
			stepComponent = getFacet(UserRunOverviewListFacet);
		
		if(stepComponent != null) {
			
			stepComponent.setRendered(true);
			renderChild(context, stepComponent);
		}
	}
	
	/**
	 * @Override
	 */
	public boolean getRendersChildren() {
		return true;
	}
}