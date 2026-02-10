package com.rs.app;

import com.rs.app.domain.entities.Role;
import com.rs.app.repositories.RoleRepo;
import com.rs.app.repositories.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MakeAdminCLI implements CommandLineRunner {
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;

    public MakeAdminCLI(RoleRepo roleRepo, UserRepo userRepo) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args){
        Role adminRole = roleRepo.findByName("ADMIN").orElseGet(() -> roleRepo.save(new Role("ADMIN")));
        Role moderatorRole = roleRepo.findByName("MODERATOR").orElseGet(() -> roleRepo.save(new Role("MODERATOR")));
        Role organizerRole = roleRepo.findByName("ORGANIZER").orElseGet(() -> roleRepo.save(new Role("ORGANIZER")));
        Role redactorRole = roleRepo.findByName("REDACTOR").orElseGet(() -> roleRepo.save(new Role("REDACTOR")));
        Role userRole = roleRepo.findByName("NORMAL").orElseGet(() -> roleRepo.save(new Role("NORMAL")));

    }
}
