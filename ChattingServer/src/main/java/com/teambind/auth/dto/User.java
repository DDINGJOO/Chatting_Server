package com.teambind.auth.dto;

import com.teambind.auth.entity.UserId;

import java.util.Objects;


public record User(
		UserId userId,
		String userName) {
	
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(userId, user.userId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(userId);
	}
}
