package cn.hyperchain.abs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2023-04-20 13:38
 **/

@Builder
@NoArgsConstructor
@AllArgsConstructor
@lombok.Data
public class Data {
    private Integer id;
    private Integer wonTime;
}
