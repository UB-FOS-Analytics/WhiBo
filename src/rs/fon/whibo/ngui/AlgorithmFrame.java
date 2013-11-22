package rs.fon.whibo.ngui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import rs.fon.whibo.GDT.problem.GenericTreeProblemBuilder;
import rs.fon.whibo.ngui.event.ListSelectionListenerImpl;
import rs.fon.whibo.ngui.event.SubproblemActionListenerImpl;
import rs.fon.whibo.ngui.extended.ProblemPreviewPanel;
import rs.fon.whibo.ngui.problem.ProblemAdapter;
import rs.fon.whibo.ngui.problem.ProblemAdapterImpl;
import rs.fon.whibo.ngui.swing.ButtonPanel;
import rs.fon.whibo.ngui.swing.ComponentListModel;
import rs.fon.whibo.ngui.swing.ParameterListPanel;
import rs.fon.whibo.problem.ProblemBuilder;
import rs.fon.whibo.problem.serialization.ProblemDecoder;
import rs.fon.whibo.problem.serialization.ProblemEncoder;

public class AlgorithmFrame extends JDialog {

	private static final long serialVersionUID = 6098688296047710821L;

	private ProblemAdapter problemAdapter;
	private JPanel contentPane;

	private static rs.fon.whibo.problem.Problem process;
	private static rs.fon.whibo.gui.panels.toolbar.Toolbar toolbar = new rs.fon.whibo.gui.panels.toolbar.Toolbar();

	private final JButton buttonNewAlgorithm = new JButton("New Algorithm");
	private final JButton buttonSaveAlgorithm = new JButton("Save Algorithm");
	private final JButton buttonOpenAlgorithm = new JButton("Open Algorithm");
	private final JLabel labelComponentName = new JLabel("Component Name");
	private final JLabel labelComponentDescription = new JLabel(
			"Component Description");
	private final JLabel labelCurrentComponentName = new JLabel();
	private final JLabel labelCurrentComponentDescription = new JLabel();
	private final JButton buttonSave = new JButton("Save");
	private final JButton buttonDisable = new JButton("Disable");

	private final ButtonPanel buttonPanel;

	private final ProblemPreviewPanel tree = new ProblemPreviewPanel();
	private final JPanel panelOptions = new JPanel();
	private final JList list = new JList();
	private final ParameterListPanel parameterListPanel;
	private String filePath;
	private final String whichOne;

	public AlgorithmFrame(ProblemBuilder builder) {
		this(builder, null, null);
	}

