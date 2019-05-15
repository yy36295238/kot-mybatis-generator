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

    private GeneralBuilder builder;
    private String tableName;

    MakeService(GeneralBuilder builder, String tableName) {
        this.builder = builder;
        this.tableName = tableName;
    }

    void makeClass() throws IOException {
        makeService();
        makeServiceImpl();
    }

    private void makeService() throws IOException {
        //泛型 BaseMapper<user>
        ClassName managerService = ClassName.get("kot.bootstarter.kotmybatis.service", "MapperManagerService");
        ClassName entity = ClassName.get(builder.packages + ".entity", CommonUtils.capitalName(tableName));

        TypeSpec.Builder serviceClassBuilder = TypeSpec.interfaceBuilder(CommonUtils.capitalName(tableName) + "Service")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("@author " + builder.author + "\n")
                .addSuperinterface(ParameterizedTypeName.get(managerService, entity));

        JavaFile javaFile = JavaFile.builder(builder.packages + ".service", serviceClassBuilder.build()).build();
        javaFile.writeTo(new File(builder.filePath));
    }

    private void makeServiceImpl() throws IOException {
        //泛型 BaseMapper<user>
        ClassName managerService = ClassName.get("kot.bootstarter.kotmybatis.service.impl", "MapperManagerServiceImpl");
        ClassName service = ClassName.bestGuess(builder.packages + ".service." + CommonUtils.capitalName(tableName) + "Service");
        ClassName entity = ClassName.get(builder.packages + ".entity", CommonUtils.capitalName(tableName));

        TypeSpec.Builder serviceClassBuilder = TypeSpec.classBuilder(CommonUtils.capitalName(tableName) + "ServiceImpl")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("@author " + builder.author + "\n")
                .addAnnotation(Service.class)
                .addSuperinterface(service)
                .superclass(ParameterizedTypeName.get(managerService, entity));

        JavaFile javaFile = JavaFile.builder(builder.packages + ".service.impl", serviceClassBuilder.build()).build();
        javaFile.writeTo(new File(builder.filePath));
    }

}

