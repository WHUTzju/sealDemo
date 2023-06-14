package cn.hyperchain.sealpdf;

import lombok.Data;

import java.security.PrivateKey;
import java.security.cert.Certificate;

/**
 * @program: dorado-EviBackend
 * @description:
 * @author: inkChain
 * @create: 2022-10-20 11:11
 **/
@Data
public class CertEntity {

    private PrivateKey privateKey;
    private Certificate[] certificateChain;
}
