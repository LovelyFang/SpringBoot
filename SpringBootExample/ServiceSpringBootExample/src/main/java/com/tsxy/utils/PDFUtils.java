package com.tsxy.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一些关于PDF的工具类
 *
 * @Author Liu_df
 * @Date 2024/3/12 14:45
 */
public class PDFUtils extends PdfPageEventHelper {

    private static final Logger logger = LoggerFactory.getLogger(PDFUtils.class);

    /**
     * 页眉
     */
    public String header = "itext测试页眉";

    /**
     * 文档字体大小, 页脚页眉最好和文本大小一致
     */
    public int presentFontSize = 15;

    /**
     * 文档页面大小，最好前面传入，否则默认为A4纸张
     */
    public Rectangle pageSize = PageSize.A4;

    // 模板
    public PdfTemplate total;

    // 基础字体对象
    public BaseFont baseFont = null;

    // 利用基础字体生成的字体对象，一般用于生成中文文字
    public Font fontDetail = null;

    /**
     * 无参构造方法.
     */
    public PDFUtils() {
    }

    /**
     * 构造方法.
     * @param presentFontSize 数据体字体大小
     * @param pageSize        页面文档大小，A4，A5，A6横转翻转等Rectangle对象
     */
    public PDFUtils(int presentFontSize, Rectangle pageSize) {
        this.presentFontSize = presentFontSize;
        this.pageSize = pageSize;
    }

    public void setPresentFontSize(int presentFontSize) {
        this.presentFontSize = presentFontSize;
    }

    /**
     * 文档打开时创建模板
     */
    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        // 共 页 的矩形的长宽高
        total = writer.getDirectContent().createTemplate(50, 50);
    }

    /**
     * 关闭每页的时候，写入页眉，写入'第几页共'这几个字。
     */
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        this.addPage(writer, document);
    }

    //加分页
    public void addPage(PdfWriter writer, Document document) {
        //设置分页页眉页脚字体
        try {
            if (baseFont == null) {
                baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
            }
            if (fontDetail == null) {
                fontDetail = new Font(baseFont, presentFontSize, Font.NORMAL);// 数据体字体
            }
        } catch (Exception e) {
            logger.error("------------------", e);
        }

        // 1.写入页眉
//        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT,
//                new Phrase(header, fontDetail),
//                document.left(), document.top() + 20, 0);
        // 2.写入前半部分的 第X页/共
        int pageS = writer.getPageNumber();
        //String foot1 = "第 " + pageS + " 页 /共";
        String foot1 = pageS + "/";
        Phrase footer = new Phrase(foot1, fontDetail);

        // 3.计算前半部分的foot1的长度，后面好定位最后一部分的'Y页'这俩字的x轴坐标，字体长度也要计算进去 = len
        float len = baseFont.getWidthPoint(foot1, presentFontSize);

        // 4.拿到当前的PdfContentByte
        PdfContentByte pdfContentByte = writer.getDirectContent();

        // 5.写入页脚1，x轴就是(右margin+左margin + right() -left()- len)/2.0F
        ColumnText.showTextAligned(pdfContentByte, Element.ALIGN_CENTER, footer,
                        (document.rightMargin() + document.right() + document.leftMargin() - document.left() - len) / 2.0F,
                        document.bottom() - 10, 0);
        pdfContentByte.addTemplate(total,
                (document.rightMargin() + document.right() + document.leftMargin() - document.left()) / 2.0F,
                document.bottom() - 10); // 调节模版显示的位置

    }

    // 加水印
    public void addWatermark(PdfWriter writer) {
        // 水印图片
        Image image;
        try {
            image = Image.getInstance("./web/images/001.jpg");
            PdfContentByte content = writer.getDirectContentUnder();
            content.beginText();
            // 开始写入水印
            for (int k = 0; k < 5; k++) {
                for (int j = 0; j < 4; j++) {
                    image.setAbsolutePosition(150 * j, 170 * k);
                    content.addImage(image);
                }
            }
            content.endText();
        } catch (Exception e) {
            logger.error("------------------", e);
        }
    }

    /**
     * 关闭文档时，替换模板，完成整个页眉页脚组件
     */
    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        // 关闭文档的时候，将模板替换成实际的 Y 值
        total.beginText();
        // 生成的模版的字体、颜色
        total.setFontAndSize(baseFont, presentFontSize);
        //页脚内容拼接  如  第1页/共2页
        //String foot2 = " " + (writer.getPageNumber()) + " 页";
        //页脚内容拼接  如  第1页/共2页
        String foot2 = String.valueOf(writer.getPageNumber());
        // 模版显示的内容
        total.showText(foot2);
        total.endText();
        total.closePath();
    }
}