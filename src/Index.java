import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.border.BevelBorder;
import java.text.SimpleDateFormat;

public class Index extends JFrame
		implements ActionListener
{
	private JFrame frame;
	private JPanel statusPanel;
	private JLabel statusLabel;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem menuItem;
	private JMenuItem fileNewMenuItem;
	private JMenuItem fileOpenMenuItem;
	private JMenuItem fileSaveMenuItem;
	private JMenuItem fileSaveAsMenuItem;
	private JMenuItem fileCloseMenuItem;
	private JMenuItem editConfigMenuItem;
	private JMenuItem editTorqueCurveMenuItem;
	private JMenuItem editShiftCurveMenuItem;
	private JMenuItem viewGraphMenuItem;
	private JMenuItem viewRawDataMenuItem;
	
	private JFileChooser fileChooser;
	private boolean unsavedChanges;
	private boolean isFileOpen;
	private File currentFile;
	
	public static void main(String[] argv)
	{
		/*
		//run integrity check
		if(!IntegrityCheck.run())
		{
			System.err.println("Integrity check failed.\nExiting...");
			return;
		}
		*/
		setDefaultLookAndFeelDecorated(false);
		new Index();
	}
		
	public Index()
	{
		//initialize variables
		frame = this;
		unsavedChanges = false;
		isFileOpen = false;
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter("Drivetrain Calc Configuration File", "dtconf"));
		currentFile = null;
		
		//initialize environment
		Constants.init();
		
		//build the menus
		menuBar = new JMenuBar();
		
		menu = new JMenu("File");
		menuBar.add(menu);
		fileNewMenuItem = new JMenuItem("New");
		fileNewMenuItem.addActionListener(this);
		menu.add(fileNewMenuItem);
		fileOpenMenuItem = new JMenuItem("Open...");
		fileOpenMenuItem.addActionListener(this);
		menu.add(fileOpenMenuItem);
		menu.addSeparator();
		fileSaveMenuItem = new JMenuItem("Save");
		fileSaveMenuItem.addActionListener(this);
		menu.add(fileSaveMenuItem);
		fileSaveAsMenuItem = new JMenuItem("Save As...");
		fileSaveAsMenuItem.addActionListener(this);
		menu.add(fileSaveAsMenuItem);
		menu.addSeparator();
		fileCloseMenuItem = new JMenuItem("Close");
		fileCloseMenuItem.addActionListener(this);
		menu.add(fileCloseMenuItem);
		menuItem = new JMenuItem("Exit");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menu = new JMenu("Edit");
		menuBar.add(menu);
		editConfigMenuItem = new JMenuItem("Configuration...");
		editConfigMenuItem.addActionListener(this);
		menu.add(editConfigMenuItem);
		editTorqueCurveMenuItem = new JMenuItem("Torque Curve...");
		editTorqueCurveMenuItem.addActionListener(this);
		menu.add(editTorqueCurveMenuItem);
		editShiftCurveMenuItem = new JMenuItem("Shift Curve...");
		editShiftCurveMenuItem.addActionListener(this);
		menu.add(editShiftCurveMenuItem);
		menu.addSeparator();
		menuItem = new JMenuItem("Constants...");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menuItem = new JMenuItem("Preferences...");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menu = new JMenu("Plot");
		menuBar.add(menu);
		viewGraphMenuItem = new JMenuItem("Graph");
		viewGraphMenuItem.addActionListener(this);
		menu.add(viewGraphMenuItem);
		viewRawDataMenuItem = new JMenuItem("Raw Data");
		viewRawDataMenuItem.addActionListener(this);
		menu.add(viewRawDataMenuItem);

		menu = new JMenu("Help");
		menuBar.add(menu);
		menuItem = new JMenuItem("Topics");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		menu.addSeparator();
		menuItem = new JMenuItem("About");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		updateMenuItems();

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
		
		frame.addWindowListener(new java.awt.event.WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent we)
			{
				if(closeConfirm())
				{
					frame.setVisible(false);
					frame.dispose();
					System.exit(0);
				}
			}
		});
		
		frame.setLocation(50, 50);
		frame.setSize(400, 200);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		frame.setTitle("Drivetrain Calc v0.1");
	}

	public void setStatus(String status)
	{
		statusLabel.setText(status);
	}

	@Override
	public void actionPerformed(ActionEvent ae)
	{
		switch(ae.getActionCommand())
		{
			case "New":
				newFile();
			break;

			case "Open...":
				openFile();
			break;

			case "Save":
				saveFile();
			break;

			case "Save As...":
				saveFileDialog();
				saveFile();
			break;

			case "Close":
				if(closeConfirm())
				{
					currentFile = null;
					isFileOpen = false;
					unsavedChanges = false;
					frame.setTitle("Drivetrain Calc v0.1");
					updateMenuItems();
				}
			break;

			case "Exit":
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			break;

			case "Constants...":
				new ConstantsDialog();
			break;

			case "Configuration...":
				new ConfigDialog();
			break;

			case "Torque Curve...":
				new TorqueCurveDialog();
			break;

			case "Shift Curve...":
				new ShiftCurveDialog();
			break;

			case "Preferences...":
				//new PreferencesDialog();
				JOptionPane.showMessageDialog(this, "Coming soon.");
			break;

			case "Graph":
				new GraphWindow();
			break;

			case "Raw Data":
				//new RawData();
				JOptionPane.showMessageDialog(this, "Coming soon.");
			break;

			case "Topics":
				//new HelpTopics();
				JOptionPane.showMessageDialog(this, "Coming soon.");
			break;

			case "About":
				JOptionPane.showMessageDialog(frame, "Drivetrain Calc v0.1\n\nAdarsh Ramanathan\nTeam Manipal Racing");
		}
	}

	private void updateMenuItems()
	{
		fileNewMenuItem.setEnabled(!isFileOpen);
		fileOpenMenuItem.setEnabled(!isFileOpen);
		fileSaveMenuItem.setEnabled(isFileOpen);
		fileSaveAsMenuItem.setEnabled(isFileOpen);
		fileCloseMenuItem.setEnabled(isFileOpen);
		editConfigMenuItem.setEnabled(isFileOpen);
		editTorqueCurveMenuItem.setEnabled(isFileOpen);
		editShiftCurveMenuItem.setEnabled(isFileOpen);
		viewGraphMenuItem.setEnabled(isFileOpen);
		viewRawDataMenuItem.setEnabled(isFileOpen);
	}
	
	private void newFile()
	{
		Config.createNew();
		isFileOpen = true;
		unsavedChanges = true;
		frame.setTitle("Drivetrain Calc v0.1 ~ Untitled");
		updateMenuItems();
	}

	private void openFile()
	{
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			if(closeConfirm())
			{
				currentFile = fileChooser.getSelectedFile();
				isFileOpen = true;
				if(!currentFile.exists())
				{
					JOptionPane.showMessageDialog(frame, "The file you selected does not exist.", "Error", JOptionPane.ERROR_MESSAGE);
					currentFile = null;
					isFileOpen = false;
					unsavedChanges = false;
					updateMenuItems();
					return;
				}
				//deserialize currentFile into a Config class instance
				Config.loadFromFile(currentFile);
				frame.setTitle(new String("Drivetrain Calc v0.1 ~ ").concat(currentFile.getName()));
				setStatus(new String("Last saved on ").concat(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").format(currentFile.lastModified())));
			}
		}
		updateMenuItems();
	}

	private void saveFile()
	{
		if(currentFile == null)
		{
			if(!saveFileDialog())
			{
				return;
			}
		}
		//serialize the Config class instance into currentFile
		Config.saveToFile(currentFile);
		frame.setTitle(new String("Drivetrain Calc v0.1 ~ ").concat(currentFile.getName()));
		unsavedChanges = false;
		updateMenuItems();
	}

	private boolean saveFileDialog()
	{
		if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			currentFile = fileChooser.getSelectedFile();
			if(currentFile.getName().indexOf(".dtconf") < 0)
			{
				currentFile = new File(currentFile.getAbsolutePath().concat(".dtconf"));
			}
			if(currentFile.exists())
			{
				switch(JOptionPane.showConfirmDialog(frame, "The file you selected already exists. Overwrite?", "Overwrite?", JOptionPane.YES_NO_OPTION))
				{
					case JOptionPane.YES_OPTION:
						return true;

					case JOptionPane.NO_OPTION:
						currentFile = null;
						return false;
				}
			}
		}
		return false;
	}

	private boolean closeConfirm()
	{
		if(isFileOpen && unsavedChanges)
		{
			switch(JOptionPane.showConfirmDialog(frame, "You have unsaved work. Save before closing?", "Save this file?", JOptionPane.YES_NO_CANCEL_OPTION))
			{
				case JOptionPane.YES_OPTION:
					saveFile();
					return true;

				case JOptionPane.NO_OPTION:
					return true;

				case JOptionPane.CANCEL_OPTION:
					return false;

				default:
					return false;
			}
		}
		else
		{
			return true;
		}
	}
}
