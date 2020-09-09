package com.li.blog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author li
 * @version 1.0
 * @since 18-9-11 下午4:15
 **/
@Controller
public class FileController {
    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    private static final String SERVER_ADDRESS = "www.lzlbog.club";
    private static final String RESOURCE_PATH = "static/upload/images";
    private static final String RESOURCE_URL = "/static/upload/images/";

    @ResponseBody
    @PostMapping("/images/upload")
    public Map<String, Object> imageUpload(@RequestParam(name = "editormd-image-file") MultipartFile file) {
        //构造文件名
        String filename = file.getOriginalFilename();
        //文件后缀名
        assert filename != null;
        String suffix = filename.substring(filename.lastIndexOf("."));
        filename = System.currentTimeMillis() + "_" + (int) new Random().nextDouble() * 1000 + suffix;
        //获取上传文件路径
        String path = new ClassPathResource(RESOURCE_PATH).getPath();
        Map<String, Object> map = new HashMap<>(4);
        //创建文件目录
        File toSave = new File(path);
        if (!toSave.isDirectory() && toSave.isFile()) {
            boolean mkdirs = toSave.mkdirs();
            if (mkdirs) {
                toSave = new File(path, filename);
            } else {
                throw new RuntimeException("创建目录失败！");
            }
        } else {
            toSave = new File(path, filename);
        }
        //保存到服务器
        try {
            file.transferTo(toSave);
            map.put("success", 1);
            map.put("message", "上传成功！");
            map.put("url", "http://" + SERVER_ADDRESS + RESOURCE_URL + filename);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.toString());

            map.put("success", 0);
            map.put("message", "上传失败！");
            return map;
        }

    }

}
