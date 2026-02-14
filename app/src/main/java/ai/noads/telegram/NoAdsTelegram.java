package ai.noads.telegram;

import ai.noads.telegram.config.HookConfig;
import ai.noads.telegram.hooks.DisableChannelSwitching;
import ai.noads.telegram.hooks.DisableDelete;
import ai.noads.telegram.hooks.DisableVideoAds;
import ai.noads.telegram.hooks.EnableForward;
import ai.noads.telegram.hooks.EnableSaveStories;
import ai.noads.telegram.hooks.DisableSponsoredMessages;
import ai.noads.telegram.hooks.HidePinnedMessages;
import ai.noads.telegram.hooks.HideStories;
import ai.noads.telegram.hooks.HideTranslatePrompt;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class NoAdsTelegram implements IXposedHookLoadPackage {
    private static final String TELEGRAM_PACKAGE = "org.telegram.messenger";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!TELEGRAM_PACKAGE.equals(lpparam.packageName) || !TELEGRAM_PACKAGE.equals(lpparam.processName))  return;

        ClassLoader cl = lpparam.classLoader;

        if (HookConfig.DISABLE_MESSAGE_DELETE.isEnabled()) safeInit(() -> DisableDelete.init(cl));
        if (HookConfig.DISABLE_CHANNEL_SWITCHING.isEnabled()) safeInit(() -> DisableChannelSwitching.init(cl));
        if (HookConfig.DISABLE_SPONSORED_MESSAGES.isEnabled()) safeInit(() -> DisableSponsoredMessages.init(cl));
        if (HookConfig.ENABLE_FORWARD.isEnabled()) safeInit(() -> EnableForward.init(cl));
        if (HookConfig.ENABLE_SAVE_STORIES.isEnabled()) safeInit(() -> EnableSaveStories.init(cl));
        if (HookConfig.DISABLE_VIDEO_ADS.isEnabled()) safeInit(() -> DisableVideoAds.init(cl));
        if (HookConfig.HIDE_STORIES.isEnabled()) safeInit(() -> HideStories.init(cl));
        if (HookConfig.HIDE_PINNED_MESSAGES.isEnabled()) safeInit(() -> HidePinnedMessages.init(cl));
        if (HookConfig.HIDE_TRANSLATE_DIALOG.isEnabled()) safeInit(() -> HideTranslatePrompt.init(cl));

        // safeInit( () -> DisableLeftEdgeSwipe.init(cl));

    }

    private void safeInit(Runnable initializer) {
        try {
            initializer.run();
        } catch (Throwable t) {
            XposedBridge.log("NoAdsTelegram: failed to init hook");
            XposedBridge.log(t);
        }
    }

}
