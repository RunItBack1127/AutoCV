package autocv.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.function.UnaryOperator;

import autocv.data.Address;
import autocv.data.Applicant;
import autocv.data.Company;
import autocv.data.JobCredentials;
import autocv.data.JobSeason;
import autocv.data.JobType;
import autocv.data.SchoolYear;
import autocv.html.CoverLetterHTMLBuilder;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * User interface containing and wrapping the mutable
 * and non-mutable functionality of the AutoCV program;
 * the UI is divided into three main stages which the
 * client can traverse in order to customize the data
 * contained in their auto-generated cover letter
 * 
 * <li><b>Main Panel</b> - contains field for managing the
 * company data portion of the cover letter</li>
 * <li><b>Job Credentials Panel</b> - mutable fields for
 * specifying the start date and job type associated with
 * the letter</li>
 * <li><b>Applicant Info Panels</b> - used for modifying the
 * applicant info stored programatically and the info stored
 * on the config file attached with the program</li>
 * 
 * @author Weston Greene
 * @since  1.0
 */
public class AutoCVGUI extends Application {
	
	/**
	 * Collection containing all available style
	 * sheets within the CSS directory attached
	 * with the program
	 * 
	 * For efficiency, all of the sheets within this
	 * list are assigned to each newly instantiated
	 * Stage, regardless if the Stage will require all
	 * of the files
	 * 
	 * In other words, the style sheets are pre-loaded
	 * programatically and used as needed by the
	 * individual Stages upon instantiation, rather than
	 * loaded as each Stage requires them
	 */
	private static List<String> stylesheets;
	
	/**
	 * Applicant information as stored on the config
	 * file attached with the program; this Applicant
	 * is initialized upon instantiation of the UI
	 * class with the data stored on the file, and is
	 * updated upon the client modifying and saving
	 * any changes within the config applicant info
	 * panel
	 */
	private static Applicant diskAppInfo;
	
	/**
	 * Applicant info as stored programatically for use
	 * within the auto-generated cover letter; this
	 * Applicant is initialized with the data stored
	 * on the attached config file, and is updated
	 * upon the client modifying and saving any changes
	 * within the main applicant info panel
	 */
	private static Applicant saveAppInfo;
	
	/**
	 * Job credentials associated with the job used
	 * with the cover letter; these credentials are
	 * updated upon the user modifying and saving any
	 * changes within the job credentials panel
	 */
	private static JobCredentials saveJobCred;
	
	/**
	 * Current applicant info stage, displaying either
	 * the main or config applicant info panels;
	 * this reference is used for closing the stage
	 * after the client presses the save button
	 */
	private static Stage appInfoStage;
	
	/**
	 * Specifies which path to the Chrome executable
	 * to use internally for the given system
	 * 
	 * Systems utilizing an older version of 64-bit
	 * Google Chrome will store their executable
	 * within the x86 Program Files directory, while
	 * recent versions ( Chrome v85.0, August 2020 )
	 * include their executable within the standard
	 * Program Files directory
	 * 
	 * This flag determines which directory the
	 * conversion process for Headless Chrome should
	 * use; failure to locate either of the two possible
	 * Chrome directories during UI initialization will
	 * result in an error dialog message
	 */
	private static boolean usex86Path;
	
	/**
	 * Runs the UI program with the specified arguments;
	 * no arguments are expected to be run with this
	 * UI class, and any provided will be ignored
	 * 
	 * @param args program arguments
	 */
	public static void main( String[] args ) {
		launch( args );
	}
	
