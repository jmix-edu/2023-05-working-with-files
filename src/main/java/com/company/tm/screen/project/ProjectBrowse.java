package com.company.tm.screen.project;

import io.jmix.ui.screen.*;
import com.company.tm.entity.Project;

@UiController("tm_Project.browse")
@UiDescriptor("project-browse.xml")
@LookupComponent("projectsTable")
public class ProjectBrowse extends StandardLookup<Project> {
}