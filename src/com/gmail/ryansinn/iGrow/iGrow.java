/*     */ package com.gmail.ryansinn.iGrow;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileWriter;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.plugin.PluginDescriptionFile;
/*     */ import org.bukkit.plugin.PluginLoader;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ 
/*     */ public class iGrow extends JavaPlugin
/*     */ {
/*  20 */   private String dirs = "plugins/iGrow/";
/*  21 */   private String file = this.dirs + "config.properties";
/*  22 */   private int TIMER_ = 33;
/*  23 */   public int AREA_ = 100;
			public boolean MINECRAFTTIMEMESSAGE_= false;
/*  24 */   private final HashMap<Player, Boolean> debugees = new HashMap();
/*  25 */   public ArrayList<Recipe> Recipes = new ArrayList();
/*  26 */   Thread Event = new onEvent(this);
/*     */ 
/*     */   public iGrow() {
/*     */   }
/*  30 */   public iGrow(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) { super(pluginLoader, noNag(instance, true), desc, folder, plugin, cLoader);
/*  31 */     noNag(instance, false); }
/*     */ 
/*     */   private static Server noNag(Server instance, boolean enable) {
/*     */     try {
/*  35 */       Method getlogger = Server.class.getMethod("getLogger", null);
/*  36 */       Logger thelogger = (Logger)getlogger.invoke(instance, null);
/*     */ 
/*  38 */       if (enable) thelogger.setLevel(Level.SEVERE); else
/*  39 */         thelogger.setLevel(null);
/*     */     } catch (NoSuchMethodException localNoSuchMethodException) {
/*     */     } catch (IllegalArgumentException localIllegalArgumentException) {
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/*     */     }
/*  45 */     return instance;
/*     */   }
/*     */ 
/*     */   public void onDisable() {
/*  49 */     this.Event.interrupt();
/*  50 */     sM("Disabled!");
/*     */   }
/*     */ 
/*     */   public void onEnable() {
/*  54 */     sM("Enabled!");
/*  55 */     ini();
/*  56 */     loadRecipes();
/*     */   }
/*     */ 
/*     */   public void ini() {
/*     */     try {
/*  61 */       File conf = new File(this.file);
/*  62 */       if (!conf.exists()) {
/*  63 */         File dirs = new File(this.dirs);
/*  64 */         File File = new File(this.file);
/*  65 */         dirs.mkdirs();
/*  66 */         File.createNewFile();
/*     */ 
/*  68 */         FileWriter fstream = new FileWriter(this.file);
/*  69 */         BufferedWriter out = new BufferedWriter(fstream);
/*  70 */         out.write("#Properties file for iGrow"); out.newLine();
/*  71 */         out.write("#The timer-interval is in SECONDS!"); out.newLine();
/*  72 */         out.write("#block-area is the area around the player to check."); out.newLine();
/*  72 */         out.write("#debug-tick-message is for debugging - this _will_ spam your server console and log."); out.newLine();
/*  73 */         out.write(""); out.newLine();
/*  74 */         out.write("timer-interval = 33"); out.newLine();
/*  75 */         out.write("check-block-area = 100");
/*  76 */         out.close();
/*     */       }
/*  78 */       FileInputStream fstream = new FileInputStream(this.file);
/*  79 */       DataInputStream in = new DataInputStream(fstream);
/*  80 */       BufferedReader br = new BufferedReader(new InputStreamReader(in));
/*     */       String strLine;
/*  82 */       while ((strLine = br.readLine()) != null)
/*     */       {
/*  83 */         if (strLine.startsWith("timer-interval = ")) {
/*  84 */           this.TIMER_ = Integer.parseInt(strLine.substring(17));
/*     */         }
/*  86 */         if (strLine.startsWith("check-block-area = ")) {
/*  87 */           this.AREA_ = Integer.parseInt(strLine.substring(19));
/*     */         }
/*     */       }
/*  90 */       in.close();
/*     */     } catch (Exception e) {
/*  92 */       System.err.println("Error: " + e.getMessage());
/*     */     }
/*  94 */     onEvent.set(this.TIMER_);
/*  95 */     this.Event.start();
/*     */   }
/*     */ 
/*     */   public void loadRecipes() {
/*     */     try {
/* 100 */       String recipes = this.dirs + "recipes.dat";
/* 101 */       File conf = new File(recipes);
/* 102 */       if (!conf.exists()) {
/* 103 */         File dirs = new File(this.dirs);
/* 104 */         File File = new File(recipes);
/* 105 */         dirs.mkdirs();
/* 106 */         File.createNewFile();
/*     */ 
/* 108 */         FileWriter fstream = new FileWriter(recipes);
/* 109 */         BufferedWriter out = new BufferedWriter(fstream);
/* 110 */         out.write("#Recipes file for iGrow"); out.newLine();
/* 111 */         out.write("#Lines that starts with the '#' sign, will be ignored!!"); out.newLine();
/* 112 */         out.write("#Usage:"); out.newLine();
/* 113 */         out.write("#  oldBlock,newBlock,needBlock,Chance:OutOf,enableNear"); out.newLine();
/* 114 */         out.write("#    - oldBlock = The block it was before"); out.newLine();
/* 115 */         out.write("#    - newBlock = The block it will change to"); out.newLine();
/* 116 */         out.write("#    - needBlock = The oldBlock needs to be connected to this block"); out.newLine();
/* 117 */         out.write("#    - Chance:OutOf = The chance to let the block change"); out.newLine();
/* 118 */         out.write("#    - enableNear = Spread newBlocks over the oldBlocks"); out.newLine();
/* 119 */         out.write("#The first recipe (mossycobble) is enabled below."); out.newLine();
/* 120 */         out.write(""); out.newLine();
/* 121 */         out.write("#Mossy Cobblestone:"); out.newLine();
/* 122 */         out.write("4,48,8,30:100,false"); out.newLine();
/* 123 */         out.write("4,48,9,30:100,false"); out.newLine();
/* 124 */         out.write(""); out.newLine();
/* 125 */         out.write("#Ice Block:"); out.newLine();
/* 126 */         out.write("8,79,80,4:15,false"); out.newLine();
/* 127 */         out.write("9,79,80,4:15,false"); out.newLine();
/* 128 */         out.write(""); out.newLine();
/* 129 */         out.write("#Other recipes:");
/* 130 */         out.close();
/*     */       }
/* 132 */       FileInputStream fstream = new FileInputStream(recipes);
/* 133 */       DataInputStream in = new DataInputStream(fstream);
/* 134 */       BufferedReader br = new BufferedReader(new InputStreamReader(in));
/*     */       String strLine;
/* 136 */       while ((strLine = br.readLine()) != null)
/*     */       {
/* 137 */         if (strLine.startsWith("#")) {
/*     */           continue;
/*     */         }
/* 140 */         String[] donees = strLine.split(",");
/* 141 */         if ((donees.length < 4) || (donees.length > 5)) {
/*     */           continue;
/*     */         }
/* 144 */         Recipe recipe = new Recipe();
/* 145 */         recipe.oldBlock = Integer.parseInt(donees[0]);
/* 146 */         recipe.newBlock = Integer.parseInt(donees[1]);
/* 147 */         recipe.needBlock = Integer.parseInt(donees[2]);
/* 148 */         if (donees[3].contains(":")) {
/* 149 */           String[] donees3 = donees[3].split(":");
/* 150 */           recipe.Chance[0] = Integer.parseInt(donees3[0]);
/* 151 */           recipe.Chance[1] = (Integer.parseInt(donees3[1]) - 1);
/*     */         }
/*     */         else {
/* 154 */           System.err.println("Error loading recipe for iGrow!");
/* 155 */           continue;
/*     */         }
/* 157 */         if (donees[4].contains("true")) {
/* 158 */           recipe.Near = true;
/*     */         }
/* 160 */         else if (donees[4].contains("false")) {
/* 161 */           recipe.Near = false;
/*     */         }
/* 163 */         this.Recipes.add(recipe);
/*     */       }
/* 165 */       sM("Loaded " + this.Recipes.size() + " recipes!");
/* 166 */       in.close();
/*     */     } catch (Exception e) {
/* 168 */       System.err.println("Error: " + e.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isDebugging(Player player) {
/* 173 */     if (this.debugees.containsKey(player)) {
/* 174 */       return ((Boolean)this.debugees.get(player)).booleanValue();
/*     */     }
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */   public void setDebugging(Player player, boolean value)
/*     */   {
/* 181 */     this.debugees.put(player, Boolean.valueOf(value));
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 185 */     PluginDescriptionFile pdfFile = getDescription();
/* 186 */     return pdfFile.getName();
/*     */   }
/*     */   public String getVersion() {
/* 189 */     PluginDescriptionFile pdfFile = getDescription();
/* 190 */     return pdfFile.getVersion();
/*     */   }
/*     */   public void sM(String message) {
/* 193 */     System.out.println("[" + getName() + " : " + getVersion() + "] " + message);
/*     */   }
/*     */   public Player getPlayer(String name) {
/* 196 */     for (Player pl : getServer().getOnlinePlayers()) {
/* 197 */       if (pl.getName().toLowerCase().startsWith(name.toLowerCase())) {
/* 198 */         return pl;
/*     */       }
/*     */     }
/* 201 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Users\Robin\Downloads\iGrow.jar
 * Qualified Name:     com.bukkit.techguard.igrow.iGrow
 * JD-Core Version:    0.6.0
 */