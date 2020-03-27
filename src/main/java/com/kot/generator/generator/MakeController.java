package com.kot.generator.generator;

import com.kot.generator.utils.ResponseResult;
import com.squareup.javapoet.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;

import static com.kot.generator.utils.CommonUtils.lowerName;

/**
 * @author yangyu
 */

class MakeController {

    private static final String PREFIX = "I";
    private GeneralBuilder builder;
    private String tableName;
    private String entityPackages;
    private String servicePackages;
    private String controllerPackages;

    MakeController(GeneralBuilder builder, String tableName) {
        this.builder = builder;
        this.tableName = tableName;
        this.entityPackages = StringUtils.isBlank(builder.entityPackages) ? builder.packages : builder.entityPackages;
        this.controllerPackages = StringUtils.isBlank(builder.controllerPackages) ? builder.packages : builder.controllerPackages;
        this.servicePackages = StringUtils.isBlank(builder.servicePackages) ? builder.packages : builder.servicePackages;
    }

    void makeClass() throws IOException {
        makeController();
    }

    private void makeController() throws IOException {
        ClassName service = ClassName.get(servicePackages + ".service", PREFIX + tableName + "Service");
        ClassName entity = ClassName.get(this.entityPackages + ".entity", tableName);

        String serviceName = lowerName(tableName) + "Service";
        FieldSpec controllerField = FieldSpec.builder(service, serviceName)
                .addModifiers(Modifier.PRIVATE)
                .addAnnotation(Autowired.class)
                .build();


        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(tableName + "Controller")
                .addModifiers(Modifier.PUBLIC)
                .addJavadoc("@author " + builder.author + "\n")
                .addAnnotation(RestController.class)
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class)
                        .addMember("value", "$S", "/api/v1/" + lowerName(tableName)).build())
                .addAnnotation(AnnotationSpec.builder(Api.class)
                        .addMember("tags", "$S", "管理API").build())
                .addModifiers(Modifier.PUBLIC)
                .addField(controllerField)
                // 新增
                .addMethod(add(entity, tableName, serviceName))
                .addMethod(list(entity, tableName, serviceName))
                .addMethod(page(entity, tableName, serviceName))
                .addMethod(id(entity, tableName, serviceName))
                .addMethod(updateById(entity, tableName, serviceName))
                .addMethod(deleteById(entity, tableName, serviceName));


        JavaFile javaFile = JavaFile.builder(this.controllerPackages + ".controller", typeSpec.build()).build();
        javaFile.writeTo(new File(builder.filePath));
    }

    private MethodSpec add(ClassName entity, String tableName, String serviceName) {
        return MethodSpec.methodBuilder("add")
                .addAnnotation(AnnotationSpec.builder(ApiOperation.class)
                        .addMember("value", "$S", "/新增").build())
                .addAnnotation(AnnotationSpec.builder(PostMapping.class)
                        .addMember("value", "$S", "/add").build())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(entity, lowerName(tableName))
                .returns(ResponseResult.class)
                .addStatement("return ResponseResult.ok(" + serviceName + ".newUpdate().insert(" + lowerName(tableName) + "))")
                .build();
    }

    private MethodSpec list(ClassName entity, String tableName, String serviceName) {
        return MethodSpec.methodBuilder("list")
                .addAnnotation(AnnotationSpec.builder(ApiOperation.class)
                        .addMember("value", "$S", "/列表").build())
                .addAnnotation(AnnotationSpec.builder(GetMapping.class)
                        .addMember("value", "$S", "/list").build())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(entity, lowerName(tableName))
                .returns(ResponseResult.class)
                .addStatement("return ResponseResult.ok(" + serviceName + ".newQuery().orderByIdDesc().list(" + lowerName(tableName) + "))")
                .build();
    }

    private MethodSpec page(ClassName entity, String tableName, String serviceName) {
        ClassName page = ClassName.get("kot.bootstarter.kotmybatis.common", "Page");
        return MethodSpec.methodBuilder("page")
                .addAnnotation(AnnotationSpec.builder(ApiOperation.class)
                        .addMember("value", "$S", "/分页").build())
                .addAnnotation(AnnotationSpec.builder(GetMapping.class)
                        .addMember("value", "$S", "/page").build())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(page, entity), "page")
                .addParameter(entity, lowerName(tableName))
                .returns(ResponseResult.class)
                .addStatement("return ResponseResult.ok(" + serviceName + ".newQuery().activeLike().orderByIdDesc().selectPage(page, " + lowerName(tableName) + "))")
                .build();
    }

    private MethodSpec id(ClassName entity, String tableName, String serviceName) {
        return MethodSpec.methodBuilder("findById")
                .addAnnotation(AnnotationSpec.builder(ApiOperation.class)
                        .addMember("value", "$S", "/根据id查询").build())
                .addAnnotation(AnnotationSpec.builder(ApiImplicitParam.class)
                        .addMember("name", "$S", "id")
                        .addMember("value", "$S", "主键id")
                        .addMember("required", "$L", "true")
                        .build())
                .addAnnotation(AnnotationSpec.builder(GetMapping.class)
                        .addMember("value", "$S", "/id").build())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Long.class, "id")
                .returns(ResponseResult.class)
                .addStatement("return ResponseResult.ok(" + serviceName + ".newQuery().findOne(" + tableName + ".builder().id(id).build()))")
                .build();
    }

    private MethodSpec updateById(ClassName entity, String tableName, String serviceName) {
        return MethodSpec.methodBuilder("updateById")
                .addAnnotation(AnnotationSpec.builder(ApiOperation.class)
                        .addMember("value", "$S", "/根据id更新").build())
                .addAnnotation(AnnotationSpec.builder(ApiImplicitParam.class)
                        .addMember("name", "$S", "id")
                        .addMember("value", "$S", "主键id")
                        .addMember("required", "$L", "true")
                        .build())
                .addAnnotation(AnnotationSpec.builder(PostMapping.class)
                        .addMember("value", "$S", "/updateById").build())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(entity, lowerName(tableName))
                .returns(ResponseResult.class)
                .addStatement("return ResponseResult.ok(" + serviceName + ".newUpdate().updateById(" + lowerName(tableName) + "))")
                .build();
    }

    private MethodSpec deleteById(ClassName entity, String tableName, String serviceName) {
        return MethodSpec.methodBuilder("deleteById")
                .addAnnotation(AnnotationSpec.builder(ApiOperation.class)
                        .addMember("value", "$S", "/根据id删除").build())
                .addAnnotation(AnnotationSpec.builder(ApiImplicitParam.class)
                        .addMember("name", "$S", "id")
                        .addMember("value", "$S", "主键id")
                        .addMember("required", "$L", "true")
                        .build())
                .addAnnotation(AnnotationSpec.builder(PostMapping.class)
                        .addMember("value", "$S", "/deleteById").build())
                .addModifiers(Modifier.PUBLIC)
                .addParameter(entity, lowerName(tableName))
                .returns(ResponseResult.class)
                .addStatement("return ResponseResult.ok(" + serviceName + ".newUpdate().delete(" + lowerName(tableName) + "))")
                .build();
    }


}

