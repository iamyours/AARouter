package io.github.iamyours.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import io.github.iamyours.aarouter.ARouter;
import io.github.iamyours.aarouter.annotation.Route;

/**
 * Created by yanxx on 2017/7/28.
 */
@AutoService(Processor.class)
public class RouteProcessor extends AbstractProcessor {
    private Filer filer;
    private Map<String, String> routes = new HashMap<>();
    private String moduleName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (Element e : roundEnvironment.getElementsAnnotatedWith(Route.class)) {
            addRoute(e);
        }
        createRouteFile();
        return true;
    }

    private void createRouteFile() {
        TypeSpec.Builder builder = TypeSpec.classBuilder("AARouterMap_" + moduleName).addModifiers(Modifier.PUBLIC);
        TypeName superInterface = ClassName.bestGuess("io.github.iamyours.aarouter.IRoute");
        builder.addSuperinterface(superInterface);
        TypeName stringType = ClassName.get(String.class);
        TypeName mapType = ParameterizedTypeName.get(ClassName.get(Map.class), stringType, stringType);
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("loadInto")
                .addAnnotation(Override.class)
                .returns(void.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(mapType, "routes");
        for (String key : routes.keySet()) {
            methodBuilder.addStatement("routes.put($S,$S)", key, routes.get(key));
        }
        builder.addMethod(methodBuilder.build());
        JavaFile javaFile = JavaFile.builder(ARouter.ROUTES_PACKAGE_NAME, builder.build()).build();
        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    private void addRoute(Element e) {
        Route route = e.getAnnotation(Route.class);
        String path = route.path();
        String name = e.toString();
        moduleName = path.substring(1,path.lastIndexOf("/"));
        routes.put(path, name);
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Route.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

}
