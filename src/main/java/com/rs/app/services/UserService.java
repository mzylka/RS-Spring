package com.rs.app.services;

import com.rs.app.domain.entities.Role;
import com.rs.app.domain.entities.User;
import com.rs.app.dto.user.UserDTO;
import com.rs.app.mappers.BlogNewsMapper;
import com.rs.app.mappers.UserMapper;
import com.rs.app.repositories.RoleRepo;
import com.rs.app.repositories.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final UserMapper userMapper;
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepo userRepo, RoleRepo roleRepo, UserMapper userMapper, BlogNewsMapper blogNewsMapper) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.userMapper = userMapper;
    }

    public User getOne(Long id){
        return userRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("User hasn't been found by id"));
    }

    public User getOne(String discordName){
        return userRepo.findByDiscordName(discordName).orElseThrow(() -> new EntityNotFoundException("User hasn't been found by Discord name"));
    }

    public User getOneByDiscordId(String discordId){
        return userRepo.findByDiscordId(discordId).orElseThrow(() -> new EntityNotFoundException("User hasn't been found by Discord ID"));
    }

    public Page<UserDTO> getAll(Pageable pageable){
        return userRepo.findAll(pageable).map(userMapper::toDto);
    }
    public Page<UserDTO> getAll(Pageable pageable, String search){
        return userRepo.findByDiscordNameContainingIgnoreCase(search, pageable).map(userMapper::toDto);
    }

    @Transactional
    public User createOrUpdate(String discordId, String username, String avatar){
        log.info("Szukanie usera prze DiscordID");
        Optional<User> optionalUser = userRepo.findByDiscordIdWithRoles(discordId);
        log.info("Szukanie usera prze DiscordID END");
        String avatarUrl = "https://cdn.discordapp.com/" + discordId + "/" + avatar;
        log.info("Tworzenie pola usera");
        User user;
        log.info("Tworzenie obiektu usera END");

        if(optionalUser.isPresent()){
            user = optionalUser.get();
            user.setDiscordName(username);
            user.setAvatarUrl(avatarUrl);
            user.setLastLogged(Instant.now());
        }
        else {
            log.info("Tworzenie obiektu usera");
            user = new User(discordId, username, avatarUrl);
            log.info("Query role");
            Role role = roleRepo.findByName("NORMAL").orElseThrow(() -> new EntityNotFoundException("Role hasn't been found"));
            log.info("Query role END");
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
        }

        log.info("Zapisywanie usera");
        return userRepo.save(user);
    }

    public void assignRole(Long userId, String roleName){
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User hasn't been found"));
        Role role = roleRepo.findByName(roleName).orElseThrow(() -> new EntityNotFoundException("Role hasn't been found"));
        user.getRoles().add(role);
        userRepo.save(user);
    }

    public void removeRole(Long userId, String roleName){
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User hasn't been found"));
        user.getRoles().removeIf(r -> r.getName().equals(roleName));
        userRepo.save(user);
    }

    public void banUser(Long userId, int days){
        User user = userRepo.findById(userId).orElseThrow(() -> new EntityNotFoundException("User hasn't been found"));
        user.ban(days);
        userRepo.save(user);
    }
}
