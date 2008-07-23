package is.idega.idegaweb.marathon.presentation.crew;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;
import javax.faces.context.FacesContext;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Participant;

/**
 * Crews related wrapper around participant bean
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/07/23 22:27:25 $ by $Author: palli $
 *
 */
public class CrewParticipant {

	private Participant participant;
	private RunBusiness runBusiness;
	private Logger logger;
	
	public static final int CREW_PARTICIPANT_ROLE_OWNER = 1;
	public static final int CREW_PARTICIPANT_ROLE_MEMBER = 2;
	public static final int CREW_PARTICIPANT_ROLE_INVITED = 3;
	public static final int CREW_PARTICIPANT_ROLE_NOT_PARTICIPANT = 4;
	
	public CrewParticipant(Participant participant) {
		
		if(participant == null)
			throw new NullPointerException("Participant not provided");
		
		this.participant = participant;
	}
	
	public CrewParticipant(Participant participant, RunBusiness runBusiness) {
		
		if(participant == null)
			throw new NullPointerException("Participant not provided");
		
		this.runBusiness = runBusiness;
		this.participant = participant;
	}

	public Participant getParticipant() {
		return participant;
	}
	
	public int getParticipantId() {
		
		return getParticipant().getRunID();
	}
	
	public String getCrewLabel() {
		
		Participant participant = getParticipant();
		
		if(isCrewOwner())
			return participant.getRunGroupName();
		else {
			
			Integer crewOwnerParticipantId = participant.getCrewInParticipantId() != null ? participant.getCrewInParticipantId() : participant.getCrewInvitedParticipantId();
			
			if(crewOwnerParticipantId == null)
				return null;
			
			try {
				Participant crewOwnerParticipant = getRunBusiness().getParticipantByPrimaryKey(crewOwnerParticipantId.intValue());
				return crewOwnerParticipant.getRunGroupName();
				
			} catch (Exception e) {
	
				getLogger().log(Level.SEVERE, "Exception while resolving crew owner participant, though the id for it was set: "+crewOwnerParticipantId);
				return null;
			}
		}
	}
	
	public void setCrewLabel(String crewLabel) {
		
		Participant participant = getParticipant();
		
		if(isCrewOwner() && !CoreConstants.EMPTY.equals(crewLabel))
			participant.setRunGroupName(crewLabel);
			
		else if(!isCrewOwner())
			getLogger().log(Level.WARNING, "Tried to set crew label for participant, which is not the owner of the crew. Participant id: "+getParticipantId());
	}
	
	protected RunBusiness getRunBusiness() {
		
		if(runBusiness == null) {
			
			try {
				runBusiness = (RunBusiness) IBOLookup.getServiceInstance(IWContext.getIWContext(FacesContext.getCurrentInstance()), RunBusiness.class);
			} catch (IBOLookupException e) {
				throw new RuntimeException(e);
			}
		}
		
		return runBusiness;
	}
	
	protected Logger getLogger() {
	
		if(logger == null)
			logger = Logger.getLogger(getClass().getName());
		
		return logger;
	}
	
	public boolean isParticipatingInCrew() {
		
		return getCrewParticipantRole() != CREW_PARTICIPANT_ROLE_NOT_PARTICIPANT;
	}
	
	public int getCrewParticipantRole() {
		
		Participant participant = getParticipant();
		
		if(isCrewOwner())
			return CREW_PARTICIPANT_ROLE_OWNER;
		
		if(participant.getCrewInParticipantId() != null)
			return CREW_PARTICIPANT_ROLE_MEMBER;
		
		if(participant.getCrewInvitedParticipantId() != null)
			return CREW_PARTICIPANT_ROLE_INVITED;
		
		return CREW_PARTICIPANT_ROLE_NOT_PARTICIPANT;
	}
	
	public boolean isCrewOwner() {
		
		Participant participant = getParticipant();
		return new Integer(getParticipantId()).equals(participant.getCrewInParticipantId());
	}

	public void setCrewOwner(boolean isCrewOwner) {
		
		Participant participant = getParticipant();
		
		if(isCrewOwner) {

			participant.setCrewInParticipantId(new Integer(getParticipantId()));
			participant.setCrewInvitedParticipantId(null);
			
		} else {
//			shouldn't be set to false, as it brings not straightforward situation here
			participant.setCrewInParticipantId(null);
			participant.setCrewInvitedParticipantId(null);
		}
	}
	
	public void store() {
		getParticipant().store();
	}
	
	public void acceptInvitation() {
	
		if(getCrewParticipantRole() == CREW_PARTICIPANT_ROLE_INVITED) {
			
			Participant participant = getParticipant();
			participant.setCrewInParticipantId(participant.getCrewInvitedParticipantId());
			participant.setCrewInvitedParticipantId(null);
			
		} else
			getLogger().log(Level.WARNING, "Tried to accept invitation, but the role wasn't 'invited'. Was: "+getCrewParticipantRole());
	}
	
