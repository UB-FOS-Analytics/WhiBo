package rs.fon.whibo.ngui.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTextField;

import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.problem.SubproblemParameter;

public class ParameterPanelImpl extends ParameterPanel {

	/**
	 * Create the panel.
	 * 
	 * @param subproblemData
	 */
	public ParameterPanelImpl(SubproblemData subproblemData) {
		super(subproblemData);

		List<SubproblemParameter> listOfParameters = subproblemData
				.getListOfParameters();
		setPanelLayout(listOfParameters.size());

		String nameOfImplementationClass = subproblemData
				.getNameOfImplementationClass();
		setLabel(nameOfImplementationClass.substring(nameOfImplementationClass
				.lastIndexOf('.') + 1));
		setSeparator();

		int index = 2;
		for (SubproblemParameter parameter : listOfParameters) {
			addParameter(parameter, index);
			index += 2;
		}

	}

	@Override
	public JComponent createValueComponent(final SubproblemParameter parameter) {
		JTextField textField = new JTextField();

		if (parameter.getXenteredValue() == null) {
			parameter.setXenteredValue(parameter.getDefaultValue());
		}

		textField.setText(parameter.getXenteredValue());
		textField.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				JTextField field = (JTextField) e.getSource();
				parameter.setXenteredValue(field.getText());
			}
		});
		return textField;
	}

}
