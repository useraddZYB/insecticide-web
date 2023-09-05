package com.programmerartist.insecticide.web.controller.view;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 程序员Artist on 16/3/28.
 */
public class Purview {
    public static final String SESSION_USER = "user";

    /**
     *
     */
    public enum Sorry {
        NOT_ADMIN("sorry! you are not admin, can not do these.");

        private String value;

        Sorry(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    /**
     *
     */
    public static final Map<String, String> SUPPER = new HashMap<>();
    static {
        SUPPER.put("admin", "123456");
        SUPPER.put("artist", "123456");
    }


    /**
     *
     * @param request
     * @return
     */
    public static boolean isAdmin(HttpServletRequest request) {

        if(null!=request && null!=request.getSession() && null!=request.getSession().getAttribute(SESSION_USER)) {
            return true;
        }

        return false;
    }

}
