package com.kms.katalon.core.ast;

import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyObject;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.ImportNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.PackageNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.builder.AstBuilder;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ArrayExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BitwiseNegationExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ClosureListExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.EmptyExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.MethodPointerExpression;
import org.codehaus.groovy.ast.expr.NotExpression;
import org.codehaus.groovy.ast.expr.PostfixExpression;
import org.codehaus.groovy.ast.expr.PrefixExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.RangeExpression;
import org.codehaus.groovy.ast.expr.SpreadExpression;
import org.codehaus.groovy.ast.expr.SpreadMapExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.ast.expr.TernaryExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.expr.UnaryMinusExpression;
import org.codehaus.groovy.ast.expr.UnaryPlusExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.AssertStatement;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.BreakStatement;
import org.codehaus.groovy.ast.stmt.CaseStatement;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.ast.stmt.ContinueStatement;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.EmptyStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.SwitchStatement;
import org.codehaus.groovy.ast.stmt.SynchronizedStatement;
import org.codehaus.groovy.ast.stmt.ThrowStatement;
import org.codehaus.groovy.ast.stmt.TryCatchStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.classgen.Verifier;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.CompilePhase;

public class GroovyParser {
    private static final String DEFAULT_INDENT_INCREASEMENT = "    ";

    public static final String[] GROOVY_IMPORTED_PACKAGES = { "java.io", "java.lang", "java.net", "java.util",
            "groovy.lang", "groovy.util" };

    public static final String[] GROOVY_IMPORTED_CLASSES = { "java.math.BigDecimal", "java.math.BigInteger" };

    private List<ClassNode> importedTypes = new ArrayList<ClassNode>();

    private Stack<String> classNameStack = new Stack<String>();

    private String currentIndent = "";

    private boolean readyToIndent = false;

    private StringBuilder stringBuilder;

    public GroovyParser(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public String getValue() {
        if (stringBuilder != null) {
            return stringBuilder.toString();
        }
        return StringUtils.EMPTY;
    }

    public void parse(Object object) {
        if (object instanceof Expression) {
            parse((Expression) object);
        } else if (object instanceof Statement) {
            parse((Statement) object);
        }

    }

    public void parse(List<? extends Expression> expressions) {
        boolean first = true;
        for (Object object : expressions) {
            Expression expression = (Expression) object;
            if (!first) {
                print(", ");
            }
            first = false;
            parse(expression);
        }
    }

    public void parse(ArgumentListExpression argumentListExpression, boolean showTypes) {
        print("(");
        int count = argumentListExpression.getExpressions().size();
        for (Expression expression : argumentListExpression.getExpressions()) {

            if (showTypes) {
                parseType(expression.getType());
                print(" ");
            }

            if (expression instanceof VariableExpression) {
                parse((VariableExpression) expression, false);
            } else if (expression instanceof ConstantExpression) {
                parse((ConstantExpression) expression, false);
            } else {
                parse(expression);
            }
            count--;
            if (count > 0) {
                print(", ");
            }
        }
        print(")");
    }

    public void parse(ArrayExpression arrayExpression) {
        print("new ");
        parseType(arrayExpression.getElementType());
        print("[");
        parse(arrayExpression.getExpressions());
        print("]");
    }

    public void parse(BooleanExpression booleanExpression) {
        if (booleanExpression instanceof NotExpression) {
            print("!(");
            parse(booleanExpression.getExpression());
            print(")");
        } else {
            parse(booleanExpression.getExpression());
        }
    }

    public void parse(BinaryExpression binaryExpression) {
        parse(binaryExpression.getLeftExpression());
        print(" " + binaryExpression.getOperation().getText() + " ");
        parse(binaryExpression.getRightExpression());
        if (binaryExpression.getOperation().getText().equals("[")) {
            print("]");
        }
    }

    public void parse(BitwiseNegationExpression bitwiseNegationExpression) {
        print("~(");
        parse(bitwiseNegationExpression.getExpression());
        print(") ");
    }

    public void parse(CastExpression castExpression) {
        print("((");
        parse(castExpression.getExpression());
        print(") as ");
        parseType(castExpression.getType());
        print(")");
    }

    public void parse(ClassExpression classExpression) {
        parseType(classExpression.getType());
    }

    public void parse(ClosureExpression closureExpression) {
        print("{ ");
        if (closureExpression.getParameters() != null) {
            parse(closureExpression.getParameters());
            print(" ->");
        }
        print("\n");
        parse(closureExpression.getCode());
        print("}");
    }

    public void parse(ConstantExpression constantExpression, boolean unwrapQuotes) {
        if (constantExpression.getValue() instanceof String && !unwrapQuotes) {
            print("'" + StringEscapeUtils.escapeJava((String) constantExpression.getValue()).replace("'", "\\'") + "'");
        } else if (constantExpression.getValue() instanceof Character) {
            print("'" + constantExpression.getValue() + "'");
        } else {
            print(constantExpression.getText());
        }
    }

    public void parse(ConstructorCallExpression constructorCallExpression) {
        if (constructorCallExpression.isSuperCall()) {
            print("super");
        } else if (constructorCallExpression.isThisCall()) {
            print("this ");
        } else {
            print("new ");
            parseType(constructorCallExpression.getType());
        }
        parse(constructorCallExpression.getArguments());
    }

    public void parse(ClosureListExpression closureListExpression) {
        boolean first = true;
        for (Expression expression : closureListExpression.getExpressions()) {
            if (!first) {
                print("; ");
            }
            first = false;
            parse(expression);
        }
    }

    public void parse(DeclarationExpression declarationExpression) {
        if (declarationExpression.getRightExpression() instanceof EmptyExpression) {
            // This to prevent the problem that is the class EmptyExpression is
            // not yet implemented for ASTVisitor
            ConstantExpression constantExpression = new ConstantExpression(null);
            declarationExpression.setRightExpression(constantExpression);
        }

        if (declarationExpression.getLeftExpression() instanceof ArgumentListExpression) {
            print("def ");
            parse((ArgumentListExpression) declarationExpression.getLeftExpression(), true);
            print(declarationExpression.getOperation().getText());
            parse(declarationExpression.getRightExpression());

            if (declarationExpression.getOperation().getText().equals("[")) {
                print("]");
            }
        } else {
            if (declarationExpression.getLeftExpression() instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) declarationExpression.getLeftExpression();
                parseType(variableExpression.getOriginType());
                print(" " + variableExpression.getName());
            } else {
                parse(declarationExpression.getLeftExpression());
            }
            print(" " + declarationExpression.getOperation().getText() + " ");
            parse(declarationExpression.getRightExpression());
            if (declarationExpression.getOperation().getText().equals("[")) {
                print("]");
            }
        }
    }

    public void parse(FieldExpression fieldExpression) {
        if (fieldExpression.getField() != null) {
            print(fieldExpression.getField().getName());
        }
    }

