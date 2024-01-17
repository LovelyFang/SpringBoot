package com.tsxy.scheduledtasks.service.impl;

import com.tsxy.scheduledtasks.config.MailProperties;
import com.tsxy.scheduledtasks.entiey.EMailDTO;
import com.tsxy.scheduledtasks.service.ExternalMailService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;

/**
 * @Author Liu_df
 * @Date 2024/1/11 10:23
 */
@Service
public class ExternalMailServiceImpl implements ExternalMailService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private MailProperties mailProperties;

    @Resource
    private JavaMailSender javaMailSender;

    public void sendMimeMessage(EMailDTO mailDTO) {

        if (mailDTO == null) {
            return;
        }

        if (StringUtils.isBlank(mailDTO.getSubject())) {
            mailDTO.setSubject("Default Subject");
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper;
        try {
            // 第二个参数true表示构造一个 multipart message 类型邮件
            // multipart message类型邮件包含多个正文、附件以及内嵌资源，邮件表现形式更加丰富
            messageHelper = new MimeMessageHelper(mimeMessage, true);

            if (StringUtils.isEmpty(mailDTO.getFrom())) {
                messageHelper.setFrom(mailProperties.getFrom());
            }
            if (ArrayUtils.isNotEmpty(mailDTO.getTo())) {
                messageHelper.setTo(mailDTO.getTo());
            }
            if (mailDTO.getCc() != null || mailDTO.getCc().length != 0) {
                messageHelper.setCc(mailDTO.getCc());
            }
            messageHelper.setSubject(mailDTO.getSubject());
//            messageHelper.setText();
//            messageHelper.addAttachment();
//            messageHelper.addInline();

            mimeMessage = messageHelper.getMimeMessage();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(mailDTO.getText(), "text/html;charset=UTF-8");

            // 描述数据关系
            /**
             * message.setText("<b>亚麻跌</b>","utf-8","html"); // 发送邮件正文
               Multipart + BodyPart 对象  添加发送附件
             */
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.setSubType("related");
            mimeMultipart.addBodyPart(mimeBodyPart);

            // 添加邮件附件
            if (ArrayUtils.isNotEmpty(mailDTO.getFilenames())) {
                for (String filename : mailDTO.getFilenames()) {
                    MimeBodyPart attachPart = new MimeBodyPart();
                    try {
                        // 附件形式 attachment
                        attachPart.attachFile(filename);
                        // 行内 inline
//                        attachPart.setDisposition(MimeBodyPart.INLINE);
//                        attachPart.setContentID("<" + contentId + ">");
//                        attachPart.setDataHandler(new DataHandler(dataSource));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mimeMultipart.addBodyPart(attachPart);
                }
            }

            mimeMessage.setContent(mimeMultipart);
            mimeMessage.saveChanges();
            logger.info("send mail [{}] to {} success!", mailDTO.getSubject(), StringUtils.join(mailDTO.getCc(), ","));
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("Send mail failed", e);
        }
        javaMailSender.send(mimeMessage);
    }

    public void sendSimpleMailMessage(EMailDTO mailDTO) {
        if (StringUtils.isEmpty(mailDTO.getFrom())) {
            mailDTO.setFrom(mailProperties.getFrom());
        }
        javaMailSender.send(mailDTO);
    }


}
