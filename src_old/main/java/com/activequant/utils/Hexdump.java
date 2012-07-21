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
 *  A class to hexdump stuff.<br>
 * <br>
 * <b>History:</b><br>
 *  - [06.05.2007] Created (Ulrich Staudinger)<br>
 *
 *  @author Ulrich Staudinger
 */
public class Hexdump {

	/**
	 * Pass in a byte array, get back a string representation in hex. 
	 * 
	 * @param data a byte array. 
	 * @return a hex dump string. 
	 */
	public static String hexdump(byte[] data) {
		StringBuffer ret = new StringBuffer(); 
		int padding = 1;
		int linesize = 25;
		for (int i = 0; i < padding; i++)
			ret.append("  ");
		int i;
		for (i = 0; i < (data.length); i++) {
			int c = data[i];
			ret.append((char) hexval[(c & 0xF0) >> 4]);
			ret.append((char) hexval[(c & 0x0F) >> 0]);
			ret.append(' ');
			if ((i + 1 ) % linesize == 0 && i != (data.length - 1)) {
				ret.append("\n");
				for (int j = 0; j < padding; j++)
					ret.append("  ");
			}
		}
		return ret.toString();
	}

	private static final byte hexval[] = { (byte) '0', (byte) '1', (byte) '2',
			(byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
			(byte) '8', (byte) '9', (byte) 'A', (byte) 'B', (byte) 'C',
			(byte) 'D', (byte) 'E', (byte) 'F' };
}
