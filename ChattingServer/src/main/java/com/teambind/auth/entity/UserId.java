package com.teambind.auth.entity;

public record UserId(
		Long id
) {
	public UserId {
		if(id == null || id < 0) {throw new IllegalArgumentException("invalid user id");}
	}
}