    public void parse(GStringExpression gStringExpression) {
        print(gStringExpression.getText());
    }

    public void parse(MapExpression mapExpression) {
        print("[");
        if (mapExpression.getMapEntryExpressions().size() == 0) {
            print(":");
        } else {
            parse(mapExpression.getMapEntryExpressions());
        }
        print("]");
    }

    public void parse(MapEntryExpression mapEntryExpression) {
        if (mapEntryExpression.getKeyExpression() instanceof SpreadMapExpression) {
            print("*");
        } else {
            if (mapEntryExpression.getKeyExpression() instanceof PropertyExpression
                    || mapEntryExpression.getKeyExpression() instanceof VariableExpression
                    || mapEntryExpression.getKeyExpression() instanceof MethodCallExpression) {
                print("(");
                parse(mapEntryExpression.getKeyExpression());
                print(")");
            } else {
                parse(mapEntryExpression.getKeyExpression());
            }
        }
        print(" : ");
        parse(mapEntryExpression.getValueExpression());
    }

    public void parse(MethodCallExpression methodCallExpression) {
        Expression objectExp = methodCallExpression.getObjectExpression();
        boolean isCustomKeywordMethod = false;
        if (objectExp instanceof VariableExpression) {
            if (((VariableExpression) objectExp).getName().equals("CustomKeywords")) {
                if (methodCallExpression.getMethod() instanceof ConstantExpression) {
                    String methodName = methodCallExpression.getMethod().getText();
                    if (!methodName.startsWith("'")) {
                        isCustomKeywordMethod = true;
                    }
                }
            }

            if (!((VariableExpression) objectExp).getName().equals("this")) {
                parse((VariableExpression) objectExp, false);
            }

        } else {
            parse(objectExp);
        }
        if (methodCallExpression.isSpreadSafe()) {
            print("*");
        }
        if (methodCallExpression.isSafe()) {
            print("?");
        }
        if (!(objectExp instanceof VariableExpression && ((VariableExpression) objectExp).getName().equals("this"))) {
            print(".");
        }
        Expression method = methodCallExpression.getMethod();
        if (method instanceof ConstantExpression) {
            if (isCustomKeywordMethod) {
                print("'");
            }
            parse((ConstantExpression) method, true);
            if (isCustomKeywordMethod) {
                print("'");
            }
        } else {
            parse(method);
        }
        parse(methodCallExpression.getArguments());
    }

    public void parse(MethodPointerExpression methodPointerExpression) {
        parse(methodPointerExpression.getExpression());
        print(".&");
        parse(methodPointerExpression.getMethodName());
    }

    public void parse(PostfixExpression postfixExpression) {
        if (postfixExpression.getExpression() instanceof VariableExpression) {
            parse(postfixExpression.getExpression());
            print(postfixExpression.getOperation().getText());
        } else {
            print("(");
            parse(postfixExpression.getExpression());
            print(")");
            print(postfixExpression.getOperation().getText());
        }
    }

    public void parse(PrefixExpression prefixExpression) {
        if (prefixExpression.getExpression() instanceof VariableExpression) {
            print(prefixExpression.getOperation().getText());
            parse(prefixExpression.getExpression());
        } else {
            print(prefixExpression.getOperation().getText());
            print("(");
            parse(prefixExpression.getExpression());
            print(")");
        }
    }

    public void parse(PropertyExpression propertyExpression) {
        parse(propertyExpression.getObjectExpression());
        if (propertyExpression.isSpreadSafe()) {
            print("*");
        }
        if (propertyExpression.isSpreadSafe()) {
            print("*");
        } else if (propertyExpression.isSafe()) {
            print("?");
        }
        print(".");
        if (propertyExpression.getProperty() instanceof ConstantExpression) {
            parse((ConstantExpression) propertyExpression.getProperty(), true);
        } else {
            parse(propertyExpression.getProperty());
        }
    }

    public void parse(RangeExpression rangeExpression) {
        print("(");
        parse(rangeExpression.getFrom());
        print("..");
        parse(rangeExpression.getTo());
        print(")");
    }

    public void parse(ListExpression listExpression) {
        print("[");
        parse(listExpression.getExpressions());
        print("]");
    }

    public void parse(SpreadExpression spreadExpression) {
        print("*");
        parse(spreadExpression.getExpression());
    }

    public void parse(SpreadMapExpression spreadMapExpression) {
        print("*:");
        parse(spreadMapExpression.getExpression());
    }

    public void parse(StaticMethodCallExpression staticMethodCallExpression) {
        print(staticMethodCallExpression.getOwnerType().getName() + "." + staticMethodCallExpression.getMethod());
        if (staticMethodCallExpression.getArguments() instanceof VariableExpression
                || staticMethodCallExpression.getArguments() instanceof MethodCallExpression) {
            print("(");
            parse(staticMethodCallExpression.getArguments());
            print(")");
        } else {
            parse(staticMethodCallExpression.getArguments());
        }
    }

    public void parse(TernaryExpression ternaryExpression) {
        parse(ternaryExpression.getBooleanExpression());
        print(" ? ");
        parse(ternaryExpression.getTrueExpression());
        print(" : ");
        parse(ternaryExpression.getFalseExpression());
    }

    public void parse(TupleExpression tupleExpression) {
        print("(");
        parse(tupleExpression.getExpressions());
        print(")");
    }

    public void parse(UnaryMinusExpression unaryMinusExpression) {
        print("-(");
        parse(unaryMinusExpression.getExpression());
        print(")");
    }

    public void parse(UnaryPlusExpression unaryPlusExpression) {
        print("+(");
        parse(unaryPlusExpression.getExpression());
        print(")");
    }

    public void parse(VariableExpression variableExpression, boolean spacePad) {
        if (spacePad) {
            print(' ' + variableExpression.getName() + ' ');
        } else {
            print(variableExpression.getName());
        }
    }

