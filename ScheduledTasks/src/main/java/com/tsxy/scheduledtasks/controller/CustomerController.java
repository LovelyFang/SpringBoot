package com.tsxy.scheduledtasks.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        String sign = request.getParameter("sign");
        String a = request.getParameter("a");
        String b = request.getParameter("b");
        String c = request.getParameter("c");
        logger.info("sign:{},a:{},b:{},c:{}",sign,a,b,c);
        this.sendSuccessData(request,response,"成功");
    }

    @RequestMapping("/scheduler")
    public void scheduler(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/templates/scheduler/index.jsp").forward(request, response);
    }

}
