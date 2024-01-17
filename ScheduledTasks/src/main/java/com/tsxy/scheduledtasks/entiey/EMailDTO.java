package com.tsxy.scheduledtasks.entiey;

import org.springframework.mail.SimpleMailMessage;

/**
 * @Author Liu_df
 * @Date 2024/1/11 10:28
 */
public class EMailDTO extends SimpleMailMessage {

    private static final long serialVersionUID = -6657438178977846197L;

    private String[] filenames;

    public String[] getFilenames() {
        return filenames;
    }

    public void setFilenames(String[] filenames) {
        this.filenames = filenames;
    }
}
