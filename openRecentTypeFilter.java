import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

public class openRecentTypeFilter extends FileFilter {

    //Accept all directories and all csv or txt files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.nlab)) {
                    return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "NeuLab (.nlab) save files";
    }
}
