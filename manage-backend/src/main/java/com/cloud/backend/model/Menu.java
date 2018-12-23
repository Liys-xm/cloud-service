package com.cloud.backend.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Menu implements Serializable {

    private static final long serialVersionUID = 749360940290141180L;

    private Long id;
    private Long parentId;
    private String name;
    private String css;
    private String url;
    private Integer sort;
    private Date createTime;
    private Date updateTime;

    //2018年12月17日 添加
    private String code;
    private String component;
    private String nameFullPath;
    private int level;
    private String url1;

    private List<Menu> child;
}
