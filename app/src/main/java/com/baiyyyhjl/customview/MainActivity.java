package com.baiyyyhjl.customview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.baiyyyhjl.customview.view.BannerView;

public class MainActivity extends AppCompatActivity {

    private BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bannerView = (BannerView) findViewById(R.id.banner_view);
        bannerView.addItem("图片一", "http://pic.to8to.com/attch/day_160218/20160218_d968438a2434b62ba59dH7q5KEzTS6OH.png", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bannerView.addItem("图片二", "http://pic.to8to.com/attch/day_160218/20160218_6410eaeeba9bc1b3e944xD5gKKhPEuEv.png", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        bannerView.addItem("图片三", "http://img3.redocn.com/20101213/20101211_0e830c2124ac3d92718fXrUdsYf49nDl.jpg", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    protected void onResume() {
        // 当暂停时应该不再滚动
        bannerView.startTurning(500);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 暂停结束后继续滚动
        bannerView.stopTurning();
        super.onPause();
    }

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
//        bannerView.setIsStop(true);
        super.onDestroy();
    }
}
