package com.gerson.projectpath_pro.mappers;

public interface Mapper<A, B> {

    B mapTo(A a);

    A mapFrom(B b);

}
