package cn.edu.jsit.smartfactory.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.jsit.smartfactory.R;

public class LanguageAdapter extends BaseAdapter {

    private Context context;
    private String[] data;
    private int isCheck=-1;
    private ViewHolder mHolder;

    public LanguageAdapter(Context context, String[] data){
        this.context = context;
        this.data = data;
    }

    public void setCheck(int pos){
        isCheck=pos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return data.length;
    }

    @Override
    public Object getItem(int position){
        return data[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.items_lv_language,null);
            mHolder = new ViewHolder();
            mHolder.language_data = (TextView)convertView.findViewById(R.id.language_data);
            mHolder.img = (ImageView)convertView.findViewById(R.id.img_isCheck);
            convertView.setTag(mHolder);
        }else {
            mHolder = (ViewHolder)convertView.getTag();
        }

        mHolder.language_data.setText(data[position]);
        if(isCheck==position){
            mHolder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.pic_ok));
        }else{
            mHolder.img.setImageDrawable(null);
        }

        return convertView;
    }

    private class ViewHolder{
        TextView language_data;
        ImageView img;
    }
}
