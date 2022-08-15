package org.chromie.listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.chromie.service.MonitoringService;
import org.jetbrains.annotations.NotNull;

/**
 * Listener to detect project open and close.
 * @author liushuai7
 */
public class ProjectOpenCloseListener implements ProjectManagerListener {

  /**
   * Invoked on project open.
   *
   * @param project opening project
   */
  @Override
  public void projectOpened(@NotNull Project project) {

    MonitoringService monitoringService =
            ApplicationManager.getApplication().getService(MonitoringService.class);
    monitoringService.init();
  }
}
