package edu.thecop.tools.statesfromdrawio.code;

class ActorLastingState extends FileElement {
    {
        content = "package %s;\n" +
                "\n" +
                "import %s.Handler;\n" +
                "\n" +
                Templates.GENERATED_CLASS +
                "public abstract class %s implements Handler<%s> {\n" +
                "    private float duration;\n" +
                "    %4$sStates nextState;\n" +
                "\n" +
                "    %3$s(float duration) {\n" +
                "        this.duration = duration;\n" +
                "    }\n" +
                "\n" +
                "    boolean endState(%4$s %s) {\n" +
                "        if (duration < %5$s.getStateTime() && nextState != null) {\n" +
                "            %5$s.setState(nextState);\n" +
                "            return true;\n" +
                "        }\n" +
                "        return false;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public abstract boolean handle(%4$s %5$s, float delta);\n" +
                "}";
    }

    ActorLastingState(String packageS, String actor) {
        fileName = actor + "LastingStateHandler";
        content = String.format(content,
                packageS,
                Templates.shortPackage(packageS),
                fileName,
                actor,
                Templates.fixFirstLetter(true, actor)
        );
    }
}
