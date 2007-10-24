/*
 * Created on 19.8.2004
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.location.data.Country;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.business.GenderBusiness;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderBMPBean;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.text.TextSoap;


/**
 * @author laddi
 */
public class MarathonGroupUsersImportBean extends IBOServiceBean implements MarathonGroupUsersImport {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -7831000010990786731L;
	public static final  String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.marathon";
	
	ImportFile file;
	UserBusiness userBusiness;
	GroupBusiness groupBusiness;
	GenderBusiness genderBusiness;
	RunBusiness runBusiness;
	Group group;
	
	
	public boolean handleRecords() throws RemoteException {
		this.userBusiness = getUserBusiness(getIWApplicationContext());
		this.groupBusiness = getGroupBusiness(getIWApplicationContext());
		this.genderBusiness = getGenderBusiness(getIWApplicationContext());
		this.runBusiness = getRunBusiness(getIWApplicationContext());
		
		Vector errors = new Vector();
		
		if (this.file != null) {
			String line = (String) this.file.getNextRecord();
			String nameTitle = line.substring(0,4);
			if (nameTitle == null || !(nameTitle.equals("Name") || nameTitle.equals("Nafn"))) {
				throw new RuntimeException("Not a valid import file");
			}
			int counter = 0;
			while (line != null && !"".equals(line)) {
				++counter;
				if (counter %10 == 0) {
					System.out.println("MarathonGroupUsersImportCounter = "+counter);
				}
				if (!handleLine(line)) {
					errors.add(line);
				}
				line = (String) this.file.getNextRecord();
			}
			System.out.println("Total numbers of runners imported: " + counter);
		}
		
		if (!errors.isEmpty()) {
			System.out.println("Errors in the following lines :");
			Iterator iter = errors.iterator();
			while (iter.hasNext()) {
				System.out.println((String) iter.next());
			}
		}
		return true;
	}
	
