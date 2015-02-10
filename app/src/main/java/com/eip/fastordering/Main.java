package com.eip.fastordering;

import android.app.Activity;
import android.net.Uri;
import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.IOAcknowledge;


public class Main extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /***
     * Attributes
     */

    private NavigationDrawerFragment _mNavigationDrawerFragment;
    private CharSequence _mTitle;
    static public Fragment[] _mTabFragments;

    static JSONObject menus;
    static JSONArray compos;
    static JSONObject cats;

    /***
     * Methods
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _mTabFragments = new Fragment[6];
        _mTabFragments[0] = new HomeFragment().newInstance(1);
        _mTabFragments[1] = new OrderFragment().newInstance(2, null);
        //_mTabFragments[2] = new ModifyFragment().newInstance(3);
        _mTabFragments[2] = new NotificationsFragment().newInstance(3);
        _mTabFragments[3] = new HistoryFragment().newInstance(4);
        //mTabFragments[5] = new OptionsFragment().newInstance(6);
        _mTabFragments[4] = new AboutFragment().newInstance(5);
        _mTabFragments[5] = new LogoutFragment().newInstance(6);

        setContentView(R.layout.activity_main);

        _mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        _mTitle = getTitle();

        // Set up the drawer.
        _mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        JSONObject obj = new JSONObject();
        try {
            obj.put("url", String.format("/elements"));
        } catch (JSONException e) {
        }

        LoginActivity._mSocket.emit("get", new IOAcknowledge() {
            @Override
            public void ack(Object... objects) {
                Log.d("ELEMENTS", "" + objects[0]);
                JSONObject rep = null;
                try {
                    rep = new JSONObject(objects[0].toString());
                } catch (JSONException e) {

                }
                Log.d("ELEMENTS 2", rep.toString());
                OrderFragment.fetchElements(rep);
            }
        }, obj);

        try {
            obj.put("url", String.format("/menus"));
        } catch (JSONException e) {
        }

        LoginActivity._mSocket.emit("get", new IOAcknowledge() {
            @Override
            public void ack(Object... objects) {
                Log.d("MENUS", "" + objects[0]);
                try {
                    menus = new JSONObject(objects[0].toString());
                } catch (JSONException e) {

                }
                Log.d("MENUS2", menus.toString());
            }
        }, obj);

        try {
            obj.put("url", String.format("/compos"));
        } catch (JSONException e) {
        }

        LoginActivity._mSocket.emit("get", new IOAcknowledge() {
            @Override
            public void ack(Object... objects) {
                Log.d("COMPOS", "" + objects[0]);
                try {
                    compos = new JSONArray(objects[0].toString());
                } catch (JSONException e) {

                }
                Log.d("COMPOS2", compos.toString());
            }
        }, obj);

        try {
            obj.put("url", String.format("/cats"));
        } catch (JSONException e) {
        }

        LoginActivity._mSocket.emit("get", new IOAcknowledge() {
            @Override
            public void ack(Object... objects) {
                Log.d("CATS", "" + objects[0]);
                try {
                    cats = new JSONObject(objects[0].toString());
                } catch (JSONException e) {

                }
                Log.d("CATS2", cats.toString());
                OrderFragment.fetchMenus(menus, compos, cats);
                Log.d("FETCH", "DONE");
            }
        }, obj);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, _mTabFragments[position])
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                _mTitle = getString(R.string.title_section1);
                break;
            case 2:
                _mTitle = getString(R.string.title_section2);
                break;
            //case 3:
             //   _mTitle = getString(R.string.title_section3);
            //    break;
            case 3:
                _mTitle = getString(R.string.title_section4);
                break;
            case 4:
                _mTitle = getString(R.string.title_section5);
                break;
            //case 6:
            //   mTitle = getString(R.string.title_section6);
            //    break;
            case 5:
                _mTitle = getString(R.string.title_section8);
                break;
            case 6:
                _mTitle = getString(R.string.title_section7);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(_mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!_mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
