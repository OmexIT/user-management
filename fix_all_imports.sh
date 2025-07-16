#!/bin/bash

# Comprehensive script to fix all imports after restructuring

echo "Fixing all missing imports..."

# Add missing imports for Auditable class
echo "Adding Auditable imports..."
find src/main/java -name "*.java" -type f -exec grep -l "extends Auditable" {} \; | while read file; do
    if ! grep -q "import com.example.usermanagement.shared.model.Auditable;" "$file"; then
        sed -i '' '/^package/a\
\
import com.example.usermanagement.shared.model.Auditable;' "$file"
    fi
done

# Add missing imports for MerchantUser in User model
echo "Adding MerchantUser imports..."
if [ -f "src/main/java/com/example/usermanagement/user/model/User.java" ]; then
    if ! grep -q "import com.example.usermanagement.merchant.model.MerchantUser;" "src/main/java/com/example/usermanagement/user/model/User.java"; then
        sed -i '' '/^package/a\
\
import com.example.usermanagement.merchant.model.MerchantUser;' "src/main/java/com/example/usermanagement/user/model/User.java"
    fi
fi

# Add missing imports for UserDto in merchant DTOs
echo "Adding UserDto imports to merchant DTOs..."
find src/main/java/com/example/usermanagement/merchant/dto -name "*.java" -type f -exec grep -l "UserDto" {} \; | while read file; do
    if ! grep -q "import com.example.usermanagement.user.dto.UserDto;" "$file"; then
        sed -i '' '/^package/a\
\
import com.example.usermanagement.user.dto.UserDto;' "$file"
    fi
done

# Add missing imports for UserSynchronizationService in merchant service
echo "Adding UserSynchronizationService imports..."
if [ -f "src/main/java/com/example/usermanagement/merchant/service/MerchantService.java" ]; then
    if ! grep -q "import com.example.usermanagement.user.service.UserSynchronizationService;" "src/main/java/com/example/usermanagement/merchant/service/MerchantService.java"; then
        sed -i '' '/^package/a\
\
import com.example.usermanagement.user.service.UserSynchronizationService;' "src/main/java/com/example/usermanagement/merchant/service/MerchantService.java"
    fi
fi

# Add missing imports for UserContextService in shared services
echo "Adding UserContextService imports..."
find src/main/java/com/example/usermanagement/shared/service -name "*.java" -type f -exec grep -l "UserContextService" {} \; | while read file; do
    if ! grep -q "import com.example.usermanagement.user.service.UserContextService;" "$file"; then
        sed -i '' '/^package/a\
\
import com.example.usermanagement.user.service.UserContextService;' "$file"
    fi
done

echo "All import fixes completed!"
