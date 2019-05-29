package org.f2blib.visitor;

import org.f2blib.ast.*;
import org.junit.Test;

import static org.f2blib.ast.ASTTest.createFunctionDefinition;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class PrettyPrintVisitorTest {

    @Test
    public void simpleFormula() {

        FunctionDefinition fd = createFunctionDefinition("MyFunc",
                new Sin(new Multiplication(Constant.PI, new Variable(0))));

        PrettyPrintVisitor ppv = new PrettyPrintVisitor();

        fd.accept(ppv);

        assertThat(ppv.getString(), is("function MyFunc;\nbegin\n    f_1 := sin(PI * x_1);\nend\n"));
    }

    @Test
    public void operatorPrecendence() {

        FunctionDefinition fd = createFunctionDefinition("MyFunc",
                new Multiplication(new Addition(new Int(3), new Int(4)), new Int(5)));

        PrettyPrintVisitor ppv = new PrettyPrintVisitor();

        fd.accept(ppv);

        assertThat(ppv.getString(), is("function MyFunc;\nbegin\n    f_1 := (3 + 4) * 5;\nend\n"));
    }

}
