package com.kot.generator.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

/**
 * @author yangyu
 */

class MakeMapper {

    private GeneralBuilder builder;
    private String tableName;
    private String entityPackages;
    private String mapperPackages;

    MakeMapper(GeneralBuilder builder, String tableName) {
        this.builder = builder;
        this.tableName = tableName;
        this.entityPackages = StringUtils.isBlank(builder.entityPackages) ? builder.packages : builder.entityPackages;
        this.mapperPackages = StringUtils.isBlank(builder.mapperPackages) ? builder.packages : builder.mapperPackages;
    }

    void makeClass() throws IOException {

        //泛型 BaseMapper<user>
        ClassName baseMapper = ClassName.get("kot.bootstarter.kotmybatis.mapper", "BaseMapper");
        ClassName entity = ClassName.get(this.entityPackages + ".entity", tableName);

        TypeSpec.Builder classBuilder = TypeSpec.interfaceBuilder(tableName + "Mapper")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("@author " + builder.author + "\n")
                .addSuperinterface(ParameterizedTypeName.get(baseMapper, entity));

        JavaFile javaFile = JavaFile.builder(this.mapperPackages + ".mapper", classBuilder.build()).build();
        javaFile.writeTo(new File(builder.filePath));
    }

}

