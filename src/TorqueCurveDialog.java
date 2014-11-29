import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*	Defines the popup dialog box that allows the user to choose
 *	various toque and saving the current valueset to file.
 */
public class TorqueCurveDialog extends JFrame
		implements ActionListener
{
	private JFrame frame;
	private JPanel mainPanel;
	private JPanel centerPanel;
	private JPanel pathPanel, cbPanel;
	private JPanel southPanel;
	private JComboBox<String> comboBox;
	private JTextField textField;
	private JButton button, browse;

	public static void main(String[] argv)
	{
		new TorqueCurveDialog();
	}

	public TorqueCurveDialog()
	{
		frame = this;
		frame.setResizable(false);
		frame.setTitle("Select Torque Curve");
		mainPanel = new JPanel();
		centerPanel = new JPanel();
		pathPanel = new JPanel();
		cbPanel = new JPanel();
		southPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		centerPanel.setLayout(new GridLayout(2, 0));
		cbPanel.setLayout(new FlowLayout());
		pathPanel.setLayout(new FlowLayout());
		southPanel.setLayout(new GridLayout(0, 2));
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		frame.add(mainPanel);
		
		String[] options = {"Briggs and Stratton OHV Intek 305, new", "Briggs and Stratton OHV Intek 305, 100 hours", "Custom..."};
		comboBox = new JComboBox<>(options);

		comboBox.addActionListener(this);
		cbPanel.add(comboBox);
		centerPanel.add(cbPanel);
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setPreferredSize(new Dimension(300, 20));
		browse = new JButton("...");
		browse.setPreferredSize(new Dimension(25, 20));
		browse.setEnabled(false);
		pathPanel.add(textField);
		pathPanel.add(browse);
		centerPanel.add(pathPanel);
		
		switch((int)Config.getInstance().getScalar(Config.TORQUE_CURVE_TYPE))
		{
			case Config.NEW_ENGINE:
				comboBox.setSelectedIndex(0);
			break;

			case Config.OLD_ENGINE:
				comboBox.setSelectedIndex(1);
			break;

			case Config.CUSTOM:
				comboBox.setSelectedIndex(2);
				textField.setEnabled(true);
				browse.setEnabled(true);
			break;

			default:
				comboBox.setSelectedIndex(0);
			break;
		}
		
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		
		button = new JButton("Accept");
		button.addActionListener(this);
		southPanel.add(button);
		button = new JButton("Cancel");
		button.addActionListener(this);
		southPanel.add(button);
		
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		switch(ae.getActionCommand())
		{
			case "comboBoxChanged":
				if(comboBox.getSelectedItem().equals("Custom..."))
				{
					textField.setEnabled(true);
					browse.setEnabled(true);
				}
				else
				{
					textField.setEnabled(false);
					browse.setEnabled(false);
				}
			break;

			case "Accept":
				switch((String)comboBox.getSelectedItem())
				{
					case "Briggs and Stratton OHV Intek 305, new":
						double[] rpm = new double[48];

						for(int i = 0 ; i < 48 ; i++)
						{
							rpm[i] = 1500 + i * 50;
						}
						
						double[] t = {15.185, 16.541, 18.439, 18.981, 19.252, 19.388, 19.523, 19.388, 19.252, 19.388, 19.523, 19.388, 19.252, 19.117, 18.981, 18.981, 19.117, 19.388, 19.388, 19.388, 19.388, 19.523, 19.794, 20.066, 20.337, 20.472, 20.472, 20.201, 19.794, 20.201, 20.744, 20.066, 19.252, 19.998, 20.201, 19.523, 18.981, 18.845, 18.642, 18.439, 18.303, 18.032, 17.896, 17.218, 16.947, 15.863, 13.693, 8.135};

						Config.getInstance().setVector(Config.TORQUE_CURVE, rpm, t);
					break;

					case "Briggs and Stratton OHV Intek 305, 100 hours":
						//do something
					break;

					case "Custom...":
						//do something
					break;

					default:
						System.err.println("Where'd that come from?");
				}
				frame.setVisible(false);
				frame.dispose();
			break;

			case "Cancel":
				frame.setVisible(false);
				frame.dispose();
			break;

			default:
				System.err.println("Where'd that come from?");
			break;
		}
	}
}
