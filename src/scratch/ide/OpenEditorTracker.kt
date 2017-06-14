/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scratch.ide

import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.fileEditor.FileEditorManagerListener.FILE_EDITOR_MANAGER
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.util.Disposer
import scratch.MrScratchManager
import scratch.ide.FileSystem

class OpenEditorTracker(
    private val mrScratchManager: MrScratchManager,
    private val fileSystem: FileSystem,
    private val application: Application = ApplicationManager.getApplication()
) {
    fun startTracking(): OpenEditorTracker {
        application.messageBus.connect().subscribe(ProjectManager.TOPIC, object : ProjectManagerListener {
            override fun projectOpened(project: Project) {
                val connection = project.messageBus.connect()
                connection.subscribe(FILE_EDITOR_MANAGER, object : FileEditorManagerListener {
                    override fun selectionChanged(event: FileEditorManagerEvent) {
                        val virtualFile = event.newFile ?: return
                        if (fileSystem.isScratch(virtualFile)) {
                            mrScratchManager.userOpenedScratch(virtualFile.name)
                        }
                    }
                })
                Disposer.register(project, connection)
            }
        })
        return this
    }
}