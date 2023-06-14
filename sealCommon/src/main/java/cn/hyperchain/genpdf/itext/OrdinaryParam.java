package cn.hyperchain.genpdf.itext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author :Libi
 * @version :1.0
 * @date :12/23/20 2:37 PM
 */
@Builder
@Data
@AllArgsConstructor
public class OrdinaryParam {
    private String eviId;
    /**
     * 证书编号
     */
    private String confirmationNo;
    /**
     * 所属主体 主体名称
     */
    private String subjectOrigin;
    /**
     * 所属主体标识 身份证号码
     */
    private String subjectIdentify;
    /**
     * 所属主体手机号码
     */
    private String subjectPhone;
    /**
     * 证据名称
     */
    private String name;
    /**
     * 存取证类型
     */
    private String type;
    /**
     * 文件大小
     */
    private String fileSize;
    /**
     * 存证时间
     */
    private String opTime;
    /**
     * 上链时间
     */
    private String chainTime;
    /**
     * 所属区块链
     */
    private String chainName;
    /**
     * 电子数据哈希
     */
    private String hash;
    /**
     * 交易哈希
     */
    private String txHash;
    /**
     * 保管机构 湖北省武汉市尚信公证处
     */
    private String custodian;


    public OrdinaryParam() {

    }

    public void setOpTime(String opTime) {
        this.opTime = opTime;
    }

    public void setChainTime(String chainTime) {
        this.chainTime = chainTime;
    }
}
