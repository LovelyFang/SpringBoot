package com.tsxy.service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 文件服务 一些和文件相关的
 * @Author Liu_df
 * @Date 2024/3/12 14:50
 */
public interface FileService {

    void generatePDFFile(HttpServletResponse response);


    void generateMedicalRecordPdfFile(Map<String, Object> params, HttpServletResponse httpServletResponse) throws Exception;
}
