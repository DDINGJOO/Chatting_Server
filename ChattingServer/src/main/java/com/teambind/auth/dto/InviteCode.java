package com.teambind.auth.dto;

public record InviteCode(
		String code
) {
	public InviteCode{
		if(code == null || code.isEmpty())
		{
			throw new IllegalArgumentException("invalid invite code");
		}
	}
}
