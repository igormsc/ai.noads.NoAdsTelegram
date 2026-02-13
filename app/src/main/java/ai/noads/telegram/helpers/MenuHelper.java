package ai.noads.telegram.helpers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.robv.android.xposed.XposedHelpers;

public class MenuHelper {

    private static final String targetText = "Save to Gallery";

    public static boolean hasSaveMenuItem(ViewGroup root) {
        return findViewWithTextRecursive(root, targetText);
    }

    private static boolean findViewWithTextRecursive(View view, String targetText) {
        if (view instanceof TextView) {
            CharSequence text = ((TextView) view).getText();
            if (text != null && targetText.equals(text.toString()))
                return true;
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++)
                if (findViewWithTextRecursive(group.getChildAt(i), targetText))
                    return true;
        }
        return false;
    }


    public static View createSaveMenuItem(Context context, String text, View.OnClickListener listener) {
        try {
            ClassLoader cl = context.getClassLoader();
            Class<?> rDrawable = XposedHelpers.findClass("org.telegram.messenger.R$drawable", cl);
            Class<?> menuSubItem = XposedHelpers.findClass("org.telegram.ui.ActionBar.ActionBarMenuSubItem", cl);

            int drawableId = (int) XposedHelpers.getStaticIntField(rDrawable, "msg_gallery");
            Object menuItem = XposedHelpers.newInstance(menuSubItem, context, false, false, false, null);
            XposedHelpers.callMethod(menuItem, "setTextAndIcon", text, drawableId, null);
            XposedHelpers.callMethod(menuItem, "setOnClickListener", listener);
            return (View) menuItem;
        } catch (Throwable t) {
            return null;
        }
    }
}