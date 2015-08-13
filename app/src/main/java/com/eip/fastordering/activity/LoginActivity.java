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

	public static Socket _mSocket   = null;
	private final String _mIpServer = "http://163.5.84.184:4242";
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

		//Add event listener to connexion button
		final Button button = (Button) findViewById(R.id.connexion_button);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				connectToServ();
			}
		});

		//Set key "enter" to validate to connect
		EditText addCourseText = (EditText) findViewById(R.id.field_pass);
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
			IO.Options opt = new IO.Options();
			opt.query = "user_key=" + "55cccc32f80d3658724d6f7e";
			_mSocket = IO.socket(_mIpServer, opt);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		//Connect and create the listeners for the socket
		_mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Log.d("LOGINACTIVITY", "CONNECT");
//				JSONObject rep = null;
//				try {
//					Log.d("LOGINACTIVITY", "TRYING");
//					rep = new JSONObject(args[0].toString());
//					Log.d("LOGINACTIVITY", "END");
//				} catch (JSONException e) {
//					Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
//				}
//				handleAuthentification(rep);
				fetchAllMenu();
			}
		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Log.d("SOCKET", "DECO");
			}
		}).on(Socket.EVENT_ERROR, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
//				Log.d("SOCKET", "ERROR=" + socketIOException.toString());
//				socketIOException.printStackTrace();

				LoginActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast msg = Toast.makeText(LoginActivity.this, R.string.login_toast_error, Toast.LENGTH_LONG);
						msg.show();
					}
				});
//				_mProgressDialog.dismiss();
			}
		}).on("receive_order", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				HistoryFragment.addOrderToList((JSONObject) args[0]);
			}
		}).on("notifications", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				NotificationsFragment.addNotificationToList((JSONObject) args[0]);
			}
		}).on("update", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				fetchAllMenu();
			}
		});
		_mSocket.connect();
		fetchElements();

//		_mSocket.connect(new IOCallback() {
//			@Override
//			public void onMessage(JSONObject json, IOAcknowledge ack) {
//				Log.d("SOCKET", "DATA JSON: " + json.toString());
//			}

//			@Override
//			public void onMessage(String data, IOAcknowledge ack) {
//				Log.d("SOCKET", "DATA SERV: " + data);
//			}

//			@Override
//			public void onError(SocketIOException socketIOException) {
//				Log.d("SOCKET", "ERROR=" + socketIOException.toString());
//				socketIOException.printStackTrace();
//
//				LoginActivity.this.runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						Toast msg = Toast.makeText(LoginActivity.this, R.string.login_toast_error, Toast.LENGTH_LONG);
//						msg.show();
//					}
//				});
//				_mProgressDialog.dismiss();
//			}
//
//			@Override
//			public void onDisconnect() {
//				Log.d("SOCKET", "DECO");
//			}

