package webserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Subtask;

import java.io.IOException;
import java.nio.charset.Charset;

public class SubtaskHandler extends HttpTaskServer {
    public SubtaskHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    public static void createSubtaskHandler(HttpExchange httpExchange) throws IOException {
        String json = readText(httpExchange);

        if (json.isEmpty()) {
            httpExchange.sendResponseHeaders(400, 0);
            return;
        }

        final Subtask subtask = gson.fromJson(json, Subtask.class);
        Integer sizeMap = manager.getSubtaskList().size();

        if (subtask.getId() == null) {
            manager.createSubtask(subtask);

            if (sizeMap >= manager.getSubtaskList().size()) {  //если список >= списку после создания задачи, то пересечение было
                httpExchange.sendResponseHeaders(406, 0);
                return;
            }

            httpExchange.sendResponseHeaders(201, 0);
        } else {
            manager.updateSubtask(subtask);

            if (manager.getSubtaskById(subtask.getId()) == null) { //если не находим задачу по айди, то она пересекается с сущ.
                httpExchange.sendResponseHeaders(406, 0);
                return;
            }

            httpExchange.sendResponseHeaders(201, 0);
        }

    }

    public static void getSubtasksHandler(HttpExchange httpExchange) throws IOException {
        String response = gson.toJson(manager.getSubtaskList());
        httpHandler.sendText(httpExchange, response);
    }

    public static void getSubtaskIDHandler(HttpExchange httpExchange, String id) throws IOException {
        Integer subtaskId = Integer.parseInt(id);

        if (manager.getSubtaskById(subtaskId) == null) {
            httpExchange.sendResponseHeaders(404, 0);
            return;
        } else {
            String response = gson.toJson(manager.getSubtaskById(subtaskId));
            httpHandler.sendText(httpExchange, response);
        }

    }

    public static void deletSubtaskIDHandler(HttpExchange httpExchange, String id) throws IOException {
        Integer subtaskId = Integer.parseInt(id);

        if (manager.getSubtaskById(subtaskId) != null) {
            manager.deleteSubtask(subtaskId);
            httpExchange.sendResponseHeaders(200, 0);
        }

    }

    private static String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
    }
}
