package ai.noads.telegram.hooks;

import android.view.View;
import android.view.ViewGroup;
import ai.noads.telegram.helpers.HookHelper;
import de.robv.android.xposed.XposedHelpers;

public class HideBottomOverlay {

    private static final String CHAT_ACTIVITY = "org.telegram.ui.ChatActivity";

    public static void init(ClassLoader cl) {
        try {
            HookHelper.hookMethod(cl, CHAT_ACTIVITY, "updateBottomOverlay", param -> {param.setResult(null);});
            HookHelper.hookMethod(cl, CHAT_ACTIVITY, "updateBottomOverlay", param -> {param.setResult(null);});
            HookHelper.hookMethod(cl, CHAT_ACTIVITY, "onResume", param -> {hideOverlay(param.thisObject);});
        } catch (Throwable ignored) {}
    }

    private static void hideOverlay(Object chatActivity) {
        if (chatActivity == null) return;
        try {
            View overlay = (View) XposedHelpers.getObjectField(chatActivity, "bottomOverlay");
            if (overlay != null && overlay.getParent() instanceof ViewGroup) ((ViewGroup) overlay.getParent()).removeView(overlay);
            else if (overlay != null) overlay.setVisibility(View.GONE);
        } catch (Throwable ignored) {}
    }
}