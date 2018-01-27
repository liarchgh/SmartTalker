package wapchief.com.collectiondemo.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import wapchief.com.collectiondemo.MainActivity;
import wapchief.com.collectiondemo.framework.BaseApplication;
import wapchief.com.collectiondemo.utils.ToastsUtils;

/**
 * Created by Wu on 2017/7/3.
 */
public class RootActivity extends ListActivity {

    private String[] mTitles = new String[]{
            "MainActivity+SideBar+GreenDao数据库",
            "RecyclerView+swipeRefrashlayout+Header",
            "TagFlowLayout热门搜索",
            "okhttp+Retrofit+RxJava查询手机号码归属地",
            "Glide+Picasso请求加载网络图片",
            "调用系统相机、图库、裁剪、上传图片（适配7.0）",
            "JPush（极光）即时通讯",
            "GreenDao+Flow仿热门搜索，本地缓存",
            "SlidingUpPanel仿美团配送",
            "几种按钮计时器的实现方法",
            "LottieAnimation动画库",
            "仿美团、饿了么购物车本地存储数据",
            "XhsEmoticonsKeyboard仿qq微信键盘",
            "VideoPlayer播放器",
            "Voice转文本(讯飞语音+百度语音)",
            "MP-Chart图表、走势图"
//            "随机云圆形标签"
    };

    private Class[] mActivities = new Class[]{
            MainActivity.class,
            RecyclerViewActivity.class,
            FlowLayoutActivity.class,
            OkhttpRetrofitActivity.class,
            GlidePicassoActivity.class,
            UpdatePhotoActivity.class,
            JPushIMActivity.class,
            SearchViewGreenDaoActivity.class,
            SlidingUpMeiTuanActivity.class,
            TimerButtonActivity.class,
            LottieAnimationActivity.class,
            ShoppingCartActivity.class,
            KeyBoardActivity.class,
            VideoPlayerActivity.class,
            Voice2TextActivity.class,
            MPChartActivity.class,
//            CloudTagActivity.class
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mTitles));
        Log.e("id===push===", JPushInterface.getRegistrationID(BaseApplication.mBaseApplication));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        startActivity(new Intent(this, mActivities[position]));
    }


    /**
     * 单击回退
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    /**
     * 双击退出
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            ToastsUtils.showShort(this,"再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
            System.exit(0);
        }
    }
}
