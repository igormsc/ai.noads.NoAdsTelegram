package ai.noads.telegram.hooks;

import java.lang.reflect.Method;
import java.util.ArrayList;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class DisableDelete {

    private static Method processUpdateArrayMethod;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void init(ClassLoader cl) {
        try {
            Class<?> messagesControllerClass = XposedHelpers.findClass("org.telegram.messenger.MessagesController", cl);

            for (Method m : messagesControllerClass.getDeclaredMethods()) {
                Class<?>[] params = m.getParameterTypes();
                if (params.length == 5 && params[0] == ArrayList.class && params[1] == ArrayList.class && params[2] == ArrayList.class && params[3] == boolean.class && params[4] == int.class) {
                    processUpdateArrayMethod = m;
                    break;
                }
            }

            if (processUpdateArrayMethod == null) return;

            XposedBridge.hookMethod(processUpdateArrayMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    try {
                        ArrayList<Object> updates = (ArrayList<Object>) param.args[0];
                        if (updates == null || updates.isEmpty()) return;

                        Class<?> tlDeleteChannel = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_updateDeleteChannelMessages", cl);
                        Class<?> tlDelete = XposedHelpers.findClass("org.telegram.tgnet.TLRPC$TL_updateDeleteMessages", cl);

                        ArrayList<Object> remainingUpdates = new ArrayList<>();

                        for (Object update : updates) {
                            Class<?> updateClass = update.getClass();
                            if (!updateClass.equals(tlDeleteChannel) && !updateClass.equals(tlDelete)) remainingUpdates.add(update);
                        }

                        param.args[0] = remainingUpdates;

                    } catch (Throwable ignored) {}
                }
            });

        } catch (Throwable ignored) {}
    }
}