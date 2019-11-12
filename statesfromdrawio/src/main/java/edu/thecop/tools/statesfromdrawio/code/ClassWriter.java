package edu.thecop.tools.statesfromdrawio.code;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class ClassWriter {
    private File dir;

    ClassWriter(File dir) {
        this.dir = dir;
    }

    void writeFile(FileElement fileElement) throws IOException {
        File file = new File(dir, fileElement.fileName + ".java");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(fileElement.content);
        writer.flush();
        writer.close();
    }

}