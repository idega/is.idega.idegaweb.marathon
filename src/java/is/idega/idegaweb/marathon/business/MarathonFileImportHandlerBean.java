package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.Participant;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.localisation.business.LocaleSwitcher;
import com.idega.core.location.data.Country;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.user.business.GenderBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.text.Name;


/**
 * @author gimmi
 */
public class MarathonFileImportHandlerBean extends IBOServiceBean  implements MarathonFileImportHandler{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -5952074105644545210L;
	ImportFile file;
	Collection countries;
	RunBusiness business;
	GenderBusiness genderBusiness;
	CharityBusiness charityBusiness;
	UserBusiness userBusiness;
	Locale englishLocale;
	Locale icelandicLocale;
	Collection runs;
	
	public static void main(String[] args) throws RemoteException {
		/*File fFile = new File("/Users/gimmi/Desktop/Marathonhlauparar.csv");
		ImportFile ifile = new ColumnSeparatedImportFile(fFile);
		
		MarathonFileImportHandlerBean m = new MarathonFileImportHandlerBean();
		m.setImportFile(ifile);
		m.handleRecords();*/
	}
	
	public boolean handleRecords() throws RemoteException {
		this.business = getBusiness(getIWApplicationContext());
		this.genderBusiness = getGenderBusiness(getIWApplicationContext());
		this.charityBusiness = getCharityBusiness(getIWApplicationContext());
		this.userBusiness = getUserBusiness(getIWApplicationContext());
		this.countries = this.business.getCountries();
		this.runs = this.business.getRuns();
		this.englishLocale = LocaleUtil.getLocale(LocaleSwitcher.englishParameterString);
		this.icelandicLocale = LocaleUtil.getLocale(LocaleSwitcher.icelandicParameterString);
		
		Vector errors = new Vector();
		if (this.file != null) {
			String line = (String) this.file.getNextRecord();
			int counter = 0;
			while (line != null && !"".equals(line)) {
				++counter;
				if (counter %100 == 0) {
					System.out.println("MarathonImportCounter = "+counter);
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
	
	private boolean handleLine(String line) {
		ArrayList values = this.file.getValuesFromRecordString(line);
		int size = values.size();
		boolean validLine = true;
		
		String participantNumber = (String) values.get(0);
		String userID = (String) values.get(1);
		String gender = (String) values.get(2);
		String dateOfBirth = (String) values.get(3);
		String name = (String) values.get(4);
		String personalID = ((String) values.get(5)).trim();
		String nationality = (String) values.get(6);
		String chipTime = (String) values.get(7);
		String runTime = (String) values.get(8);
		String run = (String) values.get(9);
		String distance = (String) values.get(10);
		String year = (String) values.get(11);
		String chipNumber = ((String) values.get(12)).trim();
		//String result = (String) values.get(13);
		//String resultInGroup = (String) values.get(14);
		String runGroupName = (String) values.get(15);
		String charity = (String) values.get(16);
		//String runGroupTime = size >= 18 ? (String) values.get(17) : null;
		String split1 = size >= 19 ? (String) values.get(18) : null;
		String split2 = size >= 20 ? (String) values.get(19) : null;
		
		int number = -1;
		try {
			number = Integer.parseInt(participantNumber);
		} catch (NumberFormatException n) {
			System.out.println("not using line... starts with : " +participantNumber);
			validLine = false;
		}
		
		if (validLine) {
			
			Iterator iter = this.countries.iterator();
			Country country = null;
			boolean found = false;
			while (iter.hasNext() && !found) {
				country = (Country) iter.next();
				if (nationality.equals(country.getIsoAbbreviation())) {
					break;
				}
			}
			
			IWTimestamp birth = new IWTimestamp(dateOfBirth);

			try {
				Group runGroup = null;
				Iterator iterator = this.runs.iterator();
				while (iterator.hasNext()) {
					Group element = (Group) iterator.next();
					if (element.getName().equals(run)) {
						runGroup = element;
						break;
					}
				}
				
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
				Collection distances = this.business.getDistancesMap(runGroup, year);
				iterator = distances.iterator();
				while (iterator.hasNext()) {
					Group element = (Group) iterator.next();
					if (element.getName().equals(distance)) {
						distanceGroup = element;
						break;
					}
				}
			
				User user = null;
				if (!userID.trim().equals("")) {
					try {
						user = this.userBusiness.getUser(Integer.valueOf(userID));
					} catch (Exception e) {
						System.out.println("User not found by pk, trying to find by other criteria");
					}
				}
				if (user == null) {
					try {
						if (personalID.length() > 0) {
							user = this.business.getUserBiz().getUser(personalID);
						}
						else {
							user = this.business.getUserBiz().getUserHome().findByDateOfBirthAndName(birth.getDate(), name);
						}
					}
					catch (FinderException fe) {
						System.out.println("User not found, creating...");
					}
				}
				if (user == null) {
					try {
						Gender theGender = this.genderBusiness.getGender(new Integer(gender));
						Name fullName = new Name(name);
						user = this.business.getUserBiz().createUser(fullName.getFirstName(), fullName.getMiddleName(), fullName.getLastName(), fullName.getName(), personalID, theGender, birth);
					}
					catch (FinderException fe) {
						System.err.println("Gender not found");
						return false;
					}
					catch (CreateException ce) {
						ce.printStackTrace();
						return false;
					}
				}
				
				Participant participant = null;
				try {
					participant = this.business.getParticipantByRunAndYear(user, runGroup, yearGroup);
				}
				catch (FinderException fe) {
					try {
						participant = this.business.importParticipant(user, runGroup, yearGroup, distanceGroup);
					}
					catch (CreateException ce) {
						ce.printStackTrace();
						return false;
					}
				}
				participant.setChipNumber(chipNumber);
				participant.setUserNationality(nationality);
				if (runGroupName != null && runGroupName.trim().length() > 0) {
					participant.setRunGroupName(runGroupName);
				}
				try {
					if (charity != null && !charity.trim().equals("")) {
						Charity charityOgranization = this.charityBusiness.getCharityHome().findByPrimaryKey(Integer.valueOf(charity.trim()));
						if (charityOgranization!= null) {
							participant.setCharityId(charityOgranization.getOrganizationalID());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.business.updateRunForParticipant(participant, number, runTime, chipTime, split1, split2);
				return true;
			}
			catch (RemoteException e1) {
				e1.printStackTrace();
				return false;
			}
		}
		return false;
	}
	

	public void setImportFile(ImportFile file) throws RemoteException {
		this.file = file;
	}

	public void setRootGroup(Group rootGroup) throws RemoteException {
	}

	public List getFailedRecords() throws RemoteException {
		return null;
	}
	
	protected RunBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (RunBusiness) IBOLookup.getServiceInstance(iwac, RunBusiness.class);
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

	protected CharityBusiness getCharityBusiness(IWApplicationContext iwac) {
		try {
			return (CharityBusiness) IBOLookup.getServiceInstance(iwac, CharityBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected UserBusiness getUserBusiness(IWApplicationContext iwac) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}