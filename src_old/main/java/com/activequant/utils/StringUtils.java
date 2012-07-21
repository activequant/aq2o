/****

    activequant - activestocks.eu

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

	
	contact  : contact@activestocks.eu
    homepage : http://www.activestocks.eu

****/
package com.activequant.utils;

/**
 * @TODO desc<br>
 * <br>
 * <b>History:</b><br>
 *  - [03.03.2007] Created (Erik Nijkamp)<br>
 *
 *
 *  @author Erik Nijkamp
 */
public class StringUtils {
	
    /**
     * cool method to repeat a number of chars :-)
     * @param aChar
     * @param numChars
     * @return
     */
	public static String repeat(char aChar, int numChars) {
		if (numChars < 0) {
			throw new IllegalArgumentException("Num chars can't be negative");
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < numChars; i++) {
			buf.append(aChar);
		}
		return buf.toString();
	}

}
