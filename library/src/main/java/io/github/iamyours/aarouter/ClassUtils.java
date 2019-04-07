package io.github.iamyours.aarouter;

// Copy from galaxy sdk ${com.alibaba.android.galaxy.utils.ClassUtils}

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexFile;

/**
 * Scanner, find out class with any conditions, copy from google source code.
 *
 * @author 正纬 <a href="mailto:zhilong.liu@aliyun.com">Contact me.</a>
 * @version 1.0
 * @since 16/6/27 下午10:58
 */
public class ClassUtils {

    private static List<DexFile> getDexFiles(Context context) throws IOException {
        List<DexFile> dexFiles = new ArrayList<>();

        BaseDexClassLoader loader = (BaseDexClassLoader) context.getClassLoader();
        try {
            Field pathListField = field("dalvik.system.BaseDexClassLoader","pathList");
            Object list = pathListField.get(loader);
            Field dexElementsField = field("dalvik.system.DexPathList","dexElements");
            Object[] dexElements = (Object[]) dexElementsField.get(list);
            Field dexFilefield = field("dalvik.system.DexPathList$Element","dexFile");
            for(Object dex:dexElements){
                DexFile dexFile = (DexFile) dexFilefield.get(dex);
                dexFiles.add(dexFile);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return dexFiles;
    }

    private static Field field(String clazz,String fieldName) throws ClassNotFoundException, NoSuchFieldException {
        Class cls = Class.forName(clazz);
        Field field = cls.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }
    /**
     * 通过指定包名，扫描包下面包含的所有的ClassName
     *
     * @param context     U know
     * @param packageName 包名
     * @return 所有class的集合
     */
    public static Set<String> getFileNameByPackageName(Context context, final String packageName) throws IOException {
        final Set<String> classNames = new HashSet<>();

        List<DexFile> dexFiles = getDexFiles(context);
        for (final DexFile dexfile : dexFiles) {
            Enumeration<String> dexEntries = dexfile.entries();
            while (dexEntries.hasMoreElements()) {
                String className = dexEntries.nextElement();
                if (className.startsWith(packageName)) {
                    classNames.add(className);
                }
            }
        }
        return classNames;
    }
}