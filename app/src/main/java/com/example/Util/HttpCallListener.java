package com.example.Util;

public interface HttpCallListener {

	public void onSuccess(String response);
	public void onFailed(String response);
}
