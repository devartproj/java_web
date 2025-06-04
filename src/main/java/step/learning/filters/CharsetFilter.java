package step.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Singleton
public class CharsetFilter implements Filter {
    private FilterConfig filterConfig ;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
    public void doFilter(                      // Основна активність фільтра
            ServletRequest servletRequest,     // !!! запит та відповідь
            ServletResponse servletResponse,   // в узагальненому виді (не НТТР)
            FilterChain filterChain            // Ланцюг подальших фільтрів
    ) throws IOException, ServletException {   // (~ delegate next)
        // Прямий хід - до сервлетів
        // request та response - ті ж самі, що й у сервлетах, просто мають
        // інший тип. Але через це можуть бути недоступні окремі методи.
        // Тому часто їх відразу перетворюють у НТТР-типи
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String charset = StandardCharsets.UTF_8.name() ;
        req.setCharacterEncoding( charset ) ;
        resp.setCharacterEncoding( charset ) ;

        // для передачі даних з фільтра можна використовувати атрибути запиту
        req.setAttribute( "charset", charset ) ;

        filterChain.doFilter(servletRequest, servletResponse);  // передача далі
        // Після цього - зворотний хід

    }
    public void destroy() {
        this.filterConfig = null;
    }
}
