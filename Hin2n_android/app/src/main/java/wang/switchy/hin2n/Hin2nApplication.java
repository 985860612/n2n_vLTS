package wang.switchy.hin2n;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import wang.switchy.hin2n.storage.db.base.DaoMaster;
import wang.switchy.hin2n.storage.db.base.DaoSession;
import wang.switchy.hin2n.tool.N2nTools;

import com.tencent.bugly.Bugly;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;


/**
 * Created by janiszhang on 2018/4/19.
 */

public class Hin2nApplication extends MultiDexApplication {

    public Context AppContext;

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    static {
        System.loadLibrary("slog");
        System.loadLibrary("uip");
        System.loadLibrary("n2n_v2s");
        System.loadLibrary("n2n_v2");
        System.loadLibrary("n2n_v1");
        System.loadLibrary("edge_v2s");
        System.loadLibrary("edge_v2");
        System.loadLibrary("edge_v1");
        System.loadLibrary("edge_jni");
    }

    //静态单例
    public static Hin2nApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        AppContext = this;

        setDatabase();

//        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.init(this, N2nTools.getMetaData(this, "UMENG_APPKEY"), N2nTools.getMetaData(this, "UMENG_CHANNEL"), UMConfigure.DEVICE_TYPE_PHONE, "");

        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        Bugly.init(getApplicationContext(), N2nTools.getMetaData(this,"BUGLY_APPID"), false);
        initShare();
    }

    private void initShare() {
        Log.e("zhangbzshare" ,"wx : " + N2nTools.getMetaData(this, "SHARE_WX_APPID")  +"::" + N2nTools.getMetaData(this, "SHARE_WX_APPSECRET"));
//        PlatformConfig.setWeixin(N2nTools.getMetaData(this, "SHARE_WX_APPID"), N2nTools.getMetaData(this, "SHARE_WX_APPSECRET"));
        PlatformConfig.setWeixin("wx5cdb9d03860cb1a2", "a836be9a4c0757d69e7bf506834803c5");
    }

    public static Hin2nApplication getInstance(){
        return instance;
    }

    /**
     * 设置greenDao
     */
    private void setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = new DaoMaster.DevOpenHelper(this, "N2N-db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
