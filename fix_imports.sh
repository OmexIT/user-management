#!/bin/bash

# Script to fix package declarations and imports after restructuring

echo "Fixing package declarations and imports..."

# Fix customer module
echo "Fixing customer module..."
find src/main/java/com/example/usermanagement/customer -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.model;/package com.example.usermanagement.customer.model;/g' {} \;
find src/main/java/com/example/usermanagement/customer -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.service;/package com.example.usermanagement.customer.service;/g' {} \;
find src/main/java/com/example/usermanagement/customer -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.controller;/package com.example.usermanagement.customer.controller;/g' {} \;
find src/main/java/com/example/usermanagement/customer -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.repository;/package com.example.usermanagement.customer.repository;/g' {} \;
find src/main/java/com/example/usermanagement/customer -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.dto;/package com.example.usermanagement.customer.dto;/g' {} \;

# Fix merchant module
echo "Fixing merchant module..."
find src/main/java/com/example/usermanagement/merchant -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.model;/package com.example.usermanagement.merchant.model;/g' {} \;
find src/main/java/com/example/usermanagement/merchant -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.service;/package com.example.usermanagement.merchant.service;/g' {} \;
find src/main/java/com/example/usermanagement/merchant -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.controller;/package com.example.usermanagement.merchant.controller;/g' {} \;
find src/main/java/com/example/usermanagement/merchant -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.repository;/package com.example.usermanagement.merchant.repository;/g' {} \;
find src/main/java/com/example/usermanagement/merchant -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.dto;/package com.example.usermanagement.merchant.dto;/g' {} \;

# Fix user module
echo "Fixing user module..."
find src/main/java/com/example/usermanagement/user -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.model;/package com.example.usermanagement.user.model;/g' {} \;
find src/main/java/com/example/usermanagement/user -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.service;/package com.example.usermanagement.user.service;/g' {} \;
find src/main/java/com/example/usermanagement/user -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.controller;/package com.example.usermanagement.user.controller;/g' {} \;
find src/main/java/com/example/usermanagement/user -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.repository;/package com.example.usermanagement.user.repository;/g' {} \;
find src/main/java/com/example/usermanagement/user -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.dto;/package com.example.usermanagement.user.dto;/g' {} \;
find src/main/java/com/example/usermanagement/user -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.mapper;/package com.example.usermanagement.user.mapper;/g' {} \;

# Fix shared module
echo "Fixing shared module..."
find src/main/java/com/example/usermanagement/shared -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.model;/package com.example.usermanagement.shared.model;/g' {} \;
find src/main/java/com/example/usermanagement/shared -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.service;/package com.example.usermanagement.shared.service;/g' {} \;
find src/main/java/com/example/usermanagement/shared -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.security;/package com.example.usermanagement.shared.security;/g' {} \;
find src/main/java/com/example/usermanagement/shared -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.exception;/package com.example.usermanagement.shared.exception;/g' {} \;
find src/main/java/com/example/usermanagement/shared -name "*.java" -exec sed -i '' 's/package com\.example\.usermanagement\.repository;/package com.example.usermanagement.shared.repository;/g' {} \;

# Fix imports across all files
echo "Fixing imports..."

# Fix imports for moved model classes
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.Customer;/import com.example.usermanagement.customer.model.Customer;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.Merchant;/import com.example.usermanagement.merchant.model.Merchant;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.MerchantUser;/import com.example.usermanagement.merchant.model.MerchantUser;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.MerchantBranch;/import com.example.usermanagement.merchant.model.MerchantBranch;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.User;/import com.example.usermanagement.user.model.User;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.PortalUser;/import com.example.usermanagement.user.model.PortalUser;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.Permission;/import com.example.usermanagement.user.model.Permission;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.Role;/import com.example.usermanagement.user.model.Role;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.RolePermission;/import com.example.usermanagement.user.model.RolePermission;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.UserPreference;/import com.example.usermanagement.user.model.UserPreference;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.UserSession;/import com.example.usermanagement.user.model.UserSession;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.UserRole;/import com.example.usermanagement.user.model.UserRole;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.Auditable;/import com.example.usermanagement.shared.model.Auditable;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.AuditLog;/import com.example.usermanagement.shared.model.AuditLog;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.Notification;/import com.example.usermanagement.shared.model.Notification;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.model\.PolicyRule;/import com.example.usermanagement.shared.model.PolicyRule;/g' {} \;

