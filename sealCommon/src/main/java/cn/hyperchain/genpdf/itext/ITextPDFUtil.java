package cn.hyperchain.genpdf.itext;

import cn.hyperchain.genpdf.zxing.ZXingCodeUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2022-10-21 09:53
 **/
public class ITextPDFUtil {

    public static final String baseFonthPath = "pdf_source/common/font/PingFang-Jian-ChangGuiTi-2.ttf";
    public static final String bgPath = "pdf_source/ordinary/img/bg.jpg";
    public static final String stampPath = "pdf_source/ordinary/img/sign.png";
    public static final String h5VerifyIp = "https://test.filoink.cn:9043/normal/query/newdetail?platform=0&confirmationNo=";

    public static final float IMAGE_HEIGHT = 842.0F;
    public static final float IMAGE_WIDTH = 595.0F;

    private File createPDF(OrdinaryParam ordinaryParam) throws Exception {
        /**
         * 0 资源
         */
        Font titleFont = null;
        Font mainFont = null;
        Image bg = null;
        Paragraph titleParagraph = null; // 标题段落
        PdfPTable subTitleParagraph = null; // 副标题
        PdfPTable mainTextParagraph = null; //正文段落
        AddTableParam mainTextStyle = null; //正文段落样式
        Paragraph picParagraph = null; // 图片资源段落

        /**
         *  1确认函初始化
         */
        Rectangle rectangle = new Rectangle(PageSize.A4);// 纸张矩形，大小采用 A4
        Document document = new Document(rectangle, 0, 0, 0, 0);///创建一个文档对象，设置初始化大小和页边距 上、下、左、右页间距
        File pdfFile = new File(System.getProperty("java.io.tmpdir"), ordinaryParam.getEviId() +
                System.currentTimeMillis() + ".pdf");//PDF 的输出位置
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile.getPath()));// 打开文档对象
        document.open();

        /**
         * 2 字体资源
         * /pdf_source/common/font/PingFang-Jian-ChangGuiTi-2.ttf
         */
        BaseFont baseFont = BaseFont.createFont(baseFonthPath,
                BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        mainFont = new Font(baseFont, 12);
        mainFont.setColor(11, 11, 11);
        titleFont = new Font(baseFont, 12);
        titleFont.setColor(255, 255, 255);
        /**
         * 背景
         */
        // 3. 段落
        titleParagraph = new Paragraph();
        titleParagraph.setAlignment(Element.ALIGN_CENTER);//居中
        titleParagraph.setSpacingBefore(138);
        titleParagraph.setFont(titleFont);
        mainTextParagraph = new PdfPTable(2);//填充表格数据
        mainTextParagraph.setHorizontalAlignment(Element.ALIGN_CENTER);//居中
        mainTextParagraph.setWidthPercentage(70);//宽度百分数
        mainTextParagraph.setWidths(new float[]{2f, 4});//每一列的宽度占比
        mainTextParagraph.setSpacingBefore(10); //上面的空间
        mainTextStyle = new AddTableParam();//正文样式
        mainTextStyle.setTitleFont(mainFont);
        mainTextStyle.setSpacing(10);
        mainTextStyle.setContextFont(mainFont);
        mainTextStyle.setContextVerticalAlignment(Element.ALIGN_MIDDLE);
        mainTextStyle.setPaddingTop(10);
        mainTextStyle.setLeading(2);
        picParagraph = new Paragraph(); //图片
        picParagraph.setAlignment(Element.ALIGN_LEFT);
        picParagraph.setSpacingBefore(50);
        picParagraph.setIndentationLeft(95);
        picParagraph.setIndentationRight(95);
        // 4. 设置背景
        bg = Image.getInstance(bgPath);
        bg.scaleAbsolute(IMAGE_WIDTH, IMAGE_HEIGHT);
        bg.setAbsolutePosition(0, 0);// （以左下角为原点）设置图片的坐标

        /**
         * 5 渲染文本
         */
        // todo 这个放到参数组装里
        String s = "存证";
        // 填充标题
        titleParagraph.add("证书编号：" + ordinaryParam.getEviId());

        // 填充正文
        /**
         * 填充正文 正文分三段
         * 1 保管人信息
         * 2 存/取证信息
         * 3 区块链信息
         */
        Map<String, String> subjectMap = new LinkedHashMap<>();
        subjectMap.put("保管人（数据来源）：", ordinaryParam.getSubjectOrigin());
        subjectMap.put("身份标识：", ordinaryParam.getSubjectIdentify());
        subjectMap.put("保管人账号（手机号)：", ordinaryParam.getSubjectPhone());

        Map<String, String> contentMap = new LinkedHashMap<>();
        contentMap.put("存证名称：", ordinaryParam.getName());
        contentMap.put("存证类型：", ordinaryParam.getType());
        //文件存证
        if (!ObjectUtils.isEmpty(ordinaryParam.getFileSize())) {
            contentMap.put("文件大小:", ordinaryParam.getFileSize());
        }
        contentMap.put("存证时间：", ordinaryParam.getOpTime());
        contentMap.put("上链时间：", ordinaryParam.getChainTime());

        Map<String, String> chainMap = new LinkedHashMap<>();
        chainMap.put("所属区块链：", ordinaryParam.getChainName());
        chainMap.put("电子数据哈希：", ordinaryParam.getHash());
        chainMap.put("交易哈希：", ordinaryParam.getTxHash());
        chainMap.put("保管机构：", ordinaryParam.getCustodian());

        /**
         * 增加空cell
         */
        Paragraph paragraph = new Paragraph(Chunk.NEWLINE);
        PdfPCell cell = new PdfPCell(paragraph);
        cell.setBorder(Rectangle.NO_BORDER);

        /**
         * 段落1
         */
        addPdfTable(mainTextParagraph, subjectMap, mainTextStyle);
        //增加空行
        mainTextParagraph.addCell(cell);
        mainTextParagraph.addCell(cell);

        /**
         * 段落2
         */
        addPdfTable(mainTextParagraph, contentMap, mainTextStyle);
        mainTextParagraph.addCell(cell);
        mainTextParagraph.addCell(cell);

        /**
         * 段落3
         */
        addPdfTable(mainTextParagraph, chainMap, mainTextStyle);


        /**
         * 6. 渲染二维码
         */
        File qrCode = ZXingCodeUtil.createQRCode(h5VerifyIp + ordinaryParam.getConfirmationNo(), false, ".png", null);
        addImageWatermark(picParagraph, qrCode.getAbsolutePath(), Image.RIGHT, 405, 112, 85, 85);
        qrCode.delete();

        /**
         * 7. 添加印章
         */
        addImageWatermark(picParagraph, stampPath, Image.RIGHT, 397, 222, 116, 116);

        /**
         * 8. 成文
         */
        // 加载资源
        if (!ObjectUtils.isEmpty(bg)) {
            document.add(bg);
        }
        document.add(new Paragraph("占位符"));
        // 加载标题文本
        if (!ObjectUtils.isEmpty(titleParagraph)) {
            document.add(titleParagraph);
        }
        // 加载副标题文本
        if (!ObjectUtils.isEmpty(subTitleParagraph)) {
            document.add(subTitleParagraph);
        }
        // 加载正文文本
        if (!ObjectUtils.isEmpty(mainTextParagraph)) {
            document.add(mainTextParagraph);
        }
        // 加载图片
        if (!ObjectUtils.isEmpty(picParagraph)) {
            document.add(picParagraph);
        }
        if (document != null) {
            document.close();
        }
        return pdfFile;
    }

    /**
     * 添加二维码、印章等图片水印
     *
     * @param paragraph     水印所属段落
     * @param watermarkPath 图片水印路径
     * @param alignment
     * @param absoluteX
     * @param absoluteY
     * @param fitWidth
     * @param fitHeight
     * @throws IOException
     * @throws BadElementException
     */
    public static void addImageWatermark(Paragraph paragraph, String watermarkPath,
                                         int alignment, float absoluteX, float absoluteY,
                                         float fitWidth, float fitHeight
    ) throws IOException, BadElementException {
        //二维码
        Image instance = Image.getInstance(watermarkPath);
        instance.setAlignment(alignment);
        instance.setAbsolutePosition(absoluteX, absoluteY);
        instance.scaleToFit(fitWidth, fitHeight);
        paragraph.add(instance);
    }

    /**
     * 快速创建想要的表格单元格
     *
     * @param text       文字
     * @param font       字体
     * @param paddingTop 单元格的行间距
     * @return
     */
    public static PdfPCell getTableParagraph(String text, Font font, int paddingTop) {
        Paragraph paragraph;
        paragraph = new Paragraph(text, font);
        PdfPCell cell = new PdfPCell(paragraph);
        cell.setPaddingTop(paddingTop);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    /**
     * 快速创建想要的确认函
     *
     * @param text
     * @param tableParagraphParam
     * @return
     */
    public static PdfPCell getTableParagraph(String text, TableParagraphParam tableParagraphParam) {
        Paragraph paragraph;
        paragraph = new Paragraph(text, tableParagraphParam.getFont());
        PdfPCell cell = new PdfPCell(paragraph);
        cell.setHorizontalAlignment(tableParagraphParam.getHorizontalAlignment());
        cell.setVerticalAlignment(tableParagraphParam.getVerticalAlignment());
        cell.setLeading(tableParagraphParam.getLeading(), 1);
        cell.setPaddingTop(tableParagraphParam.getPaddingTop());
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    /**
     * 向pdfPTable中快速添加Key-Value类型的数据
     * 参数最全并且可以快速扩展的版本，建议都使用这个
     *
     * @param pdfPTable     PDF表格对象
     * @param content       添加的内容
     * @param addTableParam 格式参数
     */
    public static void addPdfTable(PdfPTable pdfPTable, Map<String, String> content, AddTableParam addTableParam) {
        Set<String> keySet = content.keySet();
        keySet.forEach(key -> {
            PdfPCell title = getTableParagraph(key, TableParagraphParam.of(addTableParam, true));
            pdfPTable.addCell(title);
            PdfPCell context = getTableParagraph(content.get(key), TableParagraphParam.of(addTableParam, false));
            context.setPaddingLeft(addTableParam.getSpacing());
            pdfPTable.addCell(context);
        });
    }
}
