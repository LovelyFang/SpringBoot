package com.tsxy;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Liu_df
 * @Date 2023/3/29 15:24
 */
public class StringMethodTest {


    @Test
    public void testPattern() {

        String checkStatus = "6-未采样";
        checkStatus = checkStatus.replaceAll("[^\\u4e00-\\u9fa5]", ""); // 去掉非汉字
        System.out.println(checkStatus);

        Pattern p = Pattern.compile("[^\\u4e00-\\u9fa5]");
        Matcher m = p.matcher(checkStatus);
        if (m.find()) {
            String group = m.group();
//            System.out.println(group);
        }

        String statusNum = String.valueOf(checkStatus.charAt(0));
        if (!StringUtils.equalsAny(statusNum, "6", "7")){
            System.out.println("不是 6 和 7 的");
        }

//        char c = checkStatus.charAt(0);
//        String s = String.valueOf(c);
//        System.out.println(c);
    }


    /**
     * 浅试 split
     */
    @Test
    public void testSplitList(){
        String responseResult = "45060122^0185821908^07892e^https://www.chinaebill.cn/eips-wxapp-service/d?t=504&a=31PZV5IKDl&d=45060122_0185821908_07892e_236.21_20230306&s=1FE6A108DE^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^http://172.28.7.61:18001/medical-web/industryMain.do?method=displayH5&ciphertext=b6acb92dbfbf704970c55d94c7e913511a8bb4248316aad92853d16397c5d5e1c0c7ffa3c00e7d56026f4c2fc9e36fc8ff7cab8cb9d273631a2082a42c796ab31e3656c667e5a4191e3cce0bf9041204399dfc91e2c7cf3d19752c63d9c79f6398b94a95bf8ddf7cd5d630778db0f76bd2834d1b74efcb3f075c8b4ae7e107203d94afbe102b424c502f2bed36cbbe161a7a5bb5a48c8e9bacafb4c2e38ed4308c8001a242a57f995f712273d75154fd9da8a59cb375c15778a953a014b32842d06a90f372565ae1fb73499d5e81ecaf7f872a5e498061fe1e8a4600e96628b08aac7b8eb727a030d458bdebf0187bc9d6d6b1e81e252148fd9caa6f33e2fec9dd1694be129c0738792b9fde9959d92dad46ed2130d3d7c74c55aa4e7290651c461e94bb717f8224bb2e746178b03a609b179c5d460daaadd7a76cfc401b2c52^https://www.chinaebill.cn/h5/4WwD_xhL1ayVBTm_31PZV5IKDl_587F4EA2E3^1118743^I^";
        String[] split = responseResult.split("\\^");
        System.out.println(split[0]);   // https://www.chinaebill.cn/h5/4WwD_xhL1ayVBTm_31PZV5IKDl_587F4EA2E3
    }

    /**
     * 浅试 substring
     */
    @Test
    public void testSubstring(){
        String substring = "yyyyMMddHHmmss".substring(8, 12);
        System.out.println(substring);
    }

}
