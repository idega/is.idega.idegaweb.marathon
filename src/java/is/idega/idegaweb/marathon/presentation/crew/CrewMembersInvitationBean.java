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

import javax.ejb.FinderException;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import com.idega.util.CoreConstants;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/01/11 19:30:02 $ by $Author: civilis $
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
		
		try {
			Participant participant = getCrewEditWizardBean().getParticipant();
			String crewLabel = participant.getRunGroupName();
			RunBusiness runBusiness = getCrewEditWizardBean().getRunBusiness();
			
			Collection crewMembers;
			
			if(participant.isCrewOwner()) {

//				viewer is crew owner
				crewMembers = runBusiness.getParticipantsByYearAndCrewNameOrInvitationParticipantId(String.valueOf(participant.getRunYearGroupID()), crewLabel, new Integer(participant.getRunID()));
			
			} else if(crewLabel == null && participant.getCrewInvitedParticipantId() != null) {
//				viewer is invited to this crew
				
				Participant owner = runBusiness.getParticipantByPrimaryKey(participant.getCrewInvitedParticipantId().intValue());
				crewMembers = runBusiness.getParticipantsByYearAndCrewNameOrInvitationParticipantId(String.valueOf(participant.getRunYearGroupID()), owner.getRunGroupName(), new Integer(owner.getRunID()));
				
			} else {
//				viewer is crew member
				
				crewMembers = runBusiness.getParticipantsByYearAndCrewNameOrInvitationParticipantId(String.valueOf(participant.getRunYearGroupID()), crewLabel, null);
			}
			
			for (Iterator iterator = crewMembers.iterator(); iterator.hasNext();) {
				Participant member = (Participant) iterator.next();
				
				String role;
				
				if(member.isCrewOwner()) {
					role = "Crew creator";
					
				} else if(participant.getRunGroupName() != null && participant.getRunGroupName().equals(member.getRunGroupName())) {
					role = "Member";
					
				} else if(member.getCrewInvitedParticipantId() != null) {
					role = "Invited";
					
				} else {
					role = CoreConstants.EMPTY;
				}
				
				Map memberData = new HashMap(3);
				memberData.put(UICrewMembersInivitationStep.member_name, member.getUser().getName());
				memberData.put(UICrewMembersInivitationStep.member_role, role);
				memberData.put(UICrewMembersInivitationStep.member_modifyOnclick, new StringBuffer("document.getElementById('").append(UICrewMembersInivitationStep.crewMembersInvitationBean_memberDeleteParticipantIdParam).append("').value='").append(member.getPrimaryKey().toString()).append("';"));
				members.add(memberData);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
//			TODO: add msg about err
		}
		
		return new ListDataModel(members);
	}
	
	public DataModel getSearchResults() {

		List searchResults = getCrewEditWizardBean().getCrewMembersInvitationSearchResults();
		return new ListDataModel(searchResults == null ? new ArrayList() : searchResults);
	}
	
	public void deleteMember() {
		
		System.out.println("deleting member");
		try {
			Participant participant = getCrewEditWizardBean().getParticipant();
			String crewLabel = participant.getRunGroupName();
			RunBusiness runBusiness = getCrewEditWizardBean().getRunBusiness();
			
			int participantToRemoveId = Integer.parseInt(getDeleteMemberParticipantId());
			
			Participant participantToRemove = runBusiness.getParticipantByPrimaryKey(participantToRemoveId);
			
			if(participantToRemove.isCrewOwner()) {
				//add err msg
				System.out.println("owner, won't delete");
				return;
			} else if(participantToRemove.getCrewInvitedParticipantId() != null || participantToRemove.getRunGroupName().equals(crewLabel)) {
				
				participantToRemove.setCrewInvitedParticipantId(null);
				participantToRemove.setRunGroupName(null);
				participantToRemove.setIsCrewOwner(false);
				participantToRemove.store();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
//			TODO: add msg about err
		}
	}
	
	public void inviteMember() {
		
		Participant memberInvited = null;
		RunBusiness runBusiness = getCrewEditWizardBean().getRunBusiness();
		
		try {
			memberInvited = runBusiness.getParticipantByPrimaryKey(Integer.parseInt(getAddMemberParticipantId()));
			
		} catch (Exception e) {
			// TODO: add err msg
			e.printStackTrace();
			return;
		}
		
		if(memberInvited == null) {
			
//			TODO: add error message, that current user is not participant for selected run
			
			System.out.println(":participant null ....................:");
			return;
		}
		
		if(memberInvited.getRunGroupName() != null) {
//			TODO: add err msg
			System.out.println("participant already has crew");
			
			if(memberInvited.getRunGroupName().equals(getCrewEditWizardBean().getParticipant().getRunGroupName())) {
				
//				in your crew
			} else {
//				just in crew
			}
			
		} else if(memberInvited.getCrewInvitedParticipantId() != null) {
			
//			TODO: add err msg
			System.out.println("participant already has been invited to a crew");
			
			if(memberInvited.getCrewInvitedParticipantId().intValue() == getCrewEditWizardBean().getParticipant().getRunID()) {
				
//				in your crew
			} else {
//				just in crew
			}
			
		} else {
			
			memberInvited.setRunGroupName(null);
			memberInvited.setCrewInvitedParticipantId(new Integer(getCrewEditWizardBean().getParticipant().getRunID()));
			memberInvited.setIsCrewOwner(false);
			memberInvited.store();
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
		
		return "Crew \""+getCrewEditWizardBean().getParticipant().getRunGroupName()+"\" members";
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
		
		System.out.println("searching");
		
		List searchResults = new ArrayList();
		
		String searchString = getSearchString();
		
		if(searchString != null && !CoreConstants.EMPTY.equals(searchString)) {
			
			try {
				Participant participant = getCrewEditWizardBean().getParticipant();
				RunBusiness runBusiness = getCrewEditWizardBean().getRunBusiness();
				
				Collection participants = runBusiness.getParticipantsBySearchQuery(String.valueOf(participant.getRunYearGroupID()), searchString);
				
				if(participants != null) {
					
					List foundParticipantsIds = new ArrayList(participants.size());

					for (Iterator iterator = participants.iterator(); iterator.hasNext();) {
						Participant foundParticipant = (Participant)iterator.next();
						
						if(foundParticipantsIds.contains(foundParticipant.getPrimaryKey().toString()))
							continue;
						
						foundParticipantsIds.add(foundParticipant.getPrimaryKey().toString());
						
						Map foundParticipantData = new HashMap(3);
						
//						sr_participantNameExp, "Member name"));
						foundParticipantData.put(UICrewMembersInivitationStep.sr_participantName, foundParticipant.getUser().getName());
						foundParticipantData.put(UICrewMembersInivitationStep.sr_participantNumber, String.valueOf(foundParticipant.getParticipantNumber()));
						foundParticipantData.put(UICrewMembersInivitationStep.sr_modifyOnclick, new StringBuffer("document.getElementById('").append(UICrewMembersInivitationStep.crewMembersInvitationBean_memberAddParticipantIdParam).append("').value='").append(foundParticipant.getPrimaryKey().toString()).append("';"));
						searchResults.add(foundParticipantData);
					}
				}
				
				System.out.println("participants: "+participants);
				
			} catch (FinderException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception while searching participants by search query");
//				TODO: add msg
			}
			
//			if nothing found, add msg
			
		} else {
//			add err msg
		}
		
		getCrewEditWizardBean().setCrewMembersInvitationSearchResults(searchResults);
		setSearchResultListRendered(true);
	}
}