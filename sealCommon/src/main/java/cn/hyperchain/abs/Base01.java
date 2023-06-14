package cn.hyperchain.abs;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2022-12-01 14:47
 **/
public class Base01 extends Base00 {
    @Override
    String getName(String name) {
        String oriname = this.name;
        this.name = name;
        return oriname;
    }

    @Override
    String getName() {
        return this.name;
    }
}
