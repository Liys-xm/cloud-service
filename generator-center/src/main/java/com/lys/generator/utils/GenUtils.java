package com.lys.generator.utils;

import cn.hutool.core.io.FileUtil;
import com.lys.generator.entity.ColumnEntity;
import com.lys.generator.entity.ReferencedTable;
import com.lys.generator.entity.TableEntity;
import com.lys.generator.exception.RRException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 日了狗-代码生成器
 */
public class GenUtils {
    public static final char UNDERLINE = '_';

    public static List<String> getTemplates() {

        List<String> templates = new ArrayList<String>();
        templates.add("template/Mapper.java.vm");
        templates.add("template/Mapper.xml.vm");
        templates.add("template/Service.java.vm");
        templates.add("template/ServiceImpl.java.vm");
        templates.add("template/Controller.java.vm");
        templates.add("template/menu.sql.vm");
        templates.add("template/Api.java.vm");
        templates.add("template/Model.java.vm");
        return templates;
    }

    private static VelocityContext getVelocityContext(Configuration config, TableEntity tableEntity) {

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        String mainPath = config.getString("mainPath");
        mainPath = StringUtils.isBlank(mainPath) ? "com.cnadmart" : mainPath;

        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("email", config.getString("email"));
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("listReferencedTable", tableEntity.getListReferencedTable());
        map.put("hasBigDecimal", tableEntity.getHasBigDecimal());
        map.put("mainPath", mainPath);
        map.put("package", config.getString("package"));
        map.put("moduleName", config.getString("moduleName"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", com.lys.generator.utils.DateUtils.format(new Date(), com.lys.generator.utils.DateUtils.DATE_TIME_PATTERN));

        return new VelocityContext(map);
    }

    //生成渲染表的信息
    private static TableEntity getTableInfo(Configuration config, Map<String, String> table, List<ReferencedTable> listReferencedTable,
                                            List<Map<String, String>> columns) {
        boolean hasBigDecimal = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName"));
        tableEntity.setComments(table.get("tableComment"));
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config.getString("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for (Map<String, String> column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setComments(column.get("columnComment"));
            columnEntity.setExtra(column.get("extra"));
            columnEntity.setIsNullable(column.get("isNullable"));
            Object dataLength = column.get("dataLength");
            if (dataLength != null) {
                columnEntity.setDataLength(dataLength.toString());
            } else {
                columnEntity.setDataLength("");
            }
            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);
        tableEntity.setHasBigDecimal(hasBigDecimal);
        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }
        //转换驼峰
        for (ReferencedTable referencedTable : listReferencedTable) {

            String referencedColumnName = columnToJava(referencedTable.getReferencedColumnName());
            String refTableName = StringUtils.uncapitalize(tableToJava(referencedTable.getTableName(), config.getString("tablePrefix")));
            String referencedTableName = StringUtils.uncapitalize(tableToJava(referencedTable.getReferencedTableName(), config.getString("tablePrefix")));
            String refColumnName = columnToJava(referencedTable.getColumnName());
            referencedTable.setTableNameJava(refTableName);
            referencedTable.setReferencedColumnNameJava(referencedColumnName);
            referencedTable.setReferencedTableNameJava(referencedTableName);
            referencedTable.setColumnNameJava(refColumnName);
        }
        tableEntity.setListReferencedTable(listReferencedTable);
        return tableEntity;
    }


    /**
     * 生成代码
     *
     * @throws IOException
     */
    public static void generatorCode(Map<String, String> table, List<ReferencedTable> listReferencedTable,
                                     List<Map<String, String>> columns, ZipOutputStream zip) throws IOException {
        //配置信息
        Configuration config = getConfig();
        TableEntity tableEntity = getTableInfo(config, table, listReferencedTable, columns);
        VelocityContext context = getVelocityContext(config, tableEntity);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(getFileName(template, tableEntity.getClassName(), config.getString("package"), config.getString("moduleName"))));
                IOUtils.write(sw.toString(), zip, "UTF-8");

                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
    }