	/**
	 * Initialization routine for the UI
	 * 
	 * <li>Sets the x86 flag boolean if necessary and
	 * accesses one of the two valid Chrome directories,
	 * or displays the error dialog message</li>
	 * <li>Loads all typefaces and stylesheets</li>
	 * <li>Instantiates and displays the Main Panel</li>
	 * 
	 * @param  mainStage main Stage used to display Main Panel
	 * @throws Exception upon runtime exception 
	 */
	@Override
	public void start( Stage mainStage ) throws Exception {
		
		if( !new File( AutoCVPaths.CHROME_DEFAULT_PATH ).exists() ) {
			if( !( usex86Path = new File( AutoCVPaths.CHROME_X86_PATH ).
					exists() ) ) {
				
				Alert noChrome = new Alert( AlertType.NONE,
						"This application failed to detect a valid Google"
						+ " Chrome installation on the current system, or "
						+ "cannot access one of the given directories:\n\n"
						+ AutoCVPaths.CHROME_DEFAULT_PATH + "\n"
						+ AutoCVPaths.CHROME_X86_PATH + "\n"
						+ "\nPlease download any version of Chrome, check "
						+ "your program directories, or update your "
						+ "system privileges.",
						ButtonType.OK );
				noChrome.setTitle( "Google Chrome Not Found" );
				noChrome.show();
				return;
			}
		}
		
		// Register the typefaces within the FONTS
		// directory, and pre-load the stylesheets
		// from the CSS directory
		loadTypeFaces();
		stylesheets = loadStylesheets();
		
		// File containing the Path to the config file
		File configFile = new File( AutoCVGUI.class.getClass().
				getResource( AutoCVPaths.APP_DATA_PATH ).
				toURI() );
		
		// If the config file already exists,
		// instantiate and display the Main Panel;
		// otherwise, display the config applicant
		// info panel
		if( configFile.exists() ) {
			
			// Load in the applicant info from disk
			// and initialize both the disk and save
			// Applicants with the data
			diskAppInfo = loadApplicantInfo();
			saveAppInfo = diskAppInfo;
			
			// Display the Main Panel
			Scene mainScene = new Scene( new MainPanel() );
			mainScene.getStylesheets().setAll( stylesheets );
			mainStage.setScene( mainScene );
			mainStage.show();
		}
		else {
			// Display the config applicant panel,
			// and initialize the info stage reference
			// for closing
			appInfoStage = new Stage();
			
			// Anonymous version of the config applicant
			// info panel, which commits the data provided
			// by the client to the new config file, and
			// then instantiates and displays the Main Panel
			Pane configPane = new ConfigApplicantInfoPanel( true ) {
				
				@Override
				public void clickSaveButton() {
					
					// Saves the data to the config file
					super.clickSaveButton();
					
					// Initializes the save Applicant with
					// the new contents of the disk Applicant
					saveAppInfo = diskAppInfo;
					
					// Display the Main Panel
					Scene mainScene = new Scene( new MainPanel() );
					mainScene.getStylesheets().setAll( stylesheets );
					mainStage.setScene( mainScene );
					mainStage.show();
				}
			};
			Scene configScene = new Scene( configPane );
			configScene.getStylesheets().setAll( stylesheets );
			appInfoStage.setScene( configScene );
			
			appInfoStage.show();
		}
		
		// Default job credentials, assuming the
		// associated job is scheduled as an
		// internship for the upcoming summer of
		// the current year
		//
		// TODO: If the summer season of the
		// current year has passed, should
		// default to summer of next application
		// year? Next season of current year if
		// applicable?
		saveJobCred = new JobCredentials( JobType.INTERNSHIP,
				JobSeason.SUMMER, new SimpleDateFormat( "YYYY" ).
				format( new Date() ) );
	}
	
	/**
	 * Loads in the typefaces and fonts as listed
	 * within the FONTS directory; all registered
	 * fonts are removed from the system font
	 * registry after exiting the program
	 * 
	 * @throws Exception upon runtime I/O exception,
	 * 		   or a font within the FONTS directory
	 * 		   is not a supported type
	 */
	private static final void loadTypeFaces()
		throws Exception {
		
		// File containing Path to FONTS directory
		File fontsDir = new File( AutoCVGUI.class.
				getClass().getResource( AutoCVPaths.
				FONTS_PATH ).toURI() );
		
		// For all fonts within the directory,
		// register each font with the system
		// graphics environment
		for( File fontFile : fontsDir.listFiles() ) {
			
			// If the retrieved font file is not a supported
			// font type, throw an exception
			if( Font.loadFont( AutoCVGUI.class.getClass().
				getResourceAsStream( AutoCVPaths.FONTS_PATH.
						concat( fontFile.getName() ) ), 1.0f ) == null ) {
				throw new FileNotFoundException( "Invalid font format" );
			}
		}
	}
	
	/**
	 * Loads in the style sheets as listed in the
	 * CSS directory; this routine serves as a
	 * pre-loader during UI initialization, and
	 * loads all of the sheets rather than only
	 * the sheets requested by each individual
	 * Stage
	 * 
	 * @return list of URL paths to each style sheet
	 * @throws Exception upon runtime I/O exception
	 */
	private static final List<String> loadStylesheets()
		throws Exception {
		
		List<String> sheets = new LinkedList<>();
		
		File sheetsDir = new File( AutoCVGUI.class.
				getClass().getResource( AutoCVPaths.
				CSS_PATH ).toURI() );
		
		for( File sheetFile : sheetsDir.listFiles() ) {
			sheets.add( sheetFile.toURI().
					toURL().toExternalForm() );
		}
		return sheets;
	}
	
	/**
	 * Loads the applicant info from the config file
	 * during UI initialization
	 * 
	 * @return Applicant containing data from the
	 * 		   config file
	 * @throws Exception upon runtime I/O exception
	 */
	private static final Applicant loadApplicantInfo()
		throws Exception {
		
		/**
		 * Scanner wrapping a File containing
		 * the Path to the config file, used
		 * for reading in applicant data
		 */
		Scanner appScan = new Scanner( new File( AutoCVGUI.
				class.getClass().getResource( AutoCVPaths.
				APP_DATA_PATH ).toURI() ) );
		
		// Reads in the name, middle initial,
		// email, GitHub link, LinkedIn link,
		// phone number, school year, and
		// is rising data
		String name = appScan.nextLine();
		String mi = appScan.nextLine();
		String email = appScan.nextLine();
		String git = appScan.nextLine();
		String lin = appScan.nextLine();
		String phone = appScan.nextLine();
		SchoolYear year = SchoolYear.valueOf( appScan.nextLine() );
		boolean rise = Boolean.parseBoolean( appScan.nextLine() );
		
		// Present address parsing
		String sa1 = appScan.nextLine();
		String csz1 = appScan.nextLine();
		String rn1 = appScan.nextLine();
		Address add1 = new Address( sa1, csz1, rn1 );
		
		// Permanent address parsing
		String sa2 = appScan.nextLine();
		String csz2 = appScan.nextLine();
		String rn2 = appScan.nextLine();
		Address add2 = new Address( sa2, csz2, rn2 );
		
		// Closes and returns the resources utilized
		// by the Scanner
		appScan.close();
		
		// Return the Applicant with the parsed data
		return new Applicant( name, mi, email, git, lin,
			phone, year, rise, add1, add2  );
	}
	
