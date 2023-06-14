package cn.hyperchain.sealpdf;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Security;
import java.util.List;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2022-11-23 16:10
 **/
@Slf4j
public class SignVerifyUtil {

    public static void setUp() throws Exception {
        BouncyCastleProvider bcp = new BouncyCastleProvider();
        //Security.addProvider(bcp);
        Security.insertProviderAt(bcp, 1);
    }

    public static void main(String[] args) throws Exception {


        InputStream resource1 = new FileInputStream("/Users/zhangrui/IdeaProjects/sealDemo/seal/demo1678596456453-飞洛印sign-2.pdf");
        InputStream resource2 = new FileInputStream("/Users/zhangrui/IdeaProjects/sealDemo/seal/demo1678596350065-飞洛印sign-2.pdf");

        log.info("验证1:{}", verifyPdf(resource1));
        log.info("验证2:{}", verifyPdf(resource2));

    }


    private static Boolean verifyPdf(InputStream resource) throws Exception {

        boolean result = true;

        try {
            setUp();
            PdfReader reader = new PdfReader(resource);
            AcroFields acroFields = reader.getAcroFields();
            List<String> names = acroFields.getSignatureNames();
            if (null == names || names.size() == 0) {
                log.info("PDF签名不存在 验签false");
                result = false;
            }
            for (String name : names) {
                log.info("Signature name: " + name);
                log.info("Signature covers whole document: " + acroFields.signatureCoversWholeDocument(name));
                PdfPKCS7 pk = acroFields.verifySignature(name);
                log.info("Subject: " + CertificateInfo.getSubjectFields(pk.getSigningCertificate()));
                log.info("Document verifies: " + pk.verify());
                if (!pk.verify()) {
                    result = false;
                }
            }
        } catch (Exception e) {
            log.error("验证签名过程异常 ");
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}
