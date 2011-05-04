package is.idega.idegaweb.marathon.webservice.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class WebServiceBusinessHomeImpl extends IBOHomeImpl implements
		WebServiceBusinessHome {
	public Class getBeanInterfaceClass() {
		return WebServiceBusiness.class;
	}

	public WebServiceBusiness create() throws CreateException {
		return (WebServiceBusiness) super.createIBO();
	}
}