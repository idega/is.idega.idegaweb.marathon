/*
 * Created on Aug 17, 2004
 * 
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Participant;
import is.idega.idegaweb.marathon.presentation.CreateYearWindowPlugin;
import is.idega.idegaweb.marathon.presentation.RunDistanceTab;
import is.idega.idegaweb.marathon.presentation.RunYearTab;
import is.idega.idegaweb.marathon.presentation.UserRunTab;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBOServiceBean;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author birna
 * 
 */
public class RunPluginBusinessBean extends IBOServiceBean implements RunPluginBusiness, UserGroupPlugInBusiness {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3171562807916616945L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterGroupCreate(com.idega.user.data.Group)
	 */
	public void afterGroupCreateOrUpdate(Group group, Group parentGroup) throws CreateException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#afterUserCreate(com.idega.user.data.User)
	 */
	public void afterUserCreateOrUpdate(User user, Group parentGroup) throws CreateException, RemoteException {
		IWContext iwc = IWContext.getInstance();
		RunBusiness runBiz = getRunBiz(iwc);
		
		Group run = null;
		Group year = null;
		Group distanceGroup = null;
		Distance distance = null;
		
		if (parentGroup != null) {
			try {
				run = runBiz.getRunGroupOfTypeForGroup(parentGroup, IWMarathonConstants.GROUP_TYPE_RUN);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}

			try {
				year = runBiz.getRunGroupOfTypeForGroup(parentGroup, IWMarathonConstants.GROUP_TYPE_RUN_YEAR);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			
			try {
				distanceGroup = runBiz.getRunGroupOfTypeForGroup(parentGroup, IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
				distance = ConverterUtility.getInstance().convertGroupToDistance(distanceGroup);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			}
		}

		try {
			if (run != null) {
				Participant runEntry = getRunBiz(iwc).getParticipantByRunAndYear(user, run, year);
				runEntry.setRunGroupGroup(parentGroup);
				Distance oldDistance = runEntry.getRunDistanceGroup();
				runEntry.setRunDistanceGroup(distance);
				if (distance != null && oldDistance != null && ((Integer)distance.getPrimaryKey()).intValue() != ((Integer)oldDistance.getPrimaryKey()).intValue()) {
					runEntry.setParticipantNumber(runBiz.getNextAvailableParticipantNumber(run, distance));
				}
				runEntry.store();
			}
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
	}

	private RunBusiness getRunBiz(IWContext iwc) {
		RunBusiness business = null;
		try {
			business = (RunBusiness) IBOLookup.getServiceInstance(iwc, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			business = null;
		}
		return business;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeGroupRemove(com.idega.user.data.Group)
	 */
	public void beforeGroupRemove(Group group, Group parentGroup) throws RemoveException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#beforeUserRemove(com.idega.user.data.User)
	 */
	public void beforeUserRemove(User user, Group parentGroup) throws RemoveException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupPropertiesTabs(com.idega.user.data.Group)
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		if (IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE.equals(group.getGroupType())) {
			List list = new ArrayList();
			list.add(new RunDistanceTab());
			return list;
		}
		if (IWMarathonConstants.GROUP_TYPE_RUN_YEAR.equals(group.getGroupType())) {
			List list = new ArrayList();
			list.add(new RunYearTab());
			return list;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getUserPropertiesTabs(com.idega.user.data.User)
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException {
		List list = new ArrayList();
		list.add(new UserRunTab());
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateEditor(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateEditor(Group group) throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#instanciateViewer(com.idega.user.data.Group)
	 */
	public PresentationObject instanciateViewer(Group group) throws RemoteException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserAssignableFromGroupToGroup(com.idega.user.data.User,
	 *      com.idega.user.data.Group, com.idega.user.data.Group)
	 */
	public String isUserAssignableFromGroupToGroup(User user, Group sourceGroup, Group targetGroup) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#isUserSuitedForGroup(com.idega.user.data.User,
	 *      com.idega.user.data.Group)
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	public List getMainToolbarElements() throws RemoteException {
		List list = new ArrayList();
		list.add(new MarathonGroupUsersImportPlugin());
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#getGroupToolbarElements(com.idega.user.data.Group)
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException {
		if (IWMarathonConstants.GROUP_TYPE_RUN.equals(group.getGroupType())) {
			List list = new ArrayList(1);
			list.add(new CreateYearWindowPlugin());
			return list;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.user.business.UserGroupPlugInBusiness#canCreateSubGroup(com.idega.user.data.Group,java.lang.String)
	 */
	public String canCreateSubGroup(Group group, String groupTypeOfSubGroup) throws RemoteException {
		return null;
	}
}