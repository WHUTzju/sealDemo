package cn.hyperchain.abs;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2022-12-01 14:45
 **/
public abstract class Base00 {
    protected String name;
    protected String age;

    abstract String getName(String name);

    protected String setName(String name) {
        this.name = name;
        return name;
    }

    abstract String getName();
}
