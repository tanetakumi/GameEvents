package net.serveron.mcstar.gameevents.BreakBlockRun;

import net.serveron.mcstar.gameevents.GameEvents;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockRunStudium {


    public void constructStudium(GameEvents plugin, Location startLoc, int r){
        if(r<10)return ;

        new BukkitRunnable(){
            private int i = 0;
            @Override
            public void run(){
                if(i==0){
                    for(int j=0;j<10;j++){
                        for(int i=0;i<r;i++){
                            for(int k=0;k<r;k++){
                                Location l = startLoc.clone().add(i,j,k);
                                l.getBlock().setType(Material.BARRIER);
                            }
                        }
                    }
                }
                else if(i==1){
                    for(int j=1;j<9;j++){
                        for(int i=1;i<r-1;i++){
                            for(int k=1;k<r-1;k++){
                                Location l = startLoc.clone().add(i,j,k);
                                l.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }
                else if(i==2){
                    for(int j=2;j<4;j++) {
                        for (int i = 1; i < r - 1; i++) {
                            for (int k = 1; k < r - 1; k++) {
                                Location l = startLoc.clone().add(i, j, k);
                                l.getBlock().setType(Material.LAVA);
                            }
                        }
                    }
                }
                else if(i==3){
                    for (int i = 1; i < r - 1; i++) {
                        for (int k = 1; k < r - 1; k++) {
                            Location l = startLoc.clone().add(i, 5, k);
                            l.getBlock().setType(Material.BEDROCK);
                        }
                    }
                }
                else this.cancel();
                i++;
            }
        }.runTaskTimer(plugin,5,20);
    }

    public void downsizeStudium(Location startLoc,int r){
        for (int i = 1; i < r - 1; i++) {
            Location l = startLoc.clone().add(i, 5, 1);
            l.getBlock().setType(Material.AIR);
            Location l2 = startLoc.clone().add(i, 5, r-2);
            l2.getBlock().setType(Material.AIR);
        }
        for (int k = 1; k < r - 1; k++) {
            Location l = startLoc.clone().add(1, 5, k);
            l.getBlock().setType(Material.AIR);
            Location l2 = startLoc.clone().add(r-2, 5, k);
            l2.getBlock().setType(Material.AIR);
        }
    }

    public void deConstructStudium(Location startLoc,int r){
        for(int j=0;j<10;j++){
            for(int i=0;i<r;i++){
                for(int k=0;k<r;k++){
                    Location l = startLoc.clone().add(i,j,k);
                    l.getBlock().setType(Material.AIR);
                }
            }
        }
    }
}
