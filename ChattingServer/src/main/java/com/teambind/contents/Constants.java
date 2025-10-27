package com.teambind.contents;

public enum Constants {
	HTTP_SESSION_ID("HTTP_SESSION_ID");
	private String value;
	Constants(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}
