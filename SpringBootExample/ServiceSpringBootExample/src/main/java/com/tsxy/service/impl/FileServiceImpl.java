package com.tsxy.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.tsxy.service.FileService;
import com.tsxy.utils.PDFUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Author Liu_df
 * @Date 2024/3/12 14:51
 */
@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    private static final String tmpDir = System.getProperty("java.io.tmpdir") + "medicalRecord/";

    static {
        File file = new File(tmpDir);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    @Override
    public void generateMedicalRecordPdfFile(Map<String, Object> params, HttpServletResponse httpServletResponse) throws Exception {

        String patientName = MapUtils.getString(params, "patientName", "");
        String startTime = MapUtils.getString(params, "startTime", "2024-04-01");
        String endTime = MapUtils.getString(params, "endTime", "2024-05-01");
        String admissionNum = MapUtils.getString(params, "admissionNum", "");
        LocalDate startLocalDate = LocalDate.parse(startTime);
        LocalDate endLocalDate = LocalDate.parse(endTime);
        logger.info("------>{}", startLocalDate.until(endLocalDate, ChronoUnit.MONTHS));
        logger.info("------>{}", startLocalDate.until(endLocalDate, ChronoUnit.DAYS));

        Map<String, String> requestHCParam = new HashMap<>();
        requestHCParam.put("platformId", "2482");
        requestHCParam.put("hisId", "2482");
        requestHCParam.put("patientName", patientName);  // 就诊人姓名
        requestHCParam.put("status", "S");   //  订单状态（U：未缴费;S：缴费成功;F：缴费失败;Z：缴费异常;C：已取消;）
        requestHCParam.put("startTime", startTime);  // 开始时间 yyyy-MM-dd
        requestHCParam.put("endTime", endTime);    // 结束时间 yyyy-MM-dd
        requestHCParam.put("admissionNum", admissionNum); // 住院号
        requestHCParam.put("sendType", ""); // 寄取类型 自取：self ，邮寄：mail
        requestHCParam.put("auditStatus", "S");  // 审核状态 U：待审核；S：审核通过；F：审核失败；H：审核失败；C：取消审核
        requestHCParam.put("printStatus", "");  // 打印状态 1：已打印 0：未打印
        requestHCParam.put("timeStamp", String.valueOf(System.currentTimeMillis()));
        String requestHCAddress = "https://ump.med.gzhc365.com/daemon/api/medical/print/list";

        // 构造HttpClient的实例
        HttpClient httpClient = new HttpClient();
        // 创建POST方法的实例
        PostMethod method = new PostMethod(requestHCAddress);
        method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        // 将表单的值放入postMethod中
        List<NameValuePair> nameValuePairs = new ArrayList<>();
        for (String key : requestHCParam.keySet()) {
            String value = requestHCParam.get(key).toString();
            NameValuePair nameValuePair = new NameValuePair(key, value);
            nameValuePairs.add(nameValuePair);
        }
        NameValuePair[] param = nameValuePairs.toArray(new NameValuePair[0]);
        method.addParameters(param);
        // 一些请求配置参数
        HttpClientParams httpClientParams = new HttpClientParams();
        httpClientParams.setSoTimeout(30000);   // 设置读数据超时时间(单位毫秒)
        httpClientParams.setConnectionManagerTimeout(10000); // 设置连接超时时间(单位毫秒)
        httpClientParams.setContentCharset("UTF-8");
        method.setParams(httpClientParams);
        String response = "";
        try {
            // 执行postMethod
            httpClient.executeMethod(method);
            if (method.getStatusCode() == HttpStatus.SC_OK) {
                response = StreamUtils.copyToString(method.getResponseBodyAsStream(), StandardCharsets.UTF_8);
                logger.info("接口返回===>{}", response);
            }
            if (StringUtils.isBlank(response)){
                throw new Exception("请求核心异常");
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            method.releaseConnection();
        }
        JSONObject responseJson = JSONObject.parseObject(response);
        String code = responseJson.getString("code");
        if (!"0".equals(code)) {
            String msg = responseJson.getString("msg");
            throw new Exception(msg);
        }
        JSONObject responseDataJson = responseJson.getJSONObject("data");
        JSONArray recordList = responseDataJson.getJSONArray("recordList");

        if (CollectionUtils.isNotEmpty(recordList)){


//            CompletableFuture<Void> pdfFuture = CompletableFuture.runAsync(() -> {
//                for (Object o : recordList) {
//                    JSONObject recordObj = (JSONObject) o;
//                    try {
//                        this.generatePDFFile(recordObj);
//                    } catch (Exception ignored) {}
//                }
//            }, executor);


            List<Object> frontRecordList = recordList.subList(0, recordList.size() / 2);
            JSONArray frontRecordArr = new JSONArray(frontRecordList);
            List<Object> afterRecordList = recordList.subList(recordList.size() / 2, recordList.size());
            JSONArray afterRecordArr = new JSONArray(afterRecordList);

            CompletableFuture<Void> frontCardFuture = CompletableFuture.runAsync(() -> {
                generateIdentificationCardPicture(frontRecordArr);
            }, executor);

            CompletableFuture<Void> afterCardFuture = CompletableFuture.runAsync(() -> {
                generateIdentificationCardPicture(afterRecordArr);
            }, executor);

            CompletableFuture.allOf(frontCardFuture, afterCardFuture).get(20, TimeUnit.SECONDS);

            for (Object o : recordList) {
                JSONObject recordObj = (JSONObject) o;
                try {
                    this.generatePDFFile(recordObj);
                } catch (Exception ignored) {}
            }


            httpServletResponse.setContentType("application/zip");
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + startTime + "_" + endTime + ".zip\"");

            zipDirectory(tmpDir, httpServletResponse);
        }

    }

    private void addIdentificationCardPicture2PDF(JSONObject recordObj) throws Exception {

        String agtOrdNum = recordObj.getString("agtOrdNum");
        String patientName = recordObj.getString("patientName");
        JSONArray photoUrlArr = recordObj.getJSONArray("photoUrl");
        String filePath = tmpDir + agtOrdNum + ".pdf";
        PdfReader pdfReader = new PdfReader(Files.newInputStream(Paths.get(filePath)));
        PdfStamper pdfStamper = new PdfStamper(pdfReader, Files.newOutputStream(Paths.get(filePath)));

        if (CollectionUtils.isEmpty(photoUrlArr)) {
            return;
        }

        for (Object photoUrl : photoUrlArr) {
            JSONObject photoUrlObj = (JSONObject) photoUrl;
            String photoType = photoUrlObj.getString("photoType");
            switch (photoType) {
                case "1" :
                    photoType = "正面";
                    break;
                case "2" :
                    photoType = "背面";
                    break;
                case "5" :
                    photoType = "委托人正面";
                    break;
                case "6" :
                    photoType = "委托人背面";
                    break;
                case "7" :
                    photoType = "授权委托书";
                    break;
                default:
                    continue;
            }
            String patientInfo = patientName + photoType;
            String imgFilePath = tmpDir + patientInfo + ".jpg";
            logger.info(tmpDir + "------------->------>{}", agtOrdNum);
            Image image = Image.getInstance(imgFilePath);
            image.setAbsolutePosition(1, 1);
            image.setRotation(90);
            int pageSize = pdfReader.getNumberOfPages() + 1;
//            PdfContentByte content = pdfStamper.getUnderContent(pageSize);
//            content.addImage(image);

            PdfWriter writer = pdfStamper.getWriter();
            PdfContentByte pdfCanvas = pdfStamper.getOverContent(pageSize);
            if (pdfCanvas == null) {
                pdfCanvas = new PdfContentByte(writer);
            }

            pdfCanvas.addImage(image);
        }
        pdfStamper.close();
        pdfReader.close();
    }

    ExecutorService executor = Executors.newFixedThreadPool(6);

    public static void generateIdentificationCardPicture(JSONArray recordList){

        Set<String> patientInfoSet = new HashSet<>();
        for (Object o : recordList) {
            JSONObject recordObj = (JSONObject) o;
            String patientName = recordObj.getString("patientName");
            JSONArray photoUrlArr = recordObj.getJSONArray("photoUrl");
            try {
                for (Object photoUrl : photoUrlArr) {
                    JSONObject photoUrlObj = (JSONObject) photoUrl;
                    String url = photoUrlObj.getString("url");
                    String photoType = photoUrlObj.getString("photoType");
                    switch (photoType) {
                        case "1" :
                            photoType = "正面";
                            break;
                        case "2" :
                            photoType = "背面";
                            break;
                        case "5" :
                            photoType = "委托人正面";
                            break;
                        case "6" :
                            photoType = "委托人背面";
                            break;
                        case "7" :
                            photoType = "授权委托书";
                            break;
                        default:
                            continue;
                    }
                    String patientInfo = patientName + photoType;
                    if (!patientInfoSet.contains(patientInfo)) {
                        String filePath = tmpDir + patientInfo + ".jpg";
                        byte[] bytes = sendHttpGet(url);
                        downloadFile(filePath, bytes);
                        patientInfoSet.add(patientInfo);
                    }
                }
            }catch (Exception e) {
                logger.error("下载照片失败:", e);
            }
        }
    }

    private static void downloadFile(String fileName, byte[] bytes) throws Exception {

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
            throw e;
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


    private static byte[] sendHttpGet(String url) {
        RequestConfig config = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .setConnectTimeout(5000).build();

        logger.info("请求接口地址:{}", url);
        CloseableHttpResponse callback = null;
        byte[] bytes = null;
        CloseableHttpClient client = null;
        try {
            client = HttpClientBuilder.create().build();
            HttpGet get = new HttpGet(url);
            get.setConfig(config);
            callback = client.execute(get);
            if (callback.getStatusLine().getStatusCode() == 200) {
                bytes = EntityUtils.toByteArray(callback.getEntity());
            }else {
                logger.error("请求异常，请求状态为：{}", callback.getStatusLine().getStatusCode());
                throw new IOException("请求异常，请求状态为:"+callback.getStatusLine().getStatusCode());
            }

        }catch (IOException e) {
            logger.error("请求失败，error:{}", e.getMessage());
            e.printStackTrace();
        }finally {
            if (callback != null){
                try {
                    callback.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (client != null){
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }


    public void generatePDFFile(JSONObject recordObj) {
//        String tmpDir = System.getProperty("java.io.tmpdir");   // C:\Users\SAO·F~1\AppData\Local\Temp\
        Font titleFront = null;
        Font textFont = null;
        Font contentFront = null;

        BaseFont baseFont = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource("/font/simkai.ttf");
            baseFont = BaseFont.createFont(classPathResource.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            logger.error("1.创建字体失败:", e);
        }
        Document document = new Document(PageSize.A4, 70, 70, 50, 60);
        PdfWriter writer = null;

        String admissionNum = recordObj.getString("admissionNum");
        String agtOrdNum = recordObj.getString("agtOrdNum");
        String auditOper = recordObj.getString("auditOper");
        String auditTime = recordObj.getString("auditTime");
        String patientName = recordObj.getString("patientName");
        String patientMobile = recordObj.getString("patientMobile");
        if (StringUtils.isBlank(patientMobile)) {
            patientMobile = "";
        }

        JSONArray printDetail = recordObj.getJSONArray("printDetail");
        Map<String, JSONArray> printDetailMap = new HashMap<>();    // deptName&outDate, printDetailArr
        for (Object o : printDetail) {
            JSONObject printDetailObj = (JSONObject) o;
            String deptName = printDetailObj.getString("deptName");
            String outDate = printDetailObj.getString("outDate");
            String mapKey = deptName + "&" + outDate;
            if (printDetailMap.containsKey(mapKey)) {
                printDetailMap.get(mapKey).add(printDetailObj);
            } else {
                JSONArray arr = new JSONArray();
                arr.add(printDetailObj);
                printDetailMap.put(mapKey, arr);
            }
        }

        try {
            String filePath = tmpDir + agtOrdNum + ".pdf";
            writer = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(filePath)));
            document.open();
            document.addTitle("惠亚");
            document.addAuthor("liu_df@haici.com");
            document.addCreationDate();
            URL logoURL = new ClassPathResource("/logo.png").getURL();
            Image logoImage = Image.getInstance(logoURL);
            logoImage.setAbsolutePosition(40F, 790F);
            logoImage.scaleAbsolute(152F, 20F);
            document.add(logoImage);
            titleFront = new Font(baseFont, 18, Font.NORMAL, BaseColor.BLACK);
            Paragraph title = new Paragraph("复印出院病案资料申请单", titleFront);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            contentFront = new Font(baseFont, 12, Font.NORMAL);

            Paragraph paragraphContent = new Paragraph("\n  根据国家卫生计生委、国家中医药管理局《医疗机构病历管理规定》规定，" +
                    "我院为个人和保险公司等提供复印出院病历资料，按规定收取工本费(A4:0.5元/张，A3:1元/张)，" +
                    "请您详细阅读以下内容，并递交病案统计室审批后，按规定给予办理。", contentFront);
            document.add(paragraphContent);
            float[] widths = {55f, 50f, 45f};
            PdfPTable userInfoTable = new PdfPTable(widths);
            userInfoTable.setSpacingBefore(12f);
            userInfoTable.setWidthPercentage(100.0F);
            userInfoTable.setHeaderRows(1);
            userInfoTable.getDefaultCell().setHorizontalAlignment(1);

            PdfPCell cell;
            // 第一行
            cell = createPatientInfoCell("患者姓名: " + patientName, contentFront);
            userInfoTable.addCell(cell);
            cell = createPatientInfoCell("病历号(住院号): " + admissionNum, contentFront);
            userInfoTable.addCell(cell);
            cell = createPatientInfoCell("联系方式: " + patientMobile, contentFront);
            userInfoTable.addCell(cell);

            //第二行
            cell = createPatientInfoCell("审核人员: " + auditOper, contentFront);
            userInfoTable.addCell(cell);
            cell = createPatientInfoCell("审核时间: " + auditTime, contentFront);
            userInfoTable.addCell(cell);
            cell = createPatientInfoCell(" ", contentFront);
            userInfoTable.addCell(cell);


            // 设置表格的列宽和列数
            float[] widths2 = {25f, 25f, 40f, 10f};
            PdfPTable table2 = new PdfPTable(widths2);
            table2.setSpacingBefore(20f);
            table2.setWidthPercentage(100.0F);
            table2.setHeaderRows(1);
            table2.getDefaultCell().setHorizontalAlignment(1);

            // 病历复印资料项目: (请在需复印项目后打√)
            cell = new PdfPCell(new Paragraph("科室名称", contentFront));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(20);
            table2.addCell(cell);

            cell = new PdfPCell(new Paragraph("出院日期", contentFront));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Paragraph("项 目", contentFront));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Paragraph("打印份数", contentFront));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            Set<String> printDetailKeySet = printDetailMap.keySet();
            for (String deptNameOutDateKey : printDetailKeySet) {
                JSONArray printDetailArr = printDetailMap.get(deptNameOutDateKey);
                String[] split = deptNameOutDateKey.split("&");
                String deptName = split[0];
                String outDate = split[1];
                int printDetailArrLength = printDetailArr.size();
                cell = new PdfPCell(new Paragraph(deptName, contentFront));
                cell.setRowspan(printDetailArrLength);
                cell.setColspan(1);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table2.addCell(cell);

                cell = new PdfPCell(new Paragraph(outDate, contentFront));
                cell.setRowspan(printDetailArrLength);
                cell.setColspan(1);
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table2.addCell(cell);

                for (Object o : printDetailArr) {
                    JSONObject printDetailObj = (JSONObject) o;
                    String itemName = printDetailObj.getString("itemName");
                    String printNum = printDetailObj.getString("printNum");
                    cell = new PdfPCell(new Paragraph(itemName, contentFront));
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table2.addCell(cell);
                    cell = new PdfPCell(new Paragraph(printNum, contentFront));
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table2.addCell(cell);
                }
            }


            float[] materialTableWidth = {50f, 50f};
            PdfPTable materialTable = new PdfPTable(materialTableWidth);
            materialTable.setSpacingBefore(12f);
            materialTable.setWidthPercentage(100.0F);

            cell = createMaterialCell("患者有效身份证明;", contentFront);
            materialTable.addCell(cell);

            cell = createMaterialCell("患者本人或近亲家属同意的法定证明材料;", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("代办人身份证明或律师证;", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("授权代理的法定证明材料;", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("近亲属关系的法定证明材料;", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("公检法介绍信或采集证据的法定证明材料;", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("律师事务所介绍信;", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("其他;", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell(" ", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell(" ", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("申请人签名:", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("病案统计室经办人签名:", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("年    月    日", contentFront);
            cell.setColspan(2);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            materialTable.addCell(cell);

            textFont = new Font(baseFont, 12, Font.NORMAL);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("一、\t基本情况", textFont));
            document.add(userInfoTable);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("二、\t复印病历资料项目", textFont));
            document.add(table2);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("三、\t申请人按规定提供以下相关证明材料", textFont));
            document.add(materialTable);


            JSONArray photoUrlArr = recordObj.getJSONArray("photoUrl");
            if (CollectionUtils.isNotEmpty(photoUrlArr)){
                for (Object photoUrl : photoUrlArr) {
                    JSONObject photoUrlObj = (JSONObject) photoUrl;
                    String photoType = photoUrlObj.getString("photoType");
                    switch (photoType) {
                        case "1" :
                            photoType = "正面";
                            break;
                        case "2" :
                            photoType = "背面";
                            break;
                        case "5" :
                            photoType = "委托人正面";
                            break;
                        case "6" :
                            photoType = "委托人背面";
                            break;
                        case "7" :
                            photoType = "授权委托书";
                            break;
                        default:
                            continue;
                    }
                    String patientInfo = patientName + photoType;
                    String imgFilePath = tmpDir + patientInfo + ".jpg";
                    Image image = Image.getInstance(imgFilePath);
//                    image.setAbsolutePosition(100, 100);
//                    image.setScaleToFitHeight(true);
                    image.setScaleToFitLineWhenOverflow(true);
                    image.setAlignment(Image.ALIGN_CENTER);
                    float imageWidth = image.getWidth();
                    float imageHeight = image.getHeight();
                    if (imageWidth > imageHeight) {
                        image.setRotationDegrees(90F);
                    }
                    float scale = calculateScaleFactor(imageWidth, imageHeight, PageSize.A4.getWidth(), PageSize.A4.getHeight());
                    image.scalePercent(scale);
                    document.newPage();
                    document.add(image);
                }
            }

            logger.info("生成成功!");
        } catch (Exception e) {
            logger.error("导出pdf失败:", e);
        } finally {
            document.close();
            if (writer != null)
                writer.close();
        }
    }
    private static float calculateScaleFactor(float originalWidth, float originalHeight, float maxWidth, float maxHeight) {
        float scale = 1f;
        if (originalWidth > maxWidth || originalHeight > maxHeight) {
            scale = Math.min(maxWidth / originalWidth, maxHeight / originalHeight);
        }
        return scale * 100;
    }

    public void zipDirectory(String folderPath, HttpServletResponse response) {

        File folderFile = new File(folderPath);

        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            addFolderToZip(folderFile, "", zipOut);
        } catch (Exception e) {
            logger.info("------------", e);
        } finally {
            // 删除文件夹
            deleteDirectory(folderFile);
        }

    }

    private void addFolderToZip(File folder, String base, ZipOutputStream zipOutputStream) throws IOException {

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                addFolderToZip(file, base + "/" + file.getName(), zipOutputStream);
            } else {
                addToZip(base + "/" + file.getName(), file, zipOutputStream);
            }
        }
    }
    private void addToZip(String filePath, File file, ZipOutputStream zipOut) throws IOException {
        if (!file.isFile() || !file.getName().endsWith("pdf")) {
            return;
        }
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(filePath);
        zipOut.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
    }

    private void deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = new File(dir, child).delete();
                    if (!success) {
                        deleteDirectory(new File(dir, child));
                    }
                }
            }
        }
    }

    @Override
    public void generatePDFFile(HttpServletResponse response) {

        /**
         * com.itextpdf.text.Font:
         * 这个类包含了所有规范好的字体, 包括family of font、大小、样式和颜色、所有这些字体都被声明为静态常量
         */
        // 定义全局的字体静态变量
        Font titleFront = null;   // 字体的样式(真正展示的字体)
        Font titleFont;
        Font headFont;
        Font keyFont = null;
        Font textFont = null;
        Font contentFront = null;

        BaseFont baseFont = null;  // 字体
        try {
            // 创建字体(这里定义为同一种字体: 包含不同字号、不同style)
//            baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

//            BaseFont.createFont("C:\\Windows\\Fonts\\simfang.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED); //仿宋

            ClassPathResource classPathResource = new ClassPathResource("/font/simkai.ttf");
            baseFont = BaseFont.createFont(classPathResource.getPath(), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            /**
             * 外部字体
             * 字体样式的大小
             * 字体颜色的样式
             * 字体颜色
             */
            // 使用字体并给出颜色
            titleFont = new Font(baseFont, 16, Font.BOLD);
            headFont = new Font(baseFont, 14, Font.BOLD);
            keyFont = new Font(baseFont, 10, Font.BOLD);

        } catch (Exception e) {
            logger.error("1.创建字体失败:", e);
        }

        /**
         * ①com.itextpdf.text.Document:
         *  这是iText库中最常用的类, 它代表了一个pdf实例.
         *  如果你需要从零开始生成一个PDF文件, 你需要使用这个Document类.
         *  首先创建（new）该实例, 然后打开（open）它, 并添加（add）内容, 最后关闭（close）该实例, 即可生成一个pdf文件.
         */
        // 1.新建document对象
//        Document document = new Document(new RectangleReadOnly(842F, 595F));
        //设置页边距  60:左边距, 60:右边距, 72:上边距, 72:下边距
//        document.setMargins(60, 60, 72, 72);
        Document document = new Document(PageSize.A4, 70, 70, 50, 60);
        PdfWriter writer = null;
        try {
            /**
             * com.itextpdf.text.pdf.PdfWriter:
             * 当这个PdfWriter被添加到PdfDocument后, 所有添加到Document的内容将会写入到与文件或网络关联的输出流中.
             */
            // 2.建立一个书写器(Writer)与document对象关联,通过书写器(Writer)可以将文档写入到磁盘中.
            // 创建 PdfWriter 对象 第一个参数是对文档对象的引用,第二个参数是文件的实际名称,在该名称中还会给出其输出路径。
            writer = PdfWriter.getInstance(document, response.getOutputStream());
            //添加页码
            writer.setPageEvent(new PDFUtils());
            // 3.打开生成的pdf文件
            document.open();

            //设置属性
            //标题
            document.addTitle("惠亚");
            //作者
            document.addAuthor("liu_df@gzhc.com");
            //主题
            document.addSubject("this is subject");
            //关键字
            document.addKeywords("Keywords");
            //创建时间
            document.addCreationDate();
            //应用程序
            document.addCreator("liu_df@gzhc.com");

            URL logoURL = new ClassPathResource("/logo.png").getURL();
            Image logoImage = Image.getInstance(logoURL);
            // 设置图片位置的x轴和y轴
            logoImage.setAbsolutePosition(40F, 790F);
            // 设置图片的宽度和高度
            logoImage.scaleAbsolute(152F, 20F);
            // 将图片1添加到pdf文件中
            document.add(logoImage);

            /**
             * com.itextpdf.text.Paragraph:
             * 表示一个缩进的文本段落, 在段落中,你可以设置对齐方式、缩进、段落前后间隔等
             */

            titleFront = new Font(baseFont, 18, Font.NORMAL, BaseColor.BLACK);

            // 设置内容
            Paragraph title = new Paragraph("复印出院病案资料申请单", titleFront);
            // 对齐方式
            title.setAlignment(Element.ALIGN_CENTER);
            // 4.添加一个内容段落 引用字体
            document.add(title);

            contentFront = new Font(baseFont, 12, Font.NORMAL);

            Paragraph paragraphContent = new Paragraph("\n  根据国家卫生计生委、国家中医药管理局《医疗机构病历管理规定》规定，" +
                    "我院为个人和保险公司等提供复印出院病历资料，按规定收取工本费(A4:0.5元/张，A3:1元/张)，" +
                    "请您详细阅读以下内容，并递交病案统计室审批后，按规定给予办理。", contentFront);
            document.add(paragraphContent);

            // 设置表格的列宽和列数
            // 数组: size 为列数     int: 列数
            float[] widths = {50f, 50f, 50f};
            PdfPTable userInfoTable = new PdfPTable(widths);
            // 文字前间距
            userInfoTable.setSpacingBefore(12f);
            // 文字后间距
//            userInfoTable.setSpacingAfter(20f);
            // 设置表格宽度为100%
            userInfoTable.setWidthPercentage(100.0F);
            userInfoTable.setHeaderRows(1);
            userInfoTable.getDefaultCell().setHorizontalAlignment(1);

            PdfPCell cell;
            // 第一行
            cell = createPatientInfoCell("基本信息: " + "李德昌", contentFront);
            userInfoTable.addCell(cell);
            cell = createPatientInfoCell("出院科室: " + "心内科一区", contentFront);
            userInfoTable.addCell(cell);
            cell = createPatientInfoCell("病历号: " + "0116382", contentFront);
            userInfoTable.addCell(cell);

            //第二行
            cell = createPatientInfoCell("代办人: " + " ", contentFront);
            userInfoTable.addCell(cell);
            cell = createPatientInfoCell("与患者关系: " + " ", contentFront);
            userInfoTable.addCell(cell);
            cell = createPatientInfoCell("用途: " + "特定病种", contentFront);
            userInfoTable.addCell(cell);

            //第三行
            cell = createPatientInfoCell("联系人姓名: " + " ", contentFront);
            userInfoTable.addCell(cell);
            cell = createPatientInfoCell("联系人号码: " + "13516699038", contentFront);
            userInfoTable.addCell(cell);
            cell = createPatientInfoCell("出院时间: " + "2024.01.08", contentFront);
            userInfoTable.addCell(cell);


            // 设置表格的列宽和列数
            float[] widths2 = {25f, 25f, 25f, 25f};
            PdfPTable table2 = new PdfPTable(widths2);
            table2.setSpacingBefore(20f);
            table2.setWidthPercentage(100.0F);
            table2.setHeaderRows(1);
            table2.getDefaultCell().setHorizontalAlignment(1);

            // 病历复印资料项目: (请在需复印项目后打√)
            cell = new PdfPCell(new Paragraph("项 目", contentFront));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(20);
            table2.addCell(cell);

            cell = new PdfPCell(new Paragraph("打 √ 栏", contentFront));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Paragraph("项 目", contentFront));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Paragraph("打 √ 栏", contentFront));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            //详细数据
            JSONArray jsonArray = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("首页", "首页");
            jsonArray.add(obj);
            obj = new JSONObject();
            obj.put("出院记录", "出院记录");
            jsonArray.add(obj);
            if (jsonArray.size() > 0) {
                for (Object o : jsonArray) {
                    JSONObject jsonObject = (JSONObject) o;
                    PdfPCell cell1 = new PdfPCell(new Paragraph("首页", contentFront));
                    PdfPCell cell2 = new PdfPCell(new Paragraph("出院记录", contentFront));
                    PdfPCell cell3 = new PdfPCell(new Paragraph("入院记录", contentFront));
                    PdfPCell cell4 = new PdfPCell(new Paragraph("同意书", contentFront));
                    PdfPCell cell5 = new PdfPCell(new Paragraph("麻醉记录", contentFront));
                    PdfPCell cell6 = new PdfPCell(new Paragraph("手术记录", contentFront));

                    //单元格对齐方式
                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell1.setFixedHeight(20);
                    //单元格垂直对齐方式
                    cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    table2.addCell(cell1);
                    table2.addCell(cell2);
                    table2.addCell(cell3);
                    table2.addCell(cell4);
                    table2.addCell(cell5);
                    table2.addCell(cell6);
                }
            }


            float[] materialTableWidth = {50f, 50f};
            PdfPTable materialTable = new PdfPTable(materialTableWidth);
            materialTable.setSpacingBefore(12f);
            materialTable.setWidthPercentage(100.0F);

            cell = createMaterialCell("患者有效身份证明;", contentFront);
            materialTable.addCell(cell);

            cell = createMaterialCell("患者本人或近亲家属同意的法定证明材料;", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("代办人身份证明或律师证;", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("授权代理的法定证明材料;", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("近亲属关系的法定证明材料;", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("公检法介绍信或采集证据的法定证明材料;", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("律师事务所介绍信;", contentFront);
            materialTable.addCell(cell);
            cell = createMaterialCell("其他;", contentFront);
            materialTable.addCell(cell);



            textFont = new Font(baseFont, 12, Font.NORMAL);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("一、\t基本情况", textFont));
            document.add(userInfoTable);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("二、\t复印病历资料项目", textFont));
            document.add(table2);

            /**
             * rowStart：要写入的第一行的索引。如果您想将整个表格写入PDF文档，则将其设置为0。
             * rowEnd：要写入的最后一行的索引。如果您想将整个表格写入PDF文档，则将其设置为-1。
             * x：表格左下角的x坐标。
             * y：表格左下角的y坐标。
             * directContent：要将表格写入的PdfContentByte对象
             */
//            PdfContentByte directContent = writer.getDirectContent();
//            materialTable.setTotalWidth(300);
//            materialTable.writeSelectedRows(0, -1, 36, 100, directContent);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("三、\t申请人按规定提供以下相关证明材料", textFont));
            document.add(materialTable);

            logger.info("生成成功!");
        } catch (Exception e) {
            logger.error("导出pdf失败:", e);
        } finally {
            // 关闭文档
            document.close();
            // 关闭书写器
            if (writer != null)
                writer.close();
        }


    }


    private PdfPCell createMaterialCell(String content, Font contentFront) {
        PdfPCell cell = new PdfPCell(new Paragraph(content, contentFront));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setFixedHeight(20F);
        cell.disableBorderSide(15);
        cell.setPaddingTop(2F);
        cell.setPaddingBottom(2F);
        return cell;
    }

    private PdfPCell createPatientInfoCell(String content, Font contentFront) {
        PdfPCell cell = new PdfPCell(new Paragraph(content, contentFront));
        // 垂直居中
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        // 水平居中
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        // 高度
        cell.setFixedHeight(18);
        cell.setPaddingLeft(4F);
        cell.setPaddingBottom(1F);
        cell.setPaddingTop(1F);
        return cell;
    }


}
