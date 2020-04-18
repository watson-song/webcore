package com.watsontech.core.utils;

import com.watsontech.core.web.spring.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 编号生成工具
 * Created by Watson on 2020/4/16.
 */
public class NoHelper {
    //缓存编号生成器：key为编号类别，比如：order_20200416,worker,withdraw_application
    Map<String, NoGenerator> cachedGenerator = new ConcurrentHashMap<>();
    Map<String, Object> cachedGeneratorLocks = new ConcurrentHashMap<>();

    //编号池加载适配器
    NoPoolLoadAdapter poolLoadAdapter;

    static NoHelper instance = new NoHelper();

    public static abstract class NoPoolLoadAdapter {

        /**
         * 查询当前编号
         * @param type 类型
         */
        public long loadCurrentIndex(String type) {
            return loadCurrentIndex(type, null);
        }

        /**
         * 查询当前编号
         * @param prefix 前缀
         * @param type 类型
         */
        public long loadCurrentIndex(String type, String prefix) {
            String id = getKey(type, prefix);

            Long currentIndex = loadFromSource(id);//jdbcTemplate.query("select no from tb_nohelper where id=?", new SingleRowMapperResultSetExtractor<Long>(new SingleColumnRowMapper<Long>(Long.class)), id);
            return currentIndex!=null?currentIndex:-1;
        }

        private String getKey(String type, String prefix) {
            return prefix!=null?type+"_"+prefix:type;
        }

        /**
         * 仅加载当前编号
         * @param id 记录id（type_prefix）
         * @return 当前编号
         * return jdbcTemplate.query("select no from tb_nohelper where id=?", new SingleRowMapperResultSetExtractor<Long>(new SingleColumnRowMapper<Long>(Long.class)), id);
         */
        public abstract Long loadFromSource(String id);

        /**
         * 加载下一批编号
         * @param type 类型
         * @param limit 编号池大小
         */
        public long loadNextIndex(String type, int limit) {
            return loadNextIndex(type, null, limit);
        }

        /**
         * 加载下一批编号
         * @param prefix 编号前缀
         * @param type 类型
         * @param limit 编号池大小
         * @return 下一个编号（立即可用的编号）
         */
        public long loadNextIndex(String type, String prefix, int limit) {
            String id = getKey(type, prefix);

            long nextIndex;
            int success = insertNewNoRecord(id, limit);//jdbcTemplate.update("INSERT ignore INTO `tb_nohelper` (`id`, `no`) VALUES (?, ?)", id, limit);
            if (success>0) {
                //1、新增加条目
                nextIndex = 1;
            }else {
                //2、已存在条目
                //锁住 id条目并更新
                Map<String, Long> currentIndexMap = queryCurrentIndexAndVersionForUpdate(id);

                //当前已有数据，更新并返回nextIndex
                success = updateNoRecord(id, limit, currentIndexMap.get("version"));//jdbcTemplate.update("UPDATE `tb_nohelper` set `no`=no+?,`version`=`version`+1 where `id`=? and `version`=?", limit, id, currentIndexMap.get("version"));
                nextIndex = currentIndexMap.get("no");
                Assert.isTrue(success>0, "插入新编号生成器失败，请稍后再试");
            }

            return nextIndex;
        }

        /**
         * 插入新编号生成器 记录条
         * @return 插入成功条数（即是否已存在type_prefix相同记录）
         * int success = jdbcTemplate.update("INSERT ignore INTO `tb_nohelper` (`id`, `no`) VALUES (?, ?)", id, limit);
         */
        public abstract int insertNewNoRecord(String id, int limit);

        /**
         * 更新当前编号生成器 记录条
         * @return 更新成功条数（即更新type_prefix记录条数）
         * int success = jdbcTemplate.update("UPDATE `tb_nohelper` set `no`=no+?,`version`=`version`+1 where `id`=? and `version`=?", limit, id, currentIndexMap.get("version"));
         */
        public abstract int updateNoRecord(String id, int limit, Long version);

