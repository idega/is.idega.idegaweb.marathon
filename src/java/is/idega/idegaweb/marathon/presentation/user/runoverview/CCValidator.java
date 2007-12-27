package is.idega.idegaweb.marathon.presentation.user.runoverview;

import org.apache.myfaces.custom.creditcardvalidator.CreditCardValidator;


/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/12/27 20:32:56 $ by $Author: civilis $
 *
 */
public class CCValidator extends CreditCardValidator {
	
	public static final String VALIDATOR_ID = "is.idega.idegaweb.marathon.presentation.user.runoverview.CCValidator";
	
	public String getMessage() {
		
		System.out.println("msg was: "+super.getMessage());
		return "ccn not correct";
	}
}