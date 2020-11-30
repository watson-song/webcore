###How To Start
自动生成Generator代码放置在test源码里，pom文件需添加下列lib

###MybatisGenerator库
    <dependency>
        <groupId>org.mybatis.generator</groupId>
        <artifactId>mybatis-generator-core</artifactId>
        <version>1.3.7</version>
        <scope>test</scope>
    </dependency>

###freemark库
    <dependency>
        <groupId>org.freemarker</groupId>
        <artifactId>freemarker</artifactId>
        <version>2.3.30</version>
        <scope>test</scope>
    </dependency>

###Guava库    
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>30.0-jre</version>
        <scope>test</scope>
    </dependency>

###Swagger注释插件库        
    <dependency>
        <groupId>com.github.misterchangray.mybatis.generator.plugins</groupId>
        <artifactId>myBatisGeneratorPlugins</artifactId>
        <version>1.2</version>
        <scope>test</scope>
    </dependency>