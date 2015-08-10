package fr.pitiqui.survivalcraft;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class TitleManager {
    public static void sendTitle(Player player, String msgTitle, String msgSubTitle, int ticks){
        IChatBaseComponent chatTitle = ChatSerializer.a("{\"text\": \"" + msgTitle + "\"}");
        IChatBaseComponent chatSubTitle = ChatSerializer.a("{\"text\": \"" + msgSubTitle + "\"}");
        PacketPlayOutTitle p = new PacketPlayOutTitle(EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle p2 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, chatSubTitle);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(p);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(p2);
        sendTime(player, ticks);
	}
	
	private static void sendTime(Player player, int ticks){
	        PacketPlayOutTitle p = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, 20, ticks, 20);
	        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(p);
	}
	
	public static void sendActionBar(Player player, String message){
	        IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
	        PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
	        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(ppoc);
	}
	
    public static void setPlayerList(Player player, String header, String footer){
        IChatBaseComponent hj = ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent fj = ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter packet = (PacketPlayOutPlayerListHeaderFooter) constructHeaderAndFooterPacket(hj, fj);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}
	
	private static Object constructHeaderAndFooterPacket(Object header, Object footer){
	        try{
	                Object packet = PacketPlayOutPlayerListHeaderFooter.class.newInstance();
	                if(header != null){
	                        Field field = PacketPlayOutPlayerListHeaderFooter.class.getDeclaredField("a");
	                        field.setAccessible(true);
	                        field.set(packet, header);
	                        field.setAccessible(false);
	                }
	                if(footer != null){
	                        Field field = PacketPlayOutPlayerListHeaderFooter.class.getDeclaredField("b");
	                        field.setAccessible(true);
	                        field.set(packet, footer);
	                        field.setAccessible(false);
	                }
	                return packet;
	        }catch (InstantiationException | IllegalAccessException | NoSuchFieldException e){
	                e.printStackTrace();
	        }
	        return null;
	}
}
