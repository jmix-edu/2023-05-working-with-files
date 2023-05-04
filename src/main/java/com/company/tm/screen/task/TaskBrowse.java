package com.company.tm.screen.task;

import com.company.tm.app.TaskImportService;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.ui.Notifications;
import io.jmix.ui.Notifications.NotificationType;
import io.jmix.ui.UiComponents;
import io.jmix.ui.component.*;
import io.jmix.ui.download.Downloader;
import io.jmix.ui.model.CollectionLoader;
import io.jmix.ui.model.DataContext;
import io.jmix.ui.screen.*;
import com.company.tm.entity.Task;
import io.jmix.ui.screen.LookupComponent;
import io.jmix.ui.upload.TemporaryStorage;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@UiController("tm_Task.browse")
@UiDescriptor("task-browse.xml")
@LookupComponent("tasksTable")
public class TaskBrowse extends StandardLookup<Task> {

    @Autowired
    private FileStorageUploadField importTaskField;

    @Autowired
    private CollectionLoader<Task> tasksDl;

    @Autowired
    private TaskImportService taskImportService;
    @Autowired
    private Notifications notifications;
    @Autowired
    private UiComponents uiComponents;
    @Autowired
    private Downloader downloader;
    @Autowired
    private TemporaryStorage temporaryStorage;
    @Autowired
    private DataManager dataManager;

    @Subscribe("importBtn")
    public void onImportBtnClick(Button.ClickEvent event) {
        int tasks = taskImportService.importTasks();
        if (tasks > 0) {
            notifications.create()
                    .withCaption(tasks + " tasks imported")
                    .withType(NotificationType.TRAY)
                    .show();
        }

        tasksDl.load();
    }

    @Install(to = "tasksTable.attachment", subject = "columnGenerator")
    private Component tasksTableAttachmentColumnGenerator(Task task) {
        if (task.getAttachment() != null) {
            LinkButton linkButton = uiComponents.create(LinkButton.class);

            linkButton.setCaption(task.getAttachment().getFileName());
            linkButton.addClickListener(clickEvent ->
                    downloader.download(task.getAttachment()));

            return linkButton;
        }

        return null;
    }

    @Subscribe("importTaskField")
    public void onImportTaskFieldFileUploadSucceed(SingleFileUploadField.FileUploadSucceedEvent event) throws IOException {
        UUID fileId = importTaskField.getFileId();
        if (fileId == null) {
            return;
        }

        File file = temporaryStorage.getFile(fileId);
        if (file != null) {
            processFile(file);
            temporaryStorage.deleteFile(fileId);
        }
    }

    private void processFile(File file) throws IOException {
        List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
        SaveContext saveContext = new SaveContext();
        for (String line : lines) {
            Task task = dataManager.create(Task.class);
            task.setName(line);
            saveContext.saving(task);
        }

        dataManager.save(saveContext);
        tasksDl.load();
    }
}