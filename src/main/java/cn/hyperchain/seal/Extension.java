package cn.hyperchain.seal;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2022-10-20 16:24
 **/
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