package com.eip.fastordering;

import android.widget.ImageButton;
import android.widget.ListView;

import com.eip.fastordering.activity.Main;
import com.eip.fastordering.fragment.HomeFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "../app/src/main/AndroidManifest.xml", sdk = 18)
public class HomeFragmentTest {

    private static Main activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.setupActivity(Main.class);
    }

    @After
    public void tearDown() throws Exception {
        Robolectric.reset();
    }

    /**
     * Check LoginActivity creation
     * @throws Exception
     */
    @Test
    public void creationMainActivity() throws Exception {
        assertTrue(Robolectric.buildActivity(Main.class).create().get() != null);
    }

    /**
     * Check listView for orders
     * @throws Exception
     */
    @Test
    public void testLastOrders() throws Exception {
        assertTrue(activity != null);

        assertTrue(activity._mTabFragments[0] != null);
        assertTrue(activity._mTabFragments[0] instanceof HomeFragment);

        ListView listView = (ListView) activity.findViewById(R.id.home_list_order);
        assertTrue(listView != null);
        assertTrue(listView.getAdapter().isEmpty());
    }

    /**
     * Check listView for notifications
     * @throws Exception
     */
    @Test
    public void testLastNotifications() throws Exception {
        assertTrue(activity != null);

        assertTrue(activity._mTabFragments[0] != null);
        assertTrue(activity._mTabFragments[0] instanceof HomeFragment);

        ListView listView = (ListView) activity.findViewById(R.id.home_list_notif);

        assertTrue(listView != null);
        assertTrue(listView.getAdapter().isEmpty());
    }

    @Test
    public void testOrderButton() throws Exception {
        Main activity = Robolectric.setupActivity(Main.class);
        assertTrue(activity != null);

        startFragment(activity._mTabFragments[0]);

        ImageButton button = (ImageButton) activity.findViewById(R.id.home_rectangle_green);
        assertTrue(button != null);
    }
}

