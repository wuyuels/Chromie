package org.chromie.util;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * @author liushuai7
 */
public class HtmlUtil {


    private static TemplateEngine templateEngine ;

    static {

        // 定义、设置模板解析器
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        // 设置模板类型 # https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#what-is-thymeleaf
        // HTML、XML、TEXT、JAVASCRIPT、CSS、RAW
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/template/");
        templateResolver.setSuffix(".html");
        // 定义模板引擎
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    public static String getHtml(String html,Context context){
        return templateEngine.process(html, context);
    }
}
