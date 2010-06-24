/*
 * $Id: ConverterUtility.java,v 1.4 2007/12/17 13:16:53 civilis Exp $
 * Created on May 22, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.DistanceHome;
import is.idega.idegaweb.marathon.data.Run;
import is.idega.idegaweb.marathon.data.RunHome;
import is.idega.idegaweb.marathon.data.Year;
import is.idega.idegaweb.marathon.data.YearHome;
import javax.ejb.FinderException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.user.data.Group;


/**
 * Last modified: $Date: 2007/12/17 13:16:53 $ by $Author: civilis $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.4 $
 */
public class ConverterUtility {

	private static ConverterUtility util = null;
	
	private ConverterUtility() {
	}
	
	public static synchronized ConverterUtility getInstance() {
		if (util == null) {
			util = new ConverterUtility();
		}
		return util;
	}

	public Run convertGroupToRun(Group group) throws FinderException {
		return convertGroupToRun(group.getPrimaryKey());
	}
	
	public Run convertGroupToRun(Object groupPK) throws FinderException {
		try {
			RunHome home = (RunHome) IDOLookup.getHome(Run.class);
			return (Run) home.findByPrimaryKey(groupPK);
		}
		catch (IDOLookupException ile) {
			throw new FinderException(ile.getMessage());
		}
	}

	public Distance convertGroupToDistance(Group group) throws FinderException {
		return convertGroupToDistance(group.getPrimaryKey());
	}
	
	public Distance convertGroupToDistance(Object groupPK) throws FinderException {
		try {
			DistanceHome home = (DistanceHome) IDOLookup.getHome(Distance.class);
			return (Distance) home.findByPrimaryKey(groupPK);
		}
		catch (IDOLookupException ile) {
			throw new FinderException(ile.getMessage());
		}
	}

	public Year convertGroupToYear(Group group) throws FinderException {
		return convertGroupToYear(group.getPrimaryKey());
	}
	
	public Year convertGroupToYear(Object groupPK) throws FinderException {
		try {
			YearHome home = (YearHome) IDOLookup.getHome(Year.class);
			return (Year) home.findByPrimaryKey(groupPK);
		}
		catch (IDOLookupException ile) {
			throw new FinderException(ile.getMessage());
		}
	}
}