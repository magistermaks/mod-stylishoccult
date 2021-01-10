//package net.darktree.stylishoccult.items;
//
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.text.LiteralText;
//import net.minecraft.world.World;
//
//public class CorruptedBloodBottleItem extends BottleItem {
//
//    public CorruptedBloodBottleItem(Settings settings) {
//        super(settings);
//    }
//
//    @Override
//    public void onConsumed(ItemStack stack, World world, LivingEntity user) {
//        if( user instanceof PlayerEntity ) {
//            PlayerEntity player = (PlayerEntity) user;
//            player.sendMessage( new LiteralText("Consumed corrupted blood!"), true );
//        }
//    }
//
//}
