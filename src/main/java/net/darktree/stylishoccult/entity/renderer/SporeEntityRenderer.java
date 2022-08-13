package net.darktree.stylishoccult.entity.renderer;

import net.darktree.stylishoccult.entity.SparkEntity;
import net.darktree.stylishoccult.utils.ModIdentifier;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

public class SporeEntityRenderer extends SparkEntityRenderer {

	private static final Identifier TEXTURE = new ModIdentifier("textures/particle/spore.png");

	public SporeEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	protected int getLight(int i) {
		return i;
	}

	@Override
	public Identifier getTexture(SparkEntity sparkEntity) {
		return TEXTURE;
	}

}
