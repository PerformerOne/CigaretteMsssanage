package com.hupo.cigarette.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class PreferUtils {
    private static final String CONFIG_FILE = "share_hupo";
    private static SharedPreferences mSp;

    private PreferUtils() {
    }

    public static void openFile(Context context) {
        mSp = context.getSharedPreferences(CONFIG_FILE, Context.MODE_PRIVATE);
    }

    public static void clear() {
        mSp.edit().clear().commit();
    }

    public static <T> void saveBean2Sp(String keyName, T t) {
        ByteArrayOutputStream bos;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            byte[] bytes = bos.toByteArray();
            String ObjStr = Base64.encodeToString(bytes, Base64.DEFAULT);
            SharedPreferences.Editor editor = mSp.edit();
            editor.putString(keyName, ObjStr);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static <T extends Object> T getBeanFromSp(String keyNme) {
        byte[] bytes = Base64.decode(mSp.getString(keyNme, ""), Base64.DEFAULT);
        ByteArrayInputStream bis;
        ObjectInputStream ois = null;
        T obj = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = (T) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
    public static boolean put(String key, boolean value) {
        return mSp.edit().putBoolean(key, value).commit();
    }

    public static boolean put(String key, int value) {
        return mSp.edit().putInt(key, value).commit();
    }

    public static boolean put(String key, long value) {
        return mSp.edit().putLong(key, value).commit();
    }

    public static boolean put(String key, float value) {
        return mSp.edit().putFloat(key, value).commit();
    }

    public static boolean put(String key, String value) {
        return mSp.edit().putString(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return mSp.getBoolean(key, defaultValue);
    }

    public static int getInt(String key, int defaultValue) {
        return mSp.getInt(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        return mSp.getLong(key, defaultValue);
    }

    public static float getFloat(String key, float defaultValue) {
        return mSp.getFloat(key, defaultValue);
    }

    public static String getString(String key, String defaultValue) {
        return mSp.getString(key, defaultValue);
    }

    public static Map<String, ?> getAll() {
        return mSp.getAll();
    }

    public static void remove(String key) {
        mSp.edit().remove(key).commit();
    }
}
