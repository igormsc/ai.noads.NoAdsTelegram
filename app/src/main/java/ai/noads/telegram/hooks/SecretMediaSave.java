package ai.noads.telegram.hooks;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import de.robv.android.xposed.XposedHelpers;

import static ai.noads.telegram.helpers.HookHelper.hookMethod;

public class SecretMediaSave {

    private static final String TAG = "SecretMediaSave";

    public static void init(ClassLoader cl) {

        hookMethod(cl, "org.telegram.messenger.MessageObject", "isSecret", param -> param.setResult(false));

        hookMethod(cl, "org.telegram.ui.ChatActivity$ChatMessageCellDelegate", "didPressImage",
                param -> {
                    if (param.args.length > 0 && param.args[0] != null) {
                        try {
                            Object cell = param.args[0];

                            Object messageObj = XposedHelpers.callMethod(cell, "getMessageObject");
                            if (messageObj != null) {
                                Object messageOwner = XposedHelpers.getObjectField(messageObj, "messageOwner");
                                int ttl = XposedHelpers.getIntField(messageOwner, "ttl");
                                if (ttl > 0) XposedHelpers.setIntField(messageOwner, "ttl", 0);
                            }

                            ensurePhotoViewerInitialized(cl, cell);

                        } catch (Throwable ignored) {}
                    }
                });

        redirectSecretMediaViewer(cl);

    }

    private static void ensurePhotoViewerInitialized(ClassLoader cl, Object cell) {
        try {
            if (!(cell instanceof View)) return;
            View view = (View) cell;
            Context context = view.getContext();
            Activity activity = getActivity(context);
            if (activity == null) return;

            Class<?> photoViewerClass = XposedHelpers.findClass("org.telegram.ui.PhotoViewer", cl);
            Object photoViewer = XposedHelpers.callStaticMethod(photoViewerClass, "getInstance");
            XposedHelpers.callMethod(photoViewer, "setParentActivity", activity);
        } catch (Throwable ignored) {}
    }

    private static Activity getActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) return (Activity) context;
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    private static void redirectSecretMediaViewer(ClassLoader cl) {
        hookMethod(cl, "org.telegram.ui.SecretMediaViewer", "openMedia", param -> {

            if (param.args.length < 2 || param.args[0] == null) return;

            Object messageObject = param.args[0];
            Object provider = param.args[1];

            try {
                long dialogId = (long) XposedHelpers.callMethod(messageObject, "getDialogId");

                Class<?> photoViewerClass = XposedHelpers.findClass("org.telegram.ui.PhotoViewer", cl);
                Object photoViewer = XposedHelpers.callStaticMethod(photoViewerClass, "getInstance");

                // openPhoto(MessageObject, long, long, long, PhotoViewerProvider, boolean)
                XposedHelpers.callMethod(photoViewer, "openPhoto", messageObject, dialogId, 0L, 0L, provider, false);

                param.setResult(null);
            } catch (Throwable ignored) {}
        });
    }
}

