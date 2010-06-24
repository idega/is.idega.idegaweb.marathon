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
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2008/01/12 19:06:46 $ by $Author: civilis $
 *
 */
public class UICrewsOverview extends IWBaseComponent {

	public static final String COMPONENT_TYPE = "CrewsOverview";
	private static final String CrewsOverviewListFacet = "CrewsOverviewList";
	private static final String CrewRegistrationWizardFormFacet = "CrewRegistrationWizardForm";
	private static final String CrewViewFacetFacet = "CrewViewFacet";
	
	public static final String crewManageBeanExp = 									"#{crewManageBean}";
	public static final String crewManageBean_startNewCrewRegistrationExp = 		"#{crewManageBean.startNewCrewRegistration}";
	public static final String crewManageBean_wizardModeExp = 						"#{crewManageBean.wizardMode}";
	public static final String crewManageBean_crewManageHeaderValueExp = 			"#{crewManageBean.crewManageHeaderValue}";
	public static final String crewManageBean_crewLabelForOwnerExp = 				"#{crewManageBean.crewLabelForOwner}";
	public static final String crewManageBean_runsExp = 							"#{crewManageBean.runs}";
	public static final String crewManageBean_validateRunSelectionExp = 			"#{crewManageBean.validateRunSelection}";
	public static final String crewManageBean_runIdForOwnerExp =				 	"#{crewManageBean.runIdForOwner}";
	public static final String crewManageBean_createCrewExp =						"#{crewManageBean.createCrew}";
	public static final String crewManageBean_editCrewExp =							"#{crewManageBean.editCrew}";
	public static final String crewManageBean_updateCrewExp =						"#{crewManageBean.updateCrew}";
	public static final String crewManageBean_deleteCrewExp =						"#{crewManageBean.deleteCrew}";
	public static final String crewManageBean_acceptInvitationExp =					"#{crewManageBean.acceptInvitation}";
	public static final String crewManageBean_rejectInvitationExp =					"#{crewManageBean.rejectInvitation}";
	public static final String crewManageBean_viewCrewsListExp =					"#{crewManageBean.viewCrewsList}";
	public static final String crewManageBean_viewCrewViewExp =						"#{crewManageBean.viewCrewView}";
	public static final String crewManageBean_crewViewModeExp =						"#{crewManageBean.crewViewMode}";
	public static final String crewManageBean_crewViewHeaderExp = 					"#{crewManageBean.crewViewHeader}";
	
	
	public static final String crewMembersInvitationBean_deleteMemberExp =			"#{crewMembersInvitationBean.deleteMember}";
	public static final String crewMembersInvitationBean_inviteMemberExp =			"#{crewMembersInvitationBean.inviteMember}";
	public static final String crewMembersInvitationBean_membersListExp =			"#{crewMembersInvitationBean.membersList}";
	public static final String crewMembersInvitationBean_deleteMemberParticipantIdExp =	"#{crewMembersInvitationBean.deleteMemberParticipantId}";
	public static final String crewMembersInvitationBean_addMemberParticipantIdExp =	"#{crewMembersInvitationBean.addMemberParticipantId}";
	public static final String crewMembersInvitationBean_searchStringExp =			"#{crewMembersInvitationBean.searchString}";
	public static final String crewMembersInvitationBean_searchExp =				"#{crewMembersInvitationBean.search}";
	public static final String crewMembersInvitationBean_searchResultListRenderedExp =	"#{crewMembersInvitationBean.searchResultListRendered}";
	public static final String crewMembersInvitationBean_forceIdHackExp = 			"#{crewMembersInvitationBean.forceIdHack}";
	public static final String crewMembersInvitationBean_headerExp = 				"#{crewMembersInvitationBean.header}";
	public static final String crewMembersInvitationBean_searchResultsExp = 		"#{crewMembersInvitationBean.searchResults}";
	
	
	public static final String crewEditWizardBeanExp = 								"#{crewEditWizardBean}";
	public static final String crewEditWizardBean_newCrewModeExp = 					"#{crewEditWizardBean.newCrewMode}";
	public static final String crewEditWizardBean_modeExp = 						"#{crewEditWizardBean.mode}";
	public static final String crewEditWizardBean_editCrewModeExp = 				"#{crewEditWizardBean.editCrewMode}";
	public static final String crewEditWizardBean_runLabelExp = 					"#{crewEditWizardBean.runLabel}";
	public static final String crewEditWizardBean_participantIdExp =				"#{crewEditWizardBean.participantId}";
	
	public static final String crewsOverviewListBean_crewsOverviewListExp = 		"#{crewsOverviewListBean.crewsOverviewList}";
	public static final String crewsOverviewListBean_forceIdHackExp = 				"#{crewsOverviewListBean.forceIdHack}";
	
	
	/**
	 * @Override
	 */
	protected void initializeComponent(FacesContext context) {
	
		UICrewsOverviewList crewsOverviewList = (UICrewsOverviewList)context.getApplication().createComponent(UICrewsOverviewList.COMPONENT_TYPE);
		crewsOverviewList.setId(context.getViewRoot().createUniqueId());
		getFacets().put(CrewsOverviewListFacet, crewsOverviewList);
		
		UICrewView crewView = (UICrewView)context.getApplication().createComponent(UICrewView.COMPONENT_TYPE);
		crewView.setId(context.getViewRoot().createUniqueId());
		getFacets().put(CrewViewFacetFacet, crewView);
		
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
		
		ValueBinding vb = context.getApplication().createValueBinding(crewManageBeanExp);
		CrewManageBean crewViewBean = (CrewManageBean)vb.getValue(context);
		
		if(editCrewId != null) {
		
			vb = context.getApplication().createValueBinding(crewEditWizardBeanExp);
			CrewEditWizardBean crewEditWizardBean = (CrewEditWizardBean)vb.getValue(context);
			crewEditWizardBean.setCrewEditId(new Integer(editCrewId));
			crewViewBean.setWizardMode(true);
		}
		
		if(crewViewBean.isWizardMode())
			stepComponent = getFacet(CrewRegistrationWizardFormFacet);
		else if(crewViewBean.isCrewViewMode())
			stepComponent = getFacet(CrewViewFacetFacet);
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