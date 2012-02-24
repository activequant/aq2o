package com.activequant.dao;

/**
 * plain dao exception.
 * 
 * @author ustaudinger
 * 
 */
public class DaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DaoException() {

	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Exception ex) {
		super(ex);
	}
}
