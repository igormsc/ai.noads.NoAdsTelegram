package ai.noads.telegram.hooks;

import ai.noads.telegram.helpers.HookHelper;

public class DisableChatDeletion {

    private static final String TAG = "DisableChatDeletion";

    public static void init(ClassLoader cl) {

        HookHelper.hookMethod(cl, "org.telegram.messenger.MessagesController", "deleteDialog", param -> {
            if (param.args.length > 0 && param.args[0] instanceof Long) {
                long dialogId = (Long) param.args[0];
                if (isSecretChat(cl, param.thisObject, dialogId)) param.setResult(null);
            }
        });

        HookHelper.hookMethod(cl, "org.telegram.messenger.MessagesStorage", "deleteDialog", param -> {
            if (param.args.length > 0 && param.args[0] instanceof Long) {
                long dialogId = (Long) param.args[0];
                if (isSecretChat(cl, param.thisObject, dialogId)) param.setResult(null);
            }
        });

        HookHelper.hookMethod(cl, "org.telegram.messenger.MessagesController", "deleteChat", param -> {
            if (param.args.length > 0 && param.args[0] instanceof Long) {
                long chatId = (Long) param.args[0];
                if (isSecretChat(cl, param.thisObject, chatId)) param.setResult(null);
            }
        });

        HookHelper.hookMethod(cl, "org.telegram.messenger.MessagesController", "deleteHistory", param -> {
            if (param.args.length > 0 && param.args[0] instanceof Long) {
                long dialogId = (Long) param.args[0];
                if (isSecretChat(cl, param.thisObject, dialogId)) param.setResult(null);
            }
        });

        HookHelper.hookMethod(cl, "org.telegram.messenger.SecretChatHelper", "deleteSecretChat", param -> param.setResult(null));

    }

    // Temporary placeholder
    private static boolean isSecretChat(ClassLoader cl, Object messagesController, long dialogId) {
        return true;
    }
}