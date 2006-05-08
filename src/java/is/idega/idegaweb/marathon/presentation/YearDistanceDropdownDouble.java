/*
 * Created on Jul 9, 2004
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.util.LinkedHashMap;
import java.util.Map;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.SelectDropdownDouble;

/**
 * Description: This class displays a double dropdown menu. The first menu displays <br>
 * a list of year groups for a specific run (Rvk.marathon, Laugavegurinn, etc.) and the second menu displays<br>
 * a list of available distances for the specific run on the year selected<br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author birna
 */
public class YearDistanceDropdownDouble extends SelectDropdownDouble{
    
    public YearDistanceDropdownDouble() {
        super(IWMarathonConstants.GROUP_TYPE_RUN_YEAR,IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
    }
    public void main(IWContext iwc) throws Exception {
        IWResourceBundle iwrb = getResourceBundle(iwc);
        
        String runGroupID = iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN);
        if(runGroupID!=null && !runGroupID.equals("")) {
            addEmptyElement(iwrb.getLocalizedString("year_dis_ddd.select_year","Select year..."),
                    iwrb.getLocalizedString("year_dis_ddd.select_distance","Select distance..."));


            Map dis = new LinkedHashMap();
            dis.put("-1",iwrb.getLocalizedString("run_year_ddd.select_distance","Select distance..."));

 /*           while(yearIter.hasNext()) {
                Group year = (Group) yearIter.next();
                dis.putAll(runBiz.getDistancesMap(runGroup,year.getName()));
                addMenuElement(year.getPrimaryKey().toString(),year.getName(),dis);
          
            }*/
        }else {
            addEmptyElement(iwrb.getLocalizedString("year_dis_ddd.no_run","No run is selected..."),
                    iwrb.getLocalizedString("year_dis_ddd.no_run","No run is selected..."));

        }
        super.main(iwc);
    }
  	/**
  	 * @see com.idega.presentation.ui.SelectDropdownDouble#getValue(java.lang.Object)
  	 */

    protected String getValue(IWContext iwc, Object value) {
  			if (value instanceof String) {
  				String str = (String) value;
  				return str;
  			}
  			else {
  				IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IWMarathonConstants.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
  				return iwrb.getLocalizedString("run_year_ddd.select_distance","Select distance...");
  			}
  		}

  		/**
  		 * @see com.idega.presentation.ui.SelectDropdownDouble#getKey(java.lang.Object)
  		 */
  		protected String getKey(IWContext iwc, Object key) {
  			if (key instanceof String) {
  				String str = (String) key;
  				return str;
  			}
  			else {
  				return (String) key;
  			}
  		}
  		public String getBundleIdentifier() {
  			return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
  		}
  	}