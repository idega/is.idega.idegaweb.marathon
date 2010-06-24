package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHome;
import java.rmi.RemoteException;

public interface CharityBusinessHome extends IBOHome {
	public CharityBusiness create() throws CreateException, RemoteException;
}