package cn.hyperchain.sealpdf;

import com.itextpdf.text.pdf.PdfSignatureAppearance;
import lombok.Data;

import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Calendar;

/**
 * @program: dorado-EviBackend
 * @description:
 * @author: inkChain
 * @create: 2022-10-20 11:12
 **/
@Data
public class SignatureEntity {
    /**
     * 签章图片
     */
    private String sealAreaImg;
    /**
     * 签署页
     */
    private Integer page = 0;
    private String reason;
    /**
     * 签署位置 建议写 https://www.filoink.cn
     */
    private String location;
    /**
     *
     */
    private String fieldName;

    /**
     * 是否允许继续签名
     */
    private Boolean append = true;

    /**
     * 关键词：根据该关键词确定签署位置，默认为空
     */
    private String postKeyword;
    /**
     * 偏移量 X 相对于postKeyword
     */
    private Float offsetX;
    /**
     * 偏移量 Y 相对于postKeyword
     */
    private Float offsetY;
    /**
     * 签署位置 宽度
     */
    private Float width;
    /**
     * 签署位置 高度
     */
    private Float height;
    /**
     * 图章左下角x
     */
    private Float rectllx = 0f;
    /**
     * 图章左下角y
     */
    private Float rectlly = 0f;
    /**
     * 图章右上角x
     */
    private Float recturx;
    /**
     * 图章右上角y
     */
    private Float rectury;
    /**
     * 签名级别
     * 可选择
     * PdfSignatureAppearance
     * NOT_CERTIFIED,                           未认证
     * CERTIFIED_NO_CHANGES_ALLOWED,            认证_不允许更改
     * CERTIFIED_FORM_FILLING,                  认证_格式_填写
     * CERTIFIED_FORM_FILLING_AND_ANNOTATIONS   认证格式填充和注释
     */
    private int certificationLevel;

    /**
     * 签名时间
     */
    private Calendar signDate;
    /**
     * 要求提交所有者密码 原PDF文件的密码 默认为空
     */
    private String ownerpwd;
    /**
     * // 表现形式：仅描述，签章者和描述，图片和描述，仅图片
     * // DESCRIPTION, 仅描述
     * // NAME_AND_DESCRIPTION, 签章者和描述
     * // GRAPHIC_AND_DESCRIPTION, 图片和描述
     * // GRAPHIC仅图片
     */
    private PdfSignatureAppearance.RenderingMode renderingMode;
    /**
     * 摘要算法 SHA-256
     */
    private String digestAlgorithm;
    /**
     * CA 安全提供商 例如 BC
     */
    private String provider;
    /**
     * PrivateKey对象
     */
    private PrivateKey pk;
    private Certificate[] chain;


}
/**
 * signatureEntity.setSealAreaImg(sealWatermarImg);
 * // signEntity.setPage(2); 不设置，自动在最后一页签
 * signatureEntity.setReason(signReason); // 签名原因
 * signatureEntity.setLocation(npoEntity.getAddr());
 * signatureEntity.setFieldName(npo_name+"@"+DateUtils.getStrDateTime());
 * // 签章图片
 * signatureEntity.setPostKeyword(GlobalVar.SEAL_SIGN_POSITION_KEYWORD); // 查找签章位置的关键字
 * signatureEntity.setOffsetX(offsetX); // 图章图片右下角坐标点向右的偏移量，值越大越往右
 * signatureEntity.setOffsetY(offsetY); // 图章图片右下角坐标点向上的偏移量，值越大越往下
 * signatureEntity.setWidth(width); // 图章的宽度
 * signatureEntity.setHeight(height); // 图章的高度
 * signatureEntity.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);
 * signatureEntity.setDigestAlgorithm(DigestAlgorithms.SHA256); // SHA1
 * signatureEntity.setSignDate(calendar); // 签名时间
 * signatureEntity.setOwnerpwd(ownerpwd); // 所有者密码
 * // 表现形式：仅描述，签章者和描述，图片和描述，仅图片
 * // DESCRIPTION, NAME_AND_DESCRIPTION, GRAPHIC_AND_DESCRIPTION, GRAPHIC
 * signatureEntity.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);
 * signatureEntity.setPk(certEntity.getPrivateKey());
 * signatureEntity.setChain(certEntity.getCertificateChain());
 */