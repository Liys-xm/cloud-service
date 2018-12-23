package com.lys.generator.utils;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] ages) throws IOException {
        //获取当前项目的根路径
        File directory = new File("");// 参数为空
        String courseFile = directory.getCanonicalPath();
        System.out.print(directory.getAbsolutePath());
        System.out.print(courseFile);
    }
}
