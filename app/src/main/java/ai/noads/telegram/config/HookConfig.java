package ai.noads.telegram.config;

import android.content.Context;

public enum HookConfig {
    ENABLE_FORWARD("enable_forward", "Allow forwarding & saving", true),
    ENABLE_SAVE_STORIES("enable_save_stories", "Enable saving stories", true),
    DISABLE_SPONSORED_MESSAGES("disable_sponsored_messages", "Disable ads: sponsored messages", true),
    DISABLE_VIDEO_ADS("disable_video_ads", "Disable ads: on video", true),
    DISABLE_CHANNEL_SWITCHING("disable_channel_switching", "Disable channel switching", true),
    DISABLE_MESSAGE_DELETE("disable_message_delete", "Disable message deletion", true),
    HIDE_STORIES("hide_stories", "Hide stories section", false);

    public final String key;
    public final String title;
    public final boolean defaultValue;

    HookConfig(String key, String title, boolean defaultValue) {
        this.key = key;
        this.title = title;
        this.defaultValue = defaultValue;
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
