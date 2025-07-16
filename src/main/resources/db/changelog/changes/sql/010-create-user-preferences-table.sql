CREATE
    TABLE
        user_preferences(
            id BIGSERIAL PRIMARY KEY,
            user_id BIGINT NOT NULL,
            preference_key VARCHAR(100) NOT NULL,
            preference_value TEXT,
            preference_type VARCHAR(50),
            category VARCHAR(100),
            is_sensitive BOOLEAN DEFAULT FALSE,
            created_by VARCHAR(255),
            created_on TIMESTAMP,
            last_modified_by VARCHAR(255),
            last_modified_on TIMESTAMP,
            FOREIGN KEY(user_id) REFERENCES users(id) ON
            DELETE
                CASCADE,
                UNIQUE(
                    user_id,
                    preference_key
                )
        );

CREATE
    INDEX idx_user_preferences_user_id ON
    user_preferences(user_id);

CREATE
    INDEX idx_user_preferences_category ON
    user_preferences(
        user_id,
        category
    );
