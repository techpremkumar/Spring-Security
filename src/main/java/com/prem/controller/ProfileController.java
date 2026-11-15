
package com.prem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String profile(
            @AuthenticationPrincipal OAuth2User user,
            Model model) {

        model.addAttribute("name", user.getAttribute("name"));
        model.addAttribute("email", user.getAttribute("email"));
        model.addAttribute("picture", user.getAttribute("picture"));

        return "profile";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Profile Controller Working!";
    }
}
