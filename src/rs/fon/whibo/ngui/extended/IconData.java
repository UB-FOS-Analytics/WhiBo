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
package rs.fon.whibo.ngui.extended;

import javax.swing.Icon;

/**
 * Defines icon properties for gui .
 * 
 * @author Nikola Nikolic
 */
class IconData {

	/** The m_icon. */
	protected Icon m_icon;

	/** The m_expanded icon. */
	protected Icon m_expandedIcon;

	/** The m_data. */
	protected Object m_data;

	/**
	 * Instantiates a new icon data.
	 * 
	 * @param icon
	 *            the icon
	 * @param data
	 *            the data
	 */
	public IconData(Icon icon, Object data) {
		m_icon = icon;
		m_expandedIcon = null;
		m_data = data;
	}

	/**
	 * Instantiates a new icon data.
	 * 
	 * @param icon
	 *            the icon
	 * @param expandedIcon
	 *            the expanded icon
	 * @param data
	 *            the data
	 */
	public IconData(Icon icon, Icon expandedIcon, Object data) {
		m_icon = icon;
		m_expandedIcon = expandedIcon;
		m_data = data;
	}

	/**
	 * Gets the icon.
	 * 
	 * @return the icon
	 */
	public Icon getIcon() {
		return m_icon;
	}

	/**
	 * Gets the expanded icon.
	 * 
	 * @return the expanded icon
	 */
	public Icon getExpandedIcon() {
		return m_expandedIcon != null ? m_expandedIcon : m_icon;
	}

	/**
	 * Gets the object.
	 * 
	 * @return the object
	 */
	public Object getObject() {
		return m_data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return m_data.toString();
	}
}