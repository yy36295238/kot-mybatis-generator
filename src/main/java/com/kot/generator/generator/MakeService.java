package com.kot.generator.generator;

import com.kot.generator.utils.CommonUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.springframework.stereotype.Service;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

/**
 * @author yangyu
 */

class MakeService {

    private String packageName;
    private String tableName;
    private String filePath;
    private static final String AUTHOR = Main.AUTHOR;

    MakeService(String packageName, String tableName, String filePath) {
        super();
        this.packageName = packageName;
        this.tableName = tableName;
        this.filePath = filePath;
    }

    void makeClass() throws IOException {
        makeService();
        makeServiceImpl();
    }

    private void makeService() throws IOException {
        //泛型 BaseMapper<user>
        ClassName managerService = ClassName.get("kot.bootstarter.kotmybatis.service", "MapperManagerService");
        ClassName entity = ClassName.get(packageName + ".entity", CommonUtils.capitalName(tableName));

        TypeSpec.Builder serviceClassBuilder = TypeSpec.interfaceBuilder(CommonUtils.capitalName(tableName) + "Service")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("@author " + AUTHOR + "\n")
                .addSuperinterface(ParameterizedTypeName.get(managerService, entity));

        JavaFile javaFile = JavaFile.builder(packageName + ".service", serviceClassBuilder.build()).build();
        javaFile.writeTo(new File(filePath));
    }

    private void makeServiceImpl() throws IOException {
        //泛型 BaseMapper<user>
        ClassName managerService = ClassName.get("kot.bootstarter.kotmybatis.service.impl", "MapperManagerServiceImpl");
        ClassName service = ClassName.bestGuess(packageName + ".service." + CommonUtils.capitalName(tableName) + "Service");
        ClassName entity = ClassName.get(packageName + ".entity", CommonUtils.capitalName(tableName));

        TypeSpec.Builder serviceClassBuilder = TypeSpec.classBuilder(CommonUtils.capitalName(tableName) + "ServiceImpl")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("@author " + AUTHOR + "\n")
                .addAnnotation(Service.class)
                .addSuperinterface(service)
                .superclass(ParameterizedTypeName.get(managerService, entity));

        JavaFile javaFile = JavaFile.builder(packageName + ".service.impl", serviceClassBuilder.build()).build();
        javaFile.writeTo(new File(filePath));
    }

}

