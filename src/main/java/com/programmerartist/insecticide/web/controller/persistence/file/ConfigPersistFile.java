package com.programmerartist.insecticide.web.controller.persistence.file;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.programmerartist.insecticide.web.controller.InsectUtill;
import com.programmerartist.insecticide.web.controller.persistence.Persist;
import com.programmerartist.insecticide.web.controller.tunnel.Container;
import com.programmerartist.insecticide.web.controller.tunnel.domain.TraceConfig;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * Created by 程序员Artist on 16/3/28.
 */
public class ConfigPersistFile implements Persist<Map<String, TraceConfig>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigPersistFile.class);

    /**
     *
     */
    private ConfigPersistFile() {}
    /**
     * singleton
     *
     * @return
     */
    public static ConfigPersistFile getInstance(){
        return ConfigPersistFileHolder.INSTANCE;
    }
    /**
     *
     */
    private static class ConfigPersistFileHolder {
        private static final ConfigPersistFile INSTANCE = new ConfigPersistFile();
    }


    /**
     * @param data
     * @throws Exception
     */
    @Override
    public void save(Map<String, TraceConfig> data) throws Exception {
        this.resetFile(data);
    }

    /**
     * @param data
     * @throws Exception
     */
    @Override
    public void update(Map<String, TraceConfig> data) throws Exception {
        this.resetFile(data);
    }

    /**
     * @param data
     * @throws Exception
     */
    @Override
    public void delete(Map<String, TraceConfig> data) throws Exception {
        this.resetFile(data);
    }

    /**
     * @throws Exception
     */
    @Override
    public void reload() throws Exception {

        File file = new File(ConfigPersistFile.getFilePath("tName2Config.ini"));
        if(!file.exists()) return ;

        List<String> lines = FileUtils.readLines(file);
        if(null==lines || lines.size()==0) return ;

        Map<String, TraceConfig> tName2Config = new HashMap<>();
        for(String line: lines) {
            JSONObject json = JSON.parseObject(line);
            TraceConfig config = JSON.parseObject(json.getString("config"), TraceConfig.class);

            tName2Config.put(json.getString("tName"), config);
        }

        Container.Server.tName2Config = tName2Config;

        LOGGER.debug(InsectUtill.logPre(null) + "ConfigPersistFile.reload() been invoked: tName2Config={}", tName2Config.toString());
    }


    /**
     *
     * @param filename
     * @return
     */
    public static String getFilePath(String filename) throws Exception {
        String url   = ConfigPersistFile.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        File jarFile = new File(url);

        String dirPath  = jarFile.getParentFile().getParentFile().getPath() + "/persist/";
        File resultFile = new File(dirPath);
        resultFile.mkdirs();

        return dirPath + filename;
    }

    /**
     *
     * @param tName2Config
     * @throws Exception
     */
    private void resetFile(Map<String, TraceConfig> tName2Config) throws Exception {
        if(null==tName2Config || tName2Config.size()==0) return ;

        String filePath = ConfigPersistFile.getFilePath("tName2Config.ini");
        List<String> lines = new ArrayList<>();
        for(Map.Entry<String, TraceConfig> data : tName2Config.entrySet()) {

            JSONObject line = new JSONObject();
            line.put("tName", data.getKey());
            line.put("config", data.getValue());

            lines.add(line.toJSONString());
        }

        if(null!=lines && lines.size()>0) {
            FileUtils.writeLines(new File(filePath), lines, false);
        }

        LOGGER.debug(InsectUtill.logPre(null) + "ConfigPersistFile.resetFile() been invoked: tName2Config={}", tName2Config.toString());
    }


}
