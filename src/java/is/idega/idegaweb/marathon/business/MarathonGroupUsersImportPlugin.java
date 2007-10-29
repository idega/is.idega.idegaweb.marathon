package is.idega.idegaweb.marathon.business;

import java.util.HashMap;
import java.util.Map;

import com.idega.block.importer.data.GenericImportFile;
import com.idega.block.importer.presentation.Importer;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.user.app.ToolbarElement;


/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company: idega Software</p>
 * @author <a href="sigtryggur@idega.is">Sigtryggur Simonarson</a>
 * @version 1.0
 * Created on Oct 26, 2007
 */
public class MarathonGroupUsersImportPlugin implements ToolbarElement {
	
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.marathon";

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getButtonImage(com.idega.presentation.IWContext)
	 */
	public Image getButtonImage(IWContext iwc) {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getName(com.idega.presentation.IWContext)
	 */
	public String getName(IWContext iwc) {
		IWBundle bundle = iwc.getApplicationContext().getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER);
		IWResourceBundle resourceBundle = bundle.getResourceBundle(iwc);
		return resourceBundle.getLocalizedString("button.marathon_runner_import", "Runner import");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getPresentationObjectClass(com.idega.presentation.IWContext)
	 */
	public Class getPresentationObjectClass(IWContext iwc) {
		return Importer.class;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getParameterMap(com.idega.presentation.IWContext)
	 */
	public Map getParameterMap(IWContext iwc) {
		Map map = new HashMap();
		map.put(Importer.PARAMETER_IMPORT_FILE,  GenericImportFile.class.getName());
		map.put(Importer.PARAMETER_IMPORT_HANDLER, MarathonGroupUsersImportBean.class.getName());
		return map;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#isValid(com.idega.presentation.IWContext)
	 */
	public boolean isValid(IWContext iwc) {
		if (iwc.isSuperAdmin()) {
        	return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#getPriority(com.idega.presentation.IWContext)
	 */
	public int getPriority(IWContext iwc) {
		return 8;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.app.ToolbarElement#isButton(com.idega.presentation.IWContext)
	 */
	public boolean isButton(IWContext iwc) {
		return false;
	}
}
