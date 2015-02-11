package com.eip.fastordering;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;


public class LoginActivity extends Activity {

    /***
     * Attributes
     */

    public static SocketIO _mSocket = null;
    private Context _mContext = null;
    private final String _mIpServer = "http://alexis-semren.com:1337";

    static JSONObject menus;
    static JSONArray compos;
    static JSONObject cats;
    static JSONObject alacarte;

    /***
     * Methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mContext = this;
        setContentView(R.layout.activity_login);

        //Add event listener to connexion button
        final Button button = (Button) findViewById(R.id.connexion_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectToServ();
            }
        });

        //Set key "enter" to validate to connect
        EditText addCourseText = (EditText) findViewById(R.id.field_pass);
        addCourseText.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            connectToServ();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        //Init the socket
        try {
            _mSocket = new SocketIO(_mIpServer);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Close keyboard if touch outside
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        //Allow to loose focus of fields
        EditText fieldLogin = (EditText) findViewById(R.id.field_login);
        fieldLogin.clearFocus();

        EditText fieldPass = (EditText) findViewById(R.id.field_pass);
        fieldPass.clearFocus();

        return true;
    }

    private void connectToServ() {

//        Intent mainActivity = new Intent(LoginActivity.this, Main.class);
//        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mainActivity);

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle(getBaseContext().getString(R.string.spinner_title));
        progress.setMessage(getBaseContext().getString(R.string.spinner_desc));
        progress.show();

        //Connect and create the listeners for the socket
        _mSocket.connect(new IOCallback() {
            @Override
            public void onMessage(JSONObject json, IOAcknowledge ack) {
                try {
                    Log.d("SOCKET", "DATA JSON: " + json.toString(2));
                } catch (JSONException e) {
                    Log.d("SOCKET", "ERROR JSON");
                }
            }

            @Override
            public void onMessage(String data, IOAcknowledge ack) {
                Log.d("SOCKET", "DATA SERV: " + data);
            }

            @Override
            public void onError(SocketIOException socketIOException) {
                Log.d("SOCKET", "ERROR");

                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast msg = Toast.makeText(LoginActivity.this, R.string.login_toast_error, Toast.LENGTH_LONG);
                        msg.show();
                    }
                });
                progress.dismiss();

//                Intent mainActivity = new Intent(LoginActivity.this, Main.class);
//                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(mainActivity);
            }

            @Override
            public void onDisconnect() {
                Log.d("SOCKET", "DECO");
            }

            @Override
            public void onConnect() {
                Log.d("SOCKET", "CO OK");
                JSONObject msg = new JSONObject();
                try {
                    msg.put("name", ((EditText)findViewById(R.id.field_login)).getText().toString());
                    msg.put("pass", ((EditText)findViewById(R.id.field_pass)).getText().toString());
                } catch (JSONException e) {

                }
                Log.d("SOCKET", "MSG CREATED");
//                _mSocket.send(msg);
//                Log.d("SOCKET", "MSG SENT");
//
//                //TODO
//                //Check message callback check login
//                JSONObject obj = new JSONObject();
//                try {
//                    obj.put("url", String.format("/dish/read"));
//                } catch (JSONException e) {
//
//                }
//
//                _mSocket.emit("get", new IOAcknowledge() {
//                    @Override
//                    public void ack(Object... objects) {
//                        Log.d("GET", "" + objects[0]);
//                    }
//                }, obj);

                //
                // Fetch /elements
                //
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

                //
                // Fetch /menus
                //
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

                //
                // Fetch /compos
                //
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

                //
                // Fetch /cats
                //
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

                //
                // Fetch /alacarte
                //
                try {
                    obj.put("url", String.format("/alacarte"));
                } catch (JSONException e) {
                }

                LoginActivity._mSocket.emit("get", new IOAcknowledge() {
                    @Override
                    public void ack(Object... objects) {
                        Log.d("ALACARTE", "" + objects[0]);
                        try {
                            alacarte = new JSONObject(objects[0].toString());
                        } catch (JSONException e) {

                        }
                        Log.d("ALACARTE", alacarte.toString());
                        OrderFragment.fetchCard(alacarte, cats);
                        Log.d("FETCH", "DONE");
                        progress.dismiss();
                        Intent mainActivity = new Intent(LoginActivity.this, Main.class);
                        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainActivity);
                    }
                }, obj);


                //Change activity after login ok
//                Intent mainActivity = new Intent(LoginActivity.this, Main.class);
//                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(mainActivity);
            }

            @Override
            public void on(String event, IOAcknowledge ack, Object... args) {
                Log.d("SOCKET", "EVENT " + event);
            }
        });
    }
}
