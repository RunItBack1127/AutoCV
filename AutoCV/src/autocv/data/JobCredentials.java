package autocv.data;

/**
 * Credentials encapsulating class for the data
 * associated with the job within the cover
 * letter
 * 
 * @author Weston Greene
 * @since  1.0
 */
public class JobCredentials {
	
	/** Type specification for the job */
	private JobType jobType;
	
	/** Season of hire for job */
	private JobSeason jobSeason;
	
	/** Year of hire for the job */
	private String year;
	
	/**
	 * Default constructor, initializing the
	 * the job type, and the
	 * year of hire
	 * 
	 * @param jobType 	job type
	 * @param jobSeason	season of hire
	 * @param year		year of hire
	 */
	public JobCredentials( JobType jobType,
			JobSeason jobSeason, String year ) {
		
		this.jobType = jobType;
		this.jobSeason = jobSeason;
		this.year = year;
	}
	
	/**
	 * Retrieves the season of hire for
	 * the job
	 * 
	 * @return job season of hire
	 */
	public JobSeason getJobSeason() {
		return jobSeason;
	}
	
	/**
	 * Retrieves the type for the job
	 * 
	 * @return job type
	 */
	public JobType getJobType() {
		return jobType;
	}
	
	/**
	 * Retrieves the year of hire for
	 * the job
	 * 
	 * @return job year of hire
	 */
	public String getYear() {
		return year;
	}
	
	/**
	 * Determines if the two job credentials
	 * classes are equivalent; one job credential
	 * is equivalent to another if they have
	 * equivalent types, seasons, and years of hire
	 * 
	 * @return true if the job credentials are equal
	 */
	@Override
	public boolean equals( Object other ) {
		if( other instanceof JobCredentials ) {
			JobCredentials ojbc = ( JobCredentials ) other;
			return jobSeason == ojbc.jobSeason &&
					jobType == ojbc.jobType &&
					year.equals( ojbc.year );
		}
		return false;
	}
}
