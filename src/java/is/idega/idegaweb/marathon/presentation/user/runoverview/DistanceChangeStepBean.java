package is.idega.idegaweb.marathon.presentation.user.runoverview;

import is.idega.idegaweb.marathon.IWBundleStarter;
import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Participant;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
import com.idega.util.CoreConstants;
import com.idega.util.ListUtil;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.10 $
 * 
 * Last modified: $Date: 2008/07/23 22:27:25 $ by $Author: palli $
 * 
 */
public class DistanceChangeStepBean {
	private static final String PARAMETER_SHIRT_SIZES_PER_RUN = "shirt_sizes_per_run";

	private DistanceChangeWizardBean wizardBean;
	private boolean wizardMode;

	/**
	 * List<SelectItem>
	 */
	private List runDistances;

	private List runShirtSizes;

	public String getChosenDistanceName() {

		IWContext iwc = IWContext.getIWContext(FacesContext
				.getCurrentInstance());

		return iwc.getIWMainApplication().getBundle(
				IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc)
				.getLocalizedString(
						getWizardBean().getParticipant().getRunDistanceGroup()
								.getName(),
						getWizardBean().getParticipant().getRunDistanceGroup()
								.getName());
	}

	public String getChosenTShirtName() {
		IWContext iwc = IWContext.getIWContext(FacesContext
				.getCurrentInstance());

		return iwc.getIWMainApplication().getBundle(
				IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc)
				.getLocalizedString(
						getWizardBean().getParticipant().getShirtSize(),
						getWizardBean().getParticipant().getShirtSize());
	}

	public String getRunLabel() {

		IWContext iwc = IWContext.getIWContext(FacesContext
				.getCurrentInstance());
		Participant participant = getWizardBean().getParticipant();

		return iwc.getIWMainApplication().getBundle(
				IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc)
				.getLocalizedString(participant.getRunTypeGroup().getName(),
						participant.getRunTypeGroup().getName())
				+ " " + participant.getRunYearGroup().getName();
	}

	public boolean isWizardMode() {
		return wizardMode;
	}

	public void setWizardMode(boolean wizardMode) {

		this.wizardMode = wizardMode;
	}

	public List getRunDistances() {
		IWContext iwc = IWContext.getIWContext(FacesContext
				.getCurrentInstance());
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(
				IWBundleStarter.IW_BUNDLE_IDENTIFIER)
				.getResourceBundle(iwc);
		runDistances = new ArrayList();
		
		boolean isInRunGroup = false;
		if (wizardBean.getParticipant().getRunGroupName() != null && !"".equals(wizardBean.getParticipant().getRunGroupName())) {
			isInRunGroup = true;
		}
		

		SelectItem selectItem = new SelectItem();

		selectItem.setValue("-1");
		if (!isInRunGroup) {
			selectItem.setLabel(iwrb.getLocalizedString(
				"run_year_ddd.select_distance", "Select distance..."));
		} else {
			selectItem.setLabel(iwrb.getLocalizedString(
					"run_year_ddd.select_distance_run_group", "You are registered in a run group and can't change the distance"));			
		}
		runDistances.add(selectItem);

		if (!isInRunGroup) {
		try {
			Collection distancesGroups = getWizardBean().getRunBusiness()
					.getDistancesMap(
							getWizardBean().getParticipant()
									.getRunTypeGroup(),
							getWizardBean().getParticipant()
									.getRunDistanceGroup().getYear()
									.getYearString());
			List distances = new ArrayList(distancesGroups.size());
			ConverterUtility converterUtility = ConverterUtility
					.getInstance();

			for (Iterator distancesGroupsIterator = distancesGroups
					.iterator(); distancesGroupsIterator.hasNext();)
				distances
						.add(converterUtility
								.convertGroupToDistance((Group) distancesGroupsIterator
										.next()));

			List disallowedDistances = getWizardBean().getRunBusiness()
					.getDisallowedDistancesPKs(
							getWizardBean().getParticipant().getUser(),
							distances);

			for (Iterator iterator = distancesGroups.iterator(); iterator
					.hasNext();) {

				Group distanceGroup = (Group) iterator.next();

				selectItem = new SelectItem();

				if (disallowedDistances.contains(distanceGroup
						.getPrimaryKey().toString())) {

					selectItem.setValue("-1");
					selectItem
							.setLabel(new StringBuffer(iwrb
									.getLocalizedString(distanceGroup
											.getName(), distanceGroup
											.getName()))
									.append(CoreConstants.SPACE)
									.append(
											iwrb
													.getLocalizedString(
															"runDistance.choiceNotAvailableBecauseOfAge",
															"(Not available for your age)"))
									.toString());

				} else {

					selectItem.setValue(distanceGroup.getPrimaryKey()
							.toString());
					selectItem.setLabel(iwrb.getLocalizedString(
							distanceGroup.getName(), distanceGroup
									.getName()));
				}

				runDistances.add(selectItem);
			}
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		} catch (FinderException e) {
			throw new RuntimeException(e);
		}
		} else {
			selectItem = new SelectItem();
			selectItem.setValue(wizardBean.getParticipant().getRunDistanceGroup().getPrimaryKey()
					.toString());
			selectItem.setLabel(iwrb.getLocalizedString(
					wizardBean.getParticipant().getRunDistanceGroup().getName(), wizardBean.getParticipant().getRunDistanceGroup()
							.getName()));

			runDistances.add(selectItem);
		}

		return runDistances;
	}

	public void setRunDistances(List runDistances) {

		if (runDistances != null)
			this.runDistances = runDistances;
	}

