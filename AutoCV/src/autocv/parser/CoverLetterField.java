package autocv.parser;

/**
 * Field enumeration, used for declaring the
 * fields to parse and replace within the base
 * HTML file; the transitions between all of the
 * given states are implemented by the field map
 * class
 * 
 * @author Weston Greene
 * @since  1.0
 */
public enum CoverLetterField {
	
	/**
	 * Applicant first name; this first name
	 * state transitions to the INITIAL_LAST_NAME
	 * field in the cover letter
	 */
	INITIAL_FIRST_NAME("[INITIAL_FIRST_NAME]"),
	
	/**
	 * Applicant last name; this last name
	 * state transitions to the EMAIL field
	 * in the cover letter
	 */
	INITIAL_LAST_NAME("[INITIAL_LAST_NAME]"),
	
	/**
	 * Applicant first name; this first name
	 * state pertains to the first name as
	 * signed at the end of the cover letter,
	 * and transitions to SIG_MIDDLE_INITIAL
	 */
	SIG_FIRST_NAME("[SIG_FIRST_NAME]"),
	
	/**
	 * Applicant middle initial; this middle
	 * initial state pertains to the middle
	 * initial as signed at the end of the
	 * cover letter, and transitions to
	 * SIG_LAST_NAME
	 */
	SIG_MIDDLE_INITIAL("[SIG_MIDDLE_INITIAL]"),
	
	/**
	 * Applicant last name; this last name
	 * state pertains to the last name as
	 * signed at the end of the cover letter,
	 * and transitions to FIRST_NAME
	 */
	SIG_LAST_NAME("[SIG_LAST_NAME]"),
	
	/**
	 * Applicant first name; this first name
	 * state transitions to MIDDLE_INITIAL
	 */
	FIRST_NAME("[FIRST_NAME]"),
	
	/**
	 * Applicant middle initial; this middle
	 * initial state transitions to LAST_NAME
	 */
	MIDDLE_INITIAL("[MIDDLE_INITIAL]"),
	
	/**
	 * Applicant last name; this last name
	 * state is the final parse field and
	 * does not transition to any other state
	 */
	LAST_NAME("[LAST_NAME]"),
	
	/** Applicant email field */
	EMAIL("[EMAIL]"),
	
	/** Applicant present street address field */
	PRESENT_ADDRESS_1("[PRESENT_ADDRESS_1]"),
	
	/** Applicant present city/state/zip field */
	PRESENT_ADDRESS_2("[PRESENT_ADDRESS_2]"),
	
	/** Applicant room number field */
	PRESENT_ADDRESS_3("[PRESENT_ADDRESS_3]"),
	
	/** Applicant permanent street address field */
	PERMANENT_ADDRESS_1("[PERMANENT_ADDRESS_1]"),
	
	/** Applicant permanent city/state/zip field */
	PERMANENT_ADDRESS_2("[PERMANENT_ADDRESS_2]"),
	
	/** Applicant permanent P.O. Box address field */
	PERMANENT_ADDRESS_3("[PERMANENT_ADDRESS_3]"),
	
	/** Applicant LinkedIn link field */
	LINKEDIN_LINK("[LINKEDIN_LINK]"),
	
	/** Applicant GitHub link field */
	GITHUB_LINK("[GITHUB_LINK]"),
	
	/** Applicant phone number field */
	PHONE_NUMBER("[PHONE_NUMBER]"),
	
	/** Cover letter date field */
	DATE("[DATE]"),
	
	/**
	 * Company name; this company name state
	 * transitions to LAST_COMPANY_NAME
	 */
	INITIAL_COMPANY_NAME("[INITIAL_COMPANY_NAME]"),
	
	/**
	 * Company name; this company name state
	 * transitions to SIG_FIRST_NAME
	 */
	LAST_COMPANY_NAME("[LAST_COMPANY_NAME]"),
	
	/** Company street address field */
	COMPANY_ADDRESS_1("[COMPANY_ADDRESS_1]"),
	
	/** Company city/state/zip field */
	COMPANY_ADDRESS_2("[COMPANY_ADDRESS_2]"),
	
	/** Applicant is rising field */
	IS_RISING("[IS_RISING]"),
	
	/**
	 * School year field of the cover letter,
	 * enumerated by FRESHMAN, SOPHOMORE,
	 * JUNIOR, and SENIOR
	 */
	SCHOOL_YEAR("[SCHOOL_YEAR]"),
	
	/** Job title field */
	JOB_TITLE("[JOB_TITLE]"),
	
	/** Job season field */
	JOB_SEASON("[JOB_SEASON]"),
	
	/** Job year field */
	JOB_YEAR("[JOB_YEAR]"),
	
	/** Job type field */
	JOB_TYPE("[JOB_TYPE]");
	
	/**
	 * Name of the field utilized by the
	 * HTMLBuilder class for parsing the
	 * field name from the base HTML file
	 */
	private String parseName;
	
	/**
	 * Default constructor, initializing
	 * the enumeration along with its
	 * valid parse name
	 * 
	 * @param parseName parse name
	 */
	CoverLetterField( String parseName ) {
		this.parseName = parseName;
	}
	
	/**
	 * Retrieves the parse name
	 * 
	 * @return parse name
	 */
	@Override
	public String toString() {
		return parseName;
	}
}
