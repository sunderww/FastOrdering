package com.eip.fastordering;

import android.widget.Button;
import android.widget.EditText;

import com.eip.fastordering.activity.LoginActivity;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.net.URISyntaxException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "../app/src/main/AndroidManifest.xml", sdk = 18)
public class LoginTest {

    private static LoginActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.setupActivity(LoginActivity.class);
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
    public void testCreateLoginActivity() throws Exception {
        assertTrue(Robolectric.buildActivity(LoginActivity.class).create().get() != null);
    }

    /**
     * Check creation socket with correct argument
     * @throws Exception
     */
    @Test
    public void testCreateSocketCorrectIP() throws Exception {
        IO.socket(LoginActivity._mIpServer);
    }

    /**
     * Check creation socket with wrong argument
     * @throws Exception
     */
    @Test(expected = URISyntaxException.class)
    public void testCreateSocketBadSyntaxIP() throws Exception {
        IO.socket("163.5.84.18:4242");
    }

    /**
     * Check put of password
     * @throws Exception
     */
    @Test
    public void testPutPassword() throws Exception {
        assertTrue(activity != null);

        EditText editText = (EditText) activity.findViewById(R.id.field_pass);
        assertTrue(editText != null);

        editText.setText("$2a$10$Hkq1oadAQtH8FR80B7OXtesEYBIGRgi7dQxWFY78GGP89zwQtQGdG");
    }

    /**
     * Check click button of login activity
     * @throws Exception
     */
    @Test
    public void testClickLoginActivity() throws Exception {
        assertTrue(activity != null);

        Button button = (Button) activity.findViewById(R.id.connexion_button);
        assertTrue(button != null);

        assertTrue(button.performClick());
    }

    /**
     * Check authentication success
     * @throws Exception
     */
    @Test
    public void testSocketLoginSuccess() throws Exception {
        //Init the socket
        try {
            final Socket _mSocket = IO.socket(LoginActivity._mIpServer);
            if (_mSocket != null) {
                _mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONObject obj = new JSONObject();
                        try {
                            //MongoDB
                            obj.put("user_key", "$2a$10$7j7QLOwyEY6b4BBs.Zmcd.//D2QhNWRSVVINh4qaqZZQ5mgpoAKum");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        _mSocket.emit("authentication", obj, new Ack() {
                            @Override
                            public void call(Object... args) {
                                try {
                                    JSONObject rep = new JSONObject(args[0].toString());
                                    assertTrue(rep.getBoolean("answer"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
                _mSocket.connect();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Thread.sleep(2000);
    }

    /**
     * Check authentication fail
     * @throws Exception
     */
    @Test
    public void testSocketLoginFail() throws Exception {
        //Init the socket
        try {
            final Socket _mSocket = IO.socket(LoginActivity._mIpServer);
            if (_mSocket != null) {
                _mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONObject obj = new JSONObject();
                        try {
                            //MongoDB
                            obj.put("user_key", "$2a$10$7j7QLOwyEY6b4BBs.Zmcd.//D2QhNWRSVVINh4qaqZZQ5mgpoAKu");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        _mSocket.emit("authentication", obj, new Ack() {
                            @Override
                            public void call(Object... args) {
                                try {
                                    JSONObject rep = new JSONObject(args[0].toString());
                                    assertFalse(rep.getBoolean("answer"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
                _mSocket.connect();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Thread.sleep(2000);
    }

}

