/*
 * $Id: IWBundleStarter.java,v 1.3 2007/03/21 13:27:24 sigtryggur Exp $
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


/**
 * Last modified: $Date: 2007/03/21 13:27:24 $ by $Author: sigtryggur $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class IWBundleStarter implements IWBundleStartable {
	
	/* (non-Javadoc)
	 * @see com.idega.idegaweb.IWBundleStartable#start(com.idega.idegaweb.IWBundle)
	 */
	public void start(IWBundle starterBundle) {
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
}
