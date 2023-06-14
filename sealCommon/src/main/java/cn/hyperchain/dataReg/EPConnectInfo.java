package cn.hyperchain.dataReg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2023-04-13 15:40
 **/
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EPConnectInfo {
    /**
     * 平台名称
     */
    private String platName;
    /**
     * 接口地址
     */
    private String url;
    /**
     * 接口ak
     */
    private String accessKey;
    /**
     * 接口as
     */
    private String accessSecret;
}
