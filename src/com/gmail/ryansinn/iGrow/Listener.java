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
import org.bukkit.DyeColor;
import org.bukkit.TreeSpecies;

public class Listener
{
  public static void onEvent(iGrow plugin)
  {
    for (Player p2 : plugin.getServer().getOnlinePlayers()) {
      Pattern pattern = Pattern.compile("\\s*"+p2.getWorld().getName()+"\\s*"); // compile our regex check for matching world here so only once per player
      plugin.sMdebug("MineCraft Time: " + p2.getWorld().getTime() + " | " + p2.getName() + "@" + p2.getWorld().getName());
      for (int x = (int)p2.getLocation().getX() - plugin.AREA_; x <= p2.getLocation().getX() + plugin.AREA_; x++)
        for (int y = (int)p2.getLocation().getY() - plugin.AREA_; y <= p2.getLocation().getY() + plugin.AREA_; y++)
          for (int z = (int)p2.getLocation().getZ() - plugin.AREA_; z <= p2.getLocation().getZ() + plugin.AREA_; z++)
            for (Recipe r : plugin.Recipes) {
              Matcher matcher = pattern.matcher(r.world);
              if (r.world == "" || matcher.matches()) {
            	  // If data exists for the needed block, check for a match - skip if no match.
            	  if (r.needBlockData != "") {
            		  //plugin.sMdebug("Checking data. - "+r.needBlock+"@"+r.needBlockData);
            		  if (r.needBlock.matches("LEAVES")) {
            			  //plugin.sMdebug("Leaves data found.");
            			  if (p2.getWorld().getBlockAt(x, y, z).getData() != TreeSpecies.valueOf(r.needBlockData).getData()) continue;
            		  }
            		  if (r.needBlock.matches("LOG")) {
            			  //plugin.sMdebug("Leaves data found.");
            			  if (p2.getWorld().getBlockAt(x, y, z).getData() != TreeSpecies.valueOf(r.needBlockData).getData()) continue;
            		  }
            		  if (r.needBlock.matches("WOOL")) {
            			  //plugin.sMdebug("Wool data found.");
            			  if (p2.getWorld().getBlockAt(x, y, z).getData() != DyeColor.valueOf(r.needBlockData).getData()) continue;
            		  }
            	  }
            	  if (p2.getWorld().getBlockAt(x, y, z).getType() == Material.valueOf(r.needBlock)) {
            		  plugin.sMdebug("Recipe matched - found: " + r.needBlock + "@"+r.needBlockData+", " + r.oldBlock + "@" + r.oldBlockData + "->" + r.newBlock + "@"+r.newBlockData+":" + r.world);
            		  ChangeBlocks(p2.getWorld(), plugin, p2.getWorld().getBlockAt(x, y, z), r);
            	  }
            	  if ((!r.Near) || 
            			  (p2.getWorld().getBlockAt(x, y, z).getType() != Material.valueOf(r.newBlock))) continue;

            	  ChangeBlocks(p2.getWorld(), plugin, p2.getWorld().getBlockAt(x, y, z), r);
              }
            }
    }
  }

  public static void ChangeBlocks(World world, iGrow plugin, Block neededBlock, Recipe r)
  {
	  String connections = getConnected(world, neededBlock.getLocation(), Material.valueOf(r.oldBlock), r.oldBlockData);

	  if ((neededBlock.getType() == Material.valueOf(r.needBlock)) && connections != "") {
		  int random = new Random().nextInt(r.Chance[1]) + 1;
		  if (random <= r.Chance[0]) {
			  ChangeBlock(neededBlock, connections, r);
		  }
	  }
  }

  public static String getConnected(World world, Location here, Material blockMat, String data)
  {
	  String value = "";
	  if (CheckBlock(world.getBlockAt((int)here.getX() - 1, (int)here.getY(), (int)here.getZ()), blockMat, data)) {
		  value = value + "1, ";
	  }
	  if (CheckBlock(world.getBlockAt((int)here.getX() + 1, (int)here.getY(), (int)here.getZ()), blockMat, data)) {
		  value = value + "2, ";
	  }
	  if (CheckBlock(world.getBlockAt((int)here.getX(), (int)here.getY() - 1, (int)here.getZ()), blockMat, data)) {
		  value = value + "3, ";
	  }
	  if (CheckBlock(world.getBlockAt((int)here.getX(), (int)here.getY() + 1, (int)here.getZ()), blockMat, data)) {
		  value = value + "4, ";
	  }
	  if (CheckBlock(world.getBlockAt((int)here.getX(), (int)here.getY(), (int)here.getZ() - 1), blockMat, data)) {
		  value = value + "5, ";
	  }
	  if (CheckBlock(world.getBlockAt((int)here.getX(), (int)here.getY(), (int)here.getZ() + 1), blockMat, data)) {
		  value = value + "6, ";
	  }
	  return value;
  }