    /*
     * 生成代码
     * @throws IOException
     */
    public static void generatorAllCode(Map<String, String> table, List<ReferencedTable> listReferencedTable,
                                        List<Map<String, String>> columns) throws IOException {
        //配置信息
        Configuration config = getConfig();
        TableEntity tableEntity = getTableInfo(config, table, listReferencedTable, columns);
        VelocityContext context = getVelocityContext(config, tableEntity);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            if ("template/Api.java.vm".equals(template)) {
                continue;
            }
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            //获取当前项目的根路径
            File directory = new File("");// 参数为空
            String courseFile = directory.getCanonicalPath();
            String ide = config.getString("ide");
            String project = config.getString("adminproject");
            if (StringUtils.isNotEmpty(project)) {

                int i = StringUtils.lastIndexOf(courseFile, "\\");
                if ("eclipse".equals(ide)) {
                    courseFile = courseFile.substring(0, StringUtils.lastIndexOf(courseFile, "\\")) + "\\" + project;
                } else {
                    courseFile += "\\" + project;
                }

            }

            String entityFileName = getFileName(template, tableEntity.getClassName(), config.getString("package"), config.getString("moduleName"));

            FileUtil.writeString(sw.toString(), FileUtil.touch(courseFile + "\\src\\" + entityFileName), "UTF-8");
            //FileUtil.file(s)


        }
    }

    /*
     * 生成接口代码
     * @throws IOException
     */
    public static void generatorApiCode(Map<String, String> table, List<ReferencedTable> listReferencedTable,
                                        List<Map<String, String>> columns) throws IOException {
        //配置信息
        Configuration config = getConfig();

        TableEntity tableEntity = getTableInfo(config, table, listReferencedTable, columns);
        VelocityContext context = getVelocityContext(config, tableEntity);

        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            if ("template/Controller.java.vm".equals(template)) {
                continue;
            }
            if ("template/menu.sql.vm".equals(template)) {
                continue;
            }
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            //获取当前项目的根路径
            File directory = new File("");// 参数为空
            String courseFile = directory.getCanonicalPath();
            String ide = config.getString("ide");
            String project = config.getString("apiproject");
            if (StringUtils.isNotEmpty(project)) {

                int i = StringUtils.lastIndexOf(courseFile, "\\");
                if ("eclipse".equals(ide)) {
                    courseFile = courseFile.substring(0, StringUtils.lastIndexOf(courseFile, "\\")) + "\\" + project;
                } else {
                    courseFile += "\\" + project;
                }

            }

            String entityFileName = getFileName(template, tableEntity.getClassName(), config.getString("package"), config.getString("moduleName"));

            FileUtil.writeString(sw.toString(), FileUtil.touch(courseFile + "\\src\\" + entityFileName), "UTF-8");
            //FileUtil.file(s)


        }
    }


    /**
     * 更新代码
     *
     * @throws IOException
     */
    public static void updateCode(Map<String, String> table, List<ReferencedTable> listReferencedTable,
                                  List<Map<String, String>> columns) throws IOException {
        //配置信息
        Configuration config = getConfig();
        TableEntity tableEntity = getTableInfo(config, table, listReferencedTable, columns);
        VelocityContext context = getVelocityContext(config, tableEntity);
        //获取模板列表
        List<String> templates = getTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            if ("template/Model.java.vm".equals(template)) {
                //获取当前项目的根路径
                File directory = new File("");// 参数为空
                String courseFile = directory.getCanonicalPath();
                String project = config.getString("adminproject");
                String ide = config.getString("ide");
                if (StringUtils.isNotEmpty(project)) {

                    int i = StringUtils.lastIndexOf(courseFile, "\\");
                    if ("eclipse".equals(ide)) {
                        courseFile = courseFile.substring(0, StringUtils.lastIndexOf(courseFile, "\\")) + "\\" + project;
                    } else {
                        courseFile += "\\" + project;
                    }

                }


                String entityFileName = getFileName(template, tableEntity.getClassName(), config.getString("package"), config.getString("moduleName"));

                FileUtil.writeString(sw.toString(), FileUtil.touch(courseFile + "\\src\\" + entityFileName), "UTF-8");

                project = config.getString("apiproject");

                if (StringUtils.isNotEmpty(project)) {

                    int i = StringUtils.lastIndexOf(courseFile, "\\");
                    if ("eclipse".equals(ide)) {
                        courseFile = courseFile.substring(0, StringUtils.lastIndexOf(courseFile, "\\")) + "\\" + project;
                    } else {
                        courseFile += "\\" + project;
                    }

                }


                entityFileName = getFileName(template, tableEntity.getClassName(), config.getString("package"), config.getString("moduleName"));

                FileUtil.writeString(sw.toString(), FileUtil.touch(courseFile + "\\src\\" + entityFileName), "UTF-8");


                //FileUtil.file(s)
            }

        }
    }

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 驼峰格式字符串转换为下划线格式字符串
     *
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static Map camelToUnderlineMap(Map param) {
        Map<String, Object> newMap = new HashMap<String, Object>();
        Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String key = entry.getKey();
            String newKey = camelToUnderline(key);
            newMap.put(newKey, entry.getValue());
        }
        return newMap;
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName, String moduleName) {
        String packagePath = "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }

        if (template.contains("Mapper.java.vm")) {
            return packagePath + "mapper" + File.separator + className + "Mapper.java";
        }

        if (template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.vm")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains("Mapper.xml.vm")) {
            return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName
                    + File.separator + className + "Mapper.xml";
        }

        if (template.contains("menu.sql.vm")) {
            return className.toLowerCase() + "_menu.sql";
        }

        if (template.contains("Api.java.vm")) {
			/*return packagePath + "api" + File.separator + "controller" + File.separator + className
					+ "ApiController.java";*/
            return packagePath + File.separator + "controller" + File.separator + className
                    + "Controller.java";

        }

        if (template.contains("Model.java.vm")) {
            return packagePath + "entity" + File.separator + File.separator + className
                    + "Model.java";
        }

        return null;
    }
}
