package com.jxzhang.yourgrades.activity;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.SearchView;
import android.widget.Toast;

import com.jxzhang.yourgrades.fragment.GradeFragment;
import com.jxzhang.yourgrades.fragment.SchoolInfoFragment;
import com.jxzhang.yourgrades.fragment.TimeTableFragment;
import com.jxzhang.yourgrades.R;
import com.jxzhang.yourgrades.service.LongRunningRequestService;
import com.jxzhang.yourgrades.util.MyApplication;

import java.lang.reflect.Field;
import java.util.ArrayList;
/**
 * Created by J.X.Zhang on 2015/9/26.
 * 主界面Activity
 */
public class MainActivity extends FragmentActivity implements ActionBar.TabListener {
    //声明一个本地公共静态类
    public static MainActivity mMainActivityInstance = null;
    //滑动界面适配器
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;

    //初始化ServiceSharedPreference
    final SharedPreferences serviceSharedPreference = MyApplication.getContext().getSharedPreferences("service_password",MODE_PRIVATE);
    final SharedPreferences.Editor serviceSharedPreferenceEdit = serviceSharedPreference.edit();

    private long exitTime = 0;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取本类实例化对象
        mMainActivityInstance = this;

        //如果开关设置为true，那么在进入主界面的同时启动Service
        SharedPreferences isSwitchCheckedPreference = MyApplication.getContext().getSharedPreferences("is_switch_checked", MODE_PRIVATE);
        boolean is_checked = isSwitchCheckedPreference.getBoolean("is_checked", false);
        if (is_checked) {
            Intent serviceIntent = new Intent(this, LongRunningRequestService.class);
            startService(serviceIntent);
        }


        // 创建一个为APP的每一个主部件返回一个Fragment的适配器。
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        //创建一个ActionBar
        final ActionBar actionBar = getActionBar();
        //指定Home/Up不被激活，因为结构中不存在父类层次
        actionBar.setHomeButtonEnabled(true);
        //干掉图标
        actionBar.setDisplayShowHomeEnabled(false);
        //指定ActionBar使用Tab导航模式
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setIcon(R.drawable.null_icon);
        //创建一个ViewPager，为了使用两个部件之间的滑动添加适配器与监听器
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            /**
             *  When swiping between different app sections, select the corresponding tab.
             *  We can also use ActionBar.Tab#select() to do this if we have a reference to the Tab.
             *
             *  这里的Position本人理解为每个页面的position，从左到右第一个sections的position为0，以此类推
             *  当在app的不同部分滑动时，选择对应的Tab标签，如果持有Tab引用，我们也可以使用ActionBar.Tab对象的select()方法来实现
             */
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        //花式添加ActionBar.Tab 装装逼还行，然并卵
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
        setOverflowShowingAlways();     //默认屏蔽Menu键
    }

    /**
     * 自动屏蔽Menu键（Java反射）
     */
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断指定Service是否正在运行
     *
     * @return
     */
    public static boolean isLongRunningRequestServiceWorked() {
        ActivityManager myManager = (ActivityManager) MyApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(60);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals("com.jxzhang.yourgrades.service.LongRunningRequestService")) {
                return true;
            }
        }
        return false;
    }
    /**
     * 再按一次返回键退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), getString(R.string.exit_the_program), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint(Html.fromHtml("<font color = #eeeeee>" + getResources().getString(R.string.search_book_hint) + "</font>"));
        // 标识一个指定应用组件（SearchableActivity）,系统将通过Intent启动这个组件(Activity)
        ComponentName componentName = new ComponentName(this,
                LibraryActivity.class);
        // 通过组件来获取SearchableActivity的searchable.xml配置信息
        SearchableInfo searchableInfo = searchManager
                .getSearchableInfo(componentName);
        // 将SearchableActivity的配置信息配置给SearchView
        searchView.setSearchableInfo(searchableInfo);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Intent settingIntent = new Intent(this, SettingActivity.class);
                startActivity(settingIntent);
                break;
            case R.id.action_search:
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a tab enters the selected state.
     *
     * @param tab The tab that was selected
     * @param ft  A {@link FragmentTransaction} for queuing fragment operations to execute
     *            during a tab switch. The previous tab's unselect and this tab's select will be
     *            executed in a single transaction. This FragmentTransaction does not support
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    /**
     * Called when a tab exits the selected state.
     *
     * @param tab The tab that was unselected
     * @param ft  A {@link FragmentTransaction} for queuing fragment operations to execute
     *            during a tab switch. This tab's unselect and the newly selected tab's select
     *            will be executed in a single transaction. This FragmentTransaction does not
     */
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    /**
     * Called when a tab that is already selected is chosen again by the user.
     * Some applications may use this action to return to the top level of a category.
     *
     * @param tab The tab that was reselected.
     * @param ft  A {@link FragmentTransaction} for queuing fragment operations to execute
     *            once this method returns. This FragmentTransaction does not support
     */
    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * AppSectionsPagerAdapter适配器类需要覆写的第1个抽象方法：getItem
         * 返回一个Fragment，0代表从左到右第一个sections
         */
        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new GradeFragment();
                case 1:
                    return new TimeTableFragment();
                default:
                    return new SchoolInfoFragment();
            }
        }

        /**
         * AppSectionsPagerAdapter适配器类需要覆写的第2个抽象方法：getCount
         * 指定一共有几个Sections
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         * AppSectionsPagerAdapter适配器类需要覆写的第3个抽象方法：getPageTitle
         * 返回每个Sections的标题,也就是可以再这里定义Sections的标题
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "成绩信息";
                case 1:
                    return "班级课表";

                default:
                    return "更多";
            }
        }
    }
}