	private static class MainPanel extends VBox {
		
		private static final int VIRTUAL_TIME_BUDGET = 50;
		
		/**
		 * Menu item storing the contents of the company
		 * currently displayed on the main panel; this
		 * item is only active and selectable if the
		 * company name, street address, and city/state/zip
		 * fields contain characters
		 * 
		 * Company data is only saved locally for each
		 * instance of the UI class - that is no saved
		 * data is stored to disk for retrieval upon
		 * relaunch of the program
		 */
		@FXML
		private MenuItem saveCompany;
		
		/**
		 * Menu used for storing the company data saved
		 * by the client; this menu is activated upon
		 * saving at least one company 
		 */
		@FXML
		private Menu loadCompany;
		
		/**
		 * Text field used for storing the name of
		 * the company associated with the job on
		 * the cover letter
		 */
		@FXML
		private TextField companyNameField;
		
		/**
		 * Text field storing the street address of
		 * the company; no error checking is done
		 * to assess the validity of the address
		 * 
		 * TODO: Assess validity of addresses
		 */
		@FXML
		private TextField companySAField;
		
		/**
		 * Text field storing the city, state,
		 * and ZIP code data of the company; no
		 * error checking is done to assess the
		 * validity of the address
		 */
		@FXML
		private TextField companyCSZField;
		
		/**
		 * Text field for storing the job title
		 * of the job associated with the cover
		 * letter
		 */
		@FXML
		private TextField jobTitleField;
		
		/**
		 * Clears all fields, resetting the content
		 * of the main panel; active only if there
		 * is at least one character in any of the
		 * fields
		 */
		@FXML
		private Button refreshBtn;
		
		/**
		 * Generates the cover letter using all data
		 * preferences; active only if all fields contain
		 * at least one character
		 */
		@FXML
		private Button generateBtn;
		
		/**
		 * Check box used for specifying if the
		 * cover letter should be opened for viewing
		 * after generation; by default, this box is
		 * checked
		 */
		@FXML
		private CheckBox openPDFBox;
		
		/**
		 * Main menu bar present at the top left of
		 * the main panel; contains options for editing
		 * the applicant info and job credentials, and
		 * for saving and loading companies
		 */
		@FXML
		private MenuBar mainMenu;
		
		/**
		 * List containing the names of all
		 * saved companies
		 * TODO: Find more efficient data structure
		 * 		 for large number of customers saved
		 * 		 by the client
		 */
		private List<String> companies;
		
		/**
		 * Determines if the current company displayed
		 * on the main panel is already contained within
		 * the saved companies collection
		 */
		private BooleanProperty isDuplicateCompany;
		
		/**
		 * Determines if the main panel is currently
		 * generating a cover letter; disables all fields,
		 * menu navigation, and reset functionality while
		 * generating
		 */
		private BooleanProperty isGenerating;
		
		/**
		 * Default constructor, initializing the
		 * internal company collection, the boolean
		 * properties, and calls the associated
		 * FXML loader
		 */
		public MainPanel() {
			super();
			
			companies = new LinkedList<>();
			isDuplicateCompany = new SimpleBooleanProperty();
			isGenerating = new SimpleBooleanProperty();
			
			FXMLLoader mp = new FXMLLoader( AutoCVGUI.
					class.getClass().getResource( AutoCVPaths.
					MAIN_LAYOUT_PATH ) );
			mp.setRoot( this );
			mp.setController( this );
			
			try {
				mp.load();
			} catch( Exception ex ) {
				// TODO: Change to print to log file
				ex.printStackTrace();
			}
		}
		
		/**
		 * Binds the save and load company menu items,
		 * the refresh button, and the generate button
		 * to their appropriate activation protocols,
		 * and default checks the open PDF checkbox
		 */
		@FXML
		private void initialize() {
			
			// If the name within the current company
			// name field changes, check for duplicate
			// company ( a company with the same name )
			// within the saved companies collection
			StringProperty nameProp = companyNameField.textProperty();
			nameProp.addListener( ( t, old, n ) -> {
				String name = nameProp.get();
				if( !name.isEmpty() ) {
					isDuplicateCompany.set( companies.
						contains( name ) );
				}
			});
			
			// The save company menu item is disabled
			// if any of the fields, barring the job
			// title field, are empty or the current
			// company is a duplicate of one already
			// saved
			saveCompany.disableProperty().
				bind( companyNameField.textProperty().
				isEmpty().or( companySAField.textProperty().
				isEmpty().or( companyCSZField.textProperty().
				isEmpty() ) ).or( isDuplicateCompany ) );
			
			// The refresh button is disabled if all
			// of the fields are already empty, or the
			// panel is generating a cover letter
			refreshBtn.disableProperty().bind( companyNameField.
				textProperty().isEmpty().and( companySAField.
				textProperty().isEmpty().and( companyCSZField.
				textProperty().isEmpty().and( jobTitleField.
				textProperty().isEmpty() ) ) ).or( isGenerating ) );
			
			// The generate button is disabled if any
			// of the fields are empty, or the panel
			// is generating
			generateBtn.disableProperty().bind( companyNameField.
				textProperty().isEmpty().or( companySAField.
				textProperty().isEmpty().or( companyCSZField.
				textProperty().isEmpty().or( jobTitleField.
				textProperty().isEmpty() ) ) ).or( isGenerating ) );
			
			// If currently generating, disable all panel
			// components; otherwise, enable all panel
			// components
			isGenerating.addListener( ( t, old, n ) -> {
				
				boolean gen = isGenerating.get();
				
				companyNameField.setDisable( gen );
				companySAField.setDisable( gen );
				companyCSZField.setDisable( gen );
				jobTitleField.setDisable( gen );
				mainMenu.setDisable( gen );
				openPDFBox.setDisable( gen );
			});
			
			// Default check the open PDF checkbox
			openPDFBox.setSelected( true );
		}
		
