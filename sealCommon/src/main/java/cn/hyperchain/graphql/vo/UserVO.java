package cn.hyperchain.graphql.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2023-02-22 14:42
 **/
@AllArgsConstructor
@Data
public class UserVO {
    private Long userId;
    private String userName;
    private int age;
}
