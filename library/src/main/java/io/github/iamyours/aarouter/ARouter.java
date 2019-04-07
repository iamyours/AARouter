package io.github.iamyours.aarouter;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexFile;

public class ARouter {
    private Map<String, String> routes = new HashMap<>();
    private static final ARouter instance = new ARouter();
    public static final String ROUTES_PACKAGE_NAME = "io.github.iamyours.aarouter.routes";
    private WeakReference<Context> contextRef;
    public void init(Context context){
        contextRef = new WeakReference<>(context);
        try {
            Set<String> names = ClassUtils.getFileNameByPackageName(context,ROUTES_PACKAGE_NAME);
            System.out.println(names);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private ARouter() {

    }





    public static ARouter getInstance() {
        return instance;
    }

    public Postcard build(String path) {
        String component = routes.get(path);
        if (component == null) throw new RuntimeException("could not find route with " + path);
        return new Postcard(component);
    }
}
