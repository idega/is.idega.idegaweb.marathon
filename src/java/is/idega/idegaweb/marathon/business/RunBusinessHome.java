package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface RunBusinessHome extends IBOHome {
	public RunBusiness create() throws CreateException, RemoteException;
}