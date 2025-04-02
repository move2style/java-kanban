package webserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;

public class HttpTaskServer {
    public static final int PORT = 8080;
    public static final String TASK_URI = "tasks";
    public static final String EPIC_URI = "epics";
    public static final String SUBTASK_URI = "subtasks";
    public static final String HISTORY_URI = "history";
    public static final String PRIORITIAZED_URI = "prioritized";

    static TaskManager manager;
    private HttpServer server;
    static Gson gson;
    static BaseHttpHandler httpHandler = new BaseHttpHandler();

    public HttpTaskServer(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            server.createContext("/tasks", this::commonHandler);
            server.createContext("/epics", this::commonHandler);
            server.createContext("/subtasks", this::commonHandler);
            server.createContext("/history", this::commonHandler);
            server.createContext("/prioritized", this::commonHandler);
        } catch (IOException e) {
            System.out.println("Не получилось запустить http сервер");
        }
    }


    public static void main(String[] args) {
        final HttpTaskServer taskServer = new HttpTaskServer(Managers.getDefaultTaskManager(), GsonBuilders.getGson());

        taskServer.start();
    }

    void start() {
        System.out.println("---> Сервер начал работу <---" + PORT);
        this.server.start();
    }

    public void stop() {

        this.server.stop(0);
        System.out.println("---> Сервер останивил работу <---" + PORT);
    }

    private void commonHandler(HttpExchange httpExchange) {

        try {
            final String[] pathArray = httpExchange.getRequestURI().getPath().split("/");
            String path = pathArray[1];

            switch (path) {
                case TASK_URI -> taskHandler(httpExchange);
                case EPIC_URI -> epicHandler(httpExchange);
                case SUBTASK_URI -> subTaskHandler(httpExchange);
                case HISTORY_URI -> historyHandler(httpExchange);
                case PRIORITIAZED_URI -> prioritizedHandler(httpExchange);
                default -> {
                }
            }
        } catch (Exception exception) {
            System.out.printf("Возникла ошибка%n", exception.getMessage());
        } finally {
            httpExchange.close();
        }
    }

    private void taskHandler(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String method = httpExchange.getRequestMethod();

        switch (method) {
            case "GET" -> {
                if (Pattern.matches("^/tasks/\\d+$", path)) {
                    String pathId = path.replaceFirst("/tasks/", "");
                    TaskHandler.getTaskIDHandler(httpExchange, pathId);

                } else if (Pattern.matches("^/tasks$", path)) {
                    TaskHandler.getTasksHandler(httpExchange);
                }
            }
            case "POST" -> TaskHandler.createTaskHandler(httpExchange);
            case "DELETE" -> {
                if (Pattern.matches("^/tasks/\\d+$", path)) {
                    String pathId = path.replaceFirst("/tasks/", "");
                    TaskHandler.deletTaskIDHandler(httpExchange, pathId);
                }
            }
        }
    }

    private void subTaskHandler(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String method = httpExchange.getRequestMethod();

        switch (method) {
            case "GET" -> {
                if (Pattern.matches("^/subtasks/\\d+$", path)) {
                    String pathId = path.replaceFirst("/subtasks/", "");
                    SubtaskHandler.getSubtaskIDHandler(httpExchange, pathId);

                } else if (Pattern.matches("^/subtasks$", path)) {
                    SubtaskHandler.getSubtasksHandler(httpExchange);
                }
            }
            case "POST" -> SubtaskHandler.createSubtaskHandler(httpExchange);
            case "DELETE" -> {
                if (Pattern.matches("^/subtasks/\\d+$", path)) {
                    String pathId = path.replaceFirst("/subtasks/", "");
                    SubtaskHandler.deletSubtaskIDHandler(httpExchange, pathId);
                }
            }
        }
    }

    private void epicHandler(HttpExchange httpExchange) throws IOException {
        String path = httpExchange.getRequestURI().getPath();
        String method = httpExchange.getRequestMethod();

        switch (method) {
            case "GET" -> {
                if (Pattern.matches("^/epics/\\d+$", path)) {
                    String pathId = path.replaceFirst("/epics/", "");
                    EpicHandler.getEpicIDHandler(httpExchange, pathId);
                } else if (Pattern.matches("^/epics$", path)) {
                    EpicHandler.getEpicsHandler(httpExchange);
                } else if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
                    String pathId = path.replaceFirst("/epics/", "");
                    pathId = pathId.replaceFirst("/subtasks", "");
                    EpicHandler.getEpicSubtaskListHandler(httpExchange, pathId);
                }
            }
            case "POST" -> EpicHandler.createEpicHandler(httpExchange);
            case "DELETE" -> {
                if (Pattern.matches("^/epics/\\d+$", path)) {
                    String pathId = path.replaceFirst("/epics/", "");
                    EpicHandler.deletEpicIDHandler(httpExchange, pathId);
                }
            }
        }
    }

    private void historyHandler(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        switch (method) {
            case "GET" -> HistoryHandler.getHistoryHandler(httpExchange);
        }
    }

    private void prioritizedHandler(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        switch (method) {
            case "GET" -> PrioritizedHandler.getPrioritizedHandler(httpExchange);
        }
    }


}
