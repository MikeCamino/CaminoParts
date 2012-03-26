package ru.camino.parts.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SimpleSectionListAdapter extends SectionListAdapter {
	protected final Context mContext;
	protected final int mHeaderLayoutId;
	protected final int mTitleTextViewId;
	
	public SimpleSectionListAdapter(Context context, BaseAdapter adapter, SectionListAdapter.SectionDetector detector, int headerLayoutId, int titleTextViewId) {
		super(adapter, detector);
		mContext = context;
		mHeaderLayoutId = headerLayoutId;
		mTitleTextViewId = titleTextViewId;
	}
	
	public SimpleSectionListAdapter(Context context, BaseAdapter adapter, SectionListAdapter.SectionDetector detector, int headerLayoutId) {
		this(context, adapter, detector, headerLayoutId, android.R.id.title);
	}

	@Override
	protected View getSectionView(Object header, View convertView, ViewGroup parent) {
		View v;
		if (convertView != null) {
			v = convertView;
		} else {
			v = View.inflate(mContext, mHeaderLayoutId, null);
		}
		((TextView) v.findViewById(mTitleTextViewId)).setText(header.toString());
		
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
