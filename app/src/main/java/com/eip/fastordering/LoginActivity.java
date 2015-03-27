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
    private ProgressDialog _mProgressDialog;

    static JSONObject _mMenus;
    static JSONObject _mCompos;
    static JSONObject _mCats;
    static JSONObject _mAlacarte;
    static JSONObject _mLastOrders;

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
        _mProgressDialog = new ProgressDialog(this);
        _mProgressDialog.setTitle(getBaseContext().getString(R.string.spinner_title));
        _mProgressDialog.setMessage(getBaseContext().getString(R.string.spinner_desc));
        _mProgressDialog.setCanceledOnTouchOutside(false);
        _mProgressDialog.show();

        //Init the socket
        try {
            _mSocket = new SocketIO(_mIpServer);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }        //Connect and create the listeners for the socket
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
                Log.d("SOCKET", "ERROR=" + socketIOException.toString());

                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast msg = Toast.makeText(LoginActivity.this, R.string.login_toast_error, Toast.LENGTH_LONG);
                        msg.show();
                    }
                });
                _mProgressDialog.dismiss();
            }

            @Override
            public void onDisconnect() {
                Log.d("SOCKET", "DECO");
            }

            @Override
            public void onConnect() {
                JSONObject msg = new JSONObject();
                try {
                    msg.put("name", ((EditText)findViewById(R.id.field_login)).getText().toString());
                    msg.put("pass", ((EditText)findViewById(R.id.field_pass)).getText().toString());
                } catch (JSONException e) {
                    Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
                }

                //Data to fetch from server
                fetchAllMenu();
                //TODO Uncomment once done
                //fetchLastOrders();
            }

            @Override
            public void on(String event, IOAcknowledge ack, Object... args) {
                Log.d("SOCKET ON", "EVENT " + event);
                eventsToListen(event, args);
            }
        });
    }

    private void fetchAllMenu() {
        fetchElements();
        fetchMenus();
        fetchCompos();
        fetchCats();
        fetchAlacarte();
    }

    private void fetchElements() {
        JSONObject obj = createObjectURL("/elements");

        LoginActivity._mSocket.emit("get", new IOAcknowledge() {
            @Override
            public void ack(Object... objects) {
                JSONObject rep = null;
                try {
                    rep = new JSONObject(objects[0].toString());
                } catch (JSONException e) {
                    Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
                }
                OrderFragment.fetchElements(rep);
            }
        }, obj);
    }

    private void fetchMenus() {
        JSONObject obj = createObjectURL("/menus");

        LoginActivity._mSocket.emit("get", new IOAcknowledge() {
            @Override
            public void ack(Object... objects) {
                try {
                    _mMenus = new JSONObject(objects[0].toString());
                } catch (JSONException e) {
                    Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
                }
            }
        }, obj);
    }

    private void fetchCompos() {
        JSONObject obj = createObjectURL("/compos");

        LoginActivity._mSocket.emit("get", new IOAcknowledge() {
            @Override
            public void ack(Object... objects) {
                try {
                    _mCompos = new JSONObject(objects[0].toString());
                } catch (JSONException e) {
                    Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
                }
            }
        }, obj);
    }

    private void fetchCats() {
        JSONObject obj = createObjectURL("/cats");

        LoginActivity._mSocket.emit("get", new IOAcknowledge() {
            @Override
            public void ack(Object... objects) {
                try {
                    _mCats = new JSONObject(objects[0].toString());
                } catch (JSONException e) {
                    Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
                }
                OrderFragment.fetchMenus(_mMenus, _mCompos, _mCats);
            }
        }, obj);
    }

    private void fetchAlacarte() {
        JSONObject obj = createObjectURL("/alacarte");

        LoginActivity._mSocket.emit("get", new IOAcknowledge() {
            @Override
            public void ack(Object... objects) {
                try {
                    _mAlacarte = new JSONObject(objects[0].toString());
                } catch (JSONException e) {
                    Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
                }
                OrderFragment.fetchCard(_mAlacarte);
//                OrderFragment.fetchCard(_mAlacarte, _mCats);
                _mProgressDialog.dismiss();

                Intent mainActivity = new Intent(LoginActivity.this, Main.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivity);
            }
        }, obj);
    }

    private void fetchLastOrders() {
        JSONObject obj = createObjectURL("/get_last_orders");

        LoginActivity._mSocket.emit("get", new IOAcknowledge() {
            @Override
            public void ack(Object... objects) {
                try {
                    _mLastOrders = new JSONObject(objects[0].toString());
                } catch (JSONException e) {
                    Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
                }
                HistoryFragment.getLastOrders(_mLastOrders);
            }
        }, obj);
    }

    static public JSONObject createObjectURL(String URL) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("url", URL);
        } catch (JSONException e) {
            Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
        }
        return obj;
    }

    private void eventsToListen(String event, Object... args) {
        switch (event) {
            case "receive_order":
                HistoryFragment.addOrderToList((JSONObject)args[0]);
                break;

            case "notifications":
                NotificationsFragment.addNotificationToList((JSONObject)args[0]);
                break;

            case "update":
                fetchAllMenu();
                break;

            default:
                break;
        }
    }
}
