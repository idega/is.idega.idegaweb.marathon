package is.idega.idegaweb.marathon.presentation.crew;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Participant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import com.idega.util.CoreConstants;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2008/01/12 17:15:16 $ by $Author: civilis $
 *
 */
public class CrewMembersInvitationBean {

	private CrewEditWizardBean crewEditWizardBean;
	private String deleteMemberParticipantId;
	private String addMemberParticipantId;
	private String searchString;
	private boolean searchResultListRendered;
	
	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public CrewEditWizardBean getCrewEditWizardBean() {
		return crewEditWizardBean;
	}

	public void setCrewEditWizardBean(CrewEditWizardBean crewEditWizardBean) {
		this.crewEditWizardBean = crewEditWizardBean;
	}

	public DataModel getMembersList() {

		List members = new ArrayList();
		CrewParticipant crewParticipant = getCrewEditWizardBean().getCrewParticipant();
		
		try {
			RunBusiness runBusiness = getCrewEditWizardBean().getRunBusiness();
			Collection crewMembers = crewParticipant.getCrewMembers();
			
			if(crewMembers != null) {
			
				for (Iterator iterator = crewMembers.iterator(); iterator.hasNext();) {
					
					CrewParticipant memberCrewParticipant = new CrewParticipant((Participant) iterator.next(), runBusiness); 
					
					String roleLabel;
					int role = memberCrewParticipant.getCrewParticipantRole();
					
					if(role == CrewParticipant.CREW_PARTICIPANT_ROLE_NOT_PARTICIPANT) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Crew member was found, but role was 'not participant'. Participant id: "+memberCrewParticipant.getParticipantId());
						continue;
					}
					
					if(role == CrewParticipant.CREW_PARTICIPANT_ROLE_OWNER) {
						roleLabel = "Crew creator";
						
					} else if(role == CrewParticipant.CREW_PARTICIPANT_ROLE_MEMBER) {
						roleLabel = "Member";
						
					} else if(role == CrewParticipant.CREW_PARTICIPANT_ROLE_INVITED) {
						roleLabel = "Invited";
						
					} else {
						continue;
					}
					
					Map memberData = new HashMap(3);
					memberData.put(UICrewMembersInivitationStep.member_name, memberCrewParticipant.getParticipant().getUser().getName());
					memberData.put(UICrewMembersInivitationStep.member_role, roleLabel);
					memberData.put(UICrewMembersInivitationStep.member_modifyOnclick, new StringBuffer("document.getElementById('").append(UICrewMembersInivitationStep.crewMembersInvitationBean_memberDeleteParticipantIdParam).append("').value='").append(memberCrewParticipant.getParticipantId()).append("';"));
					members.add(memberData);
				}
			}
			
		} catch (Exception e) {
			
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception while getting members list. Participant id: "+crewParticipant.getParticipantId(), e);
			FacesMessage message = new FacesMessage("Error occured, while getting members list. Please try again.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		return new ListDataModel(members);
	}
	
	public DataModel getSearchResults() {

		List searchResults = getCrewEditWizardBean().getCrewMembersInvitationSearchResults();
		return new ListDataModel(searchResults == null ? new ArrayList() : searchResults);
	}
	
	public void deleteMember() {
		
		CrewParticipant crewParticipant = getCrewEditWizardBean().getCrewParticipant();
		
		try {
			int memberParticipantId = Integer.parseInt(getDeleteMemberParticipantId());
			
			if(memberParticipantId == crewParticipant.getParticipantId()) {
				
				FacesMessage message = new FacesMessage("You cannot remove crew owner from the crew. You may want to remove crew.");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}
			
			crewParticipant.deleteMember(memberParticipantId);
			
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception while deleting member. Participant id: "+crewParticipant.getParticipantId(), e);
			FacesMessage message = new FacesMessage("Error occured, while deleting member. Please try again.");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	public void inviteMember() {
		
		CrewParticipant crewParticipant = getCrewEditWizardBean().getCrewParticipant();
		
		Participant memberInvited = null;
		RunBusiness runBusiness = getCrewEditWizardBean().getRunBusiness();
		
		try {
			memberInvited = runBusiness.getParticipantByPrimaryKey(Integer.parseInt(getAddMemberParticipantId()));
			
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception while inviting member. Tried to invite with participant id: "+getAddMemberParticipantId(), e);
			FacesMessage message = new FacesMessage("Error occured, while inviting member. Please try again.");
			FacesContext.getCurrentInstance().addMessage(null, message);
			return;
		}
		
		if(memberInvited == null) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Member to invite not found with the id provided: "+getAddMemberParticipantId());
			FacesMessage message = new FacesMessage("The member to invite couldn't be found in our database");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		
		CrewParticipant memberToInvite = new CrewParticipant(memberInvited, runBusiness);
		
		if(memberToInvite.getCrewParticipantRole() != CrewParticipant.CREW_PARTICIPANT_ROLE_NOT_PARTICIPANT) {
			
			FacesMessage message = new FacesMessage("The member to invite couldn't be found in our database");
			
			if(memberToInvite.getCrewParticipantRole() == CrewParticipant.CREW_PARTICIPANT_ROLE_OWNER || memberToInvite.getCrewParticipantRole() == CrewParticipant.CREW_PARTICIPANT_ROLE_MEMBER)
				message = new FacesMessage("Member you tried to invite already is in the crew");
			else if(memberToInvite.getCrewParticipantRole() == CrewParticipant.CREW_PARTICIPANT_ROLE_INVITED)
				message = new FacesMessage("Member you tried to invite already has been invited to some crew");
			else
//				de-fault
				message = new FacesMessage("Member couldn't be invited to a crew");
			
			FacesContext.getCurrentInstance().addMessage(null, message);
			
		} else {
			
			if(!crewParticipant.isCrewOwner()) {
				
				Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Tried to invite member, but CrewParticipant was not crew owner. Participant id: "+crewParticipant.getParticipantId());
				FacesMessage message = new FacesMessage("Error occured while trying to invite crew member. Please, try again.");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}
			
			try {
				crewParticipant.inviteMember(memberInvited);
				
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception while inviting member. Tried to invite with participant id: "+getAddMemberParticipantId(), e);
				FacesMessage message = new FacesMessage("Error occured, while inviting member. Please try again.");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}
	}

	public String getDeleteMemberParticipantId() {
		return deleteMemberParticipantId;
	}

	public void setDeleteMemberParticipantId(String deleteMemberParticipantId) {
		this.deleteMemberParticipantId = deleteMemberParticipantId;
	}
	
	public boolean isForceIdHack() {
		
		return true;
	}
	
	public String getHeader() {
		
		return "Crew \""+getCrewEditWizardBean().getCrewParticipant().getCrewLabel()+"\" members";
	}

	public String getAddMemberParticipantId() {
		return addMemberParticipantId;
	}

	public void setAddMemberParticipantId(String addMemberParticipantId) {
		this.addMemberParticipantId = addMemberParticipantId;
	}

	public boolean isSearchResultListRendered() {
		
		return searchResultListRendered ? searchResultListRendered : getCrewEditWizardBean().getCrewMembersInvitationSearchResults() != null;
	}

	public void setSearchResultListRendered(boolean searchResultListRendered) {
		this.searchResultListRendered = searchResultListRendered;
	}
	
	public void search() {
		
		List searchResults = new ArrayList();
		
		String searchString = getSearchString();
		
		if(searchString != null && !CoreConstants.EMPTY.equals(searchString)) {
			
			try {
				RunBusiness runBusiness = getCrewEditWizardBean().getRunBusiness();
				
				Collection participants = runBusiness.getParticipantsBySearchQuery(String.valueOf(getCrewEditWizardBean().getCrewParticipant().getParticipant().getRunYearGroupID()), searchString);
				
				if(participants != null) {
					
					List foundParticipantsIds = new ArrayList(participants.size());

					for (Iterator iterator = participants.iterator(); iterator.hasNext();) {
						Participant foundParticipant = (Participant)iterator.next();
						
						if(foundParticipantsIds.contains(foundParticipant.getPrimaryKey().toString()))
							continue;
						
						foundParticipantsIds.add(foundParticipant.getPrimaryKey().toString());
						
						Map foundParticipantData = new HashMap(3);
						
						foundParticipantData.put(UICrewMembersInivitationStep.sr_participantName, foundParticipant.getUser().getName());
						foundParticipantData.put(UICrewMembersInivitationStep.sr_participantNumber, String.valueOf(foundParticipant.getParticipantNumber()));
						foundParticipantData.put(UICrewMembersInivitationStep.sr_modifyOnclick, new StringBuffer("document.getElementById('").append(UICrewMembersInivitationStep.crewMembersInvitationBean_memberAddParticipantIdParam).append("').value='").append(foundParticipant.getPrimaryKey().toString()).append("';"));
						searchResults.add(foundParticipantData);
					}
				}
				
			} catch (Exception e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception while searching participants by search query. Query: "+searchString, e);
				FacesMessage message = new FacesMessage("Error occured, while searching. Please try again.");
				FacesContext.getCurrentInstance().addMessage(null, message);
				return;
			}
			
			if(searchResults.isEmpty()) {
				FacesMessage message = new FacesMessage("Nothing found");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		}
		
		getCrewEditWizardBean().setCrewMembersInvitationSearchResults(searchResults);
		setSearchResultListRendered(true);
	}
}