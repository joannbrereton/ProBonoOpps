package com.jpb.probono.preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.ListView;

import com.jpb.probono.R;
import com.jpb.probono.constants.Constants;
import com.jpb.probono.utility.PBLogger;

/**
 * 
 * Borrowed from some great sample code at
 * http://blog.350nice.com/wp/archives/240
 * 
 * @author declanshanaghy http://blog.350nice.com/wp/archives/240 MultiChoice
 *         Preference Widget for Android
 * 
 * @contributor matiboy Added support for check all/none and custom separator
 *              defined in XML. IMPORTANT: The following attributes MUST be
 *              defined (probably inside attr.xml) for the code to even compile
 *              <declare-styleable name="ListPreferenceMultiSelect"> <attr
 *              format="string" name="checkAll" /> <attr format="string"
 *              name="separator" /> </declare-styleable> Whether you decide to
 *              then use those attributes is up to you.
 * 
 */
public class ListPreferenceMultiSelect extends ListPreference {
	private static final String className = "ListPreferenceMultiSelect";
	private static final String parsingSeparator = Constants.SEPARATOR_REGEXP;
	private static final String storedStringSeparator = Constants.PARAMETER_SEPARATOR;

	private static String checkAllKey = Constants.MULTISELECT_SELECT_ALL;
	private boolean[] mClickedDialogEntryIndices;

	// Constructor
	@SuppressLint("Recycle")
	public ListPreferenceMultiSelect(Context context, AttributeSet attrs) {	
		super(context, attrs);
		String TAG = className + ".ListPreferenceMultiSelect";
		PBLogger.entry(TAG);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ListPreferenceMultiSelect);
		checkAllKey = a
				.getString(R.styleable.ListPreferenceMultiSelect_checkAll);

