package com.closedai.closedai.security;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class StatelessSessionFilter implements Filter {

    private static final String COOKIE = "CLOSEDAI_SESSION";
    @SuppressWarnings("unused")
    private static final ObjectMapper JSON = new ObjectMapper();

    @Value("${app.hmac-secret}")      // put this in application.yml or an env-var
    private String secret;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;

        // ── 1) read cookie  ───────────────────────────────────────────────
        String session = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if (COOKIE.equals(c.getName())) {
                    session = c.getValue();
                    break;
                }
            }
        }

        // ── 2) verify or mint  ────────────────────────────────────────────
        if (session == null || !valid(session)) {
            session = mint();
            Cookie c = new Cookie(COOKIE, session);
            c.setHttpOnly(true);
            c.setPath("/");
            // Optional: c.setSecure(true); // only over HTTPS
            response.addCookie(c);
        }

        // ── 3) expose to controllers  ─────────────────────────────────────
        request.setAttribute("sessionId", session.split("\\.")[0]);

        chain.doFilter(req, res);
    }

    /* Generate   <uuid>.<hmac> */
    private String mint() {
        String id = UUID.randomUUID().toString();
        return id + "." + hmac(id);
    }

    /* stateless verification */
    private boolean valid(String cookie) {
        String[] parts = cookie.split("\\.");
        return parts.length == 2 && hmac(parts[0]).equals(parts[1]);
    }

    /* HMAC-SHA256(uuid, secret) → hex */
    @SuppressWarnings("UseSpecificCatch")
    private String hmac(String id) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(secret.getBytes());
            return Hex.encodeHexString(sha.digest(id.getBytes()));
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
