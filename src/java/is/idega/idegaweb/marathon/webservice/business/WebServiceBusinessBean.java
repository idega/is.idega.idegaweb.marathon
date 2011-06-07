package is.idega.idegaweb.marathon.webservice.business;

import is.idega.idegaweb.marathon.business.CharityBusiness;
import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.business.Runner;
import is.idega.idegaweb.marathon.data.Charity;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.webservice.data.WebServiceLoginSession;
import is.idega.idegaweb.marathon.webservice.data.WebServiceLoginSessionHome;
import is.idega.idegaweb.marathon.webservice.isb.server.RelayPartnerInfo;
import is.idega.idegaweb.marathon.webservice.server.CharityInformation;
import is.idega.idegaweb.marathon.webservice.server.Session;
import is.idega.idegaweb.marathon.webservice.server.SessionTimedOutException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.ICPermission;
import com.idega.core.accesscontrol.data.ICPermissionHome;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.accesscontrol.data.LoginTableHome;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.business.AddressBusiness;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
import com.idega.util.LocaleUtil;

public class WebServiceBusinessBean extends IBOServiceBean implements
		WebServiceBusiness {

	public static final String DISTANCE1 = "3km";
	public static final String DISTANCE2 = "10km";
	public static final String DISTANCE3 = "21km";
	public static final String DISTANCE4 = "42km";
	public static final String DISTANCE5 = "42km_relay";

	public static final String SIZE1 = "XS";
	public static final String SIZE2 = "S";
	public static final String SIZE3 = "M";
	public static final String SIZE4 = "L";
	public static final String SIZE5 = "XL";
	public static final String SIZE6 = "XXL";

	public boolean registerRunner(
			is.idega.idegaweb.marathon.webservice.isb.server.Session session,
			String personalID, String distance, String shirtSize, String email,
			String phone, String mobile, String leg,
			RelayPartnerInfo[] partners, String registeredBy)
			throws is.idega.idegaweb.marathon.webservice.isb.server.SessionTimedOutException {
		is.idega.idegaweb.marathon.webservice.server.Session s = new is.idega.idegaweb.marathon.webservice.server.Session(
				session.getSessionID());
		WebServiceLoginSession loginSession = validateAndUpdateLoginSession(s);
		if (loginSession != null) {
		} else {
			throw new is.idega.idegaweb.marathon.webservice.isb.server.SessionTimedOutException();
		}

		User user = null;
		Group run = null;
		Group year = null;
		List distances = null;

		try {
			run = getRunBusiness().getRunGroupByGroupId(new Integer(429727));//4));
			year = getRunBusiness().getRunGroupByGroupId(new Integer(429728)); //426626)); // TODO fix
			user = getUserBusiness().getUser(personalID);
			distances = getRunBusiness().getDistancesMap(run, "2011");
		} catch (Exception e) {
			return false;
		}

		if (run == null || year == null || user == null) {
			return false;
		}

		try {
			if (getRunBusiness().isRegisteredInRun(year.getName(), run, user)) {
				return false;
			}
		} catch (Exception e) {
		}

		if (distance == null || shirtSize == null) {
			return false;
		}

		if (!DISTANCE1.equals(distance) && !DISTANCE2.equals(distance)
				&& !DISTANCE3.equals(distance) && !DISTANCE4.equals(distance)
				&& !DISTANCE5.equals(distance)) {
			return false;
		}

		if (DISTANCE1.equals(distance)) {
			if (!SIZE2.equals(shirtSize) && !SIZE3.equals(shirtSize)
					&& !SIZE4.equals(shirtSize) && !SIZE5.equals(shirtSize)
					&& !SIZE6.equals(shirtSize)) {
				return false;
			}
		} else {
			if (user.getGender().isFemaleGender()) {
				if (!SIZE1.equals(shirtSize) && !SIZE2.equals(shirtSize)
						&& !SIZE3.equals(shirtSize) && !SIZE4.equals(shirtSize)
						&& !SIZE5.equals(shirtSize)) {
					return false;
				}
			} else {
				if (!SIZE2.equals(shirtSize) && !SIZE3.equals(shirtSize)
						&& !SIZE4.equals(shirtSize) && !SIZE5.equals(shirtSize)
						&& !SIZE6.equals(shirtSize)) {
					return false;
				}
			}
		}

		if (email == null) {
			return false;
		}

		Runner runner = new Runner();
		runner.setUser(user);
		runner.setEmail(email);
		runner.setEmail2(email);
		runner.setHomePhone(phone);
		runner.setMobilePhone(mobile);
		try {
			runner.setRun(ConverterUtility.getInstance().convertGroupToRun(run));
		} catch (FinderException e1) {
			e1.printStackTrace();
		}
		runner.setDistance(getDistance(distances, distance));
		runner.setAgree(true);
		runner.setAllowsEmails(true);
		runner.setAmount(0.0f);
		runner.setGender(user.getGender());
		runner.setName(user.getName());
		runner.setRelayLeg(leg);
		runner.setShirtSize(getShirtSizeForUser(user, shirtSize));
		runner.setPaymentGroup("ISB_2011");
		Country icelandicNationality = null;
		try {
			icelandicNationality = getAddressBusiness().getCountryHome()
					.findByIsoAbbreviation("IS");
			runner.setNationality(icelandicNationality);
		} catch (Exception e) {
		}

		if (DISTANCE5.equals(distance)) {
			boolean leg1 = false;
			boolean leg2 = false;
			boolean leg3 = false;
			boolean leg4 = false;

			if (leg == null || "".equals(leg)) {
				return false;
			} else {

				if (leg.indexOf("1") > -1) {
					leg1 = true;
				}

				if (leg.indexOf("2") > -1) {
					leg2 = true;
				}

				if (leg.indexOf("3") > -1) {
					leg3 = true;
				}

				if (leg.indexOf("4") > -1) {
					leg4 = true;
				}
			}
			
			if (partners != null) {
				if (partners.length > 3) {
					return false;
				}
				
				for (int i = 0; i < partners.length; i++) {
					RelayPartnerInfo relayPartnerInfo = partners[i];
					
					if (relayPartnerInfo.getEmail() == null || "".equals(relayPartnerInfo.getEmail())) {
						return false;
					}

					if (relayPartnerInfo.getLeg() == null || "".equals(relayPartnerInfo.getLeg())) {
						return false;
					}

					if (relayPartnerInfo.getPersonalID() == null || "".equals(relayPartnerInfo.getPersonalID())) {
						return false;
					}

					if (relayPartnerInfo.getShirtSize() == null || "".equals(relayPartnerInfo.getShirtSize())) {
						return false;
					}

					User relUser = null;
					try {
						relUser = getUserBusiness().getUser(relayPartnerInfo.getPersonalID());
					} catch (Exception e) {
						relUser = null;
					}
					
					if (relUser == null) {
						return false;
					}
					

					if (relayPartnerInfo.getLeg().indexOf("1") > -1) {
						if (leg1) {
							return false;
						}

						leg1 = true;
					}

					if (relayPartnerInfo.getLeg().indexOf("2") > -1) {
						if (leg2) {
							return false;
						}

						leg2 = true;
					}

					if (relayPartnerInfo.getLeg().indexOf("3") > -1) {
						if (leg3) {
							return false;
						}

						leg3 = true;
					}

					if (relayPartnerInfo.getLeg().indexOf("4") > -1) {
						if (leg4) {
							return false;
						}

						leg4 = true;
					}

					switch (i) {
					case 1 : 
						runner.setPartner1Email(relayPartnerInfo.getEmail());
						runner.setPartner1Leg(relayPartnerInfo.getLeg());
						runner.setPartner1Name(relUser.getName());
						runner.setPartner1ShirtSize(getShirtSizeForUser(relUser, relayPartnerInfo.getShirtSize()));
						runner.setPartner1SSN(relUser.getPersonalID());
						break;
					case 2 :
						runner.setPartner2Email(relayPartnerInfo.getEmail());
						runner.setPartner2Leg(relayPartnerInfo.getLeg());
						runner.setPartner2Name(relUser.getName());
						runner.setPartner2ShirtSize(getShirtSizeForUser(relUser, relayPartnerInfo.getShirtSize()));
						runner.setPartner2SSN(relUser.getPersonalID());
						break;
					case 3 :
						runner.setPartner3Email(relayPartnerInfo.getEmail());
						runner.setPartner3Leg(relayPartnerInfo.getLeg());
						runner.setPartner3Name(relUser.getName());
						runner.setPartner3ShirtSize(getShirtSizeForUser(relUser, relayPartnerInfo.getShirtSize()));
						runner.setPartner3SSN(relUser.getPersonalID());
						break;
					}
				}
			}
			
			if (!leg1 || !leg2 || !leg3 || !leg4) {
				return false;
			}				
		}

		Collection runners = new ArrayList();
		runners.add(runner);
		
		try {
			getRunBusiness().saveParticipants(runners, email, null, 0.0f, IWTimestamp.RightNow(), LocaleUtil.getIcelandicLocale(), true, "rm_reg.");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	private Distance getDistance(List distances, String distance) {
		String name = null;
		if (DISTANCE1.equals(distance)) {
			name = "3_km";
		} else if (DISTANCE2.equals(distance)) {
			name = "10_km";			
		} else if (DISTANCE3.equals(distance)) {
			name = "21_km";			
		} else if (DISTANCE4.equals(distance)) {
			name = "42_km";			
		} else if (DISTANCE5.equals(distance)) {
			name = "42_km_relay";			
		}
		
		Group d = null;
		Iterator it = distances.iterator();
		while (it.hasNext()) {
			d = (Group) it.next();
			if (name.equals(d.getName())) {
				break;
			}
		}
		
		if (d != null) {
			try {
				return ConverterUtility.getInstance().convertGroupToDistance(d);
			} catch (FinderException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	private String getShirtSizeForUser(User user, String shirtSize) {
		StringBuffer ret = new StringBuffer();
		if (SIZE1.equals(shirtSize)) {
			ret.append("xsmall");
		} else if (SIZE2.equals(shirtSize)) {
			ret.append("small");
		} else if (SIZE3.equals(shirtSize)) {
			ret.append("medium");
		} else if (SIZE4.equals(shirtSize)) {
			ret.append("large");
		} else if (SIZE5.equals(shirtSize)) {
			ret.append("xlarge");
		} else if (SIZE6.equals(shirtSize)) {
			ret.append("xxlarge");
		}
			
		ret.append("_");
		if (user.getGender().isFemaleGender()) {
			ret.append("female");
		} else {
			ret.append("male");
		}
		
		return ret.toString();
	}
	
	public is.idega.idegaweb.marathon.webservice.server.Charity[] getCharities() {
		try {
			Collection charities = getRunBusiness().getCharityBusiness().getCharitiesByRunYearID(new Integer(426626));
			if (charities != null && !charities.isEmpty()) {
				Iterator it = charities.iterator();
				is.idega.idegaweb.marathon.webservice.server.Charity[] ret = new is.idega.idegaweb.marathon.webservice.server.Charity[charities.size()];
				int counter = 0;
				while (it.hasNext()) {
					ret[counter] = new is.idega.idegaweb.marathon.webservice.server.Charity();
					Charity c = (Charity) it.next();
					ret[counter].setId(c.getOrganizationalID());
					ret[counter].setName(c.getName());
					ret[counter++].setDescription(c.getDescription());
				}
				
				return ret;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public is.idega.idegaweb.marathon.webservice.server.Charity getCharity(String charityPersonalID) {
		try {
			Charity charity = getRunBusiness().getCharityBusiness().getCharityByOrganisationalID(charityPersonalID);
			is.idega.idegaweb.marathon.webservice.server.Charity ret = new is.idega.idegaweb.marathon.webservice.server.Charity();
			ret.setDescription(charity.getDescription());
			ret.setId(charity.getOrganizationalID());
			ret.setName(charity.getName());
			
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	public CharityInformation getCharityInformation(String personalID)
			throws java.rmi.RemoteException {
		User user = null;
		Group run = null;
		Group year = null;
		Participant runner = null;

		try {
			run = getRunBusiness().getRunGroupByGroupId(new Integer(4));
			year = getRunBusiness().getRunGroupByGroupId(new Integer(426626)); // TODO
																				// fix
			user = getUserBusiness().getUser(personalID);

			runner = getRunBusiness().getParticipantByRunAndYear(user, run,
					year);

			Charity charity = null;
			if (runner.getCharityId() != null
					&& !runner.getCharityId().equals("")) {
				charity = getCharityBusiness().getCharityByOrganisationalID(
						runner.getCharityId());
			}

			CharityInformation info = new CharityInformation();
			if (charity != null) {
				info.setCharityID(runner.getCharityId());
				info.setCharityName(charity.getName());
			}
			info.setName(user.getName());
			info.setPersonalID(user.getPersonalID());
			Address address = null;
			try {
				address = getUserBusiness().getUsersMainAddress(user);
			} catch (Exception e) {
			}
			Email email = null;
			try {
				email = getUserBusiness().getUserMail(user);
			} catch (Exception e) {
			}
			Phone mobile = null;
			try {
				mobile = getUserBusiness().getUsersMobilePhone(user);
			} catch (Exception e) {
			}
			Phone phone = null;
			try {
				phone = getUserBusiness().getUsersHomePhone(user);
			} catch (Exception e) {
			}

			if (address != null) {
				if (address.getStreetAddress() != null) {
					info.setAddress(address.getStreetAddress());
				}
				if (address.getCity() != null) {
					info.setCity(address.getCity());
				}
				if (address.getCountry() != null) {
					info.setCountry(address.getCountry().getName());
				}
				if (address.getPostalCode() != null) {
					info.setPostalCode(address.getPostalCode().getPostalCode());
				}
			}
			info.setDistance(runner.getRunDistanceGroup().getName());
			info.setGender(user.getGender().getName());
			info.setNationality(runner.getUserNationality());
			if (phone != null && phone.getNumber() != null) {
				info.setPhone(phone.getNumber());
			}
			if (mobile != null && mobile.getNumber() != null) {
				info.setMobile(mobile.getNumber());
			}
			if (email != null && email.getEmailAddress() != null) {
				info.setEmail(email.getEmailAddress());
			}

			return info;

		} catch (FinderException e) {

			if (run == null || year == null) {
				return null;
			}

			boolean partner1 = false;
			boolean partner2 = false;
			boolean partner3 = false;

			try {
				runner = getRunBusiness().getParticipantPartnerByRunAndYear(
						personalID, run, year, 1);
				partner1 = true;

			} catch (FinderException e1) {
				try {
					runner = getRunBusiness()
							.getParticipantPartnerByRunAndYear(personalID, run,
									year, 2);
					partner2 = true;
				} catch (FinderException e2) {
					try {
						runner = getRunBusiness()
								.getParticipantPartnerByRunAndYear(personalID,
										run, year, 3);
						partner3 = true;
					} catch (FinderException e3) {
						return null;
					}
				}
			}

			CharityInformation info = new CharityInformation();
			info.setDistance(runner.getRunDistanceGroup().getName());

			if (partner1) {
				info.setName(runner.getRelayPartner1Name());
				info.setPersonalID(runner.getRelayPartner1SSN());
				info.setEmail(runner.getRelayPartner1Email());
			} else if (partner2) {
				info.setName(runner.getRelayPartner2Name());
				info.setPersonalID(runner.getRelayPartner2SSN());
				info.setEmail(runner.getRelayPartner2Email());
			} else if (partner3) {
				info.setName(runner.getRelayPartner3Name());
				info.setPersonalID(runner.getRelayPartner3SSN());
				info.setEmail(runner.getRelayPartner3Email());
			}

			Charity charity = null;
			if (runner.getCharityId() != null
					&& !runner.getCharityId().equals("")) {
				charity = getCharityBusiness().getCharityByOrganisationalID(
						runner.getCharityId());
			}
			if (charity != null) {
				info.setCharityID(runner.getCharityId());
				info.setCharityName(charity.getName());
			}

			return info;
		}
	}

	public is.idega.idegaweb.marathon.webservice.server.Session authenticateUser(
			String userName, String password) {
		try {

			LoginBusinessBean loginBean = LoginBusinessBean
					.getDefaultLoginBusinessBean();
			LoginTable loginTable = getLoginTableHome().findByLogin(userName);
			if (loginTable != null) {
				// check if user is already verified
				Collection sessions = getWebServiceLoginSessionHome()
						.findAllByUser(loginTable.getUser());
				if (sessions != null && !sessions.isEmpty()) {
					IWTimestamp now = IWTimestamp.RightNow();
					Iterator it = sessions.iterator();
					while (it.hasNext()) {
						WebServiceLoginSession loginSession = (WebServiceLoginSession) it
								.next();
						IWTimestamp lastAccess = new IWTimestamp(
								loginSession.getLastAccess());
						if (IWTimestamp.getMilliSecondsBetween(lastAccess, now) <= getSessionTimeout()) {
							loginSession.setLastAccess(now.getTimestamp());
							loginSession.store();

							return new is.idega.idegaweb.marathon.webservice.server.Session(
									loginSession.getUniqueId());
						} else {
							loginSession.setIsClosed(true);
							loginSession.store();
						}
					}
				}

				// verify password and create new ws session
				if (loginBean.verifyPassword(loginTable, password)) {

					// check if user has the correct role to use the web
					// services
					//IWContext iwc = IWContext.getInstance();
					try {
						if (!hasPermissionForGroupByRole(RoleHelperObject.getStaticInstance().toString(),
								loginTable.getUser().getPrimaryGroup(), loginTable.getUser())) {
							return new is.idega.idegaweb.marathon.webservice.server.Session(
									"-1");
						}
					} catch (Exception e) {
						return new is.idega.idegaweb.marathon.webservice.server.Session(
								"-1");
					}

					WebServiceLoginSession loginSession = getWebServiceLoginSessionHome()
							.create();
					IWTimestamp now = IWTimestamp.RightNow();
					loginSession.setCreated(now.getTimestamp());
					loginSession.setLastAccess(now.getTimestamp());
					loginSession.setIsClosed(false);
					loginSession.setUser(loginTable.getUser());
					loginSession.store();

					return new is.idega.idegaweb.marathon.webservice.server.Session(
							loginSession.getUniqueId());
				}
			}
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (CreateException e) {
			e.printStackTrace();
		}

		return new is.idega.idegaweb.marathon.webservice.server.Session("-1");
	}

	private boolean hasPermissionForGroupByRole(String permissionKey, Group group, User user) throws RemoteException {
	    
	    //get all the roles of the current users parent groups or permission controlling groups
	    //use a find method that searches for active and true ICPermissions with the following
	    //context_value=permissionKey, permission_string=collection of the current users roles, group_id = group.getPrimaryKey()
	    //If something is found then we return true, otherwise false
	    
	    //get the parent groups
	    List permissionControllingGroups = new ArrayList();
	    Collection parents = getGroupBusiness().getParentGroups(user);
	   
	    if(parents!=null && !parents.isEmpty()) {
	        Map roleMap= new HashMap();
	        
	        //get the real permission controlling group if not the parent
	        Iterator iterator = parents.iterator();
	        while (iterator.hasNext()) {
                Group parent = (Group) iterator.next();
                if(parent.getPermissionControllingGroupID()>0) {
                    Group controller = parent.getPermissionControllingGroup();
                    permissionControllingGroups.add(controller);
                }else {
                    permissionControllingGroups.add(parent);
                }
	        }
	        /////
	        
		    //create the role map we need
	        Collection permissions = getAllRolesForGroupCollection(permissionControllingGroups);
	        
	        if(!permissions.isEmpty()) {
		        Iterator iter = permissions.iterator();
		        while (iter.hasNext()) {
	                ICPermission perm = (ICPermission) iter.next();
	                String roleKey = perm.getPermissionString();
	                if(!roleMap.containsKey(roleKey)) {
	                    roleMap.put(roleKey,roleKey);
	                }   
	            }
	        }
	        else {
	            return false;
	        }
	        /////
	        
	        //see if we find role with the correct permission key and group
	        //if so we return true
	        //this could be optimized by doing a count sql instead
	        Collection validPerms;
            try {
                validPerms = getPermissionHome().findAllPermissionsByContextTypeAndContextValueAndPermissionStringCollectionAndPermissionGroup(RoleHelperObject.getStaticInstance().toString(),permissionKey,roleMap.values(),group);
                if(validPerms!=null && !validPerms.isEmpty()) {
    	            return true;    
    	        }
    	        
            } catch (FinderException e) {
                return false;
            }
            
	    }

        //has no roles or does not have the correct role
	    return false;
	}

	private Collection getAllRolesForGroupCollection(Collection groups) {
	    Collection returnCol = new Vector(); //empty
	    if(groups == null || groups.isEmpty()){
	    		return ListUtil.getEmptyList();
	    }
	    try {
	        Collection permissions=
	            getPermissionHome().findAllPermissionsByContextTypeAndPermissionGroupCollectionOrderedByContextValue(
	                    RoleHelperObject.getStaticInstance().toString(),
	                    groups);
	        
	        //only return active and only actual roles and not group permission definitation roles
	        if(permissions!=null && !permissions.isEmpty()){
	            Iterator permissionsIter = permissions.iterator();
	            while (permissionsIter.hasNext()) {
	                ICPermission perm = (ICPermission) permissionsIter.next();
	                //perm.getPermissionString().equals(perm.getContextValue()) is true if it is a marker for an active role for a group
	                //if not it is a role for a permission key
	                if(perm.getPermissionValue() && perm.getContextValue().equals(perm.getContextType())){
	                    returnCol.add(perm);
	                }
	            }
	        }		
	    }
	    catch (FinderException ex) {
	        ex.printStackTrace();
	    }
	    catch (RemoteException x) {
	        x.printStackTrace();
	    }

		return returnCol;	
	}
	
	static class RoleHelperObject {

		private static RoleHelperObject roleObject = null;
		private static final String ROLE_STRING = "role_permission";

		public RoleHelperObject() {
		}
		public static RoleHelperObject getStaticInstance() {

			if (roleObject == null) {
				roleObject = new RoleHelperObject();
			}

			return roleObject;
		}

		public String toString() {
			return ROLE_STRING;
		}
	}


	private ICPermissionHome getPermissionHome() throws RemoteException {
		return (ICPermissionHome) IDOLookup.getHome(ICPermission.class);
	}
	
	private LoginTableHome getLoginTableHome() {
		try {
			return (LoginTableHome) IDOLookup.getHome(LoginTable.class);
		} catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private int getSessionTimeout() {
		String timeout = this.getIWMainApplication().getSettings()
				.getProperty("WEBSERVICE_TIMEOUT", "900000");

		return Integer.parseInt(timeout);
	}

	private WebServiceLoginSession validateAndUpdateLoginSession(
			is.idega.idegaweb.marathon.webservice.server.Session session) {
		if (session == null) {
			return null;
		}

		if (session.getSessionID() == null
				|| "-1".equals(session.getSessionID())) {
			return null;
		}

		try {
			WebServiceLoginSession loginSession = getWebServiceLoginSessionHome()
					.findByUniqueID(session.getSessionID());
			if (loginSession != null && !loginSession.getIsClosed()) {
				IWTimestamp now = IWTimestamp.RightNow();
				IWTimestamp lastAccess = new IWTimestamp(
						loginSession.getLastAccess());
				if (IWTimestamp.getMilliSecondsBetween(lastAccess, now) <= getSessionTimeout()) {
					loginSession.setLastAccess(now.getTimestamp());
					loginSession.store();

					return loginSession;
				} else {
					loginSession.setIsClosed(true);
					loginSession.store();
				}
			}
		} catch (FinderException e) {
		}

		return null;
	}

	private WebServiceLoginSessionHome getWebServiceLoginSessionHome() {
		try {
			return (WebServiceLoginSessionHome) IDOLookup
					.getHome(WebServiceLoginSession.class);
		} catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	private RunBusiness getRunBusiness() throws IBOLookupException {
		return (RunBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				RunBusiness.class);
	}

	private UserBusiness getUserBusiness() throws IBOLookupException {
		return (UserBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				UserBusiness.class);
	}

	private GroupBusiness getGroupBusiness() throws IBOLookupException {
		return (GroupBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				GroupBusiness.class);
	}

	private AddressBusiness getAddressBusiness() throws IBOLookupException {
		return (AddressBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				AddressBusiness.class);
	}

	private CharityBusiness getCharityBusiness() throws IBOLookupException {
		return (CharityBusiness) IBOLookup.getServiceInstance(
				IWMainApplication.getDefaultIWApplicationContext(),
				CharityBusiness.class);
	}

	public boolean updateUserPassword(Session session, String personalID,
			String password) throws RemoteException, SessionTimedOutException {

		WebServiceLoginSession loginSession = validateAndUpdateLoginSession(session);
		if (loginSession != null) {
		} else {
			throw new SessionTimedOutException();
		}

		try {
			User user = getUserBusiness().getUser(personalID);
			if (getUserBusiness().hasUserLogin(user)) {
				LoginTable login = LoginDBHandler.getUserLogin(user);
				LoginDBHandler.changePassword(login, password);
			}
		} catch (Exception e) {
		}

		return false;
	}

	public boolean updateUserCharity(Session session, String personalID,
			String charityPersonalID) throws RemoteException,
			SessionTimedOutException {
		WebServiceLoginSession loginSession = validateAndUpdateLoginSession(session);
		if (loginSession != null) {
		} else {
			throw new SessionTimedOutException();
		}

		try {
			User user = getUserBusiness().getUser(personalID);
			Charity charity = getCharityBusiness()
					.getCharityByOrganisationalID(charityPersonalID);
			Group run = getRunBusiness().getRunGroupByGroupId(new Integer(4));
			Group year = getRunBusiness().getRunGroupByGroupId(
					new Integer(426626)); // TODO
			Participant participant = getRunBusiness()
					.getParticipantByRunAndYear(user, run, year);
			participant.setCharityId(charityPersonalID);
			participant.store();
		} catch (FinderException e) {
		}

		return false;
	}

	public boolean updateUserEmail(Session session, String personalID,
			String email) throws RemoteException, SessionTimedOutException {
		WebServiceLoginSession loginSession = validateAndUpdateLoginSession(session);
		if (loginSession != null) {
		} else {
			throw new SessionTimedOutException();
		}

		try {
			User user = getUserBusiness().getUser(personalID);
			getUserBusiness().updateUserMail(user, email);

			return true;
		} catch (Exception e) {
		}

		return false;
	}
}