package com.zw.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 通过 Proxy.newProxyInstance 创建的代理对象，
 * 是在jvm运行时动态生成的一个对象，是在运行时动态生成的一个对象，
 * 并且命名方式都是以$开头，Proxy为中，最后一个数字表示对象的标号 [$Proxy0]。
 */
public class JDKProxyGenerator extends ProxyGenerator {

    public JDKProxyGenerator(Object subject, Interceptor interceptor) {
        super(subject, interceptor);
    }

    public <T> T getProxy() {
        T result = null;
        Class clazz = subject.getClass();
        Class[] interfaces = clazz.getInterfaces();
        if (null != interfaces && interfaces.length > 0) {
            result = (T) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, new XXInvocationHandler());
        }
        return result;
    }

    private class XXInvocationHandler implements InvocationHandler {

        @Override
        /**
         * proxy: subject 的代理对象
         * 该参数的作用：
         *     1. 可以使用反射获取代理对象的信息（也就是proxy.getClass().getName()）。
         *     2. 可以将代理对象返回以进行连续调用，这就是proxy存在的目的。因为this并不是代理对象
         *     注意：在invoke方法中使用proxy调用方法的时需要注意，容易引起方法的循环调用[循环：invoke中调用代理的方法->invoke->invoke中调用代理的方法]
         * method：被调用的方法
         * args：调用方法传进来的参数
         */
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (interceptor != null) {
                interceptor.onPreExecute(method, args);
            }

            method.invoke(subject, args);

            if (null != interceptor) {
                interceptor.onAfterExecute(method, args);
            }

            return proxy;
        }
    }
}
