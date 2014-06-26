package com.tycoon177.mineabound.data.exceptions;

public class InvalidChunkDataException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7262520682645832050L;

	public InvalidChunkDataException() {
		// TODO Auto-generated constructor stub
	}
	
	public InvalidChunkDataException(String message) {
		super(message);
	}
	
	public InvalidChunkDataException(Throwable cause) {
		super(cause);
	}
	
	public InvalidChunkDataException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public InvalidChunkDataException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