        /**
         * 查询当前的currentIndex和数据版本，并锁住当前type_prefix记录条
         * @param id 记录id：type_prefix
         * @return map => {no=?,version=?}
         *
         * //锁住 id条目并更新
         * Map<String, Object> currentIndexMap = jdbcTemplate.query("select `no`,`version` from tb_nohelper where `id`=? for UPDATE", new Object[]{id}, new ResultSetExtractor<Map<String, Object>>() {
         *      @Override
         *      public Map<String, Object> extractData(ResultSet rs) throws SQLException, DataAccessException {
         *          rs.next();
         *          return MapBuilder.builder().putNext("no", rs.getLong("no")).putNext("version", rs.getLong("version"));
         *      }
         * });
         */
        public abstract Map<String, Long> queryCurrentIndexAndVersionForUpdate(String id);
    }

    class SimpleNoPoolLoadAdapter extends NoPoolLoadAdapter {
        Map<String, AtomicLong> cachedNoPool = new ConcurrentHashMap<>();

        @Override
        public Long loadFromSource(String id) {
            AtomicLong currentIndex = cachedNoPool.get(id);
            if (currentIndex==null) {
                currentIndex = new AtomicLong(0);
                cachedNoPool.put(id, currentIndex);
            }
            return currentIndex.longValue();
        }

        @Override
        public int insertNewNoRecord(String id, int limit) {
            cachedNoPool.putIfAbsent(id, new AtomicLong(0));
            return 0;
        }

        @Override
        public int updateNoRecord(String id, int limit, Long version) {
            AtomicLong currentIndex = cachedNoPool.get(id);
            currentIndex.addAndGet(limit);
            return 1;
        }

        @Override
        public Map<String, Long> queryCurrentIndexAndVersionForUpdate(String id) {
            AtomicLong currentIndex = cachedNoPool.get(id);
            return MapBuilder.builder().putNext("no", currentIndex.longValue()).putNext("version", 1l);
        }
    }

    private NoHelper() {}
    public static NoHelper getInstance() {
        return instance;
    }

    /**
     * 设置编号池加载适配器
     * @param poolLoadAdapter
     */
    public void setPoolLoadAdapter(NoPoolLoadAdapter poolLoadAdapter) {
        this.poolLoadAdapter = poolLoadAdapter;
    }

    public NoGenerator registerGenerator(String type/*编号类型*/, int noLength/*编号总长度*/, String prefix/*编号前缀*/, Long originalIndex/*编号初始值*/, int size/*编号池大小*/, NoGeneratorIndexFormat indexFormat) {
        NoGenerator noGenerator = new NoGenerator(type, noLength, prefix, originalIndex, size, indexFormat);
        cachedGenerator.put(type, noGenerator);
        cachedGeneratorLocks.putIfAbsent(type, new Object());
        return noGenerator;
    }

    public String stringGenerators() {
        return cachedGenerator.toString();
    }

    /**
     * 添加编号生成器
     * @param type 类型
     * @param noLength 编号长度
     * @param prefix 编号前缀
     * @param originalIndex 初始值
     * @param size 编号池大小
     * @return 编号生成器
     */
    public NoGenerator addGenerator(String type/*编号类型*/, int noLength/*编号总长度*/, String prefix/*编号前缀*/, Long originalIndex/*编号初始值*/, int size/*编号池大小*/) {
        return addGenerator(type, noLength, prefix, originalIndex, size, null);
    }
    public NoGenerator addGenerator(String type/*编号类型*/, int noLength/*编号总长度*/, String prefix/*编号前缀*/, Long originalIndex/*编号初始值*/, int size/*编号池大小*/, NoGeneratorIndexFormat indexFormat) {
        NoGenerator noGenerator = new NoGenerator(type, noLength, prefix, originalIndex, size, indexFormat);
        cachedGenerator.putIfAbsent(type, noGenerator);
//        cachedGeneratorLocks.putIfAbsent(type+prefix, new Object());
        return noGenerator;
    }

    /**
     * 根据type生成下一个编号
     * @param type
     * @return
     */
    public String nextNo(String type) {
        return nextNoWithPrefixCheck(type,null,null);
    }

