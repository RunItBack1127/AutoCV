package autocv.data;

public class Applicant {
	
	/** Applicant name */
	private String name;
	
	/**
	 * Applicant first name; the first name
	 * is treated as any string of characters
	 * prior to the first whitespace in the
	 * applicant name
	 */
	private String firstName;
	
	/**
	 * Applicant last name; the last name is
	 * treated as any string of characters
	 * following the first whitespace in the
	 * applicant name, including and after other
	 * whitespace characters
	 */
	private String lastName;
	
	/** Applicant middle initial */
	// TODO: Middle initial field in app panel
	// 		 should only accept one single letter
	private String middleInitial;
	
	/** Applicant email */
	private String email;
	
	/** Applicant GitHub link */
	private String gitHubLink;
	
	/** Applicant LinkedIn link */
	private String linkedInLink;
	
	/** Applicant phone number */
	private String phone;
	
	/** Applicant school year */
	private SchoolYear schoolYear;
	
	/**
	 * Determines if the applicant is
	 * rising to the specified year
	 */
	private boolean isRising;
	
	/** Applicant present address */
	private Address presentAddress;
	
	/** Applicant permanent address */
	private Address permanentAddress;
	
	/**
	 * Default constructor, initializing all state
	 * associated with the Applicant
	 * 
	 * @param name			   applicant name
	 * @param middleInitial	   applicant middle initial
	 * @param email			   applicant email
	 * @param gitHubLink	   applicant GitHub link
	 * @param linkedInLink	   applicant LinkedIn link
	 * @param phone			   applicant phone number
	 * @param schoolYear	   applicant school year
	 * @param isRising		   determines if applicant is rising
	 * @param presentAddress   applicant present address
	 * @param permanentAddress applicant permanent address
	 */
	public Applicant( String name, String middleInitial,
			String email, String gitHubLink, String linkedInLink,
			String phone, SchoolYear schoolYear,
			boolean isRising, Address presentAddress,
			Address permanentAddress ) {
		
		this.name = name;
		
		// Delimits the name based on whitespace,
		// and stores the first and last names
		String[] names = name.split( " " );
		firstName = names[ 0 ];
		lastName = names[ 1 ];
		
		this.middleInitial = middleInitial;
		this.email = email;
		this.gitHubLink = gitHubLink;
		this.linkedInLink = linkedInLink;
		this.phone = phone;
		this.schoolYear = schoolYear;
		this.isRising = isRising;
		this.presentAddress = presentAddress;
		this.permanentAddress = permanentAddress;
	}
	
	/**
	 * Retrieves the applicant name
	 * 
	 * @return applicant name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves the first name of
	 * the applicant
	 * 
	 * @return applicant first name
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Retrieves the last name of
	 * the applicant
	 * 
	 * @return applicant last name
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Retrieves the middle initial of
	 * the applicant
	 * 
	 * @return applicant middle initial
	 */
	public String getMiddleInitial() {
		return middleInitial;
	}
	
	/**
	 * Retrieves the applicant email
	 * 
	 * @return applicant email address
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Retrieves the GitHub link of the
	 * applicant
	 * 
	 * @return applicant GitHub link
	 */
	public String getGitHubLink() {
		return gitHubLink;
	}
	
	/**
	 * Retrieves the LinkedIn link of
	 * the applicant
	 * 
	 * @return applicant LinkedIn link
	 */
	public String getLinkedInLink() {
		return linkedInLink;
	}
	
	/**
	 * Retrieves the present address
	 * associated with the applicant
	 * 
	 * @return applicant present address
	 */
	public Address getPresentAddress() {
		return presentAddress;
	}
	
	/**
	 * Retrieves the permanent address
	 * associated with the applicant
	 * 
	 * @return applicant permanent address
	 */
	public Address getPermanentAddress() {
		return permanentAddress;
	}
	
	/**
	 * Retrieves the phone number of
	 * the applicant
	 * 
	 * @return applicant phone number
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * Retrieves the school year associated
	 * with the applicant
	 * 
	 * @return applicant school year
	 */
	public SchoolYear getSchoolYear() {
		return schoolYear;
	}
	
	/**
	 * Determines if the applicant is rising
	 * to the specified school year
	 * 
	 * @return applicant is rising flag
	 */
	public boolean isRising() {
		return isRising;
	}
	
	/**
	 * Determines if the given applicant is
	 * equivalent to another applicant; all
	 * metrics of an applicant must be
	 * equivalent for the applicants to be
	 * considered equivalent
	 * 
	 * @returns true if applicants are
	 * 			equivalent
	 */
	@Override
	public boolean equals( Object other ) {
		if( other instanceof Applicant ) {
			Applicant oapp = ( Applicant ) other;
			return name.equals( oapp.name ) &&
					email.equals( oapp.email ) &&
					gitHubLink.equals( oapp.gitHubLink ) &&
					linkedInLink.equals( oapp.linkedInLink ) &&
					presentAddress.equals( oapp.presentAddress ) &&
					permanentAddress.equals( oapp.permanentAddress ) &&
					phone.equals( oapp.phone ) &&
					schoolYear == oapp.schoolYear &&
					isRising == oapp.isRising;
		}
		return false;
	}
}
