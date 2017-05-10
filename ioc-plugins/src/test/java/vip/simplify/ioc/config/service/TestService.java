package vip.simplify.ioc.config.service;

public class TestService {
    private TestSubService testSubService;

    public String getName() {
        testSubService.printResult();
        return "testlcy";
    }
}
