package cn.hyperchain.dataReg;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2023-04-13 15:38
 **/
@Slf4j
public class getEviTest {

    public static void main(String[] args) {


        EPConnectInfo test_xzlInfo = EPConnectInfo.builder()
                .platName("信证链")
                .url("http://47.114.77.180:9090")
                .accessKey("ecd37aa7cb834af58ad596b5c6728f06")
                .accessSecret("a603ab77e10d4179ae4060d27565900d")
                .build();

        EPConnectInfo test_wzOfficeInfo = EPConnectInfo.builder()
                .platName("温州市中信公证数据存储平台")
                .url("http://101.68.74.170:27001")
                .accessKey("2881e48f-0e7b-8f53-22f8-bf852db78c99")
                .accessSecret("27b8e94c-136f-eb27-97cb-132d3213d232")
                .build();

        EPConnectInfo test_hhOfficeInfo = EPConnectInfo.builder()
                .platName("杭州互联网公证处简证平台")
                .url("http://101.68.74.170:18090")
                .accessKey("16924ea6-b6c4-53c6-9b60-0cab9d8e9a37")
                .accessSecret("43a2b1ca-1c17-be27-d5c4-a0b5616f2efa")
                .build();

        EPConnectInfo test_baoquanInfo = EPConnectInfo.builder()
                .platName("保全网")
                .url("http://101.68.74.170:9800")
                .accessKey("061b24a561194")
                .accessSecret("e8388d4721d247f7bac5f29ecbeaa5a5")
                .build();

        EPConnectInfo test_yinyuanInfo = EPConnectInfo.builder()
                .platName("鄞源公证处存证平台")
                .url("http://aonxin.com:23171")
                .accessKey("test")
                .accessSecret("123456")
                .build();

        EPConnectInfo test_ancunInfo = EPConnectInfo.builder()
                .platName("枢纽链")
                .url("http://115.236.89.210:4603/relay/preserve")
                .accessKey("b6edabf72a29f4b6823cb26a185190e3")
                .accessSecret("0DhhM2Q3ZDk1NmZhYTc5N2NkY2EwN2ZmYzNiYmQyYzc1N2I30DU1ZA")
                .build();



        EPConnectInfo prod_xzlInfo = EPConnectInfo.builder()
                .platName("信证链")
                .url("https://openapi.wetrustchain.com")
                .accessKey("8711ae078a7a499fa2f1b2491289fdaa")
                .accessSecret("b5dce053ff02405790025e8371cb2c29")
                .build();

        EPConnectInfo prod_wzOfficeInfo = EPConnectInfo.builder()
                .platName("温州市中信公证数据存储平台")
                .url("https://czptapi.wz-notary.com.cn")
                .accessKey("2881e48f-0e7b-8f53-22f8-bf852db78c99")
                .accessSecret("27b8e94c-136f-eb27-97cb-132d3213d232")
                .build();

        EPConnectInfo prod_hhOfficeInfo = EPConnectInfo.builder()
                .platName("杭州互联网公证处简证平台")
                .url("https://api.netnotary.cn")
                .accessKey("16924ea6-b6c4-53c6-9b60-0cab9d8e9a37")
                .accessSecret("43a2b1ca-1c17-be27-d5c4-a0b5616f2efa")
                .build();

        EPConnectInfo prod_baoquanInfo = EPConnectInfo.builder()
                .platName("保全网")
                .url("https://swallow.baoquan.com")
                .accessKey("fe2c26180c258d")
                .accessSecret("078e4166a48c4dfd8b41294363a8ef0b")
                .build();

        EPConnectInfo prod_zjbigDataInfo = EPConnectInfo.builder()
                .platName("浙江大数据交易中心有限公司")
                .url("https://block-chain-api.tssldc.com")
                .accessKey("fe2c26180c258d")
                .accessSecret("078e4166a48c4dfd8b41294363a8ef0b")
                .build();


//        postZjdj(test_xzlInfo, "20230414001");
//        postZjdj(test_wzOfficeInfo, "12341234");
//        postZjdj(test_hhOfficeInfo, "12341234");
//        postZjdj(test_baoquanInfo, "12341234");
//        postZjdj(test_yinyuanInfo, "Z35ZJ201116QDZFM8");//Z35ZJ201116RXLXZ4
//        postZjdj(test_ancunInfo, "E104172014460E15X088561WV1XV0I1X");
        postZjdj(test_ancunInfo, "GI91461767756W116166G7V4660XMHHF");

        //prod
//        postZjdj(prod_xzlInfo, "20230414001");
//        postZjdj(prod_wzOfficeInfo, "12341234");
//        postZjdj(prod_hhOfficeInfo, "12341234");
//        postZjdj(prod_baoquanInfo, "12341234");
//
//        postZjdj(prod_zjbigDataInfo, "0xa29b70a6c5822bb043ba1a8639c2b3edf5376d7d65a469e434fc691819c11cce");


    }


