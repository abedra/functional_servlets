package com.aaronbedra.functional.servlet.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import static com.aaronbedra.functional.servlet.ServletAdapter.servletAdapter;
import static com.aaronbedra.functional.servlet.example.GreetingServlet.greetingServlet;
import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler servletContextHandler = new ServletContextHandler(NO_SESSIONS);
        servletContextHandler.setContextPath("/");
        servletContextHandler.addServlet(new ServletHolder(servletAdapter(greetingServlet())), "/hello");
        server.setHandler(servletContextHandler);
        server.start();
        server.join();
    }
}
