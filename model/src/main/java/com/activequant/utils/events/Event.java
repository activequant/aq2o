package com.activequant.utils.events;

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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.activequant.interfaces.utils.IEventListener;
import com.activequant.interfaces.utils.IEventSink;
import com.activequant.interfaces.utils.IEventSource;

/**
 * Generic event<br>
 * <br>
 * <b>History:</b><br>
 * - [24.10.2006] Created (Erik Nijkamp)<br>
 * - [30.10.2006] Added isEmpty() (Erik Nijkamp)<br>
 * 
 * @author Erik Nijkamp
 */
public class Event<T> implements IEventSink<T>, IEventSource<T> {
    private final Queue<IEventListener<T>> listeners = new ConcurrentLinkedQueue<IEventListener<T>>();

    
    public void fire(T e) {
    	if(listeners.isEmpty())return;
        for (IEventListener<T> listener : listeners) {
            listener.eventFired(e);
        }
    }

    public boolean isEmpty() {
        return listeners.isEmpty();
    }

    public void addEventListener(IEventListener<T> listener) {
        listeners.add(listener);
    }

    public boolean removeEventListener(IEventListener<T> listener) {
        return listeners.remove(listener);
    }

    public void clear() {
        listeners.clear();
    }

    public void forward(final IEventSink<T> target) {
        listeners.add(new IEventListener<T>() {
            public void eventFired(T event) {
                target.fire(event);
            }
        });
    }
}