    /**
     * 根据type生成下一个编号
     * @param type
     * @return
     */
    public String nextNo(String type, String prefix) {
        return nextNoWithPrefixCheck(type, prefix, null);
    }

    /**
     * 根据type生成下一个编号，检查生成器是否为固定前缀
     * @param type 编号生成器类型，比如：order/worker/withdraw
     * @param extraPrefix 编号额外前缀，在正常前缀前添加额外前缀，比如：01/02/ABC
     * @param checkPrefix 编号前缀检查，比如：20200416******
     * @return
     */
    public String nextNoWithPrefixCheck(String type, String extraPrefix, String checkPrefix) {
        NoGenerator noGenerator = cachedGenerator.get(type);
        Assert.notNull(noGenerator, type+"编号生成器尚未初始化，请稍后再试");

        Object lock = cachedGeneratorLocks.getOrDefault(type, new Object());
        if (checkPrefix!=null) {
            //池编生成器切换，自动触发生成新编号生成器
            if (!noGenerator.checkPrefix(checkPrefix)) {

                if (poolLoadAdapter!=null) {
                    synchronized (lock) {
                        long nextIndex = poolLoadAdapter.loadNextIndex(type, checkPrefix, noGenerator.size);

                        noGenerator = registerGenerator(type, noGenerator.length, checkPrefix, nextIndex, noGenerator.size, noGenerator.indexFormat);
//                        System.out.println(String.format("nextNoWithPrefixCheck(%s) 生成器前缀已更改：checkPrefix=%s，type=%s,extraPrefix=%s, 新Index=%s，新generator=%s", uuid, checkPrefix, type, extraPrefix, nextIndex, noGenerator));
                    }
                }
            }

            Assert.isTrue(noGenerator.checkPrefix(checkPrefix), type+"编号生成器"+checkPrefix+"池已用完，请稍后再试");
        }

        synchronized (lock) {
            //池编号用完自动触发加载更多
            if (noGenerator.isPoolOut()) {
                if (poolLoadAdapter!=null) {
                    long nextIndex = poolLoadAdapter.loadNextIndex(type, noGenerator.prefix, noGenerator.size);

                    noGenerator = registerGenerator(type, noGenerator.length, noGenerator.prefix, nextIndex, noGenerator.size, noGenerator.indexFormat);
    //                System.out.println(String.format("nextNoWithPrefixCheck(%s) 生成器编号耗尽，生成新编号池：type=%s,prefix=%s, 新Index=%s，新generator=%s", uuid, type, noGenerator.prefix, nextIndex, noGenerator));
                }
            }

            return noGenerator.next(extraPrefix);
        }
    }


    //递增序号进制(10进制或16进制)
    public enum NoGeneratorIndexFormat {X/*16进制*/,d/*10进制*/}

    /**
     * 连续编号生成器
     */
    public class NoGenerator {

        /**
         * 检查参数前缀是否和本实例前缀相同
         * @param checkPrefix
         * @return
         */
        public boolean checkPrefix(String checkPrefix) {
            return prefix.equalsIgnoreCase(checkPrefix);
        }

        final String uuid;
        final String type;
        //编号总长度（包含前缀长度）
        final int length;
        //编号前缀
        final String prefix;

        //编号池最大编号值
        final long limitIndex;
        //编号当前索引值
        final AtomicLong currentIndex;
        //编号原始索引值
        final long startIndex;

        //编号format参数：%0{leftLength}d 比如：000234
        final String noFormat;
        final int size;
        final NoGeneratorIndexFormat indexFormat;//默认10进制

        public NoGenerator(String type, int noLength/*编号总长度*/, String prefix/*编号前缀*/, Long startIndex/*编号初始值*/, int size/*编号池大小*/) {
            this(type, noLength, prefix, startIndex, size, null);
        }

