package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class CharityBusinessHomeImpl extends IBOHomeImpl implements CharityBusinessHome {
	public Class getBeanInterfaceClass() {
		return CharityBusiness.class;
	}

	public CharityBusiness create() throws CreateException {
		return (CharityBusiness) super.createIBO();
	}
}