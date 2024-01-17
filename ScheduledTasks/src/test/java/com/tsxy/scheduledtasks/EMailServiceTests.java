package com.tsxy.scheduledtasks;

import com.tsxy.scheduledtasks.entiey.EMailDTO;
import com.tsxy.scheduledtasks.service.impl.ExternalMailServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;

/**
 * @Author Liu_df
 * @Date 2024/1/11 11:01
 */
@SpringBootTest
public class EMailServiceTests {

    /**
     * 接收人
     */
    public static final String[] TO = new String[] { "liu_df@haici.com"};

    /**
     * 抄送人
     */
    public static final String[] CC = new String[] { "" };

    /**
     * 主题
     */
    public static final String SUBJECT = "Test Email";


    @Autowired
    private ExternalMailServiceImpl mailService;

    @Test
    public void sendMimeMessage() {
        EMailDTO mailDTO = new EMailDTO();

        String text = "<html><body><h3 style='color:blue'>This is a mime message email.</h3></body></html>";
        mailDTO.setTo(TO);
        mailDTO.setCc(CC);
        mailDTO.setSubject(SUBJECT);
        mailDTO.setText(text);
        Resource resource = new ClassPathResource("moon.png");
        Resource resource2 = new ClassPathResource("Iron-man.png");
        try {
            String[] filenames = new String[] { resource.getFile().getAbsolutePath(),
                    resource2.getFile().getAbsolutePath() };
            mailDTO.setFilenames(filenames);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mailService.sendMimeMessage(mailDTO);
    }


    @Test
    public void sendSimpleMailMessage() {
        EMailDTO mailDTO = new EMailDTO();
        mailDTO.setTo(TO);
//        mailDTO.setCc(CC);
        mailDTO.setSubject(SUBJECT);
        mailDTO.setText("This is a simple message email.");
        mailService.sendSimpleMailMessage(mailDTO);

    }






}
