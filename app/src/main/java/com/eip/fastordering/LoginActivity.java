package com.eip.fastordering;

import android.app.Activity;
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

    public SocketIO _mSocket = null;
    private Context _mContext = null;
    private final String _mIpServer = "http://alexis-semren.com:1337";

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

        Intent mainActivity = new Intent(LoginActivity.this, Main.class);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivity);


        //Connect and create the listeners for the socket
//        _mSocket.connect(new IOCallback() {
//            @Override
//            public void onMessage(JSONObject json, IOAcknowledge ack) {
//                try {
//                    Log.d("SOCKET", "DATA JSON: " + json.toString(2));
//                } catch (JSONException e) {
//                    Log.d("SOCKET", "ERROR JSON");
//                }
//            }
//
//            @Override
//            public void onMessage(String data, IOAcknowledge ack) {
//                Log.d("SOCKET", "DATA SERV: " + data);
//            }
//
//            @Override
//            public void onError(SocketIOException socketIOException) {
//                Log.d("SOCKET", "ERROR");
//
//                LoginActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast msg = Toast.makeText(LoginActivity.this, R.string.login_toast_error, Toast.LENGTH_LONG);
//                        msg.show();
//                    }
//                });
//
//                Intent mainActivity = new Intent(LoginActivity.this, Main.class);
//                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(mainActivity);
//            }
//
//            @Override
//            public void onDisconnect() {
//                Log.d("SOCKET", "DECO");
//            }
//
//            @Override
//            public void onConnect() {
//                Log.d("SOCKET", "CO OK");
//                JSONObject msg = new JSONObject();
//                try {
//                    msg.put("name", ((EditText)findViewById(R.id.field_login)).getText().toString());
//                    msg.put("pass", ((EditText)findViewById(R.id.field_pass)).getText().toString());
//                } catch (JSONException e) {
//
//                }
//                Log.d("SOCKET", "MSG CREATED");
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
//                //Change activity after login ok
//                Intent mainActivity = new Intent(LoginActivity.this, Main.class);
//                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(mainActivity);
//            }
//
//            @Override
//            public void on(String event, IOAcknowledge ack, Object... args) {
//                Log.d("SOCKET", "EVENT " + event);
//            }
//        });
    }
}
