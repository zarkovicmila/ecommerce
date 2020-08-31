package fon.cartservice.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class CustomerFilter implements Filter {

    Logger logger = LoggerFactory.getLogger(CustomerFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        logger.debug("Customer header {}",httpServletRequest.getHeader("uid"));
    //    logger.debug(" X-Auth-Token header {}",httpServletRequest.getHeader(" X-Auth-Token"));

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
