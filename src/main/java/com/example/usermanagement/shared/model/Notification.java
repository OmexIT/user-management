package com.example.usermanagement.shared.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification extends Auditable<String> {

	@Id
	private Long id;

	@Column("user_id")
	private Long userId;

	@Column("notification_type")
	private String notificationType;

	private String channel;

	private String subject;

	private String content;

	@Column("is_read")
	private Boolean isRead = false;

	@Column("read_at")
	private LocalDateTime readAt;

	@Column("is_sent")
	private Boolean isSent = false;

	@Column("sent_at")
	private LocalDateTime sentAt;

	@Column("retry_count")
	private Integer retryCount = 0;

	@Column("error_message")
	private String errorMessage;

	private String metadata;

	@Column("expires_at")
	private LocalDateTime expiresAt;

	public Notification(Long userId, String notificationType, String channel, String subject, String content) {
		this.userId = userId;
		this.notificationType = notificationType;
		this.channel = channel;
		this.subject = subject;
		this.content = content;
		this.expiresAt = LocalDateTime.now().plusDays(30);
	}

	public void markAsRead() {
		this.isRead = true;
		this.readAt = LocalDateTime.now();
	}

	public void markAsSent() {
		this.isSent = true;
		this.sentAt = LocalDateTime.now();
	}
}
