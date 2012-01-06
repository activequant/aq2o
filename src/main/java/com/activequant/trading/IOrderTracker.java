package com.activequant.trading;

import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.utils.events.IEventSource;

/**
 * Order tracker (normally implemented by IBroker). Allows to get the
 * current state of the order, and attach to the events related to this
 * order.
 * <p>
 * The order gets physically sent to the broker only after tracker's
 * <code>submit</code> method is called. 
 * The purpose of splitting order submission from the order preparation
 * {@see IBroker} is to allow user to attach its event handlers and avoid race
 * conditions what events may start flowing before user is prepared to receive them.
 * <p>
 * <h1>Closed orders</h1>
 * Closed orders are orders that are no longer in processing: they have been
 * canceled, rejected, filled, or partially filled and canceled. Tracker
 * interface will throw an exception if user tries to perform actions on a closed order.
 * For a closed order, history property does not change.
 * <p>
 * What distinguishes tracker that corresponds to a closed order from the one that
 * corresponds to an open order, is the <code>orderCompletion</code> property.
 * For closed orders its not null and gives a convenient summary of the order
 * final state. Note that for closed orders some brokers may not provide
 * detailed execution information, but instead provide only final "gross" view.
 * 
 * <h1>Opened orders</h1>
 * Opened orders are the ones that may change asynchronously. Therefore, users must be
 * aware of this "volatile" nature of open orders. For example, <code>orderCompletion</code>
 * being null tells that order has not been completed at the moment when method was called.
 * By the time it returns, it may already be completed! There are two ways to deal
 * with this "volatility" that fit different programming styles: event-oriented
 * processing, and synchronous processing.
 * <p>
 * To get notified of the order events, application may attach to the tracker's
 * event source. It is better done before <code>submit()</code> was called, so
 * that it is guaranteed that no events has been missed.
 * Then, event-processing code may analyze the message type and content to
 * monitor the order state changes. Note that <code>history</code> in the tracker
 * is updated <em>before</em> event is sent by the tracker. This guarantees that
 * application code sees already updated event history in the event handling code. 
 * <p>
 * Application may choose to wait till the order completes, and then analyze the
 * completion by looking at <code>orderCompletion</code> and/or <code>history</code>
 * properties. There are <code>waitForCompletion</code> methods for doing this. 
 * 
 * <br>
 * <b>History:</b><br>
 *  - [06.12.2007] Created (Mike Kroutikov)<br>
 *
 *  @author Mike Kroutikov
 */
public interface IOrderTracker {
	
	/**
	 * Original order that initiated this tracker.
	 * If there were any updates to the original order,
	 * they are <em>NOT</em> reflected here: to find them,
	 * one have to track the update events emitted by this object.
	 * 
	 * @return order.
	 */
	Order getOrder();

	/**
	 * Venue assigned id for this order processing.
	 * 
	 * @return id.
	 */
	String getVenueAssignedId();
	
	/**
	 * Submits the order. Before this method is called, broker-assigned id is not available,
	 * and tracker is not registered with the broker (i.e. not returned by
	 * {@link IBroker#getOrders()}). Events can start flowing 
	 * only after <code>submit</code> is called. Therefore, attach all your
	 * event listeners before submitting the order.
	 * 
	 * @throws Exception
	 */
	void submit();
	
	/**
	 * Updates pending order. Note that not all updates are permitted.
	 * Refer to the broker's implementation documentation for more details.
	 * If the order has been completed (successfully or not), does nothing.
	 * 
	 * @param newOrder the order to replace the original one.
	 */
	void update(Order newOrder);
	
	/**
	 * Cancels this order. If order has been already completed (successfully or
	 * not), does nothing.
	 */
	void cancel();

	/**
	 * Returns event source for the detailed order-related events.
	 * A special event : {@link OrderCompletionEvent} is fired when
	 * the order is completed (successfully or not). This event is always the last event
	 * fired by broker with respect to this order.
	 * 
	 * @return order event source.
	 */
	IEventSource<OrderEvent> getOrderEventSource();
	
}