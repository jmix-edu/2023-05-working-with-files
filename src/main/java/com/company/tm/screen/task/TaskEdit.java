package com.company.tm.screen.task;

import com.company.tm.entity.Project;
import com.company.tm.entity.Task;
import com.company.tm.entity.User;
import io.jmix.core.usersubstitution.CurrentUserSubstitution;
import io.jmix.ui.component.BrowserFrame;
import io.jmix.ui.component.FileStorageResource;
import io.jmix.ui.model.InstanceContainer;
import io.jmix.ui.screen.*;
import org.springframework.beans.factory.annotation.Autowired;

@UiController("tm_Task.edit")
@UiDescriptor("task-edit.xml")
@EditedEntityContainer("taskDc")
public class TaskEdit extends StandardEditor<Task> {

    @Autowired
    private CurrentUserSubstitution currentUserSubstitution;

    @Autowired
    private BrowserFrame attachmentBrowserFrame;

    @Subscribe
    public void onInitEntity(InitEntityEvent<Task> event) {
        User user = (User) currentUserSubstitution.getEffectiveUser();
        event.getEntity().setAssignee(user);
    }

    @Subscribe
    public void onBeforeShow(BeforeShowEvent event) {
        refreshAttachmentPreview();
    }

    @Subscribe(id = "taskDc", target = Target.DATA_CONTAINER)
    public void onTaskDcItemPropertyChange(InstanceContainer.ItemPropertyChangeEvent<Task> event) {
        if ("project".equals(event.getProperty())) {
            Project newProject = ((Project) event.getValue());
            if (newProject != null) {
                event.getItem().setPriority(newProject.getDefaultTaskPriority());
            }
        }

        if ("attachment".equals(event.getProperty())) {
            refreshAttachmentPreview();
        }
    }

    private void refreshAttachmentPreview() {
        Task task = getEditedEntity();
        if (task.getAttachment() != null) {
            attachmentBrowserFrame.setSource(FileStorageResource.class)
                    .setFileReference(task.getAttachment())
                    .setMimeType(task.getAttachment().getContentType());
        }
    }


}