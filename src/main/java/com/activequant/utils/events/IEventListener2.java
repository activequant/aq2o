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
package com.activequant.utils.events;

/**
 * Generic event<br>
 * <br>
 * <b>History:</b><br>
 * - [24.10.2006] Created (Erik Nijkamp)<br>
 * 
 * @author Erik Nijkamp
 */
public interface IEventListener2<T1, T2> {

    public void eventFired(T1 t1, T2 t2) throws Exception;

}