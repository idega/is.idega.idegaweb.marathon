package is.idega.idegaweb.marathon.presentation.crew;

import is.idega.idegaweb.marathon.IWBundleStarter;
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

import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2008/01/12 17:15:15 $ by $Author: civilis $
 *
 */
public class CrewsOverviewListBean {
	
	private RunBusiness runBusiness;
	
	public DataModel getCrewsOverviewList() {
		
		List crews = new ArrayList();
		
		try {
			
			IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
			User ownerUser = iwc.getCurrentUser();
			
			RunBusiness runBusiness = getRunBusiness();
			Collection participants = runBusiness.getParticipantsByUser(ownerUser);
			
			if(participants != null) {
				
				for (Iterator iterator = participants.iterator(); iterator.hasNext();) {
					
					CrewParticipant crewParticipant = new CrewParticipant((Participant)iterator.next(), runBusiness);
					
					int role = crewParticipant.getCrewParticipantRole();
					
//					doesn't belong to a crew in this participation
					if(role == CrewParticipant.CREW_PARTICIPANT_ROLE_NOT_PARTICIPANT)
						continue;
					
					String crewLabel = crewParticipant.getCrewLabel();
					
					String runLabel = 
						iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc)	
						.getLocalizedString(crewParticipant.getParticipant().getRunTypeGroup().getName(), crewParticipant.getParticipant().getRunTypeGroup().getName()) + " " + crewParticipant.getParticipant().getRunYearGroup().getName();
					
					String distanceLabel = 
						iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc)
						.getLocalizedString(crewParticipant.getParticipant().getRunDistanceGroup().getName(), crewParticipant.getParticipant().getRunDistanceGroup().getName());
					
					Map crew = new HashMap(7);
					crew.put(UICrewsOverviewList.crew_label, crewLabel);
					crew.put(UICrewsOverviewList.crew_runLabel, runLabel);
					crew.put(UICrewsOverviewList.crew_distance, distanceLabel);
					crew.put(UICrewsOverviewList.crew_pidOnclick, new StringBuffer("document.getElementById('").append(UICrewsOverviewList.crewsOverviewListBean_participantIdParam).append("').value='").append(crewParticipant.getParticipantId()).append("';"));
					crew.put(UICrewsOverviewList.crew_renderedEdit, new Boolean(crewParticipant.isCrewOwner()));
					crew.put(UICrewsOverviewList.crew_renderedAcceptInvitation, new Boolean(role == CrewParticipant.CREW_PARTICIPANT_ROLE_INVITED));
					crew.put(UICrewsOverviewList.crew_renderedRejectInvitation, new Boolean(role == CrewParticipant.CREW_PARTICIPANT_ROLE_INVITED));
					
					crews.add(crew);
				}
			}
			
		} catch (Exception e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception while resolving crews overview list", e);
		}
	
		return new ListDataModel(crews);
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
	
	public boolean isForceIdHack() {
		
		return true;
	}
}