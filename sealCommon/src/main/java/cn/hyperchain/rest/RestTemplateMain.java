package cn.hyperchain.rest;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2022-10-27 13:51
 **/
public class RestTemplateMain {

    public static void main(String[] args) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(3000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
        httpMessageConverters.forEach(httpMessageConverter -> {
            if (httpMessageConverter instanceof StringHttpMessageConverter) {
                StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) httpMessageConverter;
                messageConverter.setDefaultCharset(StandardCharsets.UTF_8);
            }
        });
        Map<String, Object> paramMap = new HashMap<String, Object>() {{
            put("appKey", "2ZAiMTjRahxkgR7nQEghU2QrxFGjJTaEhj82VtWgwnTL");
            put("appSecret", "QmYsd6JTEJ2NrLJGHmiSWiwfSrdAemkLf4KaRkpQiAtTpJ");
            put("createDate", "2022-10-25");
            put("page", 1);
            put("pageSize", 10);
        }};
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(paramMap), headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "http://121.199.71.17:8100/v3/app/getEvidenceList", httpEntity, String.class);
        String result = responseEntity.getBody();
        System.out.println("result:" + result);
    }
}
