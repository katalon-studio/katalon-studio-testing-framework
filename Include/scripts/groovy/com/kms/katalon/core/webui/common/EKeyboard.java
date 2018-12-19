package com.kms.katalon.core.webui.common;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class EKeyboard {
    private Robot robot;

    public EKeyboard() {
        try {
            setRobot(new Robot());
            getRobot().delay(1000);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private ArrayList<KeyElement> initKey(String str) {
        ArrayList<String> list = new ArrayList<String>();
        while (str.length() > 0) {
            // normal key
            if (str.charAt(0) != '/') {
                list.add(String.valueOf(str.charAt(0)));
                str = str.substring(1);
            }
            // special key
            else {
                String specialKey;
                if (str.indexOf(" ") == -1) {
                    specialKey = str.substring(1);
                    str = "";
                } else {
                    specialKey = str.substring(1, str.indexOf(" "));
                    str = str.substring(specialKey.length() + 2);
                }
                list.add(specialKey);
            }
        }
        ArrayList<KeyElement> keys = new ArrayList<KeyElement>();
        keys = Util.stringToKey(list);
        return keys;
    }

    public Result pressKeys(String strKeys) {
        return pressKeys(strKeys, false);
    }

    public Result pressKeys(String strKeys, boolean isSequential) {
        Result result = new Result();
        try {
            ArrayList<KeyElement> keys = initKey(strKeys);
            if (isSequential) {
                for (KeyElement ke : keys) {
                    if (ke.getCode() != KeyEvent.CHAR_UNDEFINED) {
                        if (!ke.getShiftKey()) {
                            getRobot().keyPress(ke.getCode());
                            getRobot().keyRelease(ke.getCode());
                        } else {
                            // create shift key + key code
                            String s = "/Shift " + ke.getName();
                            pressKeys(s, false);
                        }
                    }
                }
            } else {
                for (int i = 0; i < keys.size(); i++) {
                    getRobot().keyPress(keys.get(i).getCode());
                }
                for (int i = keys.size() - 1; i >= 0; i--) {
                    getRobot().keyRelease(keys.get(i).getCode());
                }
            }
            result.setReturnValue(true);
        } catch (Exception ex) {
            result.setMessage(ex.getMessage());
            result.setReturnValue(false);
            result.setNeedReNewDriver(true);
        }
        return result;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}

class KeyElement {
    private String name;

    private int code;

    private Boolean shiftKey;

    public KeyElement(String name) {
        this.code = Util.getKeyCode(name);
        this.name = name;
        shiftKey = false;
        if (code == KeyEvent.CHAR_UNDEFINED) {
            this.code = Util.getShiftKeyCode(name.charAt(0));
            if (this.code != KeyEvent.CHAR_UNDEFINED) {
                this.shiftKey = true;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Boolean getShiftKey() {
        return shiftKey;
    }

    public void setShiftKey(Boolean shiftKey) {
        this.shiftKey = shiftKey;
    }

}

class Util {
    public static int getKeyCode(String strkey) {
        strkey = strkey.toUpperCase();
        if (strkey.equals("ENTER")) {
            return KeyEvent.VK_ENTER;
        }
        if (strkey.equals("TAB")) {
            return KeyEvent.VK_TAB;
        }
        if (strkey.equals("F1")) {
            return KeyEvent.VK_F1;
        }
        if (strkey.equals("F2")) {
            return KeyEvent.VK_F2;
        }
        if (strkey.equals("F3")) {
            return KeyEvent.VK_F3;
        }
        if (strkey.equals("F4")) {
            return KeyEvent.VK_F4;
        }
        if (strkey.equals("F5")) {
            return KeyEvent.VK_F5;
        }
        if (strkey.equals("F6")) {
            return KeyEvent.VK_F6;
        }
        if (strkey.equals("F7")) {
            return KeyEvent.VK_F7;
        }
        if (strkey.equals("F8")) {
            return KeyEvent.VK_F8;
        }
        if (strkey.equals("F9")) {
            return KeyEvent.VK_F9;
        }
        if (strkey.equals("F10")) {
            return KeyEvent.VK_F10;
        }
        if (strkey.equals("F11")) {
            return KeyEvent.VK_F11;
        }
        if (strkey.equals("F12")) {
            return KeyEvent.VK_F12;
        }
        if (strkey.equals("CAPSLOCK")) {
            return KeyEvent.VK_CAPS_LOCK;
        }
        if (strkey.equals("SHIFT")) {
            return KeyEvent.VK_SHIFT;
        }
        if (strkey.equals("CTRL")) {
            return KeyEvent.VK_CONTROL;
        }
        if (strkey.equals("ALT")) {
            return KeyEvent.VK_ALT;
        }
        if (strkey.equals("BACKSPACE")) {
            return KeyEvent.VK_BACK_SPACE;
        }
        if (strkey.equals("HOME")) {
            return KeyEvent.VK_HOME;
        }
        if (strkey.equals("END")) {
            return KeyEvent.VK_END;
        }
        if (strkey.equals("PAGEUP")) {
            return KeyEvent.VK_PAGE_UP;
        }
        if (strkey.equals("PAGEDOWN")) {
            return KeyEvent.VK_PAGE_DOWN;
        }
        if (strkey.equals("UP")) {
            return KeyEvent.VK_UP;
        }
        if (strkey.equals("DOWN")) {
            return KeyEvent.VK_DOWN;
        }
        if (strkey.equals("LEFT")) {
            return KeyEvent.VK_LEFT;
        }
        if (strkey.equals("RIGHT")) {
            return KeyEvent.VK_RIGHT;
        }
        if (strkey.equalsIgnoreCase("Esc")) {
            return KeyEvent.VK_ESCAPE;
        }

        if (strkey.length() == 1) {
            char character = strkey.charAt(0);
            if (character == ' ') return KeyEvent.VK_SPACE;
            if (character == '-') return KeyEvent.VK_MINUS;
            if (character == ',') return KeyEvent.VK_COMMA;
            if (character == '.') return KeyEvent.VK_DECIMAL;
            if (character == ';') return KeyEvent.VK_SEMICOLON;
            if (character == '=') return KeyEvent.VK_EQUALS;
            if (character == '.') return KeyEvent.VK_PERIOD;
            if (character == '/') return KeyEvent.VK_SLASH;
            if (character == '\\') return KeyEvent.VK_BACK_SLASH;

            if ((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z')
                    || (character >= '0' && character <= '9')) {
                character = Character.toUpperCase(character);
                return (int) character;
            }
        }

        return KeyEvent.CHAR_UNDEFINED;
    }

    public static int getShiftKeyCode(char c) {
        // / "!@#$%^&*()_+{}:<>?"
        if (c == '!') {
            return KeyEvent.VK_1;
        }
        if (c == '@') {
            return KeyEvent.VK_2;
        }
        if (c == '#') {
            return KeyEvent.VK_3;
        }
        if (c == '$') {
            return KeyEvent.VK_4;
        }
        if (c == '%') {
            return KeyEvent.VK_5;
        }
        if (c == '^') {
            return KeyEvent.VK_6;
        }
        if (c == '&') {
            return KeyEvent.VK_7;
        }
        if (c == '*') {
            return KeyEvent.VK_8;
        }
        // / ()_+{}:<>?
        if (c == '(') {
            return KeyEvent.VK_9;
        }
        if (c == ')') {
            return KeyEvent.VK_0;
        }
        if (c == '_') {
            return KeyEvent.VK_SUBTRACT;
        }
        if (c == '+') {
            return KeyEvent.VK_EQUALS;
        }
        if (c == '{') {
            return KeyEvent.VK_OPEN_BRACKET;
        }
        if (c == '}') {
            return KeyEvent.VK_CLOSE_BRACKET;
        }
        if (c == ':') {
            return KeyEvent.VK_SEMICOLON;
        }
        if (c == '<') {
            return KeyEvent.VK_COMMA;
        }
        if (c == '>') {
            return KeyEvent.VK_PERIOD;
        }
        if (c == '?') {
            return KeyEvent.VK_SLASH;
        }

        return KeyEvent.CHAR_UNDEFINED;
    }

    public static ArrayList<KeyElement> stringToKey(ArrayList<String> strs) {
        ArrayList<KeyElement> keyElements = new ArrayList<KeyElement>();
        for (String str : strs) {
            KeyElement ke = new KeyElement(str);
            keyElements.add(ke);
        }
        return keyElements;
    }

    /**
     * convert many normal key to keyElement
     * 
     * @param str
     * @return
     */
    public static ArrayList<KeyElement> stringToKey(String str) {
        ArrayList<KeyElement> keyElements = new ArrayList<KeyElement>();
        for (int i = 0; i < str.length(); i++) {
            KeyElement k = new KeyElement(String.valueOf(str.charAt(i)));
            keyElements.add(k);
        }
        return keyElements;
    }

    public static ArrayList<String> slipString(String str) {
        ArrayList<String> l = new ArrayList<String>();
        for (int i = 0; i < str.length(); i++) {
            String s = str.substring(i, i + 1);
            l.add(s);
        }
        return l;
    }

}

enum ExtraType {
    SEQUENTIAL, PARALLEL
}