	private boolean handleLine(String line) throws RemoteException {
		IWBundle iwb = this.getIWApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		IWResourceBundle iwrb = iwb.getResourceBundle(IWContext.getInstance().getCurrentLocale());
		
		ArrayList values = this.file.getValuesFromRecordString(line);
		String name = ((String) values.get(0)).trim();
		String personalID = ((String) values.get(1)).trim();
		String dateOfBirth = ((String) values.get(2)).trim();
		String gender = ((String) values.get(3)).trim();
		String run = ((String) values.get(4)).trim();
		String year = ((String) values.get(5)).trim();
		String distance = ((String) values.get(6)).trim();
		String email = ((String) values.get(7)).trim();
		String address = ((String) values.get(8)).trim();
		String city = ((String) values.get(9)).trim();
		String postalCode = ((String) values.get(10)).trim();
		String countryName = ((String) values.get(11)).trim();
		String nationality = ((String) values.get(12)).trim();
		String shirtSize = ((String) values.get(13)).trim();
		
		if ((name.equals("Nafn")) || (name.equals("Name")) ) {
			//ignoring headers in first line
			return true;
		}
		if (name.equals("") && personalID.equals("")) {
			//ignoring lines where neither name or personal ID is set
			return false;
		}
		
		Date dobDate = null;
		IWTimestamp dateOfBirthStamp = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
			dobDate = formatter.parse(dateOfBirth);
			dateOfBirthStamp = new IWTimestamp(dobDate);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		Country country = null;
		try {
			country = userBusiness.getAddressBusiness().getCountryHome().findByCountryName(countryName);
		} catch (FinderException e) {
			//country not found
		}
		
		User user = null;
		try {
			if (personalID != null && personalID.length() > 0) {
				personalID = TextSoap.findAndReplace(personalID, "-", "");
				personalID = TextSoap.removeWhiteSpace(personalID);
				user = this.userBusiness.getUser(personalID);
			}
		}
		catch (FinderException fe) {
			System.out.println("User not found by personal ID");
		}
		
		if (user == null) {
			try {
				if (dobDate != null) {
					user = this.userBusiness.getUserHome().findByDateOfBirthAndName(new java.sql.Date(dobDate.getTime()), name);
				}
			}
			catch (FinderException fe) {
				System.out.println("User not found by name and date_of_birth");
			}
		}
		
		
		if (user == null) {
			Gender femaleGender = null;
			Gender maleGender = null;
			try {
				femaleGender = genderBusiness.getFemaleGender();
				maleGender = genderBusiness.getMaleGender();
			} catch (FinderException e) {
				e.printStackTrace();
			}
			
			Gender theGender = null;
			if (gender != null && gender.equals(GenderBMPBean.NAME_MALE)) {
				theGender = maleGender;
			} else if (gender != null && gender.equals(GenderBMPBean.NAME_FEMALE)) {
				theGender = femaleGender;
			} 
			user = this.runBusiness.saveUser(name, personalID, dateOfBirthStamp, theGender, address, city, postalCode, country);
		}
		if (user != null) {
			if (!email.equals("")) {
				try {
					userBusiness.updateUserMail(user, email);
				} catch (Exception e){
					System.out.println("Unable to set " + email + " as email for user: "+ user);
				}
			}
		}
		
		List groupTypes = new ArrayList();
		groupTypes.add(IWMarathonConstants.GROUP_TYPE_RUN);
		Collection runs = this.groupBusiness.getGroupsByGroupNameAndGroupTypes(run, groupTypes, true);
		
		Iterator iterator = runs.iterator();
		Group runGroup = null; 
		if (iterator.hasNext()) {
			runGroup = (Group)iterator.next();
		}
		
	    if (year == null || year.equals("")) {
	    	IWTimestamp thisYearStamp = IWTimestamp.RightNow();
	    	year = String.valueOf(thisYearStamp.getYear());
	    }
		if (runGroup != null) {
			Group yearGroup = null;
			iterator = runGroup.getChildrenIterator();
			while (iterator.hasNext()) {
				Group element = (Group) iterator.next();
				if (element.getName().equals(year)) {
					yearGroup = element;
					break;
				}
			}
			
			Group distanceGroup = null;
			Collection distances = this.runBusiness.getDistancesMap(runGroup, year);
			if (distances == null) {
				System.out.println("Distance not found, ignoring line");
				return false;
			}
			iterator = distances.iterator();
			while (iterator.hasNext()) {
				Group element = (Group) iterator.next();
				if (element.getName().equals(distance)) {
					distanceGroup = element;
					break;
				}
			}
			Participant participant = null;
			boolean newParticipant = false;
			boolean movedParticipant = false;
			try {
				participant = this.runBusiness.getParticipantByRunAndYear(user, runGroup, yearGroup);
			}
			catch (FinderException fe) {
				try {
					participant = this.runBusiness.importParticipant(user, runGroup, yearGroup, distanceGroup);
					newParticipant = true;
				}
				catch (CreateException ce) {
					ce.printStackTrace();
					return false;
				}
				catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			Group ageGenderGroup = this.runBusiness.getAgeGroup(user, runGroup, distanceGroup);
			Distance distanceObject = null;
			try {
				distanceObject = ConverterUtility.getInstance().convertGroupToDistance(distanceGroup);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (participant.getRunDistanceGroupID() != ((Integer)distanceGroup.getPrimaryKey()).intValue()) {
				Group oldAgeGenderGroup = participant.getRunGroupGroup();
				Collection userIDs = new ArrayList();
				userIDs.add(user.getPrimaryKey().toString());
				userBusiness.moveUsers(IWContext.getInstance(),userIDs, oldAgeGenderGroup, ((Integer)ageGenderGroup.getPrimaryKey()).intValue());
				movedParticipant = true;
			}
			if (newParticipant || movedParticipant) {
				participant.setParticipantNumber(runBusiness.getNextAvailableParticipantNumber(runGroup, distanceObject));
			}
			participant.setRunDistanceGroup(distanceGroup);
			participant.setRunGroupGroup(ageGenderGroup);
			participant.setUserNationality(nationality);
			participant.setShirtSize(shirtSize);
			participant.store();
			
			String userNameString = "";
			String passwordString = "";
			
			if (userBusiness.hasUserLogin(user)) {
				try {
					LoginTable login = LoginDBHandler.getUserLogin(user);
					userNameString = login.getUserLogin();
					passwordString = LoginDBHandler.getGeneratedPasswordForUser();
					LoginDBHandler.changePassword(login, passwordString);
				} catch (Exception e) {
					System.out.println("Error re-generating password for user: " + user.getName());
					e.printStackTrace();
				}
			} else {
				try {
					LoginTable login = this.userBusiness.generateUserLogin(user);
					userNameString = login.getUserLogin();
					passwordString = login.getUnencryptedUserPassword();
				} catch (Exception e) {
					System.out.println("Error creating login for user: " + user.getName());
					e.printStackTrace();
				}
			}
			Run selectedRun = null;
			try {
				selectedRun = ConverterUtility.getInstance().convertGroupToRun(runGroup);
			} catch (FinderException e) {
				//Run not found
			}
			String informationPageString = "";
			String runHomePageString = "";
			if (selectedRun != null) {
				runHomePageString = selectedRun.getRunHomePage();
				if (iwrb.getLocale().equals(LocaleUtil.getIcelandicLocale())) {
					informationPageString = selectedRun.getRunInformationPage();
				} else {
					informationPageString = selectedRun.getEnglishRunInformationPage();
				}
			}
			Object[] args = { user.getName(), iwrb.getLocalizedString(runGroup.getName(),runGroup.getName()), iwrb.getLocalizedString(distance,distance), "", String.valueOf(participant.getParticipantNumber()), runHomePageString, informationPageString, userNameString, passwordString };
			String subject = iwrb.getLocalizedString("registration_received_subject_mail", "Your registration has been received.");
			String body = MessageFormat.format(iwrb.getLocalizedString("registration_received_body_mail", "Your registration has been received."), args);
			runBusiness.sendMessage(email, subject, body);
		}
		
		return true;
	}
	

	public void setImportFile(ImportFile file) throws RemoteException {
		this.file = file;
	}

	public void setRootGroup(Group rootGroup) throws RemoteException {
	}

	public List getFailedRecords() throws RemoteException {
		return null;
	}
	
	protected UserBusiness getUserBusiness(IWApplicationContext iwac) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected GroupBusiness getGroupBusiness(IWApplicationContext iwac) {
		try {
			return (GroupBusiness) IBOLookup.getServiceInstance(iwac, GroupBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	protected GenderBusiness getGenderBusiness(IWApplicationContext iwac) {
		try {
			return (GenderBusiness) IBOLookup.getServiceInstance(iwac, GenderBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	protected RunBusiness getRunBusiness(IWApplicationContext iwac) {
		try {
			return (RunBusiness) IBOLookup.getServiceInstance(iwac, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}