package com.kms.katalon.core.keyword.internal;

import groovy.transform.CompileStatic

import java.text.MessageFormat

import com.kms.katalon.core.annotation.internal.Action
import com.kms.katalon.core.constants.StringConstants
import com.kms.katalon.core.exception.StepFailedException
import com.kms.katalon.core.logging.KeywordLogger
import com.kms.katalon.core.util.internal.KeywordLoader

public class KeywordExecutor {

    public static final String PLATFORM_WEB = "web"

    public static final String PLATFORM_MOBILE = "mobile"

    public static final String PLATFORM_WEB_SERVICE = "webservice"

    public static final String PLATFORM_BUILT_IN = "builtin"

    public static final String WEB_BUILT_IN_KEYWORD_PACKAGE = "com.kms.katalon.core.webui.keyword.builtin"

    public static final String MOBILE_BUILT_IN_KEYWORD_PACKAGE = "com.kms.katalon.core.mobile.keyword.builtin"

    public static final String WEB_SERVICE_BUILT_IN_KEYWORD_PACKAGE = "com.kms.katalon.core.webservice.keyword.builtin"

    public static final String CORE_BUILT_IN_KEYWORD_PACKAGE = "com.kms.katalon.core.keyword.builtin"

    private static Map<String, List<IKeyword>> cacheActions

    static {
        cacheActions = new HashMap<>();
    }

    @CompileStatic
    public static Object execute(String keyword, Object ...params) {
        return executeKeyword(keyword,
                [
                    WEB_BUILT_IN_KEYWORD_PACKAGE,
                    MOBILE_BUILT_IN_KEYWORD_PACKAGE,
                    WEB_SERVICE_BUILT_IN_KEYWORD_PACKAGE,
                    CORE_BUILT_IN_KEYWORD_PACKAGE] as String[], params);
    }

    @CompileStatic
    public static Object executeKeywordForPlatform(String platform, String keyword, Object ...params) {
        IKeyword[] actions = getActions(keyword, getSuitablePackage(platform))
        if (actions.length != 1) {
            throw new StepFailedException(MessageFormat.format(StringConstants.KEYWORD_X_DOES_NOT_EXIST_ON_PLATFORM_Y, [keyword, platform] as Object[]))
        }
        return actions[0].execute(params)
    }

    @CompileStatic
    private static String[] getSuitablePackage(String platform) {
        switch (platform) {
            case PLATFORM_WEB:
                return [WEB_BUILT_IN_KEYWORD_PACKAGE] as String[]
            case PLATFORM_MOBILE:
                return [MOBILE_BUILT_IN_KEYWORD_PACKAGE] as String[]
            case PLATFORM_WEB_SERVICE:
                return [WEB_SERVICE_BUILT_IN_KEYWORD_PACKAGE] as String[]
            case PLATFORM_BUILT_IN:
                return [CORE_BUILT_IN_KEYWORD_PACKAGE] as String[]
            default:
                return [] as String[]
        }
    }

    @CompileStatic
    public static Object forwardKeyword(IKeyword source, String keyword, Object ...params) {
        String packageSource = source.getClass().getPackage().getName()
        IKeyword[] actions = getActions(keyword, [packageSource] as String[])

        if (actions.length != 1) {
            throw new StepFailedException(MessageFormat.format(StringConstants.KEYWORD_X_DOES_NOT_EXIST, keyword))
        }
        return actions[0].execute(params)
    }

    @CompileStatic
    private static Object executeKeyword(String keyword, String[] searchPackages, Object ...params) {
        IKeyword[] actions = getActions(keyword, searchPackages)

        if (actions.length < 1) {
            throw new StepFailedException(MessageFormat.format(StringConstants.KEYWORD_X_DOES_NOT_EXIST, keyword))
        }
        IKeyword action = actions[0];
        SupportLevel curLevel = action.getSupportLevel(params)

        for (int i = 1; i < actions.length; ++i) {
            SupportLevel actLevel = actions[i].getSupportLevel(params)
            if (actLevel.compareTo(curLevel) > 0) {
                curLevel = actLevel
                action = actions[i]
            }
        }

        return action.execute(params)
    }

    @CompileStatic
    private static List<IKeyword> getActions(String keyword, String[] searchPackages) {
        // get from cache
        if (cacheActions.containsKey(keyword)) {
            return cacheActions.get(keyword)
        }

        // not found from cache
        if (searchPackages == null || searchPackages.length == 0) {
            // avoid to return null
            return []
        }

        List<IKeyword> actions = new ArrayList<>()
        List<Class<?>> classes = KeywordLoader.listClasses(searchPackages)
        searchPackages.each({
            try {
                def className = keyword.substring(0, 1).toUpperCase() + keyword.substring(1)
                Class<?> clazz = Class.forName("${it}.${className}Keyword");
                if (!classes.contains(clazz)) {
                    classes.add(clazz)
                }
            } catch (Exception ignored) {
                System.out.println(keyword)
            }
        })
        for (Class<?> cls : classes) {
            if (!IKeyword.class.isAssignableFrom(cls)) {
                continue
            }

            Action act = (Action) cls.getAnnotation(Action.class)
            if (act == null || !act.value().equals(keyword)) {
                continue
            }

            try {
                actions.add((IKeyword) cls.newInstance())
            } catch (Exception ex) {
                throw new StepFailedException(MessageFormat.format(StringConstants.KEYWORD_EXECUTOR_ERROR_MSG, ex.getMessage()))
            }
        }
        cacheActions.put(keyword, actions)
        return actions
    }

}
