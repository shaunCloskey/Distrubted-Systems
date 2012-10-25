/*
 The example demonstrate a simple spinner

 It allows the user to choose the time
 In addition, user can choose the am/pm
*
*/
 
 
import javax.swing.*;
import java.awt.event.*;


public class Spinner extends JFrame
{
    private JPanel panel;
    private JButton ok;
    private JSpinner spinner;
    public Spinner()
    {
        // Sets the title of the window
        setTitle("JSpinner Window");
        // Sets the size of the window
        setSize(300, 200);
        // Sets the start location of the window
        setLocationRelativeTo(null);
        // Sets the action when a window is close
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Creates a spinner with initial, min, and max value
        spinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        // Creates a OK button
        ok = new JButton("OK");
        // Creates an action listener for the button
        ok.addActionListener(new okButtonListener());
        // Creates a panel
        panel = new JPanel();
        // Adds the components to the panel
        panel.add(spinner);
        panel.add(ok);
        // Adds the panel to the content pane
        add(panel);
        // Sets the visibility of the window
        setVisible(true);
    }
    private class okButtonListener implements ActionListener
    {  
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource()==ok)
            {
                Object value = spinner.getValue();
                JOptionPane.showMessageDialog(null, "You have selected " + value);
            }
        }
    }
    public static void main(String[] args)
    {
        new Spinner();
    }
}