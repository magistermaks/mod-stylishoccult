package net.darktree.stylishoccult.config;

import net.darktree.stylishoccult.utils.DifficultyBased;

public class Settings {

	@Config.Entry(group="feature", min=0, max=100)
	public float boulder_chance = 8.0f;

	@Config.Entry(group="feature", min=0, max=100)
	public float boulder_fire_chance = 45.0f;

	@Config.Entry(group="feature", min=0, max=16)
	public float boulder_radius_base = 2.1f;

	@Config.Entry(group="feature", min=0, max=100)
	public float boulder_blackstone_chance = 20.0f;

	@Config.Entry(group="feature")
	public boolean boulder_erode = true;

	@Config.Entry(group="feature", min=1, restart=true)
	public int flesh_vain_size = 30;

	@Config.Entry(group="feature", min=1, restart=true)
	public int flesh_vain_count = 2;

	@Config.Entry(group="feature", min=1, restart=true)
	public int flesh_stone_vain_size = 12;

	@Config.Entry(group="feature", min=1, restart=true)
	public int flesh_stone_vain_count = 4;

	@Config.Entry(group="feature", min=0, max=100)
	public float grass_patch_chance = 98.0f;

	@Config.Entry(group="feature", min=0, max=100)
	public float fern_chance = 5.0f;

	@Config.Entry(group="feature", min=0, max=100)
	public float wall_chance = 5f;

	@Config.Entry(group="feature", min=0, max=100)
	public float wall_rune_chance = 45.0f;

	@Config.Entry(group="feature", min=0, max=100)
	public float demon_chance = 25.0f;

	@Config.Entry(group="feature", min=0, max=100)
	public float spark_vent_chance = 16.0f;

	@Config.Entry(group="demon", min=0, max=100)
	public float emitter_exposed = 44.0f;

	@Config.Entry(group="demon", min=0, max=100)
	public float emitter_buried = 26.0f;

	@Config.Entry(group="demon", min=0, max=100)
	public float disguise_chance = 14.0f;

	@Config.Entry(group="demon", min=0, max=100)
	public float calm_chance_1 = 40.0f;

	@Config.Entry(group="demon", min=0, max=100)
	public float calm_chance_2 = 50.0f;

	@Config.Entry(group="demon", min=0, max=100)
	public int calm_radius = 2;

	@Config.Entry(group="demon", min=0)
	public float fire_ball_speed = 0.821f;

	@Config.Entry(group="demon", min=0)
	public int fire_ball_amount_min = 4;

	@Config.Entry(group="demon", min=0)
	public int fire_ball_timeout_min = 180;

	@Config.Entry(group="demon", min=0)
	public float spark_spawn_chance = 0.5263f;

	@Config.Entry(group="demon", min=1)
	public int spread_lock_buried_rarity = 6;

	@Config.Entry(group="demon", min=1)
	public int spread_lock_exposed_rarity = 10;

	@Config.Entry(group="demon", min=1)
	public int max_search_radius = 5;

	@Config.Entry(group="other", min=0)
	public int poison_time_min = 40;

	@Config.Entry(group="other", min=0)
	public int rune_blood_yield = 100;

	@Config.Entry(group="other", min=1, restart=true)
	public float spark_health = 1.0f;

	@Config.Entry(group="other", min=0, restart=true)
	public float spark_damage = 1.0f;

	@Config.Entry(group="other", min=0)
	public float spark_selfharm = 0.4f;

	@Config.Entry(group="other", min=1, restart=true)
	public float spore_health = 1.0f;

	@Config.Entry(group="other", min=0, restart=true)
	public float spore_damage = 1.0f;

	@Config.Entry(group="other", min=0)
	public float spore_selfharm = 0.4f;

	@Config.Entry(group="other", min=0, max=100)
	public float bloody_flesh_chance = 12.5f;

	@Config.Entry(group="other")
	public boolean infinite_flesh_growth = false;

	@Config.Entry(group="other", restart=true)
	public boolean add_guide_to_loottables = true;

	@Config.Entry(group="difficulty")
	public DifficultyBased spread_anger_chance = new DifficultyBased(50.0f, 33.0f, 25.0f, 20.0f);

	@Config.Entry(group="difficulty")
	public DifficultyBased spark_live_time = new DifficultyBased(13, 10, 5, 4);

	@Config.Entry(group="difficulty")
	public DifficultyBased vent_timeout = new DifficultyBased(150, 250, 350, 400);

	@Config.Entry(group="difficulty")
	public DifficultyBased rune_error_explosion = new DifficultyBased(2.5f, 2.0f, 1.5f, 1.0f);

}
