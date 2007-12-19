package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.presentation.user.runoverview.DistanceChangeWizard;
import is.idega.idegaweb.marathon.presentation.user.runoverview.UserRunOverviewList;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.idega.presentation.IWBaseComponent;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.6 $
 *
 * Last modified: $Date: 2007/12/19 19:13:34 $ by $Author: civilis $
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
		
		if(distanceGroupIdToEdit != null)
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