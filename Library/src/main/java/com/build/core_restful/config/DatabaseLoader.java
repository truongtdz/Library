package com.build.core_restful.config;

import java.util.ArrayList;
import java.util.List;

import com.build.core_restful.domain.Permission;
import com.build.core_restful.domain.Role;
import com.build.core_restful.domain.User;
import com.build.core_restful.repository.PermissionRepository;
import com.build.core_restful.repository.RoleRepository;
import com.build.core_restful.repository.UserRepository;
import com.build.core_restful.util.enums.GenderEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DatabaseLoader implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseLoader(
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");
        long countPermissions = this.permissionRepository.count();
        long countRoles = this.roleRepository.count();
        long countUsers = this.userRepository.count();

        if (countPermissions == 0) {
            ArrayList<Permission> arr = new ArrayList<>();

            // Module Address
            arr.add(new Permission("ADDRESS" , "/api/v1/address/all", "GET", "Get all address"));
            arr.add(new Permission("ADDRESS" , "/api/v1/address/by/{id}", "GET", "Get address by id"));
            arr.add(new Permission("ADDRESS" , "/api/v1/address/by/user/{id}", "GET", "Get address by user id"));
            arr.add(new Permission("ADDRESS" , "/api/v1/address/create", "POST", "Create new address"));
            arr.add(new Permission("ADDRESS" , "/api/v1/address/update", "PUT", "Update current address"));
            arr.add(new Permission("ADDRESS" , "/api/v1/address/update/default", "PUT", "Update default address"));
            arr.add(new Permission("ADDRESS" , "/api/v1/address/{id}", "DELETE", "Ban a address"));
            
            // Module Auth
            arr.add(new Permission("AUTH" , "/api/v1/auth/account", "GET", "Get account login"));
            arr.add(new Permission("AUTH" , "/api/v1/auth/login", "POST", "Login"));
            arr.add(new Permission("AUTH" , "/api/v1/auth/logout", "POST", "Logout"));
            arr.add(new Permission("AUTH" , "/api/v1/auth/refresh", "GET", "Get refresh token"));
            arr.add(new Permission("AUTH" , "/api/v1/auth/register", "POST", "Register account"));
   
            // Module Author
            arr.add(new Permission("AUTHOR" , "/api/v1/author/all", "GET", "Get all"));
            arr.add(new Permission("AUTHOR" , "/api/v1/author/by/{id}", "GET", "Get by id"));
            arr.add(new Permission("AUTHOR" , "/api/v1/author/create", "POST", "Create"));
            arr.add(new Permission("AUTHOR" , "/api/v1/author/update", "PUT", "Update"));
            arr.add(new Permission("AUTHOR" , "/api/v1/author/restore", "PUT", "Restore"));
            arr.add(new Permission("AUTHOR" , "/api/v1/author/delete", "PUT", "Delete sort"));
            arr.add(new Permission("AUTHOR" , "/api/v1/author/delete}", "DELETE", "Delete"));

            // Module Book
            arr.add(new Permission("BOOK", "/api/v1/book/all", "GET", "Search book"));
            arr.add(new Permission("BOOK", "/api/v1/book/by/{id}", "GET", "Get book by id"));
            arr.add(new Permission("BOOK", "/api/v1/book/create", "POST", "Create book"));
            arr.add(new Permission("BOOK", "/api/v1/book/update/{id}", "PUT", "Update book"));
            arr.add(new Permission("BOOK", "/api/v1/book/delete", "PUT", "Delete soft book"));
            arr.add(new Permission("BOOK", "/api/v1/book/restore", "PUT", "Restore book"));
            arr.add(new Permission("BOOK", "/api/v1/book/delete/{id}", "DELETE", "Delete book"));
            arr.add(new Permission("BOOK", "/api/v1/book/quantity/active", "GET", "Get quantity book active"));
            arr.add(new Permission("BOOK", "/api/v1/book/quantity/delete", "GET", "Get quantity book delete"));
            
            // Module Branch
            arr.add(new Permission("BRANCH", "/api/v1/branch/all", "GET", "Get all branches"));
            arr.add(new Permission("BRANCH", "/api/v1/branch/by/{id}", "GET", "Get branch by id"));
            arr.add(new Permission("BRANCH", "/api/v1/branch/create", "POST", "Create branch"));
            arr.add(new Permission("BRANCH", "/api/v1/branch/update/{id}", "PUT", "Update branch"));
            arr.add(new Permission("BRANCH", "/api/v1/branch/delete", "PUT", "Soft delete branch"));
            arr.add(new Permission("BRANCH", "/api/v1/branch/restore", "PUT", "Restore branch"));

            // Module Cart
            arr.add(new Permission("CART", "/api/v1/cart/by/user/{id}", "GET", "Get book at cart by user"));
            arr.add(new Permission("CART", "/api/v1/cart/update", "POST", "Change book to cart"));

            // Module Category
            arr.add(new Permission("CATEGORY", "/api/v1/category/all", "GET", "Get all categories"));
            arr.add(new Permission("CATEGORY", "/api/v1/category/by/{id}", "GET", "Get category by id"));
            arr.add(new Permission("CATEGORY", "/api/v1/category/create", "POST", "Create new category"));
            arr.add(new Permission("CATEGORY", "/api/v1/category/update/{id}", "PUT", "Update category"));
            arr.add(new Permission("CATEGORY", "/api/v1/category/delete", "PUT", "Delete soft author"));
            arr.add(new Permission("CATEGORY", "/api/v1/category/restore", "PUT", "Restore author"));
            arr.add(new Permission("CATEGORY", "/api/v1/category/delete/{id}", "DELETE", "Delete author"));
            
            // Module Upload
            arr.add(new Permission("UPLOAD", "/api/v1/upload/server", "POST", "Upload file to server"));
            arr.add(new Permission("UPLOAD", "/api/v1/upload/cloudinary", "POST", "Upload file to cloudinary"));
            
            // Module Notification
            arr.add(new Permission("NOTIFICATION", "/api/v1/notification/all", "GET", "Gel all"));
                        
            // Module Payment
            arr.add(new Permission("PAYMENT", "/api/v1/payment/create", "POST", "Create payment"));
            arr.add(new Permission("PAYMENT", "/api/v1/payment/webhook", "POST", "Handle payment webhook"));
            arr.add(new Permission("PAYMENT", "/api/v1/payment/status/{orderId}", "GET", "Get payment status by order ID"));

            // Module Permission
            arr.add(new Permission("PERMISSION", "/api/v1/permission/all", "GET", "Get all permissions"));
            arr.add(new Permission("PERMISSION", "/api/v1/permission/by/{id}", "GET", "Get permission by id"));
            arr.add(new Permission("PERMISSION", "/api/v1/permission/create", "POST", "Create new permission"));
            arr.add(new Permission("PERMISSION", "/api/v1/permission/update/{id}", "PUT", "Update permission"));
            arr.add(new Permission("PERMISSION", "/api/v1/permission/delete/{id}", "DELETE", "Delete permission"));
            
            // Module Rental Order
            arr.add(new Permission("RENTAL_ORDER", "/api/v1/order/rental/all", "GET", "Get all orders"));
            arr.add(new Permission("RENTAL_ORDER", "/api/v1/order/rental/by/{id}", "GET", "Get order by id"));
            arr.add(new Permission("RENTAL_ORDER", "/api/v1/order/rental/create", "POST", "Create rental order"));
            arr.add(new Permission("RENTAL_ORDER", "/api/v1/order/rental/update/confirm/{id}", "PUT", "Confirm rental order"));
            arr.add(new Permission("RENTAL_ORDER", "/api/v1/order/rental/update/cancel/{id}", "PUT", "Cancel rental order"));
            arr.add(new Permission("RENTAL_ORDER", "/api/v1/order/rental/update/status/{id}", "PUT", "Update status order"));
            arr.add(new Permission("RENTAL_ORDER", "/api/v1/order/rental/total/quantity", "GET", "Get quantity by order status"));
            arr.add(new Permission("RENTAL_ORDER", "/api/v1/order/rental/total/revenue", "GET", "Get revenue rental order"));
            arr.add(new Permission("RENTAL_ORDER", "/api/v1/order/rental/total/deposit", "GET", "Get total deposit order"));
                    
            // Module Rented Order
            arr.add(new Permission("RENTED_ORDER", "/api/v1/order/rented/all", "GET", "Get all orders"));
            arr.add(new Permission("RENTED_ORDER", "/api/v1/order/rented/by/{id}", "GET", "Get order by id"));
            arr.add(new Permission("RENTED_ORDER", "/api/v1/order/rented/create", "POST", "Create rented order"));
            arr.add(new Permission("RENTED_ORDER", "/api/v1/order/rented/update/{id}", "PUT", "Update status order"));
            
            // Module Revenue
            arr.add(new Permission("REVENUE", "/api/v1/revenue/all", "GET", "Get all revenue report"));
            arr.add(new Permission("REVENUE", "/api/v1/revenue/by/{id}", "GET", "Get revenue report by id"));
            arr.add(new Permission("REVENUE", "/api/v1/revenue/by/date", "GET", "Get revenue report by date range"));
            arr.add(new Permission("REVENUE", "/api/v1/revenue/total/rental-orders", "GET", "Get total quantity of rental orders"));
            arr.add(new Permission("REVENUE", "/api/v1/revenue/total/late-fee", "GET", "Get total late fee"));
            arr.add(new Permission("REVENUE", "/api/v1/revenue/total/rental-price", "GET", "Get total rental amount"));
            arr.add(new Permission("REVENUE", "/api/v1/revenue/total/deposit", "GET", "Get total deposit amount"));
            arr.add(new Permission("REVENUE", "/api/v1/revenue/total/revenue", "GET", "Get total revenue"));
                
            // Module Review
            arr.add(new Permission("REVIEW", "/api/v1/review/by/user/{id}", "GET", "Get reviews by user"));
            arr.add(new Permission("REVIEW", "/api/v1/review/by/book/{id}", "GET", "Get reviews by book"));
            arr.add(new Permission("REVIEW", "/api/v1/review/by/parent/{id}", "GET", "Get reviews by parent"));
            arr.add(new Permission("REVIEW", "/api/v1/review/create", "POST", "Create review"));
            arr.add(new Permission("REVIEW", "/api/v1/review/delete/{id}", "DELETE", "Delete review"));

            // Module Role
            arr.add(new Permission("ROLE", "/api/v1/role/all", "GET", "Get all roles"));
            arr.add(new Permission("ROLE", "/api/v1/role/by/{id}", "GET", "Get role by id"));
            arr.add(new Permission("ROLE", "/api/v1/role/create", "POST", "Create new role"));
            arr.add(new Permission("ROLE", "/api/v1/role/update/{id}", "PUT", "Update role"));
            arr.add(new Permission("ROLE", "/api/v1/role/delete/{id}", "DELETE", "Delete role"));

            // Module Subscribe
            arr.add(new Permission("SUBSCRIBE", "/api/v1/subscribe/create", "POST", "Create subscribe"));
            arr.add(new Permission("SUBSCRIBE", "/api/v1/subscribe/delete", "DELETE", "Delete subscribe"));

            // Module User
            arr.add(new Permission("USER", "/api/v1/user/all", "GET", "Get all user"));
            arr.add(new Permission("USER", "/api/v1/user/by/{id}", "GET", "Get user by id"));
            arr.add(new Permission("USER", "/api/v1/user/create", "POST", "Create new user"));
            arr.add(new Permission("USER", "/api/v1/user/update/{id}", "PUT", "Update user"));
            arr.add(new Permission("USER", "/api/v1/user/delete", "PUT", "Delete soft user"));
            arr.add(new Permission("USER", "/api/v1/user/restore", "PUT", "Restore user"));
            arr.add(new Permission("USER", "/api/v1/user/delete/{id}", "DELETE", "Delete user"));
            arr.add(new Permission("USER", "/api/v1/user/update/password", "PUT", "Update password user"));
            arr.add(new Permission("USER", "/api/v1/user/update/role", "PUT", "Update role user"));
            arr.add(new Permission("USER", "/api/v1/user/quantity/active", "GET", "Get quantity active"));
            arr.add(new Permission("USER", "/api/v1/user/quantity/delete", "GET", "Get quantity delete"));

            this.permissionRepository.saveAll(arr);
        }

        if (countRoles == 0) {
            List<Permission> adminPermissions = this.permissionRepository.findAll();
            Role adminRole = new Role("SUPER_ADMIN","Admin full permissions", adminPermissions);
            this.roleRepository.save(adminRole);

            List<Permission> staffPermissions = new ArrayList<>();
            staffPermissions.addAll(this.permissionRepository.findByModule("AUTH"));
            staffPermissions.addAll(this.permissionRepository.findByModule("AUTHOR"));
            staffPermissions.addAll(this.permissionRepository.findByModule("BOOK"));
            staffPermissions.addAll(this.permissionRepository.findByModule("BRANCH"));
            staffPermissions.addAll(this.permissionRepository.findByModule("CATEGORY"));
            staffPermissions.addAll(this.permissionRepository.findByModule("NOTIFICATION"));
            staffPermissions.addAll(this.permissionRepository.findByModule("RENTAL_ORDER"));
            staffPermissions.addAll(this.permissionRepository.findByModule("RENTED_ORDER"));
            staffPermissions.addAll(this.permissionRepository.findByModule("REVENUE"));
            staffPermissions.addAll(this.permissionRepository.findByModule("ORDER"));
            Role staffRole = new Role("STAFF","STAFF can management product and order", staffPermissions);
            this.roleRepository.save(staffRole);

            List<Permission> userPermissions = new ArrayList<>();
            userPermissions.addAll(this.permissionRepository.findByModule("ADDRESS"));
            userPermissions.addAll(this.permissionRepository.findByModule("AUTH"));
            userPermissions.addAll(this.permissionRepository.findByModuleAndMethod("AUTHOR", "GET"));
            userPermissions.addAll(this.permissionRepository.findByModuleAndMethod("BOOK", "GET"));
            userPermissions.addAll(this.permissionRepository.findByModuleAndMethod("BRANCH", "GET"));
            userPermissions.addAll(this.permissionRepository.findByModule("CART"));
            userPermissions.addAll(this.permissionRepository.findByModuleAndMethod("CATEGORY", "GET"));
            userPermissions.addAll(this.permissionRepository.findByModule("UPLOAD"));
            userPermissions.addAll(this.permissionRepository.findByModule("PAYMENT"));
            userPermissions.addAll(this.permissionRepository.findByApiPathEquals("/api/v1/order/rental/by/{id}"));
            userPermissions.addAll(this.permissionRepository.findByApiPathEquals("/api/v1/order/rental/create"));
            userPermissions.addAll(this.permissionRepository.findByApiPathEquals("/api/v1/order/rental/update/cancel/{id}"));
            userPermissions.addAll(this.permissionRepository.findByApiPathEquals("/api/v1/order/rented/by/{id}"));
            userPermissions.addAll(this.permissionRepository.findByApiPathEquals("/api/v1/order/rented/create"));
            userPermissions.addAll(this.permissionRepository.findByModule("REVIEW"));
            userPermissions.addAll(this.permissionRepository.findByModule("SUBSCRIBE"));
            userPermissions.addAll(this.permissionRepository.findByApiPathEquals("/api/v1/user/by/{id}"));
            userPermissions.addAll(this.permissionRepository.findByApiPathEquals("/api/v1/user/update/{id}"));
            userPermissions.addAll(this.permissionRepository.findByApiPathEquals("/api/v1/user/update/password"));

            Role userRole = new Role("USER","USER can rental book", userPermissions);
            this.roleRepository.save(userRole);
        }

        if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setAge(20);
            adminUser.setGender(GenderEnum.Male.toString());
            adminUser.setFullName("I'm super admin");
            adminUser.setPassword(this.passwordEncoder.encode("123456"));

            Role adminRole = this.roleRepository.findByName("SUPER_ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }

            this.userRepository.save(adminUser);
        }

        if (countPermissions > 0 && countRoles > 0 && countUsers > 0) {
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        } else
            System.out.println(">>> END INIT DATABASE");
    }

}
