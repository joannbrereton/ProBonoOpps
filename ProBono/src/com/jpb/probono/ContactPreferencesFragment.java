package com.jpb.probono;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.View;
import android.widget.LinearLayout;

import com.jpb.probono.helper.PreferencesHelper;
import com.jpb.probono.utility.PBLogger;

public class ContactPreferencesFragment extends PreferenceFragment  {
	private static final String className = "ContactPF";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String TAG = className + ".onCreate";
		PBLogger.entry(TAG);
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.contactprefs);

		if (!PreferencesHelper.isContactInfoReady(getActivity())) {
			this.showPromptForContactInfo();
		}

		PBLogger.exit(TAG);
	}
	
//	public void onResume() {
//		String TAG = className + ".onResume";
//		PBLogger.entry(TAG);
//
//		super.onResume();
//		//setSummaries();
//		if (!PreferencesHelper.isContactInfoReady(getActivity())) {
//			this.showPromptForContactInfo();
//		}
//		
//		PBLogger.exit(TAG);
//
//	}

	
	protected void showPromptForContactInfo() {
		String TAG = className + ".showPromptForContactInfo";
		PBLogger.entry(TAG);
		final Dialog dialog = new Dialog(this.getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar);

		dialog.setContentView(R.layout.activity_overlay);

		LinearLayout layout = (LinearLayout) dialog
				.findViewById(R.id.llOverlay_activity);
		layout.setBackgroundColor(Color.TRANSPARENT);
		layout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog.dismiss();

			}

		});

		dialog.show();
		PBLogger.exit(TAG);

	}

}
