/*
 * $Id: DistanceBMPBean.java,v 1.12 2007/12/13 20:02:00 civilis Exp $
 * Created on May 22, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.data;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import com.idega.user.data.Group;
import com.idega.user.data.GroupBMPBean;
import com.idega.util.LocaleUtil;


/**
 * Last modified: $Date: 2007/12/13 20:02:00 $ by $Author: civilis $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.12 $
 */
public class DistanceBMPBean extends GroupBMPBean  implements Group, Distance{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -2348766615544079846L;
	private static final String METADATA_USE_CHIP = "use_chip";
	private static final String METADATA_TRANSPORT_OFFERED = "transport_offered";
	
	private static final String METADATA_PRICE_ISK = "price_isk";
	private static final String METADATA_PRICE_EUR = "price_eur";
	private static final String METADATA_FAMILY_DISCOUNT = "family_discount";
	private static final String METADATA_CHILDREN_PRICE_ISK = "children_price_isk";
	private static final String METADATA_CHILDREN_PRICE_EUR = "children_price_eur";
	private static final String METADATA_PRICE_FOR_TRANSPORT_ISK = "price_for_transport_isk";
	private static final String METADATA_PRICE_FOR_TRANSPORT_EUR = "price_for_transport_eur";
	private static final String METADATA_ALLOWS_GROUPS = "allows_groups";
	private static final String METADATA_NEXT_PARTICIPANT_NUMBER = "next_participant_number";
	private static final String METADATA_NUMBER_OF_SPLITS = "number_of_splits";
	
	public static final String METADATA_MINIMUM_AGE_FOR_DISTANCE = "minimum_age_for_distance";
	public static final String METADATA_MAXIMUM_AGE_FOR_DISTANCE = "maximum_age_for_distance";

	//Suffix that all distance groups are suffixed with in their name
	public static final String KM_SUFFIX="_km";
	private Year year;
	
	public boolean isUseChip() {
		String useChip = this.getMetaData(METADATA_USE_CHIP);
		if (useChip != null) {
			return new Boolean(useChip).booleanValue();
		}
		return true;
	}

	public void setUseChip(boolean useChip) {
		setMetaData(METADATA_USE_CHIP, String.valueOf(useChip), "java.lang.Boolean");
	}
	
	public boolean isTransportOffered() {
		String transportOffered = this.getMetaData(METADATA_TRANSPORT_OFFERED);
		if (transportOffered != null) {
			return new Boolean(transportOffered).booleanValue();
		}
		return false;
	}

	public void setTransportOffered(boolean transportOffered) {
		setMetaData(METADATA_TRANSPORT_OFFERED, String.valueOf(transportOffered), "java.lang.Boolean");
	}
	
	public float getPrice(Locale locale) {
		String price = null;
		if (locale.equals(LocaleUtil.getIcelandicLocale())) {
			price = getMetaData(METADATA_PRICE_ISK);
		}
		else {
			price = getMetaData(METADATA_PRICE_EUR);
		}
		
		if (price != null) {
			return Float.parseFloat(price);
		}
		return 0;
	}
	
	public void setPriceInISK(float price) {
		setMetaData(METADATA_PRICE_ISK, String.valueOf(price), "java.lang.Float");
	}

	public void setPriceInEUR(float price) {
		setMetaData(METADATA_PRICE_EUR, String.valueOf(price), "java.lang.Float");
	}

	public float getChildrenPrice(Locale locale) {
		String price = null;
		if (locale.equals(LocaleUtil.getIcelandicLocale())) {
			price = getMetaData(METADATA_CHILDREN_PRICE_ISK);
		}
		else {
			price = getMetaData(METADATA_CHILDREN_PRICE_EUR);
		}
		
		if (price != null) {
			return Float.parseFloat(price);
		}
		else {
			return getPrice(locale);
		}
	}
	
	public void setChildrenPriceInISK(float price) {
		setMetaData(METADATA_CHILDREN_PRICE_ISK, String.valueOf(price), "java.lang.Float");
	}

	public void setChildrenPriceInEUR(float price) {
		setMetaData(METADATA_CHILDREN_PRICE_EUR, String.valueOf(price), "java.lang.Float");
	}

