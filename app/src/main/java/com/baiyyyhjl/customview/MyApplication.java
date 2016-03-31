package com.baiyyyhjl.customview;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

/**
 * Created by huangjinlong on 2016/3/31.
 */
public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
    }
    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .cacheInMemory(true)// 在内存中会缓存该图片
                .cacheOnDisk(true)// 在硬盘中会缓存该图片
                .considerExifParams(true)// 会识别图片的方向信息
                .resetViewBeforeLoading(true)// 重设图片
                .resetViewBeforeLoading(false) // default
                .delayBeforeLoading(0).cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .considerExifParams(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                .build();

        File picPath = new File(Environment.getExternalStorageDirectory()
                .getPath()+ File.separator+ "Workroll"+ File.separator+ "files");

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCacheExtraOptions(480, 800)
                        // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3)//线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 1)
                        // default
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//为了保证图片名称唯一
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                        // default
                .diskCache(new UnlimitedDiskCache(picPath))
                        // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(1000)
                .defaultDisplayImageOptions(options)
                .imageDownloader(
                        new BaseImageDownloader(getApplicationContext()))
                .writeDebugLogs() // Remove for release app
                .build();//开始构建
        ImageLoader.getInstance().init(config);
    }
}
