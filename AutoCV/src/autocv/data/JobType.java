package autocv.data;

/**
 * Describes a job type for the job in
 * the cover letter, either an internship
 * or a co-op position
 * 
 * @author Weston Greene
 * @since  1.0
 */
public enum JobType {
	
	/** Specifies an internship position */
	INTERNSHIP("INTERNSHIP"),
	
	/** Specifies a co-op position */
	CO_OP("CO-OP");
	
	/**
	 * Name of the job type; the
	 * job type name might differ from
	 * the name of the associated
	 * enumeration (i.e. CO_OP > "CO-OP" );
	 */
	private String typeName;
	
	/**
	 * Default enumeration constructor,
	 * initializing the type name for
	 * the given type
	 * 
	 * @param typeName type name
	 */
	JobType( String typeName ) {
		this.typeName = typeName;
	}
	
	/**
	 * Retrieves the type name; this is
	 * utilized by the controls in the
	 * job credentials panel for displaying
	 * the correct type name for "CO-OP",
	 * and for printing this name to the
	 * reference HTML file
	 * 
	 * @return type name
	 */
	@Override
	public String toString() {
		return typeName;
	}
}
