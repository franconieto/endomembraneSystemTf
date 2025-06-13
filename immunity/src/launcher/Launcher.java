package launcher;

import repast.simphony.runtime.RepastMain;
import java.io.File;

public class Launcher {
    public static void main(String[] args) {
        System.out.println("Working dir: " + System.getProperty("user.dir"));

        File f = new File("immunity.rs");
        if(f.exists() && f.isDirectory()) {
            System.out.println("Folder immunity.rs found!");
            String[] files = f.list();
            for(String file : files) {
                System.out.println(" - " + file);
            }
        } else {
            System.out.println("Folder immunity.rs NOT found!");
        }

        RepastMain.main(new String[] { "immunity.rs" });
    }
}
