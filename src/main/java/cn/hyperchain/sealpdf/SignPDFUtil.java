package cn.hyperchain.sealpdf;


import com.itextpdf.awt.geom.Rectangle2D.Float;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.security.*;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 签名证书信息实体
 */
@Slf4j
public class SignPDFUtil {
//    private static final Logger logger = LoggerFactory.getLogger(SignPDFUtil.class.getName());// slf4j日志记录器

    /**
     * 默认盖章的位置
     */
    private static java.lang.Float DEFAULT_X = 397f;
    private static java.lang.Float DEFAULT_Y = 222f;

    /**
     * 写入章
     */
    public static void signNpoSeal(String pdfFilePath, String signFileTargetPath, SignatureEntity signatureEntity) {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        ByteArrayOutputStream tempArrayOutputStream = new ByteArrayOutputStream();
        PdfStamper stamper = null;
        try {
            inputStream = new FileInputStream(pdfFilePath);
            PdfReader pdfReader = new PdfReader(inputStream, signatureEntity.getOwnerpwd().getBytes());//要求提交所有者密码
//            PdfReader pdfReader = new PdfReader(inputStream);//要求提交所有者密码
            log.info("PDF总页数{}", pdfReader.getNumberOfPages());
            /**
             * 签署位置调整
             */
            if (signatureEntity.getPage() == 0) {
                signatureEntity.setPage(pdfReader.getNumberOfPages());//最后一页
            }
            if (signatureEntity.getRectlly() == 0) {
                List<float[]> list = getKeyWords(pdfReader, signatureEntity.getPostKeyword());  // 二〇一九年三月二十九
                if (list.size() > 0) {
                    float[] xy = list.get(list.size() - 1);
                    signatureEntity.setRectllx(xy[0] + signatureEntity.getOffsetX());   //  260 在关键字附近盖章
                    signatureEntity.setRectlly(xy[1] - signatureEntity.getOffsetY());
                } else {
                    signatureEntity.setRectllx(DEFAULT_X);
                    signatureEntity.setRectlly(DEFAULT_Y);
                    log.error("*** " + DateUtil.dateToString(new Date()) + "     未找到盖章坐标，关键字：" + signatureEntity.getPostKeyword());
                }
                signatureEntity.setRecturx(signatureEntity.getRectllx() + signatureEntity.getWidth());
                signatureEntity.setRectury(signatureEntity.getRectlly() + signatureEntity.getHeight());
                log.info("*** " + DateUtil.dateToString(new Date()) + "     盖章坐标： leftBottom(X = " + signatureEntity.getRectllx() + ", Y = " + signatureEntity.getRectlly() + "), rightTop(X = " + signatureEntity.getRecturx() + ", Y = " + signatureEntity.getRectury() + ")");
            }

            // 创建签章工具PdfStamper ，最后一个boolean参数是否允许被追加签名
            // false的话，pdf文件只允许被签名一次，多次签名，最后一次有效
            // true的话，pdf可以被追加签名，验签工具可以识别出每次签名之后文档是否被修改
            PdfSignatureAppearance appearance = null;
            //使用签章
            if (signatureEntity.getPk() != null) {
                stamper = PdfStamper.createSignature(pdfReader, tempArrayOutputStream, '\0', null, signatureEntity.getAppend());
                // 获取数字签章属性对象
                appearance = stamper.getSignatureAppearance();
                appearance.setReason(signatureEntity.getReason());           // 签名的原因，显示在pdf签名属性中
                appearance.setLocation(signatureEntity.getLocation());       // 签名的地点，显示在pdf签名属性中
                // 设置签名的位置，页码，签名域名称，多次追加签名的时候，签名预名称不能一样 图片大小受表单域大小影响（过小导致压缩）
                // 签名的位置，是图章相对于pdf页面的位置坐标，原点为pdf页面左下角
                // 四个参数的分别是，图章左下角x，图章左下角y，图章右上角x，图章右上角y
                Rectangle rectangle = new Rectangle(
                        signatureEntity.getRectllx(),
                        signatureEntity.getRectlly(),
                        signatureEntity.getRecturx(),
                        signatureEntity.getRectury());
                try {
                    /**
                     * 将签名设置为可见
                     * pageRect–字段在页面中的位置和维度
                     * page–放置字段的页面。第一页是1
                     * fieldName–字段名或null，用于自动生成新字段名
                     */
                    appearance.setVisibleSignature(rectangle, signatureEntity.getPage(), signatureEntity.getFieldName());
                } catch (IllegalArgumentException e) {
                    log.error("====>>>> setVisibleSignature: " + e.getLocalizedMessage());
                    log.error("====>>>> ");
                    log.error("====>>>> 签章页码: " + signatureEntity.getPage());
                    log.error("====>>>> 表单域名称: " + signatureEntity.getFieldName());
                    log.error("====>>>> " + e.getLocalizedMessage());
                    log.error("====>>>> ");
                    e.printStackTrace();
                }
                // 签章时间
                appearance.setSignDate(signatureEntity.getSignDate());
                // 设置签章级别
                appearance.setCertificationLevel(signatureEntity.getCertificationLevel());
                // 读取图章图片
                Image sealAreaImg = Image.getInstance(signatureEntity.getSealAreaImg());
                // 设置图章
                appearance.setSignatureGraphic(sealAreaImg);
                // 设置图章的显示方式
                appearance.setRenderingMode(signatureEntity.getRenderingMode());
            } else {
                //不使用签章图片
                stamper = new PdfStamper(pdfReader, new FileOutputStream(signFileTargetPath));
            }

            // 签名算法
            if (signatureEntity.getPk() != null) {
                // 摘要算法
                ExternalDigest digest = new BouncyCastleDigest();
                ExternalSignature signature = new PrivateKeySignature(signatureEntity.getPk(),
                        signatureEntity.getDigestAlgorithm(),
                        signatureEntity.getProvider());
                // 关键点：调用签名算法进行签名
                // 这里可以改成调用格尔的java签名API进行操作
                // 调用itext签名方法完成pdf签章 数字签名格式，CMS,CADE
                MakeSignature.signDetached(appearance, digest, signature,
                        signatureEntity.getChain(),          //证书链
                        null,                         //CRL列表
                        null,                      //OCSP客户端
                        null,                       //时间戳客户端
                        0,                      //签名保留大小
                        MakeSignature.CryptoStandard.CADES);//签名格式 CMS，CADE
                inputStream = new ByteArrayInputStream(tempArrayOutputStream.toByteArray());
                // 定义输入流为生成的输出流内容，以完成多次签章的过程
                log.info("生成签章后的PDF文件：" + signFileTargetPath);
                outputStream = new FileOutputStream(new File(signFileTargetPath));
                outputStream.write(tempArrayOutputStream.toByteArray());
            }
        } catch (Exception e) {
            log.error("====>>>> pdfFilePath = " + pdfFilePath);
            log.error("====>>>> ownerpwd = " + signatureEntity.getOwnerpwd());
            e.printStackTrace();
        } finally {
            try {
                if (stamper != null) {
                    try {
                        stamper.close();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                }
                if (null != outputStream) {
                    outputStream.flush();
                    outputStream.close();
                }
                if (null != inputStream) {
                    inputStream.close();
                }
                if (null != tempArrayOutputStream) {
                    tempArrayOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 取证书的详细信息
     *
     * @param pkPath     证书文件地址 pfx
     * @param ksPassword 证书密码
     * @return
     */
    public static CertEntity getCertInfo(String pkPath, String ksPassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        char[] ksp = ksPassword.toCharArray();    //keystore密码
        CertEntity certEntity = new CertEntity();
        // 将证书文件放入指定路径，并读取keystore ，获得私钥和证书链
        KeyStore keyStore = null;
        keyStore = KeyStore.getInstance("PKCS12");
        System.out.println("CA PATH: " + pkPath);
        keyStore.load(new FileInputStream(pkPath), ksp);
        String alias = keyStore.aliases().nextElement();
        // 获取私钥
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, ksp);
        // 得到证书链
        Certificate[] certificateChain = keyStore.getCertificateChain(alias);
        // 将数据封装成实体
        certEntity.setPrivateKey(privateKey);
        certEntity.setCertificateChain(certificateChain);
        return certEntity;
    }

    /**
     * 取证书的详细信息
     *
     * @param keyStorePath 证书文件地址 p12
     * @param keyPassword  证书密码
     * @return
     */
    public static CertEntity getCertP12Info(String keyStorePath, String keyPassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        char[] ksp = keyPassword.toCharArray();    //keystore密码
        CertEntity certEntity = new CertEntity();
        BouncyCastleProvider provider = new BouncyCastleProvider();
        Security.addProvider(provider);
        KeyStore keyStore = null;
        keyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
        // 私钥密码 为Pkcs生成证书是的私钥密码
        keyStore.load(new FileInputStream(keyStorePath), ksp);
        String alias = keyStore.aliases().nextElement();
        // 获取私钥
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, ksp);
        // 得到证书链
        Certificate[] certificateChain = keyStore.getCertificateChain(alias);
        // 将数据封装成实体
        certEntity.setPrivateKey(privateKey);
        certEntity.setCertificateChain(certificateChain);
        return certEntity;
    }


    private static int i = 0;
    static List<float[]> arrays = new ArrayList<float[]>();
    static String sb;

    private static List<float[]> getKeyWords(PdfReader pdfReader, String keyWord) {
        try {
            int pageNum = pdfReader.getNumberOfPages();
            PdfReaderContentParser pdfReaderContentParser = new PdfReaderContentParser(pdfReader);
            for (i = 1; i <= pdfReader.getNumberOfPages(); i++) {
                pdfReaderContentParser.processContent(i, new RenderListener() {
                    @Override
                    public void renderText(TextRenderInfo textRenderInfo) {
                        String text = textRenderInfo.getText(); // 整页内容
                        if (null != text && text.contains(keyWord)) {
                            Float boundingRectange = textRenderInfo.getBaseline().getBoundingRectange();
                            sb = boundingRectange.x + "--" + boundingRectange.y + "---";
                            float[] resu = new float[3];
                            resu[0] = boundingRectange.x;
                            resu[1] = boundingRectange.y;
                            resu[2] = i;
                            arrays.add(resu);
                        }
                    }

                    @Override
                    public void renderImage(ImageRenderInfo arg0) {
                    }

                    @Override
                    public void endTextBlock() {
                    }

                    @Override
                    public void beginTextBlock() {
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrays;
    }
}
