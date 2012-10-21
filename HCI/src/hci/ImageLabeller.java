package hci;
import hci.utils.Point;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Main class of the program - handles display of the main window
 * @author Michal
 *
 */
public class ImageLabeller extends JFrame{

	/**
	 * List of all objects
	 */
	ArrayList<ArrayList<Point>> polygonsList = new ArrayList<ArrayList<Point>>();

	/**
	 * Holds all points in one object
	 */
	ArrayList<Point> currentPolygon = new ArrayList<Point>();


	/**
	 * list of all the names given to each of the polygons
	 */
	ArrayList<String> polygonNames = new ArrayList<String>();

	/**
	 * some java stuff to get rid of warnings
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * main window panel
	 */
	JPanel appPanel = null;

	/**
	 * tool box - put all buttons and stuff here!
	 */
	JPanel toolboxPanel = null;
	
	
	/**
	 * contains all the names of the polygons
	 */
	JList nameList = null;

	/**
	 * image panel - displays image and editing area
	 */
	ImagePanel imagePanel = null;
	/**
	 * handles New Object button action
	 */
	public void addNewPolygon() {
		imagePanel.addNewPolygon();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		imagePanel.paint(g); //update image panel
	}
	/**
	 * sets up application window
	 * @param imageFilename image to be loaded for editing
	 * @throws Exception
	 */
	public void setupGUI(final String imageFilename) throws Exception {
		this.addWindowListener(new WindowAdapter() {
		  	public void windowClosing(WindowEvent event) {
				imagePanel.setOpaque(true); //content panes must be opaque
		  		
		  		int confirmExit=0;
		  		boolean save = false;
		  		//Choose which confirmation message to display
		  		if((imagePanel.currentPolygon).isEmpty()){
		  		String exitMessage = "Are you sure you want to exit?";
		  		confirmExit = JOptionPane.showConfirmDialog(new JFrame(), exitMessage, "Confirm Exit",JOptionPane.YES_NO_OPTION);
		  		}else{
		  			String exitMessage = "Do you want to save the current object before exiting?";
			  	confirmExit = JOptionPane.showConfirmDialog(new JFrame(), exitMessage, "Confirm Exit",JOptionPane.YES_NO_CANCEL_OPTION);	
		  		save = true;
		  		}
		  		if (((confirmExit < 2) && (save))||((!save) && (confirmExit == 0))){
		  			if(save && confirmExit ==0){
		  				imagePanel.addNewPolygon();
		  			}
		  			//write the objects to a file
		  			FileWriter filey = null;
					try {
						filey = new FileWriter("out.txt");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					PrintWriter out = new PrintWriter(filey);
		  			polygonsList = imagePanel.returnPolygons();
		  			polygonNames = imagePanel.returnPolyNames();
		  			
		  			out.println("#");
		  			int polyIndex = 0;
		  			out.println(polygonNames.get(polyIndex));
		  			boolean firstRun = true;
		  			
		  			for (ArrayList<Point> arrayCounter: polygonsList){
		  				if(firstRun){
		  					System.out.println("in the first run");
		  				}
		  				for(Point pointCounter: arrayCounter){
		  						out.println(new Integer(pointCounter.getX()).toString());
		  						out.println(new Integer(pointCounter.getY()).toString());
		  				}
		  				if(firstRun){
		  					System.out.println("finished printing the First points");
		  				}
		  				
		  				if(!firstRun){
		  					out.println("#");
		  					polyIndex++;
		  					if(polygonNames.size() > polyIndex)
		  					{
		  						out.println(polygonNames.get(polyIndex));
		  					}
		  				}
		  				firstRun = false;
		  			}
				out.close();

				//Close the program
		  		System.out.println("Bye bye!");
		  		System.exit(0);
		  		}else{
		  		//User has cancelled the exit.
		  		System.out.println("Exit aborted.");
		  		}

		  	}
		});

		//setup main window panel
		appPanel = new JPanel();
		this.setLayout(new BoxLayout(appPanel, BoxLayout.X_AXIS));
		this.setContentPane(appPanel);

        //Create and set up the image panel.
		imagePanel = new ImagePanel(imageFilename, polygonsList, polygonNames);
		imagePanel.setOpaque(true); //content panes must be opaque

        appPanel.add(imagePanel);

        //create toolbox panel
        toolboxPanel = new JPanel();
        
        //Add button
		JButton newPolyButton = new JButton("Save Object");
		newPolyButton.setMnemonic(KeyEvent.VK_N);
		newPolyButton.setSize(50, 20);
		newPolyButton.setEnabled(true);
		newPolyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    	addNewPolygon();
			}
		});
		newPolyButton.setToolTipText("Click to save outlined object");
		
		
		//add all the names of the polygons to a JList 
		String [] name = new String[polygonNames.size()];
		int polyIndex = 0;
		for(String internalName : polygonNames)
		{
			name[polyIndex] = internalName;
			polyIndex++;
		}
		nameList = new JList(name);
		nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		nameList.setLayoutOrientation(JList.VERTICAL);
		nameList.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(nameList);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
		toolboxPanel.add(newPolyButton);
		toolboxPanel.add(listScroller);

		//add toolbox to window
		appPanel.add(toolboxPanel);

		//display all the stuff
		this.pack();
        this.setVisible(true);
	}

	int readLines() throws IOException{
		//Just finds out how many lines are in the text file
		FileReader lineCountF = new FileReader("out.txt");
		BufferedReader lineCountB = new BufferedReader(lineCountF);
		String line = null;
		int lineNumber = 0;

		while ((line = lineCountB.readLine()) != null){
			lineNumber++;
		}
		lineCountB.close();
		return lineNumber;
	}


	public void getPolygons(ImageLabeller window) throws IOException{
		//This method reads the defined objects from the file
		int numberLines = window.readLines();
		//Is file empty?
		if(numberLines==0){
			return;
		}
		
		FileReader inFile = new FileReader("out.txt");
		BufferedReader in = new BufferedReader(inFile);
		int currentLine = 0;
		while(true){
		String xcoord = in.readLine();
		System.out.println(xcoord);
		currentLine++;
		//end of object
		if(xcoord.equals("#")){
			System.out.println("Object read.");
			polygonsList.add(currentPolygon);
			currentPolygon = new ArrayList<Point>();
			//end of file
			if(currentLine == numberLines){
				in.close();
				return;
			}
			
			xcoord = in.readLine();
			System.out.println("object name : " + xcoord);
			polygonNames.add(xcoord);
			currentLine++;
			
			
			
		//y coord comes next
		}else{
		String ycoord = in.readLine();
		currentLine++;
		Point tempPoint = new Point();
		tempPoint.setX(Integer.parseInt(xcoord));
		tempPoint.setY(Integer.parseInt(ycoord));
		//add these points to the current object
		currentPolygon.add(tempPoint);
		}
		}
	}

	/**
	 * Runs the program
	 * @param argv path to an image
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String argv[]) throws IOException{
		try {
			//create a window and display the image
			ImageLabeller window = new ImageLabeller();
			window.getPolygons(window);
			//Let the user confirm the app closing.
			window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			window.setTitle("Image Labeler");
			window.setupGUI("/afs/inf.ed.ac.uk/user/s09/s0930584/workspace/HCI/src/images/U1003_0000.jpg");
		} catch (Exception e) {
			System.err.println("Image: " + "/afs/inf.ed.ac.uk/user/s09/s0930584/workspace/HCI/src/images/U1003_0000.jpg");
			e.printStackTrace();
		}
	}
}