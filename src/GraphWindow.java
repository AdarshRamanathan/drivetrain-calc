import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class GraphWindow extends JFrame
		implements ActionListener
{
	private JFrame frame;
	private JPanel statusPanel;
	private JLabel statusLabel;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenu submenu;
	private JMenuItem menuItem;
	private JRadioButtonMenuItem rbMenuItem;
	private ButtonGroup xVar, yVar;
	private JCheckBoxMenuItem autoRedrawCB, drawGridCB, drawPointsCB, drawMaximaCB, drawMinimaCB;

	private GraphPlot graph;

	private boolean autoScale, autoRedraw;
	private double xMin, yMin, xMax, yMax;
	
	public static void main(String[] argv)
	{
		new GraphWindow();
	}
		
	public GraphWindow()
	{
		//initialize variables
		frame = this;
		this.autoScale = true;
		this.autoRedraw = true;
		
		//build the menus
		menuBar = new JMenuBar();
		
		menu = new JMenu("Graph");
		menuBar.add(menu);
		
		submenu = new JMenu("X Variable");
		menu.add(submenu);
		//add submenu options here
		//disable all options except rpm, for now
		xVar = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("Engine RPM");
		rbMenuItem.setActionCommand("Engine RPM");
		rbMenuItem.addActionListener(this);
		xVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Engine Torque");
		rbMenuItem.setActionCommand("Engine Torque");
		rbMenuItem.addActionListener(this);
		xVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Engine Power");
		rbMenuItem.setActionCommand("Engine Power");
		rbMenuItem.addActionListener(this);
		xVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("CVT Ratio");
		rbMenuItem.setActionCommand("CVT Ratio");
		rbMenuItem.addActionListener(this);
		xVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Wheel RPM");
		rbMenuItem.setActionCommand("Wheel RPM");
		rbMenuItem.addActionListener(this);
		xVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Wheel Torque");
		rbMenuItem.setActionCommand("Wheel Torque");
		rbMenuItem.addActionListener(this);
		xVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Wheel Power");
		rbMenuItem.setActionCommand("Wheel Power");
		rbMenuItem.addActionListener(this);
		xVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Vehicle Speed - RLim");
		rbMenuItem.setActionCommand("Vehicle Speed - RLim");
		rbMenuItem.addActionListener(this);
		xVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Vehicle Speed - PLim");
		rbMenuItem.setActionCommand("Vehicle Speed - PLim");
		rbMenuItem.addActionListener(this);
		xVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Vehicle Speed - True");
		rbMenuItem.setActionCommand("Vehicle Speed - True");
		rbMenuItem.addActionListener(this);
		xVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Tractive Effort");
		rbMenuItem.setActionCommand("Tractive Effort");
		rbMenuItem.addActionListener(this);
		xVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Total Resistance");
		rbMenuItem.setActionCommand("Total Resistance");
		rbMenuItem.addActionListener(this);
		xVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Net Force");
		rbMenuItem.setActionCommand("Net Force");
		rbMenuItem.addActionListener(this);
		xVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		
		submenu = new JMenu("Y Variable");
		menu.add(submenu);
		//add submenu options here
		yVar = new ButtonGroup();
		rbMenuItem = new JRadioButtonMenuItem("Engine RPM");
		rbMenuItem.setActionCommand("Engine RPM");
		rbMenuItem.addActionListener(this);
		yVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Engine Torque");
		rbMenuItem.setActionCommand("Engine Torque");
		rbMenuItem.addActionListener(this);
		yVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Engine Power");
		rbMenuItem.setActionCommand("Engine Power");
		rbMenuItem.addActionListener(this);
		yVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("CVT Ratio");
		rbMenuItem.setActionCommand("CVT Ratio");
		rbMenuItem.addActionListener(this);
		yVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Wheel RPM");
		rbMenuItem.setActionCommand("Wheel RPM");
		rbMenuItem.addActionListener(this);
		yVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Wheel Torque");
		rbMenuItem.setActionCommand("Wheel Torque");
		rbMenuItem.addActionListener(this);
		yVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Wheel Power");
		rbMenuItem.setActionCommand("Wheel Power");
		rbMenuItem.addActionListener(this);
		yVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Vehicle Speed - RLim");
		rbMenuItem.setActionCommand("Vehicle Speed - RLim");
		rbMenuItem.addActionListener(this);
		yVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Vehicle Speed - PLim");
		rbMenuItem.setActionCommand("Vehicle Speed - PLim");
		rbMenuItem.addActionListener(this);
		yVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Vehicle Speed - True");
		rbMenuItem.setActionCommand("Vehicle Speed - True");
		rbMenuItem.addActionListener(this);
		yVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Tractive Effort");
		rbMenuItem.setActionCommand("Tractive Effort");
		rbMenuItem.addActionListener(this);
		yVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Total Resistance");
		rbMenuItem.setActionCommand("Total Resistance");
		rbMenuItem.addActionListener(this);
		yVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		rbMenuItem = new JRadioButtonMenuItem("Net Force");
		rbMenuItem.setActionCommand("Net Force");
		rbMenuItem.addActionListener(this);
		yVar.add(rbMenuItem);
		submenu.add(rbMenuItem);
		
		menu.addSeparator();
		
		menuItem = new JMenuItem("Scale");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem("Redraw");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		autoRedrawCB = new JCheckBoxMenuItem("Auto-redraw");
		autoRedrawCB.addActionListener(this);
		menu.add(autoRedrawCB);
		autoRedrawCB.setSelected(true);
		menu.addSeparator();
		menuItem = new JMenuItem("Close");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menu = new JMenu("View");
		menuBar.add(menu);
		drawGridCB = new JCheckBoxMenuItem("Grid");
		drawGridCB.addActionListener(this);
		menu.add(drawGridCB);
		drawPointsCB = new JCheckBoxMenuItem("Data Points");
		drawPointsCB.addActionListener(this);
		menu.add(drawPointsCB);
		drawMaximaCB = new JCheckBoxMenuItem("Maxima");
		drawMaximaCB.addActionListener(this);
		menu.add(drawMaximaCB);
		drawMinimaCB = new JCheckBoxMenuItem("Minima");
		drawMinimaCB.addActionListener(this);
		menu.add(drawMinimaCB);

		frame.setJMenuBar(menuBar);
		frame.setLayout(new BorderLayout());

		statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		frame.add(statusPanel, BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 20));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);

		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.setVisible(true);
		frame.setTitle("Graphs");
		frame.setPreferredSize(new Dimension(400, 400));
		frame.pack();
		frame.setExtendedState(frame.getExtendedState() | MAXIMIZED_BOTH);
	}

	public void setStatus(String status)
	{
		statusLabel.setText(status);
	}

	public void redrawGraph()
	{
		double[] rpm, x, y;

		setStatus("Working...");

		if(xVar.getSelection() == null || yVar.getSelection() == null)
		{
			setStatus("Aborted - one or more variable selections are null");
			return;
		}

		//generate RPM as the basis variable

		rpm = new double[64];
		
		double tmp = Config.getInstance().getScalar(Config.ENGINE_IDLE_RPM);
		double inc = (Config.getInstance().getScalar(Config.ENGINE_REDLINE) - tmp) / 64;

		for(int i = 0 ; i < rpm.length ; i++)
		{
			rpm[i] = tmp;
			tmp += inc;
		}

		//generating x

		x = new double[rpm.length];

		switch(xVar.getSelection().getActionCommand())
		{
			case "Engine RPM":
				for(int i = 0 ; i < x.length ; i++)
					x[i] = rpm[i];
			break;

			case "Engine Torque":
				for(int i = 0 ; i < x.length ; i++)
					x[i] = Config.getInstance().getScalar(Config.TORQUE_CURVE, rpm[i]);
			break;

			case "Engine Power":
				for(int i = 0 ; i < x.length ; i++)
					x[i] = Config.getInstance().getScalar(Config.TORQUE_CURVE, rpm[i]) * rpm[i] * (2 * Math.PI) / 60;
			break;

			case "CVT Ratio":
				for(int i = 0 ; i < x.length ; i++)
					x[i] = Config.getInstance().getScalar(Config.SHIFT_CURVE, rpm[i]);
			break;

			case "Wheel RPM":
				for(int i = 0 ; i < x.length ; i++)
					x[i] = rpm[i] / (Config.getInstance().getScalar(Config.SHIFT_CURVE, rpm[i]) * Config.getInstance().getScalar(Config.GEARBOX_RATIO));
			break;

			case "Wheel Torque":
				for(int i = 0 ; i < x.length ; i++)
					x[i] = Config.getInstance().getScalar(Config.TORQUE_CURVE, rpm[i]) * Config.getInstance().getScalar(Config.SHIFT_CURVE, rpm[i]) * Config.getInstance().getScalar(Config.GEARBOX_RATIO) * Config.getInstance().getScalar(Config.CVT_EFFICIENCY) * Config.getInstance().getScalar(Config.GEARBOX_EFFICIENCY) * Constants.getFudgeFactor();
			break;

			case "Wheel Power":
				for(int i = 0 ; i < x.length ; i++)
					x[i] = Config.getInstance().getScalar(Config.TORQUE_CURVE, rpm[i]) * rpm[i] * (2 * Math.PI / 60.0) * Config.getInstance().getScalar(Config.CVT_EFFICIENCY) * Config.getInstance().getScalar(Config.GEARBOX_EFFICIENCY) * Constants.getFudgeFactor();
			break;

			case "Vehicle Speed - RLim":
				for(int i = 0 ; i < x.length ; i++)
					x[i] = (rpm[i] / (Config.getInstance().getScalar(Config.SHIFT_CURVE, rpm[i]) * Config.getInstance().getScalar(Config.GEARBOX_RATIO))) * 2 * Math.PI * Config.getInstance().getScalar(Config.TYRE_RADIUS) / 60;
			break;

			case "Vehicle Speed - PLim":
				for(int i = 0 ; i < x.length ; i++)
					x[i] = Config.getInstance().getSpeed(rpm[i]);
			break;

			case "Vehicle Speed - True":
				for(int i = 0 ; i < x.length ; i++)
				{
					double t1 = (rpm[i] / (Config.getInstance().getScalar(Config.SHIFT_CURVE, rpm[i]) * Config.getInstance().getScalar(Config.GEARBOX_RATIO))) * 2 * Math.PI * Config.getInstance().getScalar(Config.TYRE_RADIUS) / 60;
					double t2 = Config.getInstance().getSpeed(rpm[i]);
					x[i] = Math.min(t1, t2);
				}
			break;

			default:
				setStatus("Unrecognized xVar selection - " + xVar.getSelection().getActionCommand());
				return;
		}
		
		//generating y

		y = new double[rpm.length];

		switch(yVar.getSelection().getActionCommand())
		{
			case "Engine RPM":
				for(int i = 0 ; i < y.length ; i++)
					y[i] = rpm[i];
			break;

			case "Engine Torque":
				for(int i = 0 ; i < y.length ; i++)
					y[i] = Config.getInstance().getScalar(Config.TORQUE_CURVE, rpm[i]);
			break;

			case "Engine Power":
				for(int i = 0 ; i < y.length ; i++)
					y[i] = Config.getInstance().getScalar(Config.TORQUE_CURVE, rpm[i]) * rpm[i] * (2 * Math.PI) / 60;
			break;

			case "CVT Ratio":
				for(int i = 0 ; i < y.length ; i++)
					y[i] = Config.getInstance().getScalar(Config.SHIFT_CURVE, rpm[i]);
			break;

			case "Wheel RPM":
				for(int i = 0 ; i < y.length ; i++)
					y[i] = rpm[i] / (Config.getInstance().getScalar(Config.SHIFT_CURVE, rpm[i]) * Config.getInstance().getScalar(Config.GEARBOX_RATIO));
			break;

			case "Wheel Torque":
				for(int i = 0 ; i < y.length ; i++)
					y[i] = Config.getInstance().getScalar(Config.TORQUE_CURVE, rpm[i]) * Config.getInstance().getScalar(Config.SHIFT_CURVE, rpm[i]) * Config.getInstance().getScalar(Config.GEARBOX_RATIO) * Config.getInstance().getScalar(Config.CVT_EFFICIENCY) * Config.getInstance().getScalar(Config.GEARBOX_EFFICIENCY) * Constants.getFudgeFactor();
			break;

			case "Wheel Power":
				for(int i = 0 ; i < y.length ; i++)
					y[i] = Config.getInstance().getScalar(Config.TORQUE_CURVE, rpm[i]) * rpm[i] * (2 * Math.PI / 60.0) * Config.getInstance().getScalar(Config.CVT_EFFICIENCY) * Config.getInstance().getScalar(Config.GEARBOX_EFFICIENCY) * Constants.getFudgeFactor();
			break;

			case "Vehicle Speed - RLim":
				for(int i = 0 ; i < y.length ; i++)
					y[i] = (rpm[i] / (Config.getInstance().getScalar(Config.SHIFT_CURVE, rpm[i]) * Config.getInstance().getScalar(Config.GEARBOX_RATIO))) * 2 * Math.PI * Config.getInstance().getScalar(Config.TYRE_RADIUS) / 60;
			break;

			case "Vehicle Speed - PLim":
				for(int i = 0 ; i < y.length ; i++)
					y[i] = Config.getInstance().getSpeed(rpm[i]);
			break;

			case "Vehicle Speed - True":
				for(int i = 0 ; i < y.length ; i++)
				{
					double t1 = (rpm[i] / (Config.getInstance().getScalar(Config.SHIFT_CURVE, rpm[i]) * Config.getInstance().getScalar(Config.GEARBOX_RATIO))) * 2 * Math.PI * Config.getInstance().getScalar(Config.TYRE_RADIUS) / 60;
					double t2 = Config.getInstance().getSpeed(rpm[i]);
					y[i] = Math.min(t1, t2);
				}
			break;

			default:
				setStatus("Unrecognized yVar selection - " + yVar.getSelection().getActionCommand());
				return;
		}
		
		if(graph != null)
			graph.setVisible(false);

		if(autoScale)
			graph = new GraphPlot(x, y, xVar.getSelection().getActionCommand(), yVar.getSelection().getActionCommand(), drawGridCB.isSelected(), drawPointsCB.isSelected(), drawMaximaCB.isSelected(), drawMinimaCB.isSelected(), this);
		else
			graph = new GraphPlot(x, y, xMin, yMin, xMax, yMax, xVar.getSelection().getActionCommand(), yVar.getSelection().getActionCommand(), drawGridCB.isSelected(), drawPointsCB.isSelected(), drawMaximaCB.isSelected(), drawMinimaCB.isSelected(), this);

		frame.add(graph, BorderLayout.CENTER);
		frame.revalidate();
		frame.repaint();
		setStatus("Done");
	}

	public void setScale(double x, double y)
	{
		if(xMax > 0 && yMax > 0)
		{
			xMax = x;
			yMax = y;
			autoScale = false;
		}
		else
		{
			JOptionPane.showMessageDialog(frame, "xMax and yMax cannot be nonpositive.");
			autoScale = true;
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		switch(ae.getActionCommand())
		{
			case "Auto-redraw":
				autoRedraw = autoRedrawCB.isSelected();
			break;

			case "Redraw":
				if(!autoRedraw)
					redrawGraph();
			break;

			case "Close":
				frame.setVisible(false);
				frame.dispose();
			break;
		}

		if(autoRedraw)
			redrawGraph();
	}
}
