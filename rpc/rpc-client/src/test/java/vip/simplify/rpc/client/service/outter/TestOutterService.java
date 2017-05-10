package vip.simplify.rpc.client.service.outter;

public class TestOutterService {
    private TestOutterSubService testOutterSubService;

    public void setTestOutterSubService(TestOutterSubService testOutterSubService) {
        this.testOutterSubService = testOutterSubService;
    }

    public void test() {
        System.out.println(testOutterSubService.getName());
    }
}
