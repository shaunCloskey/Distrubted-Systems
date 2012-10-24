package hci;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

class FileTree extends JPanel {
	
	String currentNode ="";

	
	JFrame frame = new JFrame("FileTree");
	JDialog dialog = new JDialog (frame, "fileTree");
	
	
  /** Construct a FileTree */
  public FileTree(File dir) {
    setLayout(new BorderLayout());

    // Make a tree list with all the nodes, and make it a JTree
    JTree tree = new JTree(addNodes(null, dir));   
    
    
    // Add a listener
    tree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode SelectedNode = (DefaultMutableTreeNode) e
            .getPath().getLastPathComponent();
        currentNode = "" + SelectedNode;
        System.out.println("You selected " + SelectedNode);
      }
    });

    JButton loadButton = new JButton("Load");
    loadButton.setMnemonic(KeyEvent.VK_N);
	loadButton.setSize(50, 20);
	loadButton.setEnabled(true);
	loadButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    	System.out.println("selected : " + currentNode);
		    	pullThePlug();
		}
	});
	loadButton.setToolTipText("Click to confirm the load");
	
	
    JButton cancelButton = new JButton("Cancel");
    cancelButton.setMnemonic(KeyEvent.VK_N);
	cancelButton.setSize(50, 20);
	cancelButton.setEnabled(true);
	cancelButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    	
		}
	});
	cancelButton.setToolTipText("Click to cancel load");
	
	
    // Lastly, put the JTree into a JScrollPane.
    JScrollPane scrollpane = new JScrollPane();
    scrollpane.getViewport().add(tree);
    add(BorderLayout.NORTH, scrollpane);
    
    //add the butons for load and cancel to the panel
    add(BorderLayout.EAST, loadButton);
    add(BorderLayout.WEST, cancelButton);
  }

  /** Add nodes from under "dir" into currentTop. Highly recursive. */
  DefaultMutableTreeNode addNodes(DefaultMutableTreeNode currentTop, File dir) {
    String currentPath = dir.getPath();
    DefaultMutableTreeNode currentDir = new DefaultMutableTreeNode(currentPath);
    if (currentTop != null) { // should only be null at root
      currentTop.add(currentDir);
    }
    
    Vector ol = new Vector();
    String[] tmp = dir.list();
    
    for (int i = 0; i < tmp.length; i++)
    	ol.addElement(tmp[i]);
    
    Collections.sort(ol, String.CASE_INSENSITIVE_ORDER);
    File f;
    Vector files = new Vector();
    
    // Make two passes, one for Dirs and one for Files. This is #1.
    for (int i = 0; i < ol.size(); i++) {
      String thisObject = (String) ol.elementAt(i);
      String newPath;
      if (currentPath.equals("."))
        newPath = thisObject;
      else
        newPath = currentPath + File.separator + thisObject;
      if ((f = new File(newPath)).isDirectory())
        addNodes(currentDir, f);
      else
        files.addElement(thisObject);
    }
    // Pass two: for files.
    for (int fnum = 0; fnum < files.size(); fnum++)
      currentDir.add(new DefaultMutableTreeNode(files.elementAt(fnum)));
    return currentDir;
  }

  public Dimension getMinimumSize() {
    return new Dimension(200, 400);
  }

  public Dimension getPreferredSize() {
    return new Dimension(200, 400);
  }

  
  public void pullThePlug() {
	  dialog.dispose();
  }
  
  /** Main: make a Frame, add a FileTree */
  public boolean load() {
    
    dialog.setForeground(Color.black);
    dialog.setBackground(Color.lightGray);
    System.out.println("performing a load");
    
    dialog.add(new FileTree(new File("../HCI/src/images")));
    
    dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
    dialog.pack();
    dialog.setVisible(true);
    dialog.setFocusableWindowState(true);

    return false;


  }
}