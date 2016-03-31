package com.baiyyyhjl.customview.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baiyyyhjl.customview.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjinlong on 2016/3/31.
 */
public class BannerView extends RelativeLayout {

    // 用于存储描述字段
    private List<String> titleList = new ArrayList<>();
    // 获取图片的url
    private List<String> urlList = new ArrayList<>();
    // 对每个图片点击的事件监听
    private List<OnClickListener> listenerList = new ArrayList<>();
    // 视图的title
    private TextView titleTv;
    // 视图的viewpager，来滑动显示图片，需要设置adapter
    private ViewPager vp;
    // 用于显示页数的小点
    private LinearLayout ll;
    // 加载图片工具
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private MyPagerAdapter adapter;

    // 设置滚动
    private AdSwitchTask adSwitchTask;
    private boolean turning;
    private long autoTurningTime;
    private boolean canTurn = false;

    public boolean isTurning() {
        return turning;
    }

    public void setTurning(boolean turning) {
        this.turning = turning;
    }

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_banner, this, true);
        vp = (ViewPager) view.findViewById(R.id.vp);
        titleTv = (TextView) view.findViewById(R.id.tv_adv_title);
        ll = (LinearLayout) view.findViewById(R.id.ll_dots);
        adapter = new MyPagerAdapter();
        vp.setAdapter(adapter);

        // 设置ViewPager的滑动监听
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                titleTv.setText(titleList.get(position));
                switchDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adSwitchTask = new AdSwitchTask(this);

    }

    private class AdSwitchTask implements Runnable {

        private final WeakReference<BannerView> reference;

        AdSwitchTask(BannerView convenientBanner) {
            this.reference = new WeakReference<BannerView>(convenientBanner);
        }

        @Override
        public void run() {
            // 用于判断对象是否被回收
            BannerView bannerView = reference.get();
            // 如果没有被回收，则进行计时操作，循环图片
            if (bannerView != null) {
                if (bannerView.vp != null && bannerView.turning) {
                    if (bannerView.vp.getCurrentItem() < bannerView.adapter.getCount() - 1) {
                        bannerView.vp.setCurrentItem(bannerView.vp.getCurrentItem() + 1);
                    } else if (bannerView.vp.getCurrentItem() == bannerView.adapter.getCount() - 1) {
                        bannerView.vp.setCurrentItem(0);
                    }
                    bannerView.postDelayed(bannerView.adSwitchTask, bannerView.autoTurningTime);
                }
            }
        }
    }

    /***
     * 开始翻页
     *
     * @param autoTurningTime 自动翻页时间
     * @return
     */
    public BannerView startTurning(long autoTurningTime) {
        //如果是正在翻页的话先停掉
        if (turning) {
            stopTurning();
        }
        //设置可以翻页并开启翻页
        canTurn = true;
        this.autoTurningTime = autoTurningTime;
        turning = true;
        postDelayed(adSwitchTask, autoTurningTime);
        return this;
    }

    public void stopTurning() {
        turning = false;
        removeCallbacks(adSwitchTask);
    }

    //触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
            // 开始翻页
            if (canTurn) startTurning(autoTurningTime);
        } else if (action == MotionEvent.ACTION_DOWN) {
            // 停止翻页
            if (canTurn) stopTurning();
        }
        return super.dispatchTouchEvent(ev);
    }

    private class MyPagerAdapter extends PagerAdapter {

        // 将定义的ImageView都保存在imageViews中，防止多次定义
        private List<ImageView> imageViews = new ArrayList<>();

        @Override
        public int getCount() {
            return urlList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = null;
            if (position < imageViews.size()) {
                imageView = imageViews.get(position);
            } else {
                imageView = new ImageView(container.getContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setLayoutParams(lp);
                imageViews.add(imageView);
            }
            // 防止多次加载，加载过之后设置一个标记Tag，不再多次加载
            if (imageView.getTag() == null) {
                imageLoader.displayImage(urlList.get(position), imageView);
                imageView.setOnClickListener(listenerList.get(position));
                imageView.setTag(position);
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    /**
     * 当界面刷新，有新数据的时候，先清空然后重新添加item
     */
    public void clear() {
        ll.removeAllViews();
        titleList.clear();
        urlList.clear();
        listenerList.clear();
        adapter.notifyDataSetChanged();
    }

    /**
     * 对外的一个方法，用来添加滑动图片
     *
     * @param title
     * @param url
     * @param listener
     */
    public void addItem(String title, String url, OnClickListener listener) {
        // 设置右下角的远点，每增加一个item，就往Linearlayout里面添加一个点
        View view = new View(getContext());
        view.setBackgroundResource(R.drawable.con_circle_normal);
        int size = dpToPx(8);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        int marginRight = dpToPx(5);
        params.setMargins(0, 0, marginRight, 0);
//        view.setLayoutParams(params);
        ll.addView(view, params);
        // 将数据添加到列表中
        titleList.add(title);
        urlList.add(url);
        listenerList.add(listener);
        // 数据加完，更新adapter
        adapter.notifyDataSetChanged();
        // 添加第一个item后就要选中第一个
        if (titleList.size() == 1) {
            titleTv.setText(titleList.get(0));
            switchDots(0);
        }
    }

    /**
     * 设置当前显示圆点
     *
     * @param index
     */
    private void switchDots(int index) {
        int count = ll.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = ll.getChildAt(i);
            if (index == i) {
                view.setBackgroundResource(R.drawable.con_circle_focus);
            } else {
                view.setBackgroundResource(R.drawable.con_circle_normal);
            }
        }
    }

    public int dpToPx(int dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

}
