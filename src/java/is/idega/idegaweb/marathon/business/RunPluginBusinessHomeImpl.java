/*
 * $Id: RunPluginBusinessHomeImpl.java,v 1.3 2006/07/20 16:21:01 laddi Exp $
 * Created on Dec 7, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.business;

import com.idega.business.IBOHomeImpl;


/**
 * 
 *  Last modified: $Date: 2006/07/20 16:21:01 $ by $Author: laddi $
 * 
 * @author <a href="mailto:eiki@idega.com">eiki</a>
 * @version $Revision: 1.3 $
 */
public class RunPluginBusinessHomeImpl extends IBOHomeImpl implements RunPluginBusinessHome {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -1746756879795834752L;

	protected Class getBeanInterfaceClass() {
		return RunPluginBusiness.class;
	}

	public RunPluginBusiness create() throws javax.ejb.CreateException {
		return (RunPluginBusiness) super.createIBO();
	}
}
