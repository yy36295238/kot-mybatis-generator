package com.website.entity;

import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
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
@TableName("role")
public class Role {
  /**
   * 主键
   */
  private Long id;

  /**
   * 角色（admin,owner,mamager,member）
   */
  private String role;

  /**
   * 角色名称
   */
  private String roleName;

  /**
   * 角色级别
   */
  private Integer roleLevel;
}
