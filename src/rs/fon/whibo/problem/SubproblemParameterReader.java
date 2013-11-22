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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nikola Nikolic
 * 
 *         This class is a utility class that reads all annotated properties
 *         form class
 */
public class SubproblemParameterReader {

	/**
	 * 
	 * this method reads annotated properties with
	 * {@link rs.fon.whibo.problem.Parameter} form class and returns List of
	 * {@link SubproblemParameter} objects.
	 */

	public static List<SubproblemParameter> readParameters(Class<?> classType) {
		List<SubproblemParameter> list = new ArrayList<SubproblemParameter>();

		Field[] fields = classType.getDeclaredFields();
		for (Field field : fields) {
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation.annotationType().equals(
						rs.fon.whibo.problem.Parameter.class)) {
					SubproblemParameter param = new SubproblemParameter();
					param.setDefaultValue(((rs.fon.whibo.problem.Parameter) annotation)
							.defaultValue());
					param.setMinValue(((rs.fon.whibo.problem.Parameter) annotation)
							.minValue());
					param.setMaxValue(((rs.fon.whibo.problem.Parameter) annotation)
							.maxValue());
					param.setNameOfParameter(field.getName());
					param.setParametertType(field.getType());
					list.add(param);

				}
			}
		}

		return list;

	}
}