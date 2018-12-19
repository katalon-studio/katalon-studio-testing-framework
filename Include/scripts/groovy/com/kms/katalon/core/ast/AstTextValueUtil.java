package com.kms.katalon.core.ast;

import org.apache.commons.lang.StringUtils;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureListExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.NotExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.stmt.AssertStatement;
import org.codehaus.groovy.ast.stmt.BreakStatement;
import org.codehaus.groovy.ast.stmt.CaseStatement;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.SwitchStatement;
import org.codehaus.groovy.ast.stmt.ThrowStatement;
import org.codehaus.groovy.ast.stmt.TryCatchStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;

public class AstTextValueUtil {
    private static AstTextValueUtil _instance;

    protected AstTextValueUtil() {
    }

    public static AstTextValueUtil getInstance() {
        if (_instance == null) {
            _instance = new AstTextValueUtil();
        }
        return _instance;
    }

    public String getTextValue(Object object) {
        if (object instanceof Statement) {
            return getTextValue((Statement) object);
        } else if (object instanceof Expression) {
            return getTextValue((Expression) object);
        } else if (object instanceof Token) {
            return ((Token) object).getText();
        } else if (object instanceof Parameter) {
            Parameter parameter = (Parameter) object;
            if (parameter != ForStatement.FOR_LOOP_DUMMY) {
                return parameter.getName();
            }
        } else if (object instanceof ClassNode) {
            return ((ClassNode) object).getNameWithoutPackage();
        }
        return StringUtils.EMPTY;
    }

    public String getTextValue(Statement statement) {
        if (statement instanceof ExpressionStatement) {
            return getTextValue((ExpressionStatement) statement);
        } else if (statement instanceof IfStatement) {
            return getTextValue((IfStatement) statement);
        } else if (statement instanceof AssertStatement) {
            return getTextValue((AssertStatement) statement);
        } else if (statement instanceof ForStatement) {
            return getTextValue((ForStatement) statement);
        } else if (statement instanceof WhileStatement) {
            return getTextValue((WhileStatement) statement);
        } else if (statement instanceof TryCatchStatement) {
            return getTextValue((TryCatchStatement) statement);
        } else if (statement instanceof CatchStatement) {
            return getTextValue((CatchStatement) statement);
        } else if (statement instanceof SwitchStatement) {
            return getTextValue((SwitchStatement) statement);
        } else if (statement instanceof CaseStatement) {
            return getTextValue((CaseStatement) statement);
        } else if (statement instanceof BreakStatement) {
            return getTextValue((BreakStatement) statement);
        } else if (statement instanceof ThrowStatement) {
            return getTextValue((ThrowStatement) statement);
        }
        return statement.getText();
    }

    public String getTextValue(IfStatement ifStatement) {
        return "if " + "(" + getTextValue(ifStatement.getBooleanExpression()) + ")";
    }

    public String getTextValue(ExpressionStatement expressionStatement) {
        return getTextValue(expressionStatement.getExpression());
    }

