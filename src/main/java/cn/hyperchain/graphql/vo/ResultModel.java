package cn.hyperchain.graphql.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2023-02-22 15:02
 **/
@AllArgsConstructor
@Data
public class ResultModel {
    private int code;
    private String msg;
    private String ecxp;
}
