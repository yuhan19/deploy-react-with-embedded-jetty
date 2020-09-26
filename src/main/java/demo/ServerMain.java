package demo;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServerMain {
    public void run() throws Exception {
        Server server = new Server(8080);
        this.addReactHandler(server);
        server.start();
        server.join();
    }

    private void addReactHandler(Server server) throws IOException {
        Resource reactUiResource = Resource.newResource( "src/main/resources/reactui" );

        HandlerList handlers = new HandlerList();
        handlers.setHandlers( new Handler[] { getReactHandler( reactUiResource ) } );

        server.setHandler( handlers );
    }

    private ServletContextHandler getReactHandler(Resource reactUiResource) {
        ServletContextHandler reactHandler = new ServletContextHandler();
        reactHandler.setContextPath("/");
        reactHandler.setBaseResource( reactUiResource );
        reactHandler.setWelcomeFiles(new String[] { "index.html" });

        reactHandler.addServlet( ReactServlet.class, "/reactPage");
        reactHandler.addServlet( DefaultServlet.class, "/");

        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
        errorHandler.addErrorPage( 404, "/reactPage" );
        reactHandler.setErrorHandler( errorHandler );

        return reactHandler;
    }

    private ServletContextHandler getReactHandlerWithConsoleErr(Resource reactUiResource) {
        ServletContextHandler reactHandler = new ServletContextHandler();
        reactHandler.setContextPath("/");
        reactHandler.setBaseResource( reactUiResource );
        reactHandler.setWelcomeFiles(new String[] { "index.html" });

        reactHandler.addServlet( DefaultServlet.class, "/");

        ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();
        errorHandler.addErrorPage( 404, "/" );
        reactHandler.setErrorHandler( errorHandler );

        return reactHandler;
    }

    public static class ReactServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp ) throws IOException, ServletException {
            resp.setStatus( 200 );
            req.getRequestDispatcher( "/" ).forward( req, resp );
        }
    }

    public static void main(String[] args) {
        try {
            new ServerMain().run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
