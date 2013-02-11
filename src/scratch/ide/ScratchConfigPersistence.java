package scratch.ide;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;
import scratch.ScratchConfig;
import scratch.ScratchInfo;

import java.util.List;

import static com.intellij.util.containers.ContainerUtilRt.newArrayList;

/**
 * User: dima
 * Date: 10/02/2013
 */
@State(name = "ScratchConfig", storages = {@Storage(id = "main", file = "$APP_CONFIG$/scratch_config.xml")})
public class ScratchConfigPersistence implements PersistentStateComponent<ScratchConfigPersistence> {
	private boolean isNeedMigration = true;
	private boolean isListenToClipboard = false;
	private List<String> fullScratchNamesOrdered = newArrayList();

	public static ScratchConfigPersistence getInstance() {
		return ServiceManager.getService(ScratchConfigPersistence.class);
	}

	public ScratchConfig asConfig() {
		return ScratchConfig.DEFAULT_CONFIG
				.needsMigration(isNeedMigration)
				.listenToClipboard(isListenToClipboard)
				.with(ContainerUtil.map(fullScratchNamesOrdered, new Function<String, ScratchInfo>() {
					@Override public ScratchInfo fun(String it) {
						return ScratchInfo.createFrom(it);
					}
				}));
	}

	public void updateFrom(ScratchConfig config) {
		isNeedMigration = config.needMigration;
		isListenToClipboard = config.listenToClipboard;
		fullScratchNamesOrdered = newArrayList(ContainerUtil.map(config.scratchInfos, new Function<ScratchInfo, String>() {
					@Override public String fun(ScratchInfo it) {
						return it.fullNameWithMnemonics();
					}
		}));
	}

	public List<String> getFullScratchNamesOrdered() {
		return fullScratchNamesOrdered;
	}

	public void setFullScratchNamesOrdered(List<String> fullScratchNamesOrdered) {
		this.fullScratchNamesOrdered = fullScratchNamesOrdered;
	}

	public boolean isListenToClipboard() {
		return isListenToClipboard;
	}

	public void setListenToClipboard(boolean listenToClipboard) {
		isListenToClipboard = listenToClipboard;
	}

	public boolean isNeedMigration() {
		return isNeedMigration;
	}

	public void setNeedMigration(boolean needMigration) {
		isNeedMigration = needMigration;
	}

	@Nullable @Override public ScratchConfigPersistence getState() {
		return this;
	}

	@Override public void loadState(ScratchConfigPersistence state) {
		XmlSerializerUtil.copyBean(state, this);
	}

	@SuppressWarnings("RedundantIfStatement")
	@Override public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ScratchConfigPersistence that = (ScratchConfigPersistence) o;

		if (isListenToClipboard != that.isListenToClipboard) return false;
		if (isNeedMigration != that.isNeedMigration) return false;
		if (fullScratchNamesOrdered != null ? !fullScratchNamesOrdered.equals(that.fullScratchNamesOrdered) : that.fullScratchNamesOrdered != null)
			return false;

		return true;
	}

	@Override public int hashCode() {
		int result = (isNeedMigration ? 1 : 0);
		result = 31 * result + (isListenToClipboard ? 1 : 0);
		result = 31 * result + (fullScratchNamesOrdered != null ? fullScratchNamesOrdered.hashCode() : 0);
		return result;
	}
}
