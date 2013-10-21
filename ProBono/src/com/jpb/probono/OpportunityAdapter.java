package com.jpb.probono;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jpb.probono.rest.model.Opportunity;

public class OpportunityAdapter extends ArrayAdapter<Opportunity> {

	private final List<Opportunity> list;
	private final Activity context;
	boolean checkAll_flag = false;
	boolean checkItem_flag = false;
	

	public OpportunityAdapter(Activity context, List<Opportunity> list) {
	 	super(context, R.layout.activity_opportunity, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView text;
		protected CheckBox checkbox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			convertView = inflator.inflate(R.layout.activity_opportunity, null);
			viewHolder = new ViewHolder();
			viewHolder.text = (TextView) convertView.findViewById(R.id.allText);
			viewHolder.checkbox = (CheckBox) convertView
					.findViewById(R.id.check);
			viewHolder.checkbox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							int getPosition = (Integer) buttonView.getTag(); 
							list.get(getPosition).setSelected(
									buttonView.isChecked()); // Set the value of
																// checkbox to
																// maintain its
																// state.
						}
					});
			convertView.setTag(viewHolder);
			convertView.setTag(R.id.allText, viewHolder.text);
			convertView.setTag(R.id.check, viewHolder.checkbox);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.checkbox.setTag(position); // This line is important.

		viewHolder.text.setText(list.get(position).toString());
		viewHolder.checkbox.setChecked(list.get(position).isSelected());

		return convertView;
	}
}
