package edu.thecop.tools.statesfromdrawio.code;

import edu.thecop.tools.statesfromdrawio.diagram.elements.LastingState;
import edu.thecop.tools.statesfromdrawio.diagram.elements.NextCondition;

import java.util.List;

class ActorStateLastingState extends FileElement {
    private static final String ifNext;
    private static final String initNext;

    static {
        ifNext = "        if (%s) {\n" +
                "            nextState = %sStates.%s;\n" +
                "        }\n";
        initNext = "        nextState = %sStates.%s;\n";
    }

    {
        content = "package %s;\n" +
                "\n" +
                Templates.GENERATED_CLASS +
                "public class %s extends %sLastingStateHandler {\n" +
                "    %2$s(float duration) {\n" +
                "        super(duration);\n" +
                "%s" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public boolean handle(%3$s %s, float delta) {\n" +
                "        if (endState(%5$s)) {\n" +
                "            return false;\n" +
                "        }\n" +
                "%s" +
                "        //TODO some action probably\n" +
                "%s" +
                "        return true;\n" +
                "    }\n" +
                "}";
    }

    ActorStateLastingState(String packageS, String actor, LastingState state) {
        fileName = actor + Templates.fixFirstLetter(false, state.getValue()) + "StateHandler";
        content = String.format(content,
                packageS,
                fileName,
                actor,
                createDefaultNextStateInit(state, actor),
                Templates.fixFirstLetter(true, actor),
                ActorStateLoopState.createHandleBody(state.getSortedBreakConditions(), actor),
                createHandleBody(state.getReverseSortedNextConditions(), actor)
        );
    }

    private static String createDefaultNextStateInit(LastingState lastingState, String actor) {
        if (lastingState.getDefaultCondition() != null) {
            return String.format(initNext, actor, Templates.constantBy(lastingState.getDefaultCondition().getTarget().getValue()));
        }
        return "";
    }

    private static String createHandleBody(List<NextCondition> nextConditions, String actor) {
        StringBuilder builder = new StringBuilder();
        for (NextCondition nextCondition : nextConditions) {
            builder.append(getIfNext(nextCondition, actor));
        }
        return builder.toString();
    }

    private static String getIfNext(NextCondition condition, String actor) {
        String ifCondition = condition.getValue();
        if (ifCondition.charAt(0) == '!') {
            ifCondition = "!" + Templates.fixFirstLetter(true, actor) + "." + Templates.isCondition(ifCondition.substring(1));
        } else {
            ifCondition = Templates.fixFirstLetter(true, actor) + "." + Templates.isCondition(ifCondition);
        }
        return String.format(ifNext,
                ifCondition,
                actor,
                Templates.constantBy(condition.getTarget().getValue())
        );
    }

}
