package com.zw.test;

import com.zw.test.proxy.Interceptor;
import com.zw.test.proxy.ProxyFactory;
import com.zw.test.proxy.JDKProxyGenerator;

import java.lang.reflect.Method;

public class TestProxy implements TestProxyInterface {
    @Override
    public void test() {
        System.out.println(" == test() ==");
    }

    @Override
    public void sysHello() {
        System.out.println(" == sysHello() ==");
    }

    public static void main(String[] args) {
        TestProxy testProxy = new TestProxy();
        JDKProxyGenerator xxProxy = new JDKProxyGenerator(testProxy, new Interceptor() {
            @Override
            public void onPreExecute(Method method, Object[] args) {
                System.out.println(" onPreExecute " + method.getName());
            }

            @Override
            public void onAfterExecute(Method method, Object[] args) {
                System.out.println(" onAfterExecute " + method.getName());
            }
        });

        /**
         * xxProxy.getProxy()获取的是TestProxy的代理对象
         */
        TestProxyInterface testProxyInterface = xxProxy.getProxy();
        /**
         * 调用代理对象的方法 会触发InvocationHandler 里面的invoke方法
         * 再在invoke里面 执行被代理对象的方法
         * 在调用被代理对象方法之前和之后 可以做出相应的处理
         * 从而可以实现AOP的功能
         */
        testProxyInterface.test();
        testProxyInterface.sysHello();

        System.out.println("******************************");

        TestProxyInterface pf = ProxyFactory.getInstance().getProxy(testProxy, new Interceptor() {
            @Override
            public void onPreExecute(Method method, Object[] args) {
                System.out.println(" onPreExecute " + method.getName());
            }

            @Override
            public void onAfterExecute(Method method, Object[] args) {
                System.out.println(" onAfterExecute " + method.getName());
            }
        });

        pf.sysHello();
        pf.test();
    }
}
