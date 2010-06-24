package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.business.IBOHome;

public interface RunBusinessHome extends IBOHome {
	public RunBusiness create() throws CreateException, RemoteException;
}