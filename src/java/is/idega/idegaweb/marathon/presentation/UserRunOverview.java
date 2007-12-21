package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.presentation.user.runoverview.DistanceChangeStepBean;
import is.idega.idegaweb.marathon.presentation.user.runoverview.DistanceChangeWizard;
import is.idega.idegaweb.marathon.presentation.user.runoverview.DistanceChangeWizardBean;
import is.idega.idegaweb.marathon.presentation.user.runoverview.UserRunOverviewList;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.idega.presentation.IWBaseComponent;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.8 $
 *
 * Last modified: $Date: 2007/12/21 15:04:09 $ by $Author: civilis $
 *
 */
public class UserRunOverview extends IWBaseComponent {

	private static final String UserRunOverviewListFacet = "UserRunOverviewList";
	private static final String DistanceChangeWizzardFacet = "DistanceChangeWizzard";
	
	/**
	 * @Override
	 */
	protected void initializeComponent(FacesContext context) {
		super.initializeComponent(context);
	
		getFacets().put(UserRunOverviewListFacet, new UserRunOverviewList());
		getFacets().put(DistanceChangeWizzardFacet, new DistanceChangeWizard());
	}
	
	/**
	 * @Override
	 */
	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
	
		String participantId = (String)context.getExternalContext().getRequestParameterMap().get(UserRunOverviewList.PARTICIPANT_PARAM);
		UIComponent stepComponent;
		
		ValueBinding vb = context.getApplication().createValueBinding(DistanceChangeWizard.distanceChangeStepBeanExp);
		DistanceChangeStepBean distanceChangeStepBean = (DistanceChangeStepBean)vb.getValue(context);
		
		if(participantId != null) {
		
			vb = context.getApplication().createValueBinding(DistanceChangeWizard.distanceChangeWizardBeanExp);
			DistanceChangeWizardBean distanceChangeWizardBean = (DistanceChangeWizardBean)vb.getValue(context);
			distanceChangeWizardBean.setParticipantId(participantId);
			distanceChangeStepBean.setWizardMode(true);
		}
		
		if(distanceChangeStepBean.isWizardMode())
			stepComponent = getFacet(DistanceChangeWizzardFacet);
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