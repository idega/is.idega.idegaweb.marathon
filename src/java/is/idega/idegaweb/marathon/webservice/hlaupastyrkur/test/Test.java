package is.idega.idegaweb.marathon.webservice.hlaupastyrkur.test;

import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ContestantRequest;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.ContestantServiceLocator;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.IContestantService;
import is.idega.idegaweb.marathon.webservice.hlaupastyrkur.client.Login;

import java.net.URL;

public class Test {
	public static void main(String[] args) {
		String userID = args[0];
		String passwd = args[1];

		try {
			ContestantServiceLocator locator = new ContestantServiceLocator();
			IContestantService port = locator
					.getBasicHttpBinding_IContestantService(new URL(
							"http://www.hlaupastyrkur.is/services/contestantservice.svc"));
			
			ContestantRequest request = new ContestantRequest("21_km", new Login(passwd, userID), "6906881589", "Palli Test", "pallitest", "pallitest", "0610703899", Boolean.FALSE);
			port.registerContestant(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}