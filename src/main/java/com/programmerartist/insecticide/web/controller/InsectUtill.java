package com.programmerartist.insecticide.web.controller;

import com.programmerartist.insecticide.web.controller.tunnel.domain.Constants;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 程序员Artist on 16/3/28.
 */
public class InsectUtill {

    /**
     *
     * @return
     */
    public static String logPre(HttpServletRequest request) {

        String pre = Constants.Log.PRE.getValue();
        if(null != request) {
            pre += "[" + InsectUtill.getRemoteIP(request) +"] ";

            if(null != request.getSession()) {
                //todo request.getSession.getUserName()
                pre += "";
            }
        }

        return pre;
    }


    /**
     *
     * @param request
     * @return
     */
    public static String getRemoteIP(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");

        if(isBlank(ip) || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("Proxy-Client-IP");
        if(isBlank(ip) || "unknown".equalsIgnoreCase(ip)) ip = request.getHeader("WL-Proxy-Client-IP");
        if(isBlank(ip) || "unknown".equalsIgnoreCase(ip)) ip = request.getRemoteAddr();

        return ip;
    }


    public static boolean isBlank(String ip) {
        return null==ip || "".equals(ip);
    }
}
