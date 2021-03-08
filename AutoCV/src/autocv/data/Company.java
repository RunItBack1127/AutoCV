package autocv.data;

/**
 * Company encapsulating class, storing the name
 * and address associated with the company of the
 * cover letter
 * 
 * @author Weston Greene
 * @since  1.0
 */
public class Company {
	
	/** Company name */
	private String name;
	
	/** Company address */
	private Address address;
	
	/**
	 * Default constructor, initializing the
	 * company name and address
	 * 
	 * @param name	  company name
	 * @param address company address
	 */
	public Company( String name, Address address ) {
		this.name = name;
		this.address = address;
	}
	
	/**
	 * Secondary constructor, initializing
	 * only the company name; utilized by
	 * the UI class to determine the
	 * equivalence of two companies
	 * 
	 * @param name company name
	 */
	public Company( String name ) {
		this( name, null );
	}
	
	/**
	 * Retrieves the company name
	 * 
	 * @return company name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves the company address
	 * 
	 * @return company address
	 */
	public Address getAddress() {
		return address;
	}
	
	/**
	 * Determines if two companies are
	 * equivalent; one company is equal
	 * to another if the names of the two
	 * are equivalent
	 * 
	 * @return true if the companies are equal
	 */
	@Override
	public boolean equals( Object other ) {
		if( other instanceof Company ) {
			Company ocomp = ( Company ) other;
			return name.equals( ocomp.name );
		}
		return false;
	}
	
	/**
	 * Retrieves the name of the company
	 * 
	 * @return company name
	 */
	@Override
	public String toString() {
		return name;
	}
}
