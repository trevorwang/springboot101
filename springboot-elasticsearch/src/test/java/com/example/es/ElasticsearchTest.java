package com.example.es;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notEmpty;

@SpringBootTest
public class ElasticsearchTest {
    List<User> users = Arrays.asList(
            new User("1", "小红", "m", 11, "xxxxx"),
            new User("2", "大名", "m", 11, "xxxxx"),
            new User("3", "小黑", "m", 13, "xxxxx"),
            new User("4", "祖格", "m", 13, "xxxxx"),
            new User("5", "建国", "m", 14, "xxxxx"),
            new User("6", "狗蛋", "m", 15, "xxxxx"),
            new User("7", "嘿嘿", "m", 18, "xxxxx"),
            new User("8", "红红", "m", 22, "xxxxx")
    );

    @BeforeEach
    void setup() throws IOException {
        if (userService.indexExisted()) {
            userService.deleteIndex();
        }
    }

    @Autowired
    private ESUserService userService;

    @Test
    public void existIndex() throws IOException {
        isTrue(!userService.indexExisted(), "索引不应该存在");
    }

    @Test
    public void testDeleteIndex() throws IOException {
        isTrue(userService.createIndex().acknowledged(), "索引创建成功");
        isTrue(userService.deleteIndex(), "删除索引失败");
        isTrue(!userService.indexExisted(), "删除索引失败");
    }

    @Test
    public void createIndex() throws IOException {
        isTrue(userService.createIndex().acknowledged(), "索引创建成功");
        isTrue(userService.indexExisted(), "索引不存在");
    }


    @Test
    public void bulkIndex() throws IOException {
        var result = userService.bulkIndex(users);
        notEmpty(result, "批量插入失败");
        isTrue(users.size() == result.size(), "索引数量不一致");
    }

    @Test
    public void searchByName() throws IOException, InterruptedException {
        var resultInsert = userService.bulkIndex(users);
        isTrue(users.size() == resultInsert.size(), "索引数量不一致");
        Thread.sleep(1000 * 3);
        var result = userService.searchUserByName("红");
        notEmpty(result, "搜索结果不正确");
        isTrue(2 == result.size(), "搜索数量对不上");
    }
}