	public String getFilePath() {
		return this.filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	private String fileChooserName() {
		if (whichOne.equalsIgnoreCase("wba")) {
			return ".wba file";
		}
		return "Algorithm Search Space (.ass)";
	}

	private String extension() {
		if (whichOne.equalsIgnoreCase("wba")) {
			return "wba";
		}
		return "ass";
	}

	private String newButton() {
		if (whichOne.equalsIgnoreCase("wba")) {
			return "New Algorithm";
		}
		return "New Space";
	}

	private String openButton() {
		if (whichOne.equalsIgnoreCase("wba")) {
			return "Open Algorithm";
		}
		return "Open Space";
	}

	private String saveButton() {
		if (whichOne.equalsIgnoreCase("wba")) {
			return "Save Algorithm";
		}
		return "Save Space";
	}

	private String setTitleDependingOnClass() {
		if (whichOne.equalsIgnoreCase("wba")) {
			return "Generic decision tree";
		}
		return "Evolutionary generic decision tree";
	}

	private String getWhichOne() {
		return whichOne;
	}

	public AlgorithmFrame(ProblemBuilder problemBuilder, String filePath,
			String whichOne) {
		process = problemBuilder.buildProblem();
		this.whichOne = whichOne;
		buttonNewAlgorithm.setText(newButton());
		buttonOpenAlgorithm.setText(openButton());
		buttonSaveAlgorithm.setText(saveButton());

		if (filePath != null) {
			File f = new File(filePath);
			if (f.exists()) {
				toolbar.loadFile(filePath);
			} else {
				JOptionPane.showMessageDialog(this,
						"File does not exist or it is corrupted");
				// return;
				this.problemAdapter = createProblemAdapter(problemBuilder,
						"C:\\");
				this.buttonPanel = new ButtonPanel(problemAdapter);
				this.parameterListPanel = new ParameterListPanel(problemAdapter);
				initComponents();

				buttonPanel.addActionListener(createSubproblemActionListener(
						list, parameterListPanel, labelCurrentComponentName,
						labelCurrentComponentDescription));
				list.addListSelectionListener(new ListSelectionListenerImpl(
						problemAdapter, list, parameterListPanel));

				buttonSave.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						problemAdapter.save();
						tree.refreshTreePreview(problemAdapter.getProblem());
					}
				});

				buttonNewAlgorithm.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						problemAdapter.resetProblem();
						resetGui();
					}
				});

				buttonDisable.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						problemAdapter.disableSubproblem();
						resetGui();
					}
				});

				buttonOpenAlgorithm.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser chooser = Util.createFileChooser(
								JFileChooser.OPEN_DIALOG, "Open File",
								fileChooserName(), extension(), null);
						int i = chooser.showOpenDialog(AlgorithmFrame.this);
						if (i == JFileChooser.APPROVE_OPTION) {
							setFilePath(chooser.getSelectedFile()
									.getAbsolutePath());
							try {
								problemAdapter.setProblem(ProblemDecoder
										.decodeFromXMLToProces(getFilePath()));
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							resetGui();
						}
					}
				});

				buttonSaveAlgorithm.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser chooser = Util.createFileChooser(
								JFileChooser.SAVE_DIALOG, "Save File",
								fileChooserName(), extension(), null);
						int i = chooser.showSaveDialog(AlgorithmFrame.this);
						if (i == JFileChooser.APPROVE_OPTION) {
							String file = chooser.getSelectedFile()
									.getAbsolutePath();
							if (getWhichOne().equalsIgnoreCase("wba")) {
								if (!file.endsWith(".wba")) {
									file += ".wba";
								}
							} else {
								if (!file.endsWith(".ass"))
									file += ".ass";
								setFilePath(file);
							}
							ProblemEncoder.encodeFormProcesToXML(
									problemAdapter.getProblem(), getFilePath());
							JOptionPane.showMessageDialog(contentPane,
									"File saved");
						}
					}
				});
				return;
			}
		}
		this.problemAdapter = createProblemAdapter(problemBuilder, filePath);
		this.buttonPanel = new ButtonPanel(problemAdapter);
		this.parameterListPanel = new ParameterListPanel(problemAdapter);
		initComponents();

		buttonPanel.addActionListener(createSubproblemActionListener(list,
				parameterListPanel, labelCurrentComponentName,
				labelCurrentComponentDescription));
		list.addListSelectionListener(new ListSelectionListenerImpl(
				problemAdapter, list, parameterListPanel));

		buttonSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				problemAdapter.save();
				tree.refreshTreePreview(problemAdapter.getProblem());
			}
		});

		buttonNewAlgorithm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				problemAdapter.resetProblem();
				resetGui();
			}
		});

		buttonDisable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				problemAdapter.disableSubproblem();
				resetGui();
			}
		});

		buttonOpenAlgorithm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = Util.createFileChooser(
						JFileChooser.OPEN_DIALOG, "Open File",
						fileChooserName(), extension(), null);
				int i = chooser.showOpenDialog(AlgorithmFrame.this);
				if (i == JFileChooser.APPROVE_OPTION) {
					setFilePath(chooser.getSelectedFile().getAbsolutePath());
					try {
						problemAdapter.setProblem(ProblemDecoder
								.decodeFromXMLToProces(getFilePath()));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					resetGui();
				}
			}
		});

		buttonSaveAlgorithm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = Util.createFileChooser(
						JFileChooser.SAVE_DIALOG, "Save File",
						fileChooserName(), extension(), null);
				int i = chooser.showSaveDialog(AlgorithmFrame.this);
				if (i == JFileChooser.APPROVE_OPTION) {
					String file = chooser.getSelectedFile().getAbsolutePath();
					if (getWhichOne().equalsIgnoreCase("wba")) {
						if (!file.endsWith(".wba")) {
							file += ".wba";
						}
					} else {
						if (!file.endsWith(".ass"))
							file += ".ass";
						setFilePath(file);
					}
					setFilePath(file);
					ProblemEncoder.encodeFormProcesToXML(
							problemAdapter.getProblem(), getFilePath());
					JOptionPane.showMessageDialog(contentPane, "File saved");
				}
			}
		});
	}

	public SubproblemActionListenerImpl createSubproblemActionListener(
			JList list, ParameterListPanel parameterListPanel,
			JLabel labelCurrentComponentName,
			JLabel labelCurrentComponentDescription) {
		return new SubproblemActionListenerImpl(list, parameterListPanel,
				labelCurrentComponentName, labelCurrentComponentDescription);
	}

	public ProblemAdapter createProblemAdapter(ProblemBuilder problemBuilder,
			String filePath) {
		return new ProblemAdapterImpl(problemBuilder, filePath);
	}

	private void resetGui() {
		buttonPanel.reset();

		if (list.getModel().getSize() > 0) {
			((ComponentListModel) list.getModel()).clear();
		}

		list.repaint();
		parameterListPanel.removeParameters();
		tree.refreshTreePreview(problemAdapter.getProblem());
	}

	private void initComponents() {
		initContentPane();
		initMenu();
		initButtonPanel();
		initOptionsPanel();
		initTreePanel();
	}

	private void initTreePanel() {
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 2;
		gbc_scrollPane.gridy = 2;

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, gbc_scrollPane);
		tree.setMaximumSize(new Dimension(0, 0));
		tree.refreshTreePreview(this.problemAdapter.getProblem());

		scrollPane.setViewportView(tree);
	}

	private void initButtonPanel() {
		GridBagConstraints gbc_panelAlgorithmSteps = new GridBagConstraints();
		gbc_panelAlgorithmSteps.insets = new Insets(0, 0, 0, 5);
		gbc_panelAlgorithmSteps.fill = GridBagConstraints.BOTH;
		gbc_panelAlgorithmSteps.gridx = 0;
		gbc_panelAlgorithmSteps.gridy = 2;
		contentPane.add(buttonPanel, gbc_panelAlgorithmSteps);
	}

	private void initMenu() {
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 2;
		gbc_panel.anchor = GridBagConstraints.EAST;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		JPanel panel = new JPanel();
		contentPane.add(panel, gbc_panel);

		panel.add(buttonNewAlgorithm);
		panel.add(buttonSaveAlgorithm);
		panel.add(buttonOpenAlgorithm);

		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.gridwidth = 3;
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 1;
		contentPane.add(new JSeparator(), gbc_separator);
	}

	private void initOptionsPanel() {
		GridBagConstraints gbc_panelOptions = new GridBagConstraints();
		gbc_panelOptions.insets = new Insets(10, 10, 10, 15);
		gbc_panelOptions.fill = GridBagConstraints.BOTH;
		gbc_panelOptions.gridx = 1;
		gbc_panelOptions.gridy = 2;
		contentPane.add(panelOptions, gbc_panelOptions);
		GridBagLayout gbl_panelOptions = new GridBagLayout();
		gbl_panelOptions.columnWidths = new int[] { 170, 0, 0, 0, 0 };
		gbl_panelOptions.rowHeights = new int[] { 25, 25, 180, 0, 0 };
		gbl_panelOptions.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panelOptions.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		panelOptions.setLayout(gbl_panelOptions);

		GridBagConstraints gbc_lblComponentName = new GridBagConstraints();
		gbc_lblComponentName.fill = GridBagConstraints.BOTH;
		gbc_lblComponentName.insets = new Insets(0, 0, 5, 5);
		gbc_lblComponentName.gridx = 0;
		gbc_lblComponentName.gridy = 0;
		panelOptions.add(labelComponentName, gbc_lblComponentName);

		GridBagConstraints gbc_labelComponentName = new GridBagConstraints();
		gbc_labelComponentName.fill = GridBagConstraints.BOTH;
		gbc_labelComponentName.insets = new Insets(0, 0, 5, 5);
		gbc_labelComponentName.gridx = 1;
		gbc_labelComponentName.gridy = 0;
		labelCurrentComponentName.setFont(new Font("Tahoma", Font.BOLD, 11));
		panelOptions.add(labelCurrentComponentName, gbc_labelComponentName);

		GridBagConstraints gbc_buttonSave = new GridBagConstraints();
		gbc_buttonSave.fill = GridBagConstraints.VERTICAL;
		gbc_buttonSave.insets = new Insets(0, 0, 5, 5);
		gbc_buttonSave.gridx = 2;
		gbc_buttonSave.gridy = 0;
		panelOptions.add(buttonSave, gbc_buttonSave);

		GridBagConstraints gbc_buttonDisable = new GridBagConstraints();
		gbc_buttonDisable.fill = GridBagConstraints.VERTICAL;
		gbc_buttonDisable.insets = new Insets(0, 0, 5, 0);
		gbc_buttonDisable.gridx = 3;
		gbc_buttonDisable.gridy = 0;
		panelOptions.add(buttonDisable, gbc_buttonDisable);

		GridBagConstraints gbc_lblComponentDescription = new GridBagConstraints();
		gbc_lblComponentDescription.insets = new Insets(0, 0, 5, 5);
		gbc_lblComponentDescription.fill = GridBagConstraints.BOTH;
		gbc_lblComponentDescription.gridx = 0;
		gbc_lblComponentDescription.gridy = 1;
		panelOptions
				.add(labelComponentDescription, gbc_lblComponentDescription);

		GridBagConstraints gbc_labelComponentDescription = new GridBagConstraints();
		gbc_labelComponentDescription.fill = GridBagConstraints.BOTH;
		gbc_labelComponentDescription.insets = new Insets(0, 0, 5, 5);
		gbc_labelComponentDescription.gridx = 1;
		gbc_labelComponentDescription.gridy = 1;
		panelOptions.add(labelCurrentComponentDescription,
				gbc_labelComponentDescription);

		GridBagConstraints gbc_scrollPaneSelectComponents = new GridBagConstraints();
		gbc_scrollPaneSelectComponents.gridwidth = 4;
		gbc_scrollPaneSelectComponents.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneSelectComponents.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneSelectComponents.gridx = 0;
		gbc_scrollPaneSelectComponents.gridy = 2;
		JScrollPane scrollPaneSelectComponents = new JScrollPane();
		panelOptions.add(scrollPaneSelectComponents,
				gbc_scrollPaneSelectComponents);
		list.setBackground(Color.WHITE);

		scrollPaneSelectComponents.setViewportView(list);
		scrollPaneSelectComponents.setBorder(new TitledBorder(null,
				"Select Components", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));

		GridBagConstraints gbc_scrollPaneParameters = new GridBagConstraints();
		gbc_scrollPaneParameters.gridwidth = 4;
		gbc_scrollPaneParameters.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPaneParameters.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneParameters.gridx = 0;
		gbc_scrollPaneParameters.gridy = 3;
		JScrollPane scrollPaneParameters = new JScrollPane();
		panelOptions.add(scrollPaneParameters, gbc_scrollPaneParameters);
		scrollPaneParameters.setBorder(new TitledBorder(null, "Parameters",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		scrollPaneParameters.setViewportView(parameterListPanel);
		// parameterListPanel.add(panelParameter);
	}

	private void initContentPane() {
		setTitle(setTitleDependingOnClass());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 940, 640);
		contentPane = new JPanel();
		contentPane.setAutoscrolls(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 200, 0, 200, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 1.0 };
		contentPane.setLayout(gbl_contentPane);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					// AlgorithmFrame frame = new AlgorithmFrame(new
					// GenericClustererProblemBuilder().buildProblem());
					AlgorithmFrame frame = new AlgorithmFrame(
							new GenericTreeProblemBuilder());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
