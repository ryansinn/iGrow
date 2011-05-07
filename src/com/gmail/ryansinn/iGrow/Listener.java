package com.gmail.ryansinn.iGrow;

import java.io.PrintStream;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Listener
{
  public static void onEvent(iGrow plugin)
  {
    for (Player p2 : plugin.getServer().getOnlinePlayers()) {
      Pattern pattern = Pattern.compile("\\s*"+p2.getWorld().getName()+"\\s*"); // compile our regex check for matching world here so only once per player
      plugin.sMdebug("MineCraft Time: " + p2.getWorld().getTime());
      for (int x = (int)p2.getLocation().getX() - plugin.AREA_; x <= p2.getLocation().getX() + plugin.AREA_; x++)
        for (int y = (int)p2.getLocation().getY() - plugin.AREA_; y <= p2.getLocation().getY() + plugin.AREA_; y++)
          for (int z = (int)p2.getLocation().getZ() - plugin.AREA_; z <= p2.getLocation().getZ() + plugin.AREA_; z++)
            for (Recipe r : plugin.Recipes) {
              Matcher matcher = pattern.matcher(r.world);
              if (r.world == "" || matcher.matches()) {
	              if (p2.getWorld().getBlockTypeIdAt(x, y, z) == r.needBlock) {
	                ChangeBlocks(p2.getWorld(), plugin, p2.getWorld().getBlockAt(x, y, z), r);
	              }
	              if ((!r.Near) || 
	                (p2.getWorld().getBlockTypeIdAt(x, y, z) != r.newBlock)) continue;
	              ChangeBlocks(p2.getWorld(), plugin, p2.getWorld().getBlockAt(x, y, z), r);
	            }
            }
    }
  }

  public static void ChangeBlocks(World world, iGrow plugin, Block b, Recipe r)
  {
    if ((b.getTypeId() == r.needBlock) && 
      (isConnected(world, b.getLocation(), r.oldBlock))) {
      int random = new Random().nextInt(r.Chance[1]) + 1;
      if (random <= r.Chance[0]) {
        ChangeBlock(b, getConnected(world, b.getLocation(), r.oldBlock), r.oldBlock, r.newBlock);
      }
    }

    if ((b.getTypeId() == r.newBlock) && (r.Near) && 
      (isConnected(world, b.getLocation(), r.oldBlock))) {
      int random = new Random().nextInt(r.Chance[1]) + 1;
      if (random <= r.Chance[0])
        ChangeBlock(b, getConnected(world, b.getLocation(), r.oldBlock), r.oldBlock, r.newBlock);
    }
  }

  public static String getConnected(World world, Location here, int block)
  {
    String value = "";
    if (world.getBlockAt((int)here.getX() - 1, (int)here.getY(), (int)here.getZ()).getTypeId() == block) {
      value = value + "1, ";
    }
    if (world.getBlockAt((int)here.getX() + 1, (int)here.getY(), (int)here.getZ()).getTypeId() == block) {
      value = value + "2, ";
    }
    if (world.getBlockAt((int)here.getX(), (int)here.getY() + 1, (int)here.getZ()).getTypeId() == block) {
      value = value + "3, ";
    }
    if (world.getBlockAt((int)here.getX(), (int)here.getY() - 1, (int)here.getZ()).getTypeId() == block) {
      value = value + "4, ";
    }
    if (world.getBlockAt((int)here.getX(), (int)here.getY(), (int)here.getZ() + 1).getTypeId() == block) {
      value = value + "5, ";
    }
    if (world.getBlockAt((int)here.getX(), (int)here.getY(), (int)here.getZ() - 1).getTypeId() == block) {
      value = value + "6, ";
    }
    return value;
  }

  public static boolean isConnected(World world, Location here, int block) {
    if (world.getBlockAt((int)here.getX() - 1, (int)here.getY(), (int)here.getZ()).getTypeId() == block) {
      return true;
    }
    if (world.getBlockAt((int)here.getX() + 1, (int)here.getY(), (int)here.getZ()).getTypeId() == block) {
      return true;
    }
    if (world.getBlockAt((int)here.getX(), (int)here.getY() + 1, (int)here.getZ()).getTypeId() == block) {
      return true;
    }
    if (world.getBlockAt((int)here.getX(), (int)here.getY() - 1, (int)here.getZ()).getTypeId() == block) {
      return true;
    }
    if (world.getBlockAt((int)here.getX(), (int)here.getY(), (int)here.getZ() + 1).getTypeId() == block) {
      return true;
    }

    return world.getBlockAt((int)here.getX(), (int)here.getY(), (int)here.getZ() - 1).getTypeId() == block;
  }

  public static void ChangeBlock(Block b, String value, int from, int id)
  {
    String[] values = { "", "", "", "", "", "" };
    values = value.split(", ");

    if (b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY() + 1, (int)b.getLocation().getZ()).getY() > 128)
      return;
    if (b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY() - 1, (int)b.getLocation().getZ()).getY() < 0) {
      return;
    }

    if (values[0].equals("1")) {
      Block block = b.getWorld().getBlockAt((int)b.getLocation().getX() - 1, (int)b.getLocation().getY(), (int)b.getLocation().getZ());
      if (block.getType().equals(Material.getMaterial(from))) block.setType(Material.getMaterial(id));
    }
    if (values[0].equals("2")) {
      Block block = b.getWorld().getBlockAt((int)b.getLocation().getX() + 1, (int)b.getLocation().getY(), (int)b.getLocation().getZ());
      if (block.getType().equals(Material.getMaterial(from))) block.setType(Material.getMaterial(id));
    }
    if (values[0].equals("3")) {
      Block block = b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY() + 1, (int)b.getLocation().getZ());
      if (block.getType().equals(Material.getMaterial(from))) block.setType(Material.getMaterial(id));
    }
    if (values[0].equals("4")) {
      Block block = b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY() - 1, (int)b.getLocation().getZ());
      if (block.getType().equals(Material.getMaterial(from))) block.setType(Material.getMaterial(id));
    }
    if (values[0].equals("5")) {
      Block block = b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY(), (int)b.getLocation().getZ() + 1);
      if (block.getType().equals(Material.getMaterial(from))) block.setType(Material.getMaterial(id));
    }
    if (values[0].equals("6")) {
      Block block = b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY(), (int)b.getLocation().getZ() - 1);
      if (block.getType().equals(Material.getMaterial(from))) block.setType(Material.getMaterial(id));
    }
  }
}

/* Location:           C:\Users\Robin\Downloads\iGrow.jar
 * Qualified Name:     com.bukkit.techguard.igrow.Listener
 * JD-Core Version:    0.6.0
 */