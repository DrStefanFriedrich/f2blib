package com.github.drstefanfriedrich.f2blib.visitor;

import com.github.drstefanfriedrich.f2blib.ast.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PrettyPrintVisitorTest {

    private static final String FUNCTION_NAME = "MyFunc";

    private static final String SIMPLE_FUNCTION_DEFINITION = "function MyFunc;\nbegin\n    f_1 := x_1;\nend\n";

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private PrettyPrintVisitor underTest;

    @Before
    public void setup() {
        underTest = new PrettyPrintVisitor();
    }

    @Test
    public void simpleFormula() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Sin(new Multiplication(Constant.PI, new Variable(0))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := sin(PI * x_1);\nend\n"));
    }

    @Test
    public void operatorPrecendence() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Multiplication(new Addition(new Int(3), new Int(4)), new Int(5)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := (3 + 4) * 5;\nend\n"));
    }

    @Test
    public void absSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Abs(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := abs x_1;\nend\n"));
    }

    @Test
    public void absLowerPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Neg(new Abs(new Variable(0))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := -abs x_1;\nend\n"));
    }

    @Test
    public void absHigherPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Multiplication(new Abs(new Variable(0)), new Int(5)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := abs x_1 * 5;\nend\n"));
    }

    @Test
    public void additionSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Addition(new Variable(0), new Parameter(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := x_1 + p_1;\nend\n"));
    }

    @Test
    public void additionLowerPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Multiplication(new Addition(new Variable(0), new Parameter(0)), new Int(3)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := (x_1 + p_1) * 3;\nend\n"));
    }

    @Test
    public void arccosSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Arccos(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := arccos x_1;\nend\n"));
    }

    @Test
    public void arccosLowerPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Addition(new Arccos(new Variable(0)), new Variable(1)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := arccos x_1 + x_2;\nend\n"));
    }

    @Test
    public void arccosLowerPrecedenceInside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Arccos(new Neg(new Variable(0))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := arccos -x_1;\nend\n"));
    }

    @Test
    public void arcoshSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Arcosh(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := arcosh x_1;\nend\n"));
    }

    @Test
    public void arcoshLowerPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Division(new Int(1), new Arcosh(new Variable(0))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := 1 / arcosh x_1;\nend\n"));
    }

    @Test
    public void arcoshLowerPrecedenceInside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Arcosh(new Faculty(new Int(5))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := arcosh 5!;\nend\n"));
    }

    @Test
    public void arcsinSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Arcsin(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := arcsin x_1;\nend\n"));
    }

    @Test
    public void arctanSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Arctan(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := arctan x_1;\nend\n"));
    }

    @Test
    public void arsinhSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Arsinh(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := arsinh x_1;\nend\n"));
    }

    @Test
    public void artanhSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Artanh(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := artanh x_1;\nend\n"));
    }

    @Test
    public void binomialSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Binomial(new Int(3), new Int(1)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := binomial(3, 1);\nend\n"));
    }

    @Test
    public void binomialLowerPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Neg(new Binomial(new Int(3), new Int(1))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := -binomial(3, 1);\nend\n"));
    }

    @Test
    public void binomialHigherPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Addition(new Parameter(0), new Binomial(new Int(3), new Int(1))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := p_1 + binomial(3, 1);\nend\n"));
    }

    @Test
    public void constantSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, Constant.PI);

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := PI;\nend\n"));
    }

    @Test
    public void constantHigherPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Faculty(new Int(5)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := 5!;\nend\n"));
    }

    @Test
    public void cosSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Cos(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := cos x_1;\nend\n"));
    }

    @Test
    public void coshSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Cosh(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := cosh x_1;\nend\n"));
    }

    @Test
    public void divisionSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Division(new Variable(0), new Parameter(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := x_1 / p_1;\nend\n"));
    }

    @Test
    public void divisionLowerPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Neg(new Division(new Variable(0), new Parameter(0))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := -(x_1 / p_1);\nend\n"));
    }

    @Test
    public void divisionHigherPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Addition(new Division(new Variable(0), new Parameter(0)), new Int(4)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := x_1 / p_1 + 4;\nend\n"));
    }


    @Test
    public void divisionLowerPrecedenceInside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Division(new Sinh(new Variable(0)), new Faculty(new Int(3))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := sinh x_1 / 3!;\nend\n"));
    }

    @Test
    public void divisionHigherPrecedenceInside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Division(new Addition(new Int(3), new Int(4)), new Int(5)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := (3 + 4) / 5;\nend\n"));
    }


    @Test
    public void doubSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Doub(6.71));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := 6.71;\nend\n"));
    }

    @Test
    public void expSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Exp(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := exp x_1;\nend\n"));
    }

    @Test
    public void facultySimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Faculty(new Int(1)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := 1!;\nend\n"));
    }

    @Test
    public void facultyLowerPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Power(new Int(3), new Faculty(new Int(4))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := 3 ^ (4!);\nend\n"));
    }

    @Test
    public void intSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Int(1));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := 1;\nend\n"));
    }

    @Test
    public void lnSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Ln(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := ln x_1;\nend\n"));
    }

    @Test
    public void multiplicationSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Multiplication(new Variable(0), new Parameter(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := x_1 * p_1;\nend\n"));
    }

    @Test
    public void multiplicationLowerPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Neg(new Multiplication(new Sinh(new Variable(0)), Constant.PI)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := -(sinh x_1 * PI);\nend\n"));
    }

    @Test
    public void negSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Neg(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := -x_1;\nend\n"));
    }

    @Test
    public void negLowerPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Power(new Neg(new Int(3)), new Int(4)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := (-3) ^ 4;\nend\n"));
    }

    @Test
    public void negHigherPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Sin(new Neg(new Variable(0))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := sin -x_1;\nend\n"));
    }

    @Test
    public void parameterSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Parameter(0));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := p_1;\nend\n"));
    }

    @Test
    public void parenthesisSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Parenthesis(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is(SIMPLE_FUNCTION_DEFINITION));
    }

    @Test
    public void parenthesisLowerPrecedenceInside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Parenthesis(new Sin(new Parenthesis(new Variable(0)))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := sin x_1;\nend\n"));
    }

    @Test
    public void posSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Pos(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is(SIMPLE_FUNCTION_DEFINITION));
    }

    @Test
    public void posLowerPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Pos(new Addition(new Pos(new Variable(0)), new Pos(new Variable(1)))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := +(x_1 + x_2);\nend\n"));
    }

    @Test
    public void powerSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Power(new Variable(0), new Parameter(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := x_1 ^ p_1;\nend\n"));
    }

    @Test
    public void roundSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Round(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := round x_1;\nend\n"));
    }

    @Test
    public void sinSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Sin(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := sin x_1;\nend\n"));
    }

    @Test
    public void sinhSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Sinh(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := sinh x_1;\nend\n"));
    }

    @Test
    public void sqrtSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Sqrt(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := sqrt x_1;\nend\n"));
    }

    @Test
    public void subtractionSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Subtraction(new Variable(0), new Parameter(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := x_1 - p_1;\nend\n"));
    }

    @Test
    public void subtractionLowerPrecedenceOutside() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME,
                new Multiplication(new Subtraction(new Multiplication(new Variable(0), new Parameter(0)),
                        Constant.PI), new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := (x_1 * p_1 - PI) * x_1;\nend\n"));
    }

    @Test
    public void tanSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Tan(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := tan x_1;\nend\n"));
    }

    @Test
    public void tanhSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Tanh(new Variable(0)));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function MyFunc;\nbegin\n    f_1 := tanh x_1;\nend\n"));
    }

    @Test
    public void variableSimple() {

        FunctionDefinition fd = ASTTest.createFunctionDefinition(FUNCTION_NAME, new Variable(0));

        fd.accept(underTest);

        assertThat(underTest.getString(), is(SIMPLE_FUNCTION_DEFINITION));
    }

    @Test
    public void forLoopAndForVar() {

        FunctionDefinition fd = new FunctionDefinition("a.b.c.Test", new FunctionBody(new ForLoop("i", 0, 1, 2,
                new FunctionsWrapper(new Function(0, new ForVar("i"))))));

        fd.accept(underTest);

        assertThat(underTest.getString(), is("function a.b.c.Test;\nbegin\n    for i from round p_1 to round p_2 step round p_3;\n    begin\n        f_1 := i;\n    end\nend\n"));
    }

    @Test
    public void noOp() {

        FunctionDefinition fd = new FunctionDefinition("a.b.c.Test", new FunctionBody(new ForLoop("i", 0, 1, 2,
                new FunctionsWrapper(new Function(0, NoOp.get())))));

        exception.expect(IllegalStateException.class);
        exception.expectMessage("visitNoOp must not be called on the PrettyPrintVisitor");

        fd.accept(underTest);
    }

}
