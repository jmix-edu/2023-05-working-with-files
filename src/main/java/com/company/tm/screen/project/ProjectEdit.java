package com.company.tm.screen.project;

import io.jmix.ui.screen.*;
import com.company.tm.entity.Project;

@UiController("tm_Project.edit")
@UiDescriptor("project-edit.xml")
@EditedEntityContainer("projectDc")
public class ProjectEdit extends StandardEditor<Project> {
}