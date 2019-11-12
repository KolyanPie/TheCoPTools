package edu.thecop.tools.statesfromdrawio.code;

abstract class Templates {
    private static final String IS_CONDITION;
    static final String GENERATED_CLASS;

    static {
        IS_CONDITION = "is%s()";
        GENERATED_CLASS = "/**\n" +
                " * this class was generated with\n" +
                " * theCoP tool statesfromdrawio\n" +
                " * watch https://github.com/KolyanPie/TheCoPTools\n" +
                " */\n";
    }

    static String fixFirstLetter(boolean isLow, String s) {
        if (isLow) {
            s = s.replaceFirst(s.substring(0, 1), s.substring(0, 1).toLowerCase());
        } else {
            s = s.replaceFirst(s.substring(0, 1), s.substring(0, 1).toUpperCase());
        }
        return s;
    }

    static String isCondition(String condition) {
        return String.format(IS_CONDITION, fixFirstLetter(false, condition));
    }

    static String constantBy(String var) {
        StringBuilder builder = new StringBuilder();
        var = fixFirstLetter(true, var);
        for (char c : var.toCharArray()) {
            if (Character.isUpperCase(c)) {
                builder.append('_');
            }
            builder.append(c);
        }
        return builder.toString().toUpperCase();
    }

    static String shortPackage(String packageS) {
        packageS = packageS.replaceFirst("\\.[^.]*$", "");
        return packageS;
    }
}
