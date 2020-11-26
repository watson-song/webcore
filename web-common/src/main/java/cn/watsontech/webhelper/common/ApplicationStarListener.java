package cn.watsontech.webhelper.common;

import cn.watsontech.webhelper.utils.NoHelper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.List;

/**
 * Created by Watson on 2020/4/17.
 */
//@Component
public class ApplicationStarListener implements ApplicationListener {

    final int limit;
    final List<NoGeneratorType> types;
    final NoHelper.NoPoolLoadAdapter noPoolLoadAdapter;

    public ApplicationStarListener(List<NoGeneratorType> types, int noLimit, NoHelper.NoPoolLoadAdapter noPoolLoadAdapter) {
        this.types = types;
        this.limit = noLimit;
        this.noPoolLoadAdapter = noPoolLoadAdapter;
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

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ApplicationReadyEvent) {
            //启动编号生成器
            NoHelper noHelper = NoHelper.getInstance();
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

}