	public void rejectInvitation() {
		
		if(getCrewParticipantRole() == CREW_PARTICIPANT_ROLE_INVITED) {
			
			getParticipant().setCrewInvitedParticipantId(null);
			
		} else
			getLogger().log(Level.WARNING, "Tried to accept invitation, but the role wasn't 'invited'. Was: "+getCrewParticipantRole());
	}
	
	public void deleteCrew() {
		
		if(!isCrewOwner()) {
			
			getLogger().log(Level.WARNING, "Tried to delete crew for participant, which is not the owner of the crew. Participant id: "+getParticipantId());
			throw new RuntimeException("Not a crew owner");
		}
		
		Participant participant = getParticipant();
		
		try {
			Collection crewMembers = null;
			try {
				crewMembers = getRunBusiness().getParticipantsByYearAndCrewInOrCrewInvitationParticipantId(String.valueOf(participant.getRunYearGroupID()), new Integer(getParticipantId()));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			
			for (Iterator iterator = crewMembers.iterator(); iterator.hasNext();) {
				Participant member = (Participant) iterator.next();
				
				member.setCrewInvitedParticipantId(null);
				member.setCrewInParticipantId(null);
				member.store();
			}
			
			participant.setCrewInvitedParticipantId(null);
			participant.setCrewInParticipantId(null);
			participant.setRunGroupName(null);
			participant.store();
			
		} catch (FinderException e) {
			getLogger().log(Level.WARNING, "Exception when deleting crew. Participant id: "+getParticipantId());
			throw new RuntimeException(e);
		}
	}
	
	public Collection getCrewMembers() {
		
		try {
			Participant participant = getParticipant();
			
			Integer searchParticipantId = participant.getCrewInParticipantId() != null ? participant.getCrewInParticipantId() : participant.getCrewInvitedParticipantId();
			
			if(searchParticipantId == null)
				return Collections.EMPTY_LIST;
			
			try {
				return getRunBusiness().getParticipantsByYearAndCrewInOrCrewInvitationParticipantId(String.valueOf(participant.getRunYearGroupID()), searchParticipantId);
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}
			
		} catch (FinderException e) {
			getLogger().log(Level.SEVERE, "Exception while resolving crew members. Participant id: "+getParticipantId());
			throw new RuntimeException(e);
		}
	}
	
	public void deleteMember(int memberParticipantId) {
		
		if(!isCrewOwner()) {
			
			getLogger().log(Level.WARNING, "Tried to delete crew member, when the participant is not the crew owner. Participant id: "+getParticipantId()+". Tried to delete participant id: "+memberParticipantId);
			throw new RuntimeException("Not a crew owner");
		}
		
		if(memberParticipantId == getParticipantId())
			throw new RuntimeException("Tried to delete crew owner.");
		
		Participant participantToRemove;
		
		try {
			participantToRemove = getRunBusiness().getParticipantByPrimaryKey(memberParticipantId);
		
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		
		if(participantToRemove == null) {
			
			getLogger().log(Level.SEVERE, "Couldn't find member participant to remove by participant id provided: "+memberParticipantId);
			throw new RuntimeException("Couldn't find member participant to remove by participant id provided: "+memberParticipantId);
		}

		if((participantToRemove.getCrewInParticipantId() != null && participantToRemove.getCrewInParticipantId().intValue() != getParticipantId()) ||
				(participantToRemove.getCrewInvitedParticipantId() != null && participantToRemove.getCrewInvitedParticipantId().intValue() != getParticipantId())) {
			
			getLogger().log(Level.SEVERE, "Serious error. Tried to remove crew member from some other crew. Tried to remove participant id: "+memberParticipantId);
			throw new RuntimeException("Serious error. Tried to remove crew member from some other crew. Tried to remove participant id: "+memberParticipantId);
		}
		
		participantToRemove.setCrewInParticipantId(null);
		participantToRemove.setCrewInvitedParticipantId(null);
		participantToRemove.store();
	}
	
	public void inviteMember(Participant memberToInvite) {
		
		if(!isCrewOwner()) {
			getLogger().log(Level.WARNING, "Tried to invite crew member, when the participant is not the crew owner. Participant id: "+getParticipantId()+". Tried to invite participant id: "+memberToInvite.getRunID());
			throw new RuntimeException("Not a crew owner");
		}
		
		if(memberToInvite.getCrewInParticipantId() != null || memberToInvite.getCrewInvitedParticipantId() != null)
			throw new RuntimeException("Member is either member, or invited");
		
		memberToInvite.setCrewInvitedParticipantId(new Integer(getParticipantId()));
		memberToInvite.store();
	}
}