package com.pinyougou.user.controller;

import com.pinyougou.service.UserService;
import org.apache.commons.io.FilenameUtils;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UploadController {
    /*注入文件服务的URL */
    @Value("${fileServerUrl}")
    private String fileServerUrl;
    /*上传文件*/
    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile multipartFile){
        Map<String,Object> headPicUrl = new HashMap<>();
        headPicUrl.put("status", 500);
        try{
            /* 获取文件对应的字节数组*/
            byte[] bytes = multipartFile.getBytes();
            /*获取原文件名*/
            String fileName = multipartFile.getOriginalFilename();

            /*上传文件到fastDFS服务器*/
            String path = this.getClass().getResource("/fastdfs-client.conf").getPath();
            ClientGlobal.init(path);
            StorageClient storageClient = new StorageClient();
            String[] arr = storageClient.upload_file(bytes, FilenameUtils.getExtension(fileName), null);
            StringBuilder url = new StringBuilder(fileServerUrl);
            /*拼接文件url*/
            for (String str : arr) {
                url.append("/" + str);
            }
            headPicUrl.put("status", 200);
            headPicUrl.put("url", url.toString());

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return headPicUrl;
    }
}
