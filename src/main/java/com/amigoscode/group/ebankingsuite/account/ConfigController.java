package com.amigoscode.group.ebankingsuite.account;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {
    @GetMapping("/")
    public String awsTest(){
        return "heyy i work";
    }
}
