package com.jpb.probono.utility;

import android.util.Log;

import com.jpb.probono.constants.Constants;

public class PBLogger {
	public static void entry(String method)
	{
		PBLogger.i(Constants.TAG,method + " : " + "Entry");
	}
	
	public static void exit(String method)
	{
		PBLogger.i(Constants.TAG,method + " : " + "Exit");
	}
	
	
	public static void d(String method, String msg) {
		if (Log.isLoggable(Constants.TAG, Log.DEBUG)) {
			Log.d(Constants.TAG, prefixMessage(method, msg));
		}
	}

	public static void i(String method, String msg) {
		if (Log.isLoggable(Constants.TAG, Log.INFO)) {
			Log.i(Constants.TAG, prefixMessage(method, msg));
		}
	}

	public static void e(String method, String  msg) {
		if (Log.isLoggable(Constants.TAG, Log.ERROR)) {
			Log.e(Constants.TAG, prefixMessage(method, msg));
		}
	}
	
	public static void e(String method, String msg, Exception e) {
		if (Log.isLoggable(Constants.TAG, Log.ERROR)) {
			Log.e(Constants.TAG, prefixMessage(method, msg), e);
		}
	}

	public static void v(String method, String msg) {
		if (Log.isLoggable(Constants.TAG, Log.VERBOSE)) {
			Log.v(Constants.TAG, prefixMessage(method, msg));
		}
	}

	public static void w(String method, String msg) {
		if (Log.isLoggable(Constants.TAG, Log.WARN)) {
			Log.w(Constants.TAG, prefixMessage(method, msg));

		}
	}

	public static void w(String method, String msg, Exception e) {
		if (Log.isLoggable(Constants.TAG, Log.WARN)){
			Log.w(Constants.TAG,prefixMessage(method, msg),e);
		}
		
	}
	
	private static String prefixMessage(String method, String msg)
	{
			return method + " : " + msg ;
	}
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}
}
