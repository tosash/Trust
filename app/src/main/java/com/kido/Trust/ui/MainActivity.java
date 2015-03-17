/*
 * Copyright 2015 Rudson Lima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kido.Trust.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.View;

import com.kido.Trust.R;
import com.kido.Trust.parse.ParseHelper;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;

import static com.kido.Trust.parse.ParseHelper.isUserLoggedIn;


public class MainActivity extends NavigationLiveo implements NavigationLiveoListener {

    public List<String> mListNameItem;

    @Override
    public void onUserInformation() {
        //User information here
        this.mUserName.setText("");
        this.mUserEmail.setText("");
        this.mUserPhoto.setImageResource(R.drawable.ic_no_user);
        this.mUserBackground.setImageResource(R.drawable.ic_user_background);
    }


    @Override
    public void onInt(Bundle savedInstanceState) {

        //Creation of the list items is here

        // set listener {required}
        this.setNavigationListener(this);

        //First item of the position selected from the list
        this.setDefaultStartPositionNavigation(1);

        // name of the list items
        mListNameItem = new ArrayList<>();
        mListNameItem.add(0, getString(R.string.jobs));
        mListNameItem.add(1, getString(R.string.tasks));
//        mListNameItem.add(4, getString(R.string.more_markers)); //This item will be a subHeader
        mListNameItem.add(2, getString(R.string.users));
        mListNameItem.add(3, getString(R.string.arhive));

        // icons list items
        List<Integer> mListIconItem = new ArrayList<>();
        mListIconItem.add(0, R.drawable.ic_report_black_24dp);
        mListIconItem.add(1, R.drawable.ic_drafts_black_24dp);
        mListIconItem.add(2, R.drawable.ic_star_black_24dp);
        mListIconItem.add(3, R.drawable.ic_delete_black_24dp);

//        mListIconItem.add(4, 0); //When the item is a subHeader the value of the icon 0

        //{optional} - Among the names there is some subheader, you must indicate it here
        List<Integer> mListHeaderItem = new ArrayList<>();
//        mListHeaderItem.add(4);

        //{optional} - Among the names there is any item counter, you must indicate it (position) and the value here
        SparseIntArray mSparseCounterItem = new SparseIntArray(); //indicate all items that have a counter
        mSparseCounterItem.put(0, 7);
        mSparseCounterItem.put(1, 12);

        //If not please use the FooterDrawer use the setFooterVisible(boolean visible) method with value false
        this.setFooterInformationDrawer(R.string.settings, R.drawable.ic_settings_black_24dp);

        this.setNavigationAdapter(mListNameItem, mListIconItem, mListHeaderItem, mSparseCounterItem);


        ParseHelper mParse = new ParseHelper(this, null);
        mParse.initParse();
        loginUser();
    }

    public void loginUser() {
        boolean loggedIn = isUserLoggedIn();
        if (!loggedIn)
            startActivity(new Intent(this, LoginActivity.class));
        else {
            mUserName.setText(ParseUser.getCurrentUser().getUsername());
        }
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

    @Override
    public void onItemClickNavigation(int position, int layoutContainerId) {
        Fragment mFragment = null;
        FragmentManager mFragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
                mFragment = new FragmentTreeList().newInstance(mListNameItem.get(position));
                break;
            case 1:
                mFragment = new FragmentTreeList().newInstance(mListNameItem.get(position));
                break;
            case 2:
                mFragment = new FragmentUserList().newInstance(mListNameItem.get(position));
                break;
            default:
                break;

        }

        if (mFragment != null)

        {
            mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment).commit();
        }

    }

    @Override
    public void onPrepareOptionsMenuNavigation(Menu menu, int position, boolean visible) {

        //hide the menu when the navigation is opens
        switch (position) {
            case 0:
                menu.findItem(R.id.menu_add).setVisible(!visible);
                menu.findItem(R.id.menu_search).setVisible(!visible);
                break;

            case 1:
                menu.findItem(R.id.menu_add).setVisible(!visible);
                menu.findItem(R.id.menu_search).setVisible(!visible);
                break;
        }
    }

    @Override
    public void onClickUserPhotoNavigation(View v) {
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
    }

    @Override
    public void onClickFooterItemNavigation(View v) {
        //footer onClick
        startActivity(new Intent(this, SettingsActivity.class));

    }


}