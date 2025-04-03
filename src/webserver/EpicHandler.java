package webserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Epic;

import java.io.IOException;
import java.nio.charset.Charset;

public class EpicHandler extends HttpTaskServer {
    public EpicHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    public static void createEpicHandler(HttpExchange httpExchange) throws IOException {
        String json = readText(httpExchange);

        if (json.isEmpty()) {
            httpExchange.sendResponseHeaders(400, 0);
            return;
        }

        final Epic epic = gson.fromJson(json, Epic.class);

        manager.createEpic(epic);
        httpExchange.sendResponseHeaders(201, 0);
    }

    public static void getEpicsHandler(HttpExchange httpExchange) throws IOException {
        String response = gson.toJson(manager.getEpicList());
        httpHandler.sendText(httpExchange, response);
    }

    public static void getEpicSubtaskListHandler(HttpExchange httpExchange, String id) throws IOException {
        Integer epicId = Integer.parseInt(id);

        if (manager.getEpicById(epicId) == null) {
            httpExchange.sendResponseHeaders(404, 0);
            return;
        } else {
            String response = gson.toJson(manager.getSubtaskListForEpic(epicId));
            httpHandler.sendText(httpExchange, response);
        }
    }

    public static void getEpicIDHandler(HttpExchange httpExchange, String id) throws IOException {
        Integer epicId = Integer.parseInt(id);
        Epic epic = manager.getEpicById(epicId);

        if (epic == null) {
            httpExchange.sendResponseHeaders(404, 0);
            return;
        } else {
            String response = gson.toJson(epic);
            httpHandler.sendText(httpExchange, response);
        }
    }

    public static void deletEpicIDHandler(HttpExchange httpExchange, String id) throws IOException {
        Integer epicId = Integer.parseInt(id);

        manager.deleteEpic(epicId);
        httpExchange.sendResponseHeaders(200, 0);
    }

    private static String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
    }
}