		// Initialize the array of boolean to the same size as number of entries
		mClickedDialogEntryIndices = new boolean[getEntries().length];
		PBLogger.i(TAG, "mClickedDialogEntryIndices = " + mClickedDialogEntryIndices);
		PBLogger.exit(TAG);
	}

	@Override
	public void setEntries(CharSequence[] entries) {
		super.setEntries(entries);
		// Initialize the array of boolean to the same size as number of entries
		mClickedDialogEntryIndices = new boolean[entries.length];
	}

	public ListPreferenceMultiSelect(Context context) {
		this(context, null);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		String TAG = className + ".onPrepareDialogBuilder";
		PBLogger.entry(TAG);
		CharSequence[] entries = getEntries();
		CharSequence[] entryValues = getEntryValues();
		if (entries == null || entryValues == null
				|| entries.length != entryValues.length) {
			throw new IllegalStateException(
					"ListPreference requires an entries array and an entryValues array which are both the same length");
		}

		restoreCheckedEntries();
		builder.setMultiChoiceItems(entries, mClickedDialogEntryIndices,
				new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialog, int which,
							boolean val) {
						String TAG = "builder.onClick (innerClass)";
						PBLogger.entry(TAG);
						PBLogger.i(TAG, " isCheckAllValue(which) = " + isCheckAllValue(which));
						if (isCheckAllValue(which) == true) {
							checkAll(dialog, val);
						}
						mClickedDialogEntryIndices[which] = val;
						PBLogger.i(TAG, "which = " + which + ", val = " + val);
					}
				});
		PBLogger.exit(TAG);
	}

	private boolean isCheckAllValue(int which) {
		final CharSequence[] entryValues = getEntryValues();
		if (checkAllKey != null) {
			return entryValues[which].equals(checkAllKey);
		}
		return false;
	}

	private void checkAll(DialogInterface dialog, boolean val) {
		ListView lv = ((AlertDialog) dialog).getListView();
		int size = lv.getCount();
		for (int i = 0; i < size; i++) {
			lv.setItemChecked(i, val);
			mClickedDialogEntryIndices[i] = val;
		}
	}

	public String getValuesAsString() {
		String TAG = className + ".getValuesAsString";
		PBLogger.entry(TAG);
		StringBuffer sb = new StringBuffer("");
		
		for (int i = 0; i < this.mClickedDialogEntryIndices.length; i++) {
			if (this.mClickedDialogEntryIndices[i] ) {
				if (!this.isCheckAllValue(i))
					sb.append(this.getEntries()[i]).append(
							Constants.PARAMETER_SEPARATOR);
			}
		}
		
		PBLogger.i(TAG, "sb (return value cuts last character) = " + sb.toString());
		PBLogger.exit(TAG);
		return sb.length() == 0 ? "" : sb.toString().substring(0, sb.length() - 1);
		
	}

	public String[] parseStoredValue(CharSequence val) {
		if ("".equals(val)) {
			return null;
		} else {
			return ((String) val).split(parsingSeparator);
		}
	}

	private void restoreCheckedEntries() {
		String TAG = className + ".restoreCheckedEntries";
		PBLogger.entry(TAG);
		CharSequence[] entryValues = getEntryValues();

		// Explode the string read in sharedpreferences
		String[] vals = parseStoredValue(getValue());

		PBLogger.i(TAG, "vals= " + vals);
		if (vals != null) {
			List<String> valuesList = Arrays.asList(vals);
			// for ( int j=0; j<vals.length; j++ ) {
			// TODO: Check why the trimming... Can there be some random spaces
			// added somehow? What if we want a value with trailing spaces, is
			// that an issue?
			// String val = vals[j].trim();
			for (int i = 0; i < entryValues.length; i++) {
				CharSequence entry = entryValues[i];
				if (valuesList.contains(entry) || valuesList.contains(Constants.MULTISELECT_SELECT_ALL)) {
					mClickedDialogEntryIndices[i] = true;
				}
			}
			// }
		}
		PBLogger.exit(TAG);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// super.onDialogClosed(positiveResult);
		String TAG = className + ".onDialogClosed";
		PBLogger.entry(TAG);
		ArrayList<String> values = new ArrayList<String>();

		CharSequence[] entryValues = getEntryValues();
		PBLogger.i(TAG, "positiveResult = " + positiveResult + "entryValues = " + entryValues);
		if (positiveResult && entryValues != null) {
			for (int i = 0; i < entryValues.length; i++) {
				if (mClickedDialogEntryIndices[i] == true) {
					// Don't save the state of check all option - if any
					String val = (String) entryValues[i];
					if (checkAllKey == null
							|| (val.equals(checkAllKey) == false)) {
						values.add(val);
					}
				}
			}

			if (callChangeListener(values)) {
				setValue(join(values, storedStringSeparator));
			}
		}
		PBLogger.i(TAG, "at the end of method, values = " + values);
		PBLogger.exit(TAG);
	}

	// Credits to kurellajunior on this post
	// http://snippets.dzone.com/posts/show/91
	protected static String join(Iterable<? extends Object> pColl,
			String separator) {
		Iterator<? extends Object> oIter;
		if (pColl == null || (!(oIter = pColl.iterator()).hasNext()))
			return "";
		StringBuilder oBuilder = new StringBuilder(String.valueOf(oIter.next()));
		while (oIter.hasNext())
			oBuilder.append(separator).append(oIter.next());
		return oBuilder.toString();
	}

	// TODO: Would like to keep this static but separator then needs to be put
	// in by hand or use default separator "OV=I=XseparatorX=I=VO"...
	/**
	 * 
	 * @param straw
	 *            String to be found
	 * @param haystack
	 *            Raw string that can be read direct from preferences
	 * @param separator
	 *            Separator string. If null, static default separator will be
	 *            used
	 * @return boolean True if the straw was found in the haystack
	 */
	public static boolean contains(String straw, String haystack,
			String separator) {
		if (separator == null) {
			separator = Constants.PARAMETER_SEPARATOR;
		}
		String[] vals = haystack.split(separator);
		for (int i = 0; i < vals.length; i++) {
			if (vals[i].equals(straw)) {
				return true;
			}
		}
		return false;
	}
}
