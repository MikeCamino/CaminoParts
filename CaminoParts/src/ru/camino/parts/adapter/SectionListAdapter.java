package ru.camino.parts.adapter;

import java.util.TreeMap;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class SectionListAdapter extends BaseAdapter {
	public interface SectionDetector {
		public Object detectSection(Object firstItem, Object secondItem);
	}
	
	@SuppressWarnings("unused")
	private static final String TAG = "SectionListAdapter";
	
	private int mHeaderViewType;
	
	private BaseAdapter mAdapter;
	private TreeMap<Integer, Object> mHeaders;
	private SectionListAdapter.SectionDetector mSectionDetector;

	public SectionListAdapter(BaseAdapter adapter) {
		this(adapter, null);
	}
	
	public SectionListAdapter(BaseAdapter adapter, SectionListAdapter.SectionDetector detector) {
		mSectionDetector = detector;
		mAdapter = adapter;
		mHeaderViewType = mAdapter.getViewTypeCount();
		prepareHeaders();
	}

	@Override
	public int getCount() {
		return mAdapter.getCount() + mHeaders.size();
	}

	@Override
	public Object getItem(int position) {
		Object header = mHeaders.get(Integer.valueOf(position));
		if (header != null) {
			return header;
		} else {
			return mAdapter.getItem(getOriginalItemPosition(position));
		}
	}

	@Override
	public long getItemId(int position) {
		if (isHeader(position)) {
			// TODO: Give all headers their IDs?
			return 0L;
		} else {
			return mAdapter.getItemId(getOriginalItemPosition(position));
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (isHeader(position)) {
			return getSectionView(mHeaders.get(position), convertView, parent);
		} else {
			return mAdapter.getView(getOriginalItemPosition(position), convertView, parent);
		}
	}
	
	@Override
	public int getViewTypeCount() {
		return mAdapter.getViewTypeCount() + 1;
	}
	
	@Override
	public int getItemViewType(int position) {
		if (isHeader(position)) {
			return mHeaderViewType;
		} else {
			return mAdapter.getItemViewType(getOriginalItemPosition(position));
		}
	}
	
	@Override
	public void notifyDataSetChanged() {
		mAdapter.notifyDataSetChanged();
		
		prepareHeaders();
		
		super.notifyDataSetChanged();
	}
	
	@Override
	public void notifyDataSetInvalidated() {
		mAdapter.notifyDataSetInvalidated();
		
		prepareHeaders();
		
		super.notifyDataSetInvalidated();
	}
	
	/**
	 * Returns list of generated headers
		* @return
	 */
	public TreeMap<Integer, Object> getHeaders() {
		return mHeaders;
	}
	
	public boolean isHeader(int position) {
		return mHeaders.containsKey(Integer.valueOf(position));
	}
	
	public void setSectionDetector(SectionListAdapter.SectionDetector detector) {
		mSectionDetector = detector;
	}
	
	public SectionListAdapter.SectionDetector getSectionDetector() {
		return mSectionDetector;
	}
	
	/**
	 * 
		* @param header
		* @param convertView
		* @param parent
		* @return
	 */
	protected abstract View getSectionView(Object header, View convertView, ViewGroup parent);
	
	/**
	 * 
		* @param firstItem
		* @param secondItem
		* @return {@link Object} header to return to {@link SectionListAdapter.getSectionView} or <code>null</code> if header is not needed here
	 */
	protected abstract Object getSectionHeader(Object firstItem, Object secondItem);
	
	private void prepareHeaders() {
		mHeaders = new TreeMap<Integer, Object>();
		
		int headersCreated = 0;
		
		Object firstItem;
		Object secondItem;
		
		for (int i = 0; i < mAdapter.getCount(); i++) {
			if (i == 0) {
				firstItem = null;
			} else {
				firstItem = mAdapter.getItem(i - 1);
			}
			secondItem = mAdapter.getItem(i);
			
			Object header = getSectionHeader(firstItem, secondItem);
			if (header != null) {
				mHeaders.put(i + headersCreated, header);
				headersCreated++;
			}
		}
	}
	
	private int getHeadersCountToPos(int position) {
		return mHeaders.headMap(Integer.valueOf(position)).size();
	}
	
	private int getOriginalItemPosition(int position) {
		return position - getHeadersCountToPos(position);
	}
}
