package org.chromie.util;


import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * 数据持久化
 * 文件存储
 * @author liushuai7
 */
public class FileUtil {
    public static String getFileData(String date) {
        VirtualFile fileData = VfsUtil.findFile(getDataPath(date), false);
        if (fileData == null){
            return "";
        }
        try {
            return new String(fileData.contentsToByteArray(true),StandardCharsets.UTF_8);
        } catch (Exception e) {}
        return "";
    }

    private static Path getDataPath(String date) {
        return Path.of("./chromie/"+date+".data");
    }

    public static void saveData(String date, long len, String data,String def) {
        @Nullable VirtualFile file = VfsUtil.findFile(getDataPath(date), false);
        StringBuilder text  ;
        try {
            if (file == null){
                file = VfsUtil.createChildSequent(null,VfsUtil.createDirectories("chromie" ),date,"data");
                text = new StringBuilder();
            }else {
                text = new StringBuilder(new String(file.contentsToByteArray(), StandardCharsets.UTF_8));
            }

            int l = text.toString().split("\n").length;
            for (int i = l; i <= len; i++) {
                if (i+1 == len){
                    @Nullable VirtualFile finalFile = file;
                    Runnable runnable = () -> {
                        try {
                            finalFile.setBinaryContent((text+data+"\n").getBytes(StandardCharsets.UTF_8));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    };
                    // 加入任务，由IDE调度任务
                    WriteCommandAction.runWriteCommandAction(null,runnable);
                }else {
                    text.append(def).append("\n");
                }
            }
        } catch (Exception e) {e.printStackTrace();}
    }
}
