package ai.noads.telegram.hooks;

import android.view.View;
import ai.noads.telegram.helpers.HookHelper;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class HideMuteUnmuteButton {

    private static final String CHAT_ACTIVITY = "org.telegram.ui.ChatActivity";
    private static final String BUTTON_FIELD = "bottomOverlayChatText";

    public static void init(ClassLoader cl) {
        try {
            HookHelper.hookMethod(cl, CHAT_ACTIVITY, "toggleMute", param -> {param.setResult(null); });
            HookHelper.hookMethod(cl, CHAT_ACTIVITY, "onResume", param -> {hideButton(param.thisObject);});
            HookHelper.hookMethod(cl, CHAT_ACTIVITY, "updateBottomOverlay", param -> {hideButton(param.thisObject);});
            XposedHelpers.findAndHookMethod(View.class, "setOnClickListener", View.OnClickListener.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                }
            });

        } catch (Throwable ignored) {
        }
    }

    private static void hideButton(Object chatActivity) {
        if (chatActivity == null) return;
        try {
            View button = (View) XposedHelpers.getObjectField(chatActivity, BUTTON_FIELD);
            if (button != null && button.getVisibility() != View.GONE) button.setVisibility(View.GONE);
        } catch (Throwable ignored) {}
    }
}
