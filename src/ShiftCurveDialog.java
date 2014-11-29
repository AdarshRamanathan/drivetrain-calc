import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*	Defines the popup dialog box that allows for editing of
 *	the CVT shift curve and saving the current valueset to file.
 */
public class ShiftCurveDialog extends JFrame
		implements ActionListener
{
	private JFrame frame;
	private JPanel mainPanel;
	private JPanel northPanel;
	private JPanel centerPanel;
	private JPanel southPanel;
	private JComboBox<String> comboBox;
	private JButton button;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel image;
	
	private String[][] data;

	public ShiftCurveDialog()
	{
		frame = this;
		frame.setResizable(false);
		frame.setSize(350, 300);
		frame.setTitle("Select Shift Curve");
		mainPanel = new JPanel();
		northPanel = new JPanel();
		centerPanel = new JPanel();
		southPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		southPanel.setLayout(new GridLayout(0, 2));
		mainPanel.add(northPanel, BorderLayout.NORTH);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		frame.add(mainPanel);
		
		String[] options = {"Linear", "Exponential", "Logarithmic", "Isodyne", "Custom"};
		comboBox = new JComboBox<>(options);
		
		switch((int)Config.getInstance().getScalar(Config.SHIFT_CURVE_TYPE))
		{
			case Config.LINEAR:
				comboBox.setSelectedIndex(0);
				image = new JLabel(new ImageIcon("../res/linear.png"));
			break;

			case Config.EXPONENTIAL:
				comboBox.setSelectedIndex(1);
				image = new JLabel(new ImageIcon("../res/exponential.png"));
			break;

			case Config.LOGARITHMIC:
				comboBox.setSelectedIndex(2);
				image = new JLabel(new ImageIcon("../res/log.png"));
			break;

			case Config.ISODYNE:
				comboBox.setSelectedIndex(3);
				image = new JLabel(new ImageIcon("../res/question.png"));
			break;
			
			//add a case for custom curves, eventually

			default:
				Config.getInstance().setScalar(Config.SHIFT_CURVE_TYPE, Config.LINEAR);
				comboBox.setSelectedIndex(0);
				image = new JLabel(new ImageIcon("../res/linear.png"));
			break;
		}

		comboBox.addActionListener(this);
		northPanel.add(comboBox);
		
		String[] colNames = {"RPM", "CVT Ratio"};
		data = new String[1024][2];
		table = new JTable(data, colNames);
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		table.setEnabled(false);
		table.setVisible(false);
		scrollPane.setEnabled(false);
		scrollPane.setVisible(false);
		
		mainPanel.add(image, BorderLayout.CENTER);
		image.setBounds(200, 200, 50, 50);
		image.setVisible(true);
		
		button = new JButton("Accept");
		button.addActionListener(this);
		southPanel.add(button);
		button = new JButton("Cancel");
		button.addActionListener(this);
		southPanel.add(button);

		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		switch(ae.getActionCommand())
		{
			case "comboBoxChanged":
				switch((String)comboBox.getSelectedItem())
				{
					case "Linear":
						scrollPane.setEnabled(false);
						scrollPane.setVisible(false);
						table.setEnabled(false);
						table.setVisible(false);
						image.setVisible(false);
						image = new JLabel(new ImageIcon("../res/linear.png"));
						mainPanel.add(image, BorderLayout.CENTER);
						revalidate();
						repaint();
					break;
	
					case "Exponential":
						scrollPane.setEnabled(false);
						scrollPane.setVisible(false);
						table.setEnabled(false);
						table.setVisible(false);
						image.setVisible(false);
						image = new JLabel(new ImageIcon("../res/exponential.png"));
						mainPanel.add(image, BorderLayout.CENTER);
						revalidate();
						repaint();
					break;
	
					case "Logarithmic":
						scrollPane.setEnabled(false);
						scrollPane.setVisible(false);
						table.setEnabled(false);
						table.setVisible(false);
						image.setVisible(false);
						image = new JLabel(new ImageIcon("../res/log.png"));
						mainPanel.add(image, BorderLayout.CENTER);
						revalidate();
						repaint();
					break;
	
					case "Isodyne":
						scrollPane.setEnabled(false);
						scrollPane.setVisible(false);
						table.setEnabled(false);
						table.setVisible(false);
						image.setVisible(false);
						image = new JLabel(new ImageIcon("../res/question.png"));
						mainPanel.add(image, BorderLayout.CENTER);
						revalidate();
						repaint();
					break;
	
					case "Custom":
						image.setVisible(false);
						scrollPane.setVisible(true);
						scrollPane.setEnabled(true);
						table.setVisible(true);
						table.setEnabled(true);
						mainPanel.add(scrollPane, BorderLayout.CENTER);
						revalidate();
						repaint();
						JOptionPane.showMessageDialog(frame, "Custom shift curves are not currently supported.");
					break;
				}
			break;

			case "Accept":
				switch((String)comboBox.getSelectedItem())
				{
					case "Linear":
						Config.getInstance().generateShiftCurve(Config.LINEAR);
					break;
					
					case "Exponential":
						Config.getInstance().generateShiftCurve(Config.EXPONENTIAL);
					break;
					
					case "Logarithmic":
						Config.getInstance().generateShiftCurve(Config.LOGARITHMIC);
					break;
					
					case "Isodyne":
						Config.getInstance().generateShiftCurve(Config.ISODYNE);
					break;

					case "Custom":
						JOptionPane.showMessageDialog(frame, "Custom shift curves are not currently supported.");
					break;
				}

				frame.setVisible(false);
				frame.dispose();
			break;

			case "Cancel":
				frame.setVisible(false);
				frame.dispose();
			break;

			default:
				System.out.println("Where'd that come from?");
			break;
		}
	}
}
