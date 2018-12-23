package com.cloud.model.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * 日志对象
 *
 * @author liyaoshi
 */
//builder的setter方法返回builder本身。以便可以把连接起来
// Log person = new Log.Builder(1, "张三",).module("122").params("l").remark("测试使用builder模式").build();
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log implements Serializable {

    private static final long serialVersionUID = -5398795297842978376L;

    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 模块
     */
    private String module;
    /**
     * 参数值
     */
    private String params;
    private String remark;
    /**
     * 是否执行成功
     */
    private Boolean flag;
    private Date createTime;
}
