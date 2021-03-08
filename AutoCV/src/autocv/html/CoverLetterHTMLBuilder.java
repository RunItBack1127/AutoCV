package autocv.html;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

import autocv.data.Applicant;
import autocv.data.Company;
import autocv.data.JobCredentials;
import autocv.parser.CoverLetterField;
import autocv.parser.CoverLetterFieldMap;
import autocv.ui.AutoCVPaths;

/**
 * HTML factory class for constructing the contents
 * of the custom HTML file used for printing to PDF
 * and completing the auto-generation process
 * 
 * @author Weston Greene
 * @since  1.0
 */
public class CoverLetterHTMLBuilder extends LinkedList<String> {
	
	/** Serial version associated with collection type */
	private static final long serialVersionUID = 1L;
	
	/** Reference field map for parsing HTML */
	private CoverLetterFieldMap clfMap;
	
	/**
	 * Reference applicant used for exchanging
	 * applicant fields with applicant data
	 */
	private Applicant applicant;
	
	/**
	 * Reference company used for exchanging
	 * company fields with company data
	 */
	private Company company;
	
	/**
	 * Reference job credentials used for
	 * exchanging job credential fields with
	 * job credential data
	 */
	private JobCredentials jobCred;
	
	/** Reference job title */
	private String jobTitle;
	
	/**
	 * Default constructor, initializing the
	 * reference applicant, company, job
	 * credentials, and job title
	 * 
	 * @param applicant reference applicant
	 * @param company	reference company
	 * @param jobCred	reference job credentials
	 * @param jobTitle	reference job title
	 */
	public CoverLetterHTMLBuilder( Applicant applicant,
			Company company, JobCredentials jobCred,
			String jobTitle ) {
		
		this.applicant = applicant;
		this.company = company;
		this.jobCred = jobCred;
		this.jobTitle = jobTitle;
		
		// Instantiate the field map
		clfMap = new CoverLetterFieldMap();
	}
	
	/**
	 * Constructs the contents of the custom HTML file;
	 * the contents of the reference base HTML file are
	 * first read in from disk; individual lines are parsed
	 * for fields which can be replaced by the data contained
	 * in the builder, and are exchanged before being immediately
	 * re-printed to the custom HTML file
	 * 
	 * @throws Exception upon runtime I/O exception
	 */
	public void build() throws Exception {
		
		// Scanner containing a File initialized with
		// the Path for the base HTML file to read
		Scanner htmlScan = new Scanner( new File( CoverLetterHTMLBuilder.
				class.getClass().getResource( AutoCVPaths.HTML_BASE_PATH ).
				toURI() ) );
		
		// Field reference to parse next within
		// the base file, initialized with the
		// INTIIAL_FIRST_NAME field at the start
		// of the file
		CoverLetterField currentField = CoverLetterField.INITIAL_FIRST_NAME;
		
		while( htmlScan.hasNextLine() ) {
			String htmlLine = htmlScan.nextLine();
			
			// If fields remain to be parsed within
			// the base file, and the current line
			// contains the current field to parse,
			// replace the field name in the line with
			// the data stored for that field
			if( currentField != null && htmlLine.
					contains( currentField.toString() ) ) {
				
				String replace = null;
				
				switch( currentField ) {
					case INITIAL_FIRST_NAME :
					case SIG_FIRST_NAME :
					case FIRST_NAME :
						replace = applicant.getFirstName();
						break;
					case SIG_MIDDLE_INITIAL :
					case MIDDLE_INITIAL :
						String mi = applicant.getMiddleInitial();
						replace = mi == null ? "" : " " + mi + ".";
						break;
					case INITIAL_LAST_NAME :
					case SIG_LAST_NAME :
					case LAST_NAME :
						replace = " " + applicant.getLastName();
						break;
					case EMAIL :
						replace = applicant.getEmail();
						break;
					case PRESENT_ADDRESS_1 :
						replace = applicant.getPresentAddress().
							getStreetAddress();
						break;
					case PRESENT_ADDRESS_2 :
						replace = applicant.getPresentAddress().
							getCityStateZip();
						break;
					case PRESENT_ADDRESS_3 :
						replace = applicant.getPresentAddress().
							getPOBoxRoom();
						break;
					case PERMANENT_ADDRESS_1 :
						replace = applicant.getPermanentAddress().
							getStreetAddress();
						break;
					case PERMANENT_ADDRESS_2 :
						replace = applicant.getPermanentAddress().
							getCityStateZip();
						break;
					case PERMANENT_ADDRESS_3 :
						replace = applicant.getPermanentAddress().
							getPOBoxRoom();
						break;
					case GITHUB_LINK :
						replace = applicant.getGitHubLink();
						break;
					case LINKEDIN_LINK :
						replace = applicant.getLinkedInLink();
						break;
					case PHONE_NUMBER :
						replace = applicant.getPhone();
						break;
					case DATE :
						// Format accounts for longest month name
						// (September) and prints double-digit
						// date numbers
						//
						// EX: September 05, 1996
						replace = new SimpleDateFormat( "MMMMMMMMM dd, YYYY" ).
							format( new Date() );
						break;
					case INITIAL_COMPANY_NAME :
					case LAST_COMPANY_NAME :
						replace = company.getName();
						break;
					case COMPANY_ADDRESS_1 :
						replace = company.getAddress().
							getStreetAddress();
						break;
					case COMPANY_ADDRESS_2 :
						replace = company.getAddress().
							getCityStateZip();
						break;
					case IS_RISING :
						boolean rise = applicant.isRising();
						replace = rise ? " rising" : "";
						break;
					case SCHOOL_YEAR :
						replace = " " + applicant.getSchoolYear().
							toString().toLowerCase();
						break;
					case JOB_TITLE :
						replace = jobTitle;
						break;
					case JOB_SEASON :
						replace = " " + jobCred.getJobSeason().
							toString().toLowerCase();
						break;
					case JOB_YEAR :
						replace = " " + jobCred.getYear();
						break;
					case JOB_TYPE :
						replace = jobCred.getJobType().
							toString().toLowerCase();
						break;
					default :
						// TODO: Implement Logger
						System.out.println( "Invalid field" );
				}
				
				// Replace the field in the line of HTML
				// with the data associated with the
				// current field
				htmlLine = htmlLine.replace( currentField.toString(), replace );
				
				// Update the current field to the next
				// field to parse within the file, as
				// retrieved from the field map
				currentField = clfMap.get( currentField );
			}
			
			// Add the HTML line to the builder
			add( htmlLine );
		}
		
		// Close the base HTML file Scanner
		// and return its resources
		htmlScan.close();
	}
}
