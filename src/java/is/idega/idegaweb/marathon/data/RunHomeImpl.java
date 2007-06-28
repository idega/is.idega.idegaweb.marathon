/*
 * $Id: RunHomeImpl.java,v 1.12 2007/06/28 13:34:57 tryggvil Exp $
 * Created on May 22, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import com.idega.user.data.GroupHomeImpl;


/**
 * Last modified: $Date: 2007/06/28 13:34:57 $ by $Author: tryggvil $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.12 $
 */
public class RunHomeImpl extends GroupHomeImpl implements RunHome {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 8605909025252731051L;

	protected Class getEntityInterfaceClass() {
		return Run.class;
	}

}