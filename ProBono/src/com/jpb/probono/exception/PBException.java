package com.jpb.probono.exception;

public class PBException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PBException()
	{
		super();
	}
	
	public PBException(String msg)
	{
		super(msg);
	}
	
	public PBException(Throwable e)
	{
		super(e);
	}
	public PBException(String msg, Throwable e)
	{
		super(msg,e);
	}
}
