//FileCabinet.java
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileCabinet implements Cabinet {
    private List<Folder> folders;

    public FileCabinet() {
        this.folders = new ArrayList<>();
    }

    @Override
    public Optional<Folder> findFolderByName(String name) {
        return findFolderByNameRecursive(name, folders);
    }

    private Optional<Folder> findFolderByNameRecursive(String name, List<Folder> folders) {
        for (Folder folder : folders) {
            if (folder.getName().equals(name)) {
                return Optional.of(folder);
            }
            if (folder instanceof MultiFolder) {
                Optional<Folder> found = findFolderByNameRecursive(name, ((MultiFolder) folder).getFolders());
                if (found.isPresent()) {
                    return found;
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Folder> findFoldersBySize(String size) {
        List<Folder> result = new ArrayList<>();
        findFoldersBySizeRecursive(size, folders, result);
        return result;
    }

    private void findFoldersBySizeRecursive(String size, List<Folder> folders, List<Folder> result) {
        for (Folder folder : folders) {
            if (folder.getSize().equals(size)) {
                result.add(folder);
            }
            if (folder instanceof MultiFolder) {
                findFoldersBySizeRecursive(size, ((MultiFolder) folder).getFolders(), result);
            }
        }
    }

    @Override
    public int count() {
        return countRecursive(folders);
    }

    private int countRecursive(List<Folder> folders) {
        int total = folders.size();
        for (Folder folder : folders) {
            if (folder instanceof MultiFolder) {
                total += countRecursive(((MultiFolder) folder).getFolders());
            }
        }
        return total;
    }


//Cabinet.java
import java.util.List;
import java.util.Optional;

public interface Cabinet {
    Optional<Folder> findFolderByName(String name);
    List<Folder> findFoldersBySize(String size);
    int count();
}


//Folder.java
public interface Folder {
    String getName();
    String getSize();
}


//SimpleFolder.java
public class SimpleFolder implements Folder {
    private String name;
    private String size;

    public SimpleFolder(String name, String size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSize() {
        return size;
    }
}


//MultiFolder.java
import java.util.List;

public interface MultiFolder extends Folder {
    List<Folder> getFolders();
}


    public void addFolder(Folder folder) {
        folders.add(folder);
    }
}


//ComplexFolder.java
import java.util.List;

public class ComplexFolder implements MultiFolder {
    private String name;
    private String size;
    private List<Folder> subFolders;

    public ComplexFolder(String name, String size, List<Folder> subFolders) {
        this.name = name;
        this.size = size;
        this.subFolders = subFolders;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getSize() {
        return size;
    }

    @Override
    public List<Folder> getFolders() {
        return subFolders;
    }
}


//Main.java
public class Main {
    public static void main(String[] args) {
        FileCabinet cabinet = new FileCabinet();

        Folder folder1 = new SimpleFolder("Documents", "SMALL");
        Folder folder2 = new SimpleFolder("Pictures", "LARGE");

        List<Folder> subFolders = new ArrayList<>();
        subFolders.add(new SimpleFolder("SubFolder1", "MEDIUM"));
        subFolders.add(new SimpleFolder("SubFolder2", "SMALL"));

        Folder multiFolder = new ComplexFolder("Projects", "LARGE", subFolders);

        cabinet.addFolder(folder1);
        cabinet.addFolder(folder2);
        cabinet.addFolder(multiFolder);

        System.out.println("Total folders: " + cabinet.count());
        System.out.println("Find folder by name 'Pictures': " + cabinet.findFolderByName("Pictures").isPresent());
        System.out.println("Find folders by size 'SMALL': " + cabinet.findFoldersBySize("SMALL").size());
    }
}

