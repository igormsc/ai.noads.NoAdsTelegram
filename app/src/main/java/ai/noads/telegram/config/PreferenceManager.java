package ai.noads.telegram.config;

import android.content.Context;

import de.robv.android.xposed.XSharedPreferences;

public class PreferenceManager {
    private static final String PREFS_NAME = "NoAdsTelegramPrefs";
    private static XSharedPreferences sXSharedPreferences;

    public static void setHookEnabled(HookConfig hook, boolean enabled, Context context) {
        try {
            android.content.SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_WORLD_READABLE);
            prefs.edit().putBoolean(hook.key, enabled).apply();
        } catch (Exception ignored) {}
    }

    public static boolean isHookEnabledInActivity(HookConfig hook, Context context) {
        try {
            android.content.SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_WORLD_READABLE);
            return prefs.getBoolean(hook.key, hook.defaultValue);
        } catch (Exception e) { return hook.defaultValue; }
    }

    public static boolean isHookEnabled(HookConfig hook) {
        try {
            if (sXSharedPreferences == null) sXSharedPreferences = new XSharedPreferences("ai.noads.telegram", PREFS_NAME);
            sXSharedPreferences.reload();
            return sXSharedPreferences.getBoolean(hook.key, hook.defaultValue);
        } catch (Exception e) { return hook.defaultValue; }
    }

}
