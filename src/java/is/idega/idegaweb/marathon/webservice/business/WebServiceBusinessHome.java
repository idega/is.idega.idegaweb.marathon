package is.idega.idegaweb.marathon.webservice.business;


import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.business.IBOHome;

public interface WebServiceBusinessHome extends IBOHome {
	public WebServiceBusiness create() throws CreateException, RemoteException;
}