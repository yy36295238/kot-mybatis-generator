package com.kot.generator.generator;

import com.kot.generator.utils.CommonUtils;
import com.kot.generator.utils.DatabaseUtils;
import com.squareup.javapoet.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author yangyu
 */

class MakeEntity {

    private String packageName;
    private String tableName;
    private String filePath;
    private List<DatabaseUtils.ColumnInfo> columnInfos;
    private static final String AUTHOR = Main.AUTHOR;

    MakeEntity(String packageName, String tableName, String filePath, List<DatabaseUtils.ColumnInfo> columnInfos) {
        super();
        this.packageName = packageName;
        this.tableName = tableName;
        this.filePath = filePath;
        this.columnInfos = columnInfos;
    }

    void makeClass() throws IOException {

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(CommonUtils.captureName(tableName));

        columnInfos.forEach(c -> classBuilder.addField(FieldSpec.builder(CommonUtils.changeType(c.getType()), CommonUtils.camelCaseName(c.getName()), Modifier.PRIVATE)
                .addJavadoc(c.getComment() + "\n").build()));

        // 公共方法
        classBuilder.addModifiers(Modifier.PUBLIC);
        // 添加类注解
        classBuilder.addAnnotation(Data.class)
                .addAnnotation(AllArgsConstructor.class)
                .addAnnotation(NoArgsConstructor.class)
                .addAnnotation(Builder.class)
                // 注解添加属性
                .addAnnotation(AnnotationSpec.builder(ClassName.bestGuess("kot.bootstarter.kotmybatis.annotation.TableName"))
                        .addMember("value", "$S", tableName)
                        .build())
                .addJavadoc("@author " + AUTHOR + "\n");
        JavaFile javaFile = JavaFile.builder(packageName + ".entity", classBuilder.build()).build();
        javaFile.writeTo(new File(filePath));
    }

}

