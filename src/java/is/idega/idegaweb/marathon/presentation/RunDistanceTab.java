/*
 * Created on Aug 17, 2004
 *
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.ConverterUtility;
import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.util.List;
import java.util.Locale;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.TextInput;
import com.idega.user.presentation.UserGroupTab;
import com.idega.util.ListUtil;
import com.idega.util.LocaleUtil;
import com.idega.util.MiscUtil;


/**
 * @author birna
 *
 */
public class RunDistanceTab extends UserGroupTab{
	
	private static final String PARAMETER_USE_CHIP = "use_chip";
	private static final String PARAMETER_FAMILY_DISCOUNT = "family_discount";
	private static final String PARAMETER_ALLOWS_GROUPS = "allows_groups";
	private static final String PARAMETER_TRANSPORT_OFFERED = "transport_offered";

	private static final String PARAMETER_PRICE_ISK = "price_isk";
	private static final String PARAMETER_PRICE_EUR = "price_eur";
	private static final String PARAMETER_CHILDREN_PRICE_ISK = "children_price_isk";
	private static final String PARAMETER_CHILDREN_PRICE_EUR = "children_price_eur";
	private static final String PARAMETER_PRICE_FOR_TRANSPORT_ISK = "price_for_transport_isk";
	private static final String PARAMETER_PRICE_FOR_TRANSPORT_EUR = "price_for_transport_eur";
	
	private static final String PARAMETER_NUMBER_OF_SPLITS = "number_of_splits";
	private static final String PARAMETER_SHIRT_SIZES_PER_RUN = "shirt_sizes_per_run";

	private TextInput priceISK;
	private TextInput priceEUR;
	private TextInput childrenPriceISK;
	private TextInput childrenPriceEUR;
	private TextInput priceForTransportISK;
	private TextInput priceForTransportEUR;
	
	private CheckBox useChip;
	private CheckBox familyDiscount;
	private CheckBox allowsGroups;
	private CheckBox transportOffered;
	
	private DropdownMenu numberOfSplits;
	private ShirtSizeSelectionBox shirtSizeSelectionBox;
	
	private Text priceISKText;
	private Text priceEURText;
	private Text childrenPriceISKText;
	private Text childrenPriceEURText;
	private Text priceForTransportISKText;
	private Text priceForTransportEURText;
	private Text useChipText;
	private Text familyDiscountText;
	private Text allowsGroupsText;
	private Text transportOfferedText;
	private Text numberOfSplitsText;
	private Text shirtSizeSelectionBoxText;
	
