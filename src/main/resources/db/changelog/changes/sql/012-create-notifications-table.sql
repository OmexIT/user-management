CREATE
    TABLE
        notifications(
            id BIGSERIAL PRIMARY KEY,
            user_id BIGINT NOT NULL,
            notification_type VARCHAR(100) NOT NULL,
            channel VARCHAR(50) NOT NULL,
            subject VARCHAR(500),
            content TEXT NOT NULL,
            is_read BOOLEAN DEFAULT FALSE,
            read_at TIMESTAMP,
            is_sent BOOLEAN DEFAULT FALSE,
            sent_at TIMESTAMP,
            retry_count INTEGER DEFAULT 0,
            error_message TEXT,
            metadata TEXT,
            expires_at TIMESTAMP NOT NULL,
            created_by VARCHAR(255),
            created_on TIMESTAMP,
            last_modified_by VARCHAR(255),
            last_modified_on TIMESTAMP,
            FOREIGN KEY(user_id) REFERENCES users(id) ON
            DELETE
                CASCADE
        );

CREATE
    INDEX idx_notifications_user_id ON
    notifications(user_id);

CREATE
    INDEX idx_notifications_is_read ON
    notifications(
        user_id,
        is_read
    );

CREATE
    INDEX idx_notifications_type ON
    notifications(
        user_id,
        notification_type
    );

CREATE
    INDEX idx_notifications_expires_at ON
    notifications(expires_at);