    public void parse(Expression expression) {
        if (expression instanceof ArgumentListExpression) {
            parse((ArgumentListExpression) expression, false);
        } else if (expression instanceof ArrayExpression) {
            parse((ArrayExpression) expression);
        } else if (expression instanceof BooleanExpression) {
            parse((BooleanExpression) expression);
        } else if (expression instanceof BitwiseNegationExpression) {
            parse((BitwiseNegationExpression) expression);
        } else if (expression instanceof CastExpression) {
            parse((CastExpression) expression);
        } else if (expression instanceof ClosureExpression) {
            parse((ClosureExpression) expression);
        } else if (expression instanceof ConstructorCallExpression) {
            parse((ConstructorCallExpression) expression);
        } else if (expression instanceof DeclarationExpression) {
            parse((DeclarationExpression) expression);
        } else if (expression instanceof BinaryExpression) {
            parse((BinaryExpression) expression);
        } else if (expression instanceof FieldExpression) {
            parse((FieldExpression) expression);
        } else if (expression instanceof VariableExpression) {
            parse((VariableExpression) expression, false);
        } else if (expression instanceof GStringExpression) {
            parse((GStringExpression) expression);
        } else if (expression instanceof MapExpression) {
            parse((MapExpression) expression);
        } else if (expression instanceof MapEntryExpression) {
            parse((MapEntryExpression) expression);
        } else if (expression instanceof MethodCallExpression) {
            parse((MethodCallExpression) expression);
        } else if (expression instanceof MethodPointerExpression) {
            parse((MethodPointerExpression) expression);
        } else if (expression instanceof ConstantExpression) {
            parse((ConstantExpression) expression, false);
        } else if (expression instanceof PostfixExpression) {
            parse((PostfixExpression) expression);
        } else if (expression instanceof PrefixExpression) {
            parse((PrefixExpression) expression);
        } else if (expression instanceof RangeExpression) {
            parse((RangeExpression) expression);
        } else if (expression instanceof PropertyExpression) {
            parse((PropertyExpression) expression);
        } else if (expression instanceof ClassExpression) {
            parse((ClassExpression) expression);
        } else if (expression instanceof ClosureListExpression) {
            parse((ClosureListExpression) expression);
        } else if (expression instanceof ListExpression) {
            parse((ListExpression) expression);
        } else if (expression instanceof SpreadExpression) {
            parse((SpreadExpression) expression);
        } else if (expression instanceof SpreadMapExpression) {
            parse((SpreadMapExpression) expression);
        } else if (expression instanceof StaticMethodCallExpression) {
            parse((StaticMethodCallExpression) expression);
        } else if (expression instanceof TernaryExpression) {
            parse((TernaryExpression) expression);
        } else if (expression instanceof TupleExpression) {
            parse((TupleExpression) expression);
        } else if (expression instanceof UnaryMinusExpression) {
            parse((UnaryMinusExpression) expression);
        } else if (expression instanceof UnaryPlusExpression) {
            parse((UnaryPlusExpression) expression);
        } else if (expression instanceof EmptyExpression) {
            parse(expression);
        }
    }

    public void parse(Statement statement) {
        if (statement instanceof BlockStatement) {
            parse((BlockStatement) statement);
        } else if (statement instanceof ExpressionStatement) {
            parse((ExpressionStatement) statement);
        } else if (statement instanceof ReturnStatement) {
            parse((ReturnStatement) statement);
        } else if (statement instanceof AssertStatement) {
            parse((AssertStatement) statement);
        } else if (statement instanceof BreakStatement) {
            parse((BreakStatement) statement);
        } else if (statement instanceof SwitchStatement) {
            parse((SwitchStatement) statement);
        } else if (statement instanceof ThrowStatement) {
            parse((ThrowStatement) statement);
        } else if (statement instanceof TryCatchStatement) {
            parse((TryCatchStatement) statement);
        } else if (statement instanceof IfStatement) {
            parse((IfStatement) statement);
        } else if (statement instanceof ForStatement) {
            parse((ForStatement) statement);
        } else if (statement instanceof WhileStatement) {
            parse((WhileStatement) statement);
        } else if (statement instanceof DoWhileStatement) {
            parse((DoWhileStatement) statement);
        } else if (statement instanceof SynchronizedStatement) {
            parse((SynchronizedStatement) statement);
        } else if (statement instanceof CaseStatement) {
            parse((CaseStatement) statement);
        } else if (statement instanceof CatchStatement) {
            parse((CatchStatement) statement);
        } else if (statement instanceof ContinueStatement) {
            parse((ContinueStatement) statement);
        }
    }

    public void parse(BlockStatement blockStatement) {
        Iterator<Statement> iterator = blockStatement.getStatements().iterator();
        while (iterator.hasNext()) {
            Statement statement = iterator.next();
            parse(statement);
            printLineBreak();
        }
    }

    public void parse(ExpressionStatement expressionStatement) {
        parse(expressionStatement.getExpression());
    }

    public void parse(ReturnStatement returnStatement) {
        printLineBreak();
        print("return ");
        parse(returnStatement.getExpression());
        printLineBreak();
    }

    public void parse(AssertStatement assertStatement) {
        print("assert ");
        parse(assertStatement.getBooleanExpression());
        if (assertStatement.getMessageExpression() instanceof ConstantExpression
                && !(((ConstantExpression) assertStatement.getMessageExpression()).getValue() == null)) {
            print(" : ");
            parse(assertStatement.getMessageExpression());
        }
    }

    public void parse(BreakStatement breakStatement) {
        print("break");
        printLineBreak();
    }

    public void parse(ContinueStatement continueStatement) {
        print("continue");
        printLineBreak();
    }

    public void parse(SwitchStatement switchStatement) {
        print("switch (");
        parse(switchStatement.getExpression());
        print(") {");
        printLineBreak();

        String lastIndent = increaseIndent();
        for (CaseStatement caseStatement : switchStatement.getCaseStatements()) {
            parse(caseStatement);
            printLineBreak();
        }

        if (switchStatement.getDefaultStatement() != null
                && !(switchStatement.getDefaultStatement() instanceof EmptyStatement)) {
            print("default:");
            printLineBreak();

            String lastInnerIndent = increaseIndent();
            parse(switchStatement.getDefaultStatement());
            resetIndent(lastInnerIndent);
        }
        resetIndent(lastIndent);

        print("}");
        printLineBreak();
    }

    public void parse(CaseStatement caseStatement) {
        print("case ");
        parse(caseStatement.getExpression());
        print(":");
        printLineBreak();

        String lastIndent = increaseIndent();
        parse(caseStatement.getCode());
        resetIndent(lastIndent);
    }

    public void parse(ThrowStatement throwStatement) {
        print("throw ");
        parse(throwStatement.getExpression());
        printLineBreak();
    }

    public void parse(TryCatchStatement tryCatchStatement) {
        print("try {");
        printLineBreak();

        String lastIndent = increaseIndent();
        parse(tryCatchStatement.getTryStatement());
        resetIndent(lastIndent);

        printLineBreak();
        print("}");
        printLineBreak();
        for (CatchStatement catchStatement : tryCatchStatement.getCatchStatements()) {
            parse(catchStatement);
        }
        if (tryCatchStatement.getFinallyStatement() != null
                && !(tryCatchStatement.getFinallyStatement() instanceof EmptyStatement)) {
            print("finally { ");
            printLineBreak();

            lastIndent = increaseIndent();
            parse(tryCatchStatement.getFinallyStatement());
            resetIndent(lastIndent);

            print("}");
        }
        printLineBreak();
    }

    public void parse(CatchStatement catchStatement) {
        print("catch (");
        parse(new Parameter[] { catchStatement.getVariable() });
        print(") {");
        printLineBreak();

        String lastIndent = increaseIndent();
        parse(catchStatement.getCode());
        resetIndent(lastIndent);

        print("} ");
        printLineBreak();
    }

