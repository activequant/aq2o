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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.activequant.interfaces.utils.IEventListener2;

/**
 * Generic event<br>
 * <br>
 * <b>History:</b><br>
 * - [24.10.2006] Created (Erik Nijkamp)<br>
 * - [30.10.2006] Added isEmpty() (Erik Nijkamp)<br>
 * 
 * @author Erik Nijkamp
 */
public class Event2<T1, T2> {
    private final Queue<IEventListener2<T1, T2>> listeners = new ConcurrentLinkedQueue<IEventListener2<T1, T2>>();

    public void fire(T1 t1, T2 t2) throws Exception {
        for (IEventListener2<T1, T2> listener : listeners) {
            listener.eventFired(t1, t2);
        }
    }

    public int size() {
        return listeners.size();
    }

    public boolean isEmpty() {
        return listeners.isEmpty();
    }

    public IEventListener2<T1, T2> addEventListener(IEventListener2<T1, T2> listener) {
        listeners.add(listener);
        return listener;
    }

    public void removeEventListener(IEventListener2<T1, T2> listener) {
        listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    public void forward(final Event2<T1, T2> target) {
        listeners.add(new IEventListener2<T1, T2>() {
            public void eventFired(T1 t1, T2 t2) throws Exception {
                target.fire(t1, t2);
            }
        });
    }
}