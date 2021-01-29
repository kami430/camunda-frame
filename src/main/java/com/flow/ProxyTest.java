package com.flow;

public interface ProxyTest {

    @SqlTest("${sql}")
    String bilibili(@SqlParam("sql") String sql);
}
