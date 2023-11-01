package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import java.util.Optional;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.Group;
import seedu.address.model.group.exceptions.TaskException;
import seedu.address.model.group.tasks.TaskInitializer;
import seedu.address.model.group.tasks.TaskList;

public class TasksCommand extends Command {

    public static final String COMMAND_WORD = "tasks";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Lists out all tasks for a specific group.\n"
        + "Parameters: Group Number\n"
        + "Example: " + COMMAND_WORD + " 3";

    public static final String MESSAGE_SUCCESS = "Listing out tasks for group %1$s";
    public static final String MESSAGE_TASK_GROUP_NOT_FOUND = "Group with the provided group number not found.";


    private final int groupId;

    public TasksCommand(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Retrieve the group using the groupId
        Optional<Group> optionalGroup = model.getGroupWithNumber(groupId);
        if (optionalGroup.isEmpty()) {
            throw new CommandException(MESSAGE_TASK_GROUP_NOT_FOUND);
        }

        Group group = optionalGroup.get();

        TaskList taskList = group.getTasks();

        if (taskList.isEmpty()) {
            try {
                taskList = TaskInitializer.initializeTasks();
            } catch (TaskException e) {
                throw new RuntimeException(e);
            }
            group.addTasks(taskList);
        }

        String displayedTasks = taskList.toString();

        return new CommandResult(String.format(MESSAGE_SUCCESS, groupId) + "\n" + displayedTasks);
    }

    @Override
    public boolean equals(Object other) {
        // basic checks
        if (other == this) {
            return true;
        }
        if (!(other instanceof TasksCommand)) {
            return false;
        }

        // property checks
        TasksCommand otherTasksCommand = (TasksCommand) other;
        return groupId == otherTasksCommand.groupId;
    }
}
