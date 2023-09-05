package com.programmerartist.insecticide.web.controller.tunnel;

import com.programmerartist.insecticide.web.controller.tunnel.domain.LRULinkedHashMap;
import com.programmerartist.insecticide.web.controller.tunnel.domain.TraceConfig;
import com.programmerartist.insecticide.web.controller.tunnel.domain.TraceIdBean;

import java.util.HashMap;
import java.util.Map;

/**
 * server端相关容器类
 *
 * Created by 程序员Artist on 16/3/24.
 */
public class Container {

    /**
     * 控制器主容器:
     *
     * 全局核心数据
     *
     */
    public static class Server {
        /**
         * 对每一个链路业务的配置(白名单/开关等)
         */
        public static Map<String, TraceConfig> tName2Config = new HashMap<>();
        /**
         * 记录详细的链路上报的报告
         */
        public static Map<String, Map<String, LRULinkedHashMap<String, TraceIdBean>>> tName2Did2Tid2Beans = new HashMap<>();
    }

}
