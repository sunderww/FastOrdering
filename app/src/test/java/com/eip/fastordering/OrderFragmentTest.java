package com.eip.fastordering;

import com.eip.fastordering.activity.Main;
import com.eip.fastordering.fragment.OrderFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "../app/src/main/AndroidManifest.xml", sdk = 18)
public class OrderFragmentTest {

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
    public void testCorrectFragment() throws Exception {
        assertTrue(activity != null);

        assertTrue(activity._mTabFragments[1] != null);
        assertTrue(activity._mTabFragments[1] instanceof OrderFragment);

//        ListView listView = (ListView) activity.findViewById(R.id.home_list_order);
//        assertTrue(listView != null);
//        assertTrue(listView.getAdapter().isEmpty());
    }
}

