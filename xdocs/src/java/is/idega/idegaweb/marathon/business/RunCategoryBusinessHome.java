package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface RunCategoryBusinessHome extends IBOHome {
	public RunCategoryBusiness create() throws CreateException, RemoteException;
}