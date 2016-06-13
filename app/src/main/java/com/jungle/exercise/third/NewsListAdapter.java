package com.jungle.exercise.third;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jungle.exercise.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/12.
 */
public class NewsListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String, Object>> mDataList;

    public NewsListAdapter(Context context, List<Map<String, Object>> dataList) {
        mContext = context;
        mDataList = dataList;
    }
    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null == convertView) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_news_list, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_news_image);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_news_title);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String imageUrl = mDataList.get(position).get("image_url").toString();
        String title = mDataList.get(position).get("title").toString();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageUrl, viewHolder.imageView);
        viewHolder.textView.setText(title);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView textView;
    }
}
