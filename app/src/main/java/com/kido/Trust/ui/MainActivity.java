package com.kido.Trust.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.kido.Trust.R;
import com.kido.Trust.parse.ParseHelper;
import com.parse.ParseUser;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;

import static com.kido.Trust.parse.ParseHelper.isUserLoggedIn;


public class MainActivity extends ActionBarActivity implements ViewAnimator.ViewAnimatorListener
        , OnMenuItemClickListener
{

    public static final String CLOSE = "Close";
    public static final String TASKS = "Tasks";
    public static final String USERS = "Users";
    public static final String ARHIVE = "Arhive";
    public static final String SETTINGS = "Settings";
    public static final String LOGOUT = "LogOut";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private FragmentTreeList fTreeList;
    private ViewAnimator viewAnimator;
    private int res = R.drawable.icn_settings;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fTreeList = FragmentTreeList.newInstance(R.drawable.icn_settings);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fTreeList)
                .commit();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        setActionBar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, fTreeList, drawerLayout, this);

        ParseHelper mParse = new ParseHelper(this, null);
        mParse.initParse(getIntent());
        loginUser();
    }

    public void loginUser() {
        boolean loggedIn = isUserLoggedIn();
        if (!loggedIn)
            startActivity(new Intent(this, LoginActivity.class));
        else {
//            mUserName.setText(ParseUser.getCurrentUser().getUsername());
        }
    }

    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(this.CLOSE, R.drawable.icn_quit);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(this.TASKS, R.drawable.icn_tasks);
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(this.USERS, R.drawable.icn_users);
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(this.ARHIVE, R.drawable.check_task);
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(this.SETTINGS, R.drawable.icn_settings);
        list.add(menuItem4);
        SlideMenuItem menuItem5 = new SlideMenuItem(this.LOGOUT, R.drawable.icn_logout);
        list.add(menuItem5);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.exit_title)
                .setMessage(R.string.exit_quetion)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        System.exit(RESULT_OK);
                        finishAffinity();
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();
    }


    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }


    private ScreenShotable replaceFragment(ScreenShotable screenShotable, int topPosition) {
//        this.res = this.res == R.drawable.ic_no_clientphoto ? R.drawable.ic_no_clientphoto : R.drawable.ic_no_clientphoto;
        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        SupportAnimator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        findViewById(R.id.content_overlay).setBackgroundDrawable(new BitmapDrawable(getResources(), screenShotable.getBitmap()));
        animator.start();


//        Fragment mFragment = new Fragment();
//        switch (topPosition) {
//            case 1:
//                mFragment = FragmentTreeList.newInstance(this.res);
//                break;
//            case 2:
//                mFragment = FragmentUserList.newInstance("");
//                break;
//            case 3:
//                break;
//            case 4:
//                startActivity(new Intent(this, SettingsActivity.class));
//                break;
//            case 5:
//                break;
//
//        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, (Fragment) screenShotable).commit();
        return screenShotable;
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        switch (slideMenuItem.getName()) {
            case CLOSE:
                return screenShotable;
            case TASKS:
                return replaceFragment((ScreenShotable) FragmentTreeList.newInstance(1), position);
            case USERS:
                return replaceFragment((ScreenShotable) FragmentUserList.newInstance(""), position);
            case ARHIVE:
                return replaceFragment((ScreenShotable) FragmentTreeList.newInstance(1), position);
            case SETTINGS:
                return replaceFragment((ScreenShotable) new SettingsActivity(), position);
            case LOGOUT:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.log_out_title)
                        .setMessage(R.string.log_out_quetion)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ParseUser.logOut();
                                loginUser();
                            }

                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                return screenShotable;
            default:
               return screenShotable;
        }
    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }

    @Override
    public void onMenuItemClick(View view, int i) {
        Fragment f =  getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (f instanceof FragmentTreeList) {
            Handler handler=((FragmentTreeList)f).handlerMenu;
            Message msg = new Message();
            msg.what=i;
            msg.obj=view;
            handler.sendMessage(msg);
        }
    }

}
