package com.tsxy.scheduledtasks.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author Liu_df
 * @Date 2023/3/29 16:37
 */

@RestController
@RequestMapping("api/customize")
public class CustomerController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @RequestMapping("/index")
    public void index(HttpServletRequest request, HttpServletResponse response){

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(request.getInputStream(), out);
            String s = out.toString(request.getCharacterEncoding());
            logger.info("---------->{}", s);
        } catch (IOException var3) {
            logger.error(var3.getMessage(), var3);
        }

        String sign = request.getParameter("sign");
        String a = request.getParameter("a");
        String b = request.getParameter("b");
        String c = request.getParameter("c");
        logger.info("sign:{},a:{},b:{},c:{}",sign,a,b,c);
        this.sendSuccessData(request,response,"成功");
    }


}
