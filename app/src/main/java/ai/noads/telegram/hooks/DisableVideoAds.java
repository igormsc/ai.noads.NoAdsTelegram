package ai.noads.telegram.hooks;

import static ai.noads.telegram.helpers.HookHelper.hookMethod;

public class DisableVideoAds {

    public static void init(ClassLoader cl) {
        hookMethod(cl, "org.telegram.messenger.MessagesController", "isSponsoredDisabled", param -> param.setResult(true));
        hookMethod(cl, "org.telegram.messenger.UserConfig", "isPremium", param -> { if (isCallerVideoAds()) param.setResult(true); });
        hookMethod(cl, "org.telegram.messenger.video.VideoAds", "load", param -> param.setResult(null));
    }

    private static boolean isCallerVideoAds() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        for (StackTraceElement el : stack)
            if (el.getClassName().contains("VideoAds")) return true;

        return false;
    }

}
