package io.github.iamyours.aarouter;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ARouter {
    private Map<String, String> routes = new HashMap<>();
    private static final ARouter instance = new ARouter();
    public static final String ROUTES_PACKAGE_NAME = "io.github.iamyours.aarouter.routes";
    private WeakReference<Context> contextRef;
    public void init(Context context){
        contextRef = new WeakReference<>(context);
        try {
            Set<String> names = ClassUtils.getFileNameByPackageName(context,ROUTES_PACKAGE_NAME);
            initRoutes(names);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private ARouter() {

    }

    //通过反射初始化路由
    private void initRoutes(Set<String> names) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for(String name:names){
            Class clazz = Class.forName(name);
            Object obj = clazz.newInstance();
            if(obj instanceof IRoute){
                IRoute route = (IRoute) obj;
                route.loadInto(routes);
            }
        }
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
