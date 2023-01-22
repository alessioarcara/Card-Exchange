package com.aadm.cardexchange.client.utils;

public class IgnoreAsyncCallback<T> extends BaseAsyncCallback<T> {
    @Override
    public void onSuccess(T result) {
    }
}
