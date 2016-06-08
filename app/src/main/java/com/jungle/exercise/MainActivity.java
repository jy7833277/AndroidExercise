package com.jungle.exercise;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("标题");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * 设置ActionBar的menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_share) {
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            //分享图片列表
//            share.setAction(Intent.ACTION_SEND_MULTIPLE);
//            share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
//            share.setType("image/*");
//            List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);

            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT,  "jungle");
            share.putExtra(Intent.EXTRA_TEXT,     "test content");
            //分享图片
//            share.setType("image/jpeg");
//            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)) );

            //指定接收provider的包名
//            share.setPackage("com.jungle.exercise");
            startActivity(Intent.createChooser(share, "分享选择标题"));
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(mContext, "test", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