# Fix imports for moved service classes
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.service\.CustomerService;/import com.example.usermanagement.customer.service.CustomerService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.service\.MerchantService;/import com.example.usermanagement.merchant.service.MerchantService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.service\.MerchantBranchService;/import com.example.usermanagement.merchant.service.MerchantBranchService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.service\.UserSynchronizationService;/import com.example.usermanagement.user.service.UserSynchronizationService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.service\.KeycloakRoleService;/import com.example.usermanagement.user.service.KeycloakRoleService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.service\.PermissionService;/import com.example.usermanagement.user.service.PermissionService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.service\.SessionService;/import com.example.usermanagement.user.service.SessionService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.service\.UserPreferenceService;/import com.example.usermanagement.user.service.UserPreferenceService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.service\.PermissionMappingService;/import com.example.usermanagement.user.service.PermissionMappingService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.service\.UserContextService;/import com.example.usermanagement.user.service.UserContextService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.service\.AuditService;/import com.example.usermanagement.shared.service.AuditService;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.service\.NotificationService;/import com.example.usermanagement.shared.service.NotificationService;/g' {} \;

# Fix imports for moved repository classes
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.repository\.CustomerRepository;/import com.example.usermanagement.customer.repository.CustomerRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.repository\.MerchantRepository;/import com.example.usermanagement.merchant.repository.MerchantRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.repository\.MerchantBranchRepository;/import com.example.usermanagement.merchant.repository.MerchantBranchRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.repository\.MerchantUserRepository;/import com.example.usermanagement.merchant.repository.MerchantUserRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.repository\.UserRepository;/import com.example.usermanagement.user.repository.UserRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.repository\.PortalUserRepository;/import com.example.usermanagement.user.repository.PortalUserRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.repository\.PermissionRepository;/import com.example.usermanagement.user.repository.PermissionRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.repository\.RoleRepository;/import com.example.usermanagement.user.repository.RoleRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.repository\.UserSessionRepository;/import com.example.usermanagement.user.repository.UserSessionRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.repository\.UserPreferenceRepository;/import com.example.usermanagement.user.repository.UserPreferenceRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.repository\.AuditLogRepository;/import com.example.usermanagement.shared.repository.AuditLogRepository;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.repository\.NotificationRepository;/import com.example.usermanagement.shared.repository.NotificationRepository;/g' {} \;

# Fix imports for moved DTOs
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.dto\.CustomerDto;/import com.example.usermanagement.customer.dto.CustomerDto;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.dto\.MerchantDto;/import com.example.usermanagement.merchant.dto.MerchantDto;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.dto\.MerchantUserDto;/import com.example.usermanagement.merchant.dto.MerchantUserDto;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.dto\.UserDto;/import com.example.usermanagement.user.dto.UserDto;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.dto\.PermissionDto;/import com.example.usermanagement.user.dto.PermissionDto;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.dto\.RoleDto;/import com.example.usermanagement.user.dto.RoleDto;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.dto\.PermissionCheckDto;/import com.example.usermanagement.user.dto.PermissionCheckDto;/g' {} \;

# Fix imports for moved mappers
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.mapper\.UserMapper;/import com.example.usermanagement.user.mapper.UserMapper;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.mapper\.RoleMapper;/import com.example.usermanagement.user.mapper.RoleMapper;/g' {} \;

# Fix imports for moved security classes
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.security\.UserPrincipal;/import com.example.usermanagement.shared.security.UserPrincipal;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.security\.KeycloakJwtAuthenticationConverter;/import com.example.usermanagement.shared.security.KeycloakJwtAuthenticationConverter;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.security\.KeycloakJwtAuthenticationToken;/import com.example.usermanagement.shared.security.KeycloakJwtAuthenticationToken;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.security\.DynamicPermissionEvaluator;/import com.example.usermanagement.shared.security.DynamicPermissionEvaluator;/g' {} \;

# Fix imports for moved exception classes
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.exception\.EntityNotFoundException;/import com.example.usermanagement.shared.exception.EntityNotFoundException;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.exception\.EntityExistsException;/import com.example.usermanagement.shared.exception.EntityExistsException;/g' {} \;
find src/main/java -name "*.java" -exec sed -i '' 's/import com\.example\.usermanagement\.exception\.KeycloakIntegrationException;/import com.example.usermanagement.shared.exception.KeycloakIntegrationException;/g' {} \;

echo "Import fixes completed!"
