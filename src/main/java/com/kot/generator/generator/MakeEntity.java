package com.kot.generator.generator;

import com.kot.generator.utils.CommonUtils;
import com.kot.generator.utils.DatabaseUtils;
import com.squareup.javapoet.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author yangyu
 */

class MakeEntity {

    private GeneralBuilder builder;
    private String tableName;
    private List<DatabaseUtils.ColumnInfo> columnInfos;
    private String entityPackages;

    MakeEntity(GeneralBuilder builder, String tableName, List<DatabaseUtils.ColumnInfo> columnInfos) {
        this.builder = builder;
        this.tableName = tableName;
        this.columnInfos = columnInfos;
        this.entityPackages = StringUtils.isBlank(builder.entityPackages) ? builder.packages : builder.entityPackages;
    }

    void makeClass() throws IOException {

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(tableName);

        columnInfos.forEach(c -> {
            final FieldSpec.Builder fieldBuilder = FieldSpec.builder(CommonUtils.changeType(c.getType()), CommonUtils.camelCaseName(c.getName()), Modifier.PRIVATE)
                    .addJavadoc(c.getComment() + "\n");
            if (builder.enableSwagger) {
                fieldBuilder.addAnnotation(AnnotationSpec.builder(ApiModelProperty.class)
                        .addMember("value", "$S", c.getComment())
                        .addMember("dataType", "$S", CommonUtils.changeType(c.getType()).getSimpleName())
                        .addMember("name", "$S", CommonUtils.camelCaseName(c.getName()))
                        .build());
            }
            classBuilder.addField(fieldBuilder.build());

        });


        // 公共方法
        classBuilder.addModifiers(Modifier.PUBLIC)
                // 添加类注解
                .addAnnotation(Data.class)
                .addAnnotation(AllArgsConstructor.class)
                .addAnnotation(NoArgsConstructor.class)
                .addAnnotation(Builder.class)
                // 注解添加属性
                .addAnnotation(AnnotationSpec.builder(ClassName.bestGuess("kot.bootstarter.kotmybatis.annotation.TableName"))
                        .addMember("value", "$S", tableName)
                        .build())
                .addJavadoc("@author " + builder.author + "\n");
        JavaFile javaFile = JavaFile.builder(this.entityPackages + ".entity", classBuilder.build()).build();
        javaFile.writeTo(new File(builder.filePath));
    }

}

