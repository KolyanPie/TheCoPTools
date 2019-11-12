package edu.thecop.tools.statesfromdrawio.code;

import edu.thecop.tools.statesfromdrawio.diagram.elements.BreakCondition;
import edu.thecop.tools.statesfromdrawio.diagram.elements.LoopState;

import java.util.List;

class ActorStateLoopState extends FileElement {
    private static final String ifBreak;

    static {
        ifBreak = "        if (%s) {\n" +
                "            %s.setState(%sStates.%s);\n" +
                "            return false;\n" +
                "        }\n";
    }

    {
        content = "package %s;\n" +
                "\n" +
                "import %s.Handler;\n" +
                "\n" +
                Templates.GENERATED_CLASS +
                "public class %s implements Handler<%s> {\n" +
                "    @Override\n" +
                "    public boolean handle(%4$s %s, float delta) {\n" +
                "%s" +
                "        //TODO some action probably\n" +
                "        return true;\n" +
                "    }\n" +
                "}";
    }

    ActorStateLoopState(String packageS, String actor, LoopState state) {
        fileName = actor + Templates.fixFirstLetter(false, state.getValue()) + "StateHandler";
        content = String.format(content,
                packageS,
                Templates.shortPackage(packageS),
                fileName,
                actor,
                Templates.fixFirstLetter(true, actor),
                createHandleBody(state.getSortedBreakConditions(), actor)
        );
    }

    static String createHandleBody(List<BreakCondition> breakConditions, String actor) {
        StringBuilder builder = new StringBuilder();
        for (BreakCondition breakCondition : breakConditions) {
            builder.append(getIfBreak(breakCondition, actor));
        }
        return builder.toString();
    }

    private static String getIfBreak(BreakCondition condition, String actor) {
        String ifCondition = condition.getValue();
        if (ifCondition.charAt(0) == '!') {
            ifCondition = "!" + Templates.fixFirstLetter(true, actor) + "." + Templates.isCondition(ifCondition.substring(1));
        } else {
            ifCondition = Templates.fixFirstLetter(true, actor) + "." + Templates.isCondition(ifCondition);
        }
        return String.format(ifBreak,
                ifCondition,
                Templates.fixFirstLetter(true, actor),
                actor,
                Templates.constantBy(condition.getTarget().getValue())
        );
    }


}
