package com.jpb.probono.utility;

import android.util.Log;

import com.jpb.probono.constants.Constants;

public class PBLogger {
	public static void entry(String tag)
	{
		PBLogger.d(tag,"Entry");
	}
	
	public static void exit(String tag)
	{
		PBLogger.d(tag,"Exit");
	}
	
	
	public static void d(String tag, String msg) {
		if (Log.isLoggable(shortenTag(tag), Log.DEBUG)) {
			Log.d(shortenTag(tag), msg);
		}
	}

	public static void i(String tag, String msg) {
		if (Log.isLoggable(shortenTag(tag), Log.INFO)) {
			Log.i(shortenTag(tag), msg);
		}
	}

	public static void e(String tag, String msg) {
		if (Log.isLoggable(shortenTag(tag), Log.ERROR)) {
			Log.e(shortenTag(tag), msg);
		}
	}
	
	public static void e(String tag, String msg, Exception e) {
		if (Log.isLoggable(shortenTag(tag), Log.ERROR)) {
			Log.e(shortenTag(tag), msg, e);
		}
	}

	public static void v(String tag, String msg) {
		if (Log.isLoggable(shortenTag(tag), Log.VERBOSE)) {
			Log.v(shortenTag(tag), msg);
		}
	}

	public static void w(String tag, String msg) {
		if (Log.isLoggable(shortenTag(tag), Log.WARN)) {
			Log.w(shortenTag(tag), msg);

		}
	}

	public static void w(String tag, String msg, Exception e) {
		if (Log.isLoggable(shortenTag(tag), Log.WARN)){
			Log.w(shortenTag(tag),msg,e);
		}
		
	}
	
	private static String shortenTag(String tag)
	{
		String newTag = tag;
		if (tag.length() > Constants.MAX_LENGTH_FOR_LOGGING)
		{
			newTag = tag.substring(0,Constants.MAX_LENGTH_FOR_LOGGING - 1);
		}
		return newTag ;
	}
}
