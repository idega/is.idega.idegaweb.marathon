package is.idega.idegaweb.marathon.presentation.user.runoverview;

import java.text.NumberFormat;
import java.util.Locale;

import com.idega.util.CoreConstants;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/07/23 22:27:25 $ by $Author: palli $
 *
 */
public class Price {

	public Price() {}
	
	public Price(Float price, String currencyLabel, Locale locale) {
		this.price = price;
		this.currencyLabel = currencyLabel;
		this.locale = locale;
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
		NumberFormat format = NumberFormat.getInstance(locale);
		if (locale.getLanguage().equals(new Locale("IS").getLanguage())) {
			format.setMaximumFractionDigits(0);
			format.setMinimumFractionDigits(0);
		}
		return format.format(amount.floatValue());
	}	
}