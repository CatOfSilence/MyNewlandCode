package cn.edu.jsit.smartfactory.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.edu.jsit.smartfactory.R;

public class WarnAdapter extends BaseAdapter {

    private List<String> mDatas;
    private LayoutInflater mLayoutInflater;

    public WarnAdapter(Context context, List<String> datas) {
        this.mDatas = datas;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.warn_list_item, viewGroup, false);
            holder = new ViewHolder();
            holder.textView = view.findViewById(R.id.tv_item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textView.setText(mDatas.get(i));
        return view;
    }

    private class ViewHolder {
        TextView textView;
    }


}

