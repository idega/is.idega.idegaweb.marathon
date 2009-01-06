package is.idega.idegaweb.marathon.presentation.user.runoverview;

import is.idega.idegaweb.marathon.IWBundleStarter;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.DistanceHome;
import is.idega.idegaweb.marathon.data.Participant;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.FinderException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.CreditCardNumber;
import com.idega.user.data.Group;
import com.idega.util.CoreConstants;
import com.idega.util.LocaleUtil;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.17 $
 * 
 * Last modified: $Date: 2009/01/06 09:41:37 $ by $Author: palli $
 * 
 */
public class DistanceChangeWizardBean {

	private String participantId;
	private Participant participant;
	private RunBusiness runBusiness;
	//private String newTShirtId;
	private String newDistanceId;
	private String newDistanceName;
	private String cardHolderName;
	private CreditCardNumber creditCardNumber;
	private String ccvNumber;
	private Date cardExpirationDate;
	private Price distanceChangePrice;

	private static final String PROPERTY_DISTANCE_PRICE_ISK = "distance_change_price_ISK";
	private static final String PROPERTY_DISTANCE_PRICE_EUR = "distance_change_price_EUR";
	private static final String ISK_CURRENCY_LABEL = "ISK";
	private static final String EUR_CURRENCY_LABEL = "EUR";

	public String getNewDistanceId() {

		if (newDistanceId == null)
			newDistanceId = getParticipant().getRunDistanceGroup()
					.getPrimaryKey().toString();

		return newDistanceId;
	}

	public void setNewDistanceId(String newDistanceId) {

		if (newDistanceId != null) {
			newDistanceName = null;
			this.newDistanceId = newDistanceId;
		}
	}

	/*public String getNewTShirtId() {
		if (newTShirtId == null)
			newTShirtId = getParticipant().getShirtSize();
		;

		return newTShirtId;
	}*/

	/*public void setNewTShirtId(String shirtSize) {
		if (shirtSize != null) {
			this.newTShirtId = shirtSize;
		}
	}*/

	public String getParticipantId() {
		return participantId;
	}

	public void setParticipantId(String participantId) {
		participant = null;
		newDistanceId = null;
		newDistanceName = null;
		cardHolderName = null;
		creditCardNumber = null;
		ccvNumber = null;
		cardExpirationDate = null;
		distanceChangePrice = null;
		this.participantId = participantId;
		//newTShirtId = null;
	}

	public Participant getParticipant() {

		if (participant == null) {

			try {
				participant = getRunBusiness().getParticipantByPrimaryKey(
						new Integer(getParticipantId()).intValue());

			} catch (RemoteException e) {
				throw new RuntimeException(e);
			}
		}

		return participant;
	}

	public RunBusiness getRunBusiness() {

		if (runBusiness == null) {

			try {
				runBusiness = (RunBusiness) IBOLookup.getServiceInstance(
						IWContext.getIWContext(FacesContext
								.getCurrentInstance()), RunBusiness.class);
			} catch (IBOLookupException e) {
				throw new RuntimeException(e);
			}
		}

		return runBusiness;
	}

