package hello.container;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

public interface AppInit {

    void onStartup(ServletContext servletContext);

}
