package autocv.parser;

import java.util.HashMap;

/**
 * Transition state class used for implementing
 * the functionality of the finite state machine
 * outlined by the fields of the cover letter; all
 * parse transitions used by the HTMLBuilder are
 * referenced by the key-map pairings instantiated
 * by this class
 * 
 * @author Weston Greene
 * @since  1.0
 */
public class CoverLetterFieldMap extends HashMap<CoverLetterField,
	CoverLetterField> {
	
	/** Collection serial version identifier */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor, initializing all of the
	 * transitions used by the parse logic of the
	 * HTMLBuilder
	 */
	public CoverLetterFieldMap() {
		super();
		put( CoverLetterField.INITIAL_FIRST_NAME, CoverLetterField.INITIAL_LAST_NAME );
		put( CoverLetterField.INITIAL_LAST_NAME, CoverLetterField.EMAIL );
		put( CoverLetterField.SIG_FIRST_NAME, CoverLetterField.SIG_MIDDLE_INITIAL );
		put( CoverLetterField.SIG_MIDDLE_INITIAL, CoverLetterField.SIG_LAST_NAME );
		put( CoverLetterField.SIG_LAST_NAME, CoverLetterField.FIRST_NAME );
		put( CoverLetterField.FIRST_NAME, CoverLetterField.MIDDLE_INITIAL );
		put( CoverLetterField.MIDDLE_INITIAL, CoverLetterField.LAST_NAME );
		put( CoverLetterField.LAST_NAME, CoverLetterField.EMAIL );
		put( CoverLetterField.EMAIL, CoverLetterField.PRESENT_ADDRESS_1 );
		put( CoverLetterField.PRESENT_ADDRESS_1, CoverLetterField.LINKEDIN_LINK );
		put( CoverLetterField.LINKEDIN_LINK, CoverLetterField.PERMANENT_ADDRESS_1 );
		put( CoverLetterField.PERMANENT_ADDRESS_1, CoverLetterField.PRESENT_ADDRESS_2 );
		put( CoverLetterField.PRESENT_ADDRESS_2, CoverLetterField.GITHUB_LINK );
		put( CoverLetterField.GITHUB_LINK, CoverLetterField.PERMANENT_ADDRESS_2 );
		put( CoverLetterField.PERMANENT_ADDRESS_2, CoverLetterField.PRESENT_ADDRESS_3 );
		put( CoverLetterField.PRESENT_ADDRESS_3, CoverLetterField.PHONE_NUMBER );
		put( CoverLetterField.PHONE_NUMBER, CoverLetterField.PERMANENT_ADDRESS_3 );
		put( CoverLetterField.PERMANENT_ADDRESS_3, CoverLetterField.DATE );
		put( CoverLetterField.DATE, CoverLetterField.INITIAL_COMPANY_NAME );
		put( CoverLetterField.INITIAL_COMPANY_NAME, CoverLetterField.COMPANY_ADDRESS_1 );
		put( CoverLetterField.COMPANY_ADDRESS_1, CoverLetterField.COMPANY_ADDRESS_2 );
		put( CoverLetterField.COMPANY_ADDRESS_2, CoverLetterField.IS_RISING );
		put( CoverLetterField.IS_RISING, CoverLetterField.SCHOOL_YEAR );
		put( CoverLetterField.SCHOOL_YEAR, CoverLetterField.JOB_TITLE );
		put( CoverLetterField.JOB_TITLE, CoverLetterField.JOB_TYPE );
		put( CoverLetterField.JOB_TYPE, CoverLetterField.JOB_SEASON );
		put( CoverLetterField.JOB_SEASON, CoverLetterField.JOB_YEAR );
		put( CoverLetterField.JOB_YEAR, CoverLetterField.LAST_COMPANY_NAME );
		put( CoverLetterField.LAST_COMPANY_NAME, CoverLetterField.SIG_FIRST_NAME );
	}
}
