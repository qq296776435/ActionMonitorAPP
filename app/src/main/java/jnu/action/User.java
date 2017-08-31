package jnu.action;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class User extends DataSupport{
    @Column(unique=true)
    private String uid;

    @Column
    private boolean male;
    private int age;
    private float height;
    private float weight;

    public User(String uid, boolean male, int age, float height, float weight) {
        this.uid = uid;
        this.male = male;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
