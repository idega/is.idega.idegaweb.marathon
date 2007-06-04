package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface PledgeBusinessHome extends IBOHome {
	public PledgeBusiness create() throws CreateException, RemoteException;
}