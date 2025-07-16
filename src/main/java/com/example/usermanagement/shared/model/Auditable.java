package com.example.usermanagement.shared.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.time.Instant;
import java.util.Collection;

@Getter
@Setter
public class Auditable<U> {
	@Column("created_by")
	@CreatedBy
	private U createdBy;

	@Column("created_on")
	@CreatedDate
	private Instant createdOn;

	@Column("last_modified_by")
	@LastModifiedBy
	private U lastModifiedBy;

	@Column("last_modified_on")
	@LastModifiedDate
	private Instant lastModifiedOn;

	/**
	 * Sets or updates the audit information for this entity. If createdOn is null,
	 * this is considered a new entity and creation details are also set.
	 *
	 * @param auditor
	 *            the user performing the action
	 */
	public void setAuditInfo(U auditor) {
		Instant now = Instant.now();

		if (this.createdOn == null) {
			this.createdOn = now;
			this.createdBy = auditor;
		}

		this.lastModifiedOn = now;
		this.lastModifiedBy = auditor;
	}

	/**
	 * Recursively applies audit information to this entity and all its auditable
	 * child collections.
	 *
	 * @param auditor
	 *            the user performing the action
	 * @param auditableCollections
	 *            collections of Auditable entities to process
	 */
	@SafeVarargs
	public final void applyAuditInfoRecursively(U auditor, Collection<? extends Auditable<U>>... auditableCollections) {
		// Set audit info for this entity
		setAuditInfo(auditor);

		// Set audit info for all child collections
		for (Collection<? extends Auditable<U>> collection : auditableCollections) {
			if (collection != null) {
				for (Auditable<U> item : collection) {
					if (item != null) {
						item.setAuditInfo(auditor);
					}
				}
			}
		}
	}
}
