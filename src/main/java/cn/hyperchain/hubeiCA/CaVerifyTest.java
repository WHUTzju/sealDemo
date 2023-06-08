package cn.hyperchain.hubeiCA;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2022-11-22 10:59
 **/
public class CaVerifyTest {

    private static String baseUrl = "http://58.49.112.182:8138";
    private static String appKey = "test002";
    private static String appSecret = "F0D959E6C3A84EE08022709E7EA0A947";
    private static String rule = "5";
    private static String sealID_01 = "9636AEAEE01243E7A0CF6A7B7CD21F7F";
    private static String sealID_02 = "9F876673431E4D4A8004D99B82640454";

    private static String signedFilePath = "/Users/zhangrui/Downloads/demo-signed.pdf";

    public static void main(String[] args) throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
        httpMessageConverters.forEach(httpMessageConverter -> {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) httpMessageConverter;
                messageConverter.setDefaultCharset(StandardCharsets.UTF_8);
            }
        });
        // 2、封装请求体
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        FileSystemResource resource = new FileSystemResource(new File(signedFilePath));
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(signedFilePath));
        params.add("pdfFile", resource);
        params.add("appKey", appKey);
        params.add("appSecret", appSecret);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                baseUrl + "/hbcaESMS/rest2/verifyPDF", httpEntity, String.class);

        String result = responseEntity.getBody();
        JSONObject json = JSONObject.parseObject(result);
        /**
         * ESS00000000 为请求成功，其他请参照错误码文档
         */
        String errorCode=json.getString("errorCode");
        /**
         * 成功则返回“1(表示验签通过)
         * 2(验签通过，由最后 一次签名控制的文档修订版次尚未被更改，但在其后，文 档已被更改)
         * 3(pdf 未盖章))”，失败则返回错误原 因
         */
        String errorMsg=json.getString("errorMsg");
        System.out.println("result:" + result);
    }
}
