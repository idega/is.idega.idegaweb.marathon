package is.idega.idegaweb.marathon.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author gimmi
 */
public class MarathonFileImportHandlerHomeImpl extends IBOHomeImpl implements MarathonFileImportHandlerHome {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -7824000910464645878L;

	protected Class getBeanInterfaceClass() {
		return MarathonFileImportHandler.class;
	}

	public MarathonFileImportHandler create() throws javax.ejb.CreateException {
		return (MarathonFileImportHandler) super.createIBO();
	}
}
