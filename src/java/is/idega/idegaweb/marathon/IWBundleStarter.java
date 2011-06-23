/*
 * $Id: IWBundleStarter.java,v 1.6 2007/12/21 15:04:09 civilis Exp $
 * Created on May 23, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon;

import is.idega.idegaweb.marathon.presentation.StepsBlock;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.include.GlobalIncludeManager;


/**
 * Last modified: $Date: 2007/12/21 15:04:09 $ by $Author: civilis $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.6 $
 */
public class IWBundleStarter implements IWBundleStartable {
	
	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.marathon";
	
	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#start(com.idega.idegaweb.IWBundle)
	 */
	public void start(IWBundle starterBundle) {
		updateData();
		addStandardViews(starterBundle.getApplication());
		/*IWApplicationContext iwac = IWMainApplication.getDefaultIWApplicationContext();
		String merchantPK = null;
		ICApplicationBindingBusiness abb = null;
		try {
			abb= (ICApplicationBindingBusiness) IBOLookup.getServiceInstance(iwac, ICApplicationBindingBusiness.class);
			merchantPK = abb.get(IWMarathonConstants.PROPERTY_MERCHANT_PK);
		
			if (merchantPK == null) {
				merchantPK = starterBundle.getProperty(IWMarathonConstants.PROPERTY_MERCHANT_PK);
			}
			
			try {
				TPosMerchant merchant = null;
				if (merchantPK == null) {
					merchant = ((TPosMerchantHome) IDOLookup.getHome(TPosMerchant.class)).create();
				}
				else {
					merchant = ((TPosMerchantHome) IDOLookup.getHome(TPosMerchant.class)).findByPrimaryKey(new Integer(merchantPK));
				}
				
				merchant.setName("Marathon.is");
				merchant.store();
				starterBundle.setProperty(IWMarathonConstants.PROPERTY_MERCHANT_PK, merchant.getPrimaryKey().toString());

				abb.put(IWMarathonConstants.PROPERTY_MERCHANT_PK, merchant.getPrimaryKey().toString());
			}
			catch (IDOLookupException ile) {
				ile.printStackTrace();
			}
			catch (FinderException fe) {
				fe.printStackTrace();
			}
			catch (CreateException ce) {
				ce.printStackTrace();
			}	
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

	}

	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#stop(com.idega.idegaweb.IWBundle)
	 */
	public void stop(IWBundle starterBundle) {
	}

	protected void addStandardViews(IWMainApplication iwma){
		MarathonViewManager manager = MarathonViewManager.getInstance(iwma);
		manager.getMarathonViewNode();
	}

	private void updateData() {
		GlobalIncludeManager includeManager = GlobalIncludeManager.getInstance();
		includeManager.addBundleStyleSheet(StepsBlock.IW_BUNDLE_IDENTIFIER, "/style/marathon.css");
	}
}
