## 一、申请电子印章

准备证件：电子印章申请需要到当地派出所或者行政服务大厅办理（具体根据当地情况而定），需要携带证件包括： 公司印章、公司法人的身份证以及公司营业执照等。

取章： 办理完后，一般会等上2至7天左右，等待通知去领取公司电子印章。就是一个U盘一样的加密狗，盖章时插在电脑上，按照操作说明安装一个软件进行盖章。

第三方认证机构

一般电子签章需要有第三方电子签章机构进行认证，如果嫌自己办理麻烦，提供给他们相关的材料后他们也能办理，就是费用相对高些。

他们一般提供签章接口以及pkcs12证书、对应的密钥文件，可以在程序中直接调用。这里演示一下自己生成证书，自己作为认证机构的情况。

## 二、生成PKCS12证书

### 1. PKCS介绍

PKCS：The Public-Key Cryptography Standards (简称PKCS)是由美国RSA数据安全公司及其合作伙伴制定的一组公钥密码学标准，其中包括证书申请、证书更新、证书作废表发布、扩展证书内容以及数字签名、数字信封的格式等方面的一系列相关协议。

到1999年底，PKCS已经公布了以下标准：

PKCS#1：定义RSA公开密钥算法加密和签名机制，主要用于组织PKCS#7中所描述的数字签名和数字信封[22]。

PKCS#3：定义Diffie-Hellman密钥交换协议[23]。

PKCS#5：描述一种利用从口令派生出来的安全密钥加密字符串的方法。使用MD2或MD5 从口令中派生密钥，并采用DES-CBC模式加密。主要用于加密从一个计算机传送到另一个计算机的私人密钥，不能用于加密消息[24]。

PKCS#6：描述了公钥证书的标准语法，主要描述X.509证书的扩展格式[25]。

PKCS#7：定义一种通用的消息语法，包括数字签名和加密等用于增强的加密机制，PKCS#7与PEM兼容，所以不需其他密码操作，就可以将加密的消息转换成PEM消息[26]。

PKCS#8：描述私有密钥信息格式，该信息包括公开密钥算法的私有密钥以及可选的属性集等[27]。

PKCS#9：定义一些用于PKCS#6证书扩展、PKCS#7数字签名和PKCS#8私钥加密信息的属性类型[28]。

PKCS#10：描述证书请求语法[29]。

PKCS#11：称为Cyptoki，定义了一套独立于技术的程序设计接口，用于智能卡和PCMCIA卡之类的加密设备[30]。

PKCS#12：描述个人信息交换语法标准。描述了将用户公钥、私钥、证书和其他相关信息打包的语法[31]。

PKCS#13：椭圆曲线密码体制标准[32]。

PKCS#14：伪随机数生成标准。

PKCS#15：密码令牌信息格式标准[33]。

PKCS12也就是以上标准的PKCS#12，主要用来描述个人身份信息

