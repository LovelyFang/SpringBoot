package com.tsxy.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gzhc365.component.utils.common.DateTool;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @Author Liu_df
 * @Date 2022/12/21 15:26
 */
public class FangUtils {

    public static JSONObject xml2Json(String xmlStr) throws IOException, JDOMException {
        if (StringUtils.isEmpty(xmlStr)) {
            return null;
        }
        xmlStr = xmlStr.replaceAll("\\\n", "");
        byte[] xml = xmlStr.getBytes("UTF-8");
        JSONObject json = new JSONObject();
        InputStream is = new ByteArrayInputStream(xml);
        SAXBuilder sb = new SAXBuilder();
        Document doc = sb.build(is);
        Element root = doc.getRootElement();
        json.put(root.getName(), iterateElement(root));

        return json;
    }

    private static JSONObject iterateElement(Element element) {
        List<Element> node = element.getChildren();
        JSONObject obj = new JSONObject();
        List list = null;
        for (Element child : node) {
            list = new LinkedList();
            String text = child.getTextTrim();
            if (StringUtils.isBlank(text)) {
                if (child.getChildren().size() == 0) {
                    continue;
                }
                if (obj.containsKey(child.getName())) {
                    list = (List) obj.get(child.getName());
                }
                list.add(iterateElement(child)); //遍历child的子节点
                obj.put(child.getName(), list);
            } else {
                if (obj.containsKey(child.getName())) {
                    Object value = obj.get(child.getName());
                    if(value instanceof  List){
                        list = (List) value;
                    }else {
                        list.add(value);
                    }
                }
                if (child.getChildren().size() == 0) { //child无子节点时直接设置text
                    obj.put(child.getName(), text);
                } else {
                    list.add(text);
                    obj.put(child.getName(), list);
                }
            }
        }
        return obj;
    }

    public static JSONObject xml2JsonOther(String xmlStr, String... arrParams) {
        org.dom4j.Document doc;
        try {
            List<String> arrParamsList = Arrays.asList(arrParams);
            doc = DocumentHelper.parseText(xmlStr);
            JSONObject json = new JSONObject();
            dom4j2Json(doc.getRootElement(), json, arrParamsList);
            return json;
        } catch (DocumentException e) {
            return null;
        }
    }

    public static void dom4j2Json(org.dom4j.Element element, JSONObject json, List<String> arrParamsList) {
        // 如果是属性
        for(Object o:element.attributes()){
            Attribute attr=(Attribute)o;
            json.put("@"+attr.getName(), attr.getValue());
        }
        List<org.dom4j.Element> chdEl = element.elements();
        if(chdEl.isEmpty()){    // 第一个节点,没子元素
            json.put(element.getName(), element.getText());
        }
        for(org.dom4j.Element e : chdEl){   // 有子元素
            if(!e.elements().isEmpty()){    // 子元素也有子元素
                JSONObject chdjson = new JSONObject();
                dom4j2Json(e, chdjson, arrParamsList);
                Object o = json.get(e.getName());
                if(o != null){
                    JSONArray jsona = null;
                    if(o instanceof JSONObject){    // 如果此元素已存在,则转为jsonArray
                        JSONObject jsono=(JSONObject)o;
                        json.remove(e.getName());
                        jsona=new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if(o instanceof JSONArray){
                        jsona=(JSONArray)o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                }else{
                    if(!chdjson.isEmpty()){
                        String name = e.getName();
                        if (arrParamsList.contains(name)) {
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.add(chdjson);
                            json.put(name, jsonArray);
                        } else {
                            json.put(name, chdjson);
                        }
                    }
                }
            }else{  // 子元素没有子元素
                for(Object o:element.attributes()){ // 属性值
                    Attribute attr=(Attribute)o;
                    json.put("@"+attr.getName(), attr.getValue());
                }
                json.put(e.getName(), e.getText()); // 本身值
            }
        }
    }


//------------------------------------------------------年龄的---------------------------------------------------------------------------------------------------
    private static final int invalidAge = -1; // 非法的年龄，用于处理异常。

    /**
     * 根据身份证号码计算年龄
     *
     * @param idNumber 考虑到了15位身份证，但不一定存在
     */
    public static int getAgeByIDNumber(String idNumber) {
        String dateStr;
        if (idNumber.length() == 15) {
            dateStr = "19" + idNumber.substring(6, 12);
        } else if (idNumber.length() == 18) {
            dateStr = idNumber.substring(6, 14);
        } else {//默认是合法身份证号，但不排除有意外发生
            return invalidAge;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
            Date birthday = simpleDateFormat.parse(dateStr);
            return FangUtils.getAgeByDate(birthday);
        } catch (Exception e) {
            return invalidAge;
        }

    }

    /**
     * 根据生日计算年龄
     *
     * @param dateStr 这样格式的生日 1990-01-01
     */
    public static int getAgeByDateString(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date birthday = simpleDateFormat.parse(dateStr);
            return FangUtils.getAgeByDate(birthday);
        } catch (Exception e) {
            return invalidAge;
        }
    }

    public static int getAgeByDate(Date birthday) {
        Calendar calendar = Calendar.getInstance();

        // calendar.before()有的点bug
//    if (calendar.before(birthday)) {
//      return invalidAge;
//    }
        if (calendar.getTimeInMillis() - birthday.getTime() < 0L) {
            return invalidAge;
        }

        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(birthday);

        int yearBirthday = calendar.get(Calendar.YEAR);
        int monthBirthday = calendar.get(Calendar.MONTH);
        int dayOfMonthBirthday = calendar.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirthday;

        if (monthNow <= monthBirthday) {
            if (monthNow == monthBirthday) {
                if (dayOfMonthNow < dayOfMonthBirthday) {
                    age--;
                }
            } else {
                age--;
            }
        }

        return age;
    }

    public static String getAgeByBirth(String date) {
        Integer age = 0;
        try {
            Date birthday = DateTool.getFullDate().parse(date);
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (0 == age){
                    return "";
                }
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return  0 == age ? "" : age+"";
        } catch (Exception e) {//兼容性更强,异常后返回数据
            return 0+"";
        }
    }


//------------------------------------------------------数学运算的---------------------------------------------------------------------------------------------------


    public static int coverMathFen(String money) {
        if (StringUtils.isBlank(money)) {
            return 0;
        }
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(money));
        return amount.multiply(BigDecimal.valueOf(100)).intValue();
    }

    private static final String x = "^[1-9]\\d*$";
    //判断整数（int）
    public static Boolean isInteger(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        try {
            Pattern pattern = Pattern.compile(x);
            return pattern.matcher(str).matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean isDouble(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }



}
