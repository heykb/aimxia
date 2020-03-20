/*
 * Copyright (c) 2014 xiaomaihd and/or its affiliates.All Rights Reserved.
 *            http://www.xiaomaihd.com
 */
package com.zhu.rimxia.biz.util.id;

/**
 * Created by YangFan on 15/4/24 下午3:12.
 * <p>
 * ID工具类
 */
public class IdUtil {

    private  static class SingleIdWorker{
        public static IdWorker idWorker = new IdWorker(1L,0);
    }
    private IdUtil(){};
    public static long generateId() {
        return SingleIdWorker.idWorker.nextId();
    }


}