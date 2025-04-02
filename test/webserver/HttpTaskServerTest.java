package webserver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private Gson gson;

    @BeforeEach
    void init() {
        taskManager = Managers.getDefaultTaskManager();
        gson = GsonBuilders.getGson();
        httpTaskServer = new HttpTaskServer(taskManager, gson);
        httpTaskServer.start();
    }

    @AfterEach
    void stop() {
        httpTaskServer.stop();
    }

    @Test
    void creatTaskTest() throws IOException, InterruptedException {
        Task task = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks");
        String request = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
    }

    @Test
    void getTasksListTest() throws IOException, InterruptedException {
        Task task = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");

        String request = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();
        HttpResponse<String> response2 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Задача не возвращена");
        assertEquals(1, actual.size(), "Не верное кол-во задач");
        assertEquals(task.getName(), actual.get(0).getName(), "Имя задачи не совпадает с задачей из списка");
        assertEquals(task.getStatus(), actual.get(0).getStatus(), "Статус задачи не совпадает с задачей из списка");
    }

    @Test
    void getTaskIDTestStatusCode200() throws IOException, InterruptedException {
        Task task = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");

        String request = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();
        HttpResponse<String> response2 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uriID = URI.create("http://localhost:8080/tasks/1");
        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uriID).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>() {
        }.getType();
        Task actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Задача не возвращена");
        assertEquals(1, actual.getId(), "Не верное id");
        assertEquals(task.getName(), actual.getName(), "Имя задачи не совпадает с задачей из списка");
        assertEquals(task.getStatus(), actual.getStatus(), "Статус задачи не совпадает с задачей из списка");
    }

    @Test
    void getTaskIDTestStatusCode404() throws IOException, InterruptedException {
        Task task = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks");

        String request = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();
        HttpResponse<String> response2 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uriID = URI.create("http://localhost:8080/tasks/12");
        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uriID).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    void updateTaskTest() throws IOException, InterruptedException {
        Task task = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks");
        String request = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        Task taskUpdate = new Task("Zadacha po", "opisanie", TaskStatus.NEW, 1,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().plusMinutes(15));

        String requestUpdate = gson.toJson(taskUpdate);
        HttpRequest httpRequestUpdate = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestUpdate))
                .build();

        HttpResponse<String> responseUpdate = httpClient.send(httpRequestUpdate, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, responseUpdate.statusCode());
    }

    @Test
    void updateTaskTestStatusCode406() throws IOException, InterruptedException {
        Task task = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks");
        String request = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        Task taskUpdate = new Task("Zadacha po", "opisanie", TaskStatus.NEW, 1,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        String requestUpdate = gson.toJson(taskUpdate);
        HttpRequest httpRequestUpdate = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestUpdate))
                .build();

        HttpResponse<String> responseUpdate = httpClient.send(httpRequestUpdate, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, responseUpdate.statusCode());
    }

    @Test
    void deletTaskTest() throws IOException, InterruptedException {
        Task task = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks");
        String request = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uriDelete = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks/1");
        HttpRequest httpRequestDelete = HttpRequest.newBuilder()
                .uri(uriDelete)
                .DELETE()
                .build();

        HttpResponse<String> responseDelete = httpClient.send(httpRequestDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseDelete.statusCode());
        assertEquals(0, taskManager.getTaskList().size(), "Задач не удалена из менеджера задач");
    }

    @Test
    void creatSubtaskTest() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));
        taskManager.createEpic(newepic);

        Subtask newsubtask = new Subtask("Subtask #1", "opisanie", TaskStatus.NEW, newepic.getId(),
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks");
        String request = gson.toJson(newsubtask);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, taskManager.getSubtaskList().size(), "Подзадача не создана");
    }

    @Test
    void getSubtasksListTest() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));
        taskManager.createEpic(newepic);

        Subtask newsubtask = new Subtask("Subtask #1", "opisanie", TaskStatus.NEW, newepic.getId(),
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks");
        String request = gson.toJson(newsubtask);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Подзадача не возвращена");
        assertEquals(1, actual.size(), "Не верное кол-во задач");
        assertEquals(newsubtask.getName(), actual.get(0).getName(), "Имя подзадачи не совпадает с задачей из списка");
        assertEquals(newsubtask.getStatus(), actual.get(0).getStatus(), "Статус подзадачи не совпадает с задачей из списка");
    }

    @Test
    void getSubtaskIDTestStatusCode200() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));
        taskManager.createEpic(newepic);

        Subtask newsubtask = new Subtask("Subtask #1", "opisanie", TaskStatus.NEW, newepic.getId(),
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks");
        String request = gson.toJson(newsubtask);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uriID = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uriID).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Subtask>() {
        }.getType();
        Subtask actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Подзадача не возвращена");
        assertEquals(2, actual.getId(), "Не верное id");
        assertEquals(newsubtask.getName(), actual.getName(), "Имя подзадачи не совпадает с задачей из списка");
        assertEquals(newsubtask.getStatus(), actual.getStatus(), "Статус подзадачи не совпадает с задачей из списка");
        assertEquals(newsubtask.getEpicId(), actual.getEpicId(), "Подзадача не связана с епиком или связана некорректно");
    }

    @Test
    void getSubtaskIDTestStatusCode404() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));
        taskManager.createEpic(newepic);

        Subtask newsubtask = new Subtask("Subtask #1", "opisanie", TaskStatus.NEW, newepic.getId(),
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks");
        String request = gson.toJson(newsubtask);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uriID = URI.create("http://localhost:8080/subtasks/23");
        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uriID).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    void updateSubtaskTest() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));
        taskManager.createEpic(newepic);

        Subtask newsubtask = new Subtask("Subtask #1", "opisanie", TaskStatus.NEW, newepic.getId(),
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks");
        String request = gson.toJson(newsubtask);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        newsubtask = new Subtask("Subta", "op", TaskStatus.NEW, newepic.getId(), 2,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().plusMinutes(40));

        String requestUpdate = gson.toJson(newsubtask);
        HttpRequest httpRequestUpdate = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestUpdate))
                .build();

        HttpResponse<String> responseUpdate = httpClient.send(httpRequestUpdate, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, responseUpdate.statusCode());
    }

    @Test
    void updateSubtaskTestStatusCode406() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));
        taskManager.createEpic(newepic);

        Subtask newsubtask = new Subtask("Subtask #1", "opisanie", TaskStatus.NEW, newepic.getId(),
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks");
        String request = gson.toJson(newsubtask);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        newsubtask = new Subtask("Subta", "op", TaskStatus.NEW, newepic.getId(), 2,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        String requestUpdate = gson.toJson(newsubtask);
        HttpRequest httpRequestUpdate = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestUpdate))
                .build();

        HttpResponse<String> responseUpdate = httpClient.send(httpRequestUpdate, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, responseUpdate.statusCode());
    }

    @Test
    void deletSubtaskTest() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));
        taskManager.createEpic(newepic);

        Subtask newsubtask = new Subtask("Subtask #1", "opisanie", TaskStatus.NEW, newepic.getId(),
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks");
        String request = gson.toJson(newsubtask);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uriDelete = URI.create("http://localhost:" + HttpTaskServer.PORT + "/subtasks/2");
        HttpRequest httpRequestDelete = HttpRequest.newBuilder()
                .uri(uriDelete)
                .DELETE()
                .build();

        HttpResponse<String> responseDelete = httpClient.send(httpRequestDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseDelete.statusCode());
        assertEquals(0, taskManager.getSubtaskList().size(), "Задач не удалена из менеджера задач");
    }

    @Test
    void creatEpicTest() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics");
        String request = gson.toJson(newepic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        assertEquals(1, taskManager.getEpicList().size(), "Epic не создан");
    }

    @Test
    void getEpicsListTest() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics");
        String request = gson.toJson(newepic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Епик не возвращен");
        assertEquals(1, actual.size(), "Не верное кол-во епиков");
        assertEquals(newepic.getName(), actual.get(0).getName(), "Имя епика не совпадает с епиком из списка");
        assertEquals(newepic.getStatus(), actual.get(0).getStatus(), "Статус епика не совпадает с епиком из списка");
    }

    @Test
    void getEpicIDTest() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics");
        String request = gson.toJson(newepic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uriID = URI.create("http://localhost:8080/epics/1");
        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uriID).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Epic>() {
        }.getType();
        Epic actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Епик не возвращен");
        assertEquals(1, actual.getId(), "Не верное id");
        assertEquals(newepic.getName(), actual.getName(), "Имя епика не совпадает с епиком из списка");
        assertEquals(newepic.getStatus(), actual.getStatus(), "Статус епика не совпадает с епиком из списка");
    }

    @Test
    void getEpicIDTestStatusCode404() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics");
        String request = gson.toJson(newepic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uriID = URI.create("http://localhost:8080/epics/12");
        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uriID).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    void getSubtaskByEpicIDTest() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics");
        String request = gson.toJson(newepic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        Subtask newsubtask = new Subtask("Subtask #1", "opisanie", TaskStatus.NEW, 1,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());
        taskManager.createSubtask(newsubtask);

        URI uriID = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uriID).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Епик не возвращен");
        assertEquals(2, actual.get(0).getId(), "Не верное id");
        assertEquals(newsubtask.getName(), actual.get(0).getName(), "Имя епика не совпадает с епиком из списка");
        assertEquals(newsubtask.getStatus(), actual.get(0).getStatus(), "Статус епика не совпадает с епиком из списка");
    }

    @Test
    void getSubtaskByEpicIDTestStatusCode404() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics");
        String request = gson.toJson(newepic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        Subtask newsubtask = new Subtask("Subtask #1", "opisanie", TaskStatus.NEW, 1,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());
        taskManager.createSubtask(newsubtask);

        URI uriID = URI.create("http://localhost:8080/epics/12/subtasks");
        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uriID).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    void deleteEpicTest() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics");
        String request = gson.toJson(newepic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uriDelete = URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics/1");
        HttpRequest httpRequestDelete = HttpRequest.newBuilder()
                .uri(uriDelete)
                .DELETE()
                .build();

        HttpResponse<String> responseDelete = httpClient.send(httpRequestDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseDelete.statusCode());
        assertEquals(0, taskManager.getEpicList().size(), "Epic не удален из менеджера задач");
    }

    @Test
    void getHistoryTest() throws IOException, InterruptedException {
        Epic newepic = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(121));

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/epics");
        String request = gson.toJson(newepic);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uriID = URI.create("http://localhost:8080/epics/1");
        HttpRequest httpRequest2 = HttpRequest.newBuilder().uri(uriID).GET().build();
        HttpResponse<String> response2 = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());

        URI uriHistory = URI.create("http://localhost:8080/history");
        HttpRequest httpRequestHistory = HttpRequest.newBuilder().uri(uriHistory).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequestHistory, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Список истории не возвращен");
        assertEquals(1, actual.size(), "Не верное кол-во просмотренных задач");
    }

    @Test
    void getPrioritizedTest() throws IOException, InterruptedException {
        Task task = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());

        HttpClient httpClient = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:" + HttpTaskServer.PORT + "/tasks");
        String request = gson.toJson(task);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(request))
                .build();

        HttpResponse<String> response1 = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        URI uriHistory = URI.create("http://localhost:8080/prioritized");
        HttpRequest httpRequestHistory = HttpRequest.newBuilder().uri(uriHistory).GET().build();
        HttpResponse<String> response = httpClient.send(httpRequestHistory, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Список истории не возвращен");
        assertEquals(1, actual.size(), "Не верное кол-во приоритизированных задач");
    }
}