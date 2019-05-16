package com.kot.generator.generator;

import com.kot.generator.utils.CommonUtils;
import com.squareup.javapoet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.Modifier;
import java.io.File;

/**
 * @author yangyu
 */

class MakeController {

    private GeneralBuilder builder;
    private String tableName;

    MakeController(GeneralBuilder builder, String tableName) {
        this.builder = builder;
        this.tableName = tableName;
    }


    public void makeController() throws Exception {

        ClassName service = ClassName.get(builder.packages + ".service", tableName + "Service");

        FieldSpec serviceField = FieldSpec.builder(service, CommonUtils.lowerName(tableName) + "Service")
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(Autowired.class)
                .build();

        ClassName model = ClassName.get(builder.packages + ".model", tableName);

		/*----------findOne--------*/
        MethodSpec findOne = MethodSpec.methodBuilder("findOne")
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class)
                        .addMember("value", "$S", "/findOne").build())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(model, tableName)
                .returns(model)
                .addStatement("return " + CommonUtils.lowerName(tableName) + "Service.findOne(example)")
                .build();

		/*----------page--------*/
        ClassName list = ClassName.get("java.util", "List");
        TypeName returnList = ParameterizedTypeName.get(list, model);
        MethodSpec findPage = MethodSpec.methodBuilder("page")
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class)
                        .addMember("value", "$S", "/page").build())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(model, "example")
                .returns(returnList)
                .addStatement("return " + CommonUtils.lowerName(tableName) + "Service.findPage(example)")
                .build();


		/*----------save--------*/
        MethodSpec insert = MethodSpec.methodBuilder("save")
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class)
                        .addMember("value", "$S", "/save").build())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(model, "example")
                .returns(Integer.class)
                .addStatement("return " + CommonUtils.lowerName(tableName) + "Service.insert(example)")
                .build();

		/*----------insert--------*/
        MethodSpec update = MethodSpec.methodBuilder("update")
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class)
                        .addMember("value", "$S", "/update").build())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(model, "example")
                .returns(Integer.class)
                .addStatement("return " + CommonUtils.lowerName(tableName) + "Service.update(example)")
                .build();


        TypeSpec typeSpec = TypeSpec.classBuilder(tableName + "Controller")
                .addJavadoc("@author yangyu\n")
                .addAnnotation(RestController.class)
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class)
                        .addMember("value", "$S", "/" + CommonUtils.lowerName(tableName)).build())
                .addModifiers(Modifier.PUBLIC)
                .addField(serviceField)
                .addMethod(findOne)
                .addMethod(findPage)
                .addMethod(insert)
                .addMethod(update)
                .build();

        JavaFile javaFile = JavaFile.builder(builder.packages + ".controller", typeSpec).build();
        javaFile.writeTo(new File(builder.filePath));
    }

}

