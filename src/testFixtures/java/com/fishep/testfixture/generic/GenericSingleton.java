package com.fishep.testfixture.generic;

/**
 * @Author fly.fei
 * @Date 2024/3/13 11:44
 * @Desc 泛型没有单例
 **/
public class GenericSingleton<T> {

    private T t;

    private static GenericSingleton instance;

    public static <T> GenericSingleton<T> getInstance() {
        if (instance == null) {
            synchronized (GenericSingleton.class) {
                if (instance == null) {
                    instance = new GenericSingleton<T>();
                }
            }
        }
        return instance;
    }

    private GenericSingleton() {
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

}
