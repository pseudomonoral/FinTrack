package com.financeTracker.handler;

import com.financeTracker.out.OutputData;
import java.util.List;

/**
 * The MainHandler class is responsible for handling various user requests related to workspaces.
 * It interacts with the MainController to fetch and manipulate workspace data based on user input.
 */
public class MainHandler {

    /**
     * Handles the request to show the currently booked workspaces for the authorized user.
     *
     * @param outputData The output data provider.
     * @param controller The main controller for handling business logic.
     */
    public static void handleShowCurrentWorkspaces(OutputData outputData, MainController controller) {
        List<Workspace> workspaces = controller.showCurrentWorkspaces(ApplicationContext.getAuthorizeUser().getId());
        List<WorkspaceType> workspaceTypes = controller.showWorkspaceTypes();
        if (workspaces.isEmpty()) {
            outputData.output("У вас нет забронированных рабочих пространств");
        } else {
            for (Workspace reading : workspaces) {
                outputData.output(formatWorkspace(reading, workspaceTypes));
            }
        }
    }

    /**
     * Handles the submission of a new workspace booking.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The main controller for handling business logic.
     */
    public static void handleWorkspaceSubmission(Input inputData, OutputData outputData, MainController controller) {
        outputData.output("Выберите год рабочего пространства");
        String year = inputData.in().toString();
        outputData.output("Выберите месяц рабочего пространства");
        String month = inputData.in().toString();
        outputData.output("Выберите день рабочего пространства");
        String day = inputData.in().toString();
        outputData.output("Выберите часы рабочего пространства");
        String hours = inputData.in().toString();
        outputData.output("Выберите минуты рабочего пространства");
        String minutes = inputData.in().toString();

        List<Workspace> workspacesForThisTime = controller.getWorkspaceByDate(Integer.parseInt(year), Integer.parseInt(month),
                Integer.parseInt(day), Integer.parseInt(hours), Integer.parseInt(minutes));

        if (workspacesForThisTime.isEmpty()) {
            outputData.errOutput("Нет рабочего пространства на эту дату и время");
        }


        outputData.output("Введите тип рабочего пространства");
        showAvailableWorkspaceTypes(outputData, controller);
        String workspaceTypeId = inputData.in().toString();

        Workspace workspace = new Workspace();
        for (Workspace wspace : workspacesForThisTime) {
            if (wspace.getTypeId() == Integer.parseInt(workspaceTypeId)) {
                workspace = wspace;
                break;
            }
        }
        controller.submitWorkspace(ApplicationContext.getAuthorizeUser().getId(), Long.parseLong(workspaceTypeId), workspace.getId());
    }

    /**
     * Handles the request to show all available workspaces.
     *
     * @param outputData The output data provider.
     * @param controller The main controller for handling business logic.
     */
    public static void handleAvailableWorkspaces(OutputData outputData, MainController controller) {
        List<Workspace> workspaces = controller.showAvailableWorkspaces();
        List<WorkspaceType> workspaceTypes = controller.showWorkspaceTypes();

        for (Workspace workspace : workspaces) {
            outputData.output(formatWorkspace(workspace, workspaceTypes));
        }
    }

    /**
     * Handles the request to show available workspaces for a specific date.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The main controller for handling business logic.
     */
    public static void handleAvailableWorkspacesForSpecificDate(Input inputData, OutputData outputData, MainController controller) {
        final String yearMessage = "Введите год:";
        outputData.output(yearMessage);
        String year = inputData.in().toString();
        final String monthMessage = "Введите месяц:";
        outputData.output(monthMessage);
        String month = inputData.in().toString();
        final String dayMessage = "Введите день:";
        outputData.output(dayMessage);
        String day = inputData.in().toString();

        List<Workspace> workspaces = controller.getWorkspaceByDate(Integer.parseInt(year),
                Integer.parseInt(month), Integer.parseInt(day));
        List<WorkspaceType> workspaceTypes = controller.showWorkspaceTypes();

        for (Workspace workspace : workspaces) {
            outputData.output(formatWorkspace(workspace, workspaceTypes));
        }
    }

    /**
     * Handles the request to cancel a workspace booking.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The main controller for handling business logic.
     */
    public static void bookCancel(Input inputData, OutputData outputData, MainController controller) {
        outputData.output("Выберите год рабочего пространства");
        String year = inputData.in().toString();
        outputData.output("Выберите месяц рабочего пространства");
        String month = inputData.in().toString();
        outputData.output("Выберите день рабочего пространства");
        String day = inputData.in().toString();
        outputData.output("Выберите часы рабочего пространства");
        String hours = inputData.in().toString();
        outputData.output("Выберите минуты рабочего пространства");
        String minutes = inputData.in().toString();

        List<Workspace> workspacesForThisTime = controller.getWorkspaceByDate(Integer.parseInt(year), Integer.parseInt(month),
                Integer.parseInt(day), Integer.parseInt(hours), Integer.parseInt(minutes));

        outputData.output("Введите тип рабочего пространства");
        showAvailableWorkspaceTypes(outputData, controller);
        String workspaceTypeId = inputData.in().toString();

        for (Workspace wspace : workspacesForThisTime) {
            if (wspace.getTypeId() == Integer.parseInt(workspaceTypeId)) {
                controller.bookCancel(ApplicationContext.getAuthorizeUser().getId(), wspace);
            }
        }
    }

    /**
     * Formats the workspace details into a readable string.
     *
     * @param workspace      The workspace to format.
     * @param workspaceTypes The list of workspace types.
     * @return A formatted string representing the workspace.
     */
    private static String formatWorkspace(Workspace workspace, List<WorkspaceType> workspaceTypes) {
        WorkspaceType type = workspaceTypes.get(Math.toIntExact(workspace.getTypeId() - 1));
        return String.format("%s | %s | %s",
                workspace.getStartDate(), workspace.getEndDate(), type.getTypeName());
    }

    /**
     * Formats the workspace type details into a readable string.
     *
     * @param type The workspace type to format.
     * @return A formatted string representing the workspace type.
     */
    public static String formatWorkspaceType(WorkspaceType type) {
        return String.format("%s. %s",
                type.getId(), type.getTypeName());
    }

    /**
     * Displays the available workspace types.
     *
     * @param outputData The output data provider.
     * @param controller The main controller for handling business logic.
     */
    private static void showAvailableWorkspaceTypes(OutputData outputData, MainController controller) {
        List<WorkspaceType> workspaceTypes = controller.showWorkspaceTypes();
        for (WorkspaceType type : workspaceTypes) {
            outputData.output(formatWorkspaceType(type));
        }
    }
}