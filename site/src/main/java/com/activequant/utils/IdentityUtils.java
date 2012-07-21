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
 * All objects have both identity (the object's location in memory) and state (the object's data).<br>
 * This generic class allows the developer to easily implement identity and state comparison<br>
 * for each object<br>
 * <br>
 * <b>History:</b><br>
 *  - [07.10.2007] Created (Erik Nijkamp)<br>
 *
 *  @author Erik Nijkamp
 */
public class IdentityUtils {

	public static boolean areEqual(boolean aThis, boolean aThat) {
		return aThis == aThat;
	}

	public static boolean areEqual(char aThis, char aThat) {
		return aThis == aThat;
	}

	public static boolean areEqual(long aThis, long aThat) {
		return aThis == aThat;
	}

	public static boolean areEqual(float aThis, float aThat) {
		return Float.floatToIntBits(aThis) == Float.floatToIntBits(aThat);
	}

	public static boolean areEqual(double aThis, double aThat) {
		return Double.doubleToLongBits(aThis) == Double.doubleToLongBits(aThat);
	}

	public static boolean areEqual(Object aThis, Object aThat) {
		return aThis == null ? aThat == null : aThis.equals(aThat);
	}

	public static <T extends Comparable<T>> int safeCompare(T o1, T o2) {
		if(o1 != null) {
			return o2 == null ? 1 : compare(o1, o2);
		} else {
			return o2 == null ? 0 : -1;
		}
	}
	
	// we can safely compare only apples to apples: if classes are different,
	// then we are grossly confused. -mk
	private static <T extends Comparable<T>> int compare(T o1, T o2) {
		if(o1.getClass().equals(o2.getClass())) {
			return o1.compareTo(o2);
		} else {
			throw new AssertionError("attempt to compare incomaparable: " + o1 + " " + o2 + ", " + o1.getClass() + " " + o2.getClass());
		} 
	}

	public static int safeCompare(int o1, int o2) {
		return o1 - o2;
	}

	public static int safeCompare(long o1, long o2) {
		return o1 > o2 ? 1 : ( o1 < o2 ? -1 : 0);
	}

	public static int safeCompare(char o1, char o2) {
		return o1 > o2 ? 1 : ( o1 < o2 ? -1 : 0);
	}
	
	public static int safeCompare(byte o1, byte o2) {
		return o1 > o2 ? 1 : (o1 < o2 ? -1: 0);
	}

	public static <T> int safeHashCode(T o) {
		return o == null ? 0 : o.hashCode();
	}
	
	public static int safeHashCode(int i) {
		return i;
	}

	public static int safeHashCode(long i) {
		return (int) i;
	}

	public static int safeHashCode(char i) {
		return i;
	}

	public static int safeHashCode(byte i) {
		return i;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Comparable<T>> boolean equalsTo(T o1, Object o2) {
		assert(o1 != null);
		if(o1 == o2) {
			return true;
		} else if(o2 == null) {
			return false;
		} else if(o2.getClass() == o1.getClass()) {
			return o1.compareTo((T) o2) == 0;
		} else {
			return false;
		}
	}	
}
