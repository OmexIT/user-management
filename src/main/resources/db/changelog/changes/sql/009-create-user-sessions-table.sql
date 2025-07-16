CREATE
    TABLE
        user_sessions(
            id BIGSERIAL PRIMARY KEY,
            user_id BIGINT NOT NULL,
            session_id VARCHAR(255) NOT NULL UNIQUE,
            ip_address VARCHAR(50),
            user_agent TEXT,
            device_info TEXT,
            started_at TIMESTAMP NOT NULL,
            last_activity_at TIMESTAMP NOT NULL,
            expires_at TIMESTAMP NOT NULL,
            is_active BOOLEAN DEFAULT TRUE,
            terminated_at TIMESTAMP,
            termination_reason TEXT,
            created_by VARCHAR(255),
            created_on TIMESTAMP,
            last_modified_by VARCHAR(255),
            last_modified_on TIMESTAMP,
            FOREIGN KEY(user_id) REFERENCES users(id) ON
            DELETE
                CASCADE
        );

CREATE
    INDEX idx_user_sessions_user_id ON
    user_sessions(user_id);

CREATE
    INDEX idx_user_sessions_session_id ON
    user_sessions(session_id);

CREATE
    INDEX idx_user_sessions_is_active ON
    user_sessions(is_active);

CREATE
    INDEX idx_user_sessions_expires_at ON
    user_sessions(expires_at);
