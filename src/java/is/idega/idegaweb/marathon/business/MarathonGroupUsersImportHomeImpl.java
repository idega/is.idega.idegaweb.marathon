package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class MarathonGroupUsersImportHomeImpl extends IBOHomeImpl implements MarathonGroupUsersImportHome {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -7133326740062359600L;

	public Class getBeanInterfaceClass() {
		return MarathonGroupUsersImport.class;
	}

	public MarathonGroupUsersImport create() throws CreateException {
		return (MarathonGroupUsersImport) super.createIBO();
	}
}