package com.example;

/**
 * Created by on 26.07.16.
 *
 * @author David Steiman
 */
public class Foo {
    private long id;

    private String value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Foo() {
    }

    public Foo(long id, String value) {
        this.id = id;
        this.value = value;
    }
}
