package com.eip.fastordering;

import android.app.Activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "app/src/main/AndroidManifest.xml", emulateSdk = 18)
public class MainTest {

    @Test
    public void creationLoginActivity() throws Exception {
        assertTrue(Robolectric.buildActivity(LoginActivity.class).create().get() != null);
    }

    @Test
    public void clickLoginActivity() throws Exception {
        Activity activity = Robolectric.setupActivity(LoginActivity.class);
        assertTrue(activity.findViewById(R.id.connexion_button).performClick());
    }
}

