package net.darktree.stylishoccult.gui.patchouli;

import net.darktree.stylishoccult.data.ResourceLoaders;
import net.darktree.stylishoccult.data.json.AltarRitual;
import net.minecraft.util.Identifier;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class RuneRecipeProcessor implements IComponentProcessor {

	private transient AltarRitual ritual;

	@Override
	public void setup(IVariableProvider variables) {
		ritual = ResourceLoaders.ALTAR_RITUALS.find(new Identifier(variables.get("recipe").asString()));
	}

	@Override
	public IVariable process(String key) {
		if (key.equals("blood")) {
			return IVariable.wrap(ritual.blood);
		}

		return null;
	}

}

