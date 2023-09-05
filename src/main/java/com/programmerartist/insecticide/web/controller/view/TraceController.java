package com.programmerartist.insecticide.web.controller.view;

import com.alibaba.fastjson.JSON;
import com.programmerartist.insecticide.web.controller.InsectUtill;
import com.programmerartist.insecticide.web.controller.tunnel.Container;
import com.programmerartist.insecticide.web.controller.tunnel.domain.LRULinkedHashMap;
import com.programmerartist.insecticide.web.controller.tunnel.domain.Report;
import com.programmerartist.insecticide.web.controller.tunnel.domain.TraceIdBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 链路查看
 *
 * Created by 程序员Artist on 16/3/28.
 */
@Controller
@RequestMapping("/trace")
public class TraceController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraceController.class);

    /**
     * 链路首页:
     *
     * tName2Did
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request, Model model)  {

        Map<String, Set<String>> tName2Did = new HashMap<>();

        Map<String, Map<String, LRULinkedHashMap<String, TraceIdBean>>> tName2Did2Tid2Beans = Container.Server.tName2Did2Tid2Beans;
        if(null!=tName2Did2Tid2Beans && tName2Did2Tid2Beans.size()>0) {
            Set<String> tNames = tName2Did2Tid2Beans.keySet();
            for(String tName: tNames){
                tName2Did.put(tName, tName2Did2Tid2Beans.get(tName).keySet());
            }
        }

        model.addAttribute("tName2Did", tName2Did);

        return "traceindex";
    }


    /**
     * did的链路id列表:
     *
     * tids
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("tidsByDid")
    public String tidsByDid(HttpServletRequest request, Model model)  {
        String traceName = request.getParameter("traceName");
        String did       = request.getParameter("did");
        if(null==traceName || traceName.length()==0 || null==did || did.length()==0) {
            throw new RuntimeException("BlankArgument: traceName, did");
        }

        LinkedHashMap<String, String> tid2TimeTids = new LinkedHashMap<>();

        Map<String, Map<String, LRULinkedHashMap<String, TraceIdBean>>> tName2Did2Tid2Beans = Container.Server.tName2Did2Tid2Beans;
        if(null!=tName2Did2Tid2Beans && tName2Did2Tid2Beans.containsKey(traceName)
                && tName2Did2Tid2Beans.get(traceName).containsKey(did)) {

            // 保护LRU不受查询的影响:比如get()方法
            LinkedHashMap<String, TraceIdBean> lruBak = new LinkedHashMap<>(tName2Did2Tid2Beans.get(traceName).get(did));

            // 反转下链表,让最近的显示在浏览器的最上面
            List<String> tidList = new ArrayList<>(lruBak.keySet());
            Collections.reverse(tidList);

            // 调整下样式,在tid前面加上显示一个时间
            for(String tid : tidList) {
                tid2TimeTids.put(tid, format(new Date(Long.parseLong(tid.substring(0, tid.length()-4))), "yyyy-MM-dd HH:mm:ss")
                        + " @ " + tid
                        + " @ " + lruBak.get(tid).getEnterMethod());
            }
        }

        model.addAttribute("traceName", traceName);
        model.addAttribute("did", did);
        model.addAttribute("didAlias", Container.Server.tName2Config.get(traceName).getDidToAlias().get(did));
        model.addAttribute("tid2TimeTids", tid2TimeTids);

        return "tids";
    }


    /**
     * 一个完整的链路: 点击某一个tid
     *
     * TraceIdBean
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "traceBean", produces = "text/json;charset=UTF-8")
    public String traceBean(HttpServletRequest request)  {
        String traceName = request.getParameter("traceName");
        String did       = request.getParameter("did");
        String tid       = request.getParameter("tid");
        if(null==traceName || traceName.length()==0
                || null==did || did.length()==0
                || null==tid || tid.length()==0) {
            throw new RuntimeException("BlankArgument: traceName, did, tid");
        }

        Map<String, Object> jsonMap = new LinkedHashMap<>();
        TraceIdBean traceBean = new TraceIdBean();

        Map<String, Map<String, LRULinkedHashMap<String, TraceIdBean>>> tName2Did2Tid2Beans = Container.Server.tName2Did2Tid2Beans;
        if(null!=tName2Did2Tid2Beans && tName2Did2Tid2Beans.containsKey(traceName)
                && tName2Did2Tid2Beans.get(traceName).containsKey(did) && tName2Did2Tid2Beans.get(traceName).get(did).containsKey(tid)) {

            traceBean = tName2Did2Tid2Beans.get(traceName).get(did).get(tid);

            // 调整样式以方便页面展示查看
            jsonMap.put("tid", traceBean.getTid());
            if(null!=traceBean.getReports() && traceBean.getReports().size()>0) {
                int totalCost = 0;
                for(Report report : traceBean.getReports()) totalCost += report.getCost();
                jsonMap.put("totalCost", totalCost + " ms");

                LinkedHashMap<String, Report> method2Report = new LinkedHashMap<>();
                for(Report report : traceBean.getReports()) method2Report.put(report.getMethod(), report);
                jsonMap.put("reports", method2Report);
            }
        }

        LOGGER.debug(InsectUtill.logPre(request) + "TraceController.traceBean() been invoked: traceName={}, did={}", traceName, did);
        String jsonResult = JSON.toJSONString(jsonMap);

        LOGGER.info(InsectUtill.logPre(request) + "TraceController.traceBean() been invoked: jsonResult={}", jsonResult);

        return jsonResult;
    }


    private String format(Date date, String format) {
        if(null == date) return null;
        return new SimpleDateFormat(format).format(date);
    }

}
