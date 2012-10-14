package hci;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main class of the program - handles display of the main window
 * @author Michal
 *
 */
public class ImageLabeller extends JFrame {
	/**
	 * some java stuff to get rid of warnings
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * main window panel
	 */
	JPanel appPanel = null;
	
	/**
	 * toolbox - put all buttons and stuff here!
	 */
	JPanel toolboxPanel = null;
	
	/**
	 * menu - has save, load, undo and redo 
	 */
	JMenuBar menuBar = null;
	
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
	public void setupGUI(String imageFilename) throws Exception {
		this.addWindowListener(new WindowAdapter() {
		  	public void windowClosing(WindowEvent event) {
		  		//here we exit the program (maybe we should ask if the user really wants to do it?)
		  		//maybe we also want to store the polygons somewhere? and read them next time
		  		System.out.println("Bye bye!");
		    	System.exit(0);
		  	}
		});

		this.setTitle("Label thing");
		
		//setup main window panel
		appPanel = new JPanel();
		this.setLayout(new BoxLayout(appPanel, BoxLayout.X_AXIS));
		this.setContentPane(appPanel);
		
		
		JPanel UappPanel = new JPanel( new BorderLayout());
		
		
        //Create and set up the image panel.
		imagePanel = new ImagePanel(imageFilename);
		imagePanel.setOpaque(true); //content panes must be opaque
		
        UappPanel.add(imagePanel, BorderLayout.WEST);
        
        //create a menu Panel
        JToolBar menuToolBar = new JToolBar();
        menuToolBar.setFloatable(false);
        
        ImageIcon saveIcon = new ImageIcon("./menuImages/floppy-disks.png");
        JButton save = new JButton(saveIcon);
        save.setBorder(new EmptyBorder(0 ,0, 0, 0));
        menuToolBar.add(save);
        
        UappPanel.add(menuToolBar, BorderLayout.NORTH);
        
       	//create toolbox panel
       	toolboxPanel = new JPanel();
       	toolboxPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
       	toolboxPanel.setLayout(new GridLayout(3, 1, 5, 5));
        
	    //Add button
		JButton newPolyButton = new JButton("New object");
		newPolyButton.setMnemonic(KeyEvent.VK_N);
		newPolyButton.setSize(50, 20);
		newPolyButton.setEnabled(true);
		newPolyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    	addNewPolygon();
			}
		});
		newPolyButton.setToolTipText("Click to add new object");
		
	
		toolboxPanel.add(newPolyButton);
	
		JButton editPolyButton = new JButton("Edit Current object");		
		editPolyButton.setSize(50, 20);
		editPolyButton.setEnabled(true);
		editPolyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
							
			}
		});
		editPolyButton.setToolTipText("click to edit the currently selected polygon/label.");
		
		toolboxPanel.add(editPolyButton);
		

		JButton deletePolyButton = new JButton("Delete Current object");		
		deletePolyButton.setSize(50, 20);
		deletePolyButton.setEnabled(true);
		deletePolyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
							
			}
		});
		deletePolyButton.setToolTipText("click to Delete the currently selected polygon/label.");
		
		toolboxPanel.add(deletePolyButton);


		//add toolbox to window
		UappPanel.add(toolboxPanel);
		
		appPanel.add(UappPanel);
		//display all the stuff
		this.pack();
	        this.setVisible(true);
	}
	
	/**
	 * Runs the program
	 * @param argv path to an image
	 */
	public static void main(String argv[]) {
		try {
			//create a window and display the image
			ImageLabeller window = new ImageLabeller();
			window.setupGUI("./images/U1003_0000.jpg");
		} catch (Exception e) {
			System.err.println("Image: " + "./images/U1003_0000.jpg");
			e.printStackTrace();
		}
	}
}