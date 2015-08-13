//package com.eip.fastordering.customs;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.util.Log;
//
//import com.eip.fastordering.dialog.DialogOrder;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import io.socket.IOAcknowledge;
//
///**
// * Created by Mewen on 18-Jul-15.
// */
//public class IOAcknowledgeGetOrder implements IOAcknowledge {
//
//	private Fragment         _fragment = null;
//	private FragmentActivity _activity = null;
//
//	/**
//	 * Constructor
//	 * @param fragment
//	 * @param activity
//	 */
//	public IOAcknowledgeGetOrder(Fragment fragment, FragmentActivity activity) {
//		super();
//		_fragment = fragment;
//		_activity = activity;
//	}
//
//	@Override
//	public void ack(Object... objects) {
//		JSONObject rep = null;
//		try {
//			rep = new JSONObject(objects[0].toString());
//			Log.d("IOACKNOWLEDFE", "FULL ORDER=" + rep.toString());
//		} catch (JSONException e) {
//			Log.d("IOACKNOWLEDGE", "EXCEPTION JSON:" + e.toString());
//		}
//		new DialogOrder(_activity, _fragment, rep).customView().show();
//	}
//}
