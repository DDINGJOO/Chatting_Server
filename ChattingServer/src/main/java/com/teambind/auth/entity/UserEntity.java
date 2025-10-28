package com.teambind.auth.entity;

import com.teambind.common.config.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "message_user")
@Getter
@Setter
@ToString
public class UserEntity extends BaseEntity {
	
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long userId;
	
	@Column(name = "username", nullable = false)
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name ="connection_invite_code", nullable = false)
	private String connectionInviteCode;
	
	@Column(name = "connection_count", nullable = false)
	private int connectionCount;
	
	protected UserEntity() {
		// JPA requires a no-arg constructor
	}
	
	public UserEntity(String username, String password) {
		this.username = username;
		this.password = password;
		this.connectionInviteCode = UUID.randomUUID().toString().replace("-", "");
	}
	
	


	
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		UserEntity that = (UserEntity) o;
		return Objects.equals(username, that.username);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(username);
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUsername() {
		return username;
	}
}


