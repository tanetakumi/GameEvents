package net.serveron.mcstar.gameevents.BreakBlockRun;

import net.serveron.mcstar.gameevents.GameEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockRunStudium {
    private GameEvent plugin;
    public boolean construction = false;
    private int length = 0;
    private Location startLoc;
    public BlockRunStudium(GameEvent plugin){
        this.plugin = plugin;
    }

    public void constructStudium(Location startLoc,int r){
        if(r<10)return ;

        construction = true;
        length = r;
        this.startLoc = startLoc;

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

    public void deConstructStudium(){
        construction = false;
        for(int j=0;j<10;j++){
            for(int i=0;i<length;i++){
                for(int k=0;k<length;k++){
                    Location l = startLoc.clone().add(i,j,k);
                    l.getBlock().setType(Material.AIR);
                }
            }
        }
    }
}