		/**
		 * Generates the cover letter using the preferences
		 * specified throughout the program; while generating,
		 * all other panel functionality is disabled
		 */
		@FXML
		private void generateCoverLetter() {
			
			// Enable generating
			isGenerating.set( true );
			
			// Compile the company data on the panel
			String sa = companySAField.getText();
			String csz = companyCSZField.getText();
			
			Address address = new Address( sa, csz );
			String name = companyNameField.getText();
			
			Company company = new Company( name, address );
			
			// Instantiate a new builder for constructing
			// the reference HTML file, providing the saved
			// applicant info, the compiled company, the
			// saved job credentials, and the job title
			//
			// TODO: For efficiency, do not instantiate
			// a new builder if none of the fields have
			// been modified
			CoverLetterHTMLBuilder htmlBuilder =
					new CoverLetterHTMLBuilder( saveAppInfo, company,
					saveJobCred, jobTitleField.getText() );
			
			try {
				// Build the HTML, replacing the internal
				// fields with the provided data
				htmlBuilder.build();
				
				// Store the path to the Chrome executable,
				// accounting for the x86 flag
				final String chromeAppPath = usex86Path ?
						AutoCVPaths.CHROME_X86_PATH :
							AutoCVPaths.CHROME_DEFAULT_PATH;
				
				// Store the path to the reference HTML file
				// which to write the contents of the builder
				String cvHTMLPath = AutoCVGUI.class.getClass().
						getResource( AutoCVPaths.AUTO_GEN_HTML_PATH ).
						toExternalForm().substring( 6 );
				
				// Write the contents of the builder to the
				// HTML file
				Files.write( Paths.get( cvHTMLPath ), htmlBuilder );
				
				// Store the directory which to save the
				// generated cover letter PDF
				String saveDir = "C:/Users/" + System.getenv( "USERNAME" ) + "/Downloads/" +
					saveAppInfo.getFirstName() + "_" + saveAppInfo.getMiddleInitial() +
					"_" + saveAppInfo.getLastName() + "_Cover_Letter.pdf";
				
				// Run the Chrome executable with the --headless
				// flag, specifying to run the print to PDF
				// functionality with the given PDF save directory
				// and the reference HTML file
				ProcessBuilder gen = new ProcessBuilder( "cmd", "/c", "chrome", "--headless",
						"-virtual-time-budget=" + VIRTUAL_TIME_BUDGET, "--disable-gpu",
						"--run-all-compositor-stages-before-draw",
						"--print-to-pdf=\"" + saveDir + "\"",
						cvHTMLPath );
				
				// Provide the process with the path of the
				// Chrome executable
				gen.directory( new File( chromeAppPath ) );
				
				// Thread used for generating the cover
				// letter off of the JavaFX Application
				// Thread
				Thread genOpen = new Thread() {
					
					@Override
					public void run() {
						try {
							// Starts Chrome executable process,
							// and waits for the process to
							// terminate
							gen.start().waitFor();
							
							// After generation, if the client specifies
							// to open the PDF, use the Chrome executable
							// to display the file
							//
							// TODO: Use the system default PDF viewer
							if( openPDFBox.isSelected() ) {
								ProcessBuilder open = new ProcessBuilder( "cmd", "/c", saveDir );
								// open.directory( new File( chromeAppPath ) );
								open.start();
							}
						} catch( Exception ex ) {}
						
						// Disable generating
						isGenerating.set( false );
					}
				};
				genOpen.setDaemon( true );
				genOpen.start();
				
			} catch( Exception ex ) {
				
				// TODO: Set up logger
				
				ex.printStackTrace();
				isGenerating.set( false );
			}
		}
		
		/**
		 * Saves the current company data displayed on
		 * the main panel to the list of saved companies
		 */
		@FXML
		private void saveCompany() {
			
			// Compile the company data
			String sa = companySAField.getText();
			String csz = companyCSZField.getText();
			
			final Address address = new Address( sa, csz );
			String name = companyNameField.getText();
			final Company company = new Company( name, address );
			
			// Append the company name to the
			// list of saved companies
			companies.add( name );
			
			// Instantiate a wrapper menu item, store
			// the company in the item, and then append
			// the item to the load company menu
			MenuItem compMenuItem = new MenuItem( company.getName() );
			loadCompany.getItems().add( compMenuItem );
			
			// Activate the load company menu and
			// set the duplicate flag property to
			// true
			loadCompany.setDisable( false );
			isDuplicateCompany.set( true );
			
			// Upon clicking the menu item,
			// display the stored company data
			// on the main panel
			compMenuItem.setOnAction( e -> {
				companyNameField.setText( company.getName() );
				companySAField.setText( address.getStreetAddress() );
				companyCSZField.setText( address.getCityStateZip() );
			});
		}
		
