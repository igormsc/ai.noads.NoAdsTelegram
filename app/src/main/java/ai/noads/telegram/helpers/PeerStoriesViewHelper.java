package ai.noads.telegram.helpers;


import android.view.View;
import java.lang.reflect.Method;

public class PeerStoriesViewHelper {
    public static void findAndCallSave(View anchor, Class<?> peerStoriesViewClass, Method saveMethod) {
        try {
            View current = anchor;
            while (current != null) {

                if (peerStoriesViewClass.isInstance(current)) {
                    if (saveMethod != null) saveMethod.invoke(current);
                    return;
                }

                if (current.getParent() instanceof View) current = (View) current.getParent();
                else break;

            }
        } catch (Throwable ignored) {}
    }
}