//			@Override
//			public void onConnect() {
//				Map<String, String> args = new HashMap<String, String>();
//				args.put("user_key", ((EditText) findViewById(R.id.field_pass)).getText().toString());
//				args.put("user_key", "55cccc32f80d3658724d6f7e");
//
//				JSONObject obj = createObjectURL("/authentification", args);
//
//				LoginActivity._mSocket.emit("post", new IOAcknowledge() {
//					@Override
//					public void ack(Object... objects) {
//						JSONObject rep = null;
//						try {
//							rep = new JSONObject(objects[0].toString());
//						} catch (JSONException e) {
//							Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
//						}
//						handleAuthentification(rep);
//					}
//				}, obj);
//
//				//TODO Delete once authentification okay - Alexis
//				//Data to fetch from server
//				fetchAllMenu();
//			}
//
//			@Override
//			public void on(String event, IOAcknowledge ack, Object... args) {
//				Log.d("SOCKET ON", "EVENT " + event);
//				eventsToListen(event, args);
//			}
//		});
	}

	/**
	 * Fetch all components of the card of the restaurant
	 */
	private void fetchAllMenu() {
//		fetchOptions();
		fetchElements();
		fetchMenus();
		fetchCompos();
		fetchCats();
		fetchAlacarte();
	}

	/**
	 * Fetch the /options data
	 */
	private void fetchOptions() {
		JSONObject obj = createObjectURL("/options", null);

		LoginActivity._mSocket.emit("get", new Ack() {
			@Override
			public void call(Object... objects) {
//				try {
//					TODO Uncomment once done - Alexis
//					getSharedPreferences("DATACARD", 0).edit().putString("/options", new JSONObject(objects[0].toString()).toString()).commit();
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
			}
		}, obj);

	}

	/**
	 * Fetch the /elements data
	 */
	private void fetchElements() {
		Log.d("LOGINACTIVITY", "FETCH ELEMENT");

		JSONObject obj = createObjectURL("/elements", null);

		LoginActivity._mSocket.emit("get", new Ack() {
			@Override
			public void call(Object... objects) {
				Log.d("LOGINACTIVITY", "ANSWER ELEMENT");

				try {
					getSharedPreferences("DATACARD", 0).edit().putString("/elements", new JSONObject(objects[0].toString()).toString()).commit();
				} catch (JSONException e) {
					e.printStackTrace();
				}
//                OrderFragment.fetchElements(rep);
			}
		}, obj);
	}

	/**
	 * Fetch the /menus data
	 */
	private void fetchMenus() {
		JSONObject obj = createObjectURL("/menus", null);

		LoginActivity._mSocket.emit("get", new Ack() {
			@Override
			public void call(Object... objects) {
				getSharedPreferences("DATACARD", 0).edit().putString("/menus", objects[0].toString()).commit();
			}
		}, obj);
	}

	/**
	 * Fetch the /compos data
	 */
	private void fetchCompos() {
		JSONObject obj = createObjectURL("/compos", null);

		LoginActivity._mSocket.emit("get", new Ack() {
			@Override
			public void call(Object... objects) {
				getSharedPreferences("DATACARD", 0).edit().putString("/compos", objects[0].toString()).commit();
			}
		}, obj);
	}

	/**
	 * Fetch the categories data
	 */
	private void fetchCats() {
		JSONObject obj = createObjectURL("/dishcategory/read", null);

		LoginActivity._mSocket.emit("get", new Ack() {
			@Override
			public void call(Object... objects) {
				getSharedPreferences("DATACARD", 0).edit().putString("/cats", objects[0].toString()).commit();
//                OrderFragment.fetchMenus(_mMenus, _mCompos, _mCats);
			}
		}, obj);
	}

	/**
	 * Fetch the /alacarte data
	 */
	private void fetchAlacarte() {
		JSONObject obj = createObjectURL("/alacarte", null);

		LoginActivity._mSocket.emit("get", new Ack() {
			@Override
			public void call(Object... objects) {
				getSharedPreferences("DATACARD", 0).edit().putString("/alacarte", objects[0].toString()).commit();
//                OrderFragment.fetchCard(_mAlacarte);
//				_mProgressDialog.dismiss();

				Intent mainActivity = new Intent(LoginActivity.this, Main.class);
				mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(mainActivity);
			}
		}, obj);
	}

	/**
	 * Fetch the last orders
	 */
	private void fetchLastOrders() {
		JSONObject obj = createObjectURL("/get_last_orders", null);

		LoginActivity._mSocket.emit("get", new Ack() {
			@Override
			public void call(Object... objects) {
				try {
					HistoryFragment.getLastOrders(new JSONObject(objects[0].toString()));
				} catch (JSONException e) {
					Log.d("LOGINACTIVITY", "EXCEPTION JSON:" + e.toString());
				}
			}
		}, obj);
	}

	/**
	 * Handle all different events needed
	 * @param event
	 * @param args
	 */
	private void eventsToListen(String event, Object... args) {
		switch (event) {
			case "receive_order":
				HistoryFragment.addOrderToList((JSONObject) args[0]);
				break;
			case "notifications":
				NotificationsFragment.addNotificationToList((JSONObject) args[0]);
				break;
			case "update":
				fetchAllMenu();
				break;
			case "question":
				//TODO - Alexis
				break;
			case "order_ready":
				//TODO - Alexis
				break;
			default:
				break;
		}
	}

	/**
	 * Manage things to do if authentification succeeds or not
	 * @param answer
	 */
	private void handleAuthentification(JSONObject answer) {
		try {
			if (answer == null || !answer.getBoolean("answer")) {
				//On error authentification
				LoginActivity.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast msg = Toast.makeText(LoginActivity.this, R.string.login_auth_fail, Toast.LENGTH_LONG);
						msg.show();
					}
				});
			} else {
				//Fetch all the data
				fetchAllMenu();

				//TODO Uncomment once done - Alexis
//                fetchLastOrders();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
