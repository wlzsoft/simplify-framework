package vip.simplify.classload.entity;

public class TestEntity {
	private TestUser testUser;

	public void setTestUser(TestUser testUser) {
		this.testUser = testUser;
	}

	public TestUser getTestUser() {
		System.out.println(1156);
		return testUser;
	}
}
