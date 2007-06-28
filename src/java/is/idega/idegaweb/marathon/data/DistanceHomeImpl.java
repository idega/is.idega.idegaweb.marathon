/*
 * $Id: DistanceHomeImpl.java,v 1.3 2007/06/28 13:34:57 tryggvil Exp $
 * Created on May 30, 2005
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
 * @version $Revision: 1.3 $
 */
public class DistanceHomeImpl extends GroupHomeImpl implements DistanceHome {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -3579433418387389318L;

	protected Class getEntityInterfaceClass() {
		return Distance.class;
	}
}