  public static boolean CheckBlock(Block checkBlock, Material originalBlockMat, String originalBlockData)
  {
	  //System.out.println("checkblock starts, blocktype: "+checkBlock.getType().name()+"@" + DyeColor.getByData(checkBlock.getData()).name()+ ", originalblockmat: "+originalBlockMat.name()+"@"+originalBlockData);
	  if (checkBlock.getType() == originalBlockMat)
	  {
		  //System.out.println("checking data");
		  if (originalBlockData != "")
		  {
			  if (originalBlockMat.name().matches("LEAVES")) {
				  //System.out.println("checking leaves data");
				  if (TreeSpecies.getByData(checkBlock.getData()).name().matches(originalBlockData)) {
					  return true;
				  } else {
					  return false;
				  }
			  } else if (originalBlockMat.name().matches("WOOL")) {
				  //System.out.println("checking wool data");
				  if (DyeColor.getByData(checkBlock.getData()).name().matches(originalBlockData)) {
					  //System.out.println("checking wool data -true");
					  return true;
				  } else {
					  return false;
				  }
			  } else if (originalBlockMat.name().matches("LOG")) {
				  //System.out.println("checking wool data");
				  if (TreeSpecies.getByData(checkBlock.getData()).name().matches(originalBlockData)) {
					  //System.out.println("checking wool data -true");
					  return true;
				  } else {
					  return false;
				  }
			  } else {
				  return false;
			  }
		  } else {
			  return true;
		  }

	  }
	  return false;
  }

  public static void ChangeBlock(Block b, String value, Recipe r) //, Material from, Material id)
  {
	  //System.out.println("Changing block: value="+value);
	  Material from = Material.valueOf(r.oldBlock);

	  String[] values = { "", "", "", "", "", "" };
	  values = value.split(", ");

	  // Make sure we don't go out of the worlds Y bounds (0 < Y < 128)
	  if (b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY() + 1, (int)b.getLocation().getZ()).getY() > 128)
		  return;
	  if (b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY() - 1, (int)b.getLocation().getZ()).getY() < 0) {
		  return;
	  }

	  //   if (values[0].equals("1")) {
	  //     Block block = b.getWorld().getBlockAt((int)b.getLocation().getX() - 1, (int)b.getLocation().getY(), (int)b.getLocation().getZ());
	  //        if (block.getType().equals(Material.getMaterial(from))) block.setType(Material.getMaterial(id));
	  //  }

	  if (values[0].equals("1")) {
		  Block block = b.getWorld().getBlockAt((int)b.getLocation().getX() - 1, (int)b.getLocation().getY(), (int)b.getLocation().getZ());
		  if (block.getType().equals(from)) SetBlock(block, r);
	  }
	  if (values[0].equals("2")) {
		  Block block = b.getWorld().getBlockAt((int)b.getLocation().getX() + 1, (int)b.getLocation().getY(), (int)b.getLocation().getZ());
		  if (block.getType().equals(from)) SetBlock(block, r);
	  }
	  if (values[0].equals("3")) {
		  Block block = b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY() - 1, (int)b.getLocation().getZ());
		  if (block.getType().equals(from)) SetBlock(block, r);
	  }
	  if (values[0].equals("4")) {
		  Block block = b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY() + 1, (int)b.getLocation().getZ());
		  //System.out.println("Changing block: values[0]="+values[0]+" block: "+block.getType().name()+"from="+from.name());
		  if (block.getType().equals(from)) SetBlock(block, r);
	  }
	  if (values[0].equals("5")) {
		  Block block = b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY(), (int)b.getLocation().getZ() - 1);
		  if (block.getType().equals(from)) SetBlock(block, r);
	  }
	  if (values[0].equals("6")) {
		  Block block = b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY(), (int)b.getLocation().getZ() + 1);
		  if (block.getType().equals(from)) SetBlock(block, r);
	  }
  }


  public static void SetBlock(Block b, Recipe r) // b = oldBlock, r is for newBlock and newBlockData
  {
	  b.setType(Material.valueOf(r.newBlock));
	  //System.out.println("setting value:" +b.getType().name()+"@"+DyeColor.getByData(b.getData()).name()+ " -> "+r.newBlock+"@"+r.newBlockData);
	  if (r.newBlockData != "")
	  {
		  if (r.newBlock.matches("LEAVES")) {
			  //System.out.println("setting leaves data");
			  b.setData(TreeSpecies.valueOf(r.newBlockData).getData());
		  }
		  if (r.newBlock.matches("LOG")) {
			  //System.out.println("setting logs data");
			  b.setData(TreeSpecies.valueOf(r.newBlockData).getData());
		  }
		  if (r.newBlock.matches("WOOL")) {
			  //System.out.println("setting wool data");
			  b.setData(DyeColor.valueOf(r.newBlockData).getData());
		  }
	  }

  }
  }

/* Location:           C:\Users\Robin\Downloads\iGrow.jar
 * Qualified Name:     com.bukkit.techguard.igrow.Listener
 * JD-Core Version:    0.6.0
 */