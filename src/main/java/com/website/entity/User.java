package com.website.entity;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Date;
import kot.bootstarter.kotmybatis.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yangyu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("user")
public class User {
  /**
   * 主键
   */
  private Long id;

  /**
   * 用户姓名
   */
  private String name;

  /**
   * 手机号
   */
  private String cellPhone;

  /**
   * 邮箱
   */
  private String email;

  /**
   * 用户名，登录时使用
   */
  private String userName;

  /**
   * 密码
   */
  private String password;

  /**
   * 用户状态，1=正常,2=禁用,-9=删除
   */
  private Integer userStatus;

  /**
   * 创建日期
   */
  private Date createTime;

  /**
   * 创建人
   */
  private Long createUser;

  /**
   * 最后修改日期
   */
  private Date updateTime;
}
