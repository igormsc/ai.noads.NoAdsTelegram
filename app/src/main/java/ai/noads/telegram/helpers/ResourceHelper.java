package ai.noads.telegram.helpers;

import android.content.Context;
import de.robv.android.xposed.XposedHelpers;

public class ResourceHelper {
    public static String getString(Context context, String resourceName) {
        try {
            ClassLoader cl = context.getClassLoader();
            Class<?> localeController = XposedHelpers.findClass("org.telegram.messenger.LocaleController", cl);
            Class<?> rString = XposedHelpers.findClass("org.telegram.messenger.R$string", cl);
            int stringId = (int) XposedHelpers.getStaticIntField(rString, resourceName);
            return (String) XposedHelpers.callStaticMethod(localeController, "getString", stringId);
        } catch (Throwable t) {
            return null;
        }
    }
}
