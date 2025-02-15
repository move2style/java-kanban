import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();

        Task newtask1 = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW);
        taskManager.createTask(newtask1);
        Task newtask2 = new Task("Zadacha po spisky#2", "opisanie",TaskStatus.NEW);
        taskManager.createTask(newtask2);
        Task newTask11 = new Task("Zadacha po spisky#1_1", "opisanie", TaskStatus.IN_PROGRESS,1);
        taskManager.updateTask(newTask11);
        Epic newepic1 = new Epic("Epic po spisky#1", "opisanie",TaskStatus.NEW);
        taskManager.createEpic(newepic1);
        Epic newepic11 = new Epic("Epic po spisky#1", "opisanie",TaskStatus.IN_PROGRESS,3);
        taskManager.updateEpic(newepic11);
        Epic newepic12 = new Epic("Epic po spisky#1_1", "opisanie",TaskStatus.NEW,3);
        taskManager.updateEpic(newepic12);
        Epic newepic2 = new Epic("Epic po spisky#2", "opisanie",TaskStatus.NEW);
        taskManager.createEpic(newepic2);
        Subtask newsubtask1 = new Subtask("Subtask #1", "opisanie",TaskStatus.NEW,newepic1.getId());
        Subtask newsubtask2 = new Subtask("Subtask #2", "opisanie",TaskStatus.NEW,newepic1.getId());
        Subtask newsubtask3 = new Subtask("Subtask #3", "opisanie",TaskStatus.NEW,newepic1.getId());
        taskManager.createSubtask(newsubtask1);
        taskManager.createSubtask(newsubtask2);
        taskManager.createSubtask(newsubtask3);
        Subtask newsubtask4 = new Subtask("Subtask #4", "opisanie",TaskStatus.NEW,newepic2.getId());
        Subtask newsubtask5 = new Subtask("Subtask #5", "opisanie",TaskStatus.NEW,newepic2.getId());
        taskManager.createSubtask(newsubtask4);
        taskManager.createSubtask(newsubtask5);
        Subtask newsubtask51 = new Subtask("Subtask #5_1", "opisanie 5_1",TaskStatus.NEW,newepic2.getId(),9);
        taskManager.updateSubtask(newsubtask51);
        Subtask newsubtask52 = new Subtask("Subtask #5_1", "opisanie 5_1",TaskStatus.DONE,newepic2.getId(),9);
        taskManager.updateSubtask(newsubtask52);
        Subtask newsubtask41 = new Subtask("Subtask #5_1", "opisanie 5_1",TaskStatus.DONE,newepic2.getId(),8);
        taskManager.updateSubtask(newsubtask41);
        newsubtask52 = new Subtask("Subtask #5_1", "opisanie 5_1",TaskStatus.NEW,newepic2.getId(),9);
        taskManager.updateSubtask(newsubtask52);
        newsubtask41 = new Subtask("Subtask #5_1", "opisanie 5_1",TaskStatus.NEW,newepic2.getId(),8);
        taskManager.updateSubtask(newsubtask41);

        taskManager.getEpicById(4);
        taskManager.getEpicById(4);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(8);
        taskManager.getTaskById(1);
        taskManager.getTaskById(1);
        taskManager.getSubtaskListForEpic(4);

        printAllTasks(taskManager);

    }

    private static void printAllTasks(TaskManager manager) {


        System.out.println("Задачи:");
        for (Object task : manager.getTaskList()) {
            System.out.println(task);
        }
        System.out.println("Подзадачи:");
        for (Object subtask : manager.getSubtaskList()) {
            System.out.println(subtask);
        }
        System.out.println("Эпики:");
        for (Object epic : manager.getEpicList()) {
            System.out.println(epic);
            Epic epic1 = (Epic) epic;

            for (Object task : manager.getSubtaskListForEpic(epic1.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("История:");
        for (Object task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}