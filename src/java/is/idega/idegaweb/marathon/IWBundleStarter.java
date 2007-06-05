/*
 * $Id: IWBundleStarter.java,v 1.5 2007/06/05 16:48:11 tryggvil Exp $
 * Created on May 23, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon;

import java.io.IOException;
import java.rmi.RemoteException;

import is.idega.idegaweb.marathon.presentation.StepsBlock;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.creditcard.data.KortathjonustanMerchant;
import com.idega.block.creditcard.data.KortathjonustanMerchantHome;
import com.idega.business.IBOLookup;
import com.idega.core.business.ICApplicationBindingBusiness;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.include.GlobalIncludeManager;


/**
 * Last modified: $Date: 2007/06/05 16:48:11 $ by $Author: tryggvil $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.5 $
 */
public class IWBundleStarter implements IWBundleStartable {
	
	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#start(com.idega.idegaweb.IWBundle)
	 */
	public void start(IWBundle starterBundle) {
		updateData();
		addStandardViews(starterBundle.getApplication());
		IWApplicationContext iwac = IWMainApplication.getDefaultIWApplicationContext();
		String merchantPK = null;
		ICApplicationBindingBusiness abb = null;
		try {
			abb= (ICApplicationBindingBusiness) IBOLookup.getServiceInstance(iwac, ICApplicationBindingBusiness.class);
			merchantPK = abb.get(IWMarathonConstants.PROPERTY_MERCHANT_PK);
		
			if (merchantPK == null) {
				merchantPK = starterBundle.getProperty(IWMarathonConstants.PROPERTY_MERCHANT_PK);
			}
			
			try {
				KortathjonustanMerchant merchant = null;
				if (merchantPK == null) {
					merchant = ((KortathjonustanMerchantHome) IDOLookup.getHome(KortathjonustanMerchant.class)).create();
				}
				else {
					merchant = ((KortathjonustanMerchantHome) IDOLookup.getHome(KortathjonustanMerchant.class)).findByPrimaryKey(new Integer(merchantPK));
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
		}

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
