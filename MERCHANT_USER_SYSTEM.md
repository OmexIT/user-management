# Merchant User Management System

## Overview

The Merchant User Management System provides comprehensive support for multi-tenant merchant operations with hierarchical branch structures and granular permission controls. The system supports three primary user types: Customers, Merchants, and Portal/System administrators, with enhanced focus on merchant branch management.

## Core Architecture

### Database Schema

#### Users Table
- Primary user entity supporting all user types
- Links to Keycloak for authentication
- Contains basic user information (email, display name, status)

#### Merchant Branches Table
- Hierarchical structure supporting unlimited branch levels
- Self-referential parent-child relationships
- Supports various branch types (head office, regional, outlet)
- Tracks branch-specific contact information and management

#### Enhanced Merchant Users Table
- Links users to merchants with branch-specific assignments
- Flexible role-based permissions through role_id
- Three permission scopes:
  - MERCHANT_WIDE: Access across all merchant branches
  - BRANCH_ONLY: Access limited to specific branch
  - BRANCH_AND_BELOW: Access to assigned branch and all sub-branches

### Permission System

#### Role-Based Access Control
- Permissions assigned through roles rather than direct assignment
- Role-permission mapping through role_permissions table
- Supports reusable roles across different contexts

#### Branch-Aware Permissions
- Permissions evaluated based on branch hierarchy
- Recursive queries for efficient permission checks
- Context-aware permission evaluation

## Key Features

### Branch Hierarchy Management
- Create unlimited branch levels
- Manage branch relationships
- Query branch hierarchies efficiently
- Support for branch activation/deactivation

### User-Branch Assignments
- Assign users to specific branches
- Multiple merchant support per user
- Primary merchant designation
- Custom attributes for merchant-specific data

### Permission Evaluation
- Real-time permission checking
- Branch-context aware permissions
- Hierarchical permission inheritance
- Efficient SQL-based permission queries

## Core Services

### MerchantBranchService
Primary service for branch management operations:

- **createBranch()**: Create new branches
- **createSubBranch()**: Create child branches
- **getMerchantBranches()**: List all branches for merchant
- **getBranchHierarchy()**: Get complete branch hierarchy
- **getUserBranchPermissions()**: Get user permissions for specific branch
- **hasPermissionAtBranch()**: Check specific permission at branch level

### Implementation Highlights

#### Efficient SQL Queries
Uses PostgreSQL recursive CTEs for:
- Branch hierarchy traversal
- Permission inheritance evaluation
- Descendant branch identification

#### JDBC Template Usage
Direct JDBC queries for complex operations:
- Better performance for hierarchical queries
- Cleaner code for complex joins
- Reduced overhead for permission checking

## Permission Scopes

### MERCHANT_WIDE
- User has access to all branches within the merchant
- Typically used for merchant administrators
- Permissions apply across entire merchant organization

### BRANCH_ONLY
- User access limited to specific assigned branch
- No access to parent or child branches
- Used for branch-specific staff

### BRANCH_AND_BELOW
- User has access to assigned branch and all sub-branches
- Supports hierarchical management structures
- Used for regional managers and supervisors

## Database Relationships

```
merchants (1) -> (n) merchant_branches
         (1) -> (n) merchant_users

merchant_branches (1) -> (n) merchant_branches (self-referential)
                  (1) -> (n) merchant_users

users (1) -> (n) merchant_users

roles (1) -> (n) merchant_users
      (1) -> (n) role_permissions

permissions (1) -> (n) role_permissions
```

## Usage Examples

### Creating Branch Hierarchy
```java
// Create head office
MerchantBranch headOffice = merchantBranchService.createBranch(
    merchantId, "HEAD", "Head Office", "HEAD_OFFICE");

// Create regional branch
MerchantBranch regional = merchantBranchService.createSubBranch(
    merchantId, headOffice.getId(), "REG001", "Regional Office North", "REGIONAL");

// Create outlet under regional
MerchantBranch outlet = merchantBranchService.createSubBranch(
    merchantId, regional.getId(), "OUT001", "Downtown Store", "OUTLET");
```

### Permission Checking
```java
// Check if user has permission at specific branch
boolean hasAccess = merchantBranchService.hasPermissionAtBranch(
    userId, merchantId, branchId, "SALES", "CREATE");

// Get all permissions for user at branch (considers hierarchy)
List<Permission> permissions = merchantBranchService.getUserBranchPermissions(
    userId, merchantId, branchId);
```

## Security Integration

### Keycloak Integration
- Users synchronized with Keycloak
- Authentication handled by Keycloak
- Local authorization using branch-aware permissions

### Branch-Level Security
- All operations check branch access
- Permission context includes branch information
- Hierarchical permission inheritance

## Performance Considerations

### Optimized Queries
- Uses PostgreSQL recursive CTEs for hierarchy traversal
- Indexed foreign key relationships
- Efficient permission checking with single queries

### Caching Strategy
- Branch hierarchy can be cached for performance
- Permission results suitable for caching
- Invalidation on branch structure changes

## Extensibility

### Custom Attributes
- Merchant users support custom attributes
- Branch-specific configuration storage
- Flexible metadata handling

### Additional Permission Scopes
- Architecture supports additional permission scopes
- Easy extension for new access patterns
- Configurable scope behaviors

## Migration Strategy

### Database Updates
- Clean schema definition without backward compatibility concerns
- Optimized indexes for query performance
- Foreign key constraints for data integrity

### Future Enhancements
- API endpoints for branch management
- UI components for branch hierarchy visualization
- Bulk operations for branch management
- Advanced permission reporting
