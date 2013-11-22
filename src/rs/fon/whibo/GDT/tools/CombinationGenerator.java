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
package rs.fon.whibo.GDT.tools;

import java.math.BigInteger;

/**
 * 
 * This class is used for generating all possible combinations of string arrays.
 */

public class CombinationGenerator {

	/** The a. */
	private int[] a;

	/** The n. */
	private int n;

	/** The r. */
	private int r;

	/** The num left. */
	private BigInteger numLeft;

	/** The total. */
	private BigInteger total;

	// ------------
	// Constructor
	// ------------

	/**
	 * Instantiates a new combination generator.
	 */
	public CombinationGenerator() {
		numLeft = BigInteger.ZERO;
	}

	/**
	 * Instantiates a new combination generator.
	 * 
	 * @param n
	 *            the n
	 * @param r
	 *            the r
	 */
	public CombinationGenerator(int n, int r) {
		if (r > n) {
			throw new IllegalArgumentException();
		}
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		this.n = n;
		this.r = r;
		a = new int[r];
		BigInteger nFact = getFactorial(n);
		BigInteger rFact = getFactorial(r);
		BigInteger nminusrFact = getFactorial(n - r);
		total = nFact.divide(rFact.multiply(nminusrFact));
		reset();
	}

	// ------
	// Reset
	// ------

	/**
	 * Reset.
	 */
	public void reset() {
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		numLeft = new BigInteger(total.toString());
	}

	// ------------------------------------------------
	// Return number of combinations not yet generated
	// ------------------------------------------------

	/**
	 * Gets the num left.
	 * 
	 * @return the num left
	 */
	public BigInteger getNumLeft() {
		return numLeft;
	}

	// -----------------------------
	// Are there more combinations?
	// -----------------------------

	/**
	 * Checks for more.
	 * 
	 * @return true, if successful
	 */
	public boolean hasMore() {
		return numLeft.compareTo(BigInteger.ZERO) == 1;
	}

	// ------------------------------------
	// Return total number of combinations
	// ------------------------------------

	/**
	 * Gets the total.
	 * 
	 * @return the total
	 */
	public BigInteger getTotal() {
		return total;
	}

	// ------------------
	// Compute factorial
	// ------------------

	/**
	 * Gets the factorial.
	 * 
	 * @param n
	 *            the n
	 * 
	 * @return the factorial
	 */
	private static BigInteger getFactorial(int n) {
		BigInteger fact = BigInteger.ONE;
		for (int i = n; i > 1; i--) {
			fact = fact.multiply(new BigInteger(Integer.toString(i)));
		}
		return fact;
	}

	// --------------------------------------------------------
	// Generate next combination (algorithm from Rosen p. 286)
	// --------------------------------------------------------

	/**
	 * Gets the next.
	 * 
	 * @return the next
	 */
	public int[] getNext() {

		if (numLeft.equals(total)) {
			numLeft = numLeft.subtract(BigInteger.ONE);
			return a;
		}

		int i = r - 1;
		while (a[i] == n - r + i) {
			i--;
		}
		a[i] = a[i] + 1;
		for (int j = i + 1; j < r; j++) {
			a[j] = a[i] + j - i;
		}

		numLeft = numLeft.subtract(BigInteger.ONE);
		return a;

	}

}