### JAVA生成PKCS12证书并进行存贮
```java
public class Extension {
 
        private String oid;
 
        private boolean critical;
 
        private byte[] value;
 
        public String getOid() {
            return oid;
        }
 
        public byte[] getValue() {
            return value;
        }
        public boolean isCritical() {
            return critical;
        }
    }
 
 
    import java.io.ByteArrayInputStream;
    import java.io.ByteArrayOutputStream;
    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.math.BigInteger;
    import java.security.KeyPair;
    import java.security.KeyPairGenerator;
    import java.security.KeyStore;
    import java.security.NoSuchAlgorithmException;
    import java.security.PrivateKey;
    import java.security.PublicKey;
    import java.security.SecureRandom;
    import java.security.cert.Certificate;
    import java.security.cert.CertificateFactory;
    import java.security.cert.X509Certificate;
    import java.text.SimpleDateFormat;
    import java.util.Calendar;
    import java.util.Date;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.Random;
 
    import org.bouncycastle.asn1.ASN1ObjectIdentifier;
    import org.bouncycastle.asn1.ASN1Primitive;
    import org.bouncycastle.asn1.x500.X500Name;
    import org.bouncycastle.asn1.x509.BasicConstraints;
    import org.bouncycastle.asn1.x509.CRLDistPoint;
    import org.bouncycastle.asn1.x509.DistributionPoint;
    import org.bouncycastle.asn1.x509.DistributionPointName;
    import org.bouncycastle.asn1.x509.GeneralName;
    import org.bouncycastle.asn1.x509.GeneralNames;
    import org.bouncycastle.asn1.x509.KeyUsage;
    import org.bouncycastle.cert.X509CertificateHolder;
    import org.bouncycastle.cert.X509v3CertificateBuilder;
    import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
    import org.bouncycastle.jce.provider.BouncyCastleProvider;
    import org.bouncycastle.operator.ContentSigner;
    import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
 
    public class Pkcs {
 
    private static KeyPair getKey() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA",
                new BouncyCastleProvider());
        generator.initialize(1024);
        // 证书中的密钥 公钥和私钥
        KeyPair keyPair = generator.generateKeyPair();
        return keyPair;
    }
 
    /**
     * @param password
     *            密码
     * @param issuerStr 颁发机构信息
     * 
     * @param subjectStr 使用者信息
     * 
    * @param certificateCRL 颁发地址
     * 
     * @return
     */
    public static Map<String, byte[]> createCert(String password,
            String issuerStr, String subjectStr, String certificateCRL) {
        Map<String, byte[]> result = new HashMap<String, byte[]>();
        ByteArrayOutputStream out = null;
        try {
            // 生成JKS证书
            // KeyStore keyStore = KeyStore.getInstance("JKS");
            // 标志生成PKCS12证书
            KeyStore keyStore = KeyStore.getInstance("PKCS12",
                    new BouncyCastleProvider());
            keyStore.load(null, null);
            KeyPair keyPair = getKey();
            // issuer与 subject相同的证书就是CA证书
            Certificate cert = generateCertificateV3(issuerStr, subjectStr,
                    keyPair, result, certificateCRL, null);
            // cretkey随便写，标识别名
            keyStore.setKeyEntry("cretkey", keyPair.getPrivate(),
                    password.toCharArray(), new Certificate[] { cert });
            out = new ByteArrayOutputStream();
            cert.verify(keyPair.getPublic());
            keyStore.store(out, password.toCharArray());
            byte[] keyStoreData = out.toByteArray();
            result.put("keyStoreData", keyStoreData);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }
 
    /**
     * @param issuerStr
     * @param subjectStr
     * @param keyPair
     * @param result
     * @param certificateCRL
     * @param extensions
     * @return
     */
    public static Certificate generateCertificateV3(String issuerStr,
            String subjectStr, KeyPair keyPair, Map<String, byte[]> result,
            String certificateCRL, List<Extension> extensions) {
        ByteArrayInputStream bout = null;
        X509Certificate cert = null;
        try {
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
            Date notBefore = new Date();
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(notBefore);
            // 日期加1年
            rightNow.add(Calendar.YEAR, 1);
            Date notAfter = rightNow.getTime();
            // 证书序列号
            BigInteger serial = BigInteger.probablePrime(256, new Random());
            X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(
                    new X500Name(issuerStr), serial, notBefore, notAfter,
                    new X500Name(subjectStr), publicKey);
            JcaContentSignerBuilder jBuilder = new JcaContentSignerBuilder(
                    "SHA1withRSA");
            SecureRandom secureRandom = new SecureRandom();
            jBuilder.setSecureRandom(secureRandom);
            ContentSigner singer = jBuilder.setProvider(
                    new BouncyCastleProvider()).build(privateKey);
            // 分发点
            ASN1ObjectIdentifier cRLDistributionPoints = new ASN1ObjectIdentifier(
                    "2.5.29.31");
            GeneralName generalName = new GeneralName(
                    GeneralName.uniformResourceIdentifier, certificateCRL);
            GeneralNames seneralNames = new GeneralNames(generalName);
            DistributionPointName distributionPoint = new DistributionPointName(
                    seneralNames);
            DistributionPoint[] points = new DistributionPoint[1];
            points[0] = new DistributionPoint(distributionPoint, null, null);
            CRLDistPoint cRLDistPoint = new CRLDistPoint(points);
            builder.addExtension(cRLDistributionPoints, true, cRLDistPoint);
            // 用途
            ASN1ObjectIdentifier keyUsage = new ASN1ObjectIdentifier(
                    "2.5.29.15");
            // | KeyUsage.nonRepudiation | KeyUsage.keyCertSign
            builder.addExtension(keyUsage, true, new KeyUsage(
                    KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
            // 基本限制 X509Extension.java
            ASN1ObjectIdentifier basicConstraints = new ASN1ObjectIdentifier(
                    "2.5.29.19");
            builder.addExtension(basicConstraints, true, new BasicConstraints(
                    true));
            // privKey:使用自己的私钥进行签名，CA证书
            if (extensions != null)
                for (Extension ext : extensions) {
                    builder.addExtension(
                            new ASN1ObjectIdentifier(ext.getOid()),
                            ext.isCritical(),
                            ASN1Primitive.fromByteArray(ext.getValue()));
                }
            X509CertificateHolder holder = builder.build(singer);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            bout = new ByteArrayInputStream(holder.toASN1Structure()
                    .getEncoded());
            cert = (X509Certificate) cf.generateCertificate(bout);
            byte[] certBuf = holder.getEncoded();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            // 证书数据
            result.put("certificateData", certBuf);
            //公钥
            result.put("publicKey", publicKey.getEncoded());
            //私钥
            result.put("privateKey", privateKey.getEncoded());
            //证书有效开始时间
            result.put("notBefore", format.format(notBefore).getBytes("utf-8"));
            //证书有效结束时间
            result.put("notAfter", format.format(notAfter).getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bout != null) {
                try {
                    bout.close();
                } catch (IOException e) {
                }
            }
        }
        return cert;
    }
 
    public static void main(String[] args) throws Exception{
        // CN: 名字与姓氏    OU : 组织单位名称
        // O ：组织名称  L : 城市或区域名称  E : 电子邮件
        // ST: 州或省份名称  C: 单位的两字母国家代码 
        String issuerStr = "CN=在线医院,OU=gitbook研发部,O=gitbook有限公司,C=CN,E=gitbook@sina.com,L=北京,ST=北京";
        String subjectStr = "CN=huangjinjin,OU=gitbook研发部,O=gitbook有限公司,C=CN,E=huangjinjin@sina.com,L=北京,ST=北京";
        String certificateCRL  = "https://gitbook.cn";
        Map<String, byte[]> result = createCert("123456", issuerStr, subjectStr, certificateCRL);
 
        FileOutputStream outPutStream = new FileOutputStream("c:/keystore.p12"); // ca.jks
        outPutStream.write(result.get("keyStoreData"));
        outPutStream.close();
        FileOutputStream fos = new FileOutputStream(new File("c:/keystore.cer"));
        fos.write(result.get("certificateData"));
        fos.flush();
        fos.close();
    }
    }
```