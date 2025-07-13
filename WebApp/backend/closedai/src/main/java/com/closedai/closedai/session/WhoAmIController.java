package com.closedai.closedai.session;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;



// Everything else in the app can obtain the same ID with:
// String sessionId = (String) request.getAttribute("sessionId");



/**
 * Simple diagnostic endpoint that echoes the session-id
 * injected by {@link com.closedai.closedai.security.StatelessSessionFilter}.
 */
@RestController
public class WhoAmIController {

    @GetMapping("/api/whoami")
    public String whoAmI(HttpServletRequest request) {
        // the filter stored it as request attribute
        return "Your session is " + request.getAttribute("sessionId");
    }
}
