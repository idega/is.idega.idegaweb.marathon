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

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.contact.data.PhoneBMPBean;
import com.idega.core.location.data.Country;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.business.GenderBusiness;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.LocaleUtil;
import com.idega.util.text.TextSoap;

/**
 * @author laddi
 */
public class MarathonGroupUsersImportBean extends IBOServiceBean implements
		MarathonGroupUsersImport, ImportFileHandler {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -7831000010990786731L;
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.marathon";

	ImportFile file;
	UserBusiness userBusiness;
	GroupBusiness groupBusiness;
	GenderBusiness genderBusiness;
	RunBusiness runBusiness;
	Group group;
	List failedRecords;

	String run = null;
	String year = null;
	boolean yearNext = false;
	boolean icelandic = true;

	private static final String REYKJAVIK_MARATHON = "Reykjavik Marathon";
	private static final String LAZY_TOWN_RUN = "Lazy Town Run";
	private static final String MIDNIGHT_RUN = "Midnight Run";
	private static final String LAUGAVEGUR_ULTRA_MARATHON = "Laugavegur";

	public boolean handleRecords() throws RemoteException {
		this.userBusiness = getUserBusiness(getIWApplicationContext());
		this.groupBusiness = getGroupBusiness(getIWApplicationContext());
		this.genderBusiness = getGenderBusiness(getIWApplicationContext());
		this.runBusiness = getRunBusiness(getIWApplicationContext());
		this.failedRecords = new ArrayList();

		if (this.file != null) {
			String line = (String) this.file.getNextRecord();
			/*
			 * String nameTitle = line.substring(0,4); if (nameTitle == null ||
			 * !(nameTitle.equals("Name") || nameTitle.equals("Nafn"))) { throw
			 * new RuntimeException("Not a valid import file"); }
			 */
			int counter = 0;
			while (line != null && !"".equals(line)) {
				++counter;
				if (counter % 10 == 0) {
					System.out.println("MarathonGroupUsersImportCounter = "
							+ counter);
				}
				if (!handleLine(line)) {
					failedRecords.add(line);
				}
				line = (String) this.file.getNextRecord();
			}
			System.out.println("Total numbers of runners imported: " + counter);
		}

		if (!failedRecords.isEmpty()) {
			System.out.println("Errors in the following lines :");
			Iterator iter = failedRecords.iterator();
			while (iter.hasNext()) {
				System.out.println((String) iter.next());
			}
		}
		return true;
	}

	private boolean handleLine(String line) throws RemoteException {
		IWBundle iwb = this.getIWApplicationContext().getIWMainApplication()
				.getBundle(IW_BUNDLE_IDENTIFIER);

		ArrayList values = this.file.getValuesFromRecordString(line);
		int size = values.size();

		String name = "";
		if (size > 0) {
			name = ((String) values.get(0)).trim();
		}
		String personalID = "";
		if (size > 1) {
			personalID = ((String) values.get(1)).trim();
		}
		String gender = "";
		if (size > 2) {
			gender = ((String) values.get(2)).trim();
		}
		String distance = "";
		if (size > 3) {
			distance = ((String) values.get(3)).trim();
		}
		String email = "";
		if (size > 4) {
			email = ((String) values.get(4)).trim();
		}
		String address = "";
		if (size > 5) {
			address = ((String) values.get(5)).trim();
		}
		String phone = "";
		if (size > 6) {
			phone = ((String) values.get(6)).trim();
		}
		String mobile = "";
		if (size > 7) {
			mobile = ((String) values.get(7)).trim();
		}
		String city = "";
		if (size > 8) {
			city = ((String) values.get(8)).trim();
		}
		String postalCode = "";
		if (size > 9) {
			postalCode = ((String) values.get(9)).trim();
		}
		String countryName = "";
		if (size > 10) {
			countryName = ((String) values.get(10)).trim();
		}
		String nationality = "";
		if (size > 11) {
			nationality = ((String) values.get(11)).trim();
		}
		String shirtSize = "";
		if (size > 12) {
			shirtSize = ((String) values.get(12)).trim();
		}
		String paymentGroup = "";
		if (size > 13) {
			paymentGroup = ((String) values.get(13)).trim();
		}
		String sendEmail = "";
		if (size > 14) {
			sendEmail = ((String) values.get(14)).trim();
		}

		String partner1_ssn = "";
		String partner1_name = "";
		String partner1_email = "";
		String partner1_shirtSize = "";
		String partner1_leg = "";
		if (size > 15) {
			partner1_ssn = ((String) values.get(15)).trim();
		}
		if (size > 16) {
			partner1_name = ((String) values.get(16)).trim();
		}
		if (size > 17) {
			partner1_email = ((String) values.get(17)).trim();
		}
		if (size > 18) {
			partner1_shirtSize = ((String) values.get(18)).trim();
		}
		if (size > 19) {
			partner1_leg = ((String) values.get(19)).trim();
		}

		String partner2_ssn = "";
		String partner2_name = "";
		String partner2_email = "";
		String partner2_shirtSize = "";
		String partner2_leg = "";
		if (size > 20) {
			partner2_ssn = ((String) values.get(20)).trim();
		}
		if (size > 21) {
			partner2_name = ((String) values.get(21)).trim();
		}
		if (size > 22) {
			partner2_email = ((String) values.get(22)).trim();
		}
		if (size > 23) {
			partner2_shirtSize = ((String) values.get(23)).trim();
		}
		if (size > 24) {
			partner2_leg = ((String) values.get(24)).trim();
		}

		String partner3_ssn = "";
		String partner3_name = "";
		String partner3_email = "";
		String partner3_shirtSize = "";
		String partner3_leg = "";
		if (size > 25) {
			partner3_ssn = ((String) values.get(25)).trim();
		}
		if (size > 26) {
			partner3_name = ((String) values.get(26)).trim();
		}
		if (size > 27) {
			partner3_email = ((String) values.get(27)).trim();
		}
		if (size > 28) {
			partner3_shirtSize = ((String) values.get(28)).trim();
		}
		if (size > 29) {
			partner3_leg = ((String) values.get(29)).trim();
		}

		
		if (name.equals("Hlaup") || name.equals("Run")) {
			if (name.equals("Hlaup")) {
				this.icelandic = true;
			} else {
				this.icelandic = false;
			}
			run = personalID;
			if (run == null || "".equals(run)) {
				throw new RuntimeException("No run selected");
			}

			if (run.startsWith("Reykjav")) {
				run = REYKJAVIK_MARATHON;
			} else if (run.startsWith("Laugavegur")) {
				run = LAUGAVEGUR_ULTRA_MARATHON;
			} else if (run.startsWith("Latab") || run.startsWith("Lazy")) {
				run = LAZY_TOWN_RUN;
			} else if (run.startsWith("Mi") || run.startsWith("Midnight")) {
				run = MIDNIGHT_RUN;
			} else {
				throw new RuntimeException("No run selected");
			}

			yearNext = true;

			return true;
		}

		IWResourceBundle iwrb = null;
		if (this.icelandic) {
			iwrb = iwb.getResourceBundle(LocaleUtil.getIcelandicLocale());
		} else {
			iwrb = iwb.getResourceBundle(LocaleUtil.getLocale("en"));
		}

		if (yearNext) {
			yearNext = false;
			year = personalID;
			if (year == null || "".equals(year)) {
				throw new RuntimeException("No year entered");
			}

			return true;
		}

		if ((name.equals("Nafn")) || (name.equals("Name"))) {
			// ignoring headers in first line
			return true;
		}
		if (name.equals("") && personalID.equals("") && gender.equals("")) {
			// ignoring lines where no basic userinfo is set
			return true;
		}

		if ("".equals(run) || "".equals(year) || "".equals(name)
				|| "".equals(personalID) || "".equals(gender)
				|| "".equals(distance) || "".equals(email)
				|| "".equals(address)
				|| ("".equals(phone) && "".equals(mobile)) || "".equals(city)
				|| "".equals(postalCode) || "".equals(countryName)
				|| "".equals(nationality) || "".equals(shirtSize)
				|| "".equals(paymentGroup) || "".equals(sendEmail)) {
			// reporting as failed all lines where any value is not set
			return false;
		}

		shirtSize = parseShirtSize(shirtSize);
		partner1_shirtSize = parseShirtSize(partner1_shirtSize);
		partner2_shirtSize = parseShirtSize(partner2_shirtSize);
		partner3_shirtSize = parseShirtSize(partner3_shirtSize);

		try {
			Date dobDate = null;
			IWTimestamp dateOfBirthStamp = null;

			User user = null;
			try {
				if (personalID.indexOf("/") == -1) {
					personalID = TextSoap.findAndReplace(personalID, "-", "");
					personalID = TextSoap.removeWhiteSpace(personalID);
					user = this.userBusiness.getUser(personalID);
				} else {
					SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					dobDate = formatter.parse(personalID);
					dateOfBirthStamp = new IWTimestamp(dobDate);					
				}
			} catch (FinderException fe) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				dobDate = formatter.parse(personalID);
				dateOfBirthStamp = new IWTimestamp(dobDate);
			}

			if (user == null) {
				try {
					if (dobDate != null) {
						user = this.userBusiness.getUserHome()
								.findByDateOfBirthAndName(
										new java.sql.Date(dobDate.getTime()),
										name);
					}
				} catch (FinderException fe) {
					System.out
							.println("User not found by name and date_of_birth");
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
				if (gender != null && (gender.startsWith("m") || gender.startsWith("M"))) {
					theGender = maleGender;
				} else if (gender != null
						&& (gender.startsWith("f") || gender.startsWith("F"))) {
					theGender = femaleGender;
				}
				Country country = userBusiness.getAddressBusiness()
						.getCountryHome().findByCountryName(countryName);
				user = this.runBusiness.saveUser(name, personalID,
						dateOfBirthStamp, theGender, address, postalCode, city,
						country);
			}

			if (user != null) {
				try {
					userBusiness.updateUserMail(user, email);
				} catch (Exception e) {
					System.out.println("Unable to set " + email
							+ " as email for user: " + user);
				}

				try {
					userBusiness.updateUserPhone(user, PhoneBMPBean
							.getHomeNumberID(), phone);
				} catch (Exception e) {
					System.out.println("Unable to set " + phone
							+ " as phone for user: " + user);
				}

				try {
					userBusiness.updateUserMobilePhone(user, mobile);
				} catch (Exception e) {
					System.out.println("Unable to set " + mobile
							+ " as mobile for user: " + user);
				}
			}

			List groupTypes = new ArrayList();
			groupTypes.add(IWMarathonConstants.GROUP_TYPE_RUN);
			Collection runs = this.groupBusiness
					.getGroupsByGroupNameAndGroupTypes(run, groupTypes, true);

			Iterator iterator = runs.iterator();
			Group runGroup = null;
			if (iterator.hasNext()) {
				runGroup = (Group) iterator.next();
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
				Collection distances = this.runBusiness.getDistancesMap(
						runGroup, year);
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
					participant = this.runBusiness.getParticipantByRunAndYear(
							user, runGroup, yearGroup);
				} catch (FinderException fe) {
					try {
						participant = this.runBusiness.importParticipant(user,
								runGroup, yearGroup, distanceGroup);
						newParticipant = true;
					} catch (CreateException ce) {
						ce.printStackTrace();
						return false;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				}
				
				boolean leg1Marked = false;
				boolean leg2Marked = false;
				boolean leg3Marked = false;
				boolean leg4Marked = false;
				
				if (partner1_ssn != null && !"".equals(partner1_ssn)) {
					if (participant != null) {
						participant.setRelayPartner1SSN(partner1_ssn);
						participant.setRelayPartner1Name(partner1_name);
						participant.setRelayPartner1Email(partner1_email);
						participant.setRelayPartner1ShirtSize(partner1_shirtSize);
						participant.setRelayPartner1Leg(partner1_leg);
						if (partner1_leg != null) {
							if (partner1_leg.indexOf("1") > -1) {
								leg1Marked = true;
							} 
							if (partner1_leg.indexOf("2") > -1) {
								leg2Marked = true;
							} 
							if (partner1_leg.indexOf("3") > -1) {
								leg3Marked = true;
							} 
							if (partner1_leg.indexOf("4") > -1) {
								leg4Marked = true;
							}
						}
						
						if (partner2_ssn != null && !"".equals(partner2_ssn)) {
							participant.setRelayPartner2SSN(partner2_ssn);
							participant.setRelayPartner2Name(partner2_name);
							participant.setRelayPartner2Email(partner2_email);
							participant.setRelayPartner2ShirtSize(partner2_shirtSize);
							participant.setRelayPartner2Leg(partner2_leg);

							if (partner2_leg != null) {
								if (partner2_leg.indexOf("1") > -1) {
									leg1Marked = true;
								} else if (partner2_leg.indexOf("2") > -1) {
									leg2Marked = true;
								} else if (partner2_leg.indexOf("3") > -1) {
									leg3Marked = true;
								} else if (partner2_leg.indexOf("4") > -1) {
									leg4Marked = true;
								}
							}
							
							if (partner3_ssn != null && !"".equals(partner3_ssn)) {
								participant.setRelayPartner3SSN(partner3_ssn);
								participant.setRelayPartner3Name(partner3_name);
								participant.setRelayPartner3Email(partner3_email);
								participant.setRelayPartner3ShirtSize(partner3_shirtSize);
								participant.setRelayPartner3Leg(partner3_leg);

								if (partner3_leg != null) {
									if (partner3_leg.indexOf("1") > -1) {
										leg1Marked = true;
									} else if (partner3_leg.indexOf("2") > -1) {
										leg2Marked = true;
									} else if (partner3_leg.indexOf("3") > -1) {
										leg3Marked = true;
									} else if (partner3_leg.indexOf("4") > -1) {
										leg4Marked = true;
									}
								}
							}							
						}
						
						boolean oneLegForMe = false;
						StringBuffer myLeg = new StringBuffer("");
						if (!leg1Marked) {
							myLeg.append("1");
						}

						if (!leg2Marked) {
							if (!oneLegForMe) {
								myLeg.append(",");
								oneLegForMe = true;
							}
							myLeg.append("2");
						}

						if (!leg3Marked) {
							if (!oneLegForMe) {
								myLeg.append(",");
								oneLegForMe = true;
							}
							myLeg.append("3");
						}

						if (!leg4Marked) {
							if (!oneLegForMe) {
								myLeg.append(",");
								oneLegForMe = true;
							}
							myLeg.append("4");
						}

						participant.setRelayLeg(myLeg.toString());
					}
				}
				
				Group ageGenderGroup = this.runBusiness.getAgeGroup(user,
						runGroup, distanceGroup);
				Distance distanceObject = ConverterUtility.getInstance()
						.convertGroupToDistance(distanceGroup);

				if (participant.getRunDistanceGroupID() != ((Integer) distanceGroup
						.getPrimaryKey()).intValue()) {
					Group oldAgeGenderGroup = participant.getRunGroupGroup();
					Collection userIDs = new ArrayList();
					userIDs.add(user.getPrimaryKey().toString());
					userBusiness.moveUsers(IWContext.getInstance(), userIDs,
							oldAgeGenderGroup, ((Integer) ageGenderGroup
									.getPrimaryKey()).intValue());
					movedParticipant = true;
				}
				if (newParticipant || movedParticipant) {
					participant.setParticipantNumber(runBusiness
							.getNextAvailableParticipantNumber(
									distanceObject));
				}
				participant.setRunDistanceGroup(distanceGroup);
				participant.setRunGroupGroup(ageGenderGroup);
				participant.setUserNationality(nationality);
				participant.setShirtSize(shirtSize);
				participant.setPaymentGroup(paymentGroup);
				participant.store();

				if ("y".equalsIgnoreCase(sendEmail)) {
					participant.setAllowsEmails(true);
				} else {
					participant.setAllowsEmails(false);
				}

				String userNameString = "";
				String passwordString = "";

				boolean newPassword = false;
				if (newParticipant) {
					newPassword = true;
					if (userBusiness.hasUserLogin(user)) {
						try {
							LoginTable login = LoginDBHandler
									.getUserLogin(user);
							userNameString = login.getUserLogin();
							passwordString = LoginDBHandler
									.getGeneratedPasswordForUser();
							LoginDBHandler
									.changePassword(login, passwordString);
						} catch (Exception e) {
							System.out
									.println("Error re-generating password for user: "
											+ user.getName());
							e.printStackTrace();
						}
					} else {
						try {
							LoginTable login = this.userBusiness
									.generateUserLogin(user);
							userNameString = login.getUserLogin();
							passwordString = login.getUnencryptedUserPassword();
						} catch (Exception e) {
							System.out
									.println("Error creating login for user: "
											+ user.getName());
							e.printStackTrace();
						}
					}
				}
				Run selectedRun = ConverterUtility.getInstance()
						.convertGroupToRun(runGroup);
				String informationPageString = "";
				String runHomePageString = "";
				String greeting = "";
				String receiptInfo = "";

				if (selectedRun != null) {
					runHomePageString = selectedRun.getRunHomePage();
					if (this.icelandic) {
						informationPageString = selectedRun
								.getRunInformationPage();
						greeting = selectedRun.getRunRegistrationReceiptGreeting();
						receiptInfo = selectedRun.getRunRegistrationReceiptInfo();
					} else {
						informationPageString = selectedRun
								.getEnglishRunInformationPage();
						greeting = selectedRun.getRunRegistrationReceiptGreetingEnglish();
						receiptInfo = selectedRun.getRunRegistrationReceiptInfoEnglish();
					}
				}
				
				if (informationPageString == null) {
					informationPageString = " ";
				}
				if (greeting == null) {
					greeting = " ";
				}
				if (receiptInfo == null) {
					receiptInfo = " ";
				}

				Object[] args = { 
						user.getName(), 
						iwrb.getLocalizedString(runGroup.getName(), runGroup
								.getName()) + " " + yearGroup.getName(),
						iwrb.getLocalizedString(distance, distance), 
						iwrb.getLocalizedString("shirt_size." + shirtSize, shirtSize), 
						String.valueOf(participant.getParticipantNumber()), 
						runHomePageString, informationPageString, 
						userNameString, passwordString, 
						greeting, receiptInfo };
								
				String subject = iwrb.getLocalizedString(
						"registration_received_subject_mail_" + selectedRun.getPrimaryKey().toString(),
						"Your registration has been received.");
				String body = MessageFormat.format(iwrb.getLocalizedString(
						"registration_received_body_mail_" + selectedRun.getPrimaryKey().toString(),
						"Your registration has been received."), args);
				if (newPassword) {
					runBusiness.sendMessage(email, subject, body);
				} else if (movedParticipant || newParticipant) {
					body = MessageFormat.format(iwrb.getLocalizedString(
							"registration_received_body_mail_" + selectedRun.getPrimaryKey().toString() + "_2",
							"Your registration has been received."), args);
					runBusiness.sendMessage(email, subject, body);
				}

				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// some exception occured, import-line is considered failed
			e.printStackTrace();
			return false;
		}
	}

	public String parseShirtSize(String shirtSize) {
		// Shirt size handling
		if (shirtSize.startsWith("XS c") || shirtSize.startsWith("XS b")) {
			shirtSize = "xsmall_kids";
		} else if (shirtSize.startsWith("S c") || shirtSize.startsWith("S b")) {
			shirtSize = "small_kids";
		} else if (shirtSize.startsWith("M c") || shirtSize.startsWith("M b")) {
			shirtSize = "medium_kids";
		} else if (shirtSize.startsWith("L c") || shirtSize.startsWith("L b")) {
			shirtSize = "large_kids";
		} else if (shirtSize.startsWith("S f") || shirtSize.startsWith("S kv")) {
			shirtSize = "small_female";
		} else if (shirtSize.startsWith("M f") || shirtSize.startsWith("M kv")) {
			shirtSize = "medium_female";
		} else if (shirtSize.startsWith("L f") || shirtSize.startsWith("L kv")) {
			shirtSize = "large_female";
		} else if (shirtSize.startsWith("XL f")
				|| shirtSize.startsWith("XL kv")) {
			shirtSize = "xlarge_female";
		} else if (shirtSize.startsWith("XXL f")
				|| shirtSize.startsWith("XXL kv")) {
			shirtSize = "xxlarge_female";
		} else if (shirtSize.startsWith("S m") || shirtSize.startsWith("S ka")) {
			shirtSize = "small_male";
		} else if (shirtSize.startsWith("M m") || shirtSize.startsWith("M ka")) {
			shirtSize = "medium_male";
		} else if (shirtSize.startsWith("L m") || shirtSize.startsWith("L ka")) {
			shirtSize = "large_male";
		} else if (shirtSize.startsWith("XL m")
				|| shirtSize.startsWith("XL ka")) {
			shirtSize = "xlarge_male";
		} else if (shirtSize.startsWith("XXL m")
				|| shirtSize.startsWith("XXL ka")) {
			shirtSize = "xxlarge_male";
		} else if (shirtSize.equals("XL")) {
			shirtSize = "xlarge";			
		} else if (shirtSize.equals("L")) {
			shirtSize = "large";			
		} else if (shirtSize.equals("M")) {
			shirtSize = "medium";			
		} else if (shirtSize.equals("S")) {
			shirtSize = "small";			
		} else if (shirtSize.equals("XS")) {
			shirtSize = "xsmall";			
		} else if (shirtSize.equals("XXS")) {
			shirtSize = "xxsmall";			
		} 

		return shirtSize;
	}
	
	public void setImportFile(ImportFile file) throws RemoteException {
		this.file = file;
	}

	public void setRootGroup(Group rootGroup) throws RemoteException {
	}

	public List getFailedRecords() throws RemoteException {
		return failedRecords;
	}

	protected UserBusiness getUserBusiness(IWApplicationContext iwac) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwac,
					UserBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected GroupBusiness getGroupBusiness(IWApplicationContext iwac) {
		try {
			return (GroupBusiness) IBOLookup.getServiceInstance(iwac,
					GroupBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected GenderBusiness getGenderBusiness(IWApplicationContext iwac) {
		try {
			return (GenderBusiness) IBOLookup.getServiceInstance(iwac,
					GenderBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected RunBusiness getRunBusiness(IWApplicationContext iwac) {
		try {
			return (RunBusiness) IBOLookup.getServiceInstance(iwac,
					RunBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}