    public void parse(IfStatement ifStatement) {
        print("if (");
        parse(ifStatement.getBooleanExpression());
        print(") {");

        printLineBreak();
        String lastIndent = increaseIndent();
        parse(ifStatement.getIfBlock());
        resetIndent(lastIndent);

        printLineBreak();
        if (ifStatement.getElseBlock() != null && !(ifStatement.getElseBlock() instanceof EmptyStatement)) {
            if (ifStatement.getElseBlock() instanceof IfStatement) {
                print("} else ");
                parse((IfStatement) ifStatement.getElseBlock());
            } else {
                print("} else {");
                printLineBreak();

                lastIndent = increaseIndent();
                parse(ifStatement.getElseBlock());
                resetIndent(lastIndent);

                printLineBreak();
                print("}");
            }
        } else {
            print("}");
        }
        printLineBreak();
    }

    public void parse(ForStatement forStatement) {
        print("for (");
        if (!(forStatement.getCollectionExpression() instanceof ClosureListExpression)) {
            if (forStatement.getVariable() != ForStatement.FOR_LOOP_DUMMY) {
                parse(new Parameter[] { forStatement.getVariable() });
                print(" : ");
            }
        }
        parse(forStatement.getCollectionExpression());
        print(") {");
        printLineBreak();

        String lastIndent = increaseIndent();
        parse(forStatement.getLoopBlock());
        resetIndent(lastIndent);

        print("}");
        printLineBreak();
    }

    public void parse(WhileStatement whileStatement) {
        print("while (");
        parse(whileStatement.getBooleanExpression());
        print(") {");
        printLineBreak();

        String lastIndent = increaseIndent();
        parse(whileStatement.getLoopBlock());
        resetIndent(lastIndent);

        printLineBreak();
        print("}");
        printLineBreak();
    }

    public void parse(DoWhileStatement doWhileStatement) {
        print("do {");
        printLineBreak();

        String lastIndent = increaseIndent();
        parse(doWhileStatement.getLoopBlock());
        resetIndent(lastIndent);

        print("} while (");
        printLineBreak();
        parse(doWhileStatement.getBooleanExpression());
        print(")");
        printLineBreak();
    }

    public void parse(SynchronizedStatement synchronizedStatement) {
        print("synchronized (");
        parse(synchronizedStatement.getExpression());
        print(") {");
        printLineBreak();

        String lastIndent = increaseIndent();
        parse(synchronizedStatement.getExpression());
        resetIndent(lastIndent);

        print("}");
    }

    public void parse(Parameter[] parameters) {
        boolean first = true;
        for (Parameter parameter : parameters) {
            if (!first) {
                print(", ");
            }
            first = false;

            for (AnnotationNode annotation : parameter.getAnnotations()) {
                parse(annotation);
                print(" ");
            }

            parseModifiers(parameter.getModifiers());
            parseType(parameter.getType());

            print(" " + parameter.getName());
            if (parameter.getInitialExpression() != null
                    && !(parameter.getInitialExpression() instanceof EmptyExpression)) {
                print(" = ");
                parse(parameter.getInitialExpression());
            }
        }
    }

    public void parseModifiers(int modifiers) {
        if (Modifier.isAbstract(modifiers)) {
            print("abstract ");
        }
        if (Modifier.isFinal(modifiers)) {
            print("final ");
        }
        if (Modifier.isInterface(modifiers)) {
            print("interface ");
        }
        if (Modifier.isNative(modifiers)) {
            print("native ");
        }
        if (Modifier.isPrivate(modifiers)) {
            print("private ");
        }
        if (Modifier.isProtected(modifiers)) {
            print("protected ");
        }
        if (Modifier.isPublic(modifiers)) {
            // do nothing
        }
        if (Modifier.isStatic(modifiers)) {
            print("static ");
        }
        if (Modifier.isSynchronized(modifiers)) {
            print("synchronized ");
        }
        if (Modifier.isTransient(modifiers)) {
            print("transient ");
        }
        if (Modifier.isVolatile(modifiers)) {
            print("volatile ");
        }
    }

