package rs.fon.whibo.ngui.swing;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.problem.SubproblemParameter;

/**
 * @author s.velickovic@gmail.com
 */
public abstract class ParameterPanel extends JPanel {
	protected SubproblemData subproblemData;

	public ParameterPanel(SubproblemData subproblemData) {
		this.subproblemData = subproblemData;
	}

	public abstract JComponent createValueComponent(
			final SubproblemParameter parameter);

	protected void addParameter(SubproblemParameter parameter, int index) {
		GridBagConstraints gbc_lp = gbc(0, index);
		JLabel lp = new JLabel(parameter.getNameOfParameter());
		this.add(lp, gbc_lp);

		setValueField(parameter, index);

		GridBagConstraints gbc_lg = gbc(1, index + 1);
		JLabel lg = new JLabel(String.format("(%s: %s, %s:%s, %s:%s, %s:%s)",
				"Type", parameter.getParametertType().getSimpleName(), "Min",
				parameter.getMinValue(), "Max", parameter.getMaxValue(),
				"Default", parameter.getDefaultValue()));
		lg.setFont(new Font("Tahoma", Font.PLAIN, 9));
		this.add(lg, gbc_lg);
	}

	private void setValueField(final SubproblemParameter parameter, int index) {
		GridBagConstraints gbc_textField = gbc(1, index);
		JComponent component = createValueComponent(parameter);

		this.add(component, gbc_textField);
	}

	// GUI
	private GridBagConstraints gbc(int gridx, int gridy) {
		GridBagConstraints gbc_lblAlphaValue = new GridBagConstraints();
		gbc_lblAlphaValue.fill = GridBagConstraints.BOTH;
		gbc_lblAlphaValue.insets = new Insets(0, 0, 5, 5);
		gbc_lblAlphaValue.gridx = gridx;
		gbc_lblAlphaValue.gridy = gridy;
		return gbc_lblAlphaValue;
	}

	protected void setSeparator() {
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.BOTH;
		gbc_separator_1.gridwidth = 3;
		gbc_separator_1.insets = new Insets(0, 0, 5, 0);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 1;
		JSeparator separator_1 = new JSeparator();
		this.add(separator_1, gbc_separator_1);
	}

	protected void setLabel(String subproblemTitle) {
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.gridwidth = 2;
		gbc_label.fill = GridBagConstraints.BOTH;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		JLabel label = new JLabel(subproblemTitle);
		this.add(label, gbc_label);
	}

	protected void setPanelLayout(int numberOfParameters) {
		GridBagLayout gbl_this = new GridBagLayout();
		gbl_this.columnWidths = new int[] { 130, 301, 0 };
		gbl_this.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };

		gbl_this.rowHeights = new int[numberOfParameters * 2 + 3];
		gbl_this.rowWeights = new double[numberOfParameters * 2 + 3];
		gbl_this.rowWeights[numberOfParameters * 2 + 2] = Double.MIN_VALUE;

		this.setLayout(gbl_this);
	}
}