	public String getCardHolderName() {

		if (cardHolderName == null)
			cardHolderName = getParticipant().getUser().getName();

		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {

		if (cardHolderName != null)
			this.cardHolderName = cardHolderName;
	}

	public CreditCardNumber getCreditCardNumber() {

		return creditCardNumber;
	}

	public void setCreditCardNumber(CreditCardNumber creditCardNumber) {

		if (creditCardNumber != null)
			this.creditCardNumber = creditCardNumber;
	}

	public String getCcvNumber() {
		return ccvNumber;
	}

	public void setCcvNumber(String ccvNumber) {

		if (ccvNumber != null)
			this.ccvNumber = ccvNumber;
	}

	public Date getCardExpirationDate() {

		return cardExpirationDate;
	}

	public void setCardExpirationDate(Date cardExpirationDate) {

		if (cardExpirationDate != null)
			this.cardExpirationDate = cardExpirationDate;
	}

	public String getNewDistanceName() {

		if (newDistanceName == null && getNewDistanceId() != null) {

			try {
				Distance newDistance = getDistanceByGroupId(getNewDistanceId());

				IWContext iwc = IWContext.getIWContext(FacesContext
						.getCurrentInstance());

				newDistanceName = iwc.getIWMainApplication().getBundle(
						IWBundleStarter.IW_BUNDLE_IDENTIFIER)
						.getResourceBundle(iwc).getLocalizedString(
								newDistance.getName(), newDistance.getName());

			} catch (FinderException e) {
				Logger
						.getLogger(this.getClass().getName())
						.log(
								Level.SEVERE,
								"Exception while retrieving distance group by group id",
								e);
			}
		}

		return newDistanceName;
	}

	public void setNewDistanceName(String newDistanceName) {
		this.newDistanceName = newDistanceName;
	}

	public Distance getDistanceByGroupId(String groupId) throws FinderException {

		try {
			DistanceHome home = (DistanceHome) IDOLookup
					.getHome(Distance.class);
			return (Distance) home.findByPrimaryKey(groupId);
		} catch (IDOLookupException ile) {
			throw new FinderException(ile.getMessage());
		}
	}

	public Price getDistanceChangePrice() {
		IWContext iwc = IWContext.getIWContext(FacesContext
				.getCurrentInstance());
		boolean isIcelandic = iwc.getCurrentLocale().equals(
				LocaleUtil.getIcelandicLocale());

		IWMainApplication iwma = IWMainApplication
				.getIWMainApplication(IWContext.getInstance());
		IWMainApplicationSettings applicationSettings = iwma.getSettings();

		String payedString = this.participant.getPayedAmount();
		float alreadyPayed = 0.0f;
		if (payedString != null && !"".equals(payedString)) {
			try {
				alreadyPayed = Float.parseFloat(payedString);
			} catch (Exception e) {
				alreadyPayed = 0.0f;
			}
		}

		Distance distance = null;
		float distancePrice = 0.0f;

		try {
			distance = getRunBusiness().getDistanceByID(
					Integer.parseInt(newDistanceId));
			distancePrice = distance.getPrice(iwc.getCurrentLocale());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		if (isIcelandic) {
			float price = Float.parseFloat(applicationSettings.getProperty(
					PROPERTY_DISTANCE_PRICE_ISK, "500"));
			if (distancePrice > alreadyPayed) {
				price += (distancePrice - alreadyPayed);
			}

			distanceChangePrice = new Price(new Float(price),
					ISK_CURRENCY_LABEL, iwc.getCurrentLocale());
		} else {
			float price = Float.parseFloat(applicationSettings.getProperty(
					PROPERTY_DISTANCE_PRICE_EUR, "4.5"));
			if (distancePrice > alreadyPayed) {
				price += (distancePrice - alreadyPayed);
			}

			distanceChangePrice = new Price(new Float(price),
					EUR_CURRENCY_LABEL, iwc.getCurrentLocale());
		}

		return distanceChangePrice;
	}

	public void setDistanceChangePrice(Price distanceChangePrice) {

		// fixed price
	}

	public void submitDistanceChange() {

		try {
			FacesContext context = FacesContext.getCurrentInstance();
			IWContext iwc = IWContext.getIWContext(context);
			String referenceNumber = getParticipant().getUser().getPersonalID()
					.replaceAll("-", CoreConstants.EMPTY);

			String properties = getRunBusiness().authorizePayment(
					getCardHolderName(),
					getCreditCardNumber().getFullNumber(CoreConstants.EMPTY),
					getCardExpirationDate(), getCcvNumber(),
					getDistanceChangePrice().getPrice().floatValue(),
					getDistanceChangePrice().getCurrencyLabel(),
					referenceNumber);

			String newDistanceId = getNewDistanceId();
			//String tShirtSize = getNewTShirtId();
			Distance distance = getDistanceByGroupId(newDistanceId);
			Group distanceGroup = getRunBusiness().getUserBiz().getGroupBusiness().getGroupByGroupID(Integer.parseInt(newDistanceId)); 
			Participant participant = getParticipant();

			if (!distance.getPrimaryKey().toString().equals(
					participant.getRunDistanceGroup().getPrimaryKey()
							.toString())) {
				Group ageGenderGroup = getRunBusiness().getAgeGroup(
						participant.getUser(), participant.getRunTypeGroup(),
						participant.getRunDistanceGroup());
				ageGenderGroup.removeUser(participant.getUser(),participant.getUser());
				ageGenderGroup = getRunBusiness().getAgeGroup(
						participant.getUser(), participant.getRunTypeGroup(),
						distanceGroup);
				ageGenderGroup.addGroup(participant.getUser());
				participant.setRunDistanceGroup(distance);
				participant.setRunGroupGroup(ageGenderGroup);
				participant.setPayedAmount(String.valueOf(distance.getPrice(iwc.getCurrentLocale())));
			}
			//participant.setShirtSize(tShirtSize);
			participant.store();
			setParticipantId(participant.getPrimaryKey().toString());

			getRunBusiness().finishPayment(properties);
		} catch (CreditCardAuthorizationException e) {

			FacesContext context = FacesContext.getCurrentInstance();
			IWContext iwc = IWContext.getIWContext(context);
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(
					IWBundleStarter.IW_BUNDLE_IDENTIFIER)
					.getResourceBundle(iwc);

			Logger
					.getLogger(this.getClass().getName())
					.log(
							Level.SEVERE,
							":Distance change. Exception while paying for distance change: Exception message: "
									+ e.getLocalizedMessage(iwrb)
									+ " error code: "
									+ e.getErrorNumber()
									+ ": error message: " + e.getErrorMessage(),
							e);

			FacesMessage message = new FacesMessage(
					iwrb
							.getLocalizedString(
									"dist_ch.err.authorizationException",
									"Internal error occurred while authorizing credit card. Please try again."));
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);

		} catch (Exception e) {

			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					":Distance change. Exception while changing distance:", e);

			FacesContext context = FacesContext.getCurrentInstance();
			IWContext iwc = IWContext.getIWContext(context);
			IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(
					IWBundleStarter.IW_BUNDLE_IDENTIFIER)
					.getResourceBundle(iwc);
			FacesMessage message = new FacesMessage(
					iwrb
							.getLocalizedString(
									"dist_ch.err.distanceChangeException",
									"Internal error occurred while changing distance. Please try again."));
			message.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, message);
		}
	}
}