

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * The AdminHandler class is responsible for handling various admin-specific operations related to users and workspaces.
 * It interacts with the MainController to manage user and workspace data based on admin input.
 */
public class AdminHandler {

    /**
     * Handles the request to show the list of registered participants.
     *
     * @param outputData The output data provider.
     * @param controller The main controller for handling business logic.
     */
    public static void handleShowListOfRegisteredParticipants(OutputData outputData, MainController controller) {
        List<User> userList = controller.showAllUsers();
        List<User> adminList = new ArrayList<>();

        for (User user : userList) {
            if (isAdmin(user)) adminList.add(user);
            else {
                outputData.output(formatUser(user));
            }
        }
        for (User admin : adminList) {
            outputData.output(formatUser(admin));
        }
    }

    /**
     * Handles the request to show all workspaces.
     *
     * @param outputData The output data provider.
     * @param controller The main controller for handling business logic.
     */
    public static void handleShowAllWorkspaces(OutputData outputData, MainController controller) {
        List<Workspace> workspaces = controller.showAllWorkspaces();
        for (Workspace workspace : workspaces) {
            outputData.output(workspace);
        }
    }

    /**
     * Handles the request to add a new workspace based on admin input.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The main controller for handling business logic.
     */
    public static void handleAddWorkspace(Input inputData, OutputData outputData, MainController controller) {
        final String workspaceTypeMessage = "Введите id рабочего пространства:";
        outputData.output(workspaceTypeMessage);
        String workspaceTypeId = inputData.in().toString();

        final String startDateMessage = "Введите год, месяц, день, часы, минуты начала рабочего пространства, через пробел";
        outputData.output(startDateMessage);
        List<String> startArr = new ArrayList<>(List.of(inputData.in().toString().split(" ")));
        LocalDateTime startDate = LocalDateTime.of(Integer.parseInt(startArr.get(0)),
                Integer.parseInt(startArr.get(1)),
                Integer.parseInt(startArr.get(2)),
                Integer.parseInt(startArr.get(3)),
                Integer.parseInt(startArr.get(4)));

        final String endDateMessage = "Введите год, месяц, день, часы, минуты конца рабочего пространства, через пробел";
        outputData.output(endDateMessage);
        List<String> endArr = new ArrayList<>(List.of(inputData.in().toString().split(" ")));
        LocalDateTime endDate = LocalDateTime.of(Integer.parseInt(startArr.get(0)),
                Integer.parseInt(endArr.get(1)),
                Integer.parseInt(endArr.get(2)),
                Integer.parseInt(endArr.get(3)),
                Integer.parseInt(endArr.get(4)));

        controller.addWorkspace(Workspace.builder()
                .typeId(Long.parseLong(workspaceTypeId))
                .startDate(startDate)
                .endDate(endDate)
                .build());

    }

    /**
     * Handles the request to add a new workspace type based on admin input.
     *
     * @param inputData  The input data provider.
     * @param outputData The output data provider.
     * @param controller The main controller for handling business logic.
     */
    public static void addNewWorkspaceType(Input inputData, OutputData outputData, MainController controller) {
        final String workSpaceTypeMessage = "Введите тип рабочего пространства:";
        outputData.output(workSpaceTypeMessage);
        String workspaceType = inputData.in().toString();

        controller.addNewWorkspaceType(
                WorkspaceType.builder()
                        .typeName(workspaceType)
                        .build()
        );
    }

    /**
     * Handles the deletion of a workspace identified by its ID.
     *
     * @param inputData   Input source for reading workspace ID.
     * @param outputData  Output destination for displaying messages.
     * @param controller  Main controller for accessing workspace deletion functionality.
     */
    public static void handleDeleteWorkspace(Input inputData, OutputData outputData, MainController controller) {
        final String workspaceIdMessage = "Введите айди рабочего пространства:";
        outputData.output(workspaceIdMessage);
        Long workspaceId = Long.parseLong(inputData.in().toString());
        controller.deleteWorkspace(workspaceId);
    }

    /**
     * Handles the update of a workspace with new details.
     *
     * @param inputData   Input source for reading workspace details.
     * @param outputData  Output destination for displaying messages.
     * @param controller  Main controller for accessing workspace update functionality.
     */
    public static void handleUpdateWorkspace(Input inputData, OutputData outputData, MainController controller) {
        final String workspaceTypeMessage = "Введите id рабочего пространства:";
        outputData.output(workspaceTypeMessage);
        String workspaceTypeId = inputData.in().toString();

        final String startDateMessage = "Введите год, месяц, день, часы, минуты начала рабочего пространства, через пробел:";
        outputData.output(startDateMessage);
        List<String> startArr = new ArrayList<>(List.of(inputData.in().toString().split(" ")));
        LocalDateTime startDate = LocalDateTime.of(Integer.parseInt(startArr.get(0)),
                Integer.parseInt(startArr.get(1)),
                Integer.parseInt(startArr.get(2)),
                Integer.parseInt(startArr.get(3)),
                Integer.parseInt(startArr.get(4)));

        final String endDateMessage = "Введите год, месяц, день, часы, минуты конца рабочего пространства, через пробел:";
        outputData.output(endDateMessage);
        List<String> endArr = new ArrayList<>(List.of(inputData.in().toString().split(" ")));
        LocalDateTime endDate = LocalDateTime.of(Integer.parseInt(endArr.get(0)),
                Integer.parseInt(endArr.get(1)),
                Integer.parseInt(endArr.get(2)),
                Integer.parseInt(endArr.get(3)),
                Integer.parseInt(endArr.get(4)));

        controller.updateWorkspace(Workspace.builder()
                .typeId(Long.parseLong(workspaceTypeId))
                .startDate(startDate)
                .endDate(endDate)
                .build());
    }


    /**
     * Formats the user details into a readable string.
     *
     * @param user The user to format.
     * @return A formatted string representing the user.
     */
    private static String formatUser(User user) {
        return String.format("login - %s | registration date - %s | %s",
                user.getLogin(), user.getCreatedAt(), user.getRole());
    }
}
