package com.programmerartist.insecticide.web.controller.view;

import com.programmerartist.insecticide.web.controller.InsectUtill;
import com.programmerartist.insecticide.web.controller.persistence.file.ConfigPersistFile;
import com.programmerartist.insecticide.web.controller.tunnel.Container;
import com.programmerartist.insecticide.web.controller.tunnel.domain.LRULinkedHashMap;
import com.programmerartist.insecticide.web.controller.tunnel.domain.TraceConfig;
import com.programmerartist.insecticide.web.controller.tunnel.domain.TraceIdBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 管理控制台
 *
 * Created by 程序员Artist on 16/3/28.
 *
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    /**
     * 重启的时候,可能需要从持久化里恢复配置数据
     *
     */
    static {
        try {
            ConfigPersistFile.getInstance().reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * login
     *
     * @param request
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping("login")
    public String login(HttpServletRequest request, Model model)  {

        String user     = request.getParameter("user");
        String password = request.getParameter("password");
        if(null==user || user.length()==0 || null==password || password.length()==0) {
            throw new RuntimeException("BlankArgument: user, password");
        }


        if(Purview.SUPPER.containsKey(user) && Purview.SUPPER.get(user).equals(password)) {
            request.getSession().setAttribute(Purview.SESSION_USER, user);
            return "login success";
        }else {
            return "login failed: you are not admin";
        }
    }


    /**
     * 所有配置
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("systemindex")
    public String systemindex(HttpServletRequest request, Model model)  {

        model.addAttribute("traceConfigs", Container.Server.tName2Config.values());

        return "index";
    }

    /**
     * 所有配置
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("index")
    public String index(HttpServletRequest request, Model model)  {

        model.addAttribute("traceConfigs", Container.Server.tName2Config.values());

        return "indexconfig";
    }

    /**
     * 点击 修改配置 按钮
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("preUConfig")
    public String preUConfig(HttpServletRequest request, Model model)  {
        if(!Purview.isAdmin(request)) {
            model.addAttribute("sorry", Purview.Sorry.NOT_ADMIN.getValue());
            model.addAttribute("traceConfigs", Container.Server.tName2Config.values());

            return "indexconfig";
        }

        String traceName    = request.getParameter("traceName");
        if(null==traceName || traceName.length()==0) {
            throw new RuntimeException("BlankArgument: traceName");
        }

        model.addAttribute("traceConfigs", Container.Server.tName2Config.values());
        model.addAttribute("selfConfig", Container.Server.tName2Config.get(traceName));

        LOGGER.debug(InsectUtill.logPre(request) + "AdminController.preUConfig() been invoked: traceName={}, config={}", traceName, Container.Server.tName2Config.get(traceName).toString());

        return "indexconfig";
    }

    /**
     * 修改配置
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("uConfig")
    public String uConfig(HttpServletRequest request, Model model) throws Exception {
        if(!Purview.isAdmin(request)) {
            model.addAttribute("sorry", Purview.Sorry.NOT_ADMIN.getValue());
            model.addAttribute("traceConfigs", Container.Server.tName2Config.values());

            return "indexconfig";
        }

        String traceName    = request.getParameter("traceName");
        String reportNumStr = request.getParameter("reportNum");
        String stopStr      = request.getParameter("stop");

        if(null==traceName || traceName.length()==0
                || null==reportNumStr || reportNumStr.length()==0
                || null==stopStr || stopStr.length()==0) {
            throw new RuntimeException("BlankArgument: traceName, reportNum, stop");
        }

        Boolean stop = Boolean.parseBoolean(stopStr);
        Integer reportNum = Integer.parseInt(reportNumStr);

        if(Container.Server.tName2Config.containsKey(traceName)) {
            // 修改了上报数,需要更新LRU map
            this.changeReportNum(request, traceName, reportNum);

            TraceConfig config = Container.Server.tName2Config.get(traceName);
            config.setReportNum(reportNum);
            config.setStop(stop);

            LOGGER.debug(InsectUtill.logPre(request) + "AdminController.uConfig() been invoked: traceName={}, reportNumStr={}, stopStr={}", traceName, reportNumStr, stopStr);
        }

        model.addAttribute("traceConfigs", Container.Server.tName2Config.values());

        // 更新持久化
        ConfigPersistFile.getInstance().update(Container.Server.tName2Config);

        return "indexconfig";
    }


    /**
     * 查看配置
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("qConfig")
    public String qConfig(HttpServletRequest request, Model model)  {
        String traceName = request.getParameter("traceName");
        if(null==traceName || traceName.length()==0) {
            throw new RuntimeException("BlankArgument: traceName");
        }

        if(Container.Server.tName2Config.containsKey(traceName)) {
            model.addAttribute("traceConfig", Container.Server.tName2Config.get(traceName));
        }

        return "showconfig";
    }


    /**
     * 查询traceName的dids列表
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("showDid")
    public String showDid(HttpServletRequest request, Model model)  {
        String traceName = request.getParameter("traceName");
        if(null==traceName || traceName.length()==0) {
            throw new RuntimeException("BlankArgument: traceName");
        }

        LinkedHashMap<String, String> didToAlias = new LinkedHashMap<>();
        if(Container.Server.tName2Config.containsKey(traceName)) {
            TraceConfig config = Container.Server.tName2Config.get(traceName);
            if(null!=config.getDid() && config.getDid().size()>0) {
                didToAlias = config.getDidToAlias();
            }
        }

        this.toDids(model, traceName, didToAlias);

        return "dids";
    }


    /**
     * 新增did
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "addDid", method = RequestMethod.POST)
    public String addDid(HttpServletRequest request, Model model) throws Exception {
        String traceName = request.getParameter("traceName");
        String did       = request.getParameter("did");
        String didAlias  = request.getParameter("didAlias");
        if(null == didAlias) didAlias = "";

        if(null==traceName || traceName.length()==0 || null==did || did.length()==0) {
            throw new RuntimeException("BlankArgument: traceName, did");
        }

        LinkedHashMap<String, String> didToAlias = new LinkedHashMap<>();
        if(Container.Server.tName2Config.containsKey(traceName)) {
            TraceConfig config = Container.Server.tName2Config.get(traceName);
            if(null==config.getDid() || config.getDid().size()==0) {
                config.setDid(new ArrayList<String>());
                config.setDidToAlias(new LinkedHashMap<String, String>());
            }
            didToAlias = config.getDidToAlias();

            if(!config.getDid().contains(did)) {
                config.getDid().add(did);
                config.getDidToAlias().put(did, didAlias);
            }
        }

        this.toDids(model, traceName, didToAlias);

        // 更新持久化
        ConfigPersistFile.getInstance().save(Container.Server.tName2Config);

        LOGGER.debug(InsectUtill.logPre(request) + "AdminController.addDid() been invoked: traceName={}, did={}, didAlias={}", traceName, did, didAlias);

        return "dids";
    }

    /**
     * 移除did
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("removeDid")
    public String removeDid(HttpServletRequest request, Model model) throws Exception {
        String traceName = request.getParameter("traceName");
        String did       = request.getParameter("did");
        if(null==traceName || traceName.length()==0 || null==did || did.length()==0) {
            throw new RuntimeException("BlankArgument: traceName, did");
        }

        LinkedHashMap<String, String> didToAlias = new LinkedHashMap<>();
        if(Container.Server.tName2Config.containsKey(traceName)) {
            TraceConfig config = Container.Server.tName2Config.get(traceName);
            if(!Purview.isAdmin(request)) {
                model.addAttribute("sorry", Purview.Sorry.NOT_ADMIN.getValue());
                this.toDids(model, traceName, config.getDidToAlias());

                return "dids";
            }

            if(null!=config.getDid() && config.getDid().size()>0 && config.getDid().contains(did)) {
                config.getDid().remove(did);
                config.getDidToAlias().remove(did);

                didToAlias = config.getDidToAlias();
            }
        }

        this.toDids(model, traceName, didToAlias);

        // 更新持久化
        ConfigPersistFile.getInstance().delete(Container.Server.tName2Config);

        LOGGER.debug(InsectUtill.logPre(request) + "AdminController.removeDid() been invoked: traceName={}, did={}", traceName, did);

        return "dids";
    }



//===============================================TOOL====================================================

    /**
     *
     * @param model
     * @param traceName
     * @param didToAlias
     */
    private void toDids(Model model, String traceName, LinkedHashMap<String, String> didToAlias) {

        model.addAttribute("traceName", traceName);
        model.addAttribute("didToAlias", didToAlias);
    }

    /**
     *
     * @param traceName
     * @param newReportNum
     */
    private void changeReportNum(HttpServletRequest request, String traceName, int newReportNum) {

        if(newReportNum != Container.Server.tName2Config.get(traceName).getReportNum()) {
            LOGGER.debug(InsectUtill.logPre(request) + "AdminController.changeReportNum() been invoked: traceName={}, oldReportNum={}, newReportNum={}", traceName, Container.Server.tName2Config.get(traceName).getReportNum(), newReportNum);

            Map<String, LRULinkedHashMap<String, TraceIdBean>> did2Tid2Beans = Container.Server.tName2Did2Tid2Beans.get(traceName);
            if(null==did2Tid2Beans || did2Tid2Beans.size()==0) return ;

            // 全部替换LRU map
            Set<String> dids = did2Tid2Beans.keySet();
            for(String did : dids) {
                LRULinkedHashMap<String, TraceIdBean> tid2Beans = new LRULinkedHashMap<>(newReportNum);
                tid2Beans.putAll(did2Tid2Beans.get(did));

                did2Tid2Beans.put(did, tid2Beans);
            }
        }
    }


}
