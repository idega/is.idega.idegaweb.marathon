package is.idega.idegaweb.marathon.presentation.user.runoverview;

import java.text.NumberFormat;
import java.util.Locale;

import com.idega.util.CoreConstants;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/12/29 15:42:13 $ by $Author: civilis $
 *
 */
public class Price {

	public Price() {}
	
	public Price(Float price, String currencyLabel, Locale locale) {

		this.price = price;
		this.currencyLabel = currencyLabel;
	}
	
	private Float price;
	private String currencyLabel;
	private Locale locale;
	
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public String getCurrencyLabel() {
		return currencyLabel;
	}
	public void setCurrencyLabel(String currencyLabel) {
		this.currencyLabel = currencyLabel;
	}
	
	public String getPriceLabel() {
		
		return price != null && currencyLabel != null ? new StringBuffer(locale != null ? formatAmount(locale, price) : price.toString()).append(CoreConstants.SPACE).append(currencyLabel).toString() : CoreConstants.EMPTY; 
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	private String formatAmount(Locale locale, Float amount) {
		return NumberFormat.getInstance(locale).format(amount.floatValue());
	}
}