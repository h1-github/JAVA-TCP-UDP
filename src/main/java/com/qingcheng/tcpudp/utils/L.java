package com.qingcheng.tcpudp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by wanghuan on 2017/7/6.
 */
public class L {

    private static Logger logger = LogManager.getLogger();

    public static void f(String message){
        logger.fatal(message);
    }

    public static void e(String message){
        logger.error(message);
    }

    public static void w(String message){
        logger.warn(message);
    }

    public static void i(String message){
        logger.info(message);
    }

    public static void d(String message){
        logger.debug(message);
    }

    public static void t(String message){
        logger.trace(message);
    }

}
