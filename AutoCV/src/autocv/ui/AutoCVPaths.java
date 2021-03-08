package autocv.ui;

/**
 * Paths interface detailing the locations of various
 * files and directories for different components of
 * the program
 * 
 * The UI utilizes the paths specified for I/O involving
 * loading the typefaces and style sheets, loading and
 * modifying the config file, loading the FXML for the
 * internal Stages, etc;
 * 
 * The HTML Builder class utilizes the base HTML path
 * for referencing the fields to replace with the
 * custom applicant data, and the final HTML path for
 * writing the custom HTML file to print to PDF
 * 
 * @author Weston Greene
 * @since  1.0
 */
public interface AutoCVPaths {
	
	/**
	 * Name of the program FONTS directory; used
	 * for listing all of the font files contained
	 * in the directory and registering each file
	 * with the system graphics environment
	 */
	public static final String FONTS_PATH = "/fonts/";
	
	/**
	 * Main Panel path to the FXML layout file
	 * located in the FXML directory
	 */
	public static final String MAIN_LAYOUT_PATH = "/fxml/MainPanel.fxml";
	
	/**
	 * Applicant Info Panel path to the FXML layout file
	 * located in the FXML directory; this panel is
	 * mutable barring the name field and has a refresh
	 * menu button
	 */
	public static final String MAIN_APP_INFO_PATH = "/fxml/MainApplicantInfoPanel.fxml";
	
	/**
	 * Config Applicant Info Panel path to the FXML layout
	 * file located in the FXML directory; this panel is
	 * mutable including the name field and only implements
	 * save functionality
	 */
	public static final String CONFIG_APP_INFO_PATH = "/fxml/ConfigApplicantInfoPanel.fxml";
	
	/** Job Credentials Panel path to the FXML layout file */
	public static final String JOB_CRED_PATH = "/fxml/JobCredentialsPanel.fxml";
	
	/**
	 * Path to the internal config file, storing the
	 * applicant data preferences of the client
	 */
	public static final String APP_DATA_PATH = "/app_data/config.info";
	
	/**
	 * Name of the program CSS directory, storing all
	 * of the style sheets associated with the Stages
	 * within the UI class
	 */
	public static final String CSS_PATH = "/css";
	
	/**
	 * Default Windows system path to the Chrome executable;
	 * for 64-bit editions of Chrome version 85.0 and higher,
	 * the executable will be located in this directory
	 */
	public static final String CHROME_DEFAULT_PATH = "C:/Program Files/Google/Chrome/Application";
	
	/**
	 * Secondary Windows system path to the Chrome executable;
	 * for 64-bit editions of Chrome prior to version 85.0,
	 * the executable will be located in this directory
	 */
	public static final String CHROME_X86_PATH = "C:/Program Files (x86)/Google/Chrome/Application";
	
	/** Path to the base cover letter HTML file */
	public static String HTML_BASE_PATH = "/cover_letter_data/cvbase.html";
	
	/**
	 * Path which to write the custom HTML and convert
	 * the document to PDF
	 */
	public static String AUTO_GEN_HTML_PATH = "/cover_letter_data/auto_gen_cover_letter.html";
}
