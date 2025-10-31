package com.teambind.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

public record InviteCode(
		@JsonValue String code
) {
	@JsonCreator
	public InviteCode{
		if(code == null || code.isEmpty())
		{
			throw new IllegalArgumentException("invalid invite code");
		}
	}
}
