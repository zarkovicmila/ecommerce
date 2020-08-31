package fon.zuulserver.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import fon.zuulserver.utils.FilterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class SessionFilter extends ZuulFilter {

    private static final int FILTER_ORDER=1;
    private static final boolean SHOULD_FILTER=true;

    private SessionRepository sessionRepository;
  //  private HttpSessionIdResolver httpSessionIdResolver;

    private static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    public SessionFilter(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
     //   this.httpSessionIdResolver = httpSessionIdResolver;
    }

    @Override
    public String filterType() {
        return FilterUtil.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpSession httpSession = ctx.getRequest().getSession();
        Session session = sessionRepository.findById(httpSession.getId());

        if(ctx.getRequest().getCookies() == null){
            logger.debug("No session cookie provided");
        }
   //     httpSessionIdResolver.setSessionId(ctx.getRequest(),ctx.getResponse(),httpSession.getId());
        ctx.addZuulRequestHeader("uid", httpSession.getId());
 //       ctx.addZuulResponseHeader("uid", httpSession.getId());
   //     ctx.addZuulRequestHeader("X-auth-token", httpSession.getId());
        logger.debug("Session id is {}", httpSession.getId());

        return null;
    }
}
