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

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.OptionTag
import scratch.Scratch
import scratch.ScratchConfig
import scratch.ScratchConfig.AppendType
import scratch.ScratchConfig.DefaultScratchMeaning


@State(name = "ScratchConfig", storages = arrayOf(Storage(file = "scratch_config.xml")))
data class ScratchConfigPersistence(
    @OptionTag(valueAttribute = "isListenToClipboard")
    var listenToClipboard: Boolean = false,
    var fullScratchNamesOrdered: ArrayList<String> = ArrayList(), // This MUST BE an ArrayList for IJ serialization to work.
    var lastOpenedScratch: String? = null,
    var clipboardAppendType: AppendType? = null,
    var newScratchAppendType: AppendType? = null,
    var defaultScratchMeaning: DefaultScratchMeaning? = null,
    var scratchesFolderPath: String? = null
): PersistentStateComponent<ScratchConfigPersistence> {

    fun asConfig(): ScratchConfig {
        return ScratchConfig.defaultConfig
            .listenToClipboard(listenToClipboard)
            .with(fullScratchNamesOrdered.map { Scratch(it) })
            .withLastOpenedScratch(if (lastOpenedScratch == null) null else Scratch(lastOpenedScratch!!))
            .withDefaultScratchMeaning(defaultScratchMeaning)
            .withClipboard(clipboardAppendType)
            .withNewScratch(newScratchAppendType)
    }

    fun updateFrom(config: ScratchConfig) {
        listenToClipboard = config.listenToClipboard
        fullScratchNamesOrdered = ArrayList(config.scratches.map { it.fullNameWithMnemonics })
        lastOpenedScratch = config.lastOpenedScratch?.fullNameWithMnemonics
        defaultScratchMeaning = config.defaultScratchMeaning
    }

    override fun getState() = this

    override fun loadState(state: ScratchConfigPersistence) = XmlSerializerUtil.copyBean(state, this)

    companion object {
        val instance: ScratchConfigPersistence
            get() = ServiceManager.getService(ScratchConfigPersistence::class.java)
    }
}
