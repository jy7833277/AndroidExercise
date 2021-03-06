package com.jungle.exercise.third;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jungle.exercise.R;
import com.jungle.exercise.WebViewActivity;
import com.jungle.exercise.third.dao.ToutiaoNewsService;

import org.apache.commons.collections4.MapUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Jungle
 */
public class NewsListActivity extends AppCompatActivity {

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.lv_news_list)
    ListView listView;
    BaseAdapter mAdapter;
    View mLoadMoreView;

    private Context mContext;
    private List<Map<String, Object>> dataList;
    private int mCurrent = 0;
    private int mPageSize = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);
        ButterKnife.bind(this);

        mContext = this;
        dataList = new ArrayList<>();

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void initView() {
        mAdapter = new NewsListAdapter(mContext, dataList);
        listView.setAdapter(mAdapter);
        LayoutInflater layoutInflater =
                (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View footerView =  layoutInflater.inflate(R.layout.footer_news_list, null);
        mLoadMoreView = footerView.findViewById(R.id.tv_news_load_more);
        listView.addFooterView(footerView);
    }
    public void initData() {
        final BaseAdapter adapter = mAdapter;
//        swipeRefreshLayout.setColorSchemeColors(R.color.red, R.color.yellow, R.color.green);
        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrent = 0;
                dataList.clear();
                final Observable<Map<String, Object>> observable = Observable.create(new Observable.OnSubscribe<Map<String, Object>>() {

                    @Override
                    public void call(Subscriber<? super Map<String, Object>> subscriber) {
                        List<Map<String, Object>> list = fetchNewsData(mCurrent, mPageSize);
                        for(Map<String, Object> item : list) {
                            subscriber.onNext(item);
                        }
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());

                observable.subscribe(new Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, "获取今日头条新闻错误", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Map<String, Object> m) {
//                dataList.clear();
                        dataList.add(m);
                    }
                });
            }
        };
        swipeRefreshLayout.setOnRefreshListener(listener);
        listener.onRefresh();

        mLoadMoreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrent = (mCurrent + 1) * mPageSize;
                final Observable<Map<String, Object>> observable = Observable.create(new Observable.OnSubscribe<Map<String, Object>>() {

                    @Override
                    public void call(Subscriber<? super Map<String, Object>> subscriber) {
                        List<Map<String, Object>> list = fetchNewsData(mCurrent, mPageSize);
                        for (Map<String, Object> item : list) {
                            subscriber.onNext(item);
                        }
                        subscriber.onCompleted();
                    }
                }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());

                observable.subscribe(new Subscriber<Map<String, Object>>() {
                    @Override
                    public void onCompleted() {
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, "获取今日头条新闻错误", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Map<String, Object> m) {
                        dataList.add(m);
                    }
                });
            }
        });
    }

    private List<Map<String, Object>> fetchNewsData(int offset, int count) {
        List<Map<String, Object>> list = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://toutiao.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        ToutiaoNewsService service = retrofit.create(ToutiaoNewsService.class);
        Map<String, String> options = new HashMap<>();
        options.put("offset", String.valueOf(offset));
        options.put("count", String.valueOf(count));
        Map<String, Object> result = null;
        try {
            result = service.fetchNewsListData(options).execute().body();
        } catch (IOException e) {
            Toast.makeText(mContext, "网络请求错误", Toast.LENGTH_LONG).show();
        }
        if(null != result) {
            List<List<String>> rawList = (List<List<String>>) MapUtils.getMap(result, "data").get("news");
            for(List<String> item : rawList) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("article_id", item.get(0));
                itemMap.put("title", item.get(1));
                itemMap.put("image_url", item.get(2));
                list.add(itemMap);
            }
        }
        return list;
    }

    @OnItemClick(R.id.lv_news_list)
    public void onListItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, WebViewActivity.class);
        String articleId = dataList.get(position).get("article_id").toString();
        String articleUrl = "http://toutiao.com/group/"+articleId+"/";
        intent.putExtra("article_url", articleUrl);
        startActivity(intent);
    }
}
