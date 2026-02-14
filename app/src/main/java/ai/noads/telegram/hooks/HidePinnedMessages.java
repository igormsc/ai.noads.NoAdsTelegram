package ai.noads.telegram.hooks;

import ai.noads.telegram.helpers.HookHelper;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.util.ArrayList;

public class HidePinnedMessages {

    private static final String CHAT_ACTIVITY = "org.telegram.ui.ChatActivity";
    private static final String FIELD_PINNED_VIEW = "pinnedMessageView";

    public static void init(ClassLoader cl) {
        try {
            HookHelper.hookMethod(cl, "org.telegram.messenger.MediaDataController", "loadPinnedMessages", param -> {
                if (param.args.length == 3) param.setResult(null);
                else if (param.args.length == 4) param.setResult(new ArrayList<>());
            });

            try {
                Class<?> chatActivity = Class.forName(CHAT_ACTIVITY, false, cl);
                XposedBridge.hookAllMethods(chatActivity, "createPinnedMessageView", new de.robv.android.xposed.XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        hidePinnedView(param.thisObject);
                    } });
            } catch (ClassNotFoundException ignored) {}

            HookHelper.hookMethod(cl, CHAT_ACTIVITY, "updatePinnedMessageView", param -> {param.setResult(null); hidePinnedView(param.thisObject);});

        } catch (Throwable ignored) {}
    }

    private static void hidePinnedView(Object chatActivityInstance) {
        if (chatActivityInstance == null) return;
        try {
            Object view = XposedHelpers.getObjectField(chatActivityInstance, FIELD_PINNED_VIEW);
            if (view != null) XposedHelpers.callMethod(view, "setVisibility", 8);
        } catch (Throwable ignored) {}
    }
}