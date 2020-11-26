package cn.watsontech.webhelper.mybatis.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Watson on 2020/8/12.
 * description 继承CreatedEntity接口，并额外实现用户自定义接口，多个接口用 ； 号隔开
 */
public class ExtendEntityInterfacePlugin extends PluginAdapter {
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String extraInterfacePackages = properties.getProperty("extraInterfacePackages");

        if (extraInterfacePackages!=null&&!"".equals(extraInterfacePackages)) {
            String[] interfaceClasses = extraInterfacePackages.split(";");
            for (int i = 0; i < interfaceClasses.length; i++) {
//                topLevelClass.addImportedType(interfaceClasses[i]);
                if (!"".equals(interfaceClasses[i])) {
                    topLevelClass.addSuperInterface(new FullyQualifiedJavaType(interfaceClasses[i]));
                }
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        topLevelClass.addFileCommentLine("/**" +
                "* @generatedBy Watson WebCore " + dateFormat.format(new Date()) +
                "*/");

        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }
}
