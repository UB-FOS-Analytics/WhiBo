/*
 *  WhiBo
 *
 *  Copyright (C) 2010- by WhiBo development team and the contributors
 *
 *  Complete list of developers available at our web site:
 *
 *       http://www.whibo.fon.bg.ac.rs
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package rs.fon.whibo.gui.editor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import rs.fon.whibo.gui.util.Helper;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;

/**
 * JFrame for displaying ClassSummary data of given class
 * 
 * @author Nenad Zdravkovic
 * 
 */
@SuppressWarnings("serial")
public class ClassSummaryDialog extends JDialog {
	private static final String PREFIXTITLE_FRAME = "Summary";
	private static final String TITLE_LABEL = "Component name: ";
	private static final String SUMMARY_LABEL = "Component summary";
	private static final String AUTHOR_LABEL = "Author: ";
	private static final String DATE_LABEL = "Date: ";
	private static final String ERROR_LABEL = "No summary available.";
	private static final String CLOSE_BUTTON = "Close";

	JPanel jPanelContent;
	JLabel jLabelTitle;
	JLabel jLabelAuthor;
	JLabel jLabelDate;
	JPanel jPanelSynopsys;
	JLabel jLabelError;
	JTextArea jTextAreaSynopsys;
	JPanel jPanelCloseButton;
	JButton jButtonClose;

	/**
	 * Instantiates frame and shows summary data of given class
	 * 
	 * @param clazz
	 *            Class which summary data should be displayed
	 * @param owner
	 *            Owner of this dialog
	 */
	public ClassSummaryDialog(Class clazz, Dialog owner) {
		super(owner);
		initComponents();

		ClassSummary classSummary = null;
		try {
			classSummary = getClassSummary(clazz);
		} catch (ClassNotFoundException e) {
			// Class summary couldn't be extracted from instance. Continue with
			// null reference.
			System.err
					.println("ClassSummaryDialog - ClassNotFoundException caugth:"
							+ e);
			e.printStackTrace();
		} catch (Exception ex) {
			System.err
					.println("ClassSummaryDialog - general Exception caugth: "
							+ ex);
			ex.printStackTrace();
		}

		setupSummaryData(classSummary);
	}

