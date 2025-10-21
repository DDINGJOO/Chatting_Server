package com.teambind.chattingserver.entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@NoArgsConstructor
@Getter
public class MessageEntity {
	
	@Column(name = "message_sequence", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long messageSequence;
	
	@Column(name = "user_name", nullable = false)
	private String username;
	
	@Column(name = "content", nullable = false)
	private String content;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	public MessageEntity(String username, String content) {
		this.username = username;
		this.content = content;
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
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		MessageEntity that = (MessageEntity) o;
		return messageSequence.equals(that.messageSequence);
	}
	
	@Override
	public int hashCode() {
		return messageSequence.hashCode();
	}
	
	@Override
	public String toString() {
		return "MessageEntity{" +
				"messageSequence=" + messageSequence +
				", username='" + username + '\'' +
				", content='" + content + '\'' +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt +
				'}';
	}
	
	

}