	public float getPriceForTransport(Locale locale) {
		String price = null;
		if (locale.equals(LocaleUtil.getIcelandicLocale())) {
			price = getMetaData(METADATA_PRICE_FOR_TRANSPORT_ISK);
		}
		else {
			price = getMetaData(METADATA_PRICE_FOR_TRANSPORT_EUR);
		}
		
		if (price != null) {
			return Float.parseFloat(price);
		}
		return 0;
	}

	public void setPriceForTransportInISK(float price) {
		setMetaData(METADATA_PRICE_FOR_TRANSPORT_ISK, String.valueOf(price), "java.lang.Float");
	}

	public void setPriceForTransportInEUR(float price) {
		setMetaData(METADATA_PRICE_FOR_TRANSPORT_EUR, String.valueOf(price), "java.lang.Float");
	}

	public boolean isFamilyDiscount() {
		String discount = this.getMetaData(METADATA_FAMILY_DISCOUNT);
		if (discount != null) {
			return new Boolean(discount).booleanValue();
		}
		return false;
	}

	public void setFamilyDiscount(boolean discount) {
		setMetaData(METADATA_FAMILY_DISCOUNT, String.valueOf(discount), "java.lang.Boolean");
	}

	public boolean isAllowsGroups() {
		String allowsGroups = this.getMetaData(METADATA_ALLOWS_GROUPS);
		if (allowsGroups != null) {
			return new Boolean(allowsGroups).booleanValue();
		}
		return true;
	}

	public void setAllowsGroups(boolean allowsGroups) {
		setMetaData(METADATA_ALLOWS_GROUPS, String.valueOf(allowsGroups), "java.lang.Boolean");
	}
	
	public int getNextAvailableParticipantNumber() {
		String number = this.getMetaData(METADATA_NEXT_PARTICIPANT_NUMBER);
		if (number != null) {
			return Integer.parseInt(number);
		}
		return -1;
	}
	
	public void setNextAvailableParticipantNumber(int number) {
		setMetaData(METADATA_NEXT_PARTICIPANT_NUMBER, String.valueOf(number), "java.lang.Integer");
	}

	public int getNumberOfSplits() {
		String number = this.getMetaData(METADATA_NUMBER_OF_SPLITS);
		if (number != null) {
			return Integer.parseInt(number);
		}
		return 0;
	}
	
	public void setNumberOfSplits(int number) {
		setMetaData(METADATA_NUMBER_OF_SPLITS, String.valueOf(number), "java.lang.Integer");
	}
	
	/**
	 * <p>
	 * Returns the distance set on the group in KiloMeters.
	 * Returns -1 when an error occurs or distance not decipherable.
	 * </p>
	 * @return
	 */
	public int getDistanceInKms(){
		String name = getName();
		int kmIndex = name.indexOf(KM_SUFFIX);
		if(kmIndex!=-1){
			String distStr = name.substring(0,kmIndex);
			try{
				int distance = Integer.parseInt(distStr);
				return distance;
			}
			catch(NumberFormatException nfe){
				nfe.printStackTrace();
			}
		}
		return -1;
	}

	public Year getYear(){
		if(year==null){
			List parentGroups = getParentGroups();
			for (Iterator iter = parentGroups.iterator(); iter.hasNext();) {
				Group group = (Group) iter.next();
				Object pk = group.getPrimaryKey();
				YearHome yHome;
				try {
					yHome = (YearHome) getIDOHome(Year.class);
					year = yHome.findByPrimaryKey(pk);		
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return year;
	}
	
	public int getMinimumAgeForDistance() {
		String sMinimumAge = getMetaData(METADATA_MINIMUM_AGE_FOR_DISTANCE);
		int minimumAge=-1;
		try{
			minimumAge = Integer.parseInt(sMinimumAge);
		}
		catch(Exception e){}
		return minimumAge;
	}

	public void setMinimumAgeForDistance(int minimumAgeForDistance) {
		setMetaData(METADATA_MINIMUM_AGE_FOR_DISTANCE, String.valueOf(minimumAgeForDistance));
	}

	public int getMaximumAgeForDistance() {
		String sMaximumAge = getMetaData(METADATA_MAXIMUM_AGE_FOR_DISTANCE);
		int maximumAge=-1;
		try{
			maximumAge = Integer.parseInt(sMaximumAge);
		}
		catch(Exception e){}
		return maximumAge;
	}

	public void setMaximumAgeForDistance(int maximumAgeForDistance) {
		setMetaData(METADATA_MAXIMUM_AGE_FOR_DISTANCE, String.valueOf(maximumAgeForDistance));
	}
}