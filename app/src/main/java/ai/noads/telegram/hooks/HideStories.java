package ai.noads.telegram.hooks;


import static ai.noads.telegram.helpers.HookHelper.hookMethod;

public class HideStories {

    public static void init(ClassLoader cl) {
        hookMethod(cl, "org.telegram.ui.Stories.StoriesController", "hasStories", param -> param.setResult(false));
        hookMethod(cl, "org.telegram.messenger.MessagesController", "storiesEnabled", param -> param.setResult(false));
    }

}