package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class PledgeBusinessHomeImpl extends IBOHomeImpl implements PledgeBusinessHome {
	public Class getBeanInterfaceClass() {
		return PledgeBusiness.class;
	}

	public PledgeBusiness create() throws CreateException {
		return (PledgeBusiness) super.createIBO();
	}
}