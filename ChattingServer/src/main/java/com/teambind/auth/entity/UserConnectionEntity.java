package com.teambind.auth.entity;

import com.teambind.common.config.baseEntity.BaseEntity;
import com.teambind.constant.UserConnectionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "user_connection")
@IdClass(UserConnectionId.class)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserConnectionEntity extends BaseEntity {
	
	@Id
	@Column(name = "partner_user_a_id", nullable = false)
	private Long partnerUserAId;
	
	@Id
	@Column(name = "partner_user_b_id", nullable = false)
	private Long partnerUserBId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private UserConnectionStatus status;
	
	@Column(name = "inviter_user_id", nullable = false)
	private Long invitorUserId;
	
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		UserConnectionEntity that = (UserConnectionEntity) o;
		return Objects.equals(partnerUserAId, that.partnerUserAId) && Objects.equals(partnerUserBId, that.partnerUserBId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(partnerUserAId, partnerUserBId);
	}
}
