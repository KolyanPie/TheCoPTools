package edu.thecop.tools.statesfromdrawio.code;

import java.util.Set;

class ActorInterface extends FileElement {
    {
        content = "package %s;\n" +
                "\n" +
                Templates.GENERATED_CLASS +
                "public interface %s {\n" +
                "    void setState(%2$sStates state);\n\n" +
                "    float getStateTime();\n" +
                "%s" +
                "\n    //TODO: some actions probably\n" +
                "}\n";
    }

    ActorInterface(String packageS, String actor,Set<String> conditionSet) {
        fileName = actor;
        content = String.format(content, packageS, fileName, conditionGetters(conditionSet));
    }

    private String conditionGetters(Set<String> conditionSet) {
        StringBuilder sBuilder = new StringBuilder();
        for (String condition : conditionSet) {
            sBuilder.append("\n    boolean ").append(Templates.isCondition(condition)).append(";\n");
        }
        return sBuilder.toString();
    }
}
