package thesis.webcryptoexchange.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.servlet.http.*;
import java.io.IOException;
import javax.servlet.ServletException;

@Component
public class CustomAuthSuccListener implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        HttpSession session = httpServletRequest.getSession();
        session.setAttribute("user", ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/");
    }   
}