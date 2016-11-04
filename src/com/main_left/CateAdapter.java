package com.main_left;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.a_shop.R;

public class CateAdapter extends BaseAdapter {
	private List<CategoryBean> categoryBean_list;
	private LayoutInflater mInflater;

	public CateAdapter(Context context, List<CategoryBean> categoryBean_list) {
		mInflater = LayoutInflater.from(context);
		this.categoryBean_list = categoryBean_list;
	}

	@Override
	public int getCount() {
		return categoryBean_list.size();
	}

	@Override
	public Object getItem(int position) {
		return categoryBean_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.dish_item_layout, parent,
					false);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (categoryBean_list.get(position).isSelected) {
			convertView.setBackgroundColor(Color.rgb(193,255,193));
		} else {
			convertView.setBackgroundColor(Color.rgb(255,222,173));
		}
		holder.text.setText(categoryBean_list.get(position).name);
		return convertView;
	}

	static class ViewHolder {
		TextView text;
	}
}
