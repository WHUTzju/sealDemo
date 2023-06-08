package cn.hyperchain.abs;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @program: sealDemo
 * @description:
 * @author: inkChain
 * @create: 2022-12-01 14:49
 **/
public class BaseMain {

//    public static void main(String[] args) {
//        Base00 b1 = new Base01();
//        Base00 b2 = new Base02();
//        System.out.println("张三：" + b1.getName("张三"));
//        b1.setName("张四");
//        System.out.println("张三：" + b1.getName("张三"));
//        System.out.println("陈三：" + b2.getName("陈四"));
//        System.out.println("陈四：" + b2.getName("陈五"));
//        System.out.println("陈六：" + b2.getName("陈六"));
//    }


    public static void main(String[] args) {
        List<Data> list=new ArrayList<Data>(){
            {
                add(Data.builder().wonTime(5).build());
                add(Data.builder().wonTime(1).build());
                add(Data.builder().wonTime(7).build());
                add(Data.builder().wonTime(10).build());
                add(Data.builder().wonTime(2).build());
                add(Data.builder().wonTime(23).build());
            }
        };
        System.out.println(JSONObject.toJSONString(list));
        list.sort(Comparator.comparing(Data::getWonTime));
        System.out.println(JSONObject.toJSONString(list));

    }


}
