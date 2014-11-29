import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*	Defines the popup dialog box that allows for editing of
 *	world constants and saving the current valueset to file.
 */
public class ConfigDialog extends JFrame
		implements ActionListener
{
	private JFrame frame;
	private JPanel mainPanel;
	private JPanel centerPanel;
	private JPanel southPanel;

	private JSpinner cVTReductionMaxSpinner, cVTReductionMinSpinner, gearboxReductionSpinner;
	private JSpinner cVTEfficiencySpinner, gearboxEfficiencySpinner;
	private JSpinner firewallAreaSpinner, wheelbaseSpinner, roadInclineSpinner, tyreRadiusSpinner, vehicleMassSpinner;
	private JSpinner percentWeightOnRearAxleSpinner, centreOfGravityHeightSpinner;
	private JSpinner engineIdleRPMSpinner, engineRedlineRPMSpinner, cVTEngagementRPMSpinner;

	private JButton button;
	
	public ConfigDialog()
	{
		frame = this;
		frame.setResizable(false);
		frame.setSize(380, 550);
		frame.setTitle("Drivetrain Configuration");
		mainPanel = new JPanel();
		centerPanel = new JPanel();
		southPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		centerPanel.setLayout(new GridLayout(0, 2));
		southPanel.setLayout(new GridLayout(0, 2));
		mainPanel.add(centerPanel, BorderLayout.CENTER);
		mainPanel.add(southPanel, BorderLayout.SOUTH);
		frame.add(mainPanel);

		cVTReductionMaxSpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.CVT_MAX_RATIO), 0.0000, 9999.9999, 0.01));
		cVTReductionMinSpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.CVT_MIN_RATIO), 0.0000, 9999.9999, 0.01));
		gearboxReductionSpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.GEARBOX_RATIO), 0.0000, 9999.9999, 0.01));
		cVTEfficiencySpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.CVT_EFFICIENCY), 0.0000, 1.0000, 0.01));
		gearboxEfficiencySpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.GEARBOX_EFFICIENCY), 0.0000, 1.0000, 0.01));
		firewallAreaSpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.FIREWALL_AREA), 0.0000, 9999.9999, 0.01));
		wheelbaseSpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.WHEELBASE), 0.0000, 9999.9999, 0.01));
		roadInclineSpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.ROAD_INCLINE), 0.0000, 89.9999, 0.01));
		tyreRadiusSpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.TYRE_RADIUS), 0.0000, 9999.9999, 0.01));
		vehicleMassSpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.VEHICLE_MASS), 0.0000, 9999.9999, 0.01));
		percentWeightOnRearAxleSpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.REAR_WEIGHT_PERCENT), 0.0000, 1.0000, 0.01));
		centreOfGravityHeightSpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.COG_HEIGHT), 0.0000, 9999.9999, 0.01));
		engineIdleRPMSpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.ENGINE_IDLE_RPM), 0.0000, 9999.9999, 0.01));
		engineRedlineRPMSpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.ENGINE_REDLINE), 0.0000, 9999.9999, 0.01));
		cVTEngagementRPMSpinner = new JSpinner(new SpinnerNumberModel(Config.getInstance().getScalar(Config.CVT_ENGAGEMENT_RPM), 0.0000, 9999.9999, 0.01));
		
		cVTReductionMaxSpinner.setEditor(new JSpinner.NumberEditor(cVTReductionMaxSpinner, "###0.00000"));
		cVTReductionMinSpinner.setEditor(new JSpinner.NumberEditor(cVTReductionMinSpinner, "###0.00000"));
		gearboxReductionSpinner.setEditor(new JSpinner.NumberEditor(gearboxReductionSpinner, "###0.00000"));
		cVTEfficiencySpinner.setEditor(new JSpinner.NumberEditor(cVTEfficiencySpinner, "###0.00000"));
		gearboxEfficiencySpinner.setEditor(new JSpinner.NumberEditor(gearboxEfficiencySpinner, "###0.00000"));
		firewallAreaSpinner.setEditor(new JSpinner.NumberEditor(firewallAreaSpinner, "###0.00000"));
		wheelbaseSpinner.setEditor(new JSpinner.NumberEditor(wheelbaseSpinner, "###0.00000"));
		roadInclineSpinner.setEditor(new JSpinner.NumberEditor(roadInclineSpinner, "###0.00000"));
		tyreRadiusSpinner.setEditor(new JSpinner.NumberEditor(tyreRadiusSpinner, "###0.00000"));
		vehicleMassSpinner.setEditor(new JSpinner.NumberEditor(vehicleMassSpinner, "###0.00000"));
		percentWeightOnRearAxleSpinner.setEditor(new JSpinner.NumberEditor(percentWeightOnRearAxleSpinner, "###0.00000"));
		centreOfGravityHeightSpinner.setEditor(new JSpinner.NumberEditor(centreOfGravityHeightSpinner, "###0.00000"));
		engineIdleRPMSpinner.setEditor(new JSpinner.NumberEditor(engineIdleRPMSpinner, "###0.00000"));
		engineRedlineRPMSpinner.setEditor(new JSpinner.NumberEditor(engineRedlineRPMSpinner, "###0.00000"));
		cVTEngagementRPMSpinner.setEditor(new JSpinner.NumberEditor(cVTEngagementRPMSpinner, "###0.00000"));

		centerPanel.add(new JLabel("CVT Maximum Ratio"));
		centerPanel.add(cVTReductionMaxSpinner);
		centerPanel.add(new JLabel("CVT Minimum Ratio"));
		centerPanel.add(cVTReductionMinSpinner);
		centerPanel.add(new JLabel("Gearbox Reduction"));
		centerPanel.add(gearboxReductionSpinner);
		centerPanel.add(new JLabel("CVT Efficiency"));
		centerPanel.add(cVTEfficiencySpinner);
		centerPanel.add(new JLabel("Gearbox Efficiency"));
		centerPanel.add(gearboxEfficiencySpinner);
		centerPanel.add(new JLabel("Firewall Frontal Area"));
		centerPanel.add(firewallAreaSpinner);
		centerPanel.add(new JLabel("Wheelbase"));
		centerPanel.add(wheelbaseSpinner);
		centerPanel.add(new JLabel("Road Incline"));
		centerPanel.add(roadInclineSpinner);
		centerPanel.add(new JLabel("Tyre Radius"));
		centerPanel.add(tyreRadiusSpinner);
		centerPanel.add(new JLabel("Vehicle Mass"));
		centerPanel.add(vehicleMassSpinner);
		centerPanel.add(new JLabel("Rear Axle Weight Fraction"));
		centerPanel.add(percentWeightOnRearAxleSpinner);
		centerPanel.add(new JLabel("Centre Of Gravity Height"));
		centerPanel.add(centreOfGravityHeightSpinner);
		centerPanel.add(new JLabel("Engine Idle RPM"));
		centerPanel.add(engineIdleRPMSpinner);
		centerPanel.add(new JLabel("Engine Redline RPM"));
		centerPanel.add(engineRedlineRPMSpinner);
		centerPanel.add(new JLabel("CVT Engagement RPM"));
		centerPanel.add(cVTEngagementRPMSpinner);

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
			case "Accept":
				System.out.print("Updating internal state... ");
				Config.getInstance().setScalar(Config.CVT_MAX_RATIO, (double)cVTReductionMaxSpinner.getValue());
				Config.getInstance().setScalar(Config.CVT_MIN_RATIO, (double)cVTReductionMinSpinner.getValue());
				Config.getInstance().setScalar(Config.GEARBOX_RATIO, (double)gearboxReductionSpinner.getValue());
				Config.getInstance().setScalar(Config.CVT_EFFICIENCY, (double)cVTEfficiencySpinner.getValue());
				Config.getInstance().setScalar(Config.GEARBOX_EFFICIENCY, (double)gearboxEfficiencySpinner.getValue());
				Config.getInstance().setScalar(Config.FIREWALL_AREA, (double)firewallAreaSpinner.getValue());
				Config.getInstance().setScalar(Config.WHEELBASE, (double)wheelbaseSpinner.getValue());
				Config.getInstance().setScalar(Config.ROAD_INCLINE, (double)roadInclineSpinner.getValue());
				Config.getInstance().setScalar(Config.TYRE_RADIUS, (double)tyreRadiusSpinner.getValue());
				Config.getInstance().setScalar(Config.VEHICLE_MASS, (double)vehicleMassSpinner.getValue());
				Config.getInstance().setScalar(Config.REAR_WEIGHT_PERCENT, (double)percentWeightOnRearAxleSpinner.getValue());
				Config.getInstance().setScalar(Config.COG_HEIGHT, (double)centreOfGravityHeightSpinner.getValue());
				Config.getInstance().setScalar(Config.ENGINE_IDLE_RPM, (double)engineIdleRPMSpinner.getValue());
				Config.getInstance().setScalar(Config.ENGINE_REDLINE, (double)engineRedlineRPMSpinner.getValue());
				Config.getInstance().setScalar(Config.CVT_ENGAGEMENT_RPM, (double)cVTEngagementRPMSpinner.getValue());
				System.out.println("Done.");
				frame.setVisible(false);
				frame.dispose();
			break;

			case "Cancel":
				frame.setVisible(false);
				frame.dispose();
			break;
		}
		
	}
}
