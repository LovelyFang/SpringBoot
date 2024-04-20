package com.tsxy.controller;

import com.alibaba.fastjson.JSONObject;
import com.tsxy.service.FileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @Author Liu_df
 * @Date 2022/11/15 0:16
 */

@RestController
@RequestMapping("/file")
public class FileUpload {

    @Resource
    private FileService fileService;

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

    @RequestMapping("/generateMedicalRecordPdfFile")
    public void generateMedicalRecordPdfFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> params = this.getJSON(request);
        fileService.generateMedicalRecordPdfFile(params, response);
    }

    public Map<String, Object> getJSON(HttpServletRequest request) throws Exception {
        BufferedReader streamReader = new BufferedReader( new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder responseStrBuilder = new StringBuilder();
        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseStrBuilder.append(inputStr);
        }
        return JSONObject.parseObject(responseStrBuilder.toString(), Map.class);
    }

    @GetMapping("/generatePDFFile/{fileName}")
    public void generatePDFFile(@PathVariable String fileName, HttpServletRequest request, HttpServletResponse response) {

        response.setContentType("application/pdf;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
            fileService.generatePDFFile(response);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }



    }
}
