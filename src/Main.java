import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        HistoryManager historyManager = Managers.getDefaultHistoryManager();

        Task newtask1 = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW);
        taskManager.createTask(newtask1);
        Task newtask2 = new Task("Zadacha po spisky#2", "opisanie",TaskStatus.NEW);
        taskManager.createTask(newtask2);
        Task newtask1_1 = new Task("Zadacha po spisky#1_1", "opisanie", TaskStatus.IN_PROGRESS,1);
        taskManager.updateTask(newtask1_1);
        Epic newepic1 = new Epic("Epic po spisky#1", "opisanie",TaskStatus.NEW);
        taskManager.createEpic(newepic1);
        Epic newepic1_1 = new Epic("Epic po spisky#1", "opisanie",TaskStatus.IN_PROGRESS,3);
        taskManager.updateEpic(newepic1_1);
        Epic newepic1_2 = new Epic("Epic po spisky#1_1", "opisanie",TaskStatus.NEW,3);
        taskManager.updateEpic(newepic1_2);
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
        Subtask newsubtask5_1 = new Subtask("Subtask #5_1", "opisanie 5_1",TaskStatus.NEW,newepic2.getId(),9);
        taskManager.updateSubtask(newsubtask5_1);
        Subtask newsubtask5_2 = new Subtask("Subtask #5_1", "opisanie 5_1",TaskStatus.DONE,newepic2.getId(),9);
        taskManager.updateSubtask(newsubtask5_2);
        Subtask newsubtask4_1 = new Subtask("Subtask #5_1", "opisanie 5_1",TaskStatus.DONE,newepic2.getId(),8);
        taskManager.updateSubtask(newsubtask4_1);
        newsubtask5_2 = new Subtask("Subtask #5_1", "opisanie 5_1",TaskStatus.NEW,newepic2.getId(),9);
        taskManager.updateSubtask(newsubtask5_2);
        newsubtask4_1 = new Subtask("Subtask #5_1", "opisanie 5_1",TaskStatus.NEW,newepic2.getId(),8);
        taskManager.updateSubtask(newsubtask4_1);

        taskManager.getEpicById(4);
        taskManager.getTaskById(1);
        taskManager.getSubtaskById(8);
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