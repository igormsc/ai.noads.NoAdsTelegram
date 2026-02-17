package ai.noads.telegram.hooks;

import de.robv.android.xposed.XposedHelpers;

import static ai.noads.telegram.helpers.HookHelper.hookMethod;

public class EnablePrivacy {

    public static void hideTyping(ClassLoader cl) {
        hookMethod(cl, "org.telegram.ui.ChatActivity$ChatActivityEnterViewDelegate", "needSendTyping", param -> param.setResult(null));
    }

    public static void hideStoryViewStatus(ClassLoader cl) {
        hookMethod(cl, "org.telegram.ui.Stories.StoriesController", "markStoryAsRead", param -> param.setResult(false));
    }

    public static void hideOnlineStatus(ClassLoader cl, boolean alwaysOffline) {

        hookMethod(cl, "org.telegram.tgnet.ConnectionsManager", "sendRequestInternal", param -> {
                    Object request = param.args[0];
                    try {
                        Class<?> updateStatusClass = XposedHelpers.findClass("org.telegram.tgnet.tl.TL_account$TL_account_updateStatus", cl);
                        if (updateStatusClass.isInstance(request))
                            XposedHelpers.setBooleanField(request, "offline", alwaysOffline);
                    } catch (Throwable ignored) {}
                });
    }

}