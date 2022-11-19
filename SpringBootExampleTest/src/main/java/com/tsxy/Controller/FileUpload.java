package com.tsxy.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author Liu_df
 * @Date 2022/11/15 0:16
 */

@RestController
@RequestMapping("/file")
public class FileUpload {

    @GetMapping("/downloadFile")
    public void updateUploadStatus() throws IOException {
        String fileName = "index.pdf";//文件名带格式
        String filePath = "D:\\Workspace\\JavaWorkSpace\\";
        String utlFilepath = "http://219.159.78.28:13024/temphisBT/202208/20220815000194/report-v4/20220815000194v4.pdf";
        URL url = new URL(utlFilepath);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(con.getRequestMethod());
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("User-Agent", "Mozilla/4.76");
        con.setRequestProperty("connection", "keep-alive");
        con.setDoOutput(true);
        BufferedInputStream out = new BufferedInputStream(con.getInputStream());
        File file = new File(filePath+fileName);
        file.createNewFile();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath+fileName));
        try {
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = out.read(bufferOut)) != -1) {
                bos.write(bufferOut, 0, bytes);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            out.close();
            bos.close();
        }
    }
}