        public NoGenerator(String type, int noLength/*编号总长度*/, String prefix/*编号前缀*/, Long startIndex/*编号初始值*/, int size/*编号池大小*/, NoGeneratorIndexFormat indexFormat) {
            this.type = type;
            this.length = noLength;
            this.prefix = (prefix!=null?prefix:"").toUpperCase();

            //对0进行特殊处理，当为0时跳过第0号，同时，limitIndex也少1
            this.startIndex = startIndex;
            this.size = size;

            if (startIndex==0||startIndex==1) {
                this.currentIndex = new AtomicLong(1);//第一个编号 1
                this.limitIndex = size; //limit为size 默认值；size=10，最后一个11（不包含）
            }else {
                this.currentIndex = new AtomicLong(startIndex);//第一个编号 1
                this.limitIndex = this.startIndex+size; //size=10，最后一个11（不包含）
            }

            int leftLength = length - this.prefix.length();
            this.indexFormat = indexFormat!=null?indexFormat:NoGeneratorIndexFormat.d;//默认10进制
            this.noFormat = this.prefix + "%0" + leftLength + indexFormat;

            this.uuid = UUID.randomUUID().toString();
        }

        /**
         * 查询是否编号池已用完
         * @return true已用完
         */
        public boolean isPoolOut() {
            return !(currentIndex.get()<limitIndex);
        }

        /**
         * 生成下一个编号
         */
        public String next() {
            return next(null);
        }

        /**
         * 生成下一个带额外前缀的编号
         * @param extraPrefix 额外前缀
         */
        public String next(String extraPrefix) {
            final long nextIndex;
            Assert.isTrue(currentIndex.get()<limitIndex, String.format("编号池已用完，请稍后再试，currentIndex=%s,limitIndex=%s,type|prefix=%s,startIndex=%s,uuid=%s", currentIndex.get(), limitIndex, type+"|"+prefix, startIndex, uuid));
            nextIndex = currentIndex.getAndIncrement();

            if (extraPrefix==null) {
//                return String.format(noFormat, nextIndex)+"|"+uuid+"|"+this.startIndex;
                return String.format(noFormat, nextIndex);
            }else {
//                return String.format(newNoFormat(extraPrefix), nextIndex)+"|"+uuid+"|"+this.startIndex;
                return String.format(newNoFormat(extraPrefix), nextIndex);
            }
        }

        private String newNoFormat(String extraPrefix) {
            int leftLength = this.length - extraPrefix.length() - this.prefix.length();
            if (leftLength<1) {
                leftLength = 1;
            }
            return extraPrefix.toUpperCase() + this.prefix + "%0"+leftLength+this.indexFormat;
        }

        @Override
        public String toString() {
            return String.format("type=%s| prefix=%s| startIndex=%s| limitIndex=%s| currentIndex=%s, uuid=%s", type, prefix, startIndex, limitIndex, currentIndex.longValue(), uuid);
        }

//        @Override
//        protected void finalize() throws Throwable {
//            System.out.println(String.format("type=%s,prefix=%s,curIndex=%s,size=%s,uuid=%s, %s", type, prefix, currentIndex, size, this.toString(), " 被销毁了"));
//        }
    }

    static String[] noTypes = new String[]{"order","worker","withdraw"};
    static String[] typeShorts = new String[]{"O","W","A"};
    static String[] typePrefixes = new String[]{"20200416","20200417","20200418","20200419","20200430","20200501","20200502","20200503","20200504","20200505","20200506","20200507","20200508","20200509","20200510"};

