package nledu.com.hardwarebyjar;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 通用适配器
 */
public abstract class UniversalListAdapter<T> extends BaseAdapter {

    private Collection<T> mData;
    protected Context context;
    private int layoutId;
    private int dropLayoutId;

    private T get(int pos) {
        ArrayList<T> tempArray = new ArrayList();
        tempArray.addAll(mData);
        return tempArray.get(pos);

    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Collection<T> getData() {
        return mData;
    }


    public UniversalListAdapter(Context ctx, Collection<T> data, int itemLayoutId) {
        this.context = ctx;
        this.mData = data;
        this.layoutId = itemLayoutId;
    }

    protected UniversalListAdapter(Context ctx, Collection<T> data, int itemLayoutId, int dropDownLayoutId) {
        this.context = ctx;
        this.mData = data;
        this.layoutId = itemLayoutId;
        this.dropLayoutId = dropDownLayoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getViewHolder(context, convertView, parent, layoutId);
        doSomeThing(viewHolder, position, mData, get(position));
        return viewHolder.mConvertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.getViewHolder(context, convertView, parent, dropLayoutId);
        doSomeThingOnDropView(viewHolder, position, mData, get(position));
        return viewHolder.mConvertView;
    }

    protected void doSomeThingOnDropView(ViewHolder holder, int position, Collection<T> mData, T data) {

    }

    protected abstract void doSomeThing(ViewHolder holder, int position, Collection<T> mData, T data);

    public static class ViewHolder {
        SparseArray<View> views;
        View mConvertView;

        ViewHolder(Context context, int layoutId, ViewGroup parent) {
            mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            views = new SparseArray<>();
            mConvertView.setTag(this);
        }

        static ViewHolder getViewHolder(Context context, View convertView, ViewGroup parent, int layoutId) {
            if (convertView != null) {
                return (ViewHolder) convertView.getTag();
            } else {
                return new ViewHolder(context, layoutId, parent);
            }
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T get(int viewId) {
            View view = views.get(viewId);
            if (view == null) {
                view = mConvertView.findViewById(viewId);
                views.put(viewId, view);
            }
            if (view != null) {
                return (T) view;
            } else {
                return null;
            }
        }

        public View getConvertView() {
            return mConvertView;

        }

    }

}
