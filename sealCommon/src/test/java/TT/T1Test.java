package TT;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2022-12-08 20:25
 **/
public class T1Test {

    public static void main(String[] args) {
        System.out.println(-1L << 5L);
        System.out.println(~(-1L << 5L));
        System.out.println(-1L ^ (-1L << 5L));
    }
}
