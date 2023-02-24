package com.example.mysql;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.util.Assert.isTrue;

@SpringBootTest
@Slf4j
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private User toSave;

    @BeforeEach
    public void setup() {
        toSave = new User();
        toSave.setId(111);
        toSave.setName("hello");
        toSave.setEmail("hello@me.com");

        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void testSaveUser() {
        final Role role = new Role();
        role.setName("xxxxx");
        role.setId(1);
        roleRepository.save(role);
        toSave.getRoles().add(role);
        final User user2 = userRepository.save(toSave);
        isTrue(toSave.getName().equals(user2.getName()), "用户数据不正确");





    }

    @Test
    public void testUpdateUser() {
        Iterable<User> users = userRepository.findAll();
        users.forEach(i -> log.debug(i.getName()));
        var role = new Role();

        users.forEach(i -> {
            i.getRoles().add(role);
            userRepository.save(i);
        });
    }
}
