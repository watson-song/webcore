package cn.watsontech.core.web.spring;

import cn.watsontech.core.utils.MysqlNoPoolLoadAdapter;
import cn.watsontech.core.utils.NoHelper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * Created by Watson on 2020/4/17.
 */
//@Component
//@Log4j2
public class ApplicationStarListener /*implements ApplicationListener<ApplicationReadyEvent>*/ {

    final int limit;
    final JdbcTemplate jdbcTemplate;
    final List<NoGeneratorType> types;

    public ApplicationStarListener(List<NoGeneratorType> types, int noLimit, JdbcTemplate jdbcTemplate) {
        this.types = types;
        this.limit = noLimit;
        this.jdbcTemplate = jdbcTemplate;
    }

    public class NoGeneratorType {
        int length;
        String extraPrefix;
        String name;

        public NoGeneratorType(String name, String prefix, int length) {
            this.name = name;
            this.length = length;
            this.extraPrefix = prefix;
        }

        public String name() {
            return name;
        }

        public int length() {
            return length;
        }

        public String extraPrefix() {
            return extraPrefix;
        }
    }

//    @Override
    public void onApplicationEvent() {
        //启动编号生成器
        NoHelper noHelper = NoHelper.getInstance();
        NoHelper.NoPoolLoadAdapter noPoolLoadAdapter = new MysqlNoPoolLoadAdapter(jdbcTemplate);
        noHelper.setPoolLoadAdapter(noPoolLoadAdapter);

        if (types!=null) {
            for (NoGeneratorType type:types) {
                //String type/*编号类型*/, int noLength/*编号总长度*/, String prefix/*编号前缀*/, Long originalIndex/*编号初始值*/, int size/*编号池大小*/, NoGeneratorIndexFormat indexFormat
                noHelper.registerGenerator(type.name(), type.length(), null, noPoolLoadAdapter.loadNextIndex(type.name(), limit), limit, NoHelper.NoGeneratorIndexFormat.X);
            }
        }

        System.out.println("编号生成器初始化完毕：" + noHelper.stringGenerators());
    }

}