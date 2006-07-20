/*
 * $Id: RunBusinessHomeImpl.java,v 1.12 2006/07/20 16:21:01 laddi Exp $
 * Created on Aug 16, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.business;

import com.idega.business.IBOHomeImpl;


/**
 * Last modified: $Date: 2006/07/20 16:21:01 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.12 $
 */
public class RunBusinessHomeImpl extends IBOHomeImpl implements RunBusinessHome {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -8911811424955846057L;

	protected Class getBeanInterfaceClass() {
		return RunBusiness.class;
	}

	public RunBusiness create() throws javax.ejb.CreateException {
		return (RunBusiness) super.createIBO();
	}
}
