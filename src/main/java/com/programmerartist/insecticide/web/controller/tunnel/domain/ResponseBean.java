package com.programmerartist.insecticide.web.controller.tunnel.domain;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by 程序员Artist on 16/3/25.
 */
public class ResponseBean<D> {

    private int code;
    private D domain;

    /**
     *
     */
    public enum CodeEnum {
        OK(200),
        NOT_FIND(404),
        ERROR(500);

        private int value;

        CodeEnum(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public ResponseBean(int code, D domain) {
        this.code = code;
        this.domain = domain;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public D getDomain() {
        return domain;
    }

    public void setDomain(D domain) {
        this.domain = domain;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "code=" + code +
                ", domain=" + domain +
                '}';
    }

    /**
     *
     * @return
     */
    public String toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("response", this);

        return jsonObject.toJSONString();
    }

}
