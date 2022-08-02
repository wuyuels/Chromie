// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.chromie.listener;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import org.chromie.dto.CollectData;
import org.chromie.service.MonitoringService;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @author liushuai7
 */
public class EditListener implements DocumentListener {

  @Override
  public void documentChanged(@NotNull DocumentEvent event) {


    MonitoringService monitoringService =
            ApplicationManager.getApplication().getService(MonitoringService.class);

    CollectData data = new CollectData();
    data.setW(1);
    monitoringService.add(data);
  }

}
