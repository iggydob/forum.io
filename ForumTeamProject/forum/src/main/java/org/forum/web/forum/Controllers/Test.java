package org.forum.web.forum.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api")
public class Test {
    @GetMapping("/hello")
    public String test(){
        return "Hello";
    }
}