    public static void main(String[] args) throws InterruptedException {
        NoHelper noHelper = NoHelper.getInstance();
        SimpleNoPoolLoadAdapter simpleNoPoolLoadAdapter = noHelper.new SimpleNoPoolLoadAdapter();
        noHelper.setPoolLoadAdapter(simpleNoPoolLoadAdapter);

        final Map<String, Map<String, String>> nos = new ConcurrentHashMap<>();

        int limit = 10;
        final AtomicLong[] totalLongs = new AtomicLong[noTypes.length];

        final AtomicLong[] noCrashTotalLongs = new AtomicLong[noTypes.length];

        for (int i = 0; i < noTypes.length; i++) {
            String prefix = typePrefixes[0];
            String type = noTypes[i];
            totalLongs[i] = new AtomicLong(0);
            noCrashTotalLongs[i] = new AtomicLong(0);

            noHelper.registerGenerator(type, 15, prefix, simpleNoPoolLoadAdapter.loadNextIndex(type, prefix, limit), limit, NoGeneratorIndexFormat.X);
            nos.put(type, new HashMap<String, String>());
        }

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        int timeGap = 10000;
        long times = typePrefixes.length*timeGap;
        final long startTimestamp = System.currentTimeMillis();

        for (long i = 0; i < times; i++) {
            final long j = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try{
                        int typeIndex = RandomUtils.nextInt(0, noTypes.length);
                        String type = noTypes[typeIndex];
                        //计数+1
                        totalLongs[typeIndex].incrementAndGet();

                        String prefix = typePrefixes[RandomUtils.nextInt(0, typePrefixes.length)];
                        String nextNoString = noHelper.nextNoWithPrefixCheck(type, typeShorts[typeIndex], prefix);
                        String nextNo = nextNoString.split("\\|")[0];


//                        System.out.println(String.format("%4d %s|%s%s %s, threadid=%s", (j+1), " 下一个编号：", type, prefix, nextNoString, Thread.currentThread().getId()));
                        Map<String, String> noMap = nos.get(type);
                        Set<String> nosSet = noMap.keySet();
                        if (nosSet.contains(nextNo)) {
                            System.err.println(String.format("发现编号冲突：type=%s，prefix=%s, no=%s, originalNo=%s, threadid=%s", type, prefix, nextNoString, noMap.get(nextNo), Thread.currentThread().getId()));
                        }else {
                            noMap.put(nextNo, nextNoString);
                            noCrashTotalLongs[typeIndex].incrementAndGet();
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown();

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                long usedMillseconds = System.currentTimeMillis()-startTimestamp;
                System.out.println(times+"个编号，用时："+(usedMillseconds)+"ms，"+(usedMillseconds/1000.00)+"s");

                long totalSize = 0l;
                Object[] keys = nos.keySet().toArray();
                for (int i = 0; i < keys.length; i++) {
                    Map<String, String> values = nos.get(keys[i]);
                    String key = keys[i].toString();
                    int typeIndex = -1;
                    if (noTypes[0].equals(key)) {
                        typeIndex = 0;
                    }else if (noTypes[1].equals(key)) {
                        typeIndex = 1;
                    }else if (noTypes[2].equals(key)) {
                        typeIndex = 2;
                    }

                    if (values.size()==totalLongs[typeIndex].longValue()) {
                        System.out.println(String.format("%s编号 %s个，应该%s个 ", key, values.size(), totalLongs[typeIndex]));
                    }else {
                        System.err.println(String.format("%s编号 %s个，应该%s个，少%s个 ", key, values.size(), totalLongs[typeIndex], totalLongs[typeIndex].longValue()-values.size()));
                    }
                    totalSize = totalSize + values.size();
                }

                long shouldTotalSize = totalLongs[0].longValue()+totalLongs[1].longValue()+totalLongs[2].longValue();

                long noCrashTotalSize = noCrashTotalLongs[0].longValue()+noCrashTotalLongs[1].longValue()+noCrashTotalLongs[2].longValue();

                if (shouldTotalSize==totalSize) {
                    System.out.println(String.format("合计：%s个, noCrashTotalSize=%s", totalSize, noCrashTotalSize));
                }else {
                    System.err.println(String.format("合计：%s个, 少%s个, noCrashTotalSize=%s", totalSize, shouldTotalSize-totalSize, noCrashTotalSize));
                }

                //100000 用时：1702ms，1.702
                //1000000个编号，用时：11030ms，11.03s  1000000个编号，用时：11161ms，11.161s 1000000个编号，用时：11201ms，11.201s
                //checkPrefix 没有 synchronize 1000000个编号，用时：11074ms，11.074s 1000000个编号，用时：11334ms，11.334s 1000000个编号，用时：10951ms，10.951s
            }
        });
    }
}
