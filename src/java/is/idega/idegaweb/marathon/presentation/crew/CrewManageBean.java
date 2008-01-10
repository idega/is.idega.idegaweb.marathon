package is.idega.idegaweb.marathon.presentation.crew;

import is.idega.idegaweb.marathon.IWBundleStarter;
import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Year;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2008/01/10 10:44:01 $ by $Author: civilis $
 *
 */
public class CrewManageBean {

	private boolean wizardMode = false;
	private String crewLabel;
	private String runId;
	private List runsChoices;
	
	private CrewEditWizardBean crewEditWizardBean;

	public boolean isWizardMode() {
		return wizardMode;
	}

	public void setWizardMode(boolean wizardMode) {
		this.wizardMode = wizardMode;
	}
	
	public void startNewCrewRegistration() {
		
		setWizardMode(true);
		getCrewEditWizardBean().setMode(CrewEditWizardBean.newCrewMode);
	}

	public CrewEditWizardBean getCrewEditWizardBean() {
		return crewEditWizardBean;
	}

	public void setCrewEditWizardBean(CrewEditWizardBean crewEditWizardBean) {
		this.crewEditWizardBean = crewEditWizardBean;
	}

	public String getCrewManageHeaderValue() {
		
		return getCrewEditWizardBean().isNewCrewMode() ? "Create new crew" : getCrewEditWizardBean().isEditCrewMode() ? "Edit crew" : "Manage crew";
	}

	public String getCrewLabel() {
		
		return crewLabel;
	}

	public void setCrewLabel(String crewLabel) {
		
		System.out.println("setting crew label: "+crewLabel);
		this.crewLabel = crewLabel;
	}

	public String getRunId() {
		
		if(runId == null) {
			
			Participant participant = getCrewEditWizardBean().getParticipant();
			
			if(participant != null)
				runId = String.valueOf(participant.getRunTypeGroupID());
		}
		
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}
	
	public List getRuns() {

		System.out.println("getting runs");
		if(runsChoices == null) {
			
			IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
			
			runsChoices = new ArrayList();
			
			SelectItem selectItem = new SelectItem();
			
			selectItem.setValue(CoreConstants.EMPTY);
			selectItem.setLabel(iwrb.getLocalizedString("run_year_ddd.select_run", "Select run..."));
			runsChoices.add(selectItem);
			
			String[] constrainedRunIds = null;
			
//			copied from ActiveRunDropDownMenu
			IWTimestamp thisYearStamp = IWTimestamp.RightNow();
			String yearString = String.valueOf(thisYearStamp.getYear());
			IWTimestamp nextYearStamp = IWTimestamp.RightNow();
			nextYearStamp.addYears(1);
			String nextYearString = String.valueOf(nextYearStamp.getYear());
			
			try {
				Collection runs = getCrewEditWizardBean().getRunBusiness().getRuns();
				
				if (runs != null) {
					
					System.out.println("total: "+runs.size());
					for (Iterator iterator = runs.iterator(); iterator.hasNext();) {
						
						Group run = (Group) iterator.next();
						System.out.println("run resolved: "+run);
						String runnerYearString = yearString;
						String runId = run.getPrimaryKey().toString();
						
						boolean show = false;
						boolean finished = true;
						Map yearMap = getCrewEditWizardBean().getRunBusiness().getYearsMap(run);
						Year year = (Year) yearMap.get(yearString);
						if (year != null && year.getLastRegistrationDate() != null) {
							IWTimestamp lastRegistrationDate = new IWTimestamp(year.getLastRegistrationDate());
							if (thisYearStamp.isEarlierThan(lastRegistrationDate)) {
								finished = false;
								show = true;
							}
						}
						Year nextYear = (Year) yearMap.get(nextYearString);
						if (finished && nextYear != null) {
							runnerYearString = nextYearString;
							show = true;
						}
						
						if(constrainedRunIds!=null){
							boolean match = false;
							for (int i = 0; i < constrainedRunIds.length; i++) {
								String constrainedId = constrainedRunIds[i];
								if(constrainedId.equals(runId)){
									match=true;
								}
							}
							if(!match){
								show=false;
							}
						}
						
						if (show) {
							
//							if (this.runner != null && this.runner.getUser() != null) {
//								if (!getRunBusiness().isRegisteredInRun(runnerYearString, run, this.runner.getUser())) {
//									addMenuElement(runId, iwrb.getLocalizedString(run.getName(), run.getName()));
//								}
//							}
//							else {
							
							selectItem = new SelectItem();
							
							selectItem.setValue(runId);
							selectItem.setLabel(iwrb.getLocalizedString(run.getName(), run.getName()));
							runsChoices.add(selectItem);
//							}
						}
					}
				}
				
			} catch (Exception e) {
				Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Exception while retrieving runs", e);
			}
		}
		
		return runsChoices;
	}
	
