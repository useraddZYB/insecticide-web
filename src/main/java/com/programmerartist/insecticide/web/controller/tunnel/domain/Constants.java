package com.programmerartist.insecticide.web.controller.tunnel.domain;

/**
 * 常量设置
 *
 * Created by 程序员Artist on 16/3/28.
 */
public interface Constants {

    enum Log {
        PRE("[insecticide] ");

        private String value;

        Log(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
