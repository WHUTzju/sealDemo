package cn.hyperchain.hubeiCA;

import com.alibaba.fastjson.JSON;
import org.springframework.core.io.FileSystemResource;
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
import sun.nio.ch.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2022-11-22 10:14
 **/
public class CaTest {


//    private static String baseUrl = "http://58.49.112.182:8138";
//    private static String appKey = "test002";
//    private static String appSecret = "F0D959E6C3A84EE08022709E7EA0A947";
//    private static String rule = "5";
//    private static String rule_67 = "67";//关键字"保管机构："
//    private static String sealID_01 = "9636AEAEE01243E7A0CF6A7B7CD21F7F";

    private static String baseUrl = "http://61.183.191.91:8380";
    private static String appKey = "shangxin2023";
    private static String appSecret = "88989C69B9F348F6A71146C50FAF38C0";
    private static String rule_67 = "74";//关键字"保管机构："
    private static String sealID_01 = "D625A6A6147442F6A16DFB65EA252B6E";

    private static String toFilePath = "/Users/zhangrui/IdeaProjects/sealDemo/seal/to_sign.pdf";
    private static String outFilePath = "/Users/zhangrui/IdeaProjects/sealDemo/seal/demo-signed.pdf";

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
        FileSystemResource resource = new FileSystemResource(new File(toFilePath));
        params.add("pdfFile", resource);
        params.add("appKey", appKey);
        params.add("appSecret", appSecret);
        params.add("templateIds", rule_67);
        params.add("sealId", sealID_01);

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        headers.setContentType(type);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(params, headers);

        ResponseEntity<Resource> responseEntity = restTemplate.postForEntity(
                baseUrl + "/hbcaESMS/rest2/signPDFByTemplateAndSeal", httpEntity, Resource.class);
        InputStream ins = Objects.requireNonNull(responseEntity.getBody()).getInputStream();

        File outFile = new File(outFilePath);
        FileOutputStream fos = new FileOutputStream(outFile);
        byte[] buffer = new byte[1024];
        int i = ins.read(buffer);
        while (i != -1) {
            fos.write(buffer, 0, i);
            i = ins.read(buffer);
        }

        System.out.println("result:" + outFile.getAbsolutePath());
    }
}
