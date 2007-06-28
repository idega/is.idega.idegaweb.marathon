package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Pledge;
import is.idega.idegaweb.marathon.data.PledgeHome;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.transaction.UserTransaction;

import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOLookup;
import com.idega.util.IWTimestamp;

public class PledgeBusinessBean extends IBOServiceBean implements PledgeBusiness {
	
/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -5126037977109694680L;
private PledgeHome PledgeHome;
	
	public Collection getCharities() throws EJBException {
		try {
			return getPledgeHome().findAllPledges();
		}
		catch (FinderException fe) {
			return null;
		}
	}

	public Collection saveParticipants(Collection pledgeHolders) throws IDOCreateException {
		Pledge pledge = null;
		UserTransaction trans = getSessionContext().getUserTransaction();
		try {
			trans.begin();
			try {
				Iterator runIT = pledgeHolders.iterator();
				PledgeHolder pledgeHolder = null;
				if (runIT.hasNext()) {
					pledgeHolder = (PledgeHolder)runIT.next();
				}
				if (pledgeHolder != null) {
					PledgeHome pledgeHome = (PledgeHome) getIDOHome(Pledge.class);
					pledge = pledgeHome.create();
					pledge.setParticipant(pledgeHolder.getParticipant());
					pledge.setOrganizationalID(pledgeHolder.getParticipant().getCharityId());
					pledge.setCardholderName(pledgeHolder.getCardHolderName());
					pledge.setAmountPayed(String.valueOf(pledgeHolder.getPledgeAmount()));
					pledge.setPaymentTimestamp(IWTimestamp.getTimestampRightNow().toString());
					pledge.store();
				}
			}
			catch (CreateException ce) {
				ce.printStackTrace();
			}
			catch (RemoteException re) {
				throw new IBORuntimeException(re);
			}
			trans.commit();
		}
		catch (Exception ex) {
			try {
				trans.rollback();
			}
			catch (javax.transaction.SystemException e) {
				throw new IDOCreateException(e.getMessage());
			}
			ex.printStackTrace();
			throw new IDOCreateException(ex);
		}
		Collection pledges = new ArrayList();
		pledges.add(pledge);
		return pledges;
	}

	public PledgeHome getPledgeHome() {
		if (this.PledgeHome == null) {
			try {
				this.PledgeHome = (PledgeHome) IDOLookup.getHome(Pledge.class);
			}
			catch (RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return this.PledgeHome;
	}
}