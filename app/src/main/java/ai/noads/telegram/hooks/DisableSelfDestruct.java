package ai.noads.telegram.hooks;

import ai.noads.telegram.helpers.HookHelper;

import java.util.ArrayList;

public class DisableSelfDestruct {

    private static final String TAG = "DisableSelfDestruct";

    public static void init(ClassLoader cl) {

        HookHelper.hookMethod(cl, "org.telegram.messenger.MessagesController", "deleteMessages", param -> {
            boolean isSecret = false;

            if (param.args.length > 0 && param.args[0] instanceof ArrayList) {
                ArrayList<Integer> messageIds = (ArrayList<Integer>) param.args[0];
                for (Integer id : messageIds)
                    if (id < 0) {
                        isSecret = true;
                        break;
                    }
            }

            if (!isSecret && param.args.length > 3 && param.args[3] instanceof Long) {
                long dialogId = (Long) param.args[3];
                if (dialogId < 0 && isSecretChat(cl, param.thisObject, dialogId))
                    isSecret = true;
            }

            if (isSecret) param.setResult(null);

        });

        HookHelper.hookMethod(cl, "org.telegram.messenger.MessagesStorage", "markMessagesAsDeleted", param -> {
            if (param.args.length > 1 && param.args[1] instanceof ArrayList) {
                ArrayList<Integer> messageIds = (ArrayList<Integer>) param.args[1];
                for (Integer id : messageIds)
                    if (id < 0) {
                        param.setResult(null);
                        return;
                    }
            }
        });

    }

    // Placeholder for future secret chat detection
    private static boolean isSecretChat(ClassLoader cl, Object messagesController, long dialogId) {
        return true;
    }
}