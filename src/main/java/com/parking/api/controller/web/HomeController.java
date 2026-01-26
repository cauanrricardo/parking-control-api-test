// src/main/java/com/parking/api/controller/web/HomeController.java
package com.parking.api.controller.web;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private void noCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    @GetMapping({"/", "/Inicio", "/Inicio/"})
    public String home(HttpServletResponse response) {
        noCache(response);
        return "index";
    }
}
