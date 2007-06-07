/*
 * $Id: Run.java,v 1.16 2007/06/07 23:30:19 tryggvil Exp $
 * Created on May 22, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import com.idega.user.data.Group;


/**
 * Last modified: $Date: 2007/06/07 23:30:19 $ by $Author: tryggvil $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.16 $
 */
public interface Run extends Group {

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#getFamilyDiscount
	 */
	public float getFamilyDiscount();

	/**
	 * @see is.idega.idegaweb.marathon.data.RunBMPBean#setFamilyDiscount
	 */
	public void setFamilyDiscount(float discount);

	public Year getCurrentRegistrationYear();
}