		/** Clears all fields of the main panel */
		@FXML
		private void clickRefresh() {
			companyNameField.setText( "" );
			companySAField.setText( "" );
			companyCSZField.setText( "" );
			jobTitleField.setText( "" );
		}
		
		/**
		 * Instantiates and displays the main applicant
		 * info panel; all components of this panel are
		 * mutable barring the name field, which is
		 * set by the contents of the config file
		 */
		@FXML
		private void editMainAppInfo() {
			Scene appScene = new Scene( new MainApplicantInfoPanel() );
			appScene.getStylesheets().setAll( stylesheets );
			appInfoStage = new Stage();
			appInfoStage.setScene( appScene );
			
			appInfoStage.show();
		}
		
		/**
		 * Instantiates and displays the job
		 * credentials panel
		 */
		@FXML
		private void editJobCredentials() {
			Scene jcScene = new Scene( new JobCredentialsPanel() );
			jcScene.getStylesheets().setAll( stylesheets );
			Stage jcStage = new Stage();
			jcStage.setScene( jcScene );
			
			jcStage.show();
		}
		
		/**
		 * Instantiates the config applicant info
		 * panel; all components of this panel are
		 * mutable, including the name field
		 */
		@FXML
		private void editConfigAppInfo() {
			Scene appScene = new Scene( new ConfigApplicantInfoPanel( false ) );
			appScene.getStylesheets().setAll( stylesheets );
			appInfoStage = new Stage();
			appInfoStage.setScene( appScene );
			
			appInfoStage.show();
		}
	}
	
	/**
	 * Abstract panel class encapsulating all functionality
	 * associated with an applicant info panel, providing
	 * the fields and controls for modifying the data of
	 * an applicant and the operations for saving the data
	 * 
	 * @since 1.0
	 */
	private static abstract class ApplicantInfoPanel extends VBox {
		
		/**
		 * Saves the applicant data; depending on the
		 * implementing subclass, the save operation will
		 * either save the data programatically or modify
		 * the internal config file
		 */
		@FXML
		protected Button saveBtn;
		
		/** Text field for the name of the applicant */
		@FXML
		protected TextField nameField;
		
		/** Text field for the middle initial of the applicant */
		@FXML
		protected TextField middleInitialField;
		
		/** Text field for the email of the applicant */
		@FXML
		protected TextField emailField;
		
		/** Text field for the GitHub link of the applicant */
		@FXML
		protected TextField gitHubField;
		
		/** Text field for the LinkedIn link of the applicant */
		@FXML
		protected TextField linkedInField;
		
		/** Text field for the phone number of the applicant */
		@FXML
		protected TextField phoneField;
		
		/**
		 * Choice box to select the current school year
		 * of the applicant */
		@FXML
		protected ChoiceBox<SchoolYear> schoolYearBox;
		
		/**
		 * Check box determining if the applicant is rising
		 * for the specified year
		 */
		@FXML
		protected CheckBox isRisingBox;
		
		/**
		 * Text field for present street address of
		 * the applicant
		 */
		@FXML
		protected TextField presentSAField;
		
		/**
		 * Text field for present city/state/zip of
		 * the applicant
		 */
		@FXML
		protected TextField presentCSZField;
		
		/**
		 * Text field for present room number of
		 * the applicant
		 * 
		 * TODO: Add prompt texts to all text fields
		 */
		@FXML
		protected TextField presentRNField;
		
		/**
		 * Text field for the permanent street address
		 * of the applicant 
		 */
		@FXML
		protected TextField permanentSAField;
		
		/**
		 * Text field for the permanent city/state/zip
		 * of the applicant
		 */
		@FXML
		protected TextField permanentCSZField;
		
		/**
		 * Text field for the permanent P.O. Box address
		 * of the applicant
		 */
		@FXML
		protected TextField permanentPOBField;
		
		/**
		 * Default constructor, initializing no other
		 * state than specified in the constructor of
		 * the parent layout class
		 */
		public ApplicantInfoPanel() {
			super();
		}
		
		/**
		 * Abstract routine for handling saving the
		 * applicant data; the subclasses each
		 * implement this functionality differently
		 * 
		 * @throws Exception upon runtime exception
		 */
		@FXML
		public abstract void clickSaveButton();
		
		/**
		 * Converts the data specified in the fields and
		 * controls to an Applicant; this routine is used
		 * for determining when to activate and disable the
		 * save button ( and refresh button in the main panel
		 * subclass ), and for setting the Applicant fields
		 * internally
		 * 
		 * @return Applicant containing all field and
		 * 		   control data
		 */
		public Applicant convertToApplicant() {
			
			// Present address of applicant
			Address add1 = new Address( presentSAField.getText(),
					presentCSZField.getText(), presentRNField.getText() );
			// Permanent address of applicant
			Address add2 = new Address( permanentSAField.getText(),
					permanentCSZField.getText(), permanentPOBField.getText() );
			
			// Retrieves Applicant based on field text
			// and the compiled addresses
			return new Applicant( nameField.getText(),
				middleInitialField.getText(),
				emailField.getText(), gitHubField.getText(),
				linkedInField.getText(), phoneField.getText(),
				schoolYearBox.getValue(), isRisingBox.isSelected(),
				add1, add2 );
		}
	}
	
