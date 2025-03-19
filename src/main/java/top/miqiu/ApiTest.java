package top.miqiu;

import org.junit.Test;

import java.io.IOException;

public class ApiTest {
        @Test
        public void test_BeanFactory() throws IOException {
            //1.创建bean工厂(同时完成了加载资源、创建注册单例bean注册器的操作)
            BeanFactory beanFactory = new BeanFactory();
    
            //2.第一次获取bean（通过反射创建bean，缓存bean）
            UserDao userDao1 = (UserDao) beanFactory.getBean("userDao");
            userDao1.queryUserInfo();
    
            //3.第二次获取bean（从缓存中获取bean）
            UserDao userDao2 = (UserDao) beanFactory.getBean("userDao");
            userDao2.queryUserInfo();

            hello hello = (hello) beanFactory.getBean("hello");
            hello.test();
        }
    }
