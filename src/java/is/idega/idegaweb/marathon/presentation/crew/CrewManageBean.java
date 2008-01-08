package is.idega.idegaweb.marathon.presentation.crew;

import is.idega.idegaweb.marathon.IWBundleStarter;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Year;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.data.Group;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/01/08 19:20:25 $ by $Author: civilis $
 *
 */
public class CrewManageBean {

	private boolean wizardMode = false;
	private String crewLabel;
	private String runId;
	private List runsChoices;
	private RunBusiness runBusiness;
	
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
		
//		TODO: resolve from crew (if any) if null
		System.out.println("getCrewLabel: "+crewLabel);
		
		return crewLabel;
	}

	public void setCrewLabel(String crewLabel) {
		
		System.out.println("setting crew label: "+crewLabel);
		this.crewLabel = crewLabel;
	}

	public String getRunId() {
//		TODO: resolve from crew (if any) if null
		return runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}
	
	public List getRuns() {

		
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
				
				Collection runs = getRunBusiness().getRuns();
				
				if (runs != null) {
					
					System.out.println("total: "+runs.size());
					for (Iterator iterator = runs.iterator(); iterator.hasNext();) {
						
						Group run = (Group) iterator.next();
						System.out.println("run resolved: "+run);
						String runnerYearString = yearString;
						String runId = run.getPrimaryKey().toString();
						
						boolean show = false;
						boolean finished = true;
						Map yearMap = getRunBusiness().getYearsMap(run);
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
	
	public void validateRunSelection(FacesContext context, UIComponent toValidate, Object value) {
		
		if(false) {
			
			IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
			
			((UIInput)toValidate).setValid(false);
			FacesMessage message = new FacesMessage(iwrb.getLocalizedString("dist_ch.err.ccvIncorrect", "CCV number should be a 3 digit number"));
			context.addMessage(toValidate.getClientId(context), message);
		}
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
}