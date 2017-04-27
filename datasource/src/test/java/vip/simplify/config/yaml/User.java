package vip.simplify.config.yaml;

import java.util.List;

public class User {
    private String name;
    private int age;
    private List<?> testList;

    public User(String name, int age, List<?> testList) {
        this.name = name;
        this.age = age;
        this.testList = testList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

	public List<?> getTestList() {
		return testList;
	}

	public void setTestList(List<?> testList) {
		this.testList = testList;
	}

}