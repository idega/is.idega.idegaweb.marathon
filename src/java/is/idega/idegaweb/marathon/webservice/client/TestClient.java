package is.idega.idegaweb.marathon.webservice.client;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestClient client = new TestClient();
		client.doStuff();
	}

	private void doStuff() {
		try {
			//String endpoint = "http://vala.reykjavik.is/WSLeikskoli/WSLeikskoliSoapHttpPort";

			CharityServiceServiceLocator locator = new CharityServiceServiceLocator();
			CharityService_PortType port = locator.getCharityService();
			
			CharityInformation info = port.getCharityInformation("1708724369");

			if (info != null) {
				System.out.println(info.getAddress());
				System.out.println(info.getCharityID());
				System.out.println(info.getCharityName());
				System.out.println(info.getCity());
				System.out.println(info.getCountry());
				System.out.println(info.getDistance());
				System.out.println(info.getEmail());
				System.out.println(info.getGender());
				System.out.println(info.getMobile());
				System.out.println(info.getName());
				System.out.println(info.getNationality());
				System.out.println(info.getPersonalID());
				System.out.println(info.getPhone());
				System.out.println(info.getPostalCode());
			} else {
				System.out.println("No info for user");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
