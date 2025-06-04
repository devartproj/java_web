package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.UserDao;
import step.learning.dto.models.SignupFormModel;
import step.learning.services.culture.ResourceProvider;
import step.learning.services.formparse.FormParseResult;
import step.learning.services.formparse.FormParseService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class SignupServlet extends HttpServlet {
    private final ResourceProvider resourceProvider ;
    private final FormParseService formParseService ;
    private final UserDao userDao ;

    @Inject
    public SignupServlet(ResourceProvider resourceProvider, FormParseService formParseService, UserDao userDao) {
        this.resourceProvider = resourceProvider;
        this.formParseService = formParseService;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession() ;
        // String regData = (String) session.getAttribute( "reg-data" ) ;
        Integer regStatus = (Integer) session.getAttribute( "reg-status" ) ;
        if( regStatus != null ) {  // ознака редіректу
            session.removeAttribute( "reg-status" ) ;
            req.setAttribute( "reg-data", regStatus.toString() ) ;  // передаємо на View
            if( regStatus == 2 ) {
                // модель було успішно створено і передано по сесії
                SignupFormModel formModel = (SignupFormModel) session.getAttribute( "reg-model" ) ;
                req.setAttribute( "reg-model", formModel ) ;
                Map<String, String> validationErrors =  formModel == null
                        ? new HashMap<String, String>()
                        : formModel.getValidationErrorMessages() ;
                // переклад
                for( String key : validationErrors.keySet() ) {
                    validationErrors.put( key,
                            resourceProvider.getString( validationErrors.get(key) ) ) ;
                }
                req.setAttribute( "validationErrors", validationErrors ) ;
                if( formModel != null && validationErrors.isEmpty() ) {
                    // модель валідна, передаємо її до БД
                    userDao.addFromForm( formModel ) ;
                }
            }
        }
        // return View()
        req.setAttribute("page-body", "signup.jsp");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession() ;
        // намагаємось створити модель форми
        SignupFormModel formModel ;
        try {
            FormParseResult formParseResult = formParseService.parse( req ) ;
            formModel = new SignupFormModel( formParseResult ) ;
            session.setAttribute( "reg-status", 2 ) ;
            session.setAttribute( "reg-model", formModel ) ;
        }
        catch( ParseException ex ) {
            // TODO: log ex message
            session.setAttribute( "reg-status", 1 ) ;
        }
        resp.sendRedirect( req.getRequestURI() ) ;
    }
}
