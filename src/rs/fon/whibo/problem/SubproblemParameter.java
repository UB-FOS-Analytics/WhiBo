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
package rs.fon.whibo.problem;

import java.io.Serializable;

/**
 * 
 * This class contains parameter min, max, and default values of parameter of
 * the specific component.
 * 
 * @author Nikola Nikolic
 * 
 */
public class SubproblemParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -574742590706925063L;

	private String nameOfParameter;

	private Class<?> parametertType;

	private String minValue;

	private String maxValue;

	private String defaultValue;

	private String xEnteredValue;

	private String xEnteredUpperValue;

	public String getXenteredValue() {
		return xEnteredValue; // prefix "x" is to ensure the field is seriliazed
								// as last field (since serilization is
								// lexicografical)
	}

	public void setXenteredValue(String enteredValue)
			throws IllegalArgumentException {
		validateEntry(enteredValue);
		this.xEnteredValue = enteredValue;
	}

	public String getXenteredUpperValue() {
		return xEnteredUpperValue; // prefix "x" is to ensure the field is
									// seriliazed as last field (since
									// serilization is lexicografical)
	}

	public void setXenteredUpperValue(String xEnteredUpperValue)
			throws IllegalArgumentException {
		validateEntry(xEnteredUpperValue);
		this.xEnteredUpperValue = xEnteredUpperValue;
	}

	public String getNameOfParameter() {
		return nameOfParameter;
	}

	public void setNameOfParameter(String nameOfParameter) {
		this.nameOfParameter = nameOfParameter;
	}

	public Class<?> getParametertType() {
		return parametertType;
	}

	public void setParametertType(Class<?> parametertType) {
		this.parametertType = parametertType;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Validates if the user supplied value are consistent with the values
	 * constraint of the parameter.
	 * 
	 * @param entry
	 *            the entry value for the parameter
	 * @throws IllegalArgumentException
	 *             the illegal argument exception
	 */
	private void validateEntry(String entry) throws IllegalArgumentException {

		if (entry.trim().isEmpty())
			throw new IllegalArgumentException("Value for the parameter "
					+ getNameOfParameter() + " was not entered");

		try {

			if (getParametertType().equals(Integer.class)) {
				int value = Integer.parseInt(entry);
				int min = Integer.parseInt(getMinValue());
				int max = Integer.parseInt(getMaxValue());

				if (value < min || value > max) {
					throw new IllegalArgumentException(
							"Value for the parameter " + getNameOfParameter()
									+ " is out of bounds.");
				}
			}

			if (getParametertType().equals(Float.class)) {
				float value = Float.parseFloat(entry);
				float min = Float.parseFloat(getMinValue());
				float max = Float.parseFloat(getMaxValue());

				if (value < min || value > max) {
					throw new IllegalArgumentException(
							"Value for the parameter " + getNameOfParameter()
									+ " is out of bounds.");
				}
			}

			if (getParametertType().equals(Double.class)) {
				double value = Double.parseDouble(entry);
				double min = Double.parseDouble(getMinValue());
				double max = Double.parseDouble(getMaxValue());

				if (value < min || value > max) {
					throw new IllegalArgumentException(
							"Value for the parameter " + getNameOfParameter()
									+ " is out of bounds.");
				}
			}

		} catch (Exception nfe) {
			throw new NumberFormatException(
					"Entered value does not match the expected type.\nPlease enter a new value for the parameter "
							+ getNameOfParameter());
		}

		// If none of the IFs is entered it is assumed that the type is String

	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		SubproblemParameter clone = new SubproblemParameter();
		clone.setParametertType(parametertType);
		clone.setDefaultValue(defaultValue);
		clone.setMaxValue(maxValue);
		clone.setMinValue(minValue);
		clone.setNameOfParameter(nameOfParameter);
		clone.setXenteredValue(xEnteredValue);
		if (!(xEnteredUpperValue == null))
			clone.setXenteredUpperValue(xEnteredUpperValue);
		return clone;
	}

}