	public List getRunShirtSizes() {
		Distance distance = null;
		if (getWizardBean().getNewDistanceId() != null) {
			try {
				distance = this.getWizardBean().getRunBusiness()
						.getDistanceByID(
								Integer.parseInt(getWizardBean()
										.getNewDistanceId()));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			distance = this.getWizardBean().getParticipant()
					.getRunDistanceGroup();
		}

		IWContext iwc = IWContext.getIWContext(FacesContext
				.getCurrentInstance());
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(
				IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		runShirtSizes = new ArrayList();

		SelectItem selectItem = new SelectItem();

		selectItem.setValue("-1");
		selectItem.setLabel(iwrb.getLocalizedString(
				"run_year_ddd.select_shirt_size", "Select shirt size..."));
		runShirtSizes.add(selectItem);

		if (distance != null) {
			String shirtSizeMetadata = distance
					.getMetaData(PARAMETER_SHIRT_SIZES_PER_RUN);
			List shirtSizes = null;
			if (shirtSizeMetadata != null) {
				shirtSizes = ListUtil
						.convertCommaSeparatedStringToList(shirtSizeMetadata);
			}
			if (shirtSizes != null) {
				Iterator shirtIt = shirtSizes.iterator();
				while (shirtIt.hasNext()) {
					String shirtSizeKey = (String) shirtIt.next();
					selectItem = new SelectItem();
					selectItem.setValue(shirtSizeKey);
					selectItem.setLabel(iwrb.getLocalizedString("shirt_size."
							+ shirtSizeKey, shirtSizeKey));

					runShirtSizes.add(selectItem);
				}
			}
		}

		return runShirtSizes;
	}

	public void setRunShirtSizes(List runShirtSizes) {

		if (runShirtSizes != null)
			this.runShirtSizes = runShirtSizes;
	}

	public DistanceChangeWizardBean getWizardBean() {
		return wizardBean;
	}

	public void setWizardBean(DistanceChangeWizardBean wizardBean) {
		this.wizardBean = wizardBean;
	}

	public void validateDistanceChange(FacesContext context,
			UIComponent toValidate, Object value) {

		FacesMessage message = null;

		IWContext iwc = IWContext.getIWContext(FacesContext
				.getCurrentInstance());
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(
				IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);

		try {
			String valueStr = (String) value;

			if ("-1".equals(valueStr)) {
				message = new FacesMessage(iwrb.getLocalizedString(
						"dist_ch.err.chooseAllowedDistance",
						"Please choose allowed distance"));

			} else {

				Collection distancesGroups = getWizardBean().getRunBusiness()
						.getDistancesMap(
								getWizardBean().getParticipant()
										.getRunTypeGroup(),
								getWizardBean().getParticipant()
										.getRunDistanceGroup().getYear()
										.getYearString());
				List distances = new ArrayList(distancesGroups.size());
				ConverterUtility converterUtility = ConverterUtility
						.getInstance();

				for (Iterator distancesGroupsIterator = distancesGroups
						.iterator(); distancesGroupsIterator.hasNext();)
					distances
							.add(converterUtility
									.convertGroupToDistance((Group) distancesGroupsIterator
											.next()));

				List disallowedDistances = getWizardBean().getRunBusiness()
						.getDisallowedDistancesPKs(
								getWizardBean().getParticipant().getUser(),
								distances);

				if (disallowedDistances.contains(valueStr))
					message = new FacesMessage(iwrb.getLocalizedString(
							"dist_ch.err.distanceNotAllowed",
							"Chosen distance is not allowed for you"));
			}

		} catch (Exception e) {
			Logger
					.getLogger(this.getClass().getName())
					.log(
							Level.SEVERE,
							"Exception while validating distance change select input choice",
							e);
			message = new FacesMessage(
					iwrb
							.getLocalizedString(
									"dist_ch.err.errorValidatingDistanceChoice",
									"Sorry, error occured while validating your distance choice. Please try again."));
		}

		if (message != null) {
			((UIInput) toValidate).setValid(false);
			context.addMessage(toValidate.getClientId(context), message);
		}
	}

	public void validateShirtSizeChange(FacesContext context,
			UIComponent toValidate, Object value) {

		FacesMessage message = null;

		IWContext iwc = IWContext.getIWContext(FacesContext
				.getCurrentInstance());
		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(
				IWBundleStarter.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);

		String valueStr = (String) value;

		if ("-1".equals(valueStr)) {
			message = new FacesMessage(iwrb.getLocalizedString(
					"dist_ch.err.chooseAllowedDistance",
					"Please choose allowed distance"));
		}

		if (message != null) {
			((UIInput) toValidate).setValid(false);
			context.addMessage(toValidate.getClientId(context), message);
		}
	}

	public void validateCCVNumber(FacesContext context, UIComponent toValidate,
			Object value) {

		if (!((String) value).matches("[0-9]{3}")) {

			IWContext iwc = IWContext.getIWContext(FacesContext
					.getCurrentInstance());
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(
					IWBundleStarter.IW_BUNDLE_IDENTIFIER)
					.getResourceBundle(iwc);

			((UIInput) toValidate).setValid(false);
			FacesMessage message = new FacesMessage(iwrb.getLocalizedString(
					"dist_ch.err.ccvIncorrect",
					"CCV number should be a 3 digit number"));
			context.addMessage(toValidate.getClientId(context), message);
		}
	}

	public void validateCardExpiresDate(FacesContext context,
			UIComponent toValidate, Object value) {

	}
}