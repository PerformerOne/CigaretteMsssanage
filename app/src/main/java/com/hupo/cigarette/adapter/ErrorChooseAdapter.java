package com.hupo.cigarette.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hupo.cigarette.R;

import java.util.List;

/**
 * Created by Gemini on 2018/8/24.
 *
 * 标签条件适配器
 */
public class ErrorChooseAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> list;
    private LayoutInflater inflater = null;
    private int pos;

    public ErrorChooseAdapter(Context context,List<String> lists){
        this.mContext = context;
        this.list = lists;
        // 布局装载器对象
        inflater = LayoutInflater.from(context);
    }


    public void ClickSelected(int position) {
        this.pos=position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_error_choose, null);
            holder.errorChooseTv = (TextView)view.findViewById(R.id.tv_error_choose);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.errorChooseTv.setText(list.get(i));
        if (pos == i){
            holder.errorChooseTv.setTextColor(mContext.getResources().getColor(R.color.tffffff));
            holder.errorChooseTv.setBackground(mContext.getResources().getDrawable(R.drawable.bg_error_select));
        }else {
            holder.errorChooseTv.setTextColor(mContext.getResources().getColor(R.color.tF87E3D));
            holder.errorChooseTv.setBackground(mContext.getResources().getDrawable(R.drawable.bg_error_unselect));
        }

        return view;
    }

    private final class ViewHolder {
        private TextView errorChooseTv;
    }

}
