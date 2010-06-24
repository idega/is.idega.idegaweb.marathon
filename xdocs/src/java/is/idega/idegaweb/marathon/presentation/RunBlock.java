/*
 * $Id: RunBlock.java,v 1.13 2007/10/31 12:59:39 idegaweb Exp $
 * Created on May 17, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.marathon.presentation;



import java.util.Collection;
import java.util.Iterator;

import is.idega.idegaweb.marathon.business.CharityBusiness;
import is.idega.idegaweb.marathon.business.PledgeBusiness;
import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.location.business.AddressBusiness;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Layer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Heading1;
import com.idega.presentation.text.ListItem;
import com.idega.presentation.text.Lists;
import com.idega.presentation.text.Text;
import com.idega.user.business.GenderBusiness;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;


/**
 * Last modified: $Date: 2007/10/31 12:59:39 $ by $Author: idegaweb $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.13 $
 */
public class RunBlock extends StepsBlock {
	
	protected RunBusiness getRunBusiness(IWApplicationContext iwac) {
		try {
			return (RunBusiness) IBOLookup.getServiceInstance(iwac, RunBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected CharityBusiness getCharityBusiness(IWApplicationContext iwac) {
		try {
			return (CharityBusiness) IBOLookup.getServiceInstance(iwac, CharityBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected PledgeBusiness getPledgeBusiness(IWApplicationContext iwac) {
		try {
			return (PledgeBusiness) IBOLookup.getServiceInstance(iwac, PledgeBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected UserBusiness getUserBusiness(IWApplicationContext iwac) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected GroupBusiness getGroupBusiness(IWApplicationContext iwac) {
		try {
			return (GroupBusiness) IBOLookup.getServiceInstance(iwac, GroupBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected GenderBusiness getGenderBusiness(IWApplicationContext iwac) {
		try {
			return (GenderBusiness) IBOLookup.getServiceInstance(iwac, GenderBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	protected AddressBusiness getAddressBusiness(IWApplicationContext iwac) {
		try {
			return (AddressBusiness) IBOLookup.getServiceInstance(iwac, AddressBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	protected Table getPhasesTable(int phase, int totalPhases, String key, String defaultText) {
		Table table = new Table(2, 1);
		table.setCellpadding(3);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setAlignment(2, 1, Table.HORIZONTAL_ALIGN_RIGHT);
		table.setBottomCellBorder(1, 1, 1, "#D7D7D7", "solid");
		table.setBottomCellBorder(2, 1, 1, "#D7D7D7", "solid");
		
		table.add(getHeader(localize(key, defaultText)), 1, 1);
		
		StringBuffer buffer = new StringBuffer();
		buffer.append(localize("step", "Step")).append(" ").append(phase).append(" ").append(localize("of", "of")).append(" ").append(totalPhases);
		table.add(getHeader(buffer.toString()), 2, 1);
		
		return table;
	}

	protected Table getInformationTable(String information) {
		Table table = new Table(1, 1);
		table.setCellpadding(3);
		table.setCellspacing(0);
		table.setWidth(Table.HUNDRED_PERCENT);
		table.setBottomCellBorder(1, 1, 1, "#D7D7D7", "solid");
		table.setCellpaddingBottom(1, 1, 6);
		
		table.add(getText(information), 1, 1);
		
		return table;
	}

	/**
	 * Returns the default edit icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The edit icon.
	 */
	protected Image getEditIcon(String toolTip) {
		Image editImage = this.iwb.getImage("shared/edit.gif", 12, 12);
		editImage.setToolTip(toolTip);
		return editImage;
	}

	/**
	 * Returns the default delete icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The delete icon.
	 */
	protected Image getDeleteIcon(String toolTip) {
		Image deleteImage = this.iwb.getImage("shared/delete.gif", 12, 12);
		deleteImage.setToolTip(toolTip);
		return deleteImage;
	}

	/**
	 * Returns the default forward icon with the tooltip specified.
	 * @param toolTip	The tooltip to display on mouse over.
	 * @return Image	The forward icon.
	 */
	protected Image getForwardIcon(String toolTip) {
		Image forwardImage = this.iwb.getImage("shared/forward.gif", 12, 12);
		forwardImage.setToolTip(toolTip);
		return forwardImage;
	}

	protected void showErrors(IWContext iwc, Collection errors) {
		Layer layer = new Layer(Layer.DIV);
		layer.setStyleClass("errorLayer");
		
		Layer image = new Layer(Layer.DIV);
		image.setStyleClass("errorImage");
		layer.add(image);
		
		Heading1 heading = new Heading1(getResourceBundle(iwc).getLocalizedString("application_errors_occured", "There was a problem with the following items"));
		layer.add(heading);
		
		Lists list = new Lists();
		layer.add(list);
		
		Iterator iter = errors.iterator();
		while (iter.hasNext()) {
			String element = (String) iter.next();
			ListItem item = new ListItem();
			item.add(new Text(element));
			
			list.add(item);
		}
		
		add(layer);
	}

	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}

}