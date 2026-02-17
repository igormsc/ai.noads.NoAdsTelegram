package ai.noads.telegram;

import ai.noads.telegram.hooks.DisableChatDeletion;
import ai.noads.telegram.hooks.DisableSecretChatMessageDeletion;
import ai.noads.telegram.hooks.DisableSelfDestruct;
import ai.noads.telegram.hooks.EnablePrivacy;
import ai.noads.telegram.hooks.SecretMediaSave;
import ai.noads.telegram.config.HookConfig;
import ai.noads.telegram.hooks.DisableChannelSwipeBack;
import ai.noads.telegram.hooks.DisableChannelSwitching;
import ai.noads.telegram.hooks.DisableDelete;
import ai.noads.telegram.hooks.DisableProfileSwipeBack;
import ai.noads.telegram.hooks.DisableVideoAds;
import ai.noads.telegram.hooks.EnableForward;
import ai.noads.telegram.hooks.EnableSaveStories;
import ai.noads.telegram.hooks.DisableSponsoredMessages;
import ai.noads.telegram.hooks.HideBottomOverlay;
import ai.noads.telegram.hooks.HideMuteUnmuteButton;
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
        if (HookConfig.HIDE_MUTE_BUTTON.isEnabled()) safeInit(() -> HideMuteUnmuteButton.init(cl));
        if (HookConfig.HIDE_BOTTOM_OVERLAY.isEnabled()) safeInit(() -> HideBottomOverlay.init(cl));
        if (HookConfig.DISABLE_SWIPE_PROFILE.isEnabled()) safeInit(() -> DisableProfileSwipeBack.init(cl));
        if (HookConfig.DISABLE_SWIPE_CHANNEL.isEnabled()) safeInit(() -> DisableChannelSwipeBack.init(cl));
        if (HookConfig.DISABLE_CHAT_DELETION.isEnabled()) safeInit(() -> DisableChatDeletion.init(cl));
        if (HookConfig.DISABLE_SECRET_CHAT_MESSAGE_DELETION.isEnabled()) safeInit(() -> DisableSecretChatMessageDeletion.init(cl));
        if (HookConfig.DISABLE_SELF_DESTRUCT.isEnabled()) safeInit(() -> DisableSelfDestruct.init(cl));
        if (HookConfig.ENABLE_SECRET_MEDIA_SAVE.isEnabled()) safeInit(() -> SecretMediaSave.init(cl));
        if (HookConfig.HIDE_TYPING.isEnabled()) safeInit(() -> EnablePrivacy.hideTyping(cl));

 //       if (HookConfig.HIDE_ONLINE_STATUS.isEnabled()) safeInit(() -> EnablePrivacy.hideOnlineStatus(cl , HookConfig.HIDE_ONLINE_STATUS_ALWAYS_OFFLINE.isEnabled()));
 //       if (HookConfig.HIDE_VIEW_STORY.isEnabled()) safeInit(() -> EnablePrivacy.hideStoryViewStatus(cl));

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
