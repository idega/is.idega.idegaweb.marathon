package is.idega.idegaweb.marathon.presentation.crew;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Participant;

import java.rmi.RemoteException;
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
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/01/10 18:56:26 $ by $Author: civilis $
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

		System.out.println("resolving members list");
		List members = new ArrayList();
		
		try {
			Participant participant = getCrewEditWizardBean().getParticipant();
			String crewLabel = participant.getRunGroupName();
			RunBusiness runBusiness = getCrewEditWizardBean().getRunBusiness();
			
			Collection crewMembers = runBusiness.getParticipantsByYearAndTeamName(String.valueOf(participant.getRunYearGroupID()), crewLabel);
			
			for (Iterator iterator = crewMembers.iterator(); iterator.hasNext();) {
				Participant member = (Participant) iterator.next();
				
				Map memberData = new HashMap(3);
				memberData.put(UICrewMembersInivitationStep.member_name, member.getUser().getName());
				memberData.put(UICrewMembersInivitationStep.member_isOwner, member.isCrewOwner() ? "Crew creator" : CoreConstants.EMPTY);
				memberData.put(UICrewMembersInivitationStep.member_modifyOnclick, new StringBuffer("document.getElementById('").append(UICrewMembersInivitationStep.crewMembersInvitationBean_memberDeleteParticipantIdParam).append("').value='").append(member.getPrimaryKey().toString()).append("';"));
				members.add(memberData);
			}
			
		} catch (FinderException e) {
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
			
			Collection crewMembers = runBusiness.getParticipantsByYearAndTeamName(String.valueOf(participant.getRunYearGroupID()), crewLabel);
			
			for (Iterator iterator = crewMembers.iterator(); iterator.hasNext();) {
				Participant member = (Participant) iterator.next();
				
				if(member.getPrimaryKey().toString().equals(getDeleteMemberParticipantId())) {
				
					if(member.isCrewOwner()) {
						//add err msg
						System.out.println("owner, won't delete");
						return;
					} else {
					
						member.setRunGroupName(null);
						member.setIsCrewOwner(false);
						member.store();
						return;
					}
				}
			}
			
//			add warning, that participant couldn't be found
			
		} catch (FinderException e) {
			e.printStackTrace();
//			TODO: add msg about err
		}
	}
	
	public void addMember() {
		
		System.out.println("adding member:"+getAddMemberParticipantId());
		
		Participant participant = null;
		RunBusiness runBusiness = getCrewEditWizardBean().getRunBusiness();
		
		try {
			participant = runBusiness.getParticipantByPrimaryKey(Integer.parseInt(getAddMemberParticipantId()));
			
		} catch (Exception e) {
			// TODO: add err msg
			e.printStackTrace();
			return;
		}
		
		if(participant == null) {
			
//			TODO: add error message, that current user is not participant for selected run
			
			System.out.println(":participant null ....................:");
			return;
		}
		
		if(participant.getRunGroupName() != null) {
//			TODO: add err msg
			System.out.println("participant already has crew");
		} else {
			
			participant.setRunGroupName(getCrewEditWizardBean().getParticipant().getRunGroupName());
			participant.setIsCrewOwner(false);
			participant.store();
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
		
		return "Crew members";
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