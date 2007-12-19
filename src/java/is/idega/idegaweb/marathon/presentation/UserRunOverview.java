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
 * @version $Revision: 1.5 $
 *
 * Last modified: $Date: 2007/12/19 18:21:18 $ by $Author: civilis $
 *
 */
public class UserRunOverview extends IWBaseComponent {

	/**
	 * @Override
	 */
	protected void initializeComponent(FacesContext context) {
		super.initializeComponent(context);
	
		try {
			
			getFacets().put("UserRunOverviewList", new UserRunOverviewList());
			getFacets().put("DistanceChangeWizzard", new DistanceChangeWizard());
			
		} catch (Exception e) {
			e.printStackTrace();
		} catch(Error ee) {
			ee.printStackTrace();
		}
		
	}
	
	/**
	 * @Override
	 */
	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
	
		UIComponent stepComponent;
		
		if(context.getExternalContext().getRequestParameterMap().get("wizzard") != null/* || true*/)
			stepComponent = getFacet("DistanceChangeWizzard");
		else 
			stepComponent = getFacet("UserRunOverviewList");
		
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