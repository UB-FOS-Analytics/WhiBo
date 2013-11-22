package rs.fon.whibo.ngui.swing;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import rs.fon.whibo.ngui.Util;
import rs.fon.whibo.problem.SubproblemData;
import rs.fon.whibo.problem.SubproblemParameter;

import com.jidesoft.swing.RangeSlider;

public class ParameterPanelGeneticImpl extends ParameterPanel {

	private int MIN_INT = 0;
	private int MAX_INT = 1000;
	private DecimalFormat decimalFormat = new DecimalFormat("0.###");

	/**
	 * Create the panel.
	 * 
	 * @param subproblemData
	 */
	public ParameterPanelGeneticImpl(SubproblemData subproblemData) {
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

		if (parameter.getXenteredValue() == null) {
			parameter.setXenteredValue(parameter.getMinValue());
		}

		if (parameter.getXenteredUpperValue() == null) {
			parameter.setXenteredUpperValue(parameter.getMaxValue());
		}

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		final JLabel min = new JLabel(parameter.getMinValue());
		min.setPreferredSize(new Dimension(50, 20));
		min.setHorizontalAlignment(SwingConstants.LEFT);
		final JLabel max = new JLabel(parameter.getMaxValue());
		max.setPreferredSize(new Dimension(50, 20));
		max.setHorizontalAlignment(SwingConstants.RIGHT);

		Integer minValue = (Integer) Util.getValue(
				Double.parseDouble(parameter.getXenteredValue()),
				Double.parseDouble(parameter.getMaxValue()),
				Double.parseDouble(parameter.getMinValue()),
				ParameterPanelGeneticImpl.this.MAX_INT,
				ParameterPanelGeneticImpl.this.MIN_INT, Integer.class);
		Integer maxValue = (Integer) Util.getValue(
				Double.parseDouble(parameter.getXenteredUpperValue()),
				Double.parseDouble(parameter.getMaxValue()),
				Double.parseDouble(parameter.getMinValue()),
				ParameterPanelGeneticImpl.this.MAX_INT,
				ParameterPanelGeneticImpl.this.MIN_INT, Integer.class);
		final RangeSlider r = new RangeSlider(MIN_INT, MAX_INT, minValue,
				maxValue);

		r.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {

				writeLabels(r, parameter, min, max);
			}
		});

		writeLabels(r, parameter, min, max);

		panel.add(min);
		panel.add(r);
		panel.add(max);

		return panel;
	}

	private void writeLabels(RangeSlider r, SubproblemParameter parameter,
			JLabel min, JLabel max) {
		String lowerValue = decimalFormat.format(Util.getValue(r.getLowValue(),
				this.MAX_INT, this.MIN_INT,
				Double.parseDouble(parameter.getMaxValue()),
				Double.parseDouble(parameter.getMinValue()),
				parameter.getParametertType()));
		min.setText(lowerValue);
		String upperValue = decimalFormat.format(Util.getValue(
				r.getHighValue(), this.MAX_INT, this.MIN_INT,
				Double.parseDouble(parameter.getMaxValue()),
				Double.parseDouble(parameter.getMinValue()),
				parameter.getParametertType()));
		max.setText(upperValue);

		parameter.setXenteredValue(lowerValue);
		parameter.setXenteredUpperValue(upperValue);
	}

}
