package cn.welfarezhu.billweb.filters;

import lombok.extern.slf4j.Slf4j;

import static cn.welfarezhu.billweb.util.SystemConstantValue.*;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class BillInitFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpSession session=request.getSession(true);
        if (session.getAttribute(IS_MANAGER)==null){
            session.setAttribute(IS_MANAGER,false);
        }
        if (session.getAttribute(LOGIN_USER)==null){
            session.setAttribute(IS_LOGIN,false);
        }
        if (session.getAttribute(VERIFY_FIRST_ENTER)==null){
            session.setAttribute(PAGE_SIZE,20);
            session.setAttribute(VERIFY_FIRST_ENTER,"no");
        }
        session.setMaxInactiveInterval(7200);
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info(" The init filter is initializing");
    }

    @Override
    public void destroy() {
        log.info(" The init filter is destroying");
    }
}
