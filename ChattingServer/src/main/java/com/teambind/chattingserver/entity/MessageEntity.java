package com.teambind.chattingserver.entity;

import com.teambind.common.config.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@NoArgsConstructor
@Getter
public class MessageEntity extends BaseEntity {
	
	@Column(name = "message_sequence", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long messageSequence;
	
	@Column(name = "user_name", nullable = false)
	private String username;
	
	@Column(name = "content", nullable = false)
	private String content;
	

	public MessageEntity(String username, String content) {
		this.username = username;
		this.content = content;
	}
	
	
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
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
				", createdAt=" + getCreatedAt() +
				", updatedAt=" + getUpdatedAt() +
				'}';
	}
	
	
}

