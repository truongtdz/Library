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

            // Module User
            arr.add(new Permission("USER" , "/api/v1/user", "POST", "Create a user"));
            arr.add(new Permission("USER" , "/api/v1/user", "PUT", "Update a user"));
            arr.add(new Permission("USER" , "/api/v1/user/{id}", "DELETE", "Ban a user"));
            arr.add(new Permission("USER" , "/api/v1/user/{id}", "GET", "Get a user by id"));
            arr.add(new Permission("USER" , "/api/v1/user", "GET", "Get all user with pagination"));
            arr.add(new Permission("USER" , "/api/v1/user/upload/{id}", "POST", "Upload avatar user"));
            arr.add(new Permission("USER" , "/api/v1/user", "PUT", "Update role user"));
            arr.add(new Permission("USER" , "/api/v1/user/search/**", "GET", "Search book"));

            // Module Role
            arr.add(new Permission("ROLE" , "/api/v1/role", "POST", "Create a role"));
            arr.add(new Permission("ROLE" , "/api/v1/role", "PUT", "Update a role"));
            arr.add(new Permission("ROLE" , "/api/v1/role/{id}", "DELETE", "Ban a role"));
            arr.add(new Permission("ROLE" , "/api/v1/role/{id}", "GET", "Get a role by id"));
            arr.add(new Permission("ROLE" , "/api/v1/role", "GET", "Get all role with pagination"));

            // Module Permission
            arr.add(new Permission("PERMISSION" , "/api/v1/permission", "POST", "Create a permission"));
            arr.add(new Permission("PERMISSION" , "/api/v1/permission", "PUT", "Update a permission"));
            arr.add(new Permission("PERMISSION" , "/api/v1/permission/{id}", "DELETE", "Ban a permission"));
            arr.add(new Permission("PERMISSION" , "/api/v1/permission/{id}", "GET", "Get a permission by id"));
            arr.add(new Permission("PERMISSION" , "/api/v1/permission", "GET", "Get all permission with pagination"));

            // Module Address
            arr.add(new Permission("ADDRESS" , "/api/v1/address", "POST", "Create a address"));
            arr.add(new Permission("ADDRESS" , "/api/v1/address", "PUT", "Update a address"));
            arr.add(new Permission("ADDRESS" , "/api/v1/address/{id}", "DELETE", "Ban a address"));
            arr.add(new Permission("ADDRESS" , "/api/v1/address/{id}", "GET", "Get a address by id"));
            arr.add(new Permission("ADDRESS" , "/api/v1/address/user/{id}", "GET", "Get address by user with pagination"));

            // Module Category
            arr.add(new Permission("CATEGORY" , "/api/v1/category", "POST", "Create a category"));
            arr.add(new Permission("CATEGORY" , "/api/v1/category", "PUT", "Update a category"));
            arr.add(new Permission("CATEGORY" , "/api/v1/category/{id}", "DELETE", "Ban a category"));
            arr.add(new Permission("CATEGORY" , "/api/v1/category/{id}", "GET", "Get a category by id"));
            arr.add(new Permission("CATEGORY" , "/api/v1/category", "GET", "Get all category with pagination"));

            // Module Authors
            arr.add(new Permission("AUTHORS" , "/api/v1/authors", "POST", "Create a authors"));
            arr.add(new Permission("AUTHORS" , "/api/v1/authors", "PUT", "Update a authors"));
            arr.add(new Permission("AUTHORS" , "/api/v1/authors/{id}", "DELETE", "Ban a authors"));
            arr.add(new Permission("AUTHORS" , "/api/v1/authors/{id}", "GET", "Get a authors by id"));
            arr.add(new Permission("AUTHORS" , "/api/v1/authors", "GET", "Get all authors with pagination"));

            // Module Book
            arr.add(new Permission("BOOK" , "/api/v1/book", "POST", "Create a book"));
            arr.add(new Permission("BOOK" , "/api/v1/book", "PUT", "Update a book"));
            arr.add(new Permission("BOOK" , "/api/v1/book/{id}", "DELETE", "Delete a book"));
            arr.add(new Permission("BOOK" , "/api/v1/book/{id}", "GET", "Get a book by id"));
            arr.add(new Permission("BOOK" , "/api/v1/book", "GET", "Get all book with pagination"));
            arr.add(new Permission("BOOK" , "/api/v1/book/upload/{id}", "POST", "Upload image book"));
            arr.add(new Permission("BOOK" , "/api/v1/book/cover/{id}", "PUT", "Set cover image book"));

            this.permissionRepository.saveAll(arr);
        }

        if (countRoles == 0) {
            List<Permission> adminPermissions = this.permissionRepository.findAll();
            Role adminRole = new Role("SUPER_ADMIN","Admin full permissions", adminPermissions);
            this.roleRepository.save(adminRole);

            List<Permission> userPermissions = this.permissionRepository.findByModuleAndMethod("USER", "GET");
            userPermissions.addAll(this.permissionRepository.findByModuleAndMethod("BOOK", "GET"));
            userPermissions.addAll(this.permissionRepository.findByModule("ADDRESS"));
            userPermissions.addAll(this.permissionRepository.findByModuleAndMethod("CATEGORY", "GET"));
            userPermissions.addAll(this.permissionRepository.findByModuleAndMethod("AUTHORS", "GET"));
            Role userRole = new Role("USER","USER can management address ", userPermissions);
            this.roleRepository.save(userRole);
        }

        if (countUsers == 0) {
            User adminUser = new User();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setAge(25L);
            adminUser.setGender(GenderEnum.Male);
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
