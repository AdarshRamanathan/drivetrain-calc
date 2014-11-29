import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*	Defines the popup dialog box that allows for editing of
 *	world constants and saving the current valueset to file.
 */
public class ConstantsDialog extends JFrame
		implements ActionListener
{
	private JFrame frame;
	private JPanel mainPanel;
	private JPanel centerPanel;
	private JPanel southPanel;

	private JSpinner gSpinner, cdSpinner, rhoSpinner, muSSpinner, muRSpinner, fudgeSpinner;

	private JButton button;

	public ConstantsDialog()
	{
		frame = this;
		frame.setResizable(false);
		frame.setSize(350, 250);
		frame.setTitle("Global Constants");
		mainPanel = new JPanel();
		centerPanel = new JPanel();
		southPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		centerPanel.setLayout(new GridLayout(0, 2));
		southPanel.setLayout(new GridLayout(0, 3));
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		frame.add(mainPanel);

		gSpinner = new JSpinner(new SpinnerNumberModel(Constants.getGravitationalAcceleration(), 0.0000, 9999.9999, 0.01));
		cdSpinner = new JSpinner(new SpinnerNumberModel(Constants.getDragCoefficient(), 0.0000, 9.9999, 0.01));
		rhoSpinner = new JSpinner(new SpinnerNumberModel(Constants.getAirDensity(), 0.0000, 9999.9999, 0.01));
		muSSpinner = new JSpinner(new SpinnerNumberModel(Constants.getStaticFriction(), 0.0000, 9.9999, 0.01));
		muRSpinner = new JSpinner(new SpinnerNumberModel(Constants.getRollingResistance(), 0.0000, 9.9999, 0.01));
		fudgeSpinner = new JSpinner(new SpinnerNumberModel(Constants.getFudgeFactor(), 0.0000, 9.9999, 0.01));

		gSpinner.setEditor(new JSpinner.NumberEditor(gSpinner, "###0.00000"));
		cdSpinner.setEditor(new JSpinner.NumberEditor(cdSpinner, "###0.00000"));
		rhoSpinner.setEditor(new JSpinner.NumberEditor(rhoSpinner, "###0.00000"));
		muSSpinner.setEditor(new JSpinner.NumberEditor(muSSpinner, "###0.00000"));
		muRSpinner.setEditor(new JSpinner.NumberEditor(muRSpinner, "###0.00000"));
		fudgeSpinner.setEditor(new JSpinner.NumberEditor(fudgeSpinner, "###0.00000"));
		centerPanel.add(new JLabel("                   g"));
		centerPanel.add(gSpinner);
		centerPanel.add(new JLabel("   	               cd"));
		centerPanel.add(cdSpinner);
		centerPanel.add(new JLabel("                 rho"));
		centerPanel.add(rhoSpinner);
		centerPanel.add(new JLabel("                  us"));
		centerPanel.add(muSSpinner);
		centerPanel.add(new JLabel("                  ur"));
		centerPanel.add(muRSpinner);
		centerPanel.add(new JLabel("               fudge"));
		centerPanel.add(fudgeSpinner);

		button = new JButton("Accept");
		button.addActionListener(this);
		southPanel.add(button);
		button = new JButton("Cancel");
		button.addActionListener(this);
		southPanel.add(button);
		button = new JButton("Defaults");
		button.addActionListener(this);
		southPanel.add(button);

		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		switch(ae.getActionCommand())
		{
			case "Accept":
				System.out.print("Updating internal state... ");
				Constants.setGravitationalAcceleration((double)gSpinner.getValue());
				Constants.setDragCoefficient((double)cdSpinner.getValue());
				Constants.setAirDensity((double)rhoSpinner.getValue());
				Constants.setStaticFriction((double)muSSpinner.getValue());
				Constants.setRollingResistance((double)muRSpinner.getValue());
				Constants.setFudgeFactor((double)fudgeSpinner.getValue());
				System.out.println("Done.");
				System.out.print("Writing constants to file... ");
				try
				{
					PrintWriter pw = new PrintWriter("../conf/constants", "UTF-8");
					pw.println(gSpinner.getValue());
					pw.println(cdSpinner.getValue());
					pw.println(rhoSpinner.getValue());
					pw.println(muSSpinner.getValue());
					pw.println(muRSpinner.getValue());
					pw.println(fudgeSpinner.getValue());
					pw.close();
					System.out.println("Done.");
				}
				catch(IOException ioe)
				{
					System.out.println(ioe);
					ioe.printStackTrace();
				}
				frame.setVisible(false);
				frame.dispose();
			break;

			case "Cancel":
				frame.setVisible(false);
				frame.dispose();
			break;

			case "Defaults":
				defaults();
			break;
		}
	}

	private void defaults()
	{
		gSpinner.setValue(9.80665);
		cdSpinner.setValue(0.97);
		rhoSpinner.setValue(1.225);
		muSSpinner.setValue(0.8);
		muRSpinner.setValue(0.03);
		fudgeSpinner.setValue(1.0);
	}
}
