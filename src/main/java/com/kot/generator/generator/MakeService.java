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

        //泛型 BaseMapper<user>
        ClassName managerService = ClassName.get("kot.bootstarter.kotmybatis.service.impl", "MapperManagerServiceImpl");
        ClassName entity = ClassName.get(packageName + ".entity", CommonUtils.captureName(tableName));

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(CommonUtils.captureName(tableName) + "Service")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("@author " + AUTHOR + "\n")
                .addAnnotation(Service.class)
                .superclass(ParameterizedTypeName.get(managerService, entity));

        JavaFile javaFile = JavaFile.builder(packageName + ".service", classBuilder.build()).build();
        javaFile.writeTo(new File(filePath));
    }

}

