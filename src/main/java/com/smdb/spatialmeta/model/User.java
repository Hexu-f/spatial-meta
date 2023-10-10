package com.smdb.spatialmeta.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String country;
    private String organization;

    //fixme 字段加密处理一下
    private String password;
    private Date createTime;
    private Integer create_by;
    private Date updateTime;
    private Integer updateBy;
    private Integer delFlag;

}
