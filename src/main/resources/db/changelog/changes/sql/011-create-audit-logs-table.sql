CREATE
    TABLE
        audit_logs(
            id BIGSERIAL PRIMARY KEY,
            user_id BIGINT,
            entity_type VARCHAR(100) NOT NULL,
            entity_id VARCHAR(255) NOT NULL,
            ACTION VARCHAR(100) NOT NULL,
            old_value TEXT,
            new_value TEXT,
            ip_address VARCHAR(50),
            user_agent TEXT,
            performed_at TIMESTAMP NOT NULL,
            performed_by VARCHAR(255) NOT NULL,
            context TEXT
        );

CREATE
    INDEX idx_audit_logs_user_id ON
    audit_logs(user_id);

CREATE
    INDEX idx_audit_logs_entity ON
    audit_logs(
        entity_type,
        entity_id
    );

CREATE
    INDEX idx_audit_logs_action ON
    audit_logs(ACTION);

CREATE
    INDEX idx_audit_logs_performed_at ON
    audit_logs(performed_at);

CREATE
    INDEX idx_audit_logs_performed_by ON
    audit_logs(performed_by);