	/**
	 * Main applicant panel concrete implementation class,
	 * implementing the save operation as replacing the
	 * content of the saved applicant for the entire
	 * program, and appending a refresh button
	 * 
	 * @since 1.0
	 */
	public static final class MainApplicantInfoPanel
		extends ApplicantInfoPanel {
		
		/**
		 * Refreshes the fields of the panel to the
		 * data stored in the disk applicant
		 */
		@FXML
		private Button refreshBtn;
		
		/**
		 * Default constructor, initializing the
		 * panel layout with the internal FXML
		 * Loader
		 */
		public MainApplicantInfoPanel() {
			super();
			FXMLLoader maip = new FXMLLoader( AutoCVGUI.
					class.getClass().getResource( AutoCVPaths.
					MAIN_APP_INFO_PATH ) );
			maip.setRoot( this );
			maip.setController( this );
			
			try {
				maip.load();
			} catch( Exception ex ) {
				// TODO: Implement Logger
				ex.printStackTrace();
			}
		}
		
		/**
		 * Initializes all fields and controls with the
		 * content stored in the saved applicant; the
		 * name fields are disabled by default
		 */
		@FXML
		private void initialize() {
			nameField.setText( saveAppInfo.getName() );
			middleInitialField.setText( saveAppInfo.getMiddleInitial() );
			emailField.setText( saveAppInfo.getEmail() );
			gitHubField.setText( saveAppInfo.getGitHubLink() );
			linkedInField.setText( saveAppInfo.getLinkedInLink() );
			phoneField.setText( saveAppInfo.getPhone() );
			
			schoolYearBox.setItems( FXCollections.
					observableArrayList( SchoolYear.
					values() ) );
			schoolYearBox.setValue( saveAppInfo.getSchoolYear() );
			
			isRisingBox.setSelected( saveAppInfo.isRising() );
			
			Address add1 = saveAppInfo.getPresentAddress();
			presentSAField.setText( add1.getStreetAddress() );
			presentCSZField.setText( add1.getCityStateZip() );
			presentRNField.setText( add1.getPOBoxRoom() );
			
			Address add2 = saveAppInfo.getPermanentAddress();
			permanentSAField.setText( add2.getStreetAddress() );
			permanentCSZField.setText( add2.getCityStateZip() );
			permanentPOBField.setText( add2.getPOBoxRoom() );
			
			// For a change in each field in the panel,
			// determine if the save and refresh buttons
			// are activated or disabled
			ChangeListener<String> chs = new ChangeListener<String>() {

				@Override
				public void changed( ObservableValue<? extends String> str,
					String o, String n ) {
					
					// Compile the Applicant specified by the
					// field data of the panel
					Applicant app = convertToApplicant();
					
					// If the Applicant is equivalent to
					// the saved Applicant, disable the save
					// button; otherwise, activate it
					saveBtn.setDisable( app.equals( saveAppInfo ) );
					
					// If the Applicant is equivalent to
					// the disk Applicant, disable the
					// refresh button; otherwise, activate
					// it
					refreshBtn.setDisable( app.equals( diskAppInfo ) );
				}
			};
			
			nameField.textProperty().addListener( chs );
			middleInitialField.textProperty().addListener( chs );
			emailField.textProperty().addListener( chs );
			gitHubField.textProperty().addListener( chs );
			linkedInField.textProperty().addListener( chs );
			phoneField.textProperty().addListener( chs );
			
			schoolYearBox.valueProperty().addListener( ( t, old, n ) -> {
				Applicant app = convertToApplicant();
				saveBtn.setDisable( app.equals( saveAppInfo ) );
				refreshBtn.setDisable( app.equals( diskAppInfo ) );
			});
			
			isRisingBox.selectedProperty().addListener( ( t, old, n ) -> {
				Applicant app = convertToApplicant();
				saveBtn.setDisable( app.equals( saveAppInfo ) );
				refreshBtn.setDisable( app.equals( diskAppInfo ) );
			});
			
			presentSAField.textProperty().addListener( chs );
			presentCSZField.textProperty().addListener( chs );
			presentRNField.textProperty().addListener( chs );
			permanentSAField.textProperty().addListener( chs );
			permanentCSZField.textProperty().addListener( chs );
			permanentPOBField.textProperty().addListener( chs );
		}
		
		/**
		 * Replaces the current save applicant with
		 * an Applicant encapsulating the contents
		 * of the current panel
		 */
		@Override
		public void clickSaveButton() {
			// Replace the current saved applicant,
			// and disable the save button
			saveAppInfo = convertToApplicant();
			saveBtn.setDisable( true );
			
			// Remove the focus from any of the
			// components in the panel
			this.requestFocus();
		}
		
		/**
		 * Reverts the data in each of the fields
		 * to the data stored in the disk applicant;
		 * in other words, loads the data from the
		 * current config file and displays it on
		 * the panel
		 */
		@FXML
		public void clickRefreshButton() {
			nameField.setText( diskAppInfo.getName() );
			middleInitialField.setText( diskAppInfo.getMiddleInitial() );
			emailField.setText( diskAppInfo.getEmail() );
			gitHubField.setText( diskAppInfo.getGitHubLink() );
			linkedInField.setText( diskAppInfo.getLinkedInLink() );
			phoneField.setText( diskAppInfo.getPhone() );
			
			schoolYearBox.setValue( diskAppInfo.getSchoolYear() );
			
			isRisingBox.setSelected( diskAppInfo.isRising() );
			
			Address add1 = diskAppInfo.getPresentAddress();
			presentSAField.setText( add1.getStreetAddress() );
			presentCSZField.setText( add1.getCityStateZip() );
			presentRNField.setText( add1.getPOBoxRoom() );
			
			Address add2 = diskAppInfo.getPermanentAddress();
			permanentSAField.setText( add2.getStreetAddress() );
			permanentCSZField.setText( add2.getCityStateZip() );
			permanentPOBField.setText( add2.getPOBoxRoom() );
			
			// Disable the refresh button
			refreshBtn.setDisable( true );
			
			// Remove the focus from any of the
			// components on the current panel
			this.requestFocus();
		}
	}
	
