package cn.hyperchain.dataReg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: iprp
 * @description: 第三方存证返回值
 * @author: inkChain
 * @create: 2023-04-19 15:04
 **/
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EPEviInfo {

    /**
     * 数据存证编号
     */
    private String certNo;

    /**
     * 存证主体名称
     */
    private String certSubjectName;

    /**
     * 存证主体编码（身份证号or统一社会信用代码）
     */
    private String certSubjectNum;
    /**
     * 哈希算法
     * 例如：SHA-256、MD5等
     */
    private String certHashMethod;

    /**
     * 存证
     */
    private String certHash;

    /**
     * 数据结构
     * 数据结构，以K-V形式将数据结构转换成Json字符串；用于审核时与登记填写时示例保持一致；例如：
     * [{"fieldName":"日期","exampleValue":"2022/01/02"},{"fieldName":"地区","exampleValue":"浙江省杭州市"}]
     */
    private String certField;

    /**
     * 数据规模
     * 数据规模（即数据条数），例如：12000000
     */
    private String certNums;
}
