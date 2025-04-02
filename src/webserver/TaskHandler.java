package webserver;

import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.nio.charset.Charset;

public class TaskHandler extends HttpTaskServer {
    public TaskHandler(TaskManager manager, Gson gson) {
        super(manager, gson);
    }

    public static void createTaskHandler(HttpExchange httpExchange) throws IOException {
        String json = readText(httpExchange);

        if (json.isEmpty()) {
            httpExchange.sendResponseHeaders(400, 0);
            return;
        }

        final Task task = gson.fromJson(json, Task.class);
        Integer sizeMap = manager.getTaskList().size();

        if (task.getId() == null) {
            manager.createTask(task);

            if (sizeMap >= manager.getTaskList().size()) {  //если список >= списку после создания задачи, то пересечение было
                httpExchange.sendResponseHeaders(406, 0);
                return;
            }

            httpExchange.sendResponseHeaders(201, 0);
        } else {
            manager.updateTask(task);

            if (manager.getTaskById(task.getId()) == null) { //если не находим задачу по айди, то она пересекается с сущ.
                httpExchange.sendResponseHeaders(406, 0);
                return;
            }

            httpExchange.sendResponseHeaders(201, 0);
        }

    }

    public static void getTasksHandler(HttpExchange httpExchange) throws IOException {
        String response = gson.toJson(manager.getTaskList());
        httpHandler.sendText(httpExchange, response);
    }

    public static void getTaskIDHandler(HttpExchange httpExchange, String id) throws IOException {
        Integer taskId = Integer.parseInt(id);

        if (manager.getTaskById(taskId) == null) {
            httpExchange.sendResponseHeaders(404, 0);
            return;
        } else {
            String response = gson.toJson(manager.getTaskById(taskId));
            httpHandler.sendText(httpExchange, response);
        }

    }

    public static void deletTaskIDHandler(HttpExchange httpExchange, String id) throws IOException {
        Integer taskId = Integer.parseInt(id);

        if (manager.getTaskById(taskId) != null) {
            manager.deleteTask(taskId);
            httpExchange.sendResponseHeaders(200, 0);
        }
    }

    private static String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), Charset.defaultCharset());
    }
}
