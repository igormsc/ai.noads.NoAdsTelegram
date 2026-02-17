package ai.noads.telegram.config;

import android.content.Context;

public enum HookConfig {
    DISABLE_SPONSORED_MESSAGES("disable_sponsored_messages", "Disable ads: sponsored messages", true, Category.ADS),
    DISABLE_VIDEO_ADS("disable_video_ads", "Disable ads: on video", true, Category.ADS),

    ENABLE_FORWARD("enable_forward", "Allow forwarding & saving", true, Category.MESSAGING),
    DISABLE_MESSAGE_DELETE("disable_message_delete", "Disable message deletion", true, Category.MESSAGING),
    DISABLE_CHAT_DELETION("disable_chat_deletion", "Disable chat deletion", false, Category.MESSAGING),

    DISABLE_SECRET_CHAT_MESSAGE_DELETION("disable_secret_chat_message_deletion", "Disable secret chat message deletion", false, Category.SECRET_CHAT),
    DISABLE_SELF_DESTRUCT("disable_self_destruct", "Disable self destruct", false, Category.SECRET_CHAT),
    ENABLE_SECRET_MEDIA_SAVE("enable_secret_media_save", "Enable secret media save", false, Category.SECRET_CHAT),

    ENABLE_SAVE_STORIES("enable_save_stories", "Enable saving stories", true, Category.STORIES),
    HIDE_STORIES("hide_stories", "Hide stories section", false, Category.STORIES),

    DISABLE_CHANNEL_SWITCHING("disable_channel_switching", "Disable channel switching", true, Category.UI),
    HIDE_PINNED_MESSAGES("hide_pinned_messages", "Hide pinned messages", false, Category.UI),
    HIDE_TRANSLATE_DIALOG("hide_translate_dialog", "Hide translate dialog", false, Category.UI),
    HIDE_MUTE_BUTTON("hide_mute_button", "Hide mute button", false, Category.UI),

    HIDE_TYPING("hide_typing", "Hide typing", false, Category.PRIVACY),

    HIDE_BOTTOM_OVERLAY("hide_bottom_overlay", "Hide bottom overlay", false, Category.UI),
    DISABLE_SWIPE_PROFILE("disable_swipe_profile", "Disable swipe profile", false, Category.UI),
    DISABLE_SWIPE_CHANNEL("disable_swipe_channel", "Disable swipe channel", false, Category.UI);

    public final String key;
    public final String title;
    public final boolean defaultValue;
    public final Category category;

    HookConfig(String key, String title, boolean defaultValue, Category category) {
        this.key = key;
        this.title = title;
        this.defaultValue = defaultValue;
        this.category = category;
    }

    public enum Category {
        ADS("Ads"),
        MESSAGING("Messaging"),
        PRIVACY("Privacy"),
        SECRET_CHAT("Secret Chat"),
        STORIES("Stories"),
        UI("UI");

        public final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }
    }

    public boolean isEnabled() {
        return PreferenceManager.isHookEnabled(this);
    }

    public void setEnabled(boolean enabled, Context context) {
        PreferenceManager.setHookEnabled(this, enabled, context);
    }

    public boolean isEnabledInActivity(Context context) {
        return PreferenceManager.isHookEnabledInActivity(this, context);
    }

    public static HookConfig[] getAll() {
        return values();
    }
}