    public static void postZjdj(EPConnectInfo epConnectInfo, String certNo) {
        log.info("start 平台名称:{} 编号:{}", epConnectInfo.getPlatName(), certNo);
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
        httpMessageConverters.forEach(httpMessageConverter -> {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) httpMessageConverter;
                messageConverter.setDefaultCharset(StandardCharsets.UTF_8);
            }
        });
        // 2、封装请求体
        Map<String, Object> paramMap = new HashMap<String, Object>() {{
            put("certNo", certNo);
        }};
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("accessKey", epConnectInfo.getAccessKey());
//        headers.set("accessSecret", epConnectInfo.getAccessKey());
        String timestamp = String.valueOf(System.currentTimeMillis());
        String toSignStr = epConnectInfo.getAccessSecret() + timestamp + certNo;
        String accessSign = Hashing.sha256().hashBytes(toSignStr.getBytes(StandardCharsets.UTF_8)).toString();
        headers.set("accessSign", accessSign);
        headers.set("timestamp", timestamp);

        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(paramMap), headers);
        log.info("httpEntity:{}", JSONObject.toJSONString(httpEntity));
        log.info("accessKey:{}\ntimestamp:{}\naccessSign:{}", epConnectInfo.getAccessKey(), timestamp, accessSign);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                epConnectInfo.getUrl() + "/zjdj/getEvidenceInfo", httpEntity, String.class);
//        String result = new String(Objects.requireNonNull(responseEntity.getBody()).getBytes(), StandardCharsets.UTF_8);
        String result = responseEntity.getBody();
        System.out.println("result:" + result);


        log.info("start 平台名称:{} 结果:{}", epConnectInfo.getPlatName(), result);
        log.info("start 平台名称:{} 结果:{}", epConnectInfo.getPlatName(), unicodeDecode(result));

//        JSONObject jsonObject=JSONObject.parseObject(unicodeDecode(result));
//        EPEviInfo info=jsonObject.getObject("data",EPEviInfo.class);

//        log.info("info{}",info.getCertNo());
    }


    public static String getStringHash(String str) {
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
            encodestr = toHexString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    /**
     * byte数组转16进制字符串
     *
     * @param src byte数组
     * @return 16进制字符串
     */
    private static String toHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv + "");
        }
        return stringBuilder.toString();
    }

//    public static void main(String[] args) {
////        long start = System.currentTimeMillis();
////        System.out.println(getStringHash("hello"));
////
////        System.out.println(Hashing.sha256().hashBytes("hello".getBytes(StandardCharsets.UTF_8)).toString());
//
//    }

    /**
     * @param string
     * @return 转换之后的内容
     * @Title: unicodeDecode
     * @Description: unicode解码 将Unicode的编码转换为中文
     */
    private final static Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
    public static String unicodeDecode(String string) {

        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }
}
