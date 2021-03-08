package autocv.data;

/**
 * Address encapsulation class, containing
 * reference to the street address, the
 * city/state/zip data, and a tertiary
 * address of either an applicant or a
 * company
 * 
 * A tertiary address is treated as either
 * the room number of the present address of
 * an applicant, or as the P.O. Box address
 * of the permanent address of an applicant
 * 
 * @author Weston Greene
 * @since  1.0
 */
public class Address {
	
	/**
	 * Street address associated with the
	 * master address
	 */
	private String streetAddress;
	
	/**
	 * City, state, and zip code information
	 * associated with the master address
	 */
	private String cityStateZip;
	
	/**
	 * Tertiary information associated with
	 * the master address; programmatically,
	 * this is associated with either the
	 * present room number or the permanent
	 * P.O. Box address
	 */
	private String poBoxRoom;
	
	/**
	 * Default constructor, initializing the
	 * street address and the city/state/zip
	 * information
	 * 
	 * @param streetAddress street address
	 * @param cityStateZip  city/state/zip info
	 */
	public Address( String streetAddress,
			String cityStateZip ) {
		this( streetAddress, cityStateZip, null );
	}
	
	/**
	 * Secondary constructor, initializing the
	 * street address, city/state/zip information,
	 * and the tertiary address
	 * 
	 * @param streetAddress street address
	 * @param cityStateZip  city/state/zip info
	 * @param poBoxRoom		room number or P.O. Box
	 * 						address
	 */
	public Address( String streetAddress,
			String cityStateZip, String poBoxRoom ) {
		
		this.streetAddress = streetAddress;
		this.cityStateZip = cityStateZip;
		this.poBoxRoom = poBoxRoom;
	}
	
	/**
	 * Retrieves the street address
	 * 
	 * @return street address
	 */
	public String getStreetAddress() {
		return streetAddress;
	}
	
	/**
	 * Returns the city/state/zip information
	 * 
	 * @return city/state/zip info
	 */
	public String getCityStateZip() {
		return cityStateZip;
	}
	
	/**
	 * Retrieves the tertiary address
	 * 
	 * @return room number or P.O. Box
	 * 		   address
	 */
	public String getPOBoxRoom() {
		return poBoxRoom;
	}
	
	/**
	 * Determines if the given addresses are
	 * equivalent - an address is considered
	 * equivalent to another address if their
	 * street addresses, city/state/zip info,
	 * and tertiary addresses are equivalent
	 * 
	 * @returns true if addresses are equivalent
	 */
	@Override
	public boolean equals( Object other ) {
		if( other instanceof Address ) {
			Address oadd = ( Address ) other;
			return streetAddress.equals( oadd.streetAddress ) &&
					cityStateZip.equals( oadd.cityStateZip ) &&
					poBoxRoom.equals( oadd.poBoxRoom );
		}
		return false;
	}
}
