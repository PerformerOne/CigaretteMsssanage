package com.hupo.cigarette.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.amap.api.location.AMapLocation;
import com.hupo.cigarette.bean.User;
import com.hupo.cigarette.dao.greendao.DaoMaster;
import com.hupo.cigarette.dao.greendao.DaoSession;
import com.hupo.cigarette.utils.PreferUtils;
import com.hupo.cigarette.utils.Utils;
import com.idescout.sql.SqlScoutServer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.util.Stack;

/**
 * Created by Gemini on 2018/10/25.
 */

public class App extends Application {

    private static final String DATA_BASE_NAME = "hupo";

    private static Context ctx;
    private static App app;
    private User user;
    private volatile AMapLocation location;

    public static Context getCtx() {
        return ctx;
    }

    private static Stack<Activity> atyStack = new Stack<Activity>();
    private static DisplayImageOptions options;
    private static DisplayImageOptions.Builder builder;

    public static DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
        Utils.init(this);
        ctx = getApplicationContext();
        PreferUtils.openFile(this);
        initImageLoader(getApplicationContext());
        initSimpleOption();
        setupDataBase(ctx);
        SqlScoutServer.create(this, getPackageName());
    }

    public AMapLocation getLocation() {
        return location;
    }

    public void setLocation(AMapLocation location) {
        this.location = location;
    }

    private void setupDataBase(Context context){
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context,DATA_BASE_NAME);
        Database db = openHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession(){
        return mDaoSession;
    }

    public static void push(Activity aty) {
        atyStack.push(aty);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        while (!atyStack.empty()) {
            atyStack.pop().finish();
        }
//        Process.killProcess(Process.myPid());

    }

    public static void closeSeries(Class<? extends  Activity> cls) {
        if (atyStack.isEmpty()) {
            return;
        }
        for (int pos = findPos(cls); pos < atyStack.size() - 1; ) {
            atyStack.pop().finish();
        }
    }

    private static int findPos(Class<? extends  Activity> cls) {
        for (int pos = 0; pos < atyStack.size(); pos++) {
            if (atyStack.get(pos).getClass() == cls) {
                return pos;
            }
        }
        throw new IllegalStateException();
    }
    public static Stack<Activity> getAtyStack(){
        return atyStack;
    }
    public static void pop(Activity aty) {
        atyStack.remove(aty);
    }

    private void initSimpleOption() {
        builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .resetViewBeforeLoading(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565);// ����ͼƬ�Ľ�������//
//                .resetViewBeforeLoading(true).displayer(new FadeInBitmapDisplayer(2000)).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565);// ����ͼƬ�Ľ�������//
    }


    public static DisplayImageOptions getRoundImage(Integer fallbackResource, Integer loadingResource) {
        builder.cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .resetViewBeforeLoading(true).displayer(new RoundedBitmapDisplayer(10)).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565);// ����ͼƬ�Ľ�������//
        options = builder.showImageOnLoading(loadingResource).
                showImageForEmptyUri(fallbackResource).build();
        return options;
    }

    @SuppressWarnings("deprecation")
    public void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "panoramic/image");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheSize(50 * 480 * 800)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .discCacheFileCount(100) //缓存的文件数量
                .discCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static DisplayImageOptions getSimpleOptions(Integer fallbackResource, Integer loadingResource) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        builder.cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .resetViewBeforeLoading(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565);
        options = builder.showImageOnLoading(loadingResource).
                showImageForEmptyUri(fallbackResource).build();
        return options;
    }

    public static synchronized App getInstance(){
        return app;
    }

    public synchronized User getUser() {
        if (user==null){
            user=PreferUtils.getBeanFromSp("user");
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void deleteAllDB(){
        getDaoSession().getDrawInvsDetailInfoDao().deleteAll();
        getDaoSession().getGetCarDrawInvsInfoDao().detachAll();
        getDaoSession().getOrderModelInfoDao().deleteAll();
        getDaoSession().getSendNoAdapterInfoDao().deleteAll();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this) ;
    }
}