	/**
	 * Config mutable applicant panel implementation class;
	 * this class implements no further functionality in
	 * terms of layout, however the save operation overwrites
	 * the contents of the config file and the stored
	 * disk applicant
	 */
	public static class ConfigApplicantInfoPanel
		extends ApplicantInfoPanel {
		
		/**
		 * Flag to determine if this panel is in
		 * set up mode; the panel will not close
		 * upon clicking the save button when not
		 * in set up mode
		 */
		private boolean setUpMode;
		
		/**
		 * Default constructor, initializing the
		 * set up mode of the panel and instantating
		 * the FXML loader
		 * 
		 * @param setUpMode set up mode flag
		 */
		public ConfigApplicantInfoPanel( boolean setUpMode ) {
			super();
			
			this.setUpMode = setUpMode;
			
			FXMLLoader caip = new FXMLLoader( AutoCVGUI.
					class.getClass().getResource( AutoCVPaths.
					CONFIG_APP_INFO_PATH ) );
			caip.setRoot( this );
			caip.setController( this );
			
			try {
				caip.load();
			} catch( Exception ex ) {
				ex.printStackTrace();
			}
		}
		
		/**
		 * If the panel is not in set up mode, instantiates
		 * all of the fields with the data contained in the
		 * disk applicant
		 */
		@FXML
		private void initialize() {
			schoolYearBox.setItems( FXCollections.
					observableArrayList( SchoolYear.
					values() ) );
			
			if( !setUpMode ) {
				nameField.setText( diskAppInfo.getName() );
				middleInitialField.setText( diskAppInfo.getMiddleInitial() );
				emailField.setText( diskAppInfo.getEmail() );
				gitHubField.setText( diskAppInfo.getGitHubLink() );
				linkedInField.setText( diskAppInfo.getLinkedInLink() );
				phoneField.setText( diskAppInfo.getPhone() );
				
				schoolYearBox.setValue( diskAppInfo.getSchoolYear() );
				
				isRisingBox.setSelected( diskAppInfo.isRising() );
				
				Address add1 = diskAppInfo.getPresentAddress();
				presentSAField.setText( add1.getStreetAddress() );
				presentCSZField.setText( add1.getCityStateZip() );
				presentRNField.setText( add1.getPOBoxRoom() );
				
				Address add2 = diskAppInfo.getPermanentAddress();
				permanentSAField.setText( add2.getStreetAddress() );
				permanentCSZField.setText( add2.getCityStateZip() );
				permanentPOBField.setText( add2.getPOBoxRoom() );
				
				ChangeListener<String> chs = new ChangeListener<String>() {

					@Override
					public void changed( ObservableValue<? extends String> str,
						String o, String n ) {
						
						saveBtn.setDisable( convertToApplicant().equals( diskAppInfo ) );
					}
				};
				
				nameField.textProperty().addListener( chs );
				middleInitialField.textProperty().addListener( chs );
				emailField.textProperty().addListener( chs );
				gitHubField.textProperty().addListener( chs );
				linkedInField.textProperty().addListener( chs );
				phoneField.textProperty().addListener( chs );
				
				schoolYearBox.valueProperty().addListener( ( t, old, n ) -> {
					saveBtn.setDisable( convertToApplicant().equals( diskAppInfo ) );
				});
				
				isRisingBox.selectedProperty().addListener( ( t, old, n ) -> {
					saveBtn.setDisable( convertToApplicant().equals( diskAppInfo ) );
				});
				
				presentSAField.textProperty().addListener( chs );
				presentCSZField.textProperty().addListener( chs );
				presentRNField.textProperty().addListener( chs );
				permanentSAField.textProperty().addListener( chs );
				permanentCSZField.textProperty().addListener( chs );
				permanentPOBField.textProperty().addListener( chs );
			}
		}
		
		/**
		 * Overwrites the contents of the config file
		 * with the data stored in the fields of the
		 * applicant panel
		 */
		@Override
		public void clickSaveButton() {
			// Constructs list of data from the fields
			// and controls of the panel
			List<String> appInfo = Arrays.asList( nameField.getText(),
					middleInitialField.getText(), emailField.getText(),
					gitHubField.getText(), linkedInField.getText(),
					phoneField.getText(), String.valueOf( schoolYearBox.
					getValue() ), String.valueOf( isRisingBox.isSelected() ),
					presentSAField.getText(), presentCSZField.getText(),
					presentRNField.getText(), permanentSAField.getText(),
					permanentCSZField.getText(), permanentPOBField.getText() );
			
			try {
				// Write app info contents to config file
				Files.write( Paths.get( AutoCVGUI.class.getClass().
						getResource( AutoCVPaths.APP_DATA_PATH ).
						toExternalForm() ), appInfo,
						StandardCharsets.UTF_8,
						StandardOpenOption.CREATE );
			} catch( Exception ex ) {
				// TODO: Initialize Logger
			}
			
			// Replace disk applicant with Applicant
			// compiled from field data
			diskAppInfo = convertToApplicant();
			
			// If in set up mode, close the applicant
			// panel - this is used in the UI initialization
			// for displaying the Main Panel afterwards;
			// otherwise, disable the save button
			if( setUpMode ) {
				appInfoStage.close();
			}
			else {
				saveBtn.setDisable( true );
				
				// Remove the focus from all components
				// within the applicant panel
				this.requestFocus();
			}
		}
	}
	