    public void parse(AnnotationNode annotationNode) {
        print('@' + annotationNode.getClassNode().getName());
        if (annotationNode.getMembers() != null && !annotationNode.getMembers().isEmpty()) {
            print("(");
            boolean first = true;

            Iterator<Entry<String, Expression>> it = annotationNode.getMembers().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Expression> pairs = it.next();
                if (first) {
                    first = false;
                } else {
                    print(", ");
                }
                print(pairs.getKey() + " = ");
                parse(pairs.getValue());
            }
            print(")");
        }
    }

    public void parseType(ClassNode classNode) {
        String name = classNode.getName();
        if (name.startsWith("[")) {
            /*
             * int numDimensions = 0; while (name.charAt(numDimensions) == '[') { numDimensions++; } StringBuilder
             * stringBuilder = new StringBuilder(); stringBuilder.append(classNode.getTypeClass().getComponentType() ==
             * null ? classNode.getTypeClass() .getComponentType().getSim : Object.class.getName()); for (int i = 0; i <
             * numDimensions; i++) { stringBuilder.append("[]"); }
             */
            print(classNode.toString());
        } else if (classNode.getName().equals(Object.class.getName())) {
            print("def");
        } else {
            if (importedTypes.contains(classNode)) {
                print(classNode.getNameWithoutPackage());
            } else {
                boolean isImported = false;
                for (String groovyImportedClass : GROOVY_IMPORTED_CLASSES) {
                    if (name.equals(groovyImportedClass)) {
                        isImported = true;
                        break;
                    }
                }
                for (String groovyImportedPackage : GROOVY_IMPORTED_PACKAGES) {
                    if (name.startsWith(groovyImportedPackage)) {
                        isImported = true;
                        break;
                    }
                }
                if (isImported) {
                    print(classNode.getNameWithoutPackage());
                } else {
                    print(name);
                }
            }
        }
        parse(classNode.getGenericsTypes());
    }

    public void parse(GenericsType[] generics) {
        if (generics != null && generics.length > 0) {
            print("<");
            boolean first = true;
            for (GenericsType generic : generics) {
                if (!first) {
                    print(", ");
                }
                first = false;
                print(generic.getName());
                if (generic.getUpperBounds() != null && generic.getUpperBounds().length > 0) {
                    print(" extends ");
                    boolean innerFirst = true;
                    for (ClassNode upperBound : generic.getUpperBounds()) {
                        if (!innerFirst) {
                            print(" & ");
                        }
                        innerFirst = false;
                        parseType(upperBound);
                    }
                }
                if (generic.getLowerBound() != null) {
                    print(" super ");
                    parseType(generic.getLowerBound());
                }
            }
            print(">");
        }
    }

    public void parseClass(ClassNode classNode) {
        classNameStack.push(classNode.getName());

        for (AnnotationNode annotationNode : classNode.getAnnotations()) {
            parse(annotationNode);
            printLineBreak();
        }

        parseModifiers(classNode.getModifiers());
        print("class " + classNode.getNameWithoutPackage());
        parse(classNode.getGenericsTypes());
        if (classNode.getInterfaces().length == 1
                && classNode.getInterfaces()[0].getName().equals(GroovyObject.class.getName())) {
            // do nothing
        } else {
            boolean first = true;
            for (ClassNode interfaceNode : classNode.getInterfaces()) {
                if (!interfaceNode.getName().equals(GroovyObject.class.getName())) {
                    if (!first) {
                        print(", ");
                    } else {
                        print(" implements ");
                    }
                    first = false;
                    parseType(interfaceNode);
                }
            }
        }
        if (!(classNode.getSuperClass().getName().equals(Object.class.getName()))) {
            print(" extends ");
            parseType(classNode.getSuperClass());
        }
        print(" { ");
        printDoubleLineBreak();

        String lastIndent = increaseIndent();
        for (FieldNode fieldNode : classNode.getFields()) {
            parseField(fieldNode);
        }
        printDoubleLineBreak();
        for (ConstructorNode constructorNode : classNode.getDeclaredConstructors()) {
            parseMethod(constructorNode);
        }
        printLineBreak();
        for (MethodNode methodNode : classNode.getMethods()) {
            parseMethod(methodNode);
        }
        resetIndent(lastIndent);

        print("}");
        printLineBreak();
        classNameStack.pop();
    }

    public void parseMethod(MethodNode methodNode) {
        if (methodNode.getLineNumber() > 0) {
            if (methodNode.getCode() instanceof ReturnStatement
                    && ((ReturnStatement) methodNode.getCode()).getExpression() instanceof ConstantExpression
                    && ((ConstantExpression) ((ReturnStatement) methodNode.getCode()).getExpression()).getValue() == null) {
                methodNode.setCode(new BlockStatement());
            } else {
                for (AnnotationNode annotationNode : methodNode.getAnnotations()) {
                    parse(annotationNode);
                    printLineBreak();
                }

                parseModifiers(methodNode.getModifiers());
                if (methodNode.getName().equals("<init>")) {
                    print(classNameStack.peek() + "(");
                    parse(methodNode.getParameters());
                    print(") {");
                    printLineBreak();
                } else if (methodNode.getName().equals("<clinit>")) {
                    print("{ "); // will already have 'static' from modifiers
                    printLineBreak();
                } else {
                    parseType(methodNode.getReturnType());
                    print(" " + methodNode.getName() + "(");
                    parse(methodNode.getParameters());
                    print(")");
                    if (methodNode.getExceptions() != null && methodNode.getExceptions().length > 0) {
                        boolean first = true;
                        print(" throws ");
                        for (ClassNode exceptionClassNode : methodNode.getExceptions()) {
                            if (!first) {
                                print(", ");
                            }
                            first = false;
                            parseType(exceptionClassNode);
                        }
                    }
                    print(" {");
                    printLineBreak();
                }

                String lastIndent = increaseIndent();
                parse(methodNode.getCode());
                resetIndent(lastIndent);
                printLineBreak();
                print("}");
                printDoubleLineBreak();
            }
        }
    }

    public void parseField(FieldNode fieldNode) {
        if (fieldNode.getLineNumber() > 0) {
            for (AnnotationNode annotationNode : fieldNode.getAnnotations()) {
                parse(annotationNode);
                printLineBreak();
            }
            parseModifiers(fieldNode.getModifiers());
            parseType(fieldNode.getType());
            print(" " + fieldNode.getName() + " ");
            if (fieldNode.getInitialValueExpression() != null) {
                // do not print initial expression, as this is executed as part
                // of the constructor, unless on constant
                Expression initialValueExpression = fieldNode.getInitialValueExpression();
                if (initialValueExpression instanceof ConstantExpression) {
                    initialValueExpression = Verifier.transformToPrimitiveConstantIfPossible((ConstantExpression) initialValueExpression);
                }
                ClassNode type = initialValueExpression.getType();
                if (Modifier.isStatic(fieldNode.getModifiers()) && Modifier.isFinal(fieldNode.getModifiers())
                        && initialValueExpression instanceof ConstantExpression && type == fieldNode.getType()
                        && ClassHelper.isStaticConstantInitializerType(type)) {
                    // GROOVY-5150: final constants may be initialized directly
                    print(" = ");
                    if (ClassHelper.STRING_TYPE == type) {
                        print("'" + initialValueExpression.getText().replaceAll("'", "\\\\'") + "'");
                    } else if (ClassHelper.char_TYPE == type) {
                        print("'" + initialValueExpression.getText() + "'");
                    } else {
                        print(initialValueExpression.getText());
                    }
                }
            }
            printLineBreak();
        }
    }

    public String increaseIndent() {
        String lastIndent = currentIndent;
        currentIndent = currentIndent + DEFAULT_INDENT_INCREASEMENT;
        return lastIndent;
    }

    public void resetIndent(String lastIndent) {
        currentIndent = lastIndent;
    }

    public void parse(ImportNode importNode) {
        if (importNode != null) {
            importedTypes.add(importNode.getType());
            for (AnnotationNode annotationNode : importNode.getAnnotations()) {
                parse(annotationNode);
                printLineBreak();
            }
            print(importNode.getText());
            printLineBreak();
        }
    }

    public void parse(PackageNode packageNode) {
        if (packageNode != null) {
            for (AnnotationNode annotationNode : packageNode.getAnnotations()) {
                parse(annotationNode);
                printLineBreak();
            }

            if (packageNode.getText().endsWith(".")) {
                print(packageNode.getText().substring(0, packageNode.getText().length() - 1));
            } else {
                print(packageNode.getText());
            }
            printDoubleLineBreak();
        }
    }

    public static List<ASTNode> parseGroovyScriptIntoAstNodes(String scriptContent) throws Exception {
        if (scriptContent != null) {
            scriptContent = scriptContent.trim();
            if (!scriptContent.isEmpty()) {
                return new AstBuilder().buildFromString(CompilePhase.CONVERSION, false, scriptContent);
            }
        }
        return Collections.emptyList();
    }

    public static ASTNode parseGroovyScriptAndGetFirstItem(String scriptContent) throws Exception {
        List<ASTNode> astNodes = parseGroovyScriptIntoAstNodes(scriptContent);
        for (ASTNode astNode : astNodes) {
            if (astNode instanceof ClassNode) {
                if (((ClassNode) astNode).isScript()) {
                    ClassNode mainClassNode = ((ClassNode) astNode);
                    for (MethodNode methodNode : mainClassNode.getMethods()) {
                        if (methodNode.getName().equals("run") && methodNode.getCode() instanceof BlockStatement) {
                            BlockStatement blockStatement = (BlockStatement) methodNode.getCode();
                            if (blockStatement.getStatements().size() > 0) {
                                return blockStatement.getStatements().get(0);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private void parsePackageAndImport(List<? extends ASTNode> astNodes) {
        Comparator<ImportNode> importNodeComparator = new Comparator<ImportNode>() {

            @Override
            public int compare(ImportNode n1, ImportNode n2) {
                if (n1 == n2) {
                    return 0;
                }

                if (n1 == null) {
                    return -1;
                }

                if (n2 == null) {
                    return 1;
                }

                return n1.getClassName().compareTo(n2.getClassName());
            }
        };

        for (ASTNode astNode : astNodes) {
            if (astNode instanceof ClassNode) {

                ModuleNode moduleNode = ((ClassNode) astNode).getModule();
                parse(moduleNode.getPackage());

                // Static import
                Map<String, ImportNode> staticImports = moduleNode.getStaticImports();
                if (staticImports.size() > 0) {
                    List<ImportNode> staticImportNodes = new ArrayList<>(staticImports.values());
                    Collections.sort(staticImportNodes, importNodeComparator);
                    for (ImportNode importNode : staticImportNodes) {
                        parse(importNode);
                    }
                }

                // Import
                List<ImportNode> imports = moduleNode.getImports();
                if (imports.size() > 0) {
                    Collections.sort(imports, importNodeComparator);
                    for (ImportNode importNode : imports) {
                        parse(importNode);
                    }
                    printDoubleLineBreak();
                }
            }
        }
    }

    private void print(String string) {
        if (readyToIndent) {
            stringBuilder.append(currentIndent);
            readyToIndent = false;
            stringBuilder.trimToSize();
        }
        stringBuilder.append(string);
    }

    private void printLineBreak() {
        if (!stringBuilder.toString().endsWith("\n")) {
            print("\n");
        }
        readyToIndent = true;
    }

    private void printDoubleLineBreak() {
        String output = stringBuilder.toString();
        if (output.endsWith("\n\n")) {
            // do nothing
        } else if (output.endsWith("\n")) {
            print("\n");
        } else {
            print("\n\n");
        }
        readyToIndent = true;
    }

    public void parseGroovyAstIntoScript(List<? extends ASTNode> astNodes) {
        importedTypes.clear();

        parsePackageAndImport(astNodes);

        for (ASTNode astNode : astNodes) {
            if (astNode instanceof BlockStatement) {
                parse((BlockStatement) astNode);
                printLineBreak();
            } else if (astNode instanceof ClassNode) {
                if (((ClassNode) astNode).isScriptBody()) {
                    for (MethodNode methodNode : ((ClassNode) astNode).getMethods()) {
                        if (methodNode.getName() != "run") {
                            parseMethod(methodNode);
                        }
                    }
                } else {
                    parseClass((ClassNode) astNode);
                    printLineBreak();
                }
            } else if (astNode instanceof MethodNode) {
                parseMethod((MethodNode) astNode);
            }
        }
    }

    public void parseGroovyAstIntoClass(List<ASTNode> astNodes) throws Exception {
        importedTypes.clear();

        parsePackageAndImport(astNodes);

        for (ASTNode astNode : astNodes) {
            if (astNode instanceof ClassNode) {
                parseClass((ClassNode) astNode);
            }
        }
    }

    public static List<ASTNode> generateNewScript() {
        String scriptClassName = "script" + System.currentTimeMillis();
        GroovyCodeSource codeSource = new GroovyCodeSource("", scriptClassName + ".groovy", "/groovy/script");
        CompilationUnit cu = new CompilationUnit();
        cu.addSource(codeSource.getName(), "");
        cu.compile(CompilePhase.CONVERSION.getPhaseNumber());
        cu.getAST().getModules();
        List<ASTNode> astNodes = new ArrayList<ASTNode>();
        for (ModuleNode module : cu.getAST().getModules()) {
            if (module.getStatementBlock() != null) {
                astNodes.add(module.getStatementBlock());
            }
            for (ClassNode classNode : module.getClasses()) {
                astNodes.add(classNode);
            }
        }
        return astNodes;
    }

    public static Statement cloneStatement(Statement statement) {
        if (statement instanceof ExpressionStatement) {
            return cloneExpressionStatement((ExpressionStatement) statement);
        } else if (statement instanceof AssertStatement) {
            return cloneAssertStatement((AssertStatement) statement);
        } else if (statement instanceof BlockStatement) {
            return cloneBlockStatement((BlockStatement) statement);
        } else if (statement instanceof BreakStatement) {
            return cloneBreakStatement((BreakStatement) statement);
        } else if (statement instanceof CaseStatement) {
            return cloneCaseStatement((CaseStatement) statement);
        } else if (statement instanceof CatchStatement) {
            return cloneCatchStatement((CatchStatement) statement);
        } else if (statement instanceof ContinueStatement) {
            return cloneContinueStatement((ContinueStatement) statement);
        } else if (statement instanceof DoWhileStatement) {
            return cloneDoWhileStatement((DoWhileStatement) statement);
        } else if (statement instanceof ForStatement) {
            return cloneForStatement((ForStatement) statement);
        } else if (statement instanceof IfStatement) {
            return cloneIfStatement((IfStatement) statement);
        } else if (statement instanceof EmptyStatement) {
            return cloneEmptyStatement((EmptyStatement) statement);
        } else if (statement instanceof ReturnStatement) {
            return cloneReturnStatement((ReturnStatement) statement);
        } else if (statement instanceof SwitchStatement) {
            return cloneSwitchStatement((SwitchStatement) statement);
        } else if (statement instanceof SynchronizedStatement) {
            return cloneSynchronizedStatement((SynchronizedStatement) statement);
        } else if (statement instanceof ThrowStatement) {
            return cloneThrowStatement((ThrowStatement) statement);
        } else if (statement instanceof TryCatchStatement) {
            return cloneTryCatchStatement((TryCatchStatement) statement);
        } else if (statement instanceof WhileStatement) {
            return cloneWhileStatement((WhileStatement) statement);
        }
        return null;
    }

    public static WhileStatement cloneWhileStatement(WhileStatement whileStatement) {
        return new WhileStatement(cloneBooleanExpression(whileStatement.getBooleanExpression()),
                cloneStatement(whileStatement.getLoopBlock()));
    }

    public static TryCatchStatement cloneTryCatchStatement(TryCatchStatement tryCatchStatement) {
        TryCatchStatement clonedTryCatchStatement = new TryCatchStatement(
                cloneStatement(tryCatchStatement.getTryStatement()),
                cloneStatement(tryCatchStatement.getFinallyStatement()));
        for (CatchStatement statement : tryCatchStatement.getCatchStatements()) {
            clonedTryCatchStatement.addCatch(cloneCatchStatement(statement));
        }
        return clonedTryCatchStatement;
    }

    public static ThrowStatement cloneThrowStatement(ThrowStatement throwStatement) {
        return new ThrowStatement(cloneExpression(throwStatement.getExpression()));
    }

    public static SynchronizedStatement cloneSynchronizedStatement(SynchronizedStatement synchronizedStatement) {
        return new SynchronizedStatement(cloneExpression(synchronizedStatement.getExpression()),
                cloneStatement(synchronizedStatement.getCode()));
    }

    public static SwitchStatement cloneSwitchStatement(SwitchStatement switchStatement) {
        List<CaseStatement> statementList = new ArrayList<CaseStatement>();
        for (CaseStatement statement : switchStatement.getCaseStatements()) {
            statementList.add(cloneCaseStatement(statement));
        }
        return new SwitchStatement(cloneExpression(switchStatement.getExpression()), statementList,
                cloneStatement(switchStatement.getDefaultStatement()));
    }

    public static ReturnStatement cloneReturnStatement(ReturnStatement returnStatement) {
        return new ReturnStatement(cloneExpression(returnStatement.getExpression()));
    }

    public static EmptyStatement cloneEmptyStatement(EmptyStatement emptyStatement) {
        return new EmptyStatement();
    }

    public static Statement cloneIfStatement(IfStatement ifStatement) {
        return new IfStatement(cloneBooleanExpression(ifStatement.getBooleanExpression()),
                cloneStatement(ifStatement.getIfBlock()), cloneStatement(ifStatement.getElseBlock()));
    }

    public static ForStatement cloneForStatement(ForStatement forStatement) {
        return new ForStatement(forStatement.getVariable(), cloneExpression(forStatement.getCollectionExpression()),
                cloneStatement(forStatement.getLoopBlock()));
    }

    public static DoWhileStatement cloneDoWhileStatement(DoWhileStatement doWhileStatement) {
        return new DoWhileStatement(cloneBooleanExpression(doWhileStatement.getBooleanExpression()),
                cloneStatement(doWhileStatement.getLoopBlock()));
    }

    public static ContinueStatement cloneContinueStatement(ContinueStatement continueStatement) {
        return new ContinueStatement(continueStatement.getLabel());
    }

    public static CatchStatement cloneCatchStatement(CatchStatement catchStatement) {
        return new CatchStatement(catchStatement.getVariable(), cloneStatement(catchStatement.getCode()));
    }

    public static CaseStatement cloneCaseStatement(CaseStatement caseStatement) {
        return new CaseStatement(cloneExpression(caseStatement.getExpression()),
                cloneStatement(caseStatement.getCode()));
    }

    public static BreakStatement cloneBreakStatement(BreakStatement breakStatement) {
        return new BreakStatement(breakStatement.getLabel());
    }

    public static BlockStatement cloneBlockStatement(BlockStatement blockStatement) {
        List<Statement> statementList = new ArrayList<Statement>();
        for (Statement statement : blockStatement.getStatements()) {
            statementList.add(cloneStatement(statement));
        }
        return new BlockStatement(statementList, blockStatement.getVariableScope());
    }

    public static AssertStatement cloneAssertStatement(AssertStatement assertStatement) {
        return new AssertStatement(cloneBooleanExpression(assertStatement.getBooleanExpression()),
                cloneExpression(assertStatement.getMessageExpression()));
    }

    public static ExpressionStatement cloneExpressionStatement(ExpressionStatement expressionStatement) {
        return new ExpressionStatement(cloneExpression(expressionStatement.getExpression()));
    }

    public static Expression cloneExpression(Expression expression) {
        if (expression instanceof ArrayExpression) {
            return cloneArrayExpression((ArrayExpression) expression);
        } else if (expression instanceof MethodCallExpression) {
            return cloneMethodCallExpression((MethodCallExpression) expression);
        } else if (expression instanceof StaticMethodCallExpression) {
            return cloneStaticMethodCallExpression((StaticMethodCallExpression) expression);
        } else if (expression instanceof VariableExpression) {
            return cloneVariableExpression((VariableExpression) expression);
        } else if (expression instanceof ClassExpression) {
            return cloneClassExpression((ClassExpression) expression);
        } else if (expression instanceof ClosureExpression) {
            return cloneClosureExpression((ClosureExpression) expression);
        } else if (expression instanceof CastExpression) {
            return cloneCastExpression((CastExpression) expression);
        } else if (expression instanceof FieldExpression) {
            return cloneFieldExpression((FieldExpression) expression);
        } else if (expression instanceof PropertyExpression) {
            return clonePropertyExpression((PropertyExpression) expression);
        } else if (expression instanceof ConstantExpression) {
            return cloneConstantExpression((ConstantExpression) expression);
        } else if (expression instanceof ArgumentListExpression) {
            return cloneArgumentListExpression((ArgumentListExpression) expression);
        } else if (expression instanceof ClosureListExpression) {
            return cloneClosureListExpression((ClosureListExpression) expression);
        } else if (expression instanceof ListExpression) {
            return cloneListExpression((ListExpression) expression);
        } else if (expression instanceof MapExpression) {
            return cloneMapExpression((MapExpression) expression);
        } else if (expression instanceof MapEntryExpression) {
            return cloneMapEntryExpression((MapEntryExpression) expression);
        } else if (expression instanceof BooleanExpression) {
            return cloneBooleanExpression((BooleanExpression) expression);
        } else if (expression instanceof BinaryExpression) {
            return cloneBinaryExpression((BinaryExpression) expression);
        } else if (expression instanceof RangeExpression) {
            return cloneRangeExpression((RangeExpression) expression);
        } else if (expression instanceof PostfixExpression) {
            return clonePostfixExpression((PostfixExpression) expression);
        } else if (expression instanceof PrefixExpression) {
            return clonePrefixExpression((PrefixExpression) expression);
        } else if (expression instanceof SpreadExpression) {
            return cloneSpreadExpression((SpreadExpression) expression);
        } else if (expression instanceof SpreadMapExpression) {
            return cloneSpreadMapExpression((SpreadMapExpression) expression);
        } else if (expression instanceof TernaryExpression) {
            return cloneTernaryExpression((TernaryExpression) expression);
        } else if (expression instanceof TupleExpression) {
            return cloneTupleExpression((TupleExpression) expression);
        } else if (expression instanceof ConstructorCallExpression) {
            return cloneConstructorCallExpression((ConstructorCallExpression) expression);
        }
        return null;
    }

    public static VariableExpression cloneVariableExpression(VariableExpression variableExpression) {
        return new VariableExpression(variableExpression.getText());
    }

    public static ClassExpression cloneClassExpression(ClassExpression classExpression) {
        return new ClassExpression(classExpression.getType());
    }

    public static ClosureExpression cloneClosureExpression(ClosureExpression closureExpression) {
        return new ClosureExpression(closureExpression.getParameters(), cloneStatement(closureExpression.getCode()));
    }

    public static PropertyExpression clonePropertyExpression(PropertyExpression propertyExpression) {
        return new PropertyExpression(cloneExpression(propertyExpression.getObjectExpression()),
                cloneExpression(propertyExpression.getProperty()));
    }

    public static FieldExpression cloneFieldExpression(FieldExpression fieldExpression) {
        return new FieldExpression(fieldExpression.getField());
    }

    public static ConstantExpression cloneConstantExpression(ConstantExpression constantExpression) {
        return new ConstantExpression(constantExpression.getValue());
    }

    public static BooleanExpression cloneBooleanExpression(BooleanExpression booleanExpression) {
        if (booleanExpression instanceof NotExpression) {
            return new NotExpression(cloneExpression(booleanExpression.getExpression()));
        }
        return new BooleanExpression(cloneExpression(booleanExpression.getExpression()));
    }

    public static BinaryExpression cloneBinaryExpression(BinaryExpression binaryExpression) {
        return new BinaryExpression(cloneExpression(binaryExpression.getLeftExpression()),
                binaryExpression.getOperation(), cloneExpression(binaryExpression.getRightExpression()));
    }

    public static CastExpression cloneCastExpression(CastExpression castExpression) {
        return new CastExpression(castExpression.getType(), cloneExpression(castExpression.getExpression()));
    }

    public static ArrayExpression cloneArrayExpression(ArrayExpression arrayExpression) {
        List<Expression> expressionList = new ArrayList<Expression>();
        for (Expression expression : arrayExpression.getExpressions()) {
            expressionList.add(cloneExpression(expression));
        }
        List<Expression> sizeExpressionList = new ArrayList<Expression>();
        for (Expression expression : arrayExpression.getSizeExpression()) {
            sizeExpressionList.add(cloneExpression(expression));
        }
        return new ArrayExpression(arrayExpression.getType(), expressionList, sizeExpressionList);
    }

    public static ListExpression cloneListExpression(ListExpression listExpression) {
        List<Expression> expressionList = new ArrayList<Expression>();
        for (Expression expression : listExpression.getExpressions()) {
            expressionList.add(cloneExpression(expression));
        }
        return new ListExpression(expressionList);
    }

    public static ArgumentListExpression cloneArgumentListExpression(ArgumentListExpression argumentListExpression) {
        List<Expression> expressionList = new ArrayList<Expression>();
        for (Expression expression : argumentListExpression.getExpressions()) {
            expressionList.add(cloneExpression(expression));
        }
        return new ArgumentListExpression(expressionList);
    }

    public static BitwiseNegationExpression cloneBitwiseNegationExpression(
            BitwiseNegationExpression bitwiseNegationExpression) {
        return new BitwiseNegationExpression(cloneExpression(bitwiseNegationExpression.getExpression()));
    }

    public static MethodCallExpression cloneMethodCallExpression(MethodCallExpression methodCallExpression) {
        return new MethodCallExpression(cloneExpression(methodCallExpression.getObjectExpression()),
                cloneExpression(methodCallExpression.getMethod()), cloneExpression(methodCallExpression.getArguments()));
    }

    public static StaticMethodCallExpression cloneStaticMethodCallExpression(
            StaticMethodCallExpression staticMethodCallExpression) {
        return new StaticMethodCallExpression(staticMethodCallExpression.getType(),
                staticMethodCallExpression.getMethod(), cloneExpression(staticMethodCallExpression.getArguments()));
    }

    public static MethodPointerExpression cloneMethodPointerExpression(MethodPointerExpression methodPointerExpression) {
        return new MethodPointerExpression(cloneExpression(methodPointerExpression.getExpression()),
                cloneExpression(methodPointerExpression.getMethodName()));
    }

    public static ConstructorCallExpression cloneConstructorCallExpression(
            ConstructorCallExpression constructorCallExpression) {
        return new ConstructorCallExpression(constructorCallExpression.getType(),
                cloneExpression(constructorCallExpression.getArguments()));
    }

    public static RangeExpression cloneRangeExpression(RangeExpression rangeExpression) {
        return new RangeExpression(cloneExpression(rangeExpression.getFrom()),
                cloneExpression(rangeExpression.getTo()), rangeExpression.isInclusive());
    }

    public static MapEntryExpression cloneMapEntryExpression(MapEntryExpression mapEntryExpression) {
        return new MapEntryExpression(cloneExpression(mapEntryExpression.getKeyExpression()),
                cloneExpression(mapEntryExpression.getValueExpression()));
    }

    public static MapExpression cloneMapExpression(MapExpression mapExpression) {
        List<MapEntryExpression> expressionList = new ArrayList<MapEntryExpression>();
        for (MapEntryExpression expression : mapExpression.getMapEntryExpressions()) {
            expressionList.add(cloneMapEntryExpression(expression));
        }
        return new MapExpression(expressionList);
    }

    public static ClosureListExpression cloneClosureListExpression(ClosureListExpression closureListExpression) {
        List<Expression> expressionList = new LinkedList<Expression>();
        for (Expression expression : closureListExpression.getExpressions()) {
            expressionList.add(cloneExpression(expression));
        }
        return new ClosureListExpression(expressionList);
    }

    public static PostfixExpression clonePostfixExpression(PostfixExpression postfixExpression) {
        return new PostfixExpression(cloneExpression(postfixExpression.getExpression()),
                postfixExpression.getOperation());
    }

    public static PrefixExpression clonePrefixExpression(PrefixExpression prefixExpression) {
        return new PrefixExpression(prefixExpression.getOperation(), cloneExpression(prefixExpression.getExpression()));
    }

    public static SpreadExpression cloneSpreadExpression(SpreadExpression spreadExpression) {
        return new SpreadExpression(cloneExpression(spreadExpression.getExpression()));
    }

    public static SpreadMapExpression cloneSpreadMapExpression(SpreadMapExpression spreadMapExpression) {
        return new SpreadMapExpression(cloneExpression(spreadMapExpression.getExpression()));
    }

    public static TernaryExpression cloneTernaryExpression(TernaryExpression ternaryExpression) {
        return new TernaryExpression(cloneBooleanExpression(ternaryExpression.getBooleanExpression()),
                cloneExpression(ternaryExpression.getTrueExpression()),
                cloneExpression(ternaryExpression.getFalseExpression()));
    }

    public static TupleExpression cloneTupleExpression(TupleExpression tupleExpression) {
        List<Expression> expressionList = new LinkedList<Expression>();
        for (Expression expression : tupleExpression.getExpressions()) {
            expressionList.add(cloneExpression(expression));
        }
        return new TupleExpression(expressionList);
    }

    public static MethodNode cloneMethodNode(MethodNode methodNode) {
        return new MethodNode(methodNode.getName(), methodNode.getModifiers(), methodNode.getReturnType(),
                methodNode.getParameters(), methodNode.getExceptions(), cloneStatement(methodNode.getCode()));
    }
}
