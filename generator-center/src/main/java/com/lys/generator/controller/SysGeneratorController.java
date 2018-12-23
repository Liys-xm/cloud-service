package com.lys.generator.controller;

import com.alibaba.fastjson.JSON;
import com.lys.generator.service.SysGeneratorService;
import com.lys.generator.utils.*;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * 日了狗-代码生成器
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
    @Autowired
    private SysGeneratorService sysGeneratorService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    public Result list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        List<Map<String, Object>> list = sysGeneratorService.queryList(query);
        int total = sysGeneratorService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());

        return Result.ok().put("page", pageUtil);
    }

    /**
     * 生成代码
     *
     * @throws Exception
     */
    @RequestMapping("/doc")
    public void doc(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map map = DocMapFactory.build();

        Template template = FreemarkerUtils.getTemplate("dbTemplate.ftl");
        // 根据模板生成文件
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        StringWriter sw = new StringWriter();
        template.process(map, sw);
        IOUtils.write(sw.toString(), outputStream, "UTF-8");
        IOUtils.closeQuietly(sw);
        IOUtils.closeQuietly(outputStream);
        byte[] data = outputStream.toByteArray();
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"doc.doc\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code")
    public void code(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] tableNames = new String[]{};
        String tables = request.getParameter("tables");
        tableNames = JSON.parseArray(tables).toArray(tableNames);

        byte[] data = sysGeneratorService.generatorCode(tableNames);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"generator.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }

    /**
     * 更新全部后端代码
     */
    @RequestMapping("/allcode")
    @ResponseBody
    public Result allcode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] tableNames = new String[]{};
        String tables = request.getParameter("tables");
        tableNames = JSON.parseArray(tables).toArray(tableNames);

        sysGeneratorService.generatorAllCode(tableNames);

        return Result.ok("后端代码全部更新成功，请刷新IDE");
    }

    /**
     * 更新全部api接口代码
     */
    @RequestMapping("/apicode")
    @ResponseBody
    public Result apicode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] tableNames = new String[]{};
        String tables = request.getParameter("tables");
        tableNames = JSON.parseArray(tables).toArray(tableNames);

        sysGeneratorService.generatorApiCode(tableNames);

        return Result.ok("移动端接口全部更新成功，请刷新IDE");
    }


    /**
     * 更新代码
     */
    @ResponseBody
    @RequestMapping("/update")
    public Result update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] tableNames = new String[]{};
        String tables = request.getParameter("tables");
        tableNames = JSON.parseArray(tables).toArray(tableNames);

        sysGeneratorService.updateCode(tableNames);
        return Result.ok("代码更新成功，请刷新IDE");

    }
}
