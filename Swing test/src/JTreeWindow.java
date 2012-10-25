/*
	This is a JTree program

*
*/

import javax.swing.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class JTreeWindow extends JFrame
{
	private JTree tree;
	private DefaultTreeModel model;
	private JLabel name;
	private JPanel panel;

	public JTreeWindow()
	{
		// Sets the title of the window

		setTitle("JTree Window");

		// Sets the size of the window

		setSize(300, 400);

		// Sets the start location of the window

		setLocationRelativeTo(null);

		// Sets the action when the window close

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Creates a panel

		panel = new JPanel();

		// Create a empty tree node

		DefaultMutableTreeNode root, Program_Files, Windows, System_32;

		// Creates the root directory

		root = new DefaultMutableTreeNode("C");

		// Adds program files, windows, and system 32 to the root directory

		Program_Files = create("Program Files", root);
		Windows = create("Program Files", root);
		System_32 = create("System 32", root);

		// Add files to the program file folder

		create("Winamp", Program_Files);
		create("JGrasp", Program_Files);
		create("Dev++", Program_Files);

		// Add files to the windows folder

		create("Window Media", Windows);
		create(".dll", Windows);
		create(".temp", Windows);

		// Add files to System 32

		create(".source", System_32);
		create("cookies", System_32);
		create("bugs", System_32);

		// Creates a tree that displays the tree that starts at the root node

		tree = new JTree(root);

		// Gets the model of the tree

		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		// Sets the number of rows visible in the display

		tree.setVisibleRowCount(11);

		// Create an even listener for the tree

		tree.addTreeSelectionListener(new TreeListener());

		// Add a scroll pane to the tree

		JScrollPane sp = new JScrollPane(tree);

		// Create a label

		name = new JLabel();

		// Add the scroll pane and label into the panel

		panel.add(name);
		panel.add(sp);

		// Add the panel to the content pane

		add(panel);

		// Sets the visibility of the window

		setVisible(true);

	}

	private DefaultMutableTreeNode create(String title, DefaultMutableTreeNode parent)
	{
		// Creates a tree node with a specified user object

		DefaultMutableTreeNode creating = new DefaultMutableTreeNode(title);

		// Adds it to the tree

		parent.add(creating);

		return creating;

	}

	private class TreeListener implements TreeSelectionListener
	{
		public void valueChanged(TreeSelectionEvent e)
		{
			// Gets the currently selected node

			Object getSelect = tree.getLastSelectedPathComponent();

			DefaultMutableTreeNode creating = (DefaultMutableTreeNode) getSelect;

			String title = (String)creating.getUserObject();

			name.setText(title);

		}
	}

	public static void main(String[] args)
	{
		new JTreeWindow();

	}
}

