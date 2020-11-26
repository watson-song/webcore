package cn.watsontech.webhelper.datasource;

import java.util.function.Supplier;

/**
 * Copyright to watsontech
 * Created by Watson on 2019/12/24.
 */
public class DBContextHolder {

    //默认主数据库
    private static final ThreadLocal<DBTypeEnum> contextHolder = ThreadLocal.withInitial(new Supplier<DBTypeEnum>() {
        @Override
        public DBTypeEnum get() {
            return DBTypeEnum.MASTER;
        }
    });

    public static void set(DBTypeEnum dbType) {
        if (dbType!=null) {
            contextHolder.set(dbType);
        }
    }

    public static DBTypeEnum get() {
        return contextHolder.get();
    }

    public static void master() {
        set(DBTypeEnum.MASTER);
    }

    public static void slave() {
        set(DBTypeEnum.SLAVE);
    }

}