	public RunDistanceTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString("run_tab.distance_name", "Distance info"));
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
			Boolean useChip = new Boolean(iwc.isParameterSet(PARAMETER_USE_CHIP));
			Boolean familyDiscount = new Boolean(iwc.isParameterSet(PARAMETER_FAMILY_DISCOUNT));
			Boolean allowsGroups = new Boolean(iwc.isParameterSet(PARAMETER_ALLOWS_GROUPS));
			Boolean transportOffered = new Boolean(iwc.isParameterSet(PARAMETER_TRANSPORT_OFFERED));
			
			String priceISK = iwc.getParameter(PARAMETER_PRICE_ISK);
			String priceEUR = iwc.getParameter(PARAMETER_PRICE_EUR);
			String childPriceISK = iwc.getParameter(PARAMETER_CHILDREN_PRICE_ISK);
			String childPriceEUR = iwc.getParameter(PARAMETER_CHILDREN_PRICE_EUR);
			String priceForTransportISK = iwc.getParameter(PARAMETER_PRICE_FOR_TRANSPORT_ISK);
			String priceForTransportEUR = iwc.getParameter(PARAMETER_PRICE_FOR_TRANSPORT_EUR);
			
			String numberOfSplits = iwc.getParameter(PARAMETER_NUMBER_OF_SPLITS);
			String[] shirtSizesPerRun = iwc.getParameterValues(PARAMETER_SHIRT_SIZES_PER_RUN);
			
			this.fieldValues.put(PARAMETER_USE_CHIP, useChip);
			this.fieldValues.put(PARAMETER_FAMILY_DISCOUNT, familyDiscount);
			this.fieldValues.put(PARAMETER_ALLOWS_GROUPS, allowsGroups);
			this.fieldValues.put(PARAMETER_TRANSPORT_OFFERED, transportOffered);

			if (priceISK != null) {
				this.fieldValues.put(PARAMETER_PRICE_ISK, new Float(priceISK));
			}
			if (priceEUR != null) {
				this.fieldValues.put(PARAMETER_PRICE_EUR, new Float(priceEUR));
			}
			if (childPriceISK != null) {
				this.fieldValues.put(PARAMETER_CHILDREN_PRICE_ISK, new Float(childPriceISK));
			}
			if(childPriceEUR != null){
				this.fieldValues.put(PARAMETER_CHILDREN_PRICE_EUR, new Float(childPriceEUR));
			}
			if (priceForTransportISK != null) {
				this.fieldValues.put(PARAMETER_PRICE_FOR_TRANSPORT_ISK, new Float(priceForTransportISK));
			}
			if (priceForTransportEUR != null) {
				this.fieldValues.put(PARAMETER_PRICE_FOR_TRANSPORT_EUR, new Float(priceForTransportEUR));
			}
			if(numberOfSplits != null){
				this.fieldValues.put(PARAMETER_NUMBER_OF_SPLITS, new Integer(numberOfSplits));
			}
			if(shirtSizesPerRun != null){
				this.fieldValues.put(PARAMETER_SHIRT_SIZES_PER_RUN, shirtSizesPerRun);
			}
			updateFieldsDisplayStatus();
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initFieldContents()
	 */
	public void initFieldContents() {
		try {
			Distance distance = ConverterUtility.getInstance().convertGroupToDistance(new Integer(getGroupId()));
			
			this.fieldValues.put(PARAMETER_USE_CHIP, new Boolean(distance.isUseChip()));
			this.fieldValues.put(PARAMETER_FAMILY_DISCOUNT, new Boolean(distance.isFamilyDiscount()));
			this.fieldValues.put(PARAMETER_ALLOWS_GROUPS, new Boolean(distance.isAllowsGroups()));
			this.fieldValues.put(PARAMETER_TRANSPORT_OFFERED, new Boolean(distance.isTransportOffered()));
			this.fieldValues.put(PARAMETER_PRICE_ISK, new Float(distance.getPrice(LocaleUtil.getIcelandicLocale())));
			this.fieldValues.put(PARAMETER_PRICE_EUR, new Float(distance.getPrice(Locale.ENGLISH)));
			this.fieldValues.put(PARAMETER_CHILDREN_PRICE_ISK, new Float(distance.getChildrenPrice(LocaleUtil.getIcelandicLocale())));
			this.fieldValues.put(PARAMETER_CHILDREN_PRICE_EUR, new Float(distance.getChildrenPrice(Locale.ENGLISH)));
			this.fieldValues.put(PARAMETER_PRICE_FOR_TRANSPORT_ISK, new Float(distance.getPriceForTransport(LocaleUtil.getIcelandicLocale())));
			this.fieldValues.put(PARAMETER_PRICE_FOR_TRANSPORT_EUR, new Float(distance.getPriceForTransport(Locale.ENGLISH)));
			this.fieldValues.put(PARAMETER_NUMBER_OF_SPLITS, new Integer(distance.getNumberOfSplits()));
			String shirtSizeMetadata = distance.getMetaData(PARAMETER_SHIRT_SIZES_PER_RUN);
			if (shirtSizeMetadata != null) {
				String[] shirtSizeMetadataArray = MiscUtil.str2array(shirtSizeMetadata,",");
				this.fieldValues.put(PARAMETER_SHIRT_SIZES_PER_RUN, shirtSizeMetadataArray);
			}

			updateFieldsDisplayStatus();
		}
		catch (Exception e) {
			System.err.println("RunDistanceTab error initFieldContents, GroupId : " + getGroupId());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFieldNames()
	 */
	public void initializeFieldNames() {
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFields()
	 */
	public void initializeFields() {
		this.priceISK = new TextInput(PARAMETER_PRICE_ISK);
		this.priceISK.setAsFloat("Not a valid price");

		this.priceEUR = new TextInput(PARAMETER_PRICE_EUR);
		this.priceEUR.setAsFloat("Not a valid price");

		this.childrenPriceISK = new TextInput(PARAMETER_CHILDREN_PRICE_ISK);
		this.childrenPriceISK.setAsFloat("Not a valid price");

		this.childrenPriceEUR = new TextInput(PARAMETER_CHILDREN_PRICE_EUR);
		this.childrenPriceEUR.setAsFloat("Not a valid price");
		
		this.priceForTransportISK = new TextInput(PARAMETER_PRICE_FOR_TRANSPORT_ISK);
		this.priceForTransportISK.setAsFloat("Not a valid price");

		this.priceForTransportEUR = new TextInput(PARAMETER_PRICE_FOR_TRANSPORT_EUR);
		this.priceForTransportEUR.setAsFloat("Not a valid price");
		
		this.useChip = new CheckBox(PARAMETER_USE_CHIP);
		this.familyDiscount = new CheckBox(PARAMETER_FAMILY_DISCOUNT);
		this.allowsGroups = new CheckBox(PARAMETER_ALLOWS_GROUPS);
		this.transportOffered = new CheckBox(PARAMETER_TRANSPORT_OFFERED);
		
		this.numberOfSplits = new DropdownMenu(PARAMETER_NUMBER_OF_SPLITS);
		this.numberOfSplits.addMenuElement(0, "0");
		this.numberOfSplits.addMenuElement(1, "1");
		this.numberOfSplits.addMenuElement(2, "2");

		this.shirtSizeSelectionBox = new ShirtSizeSelectionBox(PARAMETER_SHIRT_SIZES_PER_RUN);
		this.shirtSizeSelectionBox.initialize(IWContext.getInstance());
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		this.fieldValues.put(PARAMETER_USE_CHIP, new Boolean(false));
		this.fieldValues.put(PARAMETER_FAMILY_DISCOUNT, new Boolean(false));
		this.fieldValues.put(PARAMETER_ALLOWS_GROUPS, new Boolean(false));
		this.fieldValues.put(PARAMETER_TRANSPORT_OFFERED, new Boolean(false));
		this.fieldValues.put(PARAMETER_PRICE_ISK, new Float(0));
		this.fieldValues.put(PARAMETER_PRICE_EUR, new Float(0));
		this.fieldValues.put(PARAMETER_CHILDREN_PRICE_ISK, new Float(0));
		this.fieldValues.put(PARAMETER_CHILDREN_PRICE_EUR, new Float(0));
		this.fieldValues.put(PARAMETER_PRICE_FOR_TRANSPORT_ISK, new Float(0));
		this.fieldValues.put(PARAMETER_PRICE_FOR_TRANSPORT_EUR, new Float(0));
		this.fieldValues.put(PARAMETER_NUMBER_OF_SPLITS, new Integer(0));
		this.fieldValues.put(PARAMETER_SHIRT_SIZES_PER_RUN, new String[0]);
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#initializeTexts()
	 */
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		this.priceISKText = new Text(iwrb.getLocalizedString("run_tab.price_ISK", "Price (ISK)"));
		this.priceISKText.setBold();
		
		this.priceEURText = new Text(iwrb.getLocalizedString("run_tab.price_EUR", "Price (EUR)"));
		this.priceEURText.setBold();
		
		this.childrenPriceISKText = new Text(iwrb.getLocalizedString("run_tab.children_price_ISK", "Children price (ISK)"));
		this.childrenPriceISKText.setBold();
		
		this.childrenPriceEURText = new Text(iwrb.getLocalizedString("run_tab.children_price_EUR", "Children price (EUR)"));
		this.childrenPriceEURText.setBold();
		
		this.priceForTransportISKText = new Text(iwrb.getLocalizedString("run_tab.price_for_transport_ISK", "Price tor transport (ISK)"));
		this.priceForTransportISKText.setBold();
		
		this.priceForTransportEURText = new Text(iwrb.getLocalizedString("run_tab.price_for_transport_EUR", "Price for transport (EUR)"));
		this.priceForTransportEURText.setBold();
		
		this.useChipText = new Text(iwrb.getLocalizedString("run_tab.use_chip", "Uses chips"));
		this.useChipText.setBold();
		
		this.familyDiscountText = new Text(iwrb.getLocalizedString("run_tab.family_discount", "Uses family discount"));
		this.familyDiscountText.setBold();

		this.allowsGroupsText = new Text(iwrb.getLocalizedString("run_tab.allows_groups", "Allows groups"));
		this.allowsGroupsText.setBold();
		
		this.transportOfferedText = new Text(iwrb.getLocalizedString("run_tab.transport_offered", "Transport offered"));
		this.transportOfferedText.setBold();

		this.numberOfSplitsText = new Text(iwrb.getLocalizedString("run_tab.number_of_splits", "Number of splits"));
		this.numberOfSplitsText.setBold();

		this.shirtSizeSelectionBoxText = new Text(iwrb.getLocalizedString("run_tab.shirt_sizes", "Shirt sizes"));
		this.shirtSizeSelectionBoxText.setBold();
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#lineUpFields()
	 */
	public void lineUpFields() {
		resize(1, 1);
		setCellpadding(0);
		setCellspacing(0);
		
		Table table = new Table(2, 4);
		table.setCellpadding(5);
		table.setCellspacing(0);
		table.setWidth(1, "50%");
		table.setWidth(2, "50%");
		
		table.setWidth(Table.HUNDRED_PERCENT);
		table.add(this.priceISKText, 1, 1);
		table.add(Text.getBreak(), 1, 1);
		table.add(this.priceISK, 1, 1);
		
		table.add(this.childrenPriceISKText, 2, 1);
		table.add(Text.getBreak(), 2, 1);
		table.add(this.childrenPriceISK, 2, 1);
		
		table.add(this.priceEURText, 1, 2);
		table.add(Text.getBreak(), 1, 2);
		table.add(this.priceEUR, 1, 2);
		
		table.add(this.childrenPriceEURText, 2, 2);
		table.add(Text.getBreak(), 2, 2);
		table.add(this.childrenPriceEUR, 2, 2);
		
		table.mergeCells(1, 3, 2, 3);
		table.add(this.numberOfSplitsText, 1, 3);
		table.add(Text.getNonBrakingSpace(), 1, 3);
		table.add(this.numberOfSplits, 1, 3);

		table.mergeCells(1, 4, 2, 4);
		table.add(this.useChip, 1, 4);
		table.add(Text.getNonBrakingSpace(), 1, 4);
		table.add(this.useChipText, 1, 4);
		
		table.add(Text.getBreak(), 1, 4);
		
		table.add(this.familyDiscount, 1, 4);
		table.add(Text.getNonBrakingSpace(), 1, 4);
		table.add(this.familyDiscountText, 1, 4);

		table.add(Text.getBreak(), 1, 4);
		
		table.add(this.allowsGroups, 1, 4);
		table.add(Text.getNonBrakingSpace(), 1, 4);
		table.add(this.allowsGroupsText, 1, 4);
		table.add(Text.getBreak(), 1, 4);
		table.add(this.transportOffered, 1, 4);
		table.add(Text.getNonBrakingSpace(), 1, 4);
		table.add(this.transportOfferedText, 1, 4);
		table.add(Text.getBreak(), 1, 4);
		table.add(this.priceForTransportISKText, 1, 4);
		table.add(Text.getBreak(), 1, 4);
		table.add(this.priceForTransportISK, 1, 4);
		table.add(Text.getBreak(), 1, 4);
		table.add(this.priceForTransportEURText, 1, 4);
		table.add(Text.getBreak(), 1, 4);
		table.add(this.priceForTransportEUR, 1, 4);
		table.add(Text.getBreak(), 1, 4);
		table.add(Text.getBreak(), 1, 4);
		table.add(this.shirtSizeSelectionBoxText, 1, 4);
		table.add(Text.getBreak(), 1, 4);
		table.add(this.shirtSizeSelectionBox,1,4);
		
		add(table, 1, 1);
	}
	
	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {
		try {
			if (getGroupId() > -1) {
				Distance distance = ConverterUtility.getInstance().convertGroupToDistance(new Integer(getGroupId()));
				
				distance.setUseChip(((Boolean) this.fieldValues.get(PARAMETER_USE_CHIP)).booleanValue());
				distance.setFamilyDiscount(((Boolean) this.fieldValues.get(PARAMETER_FAMILY_DISCOUNT)).booleanValue());
				distance.setAllowsGroups(((Boolean) this.fieldValues.get(PARAMETER_ALLOWS_GROUPS)).booleanValue());
				distance.setTransportOffered(((Boolean) this.fieldValues.get(PARAMETER_TRANSPORT_OFFERED)).booleanValue());
				
				distance.setPriceInISK(((Float) this.fieldValues.get(PARAMETER_PRICE_ISK)).floatValue());
				distance.setPriceInEUR(((Float) this.fieldValues.get(PARAMETER_PRICE_EUR)).floatValue());
				distance.setChildrenPriceInISK(((Float) this.fieldValues.get(PARAMETER_CHILDREN_PRICE_ISK)).floatValue());
				distance.setChildrenPriceInEUR(((Float) this.fieldValues.get(PARAMETER_CHILDREN_PRICE_EUR)).floatValue());
				distance.setPriceForTransportInISK(((Float) this.fieldValues.get(PARAMETER_PRICE_FOR_TRANSPORT_ISK)).floatValue());
				distance.setPriceForTransportInEUR(((Float) this.fieldValues.get(PARAMETER_PRICE_FOR_TRANSPORT_EUR)).floatValue());

				distance.setNumberOfSplits(((Integer) this.fieldValues.get(PARAMETER_NUMBER_OF_SPLITS)).intValue());
				String[] shirtSizesPerRun = (String[]) this.fieldValues.get(PARAMETER_SHIRT_SIZES_PER_RUN);
				if(shirtSizesPerRun!=null && shirtSizesPerRun.length != 0){
					List abbrList = ListUtil.convertStringArrayToList(shirtSizesPerRun);
					if(abbrList.isEmpty()){
						distance.setMetaData(PARAMETER_SHIRT_SIZES_PER_RUN, "");
					}
					else{
						String commaSeparated = ListUtil.convertListOfStringsToCommaseparatedString(abbrList);
						distance.setMetaData(PARAMETER_SHIRT_SIZES_PER_RUN, commaSeparated);
					}
				}

				distance.store();
			}
		}
		catch (Exception e) {
			//return false;
			e.printStackTrace(System.err);
			throw new RuntimeException("update group exception");
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		this.useChip.setChecked(((Boolean) this.fieldValues.get(PARAMETER_USE_CHIP)).booleanValue());
		this.familyDiscount.setChecked(((Boolean) this.fieldValues.get(PARAMETER_FAMILY_DISCOUNT)).booleanValue());
		this.allowsGroups.setChecked(((Boolean) this.fieldValues.get(PARAMETER_ALLOWS_GROUPS)).booleanValue());
		this.transportOffered.setChecked(((Boolean) this.fieldValues.get(PARAMETER_TRANSPORT_OFFERED)).booleanValue());
		
		this.priceISK.setContent(((Float) this.fieldValues.get(PARAMETER_PRICE_ISK)).toString());
		this.priceEUR.setContent(((Float) this.fieldValues.get(PARAMETER_PRICE_EUR)).toString());
		this.childrenPriceISK.setContent(((Float) this.fieldValues.get(PARAMETER_CHILDREN_PRICE_ISK)).toString());
		this.childrenPriceEUR.setContent(((Float) this.fieldValues.get(PARAMETER_CHILDREN_PRICE_EUR)).toString());
		this.priceForTransportISK.setContent(((Float) this.fieldValues.get(PARAMETER_PRICE_FOR_TRANSPORT_ISK)).toString());
		this.priceForTransportEUR.setContent(((Float) this.fieldValues.get(PARAMETER_PRICE_FOR_TRANSPORT_EUR)).toString());
		

		this.numberOfSplits.setSelectedElement(((Integer) this.fieldValues.get(PARAMETER_NUMBER_OF_SPLITS)).intValue());
		this.shirtSizeSelectionBox.setSelectedElements((String[]) this.fieldValues.get(PARAMETER_SHIRT_SIZES_PER_RUN));
	}
	
	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}
}