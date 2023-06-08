package cn.hyperchain.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @program: hook-open
 * @description:
 * @author: inkChain
 * @create: 2022-12-06 21:37
 **/
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InitiateForensicsDTO {
    /**
     * 取证网址
     */
    @NotEmpty(message = "取证网址不能为空")
    private String address;
    /**
     * 应用取证编号 和app_id联合保证唯一
     */
    @NotEmpty(message = "应用取证编号不能为空")
    private String appForensicsNum;
    /**
     * 取证名称
     */
    @NotEmpty(message = "取证名称不能为空")
    private String forensicsName;
    /**
     * 取证发起时间
     */
    @NotNull(message = "取证发起时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date forensicsInitiateTime;
    /**
     * 应用code
     */
    @NotEmpty(message = "应用code不能为空")
    private String appCode;
}
