package eu.highgeek.paper.adapters;

import com.willfp.eco.core.items.Items;
import eu.highgeek.common.CommonMain;
import eu.highgeek.paper.PaperMain;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;

import javax.swing.plaf.basic.ComboPopup;
import java.util.Arrays;
import java.util.Objects;

public class ItemStackUtils {

    public static String toString(ItemStack itemStack){
        return Items.toSNBT(itemStack);
    }

    public static ItemStack fromString(String snbt){
        if (snbt == null || snbt.isBlank() || snbt.isEmpty()){
            return new ItemStack(Material.AIR);
        }
        try {
            ItemStack itemStack = Items.fromSNBT(snbt);
            if(itemStack.getType() == Material.AIR){
                return returnErrorBarrier();
            }
            return Objects.requireNonNullElseGet(itemStack, ItemStackUtils::returnErrorBarrier);
        }catch (Exception e){
            PaperMain.getMainInstance().getCommonLogger().warn("stringToItemStack exception: " + e.getMessage());
            return returnErrorBarrier();
        }
    }

    public static ItemStack returnErrorBarrier(){
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("Invalid Item");
        meta.setLore(Arrays.asList("Item doesn't exists on this server.", "If should contact admin."));
        item.setItemMeta(meta);
        return item;
    }

    public static Component itemStackToChatComponent(ItemStack item){
        //TODO test
        //BinaryTagHolder tagHolder = BinaryTagHolder.binaryTagHolder(toString(item));
        //HoverEvent.ShowItem showItem = HoverEvent.ShowItem.showItem(item.getType().getKey(), item.getAmount(), tagHolder);
        Component message = Component.text("[" + item.toString() + "]");
        message = message.hoverEvent(item);
        return message;
    }

}
