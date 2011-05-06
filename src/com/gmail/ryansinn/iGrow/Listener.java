/*     */ package com.gmail.ryansinn.iGrow;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Random;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Player;
/*     */ 
/*     */ public class Listener
/*     */ {
/*     */   public static void onEvent(iGrow plugin)
/*     */   {
/*  18 */     for (Player p2 : plugin.getServer().getOnlinePlayers()) {
/*  19 */       System.out.println("MineCraft Time: " + p2.getWorld().getTime());
/*  20 */       for (int x = (int)p2.getLocation().getX() - plugin.AREA_; x <= p2.getLocation().getX() + plugin.AREA_; x++)
/*  21 */         for (int y = (int)p2.getLocation().getY() - plugin.AREA_; y <= p2.getLocation().getY() + plugin.AREA_; y++)
/*  22 */           for (int z = (int)p2.getLocation().getZ() - plugin.AREA_; z <= p2.getLocation().getZ() + plugin.AREA_; z++)
/*  23 */             for (Recipe r : plugin.Recipes) {
/*  24 */               if (p2.getWorld().getBlockTypeIdAt(x, y, z) == r.needBlock) {
/*  25 */                 ChangeBlocks(p2.getWorld(), plugin, p2.getWorld().getBlockAt(x, y, z), r);
/*     */               }
/*  27 */               if ((!r.Near) || 
/*  28 */                 (p2.getWorld().getBlockTypeIdAt(x, y, z) != r.newBlock)) continue;
/*  29 */               ChangeBlocks(p2.getWorld(), plugin, p2.getWorld().getBlockAt(x, y, z), r);
/*     */             }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static void ChangeBlocks(World world, iGrow plugin, Block b, Recipe r)
/*     */   {
/*  40 */     if ((b.getTypeId() == r.needBlock) && 
/*  41 */       (isConnected(world, b.getLocation(), r.oldBlock))) {
/*  42 */       int random = new Random().nextInt(r.Chance[1]) + 1;
/*  43 */       if (random <= r.Chance[0]) {
/*  44 */         ChangeBlock(b, getConnected(world, b.getLocation(), r.oldBlock), r.oldBlock, r.newBlock);
/*     */       }
/*     */     }
/*     */ 
/*  48 */     if ((b.getTypeId() == r.newBlock) && (r.Near) && 
/*  49 */       (isConnected(world, b.getLocation(), r.oldBlock))) {
/*  50 */       int random = new Random().nextInt(r.Chance[1]) + 1;
/*  51 */       if (random <= r.Chance[0])
/*  52 */         ChangeBlock(b, getConnected(world, b.getLocation(), r.oldBlock), r.oldBlock, r.newBlock);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getConnected(World world, Location here, int block)
/*     */   {
/*  59 */     String value = "";
/*  60 */     if (world.getBlockAt((int)here.getX() - 1, (int)here.getY(), (int)here.getZ()).getTypeId() == block) {
/*  61 */       value = value + "1, ";
/*     */     }
/*  63 */     if (world.getBlockAt((int)here.getX() + 1, (int)here.getY(), (int)here.getZ()).getTypeId() == block) {
/*  64 */       value = value + "2, ";
/*     */     }
/*  66 */     if (world.getBlockAt((int)here.getX(), (int)here.getY() + 1, (int)here.getZ()).getTypeId() == block) {
/*  67 */       value = value + "3, ";
/*     */     }
/*  69 */     if (world.getBlockAt((int)here.getX(), (int)here.getY() - 1, (int)here.getZ()).getTypeId() == block) {
/*  70 */       value = value + "4, ";
/*     */     }
/*  72 */     if (world.getBlockAt((int)here.getX(), (int)here.getY(), (int)here.getZ() + 1).getTypeId() == block) {
/*  73 */       value = value + "5, ";
/*     */     }
/*  75 */     if (world.getBlockAt((int)here.getX(), (int)here.getY(), (int)here.getZ() - 1).getTypeId() == block) {
/*  76 */       value = value + "6, ";
/*     */     }
/*  78 */     return value;
/*     */   }
/*     */ 
/*     */   public static boolean isConnected(World world, Location here, int block) {
/*  82 */     if (world.getBlockAt((int)here.getX() - 1, (int)here.getY(), (int)here.getZ()).getTypeId() == block) {
/*  83 */       return true;
/*     */     }
/*  85 */     if (world.getBlockAt((int)here.getX() + 1, (int)here.getY(), (int)here.getZ()).getTypeId() == block) {
/*  86 */       return true;
/*     */     }
/*  88 */     if (world.getBlockAt((int)here.getX(), (int)here.getY() + 1, (int)here.getZ()).getTypeId() == block) {
/*  89 */       return true;
/*     */     }
/*  91 */     if (world.getBlockAt((int)here.getX(), (int)here.getY() - 1, (int)here.getZ()).getTypeId() == block) {
/*  92 */       return true;
/*     */     }
/*  94 */     if (world.getBlockAt((int)here.getX(), (int)here.getY(), (int)here.getZ() + 1).getTypeId() == block) {
/*  95 */       return true;
/*     */     }
/*     */ 
/*  98 */     return world.getBlockAt((int)here.getX(), (int)here.getY(), (int)here.getZ() - 1).getTypeId() == block;
/*     */   }
/*     */ 
/*     */   public static void ChangeBlock(Block b, String value, int from, int id)
/*     */   {
/* 104 */     String[] values = { "", "", "", "", "", "" };
/* 105 */     values = value.split(", ");
/*     */ 
/* 107 */     if (b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY() + 1, (int)b.getLocation().getZ()).getY() > 128)
/* 108 */       return;
/* 109 */     if (b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY() - 1, (int)b.getLocation().getZ()).getY() < 0) {
/* 110 */       return;
/*     */     }
/*     */ 
/* 113 */     if (values[0].equals("1")) {
/* 114 */       Block block = b.getWorld().getBlockAt((int)b.getLocation().getX() - 1, (int)b.getLocation().getY(), (int)b.getLocation().getZ());
/* 115 */       if (block.getType().equals(Material.getMaterial(from))) block.setType(Material.getMaterial(id));
/*     */     }
/* 117 */     if (values[0].equals("2")) {
/* 118 */       Block block = b.getWorld().getBlockAt((int)b.getLocation().getX() + 1, (int)b.getLocation().getY(), (int)b.getLocation().getZ());
/* 119 */       if (block.getType().equals(Material.getMaterial(from))) block.setType(Material.getMaterial(id));
/*     */     }
/* 121 */     if (values[0].equals("3")) {
/* 122 */       Block block = b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY() + 1, (int)b.getLocation().getZ());
/* 123 */       if (block.getType().equals(Material.getMaterial(from))) block.setType(Material.getMaterial(id));
/*     */     }
/* 125 */     if (values[0].equals("4")) {
/* 126 */       Block block = b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY() - 1, (int)b.getLocation().getZ());
/* 127 */       if (block.getType().equals(Material.getMaterial(from))) block.setType(Material.getMaterial(id));
/*     */     }
/* 129 */     if (values[0].equals("5")) {
/* 130 */       Block block = b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY(), (int)b.getLocation().getZ() + 1);
/* 131 */       if (block.getType().equals(Material.getMaterial(from))) block.setType(Material.getMaterial(id));
/*     */     }
/* 133 */     if (values[0].equals("6")) {
/* 134 */       Block block = b.getWorld().getBlockAt((int)b.getLocation().getX(), (int)b.getLocation().getY(), (int)b.getLocation().getZ() - 1);
/* 135 */       if (block.getType().equals(Material.getMaterial(from))) block.setType(Material.getMaterial(id));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Users\Robin\Downloads\iGrow.jar
 * Qualified Name:     com.bukkit.techguard.igrow.Listener
 * JD-Core Version:    0.6.0
 */