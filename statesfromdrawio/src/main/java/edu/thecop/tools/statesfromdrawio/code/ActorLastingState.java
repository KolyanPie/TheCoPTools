package edu.thecop.tools.statesfromdrawio.code;

import edu.thecop.tools.statesfromdrawio.diagram.elements.LastingState;

import java.util.Collection;

class ActorLastingState extends FileElement {
    private static final String sfIntConstant;

    static {
        sfIntConstant = "static final int %s = 1;\n";
    }

    {
        content = "package %s;\n" +
                "\n" +
                "import %s.Handler;\n" +
                "\n" +
                Templates.GENERATED_CLASS +
                "public abstract class %s implements Handler<%s> {\n" +
                "%s\n" +
                "    private float duration;\n" +
                "    %4$sStates nextState;\n" +
                "\n" +
                "    %3$s(float duration) {\n" +
                "        this.duration = duration;\n" +
                "    }\n" +
                "\n" +
                "    boolean endState(%4$s %s) {\n" +
                "        if (duration < %6$s.getStateTime() && nextState != null) {\n" +
                "            %6$s.setState(nextState);\n" +
                "            return true;\n" +
                "        }\n" +
                "        return false;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public abstract boolean handle(%4$s %6$s, float delta);\n" +
                "}";
    }

    ActorLastingState(String packageS, String actor, Collection<LastingState> lastingStates) {
        fileName = actor + "LastingStateHandler";
        content = String.format(content,
                packageS,
                Templates.shortPackage(packageS),
                fileName,
                actor,
                addConstants(lastingStates),
                Templates.fixFirstLetter(true, actor)
        );
    }

    private static String addConstants(Collection<LastingState> lastingStates) {
        StringBuilder builder = new StringBuilder();
        for (LastingState lastingState : lastingStates) {
            builder.append(String.format(sfIntConstant, Templates.constantBy(lastingState.getValue()) + "_STATE_DURATION"));
        }
        return builder.toString();
    }
}
