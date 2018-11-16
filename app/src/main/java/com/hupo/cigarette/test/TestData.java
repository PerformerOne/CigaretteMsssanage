package com.hupo.cigarette.test;


import java.util.ArrayList;
import java.util.List;

public class TestData {

    /**
     * 返回测试数据
     *
     * @return
     */
    public static List<String> getTestData() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i <8; i++) {
            data.add(String.valueOf(i));
        }
        return data;
    }


    /**
     * 返回测试数据
     *
     * @return
     */
    public static List<String> getTestData2() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 2; i++) {
            data.add(String.valueOf(i));
        }
        return data;
    }

    /**
     * 返回测试数据
     *
     * @return
     */
    public static List<String> getTestData3() {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < 8; i++) {
            data.add(String.valueOf(i));
        }
        return data;
    }

    /**
     * 返回测试数据
     *
     * @return
     */
    public static List<String> getTestData(int n) {
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < n; i++) {
            data.add(String.valueOf(i));
        }
        return data;
    }
}
