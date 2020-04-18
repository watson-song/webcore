package cn.watsontech.core.utils;

import java.util.Random;

/**
 * Created by watson on 2020/4/17.
 */
public class RandomUtils {

    public static int nextInt(int startInclude, int endExclude) {
        return startInclude+new Random().nextInt(endExclude);
    }
}
