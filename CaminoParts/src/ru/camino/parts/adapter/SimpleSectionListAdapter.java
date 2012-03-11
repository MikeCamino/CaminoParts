package ru.camino.parts.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SimpleSectionListAdapter extends SectionListAdapter {
	protected Context mContext;
	protected int mHeaderLayoutId;
	
	public SimpleSectionListAdapter(BaseAdapter a) {
		super(a);
	}
	
	public SimpleSectionListAdapter(Context context, BaseAdapter adapter, SectionListAdapter.SectionDetector detector, int headerLayoutId) {
		super(adapter, detector);
		mContext = context;
		mHeaderLayoutId = headerLayoutId;
	}

	@Override
	protected View getSectionView(Object header, View convertView, ViewGroup parent) {
		View v;
		if (convertView != null) {
			v = convertView;
		} else {
			v = View.inflate(mContext, mHeaderLayoutId, null);
		}
		((TextView) v.findViewById(android.R.id.title)).setText(header.toString());
		
		return v;
	}

	@Override
	protected Object getSectionHeader(Object firstItem, Object secondItem) {
		if (getSectionDetector() != null) {
			return getSectionDetector().detectSection(firstItem, secondItem);
		} else {
			return null;
		}
	}
}
