package cn.hyperchain.seal;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2022-10-20 16:46
 **/
public class SealMain {
    // CN: 名字与姓氏    OU : 组织单位名称
    // O ：组织名称  L : 城市或区域名称  E : 电子邮件
    // ST: 州或省份名称  C: 单位的两字母国家代码
//    private static String issuerStr = "CN=在线医院,OU=gitbook研发部,O=gitbook有限公司,C=CN,E=gitbook@sina.com,L=北京,ST=北京";
//    private static String subjectStr = "CN=huangjinjin,OU=gitbook研发部,O=gitbook有限公司,C=CN,E=huangjinjin@sina.com,L=北京,ST=北京";
//    private static String certificateCRL = "https://gitbook.cn";
//    private static String password = "123456";

    private final static String issuerStr = "CN=飞洛印,OU=飞洛印,O=杭州趣链有限公司,C=CN,E=filoink@hyperchain.cn,L=杭州,ST=杭州";
    private final static String subjectStr = "CN=飞洛印,OU=飞洛印,O=杭州趣链有限公司,C=CN,E=filoink@hyperchain.cn,L=杭州,ST=杭州";
    private final static String certificateCRL = "https://filoink.cn";
    private final static String password = "123456";

    private final static String kesP12Path = "/Users/zhangrui/IdeaProjects/sealDemo/seal/keystore/keystore.p12";
    private final static String keyCerPath = "/Users/zhangrui/IdeaProjects/sealDemo/seal/keystore/keystore.cer";

    public static void main(String[] args) throws Exception {

        Map<String, byte[]> result = PKCSUtil.createCert(
                password,
                issuerStr,
                subjectStr,
                certificateCRL);
        FileOutputStream outPutStream = new FileOutputStream(kesP12Path); // ca.jks
        outPutStream.write(result.get("keyStoreData"));
        outPutStream.close();
        FileOutputStream fos = new FileOutputStream(keyCerPath);
        fos.write(result.get("certificateData"));
        fos.flush();
        fos.close();
    }
}
