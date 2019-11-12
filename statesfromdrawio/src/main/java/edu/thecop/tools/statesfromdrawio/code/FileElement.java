package edu.thecop.tools.statesfromdrawio.code;

abstract class FileElement {
    String fileName;
    String content;

    String getFileName() {
        return fileName;
    }

    String getContent() {
        return content;
    }
}
