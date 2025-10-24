package com.teambind.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "message_user")
@NoArgsConstructor
@Getter
public class MessageUserEntity {
	
	@Column(name = "user_id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long userId;
	
	@Column(name = "username", nullable = false)
	private String username;
	
	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	public MessageUserEntity(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	
	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = this.createdAt;
	}
	
	@PreUpdate
	public void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}
	

	


	
	@Override
	public String toString() {
		return "MessageEntity{" +
				"userId=" + userId +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				'}';
	}
	
	
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		MessageUserEntity that = (MessageUserEntity) o;
		return Objects.equals(userId, that.userId);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(userId);
	}
}