	/**
	 * Job credentials panel class containing all controls
	 * and functionality for modifying the job season, job
	 * year, and job type for the job associated with the
	 * generated cover letter
	 * 
	 * @since 1.0
	 */
	public static class JobCredentialsPanel extends VBox {
		
		/**
		 * Replaces the saved job credentials with
		 * the data specified by the control data
		 */
		@FXML
		private Button saveBtn;
		
		/**
		 * Control specifying type of the cover
		 * letter job, either INTERNSHIP or CO-OP;
		 * defaults to INTERNSHIP
		 */
		@FXML
		private ChoiceBox<JobType> jobTypeBox;
		
		/**
		 * Control specifying the job season of
		 * the cover letter job, either SPRING,
		 * SUMMER, FALL, or WINTER; defaults to
		 * SUMMER
		 */
		@FXML
		private ChoiceBox<JobSeason> jobSeasonBox;
		
		/**
		 * Control specifying the year of operation
		 * of the cover letter job; defaults to the
		 * current year
		 */
		@FXML
		private TextField yearField;
		
		/**
		 * Default construction, initializing all
		 * parent state and instantiating the FXML
		 * loader
		 */
		public JobCredentialsPanel() {
			super();
			FXMLLoader jcp = new FXMLLoader( AutoCVGUI.
					class.getClass().getResource( AutoCVPaths.
					JOB_CRED_PATH ) );
			jcp.setRoot( this );
			jcp.setController( this );
			
			try {
				jcp.load();
			} catch( Exception ex ) {
				// TODO: Initialize Logger
				System.out.println( ex.getMessage() );
			}
		}
		
		/**
		 * Initializes the settings and displayed values
		 * of all controls
		 */
		@FXML
		private void initialize() {
			jobSeasonBox.setItems( FXCollections.
				observableArrayList( JobSeason.
				values() ) );
			jobTypeBox.setItems( FXCollections.
				observableArrayList( JobType.
				values() ) );
			
			jobSeasonBox.setValue( saveJobCred.getJobSeason() );
			jobTypeBox.setValue( saveJobCred.getJobType() );
			yearField.setText( saveJobCred.getYear() );
			
			// Specifies a change formatter which only
			// accepts valid years for the year field
			// (i.e. maximum of four digits)
			final UnaryOperator<Change> op = new UnaryOperator<Change>() {

				private static final int MAX_LENGTH = 4;
				
				@Override
				public Change apply( Change c ) {
					
					String text = c.getText();
					
					if( !text.isEmpty() ) {
						
						if( !Character.isDigit( text.charAt( 0 ) ) ) {
							c.setText( "" );
							return c;
						}
						else {
							if( c.getControlNewText().equals( "0" ) ) {
								c.setText( "" );
								return c;
							}
						}
						
						if( c.getControlNewText().length() > MAX_LENGTH ) {
							c.setText( "" );
							return c;
						}
					}
					return c;
				}
			};
			yearField.setTextFormatter( new TextFormatter<>( op ) );
			
			// On each control, for each change, determine
			// if the save button should be disabled or
			// activated; the save button is disabled if
			// the current job credentials match the
			// saved job credentials
			jobSeasonBox.valueProperty().addListener( ( t, old, n ) -> {
				saveBtn.setDisable( convertToJobCredentials().equals( saveJobCred ) );
			});
			
			jobSeasonBox.valueProperty().addListener( ( t, old, n ) -> {
				saveBtn.setDisable( convertToJobCredentials().equals( saveJobCred ) );
			});
			
			yearField.textProperty().addListener( ( t, old, n ) -> {
				saveBtn.setDisable( convertToJobCredentials().equals( saveJobCred ) );
			});
		}
		
		/**
		 * Replaces the current saved job
		 * credentials
		 */
		@FXML
		private void clickSave() {
			saveJobCred = convertToJobCredentials();
			saveBtn.setDisable( true );
		}
		
		/**
		 * Converts the control data to job credentials;
		 * this routine is used to determine the active
		 * or disable state of the save button, and for
		 * saving the credentials
		 * 
		 * @return JobCredentials encapsulating control data
		 */
		private JobCredentials convertToJobCredentials() {
			return new JobCredentials( jobTypeBox.getValue(),
					jobSeasonBox.getValue(),
					yearField.getText() );
		}
	}
}
