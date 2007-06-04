package is.idega.idegaweb.marathon.webservice.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public class WSTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		String testUrl = "http://localhost:8090/services/RegistrationService";
		MarathonRegistrationServiceServiceLocator locator = new MarathonRegistrationServiceServiceLocator();
		
		String runId = "4";
		String year="2007";
		
		try {
			
			MarathonRegistrationService service = locator.getRegistrationService(new URL(testUrl));
			Registrations regs = service.getRegistrations(runId, year);
			
			Registration[] registrations = regs.getRegistrations();
			for (int i = 0; i < registrations.length; i++) {
				Registration reg = registrations[i];
				System.out.println("Found registration for personalId="+reg.getPersonalId()+",distance="+reg.getDistance());
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
