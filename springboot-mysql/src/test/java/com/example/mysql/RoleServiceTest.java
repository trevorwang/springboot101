package com.example.mysql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class RoleServiceTest {

    @Autowired
    private RoleRepository repository;

    @Test
    public void testAddRole() {
        var list = repository.findByNameContaining("test");
        if (list.isEmpty()) {
            var role = new Role();
            role.setName("test");
            repository.save(role);
            list = repository.findByNameContaining("test");
        }
        Assert.notEmpty(list, "角色不存在");
    }

    @Test
    public void insertRoles() {
        repository.deleteAll();
        Assert.isTrue(!repository.findAll().iterator().hasNext(), "数据库没有删除干净");
        var role = new Role();
        role.setName("role1");
        var role2 = new Role();
        role2.setName("role2");
        repository.save(role);
        repository.save(role2);
        var list = repository.findByNameContaining("role");
        Assert.isTrue(list.size() > 1, "角色列表数量不对");
    }
}
