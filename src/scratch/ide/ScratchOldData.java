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
package scratch.ide;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmitry Kandalov
 */
@State(name = "ScratchData", storages = {@Storage(id = "main", file = "$APP_CONFIG$/scratch.xml")})
public class ScratchOldData implements PersistentStateComponent<ScratchOldData> {
	private static final int SIZE = 5;

	private final String scratchTexts[];
	private boolean appendContentFromClipboard;
	private String lastOpenedFileName;
	private boolean migrateToPhysicalFiles = true;
	private List<String> createdFileNames = new ArrayList<String>();

	public static ScratchOldData getInstance() {
		return ServiceManager.getService(ScratchOldData.class);
	}

	public ScratchOldData() {
		scratchTexts = new String[SIZE];
		Arrays.fill(scratchTexts, "");
	}

	// used by intellij serializer through reflection
	@SuppressWarnings({"UnusedDeclaration"})
	public String[] getScratchText() {
		return scratchTexts;
	}

	// used by intellij serializer through reflection
	@SuppressWarnings({"UnusedDeclaration"})
	public void setScratchText(String[] text) {
		System.arraycopy(text, 0, scratchTexts, 0, text.length);
	}

	@SuppressWarnings("UnusedDeclaration")
	public boolean isAppendContentFromClipboard() {
		return appendContentFromClipboard;
	}

	@SuppressWarnings("UnusedDeclaration")
	public void setAppendContentFromClipboard(boolean appendContentFromClipboard) {
		this.appendContentFromClipboard = appendContentFromClipboard;
	}

	public String[] getScratchTextInternal() {
		String result[] = new String[SIZE];
		for (int i = 0; i < result.length; i++) {
			result[i] = XmlUtil.unescape(scratchTexts[i]);
		}
		return result;
	}

	@Override
	public ScratchOldData getState() {
		return this;
	}

	@Override
	public void loadState(ScratchOldData scratchOldData) {
		XmlSerializerUtil.copyBean(scratchOldData, this);
	}

	@Nullable
	public String getLastOpenedFileName() {
		return lastOpenedFileName;
	}

	public void setLastOpenedFileName(@Nullable String lastOpenedFileName) {
		this.lastOpenedFileName = lastOpenedFileName;
	}

	public boolean isMigrateToPhysicalFiles() {
		return migrateToPhysicalFiles;
	}

	public void setMigrateToPhysicalFiles(boolean migrateToPhysicalFiles) {
		this.migrateToPhysicalFiles = migrateToPhysicalFiles;
	}

	public List<String> getCreatedFileNames() {
		return createdFileNames;
	}

	public void setCreatedFileNames(List<String> createdFileNames) {
		this.createdFileNames = createdFileNames;
	}

	public void addCreatedFile(String name) {
		if (!createdFileNames.contains(name)) {
			createdFileNames.add(name);
		}
	}

	public void removeCreatedFileName(String createdFileName) {
		createdFileNames.remove(createdFileName);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass()) {
			return false;
		} else {
			ScratchOldData that = (ScratchOldData) o;
			return Arrays.equals(scratchTexts, that.scratchTexts);
		}
	}

	@Override
	public int hashCode() {
		return scratchTexts == null ? 0 : Arrays.hashCode(scratchTexts);
	}
}
