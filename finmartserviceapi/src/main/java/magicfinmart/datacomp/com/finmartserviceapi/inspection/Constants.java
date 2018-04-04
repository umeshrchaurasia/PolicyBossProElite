package magicfinmart.datacomp.com.finmartserviceapi.inspection;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Rajeev Ranjan on 12/12/2017.
 */

public class Constants {
    public static final String SHARED_PREF_ALL_MASTER = "shared__all_master";
    public static final String SHARED_PREFERENCE_POLICYBOSS = "policyboss_inspect";
    public static final String FRONTREAR = "frontrear";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String GLASS = "glass";
    public static final String REGISTER = "register";
    public static void hideKeyBoard(View view, Context context) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static SharedPreferences getSharedPreference(Context context) {
        return context.getSharedPreferences(Constants.SHARED_PREFERENCE_POLICYBOSS, MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getSharedPreferenceEditor(Context context) {
        return getSharedPreference(context).edit();
    }
}

