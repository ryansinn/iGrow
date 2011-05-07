package com.gmail.ryansinn.iGrow;

public class onEvent extends Thread
{
  private static int tick;
  private iGrow plugin;

  public onEvent(iGrow i)
  {
    this.plugin = i;
  }
  public static void set(int t) {
    tick = t * 1000 - 5000;
  }

  public void run() {
    try {
      while (true) {
        Thread.sleep(tick);
        Listener.onEvent(this.plugin);
      }
    }
    catch (InterruptedException e)
    {
    }
  }
}

/* Location:           C:\Users\Robin\Downloads\iGrow.jar
 * Qualified Name:     com.bukkit.techguard.igrow.onEvent
 * JD-Core Version:    0.6.0
 */