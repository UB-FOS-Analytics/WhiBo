package rs.fon.whibo.integration.adapters.parameter;

import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import rs.fon.whibo.integration.interfaces.IParameterTypeProcess;
import rs.fon.whibo.ngui.GeneticAlgorithmFrame;
import rs.fon.whibo.problem.ProblemBuilder;

import com.rapidminer.gui.properties.celleditors.value.FileValueCellEditor;
import com.rapidminer.operator.Operator;

public class ProblemFileValueEditorEA extends FileValueCellEditor {
	private static final long serialVersionUID = 1L;

	private JButton processButton;

	private IParameterTypeProcess type;

	JDialog dialog = null;

	public ProblemFileValueEditorEA(ParameterTypeProblemFileEA type) {
		super(type);
		this.type = type;

		processButton = new JButton("Design Space");
		addAction();
		addButton(createFileChooserButton(), GridBagConstraints.WEST);
		addButton(processButton, GridBagConstraints.REMAINDER);

		// Obavezna ipmlementacija definisana u nadklasi
	}

	public void setOperator(Operator operator) {
		// Obavezna ipmlementacija definisana u nadklasi

	}

	private void addAction() {
		processButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProblemBuilder pb = null;
				try {
					pb = type.getProcessBuilder().newInstance();
					if (dialog == null) {
						dialog = new GeneticAlgorithmFrame(pb,
								(String) getCellEditorValue(), "ass");
						dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
					}
					dialog.setVisible(true);
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}

			}
		});
	}

	@Override
	public boolean rendersLabel() {
		// TODO Auto-generated method stub
		return true;
	}
}
