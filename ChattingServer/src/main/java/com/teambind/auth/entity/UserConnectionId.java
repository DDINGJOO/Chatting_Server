package com.teambind.auth.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class UserConnectionId  implements Serializable {
	
	private Long partnerUserAId;
	private Long partnerUserBId;
	
	
}
