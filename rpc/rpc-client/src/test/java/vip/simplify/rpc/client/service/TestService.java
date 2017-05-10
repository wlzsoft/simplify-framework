package vip.simplify.rpc.client.service;

public class TestService {
    private TestSubService testSubService;

    public String getName() {
        testSubService.printResult();
        return "testlcy";
    }
}
