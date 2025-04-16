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

            // Module Product
            this.permissionRepository.saveAll(arr);
        }

        if (countRoles == 0) {
            List<Permission> adminPermissions = this.permissionRepository.findAll();
            Role adminRole = new Role("SUPER_ADMIN","Admin full permissions", adminPermissions);
            this.roleRepository.save(adminRole);

            List<Permission> hrPermissions = this.permissionRepository.findByModule("JOBS");
            hrPermissions.addAll(this.permissionRepository.findByModule("RESUMES"));
            Role hrRole = new Role("HR","Hr can management Job and Resumes", hrPermissions);
            this.roleRepository.save(hrRole);

            List<Permission> userPermissions = this.permissionRepository.findByModuleAndMethod("JOBS", "GET");
            userPermissions.addAll(this.permissionRepository.findByModule("RESUMES"));
            Role userRole = new Role("USER","USER can management resumes ", userPermissions);
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
