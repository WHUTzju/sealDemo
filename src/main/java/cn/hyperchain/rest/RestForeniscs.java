package cn.hyperchain.rest;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2022-12-06 21:44
 **/
@Slf4j
public class RestForeniscs {
    private final static String INITIATE_URL = "/webForensics/initiate";
    private final static String GET_RESULT_URL = "/webForensics/getResult";
    private final static String CHECK_CAPACITY_URL = "/webForensics/checkCapacity";
    //    private final static String BASE_URL = "http://172.16.0.53/internal/api/open-web-forensics-task";
    private final static String BASE_URL = "http://127.0.0.1:8080";

    public static void main(String[] args) {
        initiate(InitiateForensicsDTO.builder()
                .address("https://www.baidu.com")
                .appForensicsNum("20220906359300bec")
                .forensicsName("测试网页取证")
                .forensicsInitiateTime(new Date())
                .appCode("filoink-open")
                .build());
        checkCapacity("filoink-open", 1);
        getResult("filoink-open", "20220906359300bec");
    }


    /**
     * 调用网页取证服务 node
     */
    public static String initiate(InitiateForensicsDTO initiateForensicsDTO) {

        String res = "";
        try {

            RestTemplate restTemplate = new RestTemplate();
            List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
            httpMessageConverters.forEach(httpMessageConverter -> {
                if (httpMessageConverter instanceof StringHttpMessageConverter) {
                    StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) httpMessageConverter;
                    messageConverter.setDefaultCharset(StandardCharsets.UTF_8);
                }
            });
            Map<String, Object> paramMap = new HashMap<String, Object>() {{
                put("address", initiateForensicsDTO.getAddress());
                put("appForensicsNum", initiateForensicsDTO.getAppForensicsNum());
                put("forensicsName", initiateForensicsDTO.getForensicsName());
                put("forensicsInitiateTime", initiateForensicsDTO.getForensicsInitiateTime());
                put("appCode", initiateForensicsDTO.getAppCode());
            }};
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(paramMap), headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    BASE_URL + INITIATE_URL, httpEntity, String.class);
            res = responseEntity.getBody();

            log.info("网页取证调用结果:{}", res);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * 检测任务队列能否容纳这么多任务
     *
     * @param tasksNumber
     */
    public static void checkCapacity(String appCode, Integer tasksNumber) {
        String res = "";
        try {
            RestTemplate restTemplate = new RestTemplate();
            List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
            httpMessageConverters.forEach(httpMessageConverter -> {
                if (httpMessageConverter instanceof StringHttpMessageConverter) {
                    StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) httpMessageConverter;
                    messageConverter.setDefaultCharset(StandardCharsets.UTF_8);
                }
            });
            Map<String, Object> paramMap = new HashMap<String, Object>() {{
                put("tasksNumber", tasksNumber);
                put("appCode", appCode);
            }};
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);

//            ResponseEntity<String> responseEntity = restTemplate.getForEntity(BASE_URL + CHECK_CAPACITY_URL + "?"
//                            + "tasksNumber={tasksNumber}" + "&"
//                            + "appCode={appCode}"
//                    , String.class, paramMap);
            ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_URL + CHECK_CAPACITY_URL + "?"
                            + "tasksNumber={tasksNumber}" + "&"
                            + "appCode={appCode}",
                    HttpMethod.GET, httpEntity, String.class, paramMap);
            res = responseEntity.getBody();
            log.info("网页取证调用结果:{}", res);

        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("网页取证容量检测服务,调用结果:{}", res);
    }

    /**
     * 获得结果
     *
     * @param forensicId
     */
    public static void getResult(String appCode, String forensicId) {

        String res = "";
        try {
            RestTemplate restTemplate = new RestTemplate();
            List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
            httpMessageConverters.forEach(httpMessageConverter -> {
                if (httpMessageConverter instanceof StringHttpMessageConverter) {
                    StringHttpMessageConverter messageConverter = (StringHttpMessageConverter) httpMessageConverter;
                    messageConverter.setDefaultCharset(StandardCharsets.UTF_8);
                }
            });
            Map<String, Object> paramMap = new HashMap<String, Object>() {{
                put("appForensicsNum", forensicId);
                put("appCode", appCode);
            }};
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);

            //        restTemplate.getForEntity(BASE_URL + GET_RESULT_URL, String.class, paramMap);
            ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_URL + GET_RESULT_URL + "?"
                            + "appForensicsNum={appForensicsNum}" + "&"
                            + "appCode={appCode}",
                    HttpMethod.GET, httpEntity, String.class, paramMap);
            res = responseEntity.getBody();
            log.info("网页取证调用结果:{}", res);

        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("网页取证容量检测服务,调用结果:{}", res);

    }

}
