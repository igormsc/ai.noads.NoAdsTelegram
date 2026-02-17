package ai.noads.telegram.hooks;

import ai.noads.telegram.helpers.HookHelper;
import de.robv.android.xposed.XposedBridge;

public class DisableSecretChatMessageDeletion {

    private static final String TAG = "DisableSecretChatMessageDeletion";

    public static void init(ClassLoader cl) {
        XposedBridge.log(TAG + ": init started");

        blockSecretChatHelperMethods(cl);

        HookHelper.hookMethod(cl, "org.telegram.ui.ChatActivity", "sendSecretMediaDelete", param -> param.setResult(null));

        HookHelper.hookMethod(cl, "org.telegram.messenger.MessagesStorage", "markMessagesAsDeletedByRandoms", param -> param.setResult(null));

        HookHelper.hookMethod(cl, "org.telegram.messenger.MessagesStorage", "emptyMessagesMedia",
                param -> {
                    if (param.args.length > 0 && param.args[0] instanceof Long) {
                        long dialogId = (Long) param.args[0];
                        if (dialogId < 0 && isSecretChat(cl, param.thisObject, dialogId)) param.setResult(null);

                    }
                });
    }

    private static void blockSecretChatHelperMethods(ClassLoader cl) {
        String[] methods = {"deleteMessages", "onMessageTTLExpired", "processPendingEncMessages"};
        for (String method : methods)
            HookHelper.hookMethod(cl, "org.telegram.messenger.SecretChatHelper", method, param -> param.setResult(null));
    }

    // Temporary placeholder
    private static boolean isSecretChat(ClassLoader cl, Object messagesStorage, long dialogId) {
        return true;
    }
}

