package com.lwt.tavern;

import com.lwt.tavern.common.Result;
import com.lwt.tavern.entity.User;
import com.lwt.tavern.service.UserService;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class TavernApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void testPassword() {
        System.out.println(new BCryptPasswordEncoder().encode("123"));
    }

    @Test
    void testTime() {
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void testUser(){
        User user = userService.getById(1);
        System.out.println(user);
    }

    @Test
    public void testMarkDown(){
        // 1. 创建 Parser（解析器）
        Parser parser = Parser.builder().build();

        // 2. 解析 Markdown 文本，得到节点树（AST）
        Node document = parser.parse("This is *Markdown* text.");

        // 3. 创建 HTML 渲染器，并渲染节点树
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(document);
        // 输出: "<p>This is <em>Markdown</em> text.</p>\n"
        System.out.println(html);
    }

    @Test
    public void testSendCode(){
        Result result = userService.sendCode("14762386203");
        System.out.println("=================================");
        System.out.println("code:"+result);
        System.out.println("==================================");
    }

}
