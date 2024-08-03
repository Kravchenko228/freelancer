package com.freelancer;

public class DataModel<T> {
    private T data;

    public DataModel(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
