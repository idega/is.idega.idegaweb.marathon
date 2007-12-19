package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.presentation.user.runoverview.DistanceChangeWizard;
import is.idega.idegaweb.marathon.presentation.user.runoverview.DistanceChangeWizardBean;
import is.idega.idegaweb.marathon.presentation.user.runoverview.UserRunOverviewList;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.wizard.WizardControlValues;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.7 $
 *
 * Last modified: $Date: 2007/12/19 21:49:49 $ by $Author: civilis $
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
	
		String distanceGroupIdToEdit = (String)context.getExternalContext().getRequestParameterMap().get(UserRunOverviewList.CHANGE_DISTANCE_PARAM);
		UIComponent stepComponent;
		
		ValueBinding vb = context.getApplication().createValueBinding(DistanceChangeWizard.distanceChangeWizardBeanExp);
		DistanceChangeWizardBean distanceChangeWizardBean = (DistanceChangeWizardBean)vb.getValue(context);
		
		if(distanceGroupIdToEdit != null) {
			
			distanceChangeWizardBean.setWizardMode(true);
			distanceChangeWizardBean.setDistanceId(distanceGroupIdToEdit);
		}
		
		if(distanceChangeWizardBean.isWizardMode())
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