	private void initComponents() {
		jPanelContent = new JPanel();
		jLabelTitle = new JLabel();
		jLabelAuthor = new JLabel();
		jLabelDate = new JLabel();
		jPanelSynopsys = new JPanel(new BorderLayout());
		jLabelError = new JLabel();
		jTextAreaSynopsys = new JTextArea();
		jPanelCloseButton = new JPanel();
		jButtonClose = new JButton();

		jTextAreaSynopsys.setLineWrap(true);
		jTextAreaSynopsys.setWrapStyleWord(true);
		jTextAreaSynopsys.setEditable(false);

		jLabelTitle.setAlignmentX(LEFT_ALIGNMENT);
		jLabelAuthor.setAlignmentX(LEFT_ALIGNMENT);
		jLabelDate.setAlignmentX(LEFT_ALIGNMENT);
		jPanelSynopsys.setAlignmentX(LEFT_ALIGNMENT);
		jTextAreaSynopsys.setAlignmentX(LEFT_ALIGNMENT);

		jPanelSynopsys.setBorder(BorderFactory
				.createTitledBorder(SUMMARY_LABEL));
		jPanelSynopsys.add(jTextAreaSynopsys);

		jPanelContent.add(jLabelTitle);
		jPanelContent.add(jLabelAuthor);
		jPanelContent.add(jLabelDate);
		jPanelContent.add(jPanelSynopsys);
		jPanelContent.add(jLabelError);
		jPanelContent.add(Box.createRigidArea(new Dimension(0, 5)));
		jPanelContent.setLayout(new BoxLayout(jPanelContent,
				BoxLayout.PAGE_AXIS));
		jPanelContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 3, 10));

		jPanelCloseButton.add(Box.createHorizontalGlue());
		jPanelCloseButton.add(jButtonClose);
		jPanelCloseButton.add(Box.createRigidArea(new Dimension(10, 0)));
		jPanelCloseButton.setLayout(new BoxLayout(jPanelCloseButton,
				BoxLayout.LINE_AXIS));
		jPanelCloseButton.setBorder(BorderFactory
				.createEmptyBorder(0, 0, 10, 2));

		Container container = getContentPane();
		container.add(jPanelContent, BorderLayout.CENTER);
		container.add(jPanelCloseButton, BorderLayout.PAGE_END);

		jButtonClose.setText(CLOSE_BUTTON);

		jButtonClose.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeFrame();
			}
		});

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(WindowEvent winEvt) {
				closeFrame();
			}
		});

		setSize(600, 300);
	}

	/**
	 * Extracts ClassSummary object from class's source
	 * 
	 * @param Class
	 *            to extract summary from
	 * @return ClassSummary
	 * @throws ClassNotFoundException
	 */
	private ClassSummary getClassSummary(Class clazz)
			throws ClassNotFoundException {
		JavaClass clazzSource = getJavaClassSource(clazz);

		ClassSummary classSummary = new ClassSummary();

		String componentNameTag;
		if (clazzSource.getTagsByName("componentName").length > 0) {
			componentNameTag = clazzSource.getTagsByName("componentName")[0]
					.getValue();
		} else {
			componentNameTag = Helper.stripPackageName(clazz.getName());
		}
		classSummary.setComponentName(componentNameTag);

		String authorTag;
		if (clazzSource.getTagsByName("author").length > 0) {
			authorTag = clazzSource.getTagsByName("author")[0].getValue();
		} else {
			authorTag = "";
		}
		classSummary.setAuthor(authorTag);

		String dateTag;
		if (clazzSource.getTagsByName("date").length > 0) {
			dateTag = clazzSource.getTagsByName("date")[0].getValue();
		} else {
			dateTag = "";
		}
		classSummary.setDate(dateTag);

		String commentTag;
		if (clazzSource.getComment() != null) {
			commentTag = clazzSource.getComment();
		} else {
			commentTag = "";
		}
		classSummary.setComment(commentTag);

		return classSummary;
	}

	/**
	 * Extracts source for java class. If not during development phase source is
	 * being read from plugins jar, otherwise source is read from project
	 * structure
	 * 
	 * @param clazz
	 *            Class for which source is needed
	 * @return clazz source structure
	 */
	private JavaClass getJavaClassSource(Class clazz) {
		String clazzFileName = clazz.getName().replace('.', '/') + ".java";
		JavaDocBuilder builder = new JavaDocBuilder();
		try {
			// trying to load Whibo.jar if it exists. This is usually case when
			// Rapid Miner is started out of the development environment (when
			// WhiBo.jar is deployed)
			String whiboPluginPath = getClass().getProtectionDomain()
					.getCodeSource().getLocation().getPath()
					.replaceAll("%20", " ");
			JarFile jarFile = new JarFile(whiboPluginPath);
			ZipEntry clazzZipEntry = jarFile.getEntry(clazzFileName);
			InputStream inputStream = null;
			try {
				inputStream = jarFile.getInputStream(clazzZipEntry);
			} catch (IOException e1) {
				e1.printStackTrace();
				throw e1;
			}
			builder.addSource(new InputStreamReader(inputStream));
		} catch (IOException e1) {
			// trying to load classFile from source file. This path is usually
			// executed when application is run in development phase.
			String classFile = getClass().getProtectionDomain().getCodeSource()
					.getLocation().getPath().replaceAll("%20", " ")
					+ "../src/" + clazzFileName;
			try {
				builder.addSource(new FileReader(classFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return builder.getClasses()[0];
	}

	/**
	 * Sets summary data to components
	 * 
	 * @param classSummary
	 *            Summary data
	 */
	private void setupSummaryData(ClassSummary classSummary) {
		if (classSummary == null) {
			setTitle(ERROR_LABEL);
			jPanelSynopsys.setVisible(false);
			jLabelError.setText(ERROR_LABEL);
			jLabelError.setVisible(true);
		} else {
			Boolean titleExist = !classSummary.getComponentName().trim()
					.isEmpty();
			Boolean authorExist = !classSummary.getAuthor().trim().isEmpty();
			Boolean dateExist = !classSummary.getDate().trim().isEmpty();
			Boolean commentExist = !classSummary.getComment().trim().isEmpty();

			if (titleExist) {
				jLabelTitle.setText(TITLE_LABEL
						+ classSummary.getComponentName());
				setTitle(PREFIXTITLE_FRAME + " - "
						+ classSummary.getComponentName());
			} else {
				setTitle(PREFIXTITLE_FRAME);
			}
			if (authorExist) {
				jLabelAuthor.setText(AUTHOR_LABEL + classSummary.getAuthor());
			}
			if (dateExist) {
				jLabelDate.setText(DATE_LABEL + classSummary.getDate());
			}
			if (commentExist) {
				jTextAreaSynopsys.setText(classSummary.getComment());
				jPanelSynopsys.setVisible(true);
			} else {
				jPanelSynopsys.setVisible(false);
			}
			if (!(titleExist || authorExist || dateExist || commentExist)) {
				jLabelError.setText(ERROR_LABEL);
				jLabelError.setVisible(true);
			} else {
				jLabelError.setVisible(false);
			}
		}
	}

	private void closeFrame() {
		setVisible(false);
		dispose();
	}
}