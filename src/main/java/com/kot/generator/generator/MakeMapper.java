package com.kot.generator.generator;

import com.kot.generator.utils.CommonUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

/**
 * @author yangyu
 */

class MakeMapper {

    private String packageName;
    private String tableName;
    private String filePath;
    private static final String AUTHOR = Main.AUTHOR;

    MakeMapper(String packageName, String tableName, String filePath) {
        super();
        this.packageName = packageName;
        this.tableName = tableName;
        this.filePath = filePath;
    }

    void makeClass() throws IOException {

        //泛型 BaseMapper<user>
        ClassName baseMapper = ClassName.get("kot.bootstarter.kotmybatis.mapper", "BaseMapper");
        ClassName entity = ClassName.get(packageName + ".entity", CommonUtils.capitalName(tableName));

        TypeSpec.Builder classBuilder = TypeSpec.interfaceBuilder(CommonUtils.capitalName(tableName) + "Mapper")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("@author " + AUTHOR + "\n")
                .addSuperinterface(ParameterizedTypeName.get(baseMapper, entity));

        JavaFile javaFile = JavaFile.builder(packageName + ".mapper", classBuilder.build()).build();
        javaFile.writeTo(new File(filePath));
    }

}

