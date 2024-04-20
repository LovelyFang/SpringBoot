package com.tsxy.utils;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @Author Liu_df
 * @Date 2024/3/7 14:19
 */
@Component
public class FileUpload {

    private static final Logger logger = LoggerFactory.getLogger(FileUpload.class);

    public static void readFile(String fileName, byte[] bytes){
        fileName = "D:\\" + fileName;
        File file = new File(fileName);
        if (file.exists()) {
            logger.info("文件已存在,直接读取本地文件.");
            return;
        }
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bos = null;
        try {
            inputStream = new ByteArrayInputStream(bytes);
            bufferedInputStream = new BufferedInputStream(inputStream);
            File downloadFile = new File(fileName);
            if (downloadFile.createNewFile()) {
                bos = new BufferedOutputStream(Files.newOutputStream(Paths.get(fileName)));
                int bytesInt;
                byte[] bufferOut = new byte[1024];
                while ((bytesInt = bufferedInputStream.read(bufferOut)) != -1) {
                    bos.write(bufferOut, 0, bytesInt);
                }
            } else {
                logger.info("文件已存在.");
            }
        } catch (Exception e) {
            logger.info("下载文件失败!", e);
        } finally {
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                logger.debug("关闭 BufferedOutputStream 流失败!", e);
            }
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
            } catch (IOException e) {
                logger.debug("关闭 BufferedInputStream 流失败!");
            }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.debug("关闭 InputStream 流失败!", e);
            }
        }
    }

    public static void writeFile(String fileName){

        fileName = "D:\\" + fileName;
        Path filePath = Paths.get(fileName);
        try {
            byte[] bytes = Files.readAllBytes(filePath);
            logger.info("读成byte====>{}", new String(bytes));
            byte[] base64 = Base64.getEncoder().encode(bytes);
            logger.info("转成base64====>{}", new String(base64));
            Path base64path = Paths.get("D:\\useBase64.png");
            Files.write(base64path, base64);
        } catch (IOException e) {
            logger.info("文件读取失败.", e);
        }


    }

}
