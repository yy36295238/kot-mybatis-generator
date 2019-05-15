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

    private GeneralBuilder builder;
    private String tableName;

    MakeMapper(GeneralBuilder builder, String tableName) {
        this.builder = builder;
        this.tableName = tableName;
    }

    void makeClass() throws IOException {

        //泛型 BaseMapper<user>
        ClassName baseMapper = ClassName.get("kot.bootstarter.kotmybatis.mapper", "BaseMapper");
        ClassName entity = ClassName.get(builder.packages + ".entity", CommonUtils.capitalName(tableName));

        TypeSpec.Builder classBuilder = TypeSpec.interfaceBuilder(CommonUtils.capitalName(tableName) + "Mapper")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("@author " + builder.author + "\n")
                .addSuperinterface(ParameterizedTypeName.get(baseMapper, entity));

        JavaFile javaFile = JavaFile.builder(builder.packages + ".mapper", classBuilder.build()).build();
        javaFile.writeTo(new File(builder.filePath));
    }

}

