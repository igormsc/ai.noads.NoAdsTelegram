package ai.noads.telegram.hooks;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import ai.noads.telegram.helpers.MenuHelper;
import ai.noads.telegram.helpers.PeerStoriesViewHelper;
import ai.noads.telegram.helpers.ResourceHelper;

import static ai.noads.telegram.helpers.HookHelper.hookMethod;

public class EnableSaveStories {

    private static Class<?> peerStoriesViewClass = null;
    private static Class<?> customPopupMenuClass = null;
    private static Method peerStoriesViewSaveMethod = null;

    public static void init(ClassLoader cl) {
        try {
            peerStoriesViewClass = XposedHelpers.findClass("org.telegram.ui.Stories.PeerStoriesView", cl);
            customPopupMenuClass = XposedHelpers.findClass("org.telegram.ui.Components.CustomPopupMenu", cl);

            try {
                peerStoriesViewSaveMethod = peerStoriesViewClass.getDeclaredMethod("saveToGallery");
                peerStoriesViewSaveMethod.setAccessible(true);
            } catch (NoSuchMethodException ignored) {}

            hookPopupMenuShow();
            hookMenuCreationMethods();
            forcePremiumStatus(cl);
            blockPremiumBlockedNotifications();
            hookAllowScreenshots(cl);

        } catch (Throwable ignored) {}
    }


    private static void hookAllowScreenshots(ClassLoader cl) {
        try {
            hookMethod(cl, "org.telegram.ui.Stories.PeerStoriesView$StoryItemHolder", "allowScreenshots", param -> param.setResult(true));
        } catch (Throwable ignored) {}
    }

    private static void hookPopupMenuShow() {
        try {
            XposedBridge.hookAllMethods(customPopupMenuClass, "show", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    try {
                        Object menu = param.thisObject;
                        View anchor = (View) param.args[0];
                        if (anchor == null) return;

                        Object popupLayout = XposedHelpers.getObjectField(menu, "popupLayout");
                        if (popupLayout == null) return;

                        Context context = anchor.getContext();
                        String saveText = ResourceHelper.getString(context, "SaveToGallery");

                        if (saveText == null) return;

                        if (popupLayout instanceof ViewGroup) {
                            ViewGroup menuGroup = (ViewGroup) popupLayout;
                            if (MenuHelper.hasSaveMenuItem(menuGroup)) return;
                        }

                        View saveItem = MenuHelper.createSaveMenuItem(context, saveText, v -> {PeerStoriesViewHelper.findAndCallSave(anchor, peerStoriesViewClass, peerStoriesViewSaveMethod);});
                        if (saveItem != null) XposedHelpers.callMethod(popupLayout, "addView", saveItem);

                    } catch (Throwable ignored) {}
                } });
        } catch (Throwable ignored) {}
    }

    private static void hookMenuCreationMethods() {
        try {
            String[] targetMethods = {"createMenu", "fillMenu", "prepareMenu"};
            for (String methodName : targetMethods) {
                try {
                    XposedBridge.hookAllMethods(peerStoriesViewClass, methodName, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) {enableSaveMenuItems(param.getResult());}
                    });
                } catch (Throwable ignored) {}
            }
        } catch (Throwable ignored) {}
    }

    private static void enableSaveMenuItems(Object menu) {
        try {
            if (menu instanceof Menu) {
                Menu androidMenu = (Menu) menu;
                for (int i = 0; i < androidMenu.size(); i++) {
                    MenuItem item = androidMenu.getItem(i);
                    CharSequence title = item.getTitle();
                    if (title != null) {
                        String lower = title.toString().toLowerCase();
                        if (lower.contains("save") || lower.contains("gallery")) {
                            item.setEnabled(true);
                        }
                    }
                    SubMenu subMenu = item.getSubMenu();
                    if (subMenu != null) enableSaveMenuItems(subMenu);
                }
            }
        } catch (Throwable ignored) {}
    }

    private static void forcePremiumStatus(ClassLoader cl) {
        hookMethod(cl, "org.telegram.ui.Stories.StoriesController", "isPremium", param -> param.setResult(true));
        hookMethod(cl, "org.telegram.messenger.UserConfig", "isPremium", param -> param.setResult(true));
    }

    private static void blockPremiumBlockedNotifications() {
        try {
            String[] blockMethods = {"showPremiumBlockedDialog", "showPremiumRequired"};
            for (String methodName : blockMethods)
                try {
                    XposedBridge.hookAllMethods(peerStoriesViewClass, methodName, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            param.setResult(null);
                        }
                    });
                } catch (Throwable ignored) {}

        } catch (Throwable ignored) {}
    }
}