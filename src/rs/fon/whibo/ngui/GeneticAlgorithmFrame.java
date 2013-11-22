package rs.fon.whibo.ngui;

import java.awt.EventQueue;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import rs.fon.whibo.GDT.problem.GenericTreeProblemBuilder;
import rs.fon.whibo.ngui.event.SubproblemActionListenerGeneticImpl;
import rs.fon.whibo.ngui.event.SubproblemActionListenerImpl;
import rs.fon.whibo.ngui.problem.ProblemAdapter;
import rs.fon.whibo.ngui.problem.ProblemAdapterGeneticImpl;
import rs.fon.whibo.ngui.swing.ParameterListPanel;
import rs.fon.whibo.problem.ProblemBuilder;

public class GeneticAlgorithmFrame extends AlgorithmFrame {

	public GeneticAlgorithmFrame(ProblemBuilder builder) {
		super(builder);
	}

	public GeneticAlgorithmFrame(ProblemBuilder problemBuilder,
			String filePath, String whichOne) {
		super(problemBuilder, filePath, whichOne);
	}

	@Override
	public SubproblemActionListenerImpl createSubproblemActionListener(
			JList list, ParameterListPanel parameterListPanel,
			JLabel labelCurrentComponentName,
			JLabel labelCurrentComponentDescription) {
		return new SubproblemActionListenerGeneticImpl(list,
				parameterListPanel, labelCurrentComponentName,
				labelCurrentComponentDescription);
	}

	@Override
	public ProblemAdapter createProblemAdapter(ProblemBuilder problemBuilder,
			String filePath) {
		return new ProblemAdapterGeneticImpl(problemBuilder, filePath);
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
					AlgorithmFrame frame = new GeneticAlgorithmFrame(
							new GenericTreeProblemBuilder());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
