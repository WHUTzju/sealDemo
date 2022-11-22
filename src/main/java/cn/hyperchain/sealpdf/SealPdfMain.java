package cn.hyperchain.sealpdf;

import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.security.DigestAlgorithms;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2022-10-20 17:14
 **/
public class SealPdfMain {

    //    private final static String keyStorePath = "/Users/zhangrui/IdeaProjects/sealDemo/seal/keystore/keystore.p12";
    private final static String keyStorePath = "/Users/zhangrui/IdeaProjects/sealDemo/seal/filoink.pfx";
//    private final static String keyStorePath = "/Users/zhangrui/IdeaProjects/sealDemo/seal/常田加密证书.pfx";
    private final static String keyPassword = "123456";


        private final static String pdfFilePath = "/Users/zhangrui/IdeaProjects/sealDemo/seal/demo.pdf";
//    private final static String pdfFilePath = "/Users/zhangrui/IdeaProjects/sealDemo/seal/demo_quzheng.pdf";


    private final static String sealWatermarImg = "/Users/zhangrui/IdeaProjects/sealDemo/seal/sign.png";
    private final static String signReason = "电子数据存证证书";
    private final static String location = "https://filoink.cn";
    private final static String fieldName = "inkchain";

    private final static Float width = 116f;        // 图章的宽度 中信128 黄河120
    private final static Float height = 116f;    // 图章的高度 中信128 黄河120
        private final static String SEAL_SIGN_POSITION_KEYWOR = "签署人";// 查找签章位置的关键字
//    private final static String SEAL_SIGN_POSITION_KEYWOR = "湖北省武汉市尚信公证处";// 查找签章位置的关键字
    private final static Float offsetX = 85f;    // 图章图片右下角坐标点向右的偏移量，值越大越往右
    private final static Float offsetY = 40f;     // 图章图片右下角坐标点向上的偏移量，值越大越往下

    private final static String ownerpwd = "";// 所有者密码

    private static SignatureEntity getSignatureEntity() {
        SignatureEntity signatureEntity = new SignatureEntity();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"), Locale.CHINA);

//        Float offsetX = 190f + (120f - width);    // 图章图片右下角坐标点向右的偏移量，值越大越往右
//        Float offsetY = 85f;     // 图章图片右下角坐标点向上的偏移量，值越大越往下

        signatureEntity.setSealAreaImg(sealWatermarImg);
        // signEntity.setPage(2); 不设置，自动在最后一页签
        signatureEntity.setReason(signReason); // 签名原因
        signatureEntity.setLocation(location);
        signatureEntity.setFieldName(fieldName + "@" + DateUtil.dateToString(new Date()));
        // 签章图片
        signatureEntity.setPostKeyword(SEAL_SIGN_POSITION_KEYWOR); // 查找签章位置的关键字
        signatureEntity.setOffsetX(offsetX); // 图章图片右下角坐标点向右的偏移量，值越大越往右
        signatureEntity.setOffsetY(offsetY); // 图章图片右下角坐标点向上的偏移量，值越大越往下
        signatureEntity.setWidth(width); // 图章的宽度
        signatureEntity.setHeight(height); // 图章的高度
        signatureEntity.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
        signatureEntity.setDigestAlgorithm(DigestAlgorithms.SHA256); // SHA1
        signatureEntity.setSignDate(calendar); // 签名时间
        signatureEntity.setOwnerpwd(ownerpwd); // 所有者密码
        // 表现形式：仅描述，签章者和描述，图片和描述，仅图片
        // DESCRIPTION, 仅描述
        // NAME_AND_DESCRIPTION, 签章者和描述
        // GRAPHIC_AND_DESCRIPTION, 图片和描述
        // GRAPHIC仅图片
        signatureEntity.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
//        signatureEntity.setProvider("");


        return signatureEntity;
    }

    public static void main(String[] args) throws Exception {

        /**
         * 签章要素
         */
        SignatureEntity signatureEntity = getSignatureEntity();
        String pdfFileTargetPath = pdfFilePath.substring(0, pdfFilePath.lastIndexOf(".pdf")) + System.currentTimeMillis() + "-飞洛印sign.pdf";

//        signatureEntity.setRectllx(397f);
//        signatureEntity.setRectury(222f);

        /**
         * 签名证书
         */
        CertEntity certEntity = SignPDFUtil.getCertInfo(keyStorePath, keyPassword);
        signatureEntity.setPk(certEntity.getPrivateKey());
        signatureEntity.setChain(certEntity.getCertificateChain());
        SignPDFUtil.signNpoSeal(pdfFilePath,
                pdfFileTargetPath,
                signatureEntity);
    }

}
