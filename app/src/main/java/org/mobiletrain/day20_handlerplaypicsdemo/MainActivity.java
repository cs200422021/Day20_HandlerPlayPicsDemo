package org.mobiletrain.day20_handlerplaypicsdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    // 视图集合
    private List<View> views;

    // 展示视图的坐标
    private int position;

    // 定时器
    private Timer timer;

    private final int MSG_WHAT = 1;
    private final long PERIOD_TIME = 3 * 1000;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT:
                    position++;
                    viewPager.setCurrentItem(position);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    // 初始化视图
    private void init() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        View view01 = getLayoutInflater().inflate(R.layout.view_01, null);
        View view02 = getLayoutInflater().inflate(R.layout.view_02, null);
        View view03 = getLayoutInflater().inflate(R.layout.view_03, null);
        View view04 = getLayoutInflater().inflate(R.layout.view_01, null);

        views = new ArrayList<>();
        views.add(view01);
        views.add(view02);
        views.add(view03);
        views.add(view04);

        // 绑定适配器
        viewPager.setAdapter(new MyPagerAdapter(views));
        // 添加监听器
        viewPager.addOnPageChangeListener(this);

        // 初始化定时器
        timer = new Timer();
        // 定时器执行定时任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("1542", "Thread : " + Thread.currentThread().getName());
                Message message = Message.obtain();
                message.what = MSG_WHAT;
                handler.sendEmptyMessageDelayed(message.what, PERIOD_TIME);
            }
        }, 0, PERIOD_TIME);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (position == views.size() - 1 && state == ViewPager.SCROLL_STATE_IDLE) {
            position = 0;
            viewPager.setCurrentItem(position, false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewPager.removeOnPageChangeListener(this);
        // 取消定时器任务
        timer.cancel();
        timer = null;
    }
}
