package net.darktree.stylishoccult.data;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import net.darktree.stylishoccult.StylishOccult;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

public abstract class SimpleDirectoryResourceReloadListener implements SimpleSynchronousResourceReloadListener {

	protected final static JsonParser PARSER = new JsonParser();
	protected final static Gson GSON = new Gson();
	private final String path;

	protected SimpleDirectoryResourceReloadListener(String path) {
		this.path = path;
	}

	@Override
	public void reload(ResourceManager manager) {
		onReloadStart();

		for(Identifier id : manager.findResources(this.path, path -> path.endsWith(".json"))) {
			if (id.getNamespace().equals(StylishOccult.NAMESPACE)) {
				try (InputStream stream = manager.getResource(id).getInputStream()) {
					apply(id, new InputStreamReader(stream, StandardCharsets.UTF_8));
				} catch (Exception e) {
					StylishOccult.LOGGER.error("Error occurred while loading resource json: " + id, e);
				}
			}
		}

		onReloadEnd();
	}

	public void onReloadStart() {

	}

	public void onReloadEnd() {

	}

	public abstract void apply(Identifier identifier, Reader reader);

}
