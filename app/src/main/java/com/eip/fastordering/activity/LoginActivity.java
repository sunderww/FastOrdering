package com.eip.fastordering.activity;

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

import com.eip.fastordering.R;
import com.eip.fastordering.customs.StockMenu;
import com.eip.fastordering.fragment.HistoryFragment;
import com.eip.fastordering.fragment.NotificationsFragment;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Map;

public class LoginActivity extends Activity {

    public static Socket _mSocket = null;
    public static final String _mIpServer = "http://163.5.84.184:4242";
    private ProgressDialog _mProgressDialog;

    /**
     * Create JSONObject with /event to ask to the server
     * @param URL
     * @return
     */
    static public JSONObject createObjectURL(String URL, Map<String, String> args) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("url", URL);
            if (args != null) {
                for (Map.Entry<String, String> entry : args.entrySet()) {
                    obj.put(entry.getKey(), entry.getValue());
                }
            }
        } catch (JSONException e) {
            Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
        }
        return obj;
    }

    /**
     * Create the content of the view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StockMenu.instance().init(getApplicationContext());

        //Add event listener to connexion button
        final Button button = (Button) findViewById(R.id.connexion_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                connectToServ();
            }
        });

        //Set key "enter" to validate to connect
//        StockMenu.instance().write("/pass", "$2a$10$7j7QLOwyEY6b4BBs.Zmcd.//D2QhNWRSVVINh4qaqZZQ5mgpoAKum");
        EditText addCourseText = (EditText) findViewById(R.id.field_pass);
        addCourseText.setText(StockMenu.instance().read("/pass"));

        addCourseText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
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

        if (_mSocket != null && _mSocket.connected()) {
            _mProgressDialog = new ProgressDialog(this);
            _mProgressDialog.setTitle(getBaseContext().getString(R.string.spinner_title));
            _mProgressDialog.setMessage(getBaseContext().getString(R.string.spinner_desc));
            _mProgressDialog.setCanceledOnTouchOutside(false);
            _mProgressDialog.show();

            fetchAllMenu();
        }
    }

    /**
     * Create the options menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle different case with keyboard/focus
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Close keyboard if touch outside
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        //Allow to loose focus of fields
        EditText fieldPass = (EditText) findViewById(R.id.field_pass);
        fieldPass.clearFocus();
        return true;
    }

    /**
     * Send data to serv and fetch datas
     * Setup sockets
     */
    private void connectToServ() {
        _mProgressDialog = new ProgressDialog(this);
        _mProgressDialog.setTitle(getBaseContext().getString(R.string.spinner_title));
        _mProgressDialog.setMessage(getBaseContext().getString(R.string.spinner_desc));
        _mProgressDialog.setCanceledOnTouchOutside(false);
        _mProgressDialog.show();

        //Init the socket
        try {
            _mSocket = IO.socket(_mIpServer);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //Connect and create the listeners for the socket
        _mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("LOGINACTIVITY", "CONNECT");
                JSONObject obj = new JSONObject();
                try {
                    obj.put("user_key", StockMenu.instance().read("/pass"));
                    //MongoDB
//                    obj.put("user_key", "$2a$10$BJxQp5KVCuui/trKjnnkneVldtHHweJoIshxwuZ3rFy6XGhFSFESq");

                    //MySQL
//                    obj.put("user_key", "$2a$10$L1ILmJrsRlonQnBfykNapuR9l./q/WQOAiq2o8B8nPailprqoq0Pu");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("LOGINACTIVITY", "LAUNCH AUTH");

                _mSocket.emit("authentication", obj, new Ack() {
                    @Override
                    public void call(Object... args) {
                        Log.d("LOGINACTIVITY", "SOCKETID ANSWER");
                        try {
                            Log.d("LOGINACTIVITY", "SOCKETID=" + args[0].toString());
                            JSONObject rep = new JSONObject(args[0].toString());
                            if (rep.getBoolean("answer")) {
                                EditText addCourseText = (EditText) findViewById(R.id.field_pass);
                                StockMenu.instance().write("/pass", addCourseText.getText().toString());
                                fetchAllMenu();
                            } else {
                                Log.d("LOGIN ACTIVITY", "Connection denied");
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast msg = Toast.makeText(LoginActivity.this, R.string.login_toast_error, Toast.LENGTH_LONG);
                                        msg.show();
                                    }
                                });
                                _mProgressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("SOCKET", "DECO");
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("SOCKET", "ERROR=SOCKET");
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast msg = Toast.makeText(LoginActivity.this, R.string.login_toast_error, Toast.LENGTH_LONG);
                        msg.show();
                    }
                });
                _mProgressDialog.dismiss();
            }
        }).on("receive_order", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("RECEIVEORDER", "NOTIF ORDER");
                Log.d("RECEIVEORDER", "NOTIF=" + args[0]);
                HistoryFragment.addOrderToList((JSONObject) args[0]);
            }
        }).on("notifications", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("NOTIF", "Notif recue = " + args[0]);
                NotificationsFragment.addNotificationToList((JSONObject) args[0], LoginActivity.this);
            }
        }).on("update", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                fetchAllMenu();
            }
        });
        _mSocket.connect();
    }

    /**
     * Fetch all components of the card of the restaurant
     */
    private void fetchAllMenu() {
        fetchOptions();
        fetchElements();
        fetchMenus();
        fetchCompos();
        fetchCats();
        fetchAlacarte();
        fetchLastOrders();
    }

    /**
     * Fetch the /options data
     */
    private void fetchOptions() {
        JSONObject obj = createObjectURL("/options", null);

        LoginActivity._mSocket.emit("get", obj, new Ack() {
            @Override
            public void call(Object... objects) {
                try {
                    Log.d("LOGINACTIVITY", "OPTIONS=" + objects[0].toString());
//					TODO Uncomment once done - Alexis
                    StockMenu.instance().write("/options", new JSONObject(objects[0].toString()).getJSONObject("body").toString());
//                    getSharedPreferences("DATACARD", 0).edit().putString("/options", new JSONObject(objects[0].toString()).toString()).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * Fetch the /elements data
     */
    private void fetchElements() {
        Log.d("LOGINACTIVITY", "FETCH ELEMENT");

        final JSONObject obj = createObjectURL("/elements", null);
        Log.d("TEST", "" + obj.toString());
        LoginActivity._mSocket.emit("get", obj, new Ack() {
            @Override
            public void call(Object... objects) {
                Log.d("LOGINACTIVITY", "ANSWER ELEMENT");

                try {
                    Log.d("LOGINACTIVITY", "ELEMENT=" + objects[0].toString());
                    StockMenu.instance().write("/elements", new JSONObject(objects[0].toString()).getJSONObject("body").toString());

//                    getSharedPreferences("DATACARD", 0).edit().putString("/elements", new JSONObject(objects[0].toString()).getJSONObject("body").toString()).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Fetch the /menus data
     */
    private void fetchMenus() {
        JSONObject obj = createObjectURL("/menus", null);
        Log.d("LOGINACTIVITY", "FETCH MENUS");

        LoginActivity._mSocket.emit("get", obj, new Ack() {
            @Override
            public void call(Object... objects) {
                Log.d("LOGINACTIVITY", "ANSWER MENUS");

                try {
                    Log.d("LOGINACTIVITY", "MENUS=" + objects[0].toString());
                    StockMenu.instance().write("/menus", new JSONObject(objects[0].toString()).getJSONObject("body").toString());

//                    getSharedPreferences("DATACARD", 0).edit().putString("/menus", new JSONObject(objects[0].toString()).getJSONObject("body").toString()).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Fetch the /compos data
     */
    private void fetchCompos() {
        JSONObject obj = createObjectURL("/compos", null);
        Log.d("LOGINACTIVITY", "FETCH COMPOS");

        LoginActivity._mSocket.emit("get", obj, new Ack() {
            @Override
            public void call(Object... objects) {
                Log.d("LOGINACTIVITY", "ANSWER COMPOS");
                try {
                    Log.d("LOGINACTIVITY", "COMPOS=" + objects[0].toString());
                    StockMenu.instance().write("/compos", new JSONObject(objects[0].toString()).getJSONObject("body").toString());

//                    getSharedPreferences("DATACARD", 0).edit().putString("/compos", new JSONObject(objects[0].toString()).getJSONObject("body").toString()).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Fetch the categories data
     */
    private void fetchCats() {
        JSONObject obj = createObjectURL("/cats", null);
        Log.d("LOGINACTIVITY", "FETCH CATS");

        LoginActivity._mSocket.emit("get", obj, new Ack() {
            @Override
            public void call(Object... objects) {
                Log.d("LOGINACTIVITY", "ANSWER CATS");
                try {
                    StockMenu.instance().write("/cats", new JSONObject(objects[0].toString()).getJSONObject("body").toString());
                    Log.d("LOGINACTIVITY", "CATS=" + objects[0].toString());

//                    getSharedPreferences("DATACARD", 0).edit().putString("/cats", new JSONObject(objects[0].toString()).getJSONObject("body").toString()).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                OrderFragment.fetchMenus(_mMenus, _mCompos, _mCats);
            }
        });
    }

    /**
     * Fetch the /alacarte data
     */
    private void fetchAlacarte() {
        JSONObject obj = createObjectURL("/alacarte", null);
        Log.d("LOGINACTIVITY", "FETCH ALACARTE");

        LoginActivity._mSocket.emit("get", obj, new Ack() {
            @Override
            public void call(Object... objects) {
                Log.d("LOGINACTIVITY", "ANSWER ALACARTE");
                try {
                    Log.d("LOGINACTIVITY", "ALACARTE=" + objects[0].toString());
                    StockMenu.instance().write("/alacarte", new JSONObject(objects[0].toString()).getJSONObject("body").toString());

//                    getSharedPreferences("DATACARD", 0).edit().putString("/alacarte", new JSONObject(objects[0].toString()).getJSONObject("body").toString()).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                OrderFragment.fetchCard(_mAlacarte);
                _mProgressDialog.dismiss();

                Intent mainActivity = new Intent(LoginActivity.this, Main.class);
                mainActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(mainActivity);
                finish();
            }
        });
    }

    /**
     * Fetch the last orders
     */
    private void fetchLastOrders() {

        LoginActivity._mSocket.emit("get_last_orders", new Ack() {
            @Override
            public void call(Object... objects) {
                try {
                    Log.d("HISTORY", "LAST ORDERS=" + new JSONObject(objects[0].toString()));
                    HistoryFragment.getLastOrders(new JSONObject(objects[0].toString()));
                } catch (JSONException e) {
                    Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
                }
            }
        });
    }
}
