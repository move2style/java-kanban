package webserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;

import java.io.IOException;
import java.nio.charset.Charset;

public class PrioritizedHandler extends HttpTaskServer {

    public PrioritizedHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    public static void getPrioritizedHandler(HttpExchange httpExchange) throws IOException {
        String response = gson.toJson(manager.getPrioritizedTasks());
        httpHandler.sendText(httpExchange, response);
    }

    private static String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
    }
}