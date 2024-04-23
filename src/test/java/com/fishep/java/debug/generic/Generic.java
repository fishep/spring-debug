package com.fishep.java.debug.generic;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author fly.fei
 * @Date 2024/3/13 11:44
 * @Desc java 使用擦拭法实现的泛型，虚拟机对泛型其实一无所知，所有的工作都是编译器做的。
 * Java的泛型是由编译器在编译时实行的，编译器内部永远把所有类型T视为Object处理，但是，在需要转型的时候，编译器会根据T的类型自动为我们实行安全地强制转型。
 * 静态方法在编译时，确定类型， 所以java无法在静态属性和静态方法上使用泛型
 * 擦拭法决定了泛型<T>：
 * 不能是基本类型，例如：int；
 * 不能获取带泛型类型的Class，例如：Pair<String>.class；
 * 不能判断带泛型类型的类型，例如：x instanceof Pair<String>；
 * 不能实例化T类型，例如：new T()。
 **/
@Slf4j
public class Generic<String, T> extends GenericParent<T> implements GenericInterface<T> {

}
