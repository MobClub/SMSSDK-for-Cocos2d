package cn.smssdk.gui.util;

import com.mob.MobSDK;
import com.mob.tools.utils.SharePrefrenceHelper;

import cn.smssdk.gui.entity.Profile;

/**
 * Created by weishj on 2017/9/14.
 */

public class GUISPDB {
	private static final String DB_NAME = "SMSSDKGUI_SPDB";
	private static final int DB_VERSION = 1;
	private static final String KEY_TEMP_CODE = "key_tempCode";
	private static final String KEY_PROFILE = "key_profile";

	private static SharePrefrenceHelper sp;

	static {
		sp = new SharePrefrenceHelper(MobSDK.getContext());
		sp.open(DB_NAME, DB_VERSION);
	}

	public static void setTempCode(String tempCode) {
		sp.putString(KEY_TEMP_CODE, tempCode);
	}

	public static String getTempCode() {
		return sp.getString(KEY_TEMP_CODE);
	}

	public static void setProfile(Profile profile) {
		sp.put(KEY_PROFILE, profile);
	}

	public static Profile getProfile() {
		return (Profile) sp.get(KEY_PROFILE);
	}
}
