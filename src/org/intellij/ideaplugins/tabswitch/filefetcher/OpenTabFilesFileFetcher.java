package org.intellij.ideaplugins.tabswitch.filefetcher;

import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ArrayUtil;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Creates a list of {@link VirtualFile} by fetching all the files that are open in tabs in current project.
 */
public class OpenTabFilesFileFetcher implements FileFetcher<VirtualFile> {
  private static Set<String> ignoredExtensions = new HashSet<>();
  static {
    ignoredExtensions.add("class");
  }

  @Override
  public List<VirtualFile> getFiles(Project project) {
    FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
    return getOpenFiles(fileEditorManager, ArrayUtil.reverseArray(EditorHistoryManager.getInstance(project).getFiles()));
  }

  private List<VirtualFile> getOpenFiles(FileEditorManager fileEditorManager, VirtualFile[] recentFiles) {
    List<VirtualFile> openFiles = new LinkedList<>();
    int editorTabLimit = UISettings.getInstance().EDITOR_TAB_LIMIT;
    for (VirtualFile file : recentFiles) {
      if (openFiles.size() <= editorTabLimit && fileEditorManager.isFileOpen(file) && !openFiles.contains(file)
              && !ignoredExtensions.contains(file.getExtension())) {
        openFiles.add(file);
      }
    }
    return openFiles;
  }
}