    public String getTextValue(AssertStatement assertStatement) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("assert ");
        stringBuilder.append(getTextValue(assertStatement.getBooleanExpression()));
        if (assertStatement.getMessageExpression() instanceof ConstantExpression
                && !(((ConstantExpression) assertStatement.getMessageExpression()).getValue() == null)) {
            stringBuilder.append(" : ");
            stringBuilder.append(getTextValue(assertStatement.getMessageExpression()));
        }
        return stringBuilder.toString();
    }

    public String getInputTextValue(ForStatement forStatement) {
        String value = "";
        if (!(forStatement.getCollectionExpression() instanceof ClosureListExpression)) {
            if (forStatement.getVariable() != ForStatement.FOR_LOOP_DUMMY) {
                StringBuilder stringBuilder = new StringBuilder();
                GroovyParser astUtil = new GroovyParser(stringBuilder);
                astUtil.parse(new Parameter[] { forStatement.getVariable() });
                value += astUtil.getValue();
                value += " : ";
            }
        }
        return value + getTextValue(forStatement.getCollectionExpression());
    }

    public String getTextValue(ForStatement forStatement) {
        return "for (" + getInputTextValue(forStatement) + ")";
    }

    public String getTextValue(WhileStatement whileStatement) {
        return "while " + "(" + getTextValue(whileStatement.getBooleanExpression()) + ")";
    }

    public String getTextValue(TryCatchStatement catchStatement) {
        return ("try");
    }

    public String getTextValue(ThrowStatement throwStatement) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("throw ");
        stringBuilder.append(getTextValue(throwStatement.getExpression()));
        return stringBuilder.toString();
    }

    public String getTextValue(CatchStatement catchStatement) {
        StringBuilder stringBuilder = new StringBuilder();
        new GroovyParser(stringBuilder).parse(new Parameter[] { catchStatement.getVariable() });
        return ("catch (" + stringBuilder.toString() + ")");
    }

    public String getTextValue(SwitchStatement switchStatement) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("switch (");
        stringBuilder.append(getTextValue(switchStatement.getExpression()));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public String getTextValue(CaseStatement caseStatement) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("case ");
        stringBuilder.append(getTextValue(caseStatement.getExpression()));
        stringBuilder.append(":");
        return stringBuilder.toString();
    }

    public String getTextValue(BreakStatement breakStatement) {
        return "break";
    }

    public String getTextValue(Expression expression) {
        if (expression instanceof BinaryExpression) {
            return getTextValue((BinaryExpression) expression);
        } else if (expression instanceof ConstantExpression) {
            return getTextValue((ConstantExpression) expression);
        } else if (expression instanceof StaticMethodCallExpression) {
            return getTextValue((StaticMethodCallExpression) expression);
        } else if (expression instanceof MethodCallExpression) {
            return getTextValue((MethodCallExpression) expression);
        } else if (expression instanceof TupleExpression) {
            return getTextValue((TupleExpression) expression);
        } else if (expression instanceof PropertyExpression) {
            return getTextValue((PropertyExpression) expression);
        } else if (expression instanceof CastExpression) {
            return getTextValue((CastExpression) expression);
        } else if (expression instanceof ListExpression) {
            return getTextValue((ListExpression) expression);
        } else if (expression instanceof MapExpression) {
            return getTextValue((MapExpression) expression);
        } else if (expression instanceof BooleanExpression) {
            return getTextValue((BooleanExpression) expression);
        } else if (expression instanceof ArgumentListExpression) {
            return getTextValue((ArgumentListExpression) expression);
        } else if (expression instanceof ClassExpression) {
            return getTextValue((ClassExpression) expression);
        } else if (expression != null) {
            return expression.getText();
        } else {
            return "null";
        }
    }

    public String getTextValue(ArgumentListExpression argumentListExpression) {
        StringBuilder value = new StringBuilder();
        value.append("(");
        int count = argumentListExpression.getExpressions().size();
        for (Expression expression : argumentListExpression.getExpressions()) {
            value.append(getTextValue(expression));
            count--;
            if (count > 0) {
                value.append(", ");
            }
        }
        value.append(")");
        return value.toString();
    }

    public String getTextValue(CastExpression castExpression) {
        return getTextValue(castExpression.getExpression());
    }

    public String getTextValue(BooleanExpression booleanExpression) {
        if (booleanExpression instanceof NotExpression) {
            return "!(" + getTextValue(booleanExpression.getExpression()) + ")";
        }
        return getTextValue(booleanExpression.getExpression());
    }

    public String getTextValue(ConstantExpression constantExpression) {
        if (constantExpression.getValue() instanceof String) {
            return "\"" + constantExpression.getText() + "\"";
        } else if (constantExpression.getValue() instanceof Character) {
            return "'" + constantExpression.getText() + "'";
        }
        return constantExpression.getText();
    }

    public String getTextValue(BinaryExpression binaryExpression) {
        if (binaryExpression.getOperation().getType() == Types.LEFT_SQUARE_BRACKET) {
            return getTextValue(binaryExpression.getLeftExpression()) + "["
                    + getTextValue(binaryExpression.getRightExpression()) + "]";
        }
        return getTextValue(binaryExpression.getLeftExpression()) + " " + binaryExpression.getOperation().getText()
                + " " + getTextValue(binaryExpression.getRightExpression());
    }
    
    public String getTextValue(StaticMethodCallExpression methodCallExpression) {
        String fullyQualifiedObject = methodCallExpression.getOwnerType().getTypeClass().getName();
        String meth = methodCallExpression.getMethod();
        String args = getTextValue(methodCallExpression.getArguments());
        return getMethodCallTextValue(fullyQualifiedObject, meth, args);
    }

    private String getMethodCallTextValue(String fullyQualifiedObject, String meth, String args) {
        String object;
        if (fullyQualifiedObject.equals("this") || 
                fullyQualifiedObject.startsWith("com.kms.katalon") ||
                fullyQualifiedObject.startsWith("CustomKeywords")) {
            object = "";
        } else {
            String[] tokens = fullyQualifiedObject.split("\\.");
            object = tokens[tokens.length - 1] + ".";
        }
        return object + meth + args;
    }

    public String getTextValue(MethodCallExpression methodCallExpression) {
        String fullyQualifiedObject = getTextValue(methodCallExpression.getObjectExpression());
        String meth = methodCallExpression.getMethod().getText();
        String args = getTextValue(methodCallExpression.getArguments());
        return getMethodCallTextValue(fullyQualifiedObject, meth, args);
    }
    
    public String getTextValue(ClassExpression classExpression) {
        return classExpression.getType().getTypeClass().getName();
    }

    public String getTextValue(TupleExpression tupleExpression) {
        StringBuilder buffer = new StringBuilder("(");
        boolean first = true;
        for (Expression expression : tupleExpression.getExpressions()) {
            if (first) {
                first = false;
            } else {
                buffer.append(", ");
            }

            buffer.append(getTextValue(expression));
        }
        buffer.append(")");
        return buffer.toString();
    }

    public String getTextValue(PropertyExpression propertyExpression) {
        return propertyExpression.getProperty().getText();
    }

    public String getTextValue(ListExpression listExpression) {
        StringBuilder buffer = new StringBuilder("[");
        boolean first = true;
        for (Expression expression : listExpression.getExpressions()) {
            if (first) {
                first = false;
            } else {
                buffer.append(", ");
            }

            buffer.append(getTextValue(expression));
        }
        buffer.append("]");
        return buffer.toString();
    }

    public String getTextValue(MapExpression mapExpression) {
        StringBuilder sb = new StringBuilder(32);
        sb.append("[");
        int size = mapExpression.getMapEntryExpressions().size();
        MapEntryExpression mapEntryExpression = null;
        if (size > 0) {
            mapEntryExpression = mapExpression.getMapEntryExpressions().get(0);
            sb.append(getTextValue(mapEntryExpression.getKeyExpression()) + ":"
                    + getTextValue(mapEntryExpression.getValueExpression()));
            for (int i = 1; i < size; i++) {
                mapEntryExpression = mapExpression.getMapEntryExpressions().get(i);
                sb.append(", " + getTextValue(mapEntryExpression.getKeyExpression()) + ":"
                        + getTextValue(mapEntryExpression.getValueExpression()));
                if (sb.length() > 120 && i < size - 1) {
                    sb.append(", ... ");
                    break;
                }
            }
        } else {
            sb.append(":");
        }
        sb.append("]");
        return sb.toString();
    }

    public String getTextValue(FieldNode fieldNode) {
        if (fieldNode.getInitialExpression() != null) {
            return fieldNode.getName() + " = " + getTextValue(fieldNode.getInitialExpression());
        }
        return fieldNode.getName();
    }
}
