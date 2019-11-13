package edu.thecop.tools.statesfromdrawio.code;

import edu.thecop.tools.statesfromdrawio.diagram.elements.LastingState;
import edu.thecop.tools.statesfromdrawio.diagram.elements.LoopState;

import java.util.Collection;

class ActorStatesEnum extends FileElement {
    private static final String enumVar;

    static {
        enumVar = "\n    %s(new %1$s%1$sStateHandler(%s)),";
    }

    {
        content = "package %s;\n" +
                "\n" +
                "import %s.Handler;\n" +
                "import %2$s.State;\n" +
                "\n" +
                "import static %1$s.%sLastingStateHandler.*;\n" +
                "\n" +
                Templates.GENERATED_CLASS +
                "public enum %3$sStates implements State<%3$s> {" +
                "%s\n" +
                "    public static final int COUNT_OF_LOOP_ANIMATIONS = %s;\n" +
                "\n" +
                "    private Handler<%3$s> state;\n" +
                "\n" +
                "    %3$sStates(Handler<%3$s> state) {\n" +
                "        this.state = state;\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public boolean handle(%3$s %s, float delta) {\n" +
                "        return state.handle(%6$s, delta);\n" +
                "    }\n" +
                "\n" +
                "    @Override\n" +
                "    public String getAnimationName() {\n" +
                "        return name();\n" +
                "    }\n" +
                "}";
    }

    ActorStatesEnum(String packageS, String actor, Collection<LoopState> loopStates, Collection<LastingState> lastingStates) {
        fileName = actor + "States";
        content = String.format(content,
                packageS,
                Templates.shortPackage(packageS),
                actor,
                createEnumVars(actor, loopStates, lastingStates),
                loopStates.size(),
                Templates.fixFirstLetter(true, actor)
                );
    }

    private static String createEnumVars(String actor, Collection<LoopState> loopStates, Collection<LastingState> lastingStates) {
        StringBuilder builder = new StringBuilder();
        for (LoopState loopState : loopStates) {
            builder.append(createEnumVar(actor, loopState));
        }
        for (LastingState lastingState : lastingStates) {
            builder.append(createEnumVar(actor, lastingState));
        }
        builder.deleteCharAt(builder.length() - 1).append(';');
        return builder.toString();
    }

    private static String createEnumVar(String actor, LoopState state) {
        String enumVar = "";
        if (state instanceof LastingState) {
            enumVar = "%1$s_STATE_DURATION";
        }
        enumVar = String.format(ActorStatesEnum.enumVar, "%s", enumVar);
        String constant = Templates.constantBy(state.getValue());
        return String.format(enumVar, constant, actor, Templates.fixFirstLetter(false, state.getValue()));
    }
}
