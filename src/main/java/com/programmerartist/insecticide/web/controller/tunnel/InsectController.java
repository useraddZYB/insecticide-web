package com.programmerartist.insecticide.web.controller.tunnel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.programmerartist.insecticide.web.controller.InsectUtill;
import com.programmerartist.insecticide.web.controller.tunnel.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 隧道
 *
 * Created by 程序员Artist on 16/3/28.
 *
 */
@Controller
@RequestMapping("/insecticide")
public class InsectController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsectController.class);

    /**
     * 客户端初始化
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("init")
    public String init(HttpServletRequest request)  {
        String traceName = request.getParameter(ParamTunnel.Init.TRACE_NAME);
        String stopStr   = request.getParameter(ParamTunnel.Init.STOP);
        if(null==traceName || traceName.length()==0) {
            throw new RuntimeException("BlankArgument: traceName");
        }

        Boolean stop = Boolean.parseBoolean(stopStr);

        // 存在,就更新; 不存在就新建
        if(Container.Server.tName2Config.containsKey(traceName)) {
            Container.Server.tName2Config.get(traceName).setStop(stop);
        }else {
            Container.Server.tName2Config.put(traceName, new TraceConfig(traceName, new ArrayList<String>(), stop, new LinkedHashMap<String, String>()));
        }

        LOGGER.debug(InsectUtill.logPre(request) + "InsectController.init() been invoked: traceName={}, stopStr={}", traceName, stopStr);

        return "";
    }


    /**
     * 心跳
     *
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("heartbeats")
    public void heartbeats(HttpServletRequest request, HttpServletResponse response)  {
        String traceName = request.getParameter(ParamTunnel.HeartBeats.TRACE_NAME);
        if(null==traceName || traceName.length()==0) {
            throw new RuntimeException("BlankArgument: traceName");
        }

        ResponseBean<TraceConfig> resultBean = new ResponseBean<>(ResponseBean.CodeEnum.NOT_FIND.getValue(), null);
        if(Container.Server.tName2Config.containsKey(traceName)) {
            resultBean = new ResponseBean<>(ResponseBean.CodeEnum.OK.getValue(), Container.Server.tName2Config.get(traceName));
        }

        try {
            response.getWriter().write(resultBean.toJson());

            LOGGER.debug(InsectUtill.logPre(request) + "InsectController.heartbeats() been invoked: traceName={}, resultBean={}", traceName, resultBean.toJson());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(InsectUtill.logPre(request) + "InsectController.heartbeats() error: ", e);
        }

        return ;
    }

    /**
     * 上报报告
     *
     * @param reportJson
     * @return
     */
    @ResponseBody
    @RequestMapping(value="report", method = RequestMethod.POST)
    public String report(HttpServletRequest request, @RequestBody String reportJson)  {

        try {
            LOGGER.debug(InsectUtill.logPre(request) + "InsectController.report() been invoked: reportJson={}", reportJson);
            if(null==reportJson || reportJson.length()==0) {
                throw new RuntimeException("BlankArgument: reportJson");
            }

            JSONObject jsonObject = JSON.parseObject(reportJson);
            Report reportBean = JSONObject.parseObject(jsonObject.get(ParamTunnel.ReportParam.DOMAIN).toString(), Report.class);
            LOGGER.debug(InsectUtill.logPre(request) + "InsectController.report() been invoked: reportBean={}", reportBean);

            String traceName = reportBean.getTraceName();
            String did       = reportBean.getDid();
            String tid       = reportBean.getTraceId();
            if(null!=tid && tid.length()>0) {
                tid = tid.split("_")[0];
            }

            int reportNum = 0;
            if(Container.Server.tName2Config.containsKey(traceName)) {
                reportNum = Container.Server.tName2Config.get(traceName).getReportNum();
            }

            Map<String, Map<String, LRULinkedHashMap<String, TraceIdBean>>> tName2Did2Tid2Beans = Container.Server.tName2Did2Tid2Beans;
            if(!tName2Did2Tid2Beans.containsKey(traceName)) tName2Did2Tid2Beans.put(traceName, new HashMap<String, LRULinkedHashMap<String, TraceIdBean>>());
            if(!tName2Did2Tid2Beans.get(traceName).containsKey(did)) tName2Did2Tid2Beans.get(traceName).put(did, new LRULinkedHashMap<String, TraceIdBean>(reportNum));

            LRULinkedHashMap<String, TraceIdBean> tid2Bean = tName2Did2Tid2Beans.get(traceName).get(did);

            // 如果是已有到链路的某一个节点的上报,则直接插入
            if(tid2Bean.containsKey(tid)){
                tid2Bean.get(tid).getReports().add(reportBean);  // TreeSet用timestamp自动排序
            }
            // 如果还没有,则可能需要做LRU处理
            else {
                /**
                 LRU 处理:
                      未满: 直接插入到最后
                      溢出: 移除最前面的,顶上最后的
                 */
                tid2Bean.put(tid, new TraceIdBean(tid, reportBean.getMethod(), new TreeSet<Report>()));
                tid2Bean.get(tid).getReports().add(reportBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(InsectUtill.logPre(request) + "InsectController.report() error: ", e);
        }

        return "";
    }




//==========================================TEST=============================================


    /**
     * TEST
     *
     * @param args
     */
    public static void main(String[] args) {

        /*String reportJson = "{\"domain\":{\"cost\":12,\"did\":\"00000001\",\"ip\":\"192.168.1.103\",\"jsonRequ\":\"[{\\\"debug\\\":false,\\\"encode\\\":\\\"UTF8\\\",\\\"evictFromLogAnalysis\\\":false,\\\"exid\\\":0,\\\"forceNoExperiment\\\":false,\\\"forceToExperiment\\\":0,\\\"insectTrace\\\":\\\"14590074967291950_1459007496729\\\",\\\"lastId\\\":1459007486704,\\\"lastWeight\\\":0,\\\"leastEffort\\\":false,\\\"noCache\\\":false,\\\"pageSize\\\":10,\\\"placementId\\\":0,\\\"predictorCacheTime\\\":21600,\\\"refreshCache\\\":false,\\\"refreshTime\\\":0,\\\"target\\\":{\\\"brandId\\\":0,\\\"cityId\\\":1,\\\"did\\\":\\\"00000001\\\",\\\"flag\\\":0,\\\"itemId\\\":0,\\\"keywordsId\\\":0,\\\"newsId\\\":0,\\\"userId\\\":1}}]\",\"jsonResp\":\"{\\\"debugInfo\\\":{\\\"appName\\\":\\\"xx.recommender\\\",\\\"latency\\\":-1,\\\"messageStack\\\":[]},\\\"encode\\\":\\\"UTF8\\\",\\\"end\\\":false,\\\"experimentId\\\":0,\\\"items\\\":[],\\\"lastWeight\\\":0,\\\"requestId\\\":0,\\\"resultCode\\\":\\\"OK\\\"}\",\"method\":\"RecommenderServiceImpl.getRecommendations()\",\"timestamp\":1459007496729,\"traceId\":\"14590074967291950_1459007496729\",\"traceName\":\"dataapp\"}}";

        JSONObject jsonObject = JSON.parseObject(reportJson);
        System.out.println("reportBean1=" + jsonObject.get(ParamTunnel.ReportParam.DOMAIN).toString());
        Report reportBean = JSONObject.parseObject(jsonObject.get(ParamTunnel.ReportParam.DOMAIN).toString(), Report.class);
        System.out.println("reportBean=" + reportBean);

        System.out.println(JSON.toJSONString(reportBean));*/
        /*System.out.println(JSONArray.parseArray(reportBean.getJsonRequ()));

        System.out.println(JSON.parse(reportBean.getJsonRequ()));
        System.out.println(JSON.parse(reportBean.getJsonResp()));*/

        LRULinkedHashMap<String, TraceIdBean> test = new LRULinkedHashMap<>(4);
        test.put("a", new TraceIdBean());
        test.put("b", new TraceIdBean());
        test.put("c", new TraceIdBean());
        test.put("d", new TraceIdBean());

        /*test.get("b").setTid("bbbb");
        test.put("e", new TraceIdBean());
        test.get("c").setTid("bbbb");*/

        /*LinkedHashMap<String, TraceIdBean> copy = new LinkedHashMap<>(test);

        List<String> reverse = new ArrayList<String>(copy.keySet());
        Collections.reverse(reverse);
        System.out.println(reverse);
        for(String key : reverse) {
            copy.get(key);
        }*/
        System.out.println(test.keySet());

        LRULinkedHashMap<String, TraceIdBean> test1 = new LRULinkedHashMap<>(5);
        test1.putAll(test);

        System.out.println(test.keySet());
        System.out.println(test1.keySet());
    }

}
