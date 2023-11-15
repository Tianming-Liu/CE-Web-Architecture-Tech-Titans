package com.czandlh.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * spring boot 容器加载后自动监听
 */
@Component
public class MyCommandRunner implements CommandLineRunner {

    private String loginUrl = "http://127.0.0.1:8080";

    private boolean isOpen = true;

    @Override
    public void run(String... args) {
        if (isOpen) {
            System.out.println("自动加载指定的页面");
            try {
                Runtime.getRuntime().exec("cmd /c start " + loginUrl);  // 可以指定自己的路径
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("浏览器打开页面异常");
            }
        }
    }

}

