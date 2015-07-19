package com.eip.fastordering.activity;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.eip.fastordering.R;
import com.eip.fastordering.fragment.AboutFragment;
import com.eip.fastordering.fragment.HistoryFragment;
import com.eip.fastordering.fragment.HomeFragment;
import com.eip.fastordering.fragment.NavigationDrawerFragment;
import com.eip.fastordering.fragment.NotificationsFragment;
import com.eip.fastordering.fragment.OrderFragment;


public class Main extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    static public Fragment[] _mTabFragments;
    static public NotificationCompat.Builder _mBuilder;
    static public NotificationManager _mNotifyMgr;
    /***
     * Attributes
     */

    private NavigationDrawerFragment _mNavigationDrawerFragment;
    private CharSequence _mTitle;

    /***
     * Methods
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.logo).setAutoCancel(true);
        Intent resultIntent = new Intent(this, Main.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        new Intent(),
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        _mBuilder.setContentIntent(resultPendingIntent);
        _mNotifyMgr = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        _mTabFragments = new Fragment[6];
        _mTabFragments[0] = new HomeFragment().newInstance(1);
        _mTabFragments[1] = new OrderFragment().newInstance(2, null, null);
        _mTabFragments[2] = new NotificationsFragment().newInstance(3);
        _mTabFragments[3] = new HistoryFragment().newInstance(4);
        _mTabFragments[4] = new AboutFragment().newInstance(5);
        _mTabFragments[5] = null;

        setContentView(R.layout.activity_main);

        _mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        _mTitle = getTitle();

        // Set up the drawer.
        _mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
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
            case 3:
                _mTitle = getString(R.string.title_section4);
                break;
            case 4:
                _mTitle = getString(R.string.title_section5);
                break;
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