	protected void validateCrewLabel(FacesContext context, String crewLabel, Integer runId) {
	
		IWTimestamp ts = IWTimestamp.RightNow();
	    Integer currentYear = new Integer(ts.getYear());
	    
	    RunBusiness runBusiness = getCrewEditWizardBean().getRunBusiness();
	    boolean crewLabelExists = runBusiness.isCrewLabelAlreadyExistsForRun(runId.intValue(), currentYear.intValue(), crewLabel);
	    
	    if(crewLabelExists) {
//	    	TODO: check if it belongs to current user
	    	
	    	boolean ownerCurrentUser = false;
	    	
	    	if(ownerCurrentUser) {
//	    		TODO: display message, that he should edit his existing crew
	    	} else {
//	    		TODO: display message, that the group with such name already exists for the run
	    	}
	    }
	    
	    System.out.println("crewLabelExists: "+crewLabelExists);
	}
	
	public void validateRunSelection(FacesContext context, UIComponent toValidate, Object value) {
		
//		will be caught by required
		if(value == null)
			return;
		
		IWContext iwc = IWContext.getIWContext(context);
		
		try {
			Integer runId = new Integer((String)value);
			
			User ownerUser = iwc.getCurrentUser();
			RunBusiness runBusiness = getCrewEditWizardBean().getRunBusiness();
			Group runGroup = runBusiness.getRunGroupByGroupId(runId);
			Group yearGroup = ConverterUtility.getInstance().convertGroupToRun(runGroup).getCurrentRegistrationYear();
			
			Participant participant = null;
			
			try {
				participant = runBusiness.getParticipantByRunAndYear(ownerUser, runGroup, yearGroup);
				
			} catch (FinderException e) {
				// TODO: this happens, when nothing found
			}
			
			if(participant == null)
//				will be caught by createCrew()
				return;

			if(participant.getRunGroupName() != null) {
				
//				already registered to a crew
				IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
				
				String messageText;
				
				if(participant.isCrewOwner())
					messageText = "You have already created and registered to the crew labeled "+participant.getRunGroupName()+" for this run. You may edit crew from crews list, and/or delete it.";
				else
					messageText = "You are registered to the crew labeled "+participant.getRunGroupName()+" for this run already.";
				
				
				((UIInput)toValidate).setValid(false);
				FacesMessage message = new FacesMessage(messageText);
				context.addMessage(toValidate.getClientId(context), message);
			}
			
		} catch (Exception e) {
			
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);

//			TODO: add err messages
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Exception while validating run choice", e);
		}
	}
	
	public void createCrew() {

		FacesContext context = FacesContext.getCurrentInstance();
		
		String crewLabel = getCrewLabel();
		Integer runId = new Integer(getRunId());
		
		validateCrewLabel(context, crewLabel, runId);
		
		if(context.getMessages() != null && context.getMessages().hasNext())
			return;
		
		try {
			
			IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
			
			User ownerUser = iwc.getCurrentUser();
			RunBusiness runBusiness = getCrewEditWizardBean().getRunBusiness();
			Group runGroup = runBusiness.getRunGroupByGroupId(runId);
			Group yearGroup = ConverterUtility.getInstance().convertGroupToRun(runGroup).getCurrentRegistrationYear();
			
			Participant participant = null;
			
			try {
				participant = runBusiness.getParticipantByRunAndYear(ownerUser, runGroup, yearGroup);
				
			} catch (FinderException e) {
				// TODO: this happens, when nothing found
			}
			
			if(participant == null) {
				
//				TODO: add error message, that current user is not participant for selected run
				
				System.out.println(":participant null ....................:");
				return;
			}
			
			participant.setRunGroupName(crewLabel);
			participant.setIsCrewOwner(true);
			participant.store();
			getCrewEditWizardBean().setParticipant(participant);
			getCrewEditWizardBean().setMode(CrewEditWizardBean.editCrewMode);
			
		} catch (Exception e) {

//			TODO: add err messages
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Exception while creating new crew", e);
		}
		
		System.out.println("creating crew for label: "+crewLabel+" : runId: "+runId);
		
	}
	
	public void editCrew() {
		
		Participant participant = getCrewEditWizardBean().getParticipant();
		
//		TODO: check if this participant is amongst current user participants
		
		if(!participant.isCrewOwner()) {
//			TODO: add msg
			System.out.println("wrong wrong");
			setWizardMode(false);
		}
		
		setCrewLabel(participant.getRunGroupName());
		
		setWizardMode(true);
		getCrewEditWizardBean().setMode(CrewEditWizardBean.editCrewMode);
		
		System.out.println("edit crew:"+getCrewEditWizardBean().getParticipantId());
		
	}
	
	public void updateCrew() {

		System.out.println("updating crew");
		
		FacesContext context = FacesContext.getCurrentInstance();
		
		Participant participant = getCrewEditWizardBean().getParticipant();
		String crewLabel = getCrewLabel();
		
		if(!crewLabel.equals(participant.getRunGroupName())) {
		
			Integer runId = new Integer(getRunId());
			validateCrewLabel(context, crewLabel, runId);
			
			participant.setRunGroupName(crewLabel);
			participant.store();
		}
	}
}