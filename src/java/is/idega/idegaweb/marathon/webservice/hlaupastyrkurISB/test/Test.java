package is.idega.idegaweb.marathon.webservice.hlaupastyrkurISB.test;

import is.idega.idegaweb.marathon.webservice.hlaupastyrkurISB.client.ISBServiceServiceLocator;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkurISB.client.ISBService_PortType;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkurISB.client.Login;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkurISB.client.RunnerInfo;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkurISB.client.Session;

import java.net.URL;

public class Test {
	public static void main(String[] args) {
		String loginName = args[0];
		String password = args[1];
		try {
			System.out.println("loginName = " + loginName);
			System.out.println("password = " + password);
			
			ISBServiceServiceLocator locator = new ISBServiceServiceLocator();
			ISBService_PortType port = locator.getISBService();//new URL("http://localhost:8080/services/ISBService"));
			
			Login login = new Login(loginName, password);
			
			Session session = port.authenticateUser(login);
			
			if (session == null) {
				System.out.println("session is null");
			} else {
				System.out.println("session = " + session.getSessionID());
				
				RunnerInfo in1 = new RunnerInfo("21km", "palli@idega.is", null, "1234557", null, "0610703899", "1234567", "palli", session, "M");
				boolean reg = port.registerRunner(in1 );
				
				System.out.println("registered = "+ reg);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}