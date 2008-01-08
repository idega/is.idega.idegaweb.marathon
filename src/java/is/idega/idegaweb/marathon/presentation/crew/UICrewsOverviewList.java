package is.idega.idegaweb.marathon.presentation.crew;

import is.idega.idegaweb.marathon.IWBundleStarter;
import is.idega.idegaweb.marathon.presentation.user.runoverview.UIDistanceChangeWizard;

import java.io.IOException;

import javax.faces.application.Application;
import javax.faces.component.html.HtmlCommandLink;
import javax.faces.component.html.HtmlForm;
import javax.faces.component.html.HtmlInputHidden;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.context.FacesContext;

import org.apache.myfaces.custom.htmlTag.HtmlTag;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/01/08 19:20:24 $ by $Author: civilis $
 *
 */
public class UICrewsOverviewList extends IWBaseComponent {

	public static final String COMPONENT_TYPE = "idega_CrewsOverviewList";
	public static final String EDIT_CREW_ID = "ecid";
	
	private static final String containerFacet = "container";
	
	/**
	 * @Override
	 */
	protected void initializeComponent(FacesContext context) {
	
		Application application = context.getApplication();
		IWContext iwc = IWContext.getIWContext(context);
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		
		HtmlForm form = (HtmlForm)context.getApplication().createComponent(HtmlForm.COMPONENT_TYPE);
		form.setId(context.getViewRoot().createUniqueId());
		getFacets().put(containerFacet, form);
		
		HtmlTag containerDiv = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		containerDiv.setId(context.getViewRoot().createUniqueId());
		containerDiv.setValue(divTag);
		form.getChildren().add(containerDiv);
		
		HtmlCommandLink startNewCrewRegLink = (HtmlCommandLink)application.createComponent(HtmlCommandLink.COMPONENT_TYPE);
		startNewCrewRegLink.setId(context.getViewRoot().createUniqueId());
		startNewCrewRegLink.setValue("x");
		startNewCrewRegLink.setAction(application.createMethodBinding(UICrewRegistrationWizard.crewManageBean_startNewCrewRegistrationExp, null));
		
		containerDiv.getChildren().add(startNewCrewRegLink);
	}
	
	/**
	 * @Override
	 */
	public void encodeChildren(FacesContext context) throws IOException {
		
		renderChild(context, getFacet(containerFacet));
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
	public String getId() {
		String id = super.getId();

		if(id == null) {
			
			id = FacesContext.getCurrentInstance().getViewRoot().createUniqueId();
			System.out.println("id was null, creating: "+id);
		}
		
		return id;
	}
}