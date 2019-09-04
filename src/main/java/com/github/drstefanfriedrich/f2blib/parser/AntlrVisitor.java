/*****************************************************************************
 *
 * Copyright (c) 2019 Stefan Friedrich
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package com.github.drstefanfriedrich.f2blib.parser;

import com.github.drstefanfriedrich.f2blib.antlr.FunctionsBaseVisitor;
import com.github.drstefanfriedrich.f2blib.antlr.FunctionsParser;
import com.github.drstefanfriedrich.f2blib.ast.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Visit the parser output of Antlr and convert it to a {@link com.github.drstefanfriedrich.f2blib.ast.FunctionDefinition}.
 */
class AntlrVisitor extends FunctionsBaseVisitor<Object> {

    @Override
    public Object visitFunction_definition(FunctionsParser.Function_definitionContext ctx) {

        String className = ctx.className.getText();

        return new FunctionDefinition(className, (FunctionBody) ctx.function_body().accept(this));
    }

    @Override
    public Object visitFunction_body(FunctionsParser.Function_bodyContext ctx) {

        if (ctx.for_loop() == null) {
            return new FunctionBody((FunctionsWrapper) ctx.single_valued_functions().accept(this));
        }
        return new FunctionBody((ForLoop) ctx.for_loop().accept(this));
    }

    @Override
    public Object visitSingle_valued_functions(FunctionsParser.Single_valued_functionsContext ctx) {

        List<FunctionsParser.Single_valued_functionContext> functions = ctx.single_valued_function();
        List<FunctionsParser.Auxiliary_variableContext> auxVars = ctx.auxiliary_variable();

        List<Function> result = new ArrayList<>();
        List<AuxiliaryVariable> auxs = new ArrayList<>();

        for (FunctionsParser.Single_valued_functionContext function : functions) {
            result.add((Function) function.accept(this));
        }
        for (FunctionsParser.Auxiliary_variableContext aux : auxVars) {
            auxs.add((AuxiliaryVariable) aux.accept(this));
        }

        if (result.isEmpty()) {
            return null;
        }

        if (ctx.offset != null) {
            return new FunctionsWrapper(auxs, result,
                    new MarkovShift((IntExpression) ctx.offset.accept(this)));
        }

        return new FunctionsWrapper(result);
    }

    @Override
    public Object visitFor_loop(FunctionsParser.For_loopContext ctx) {

        String variableName = ctx.intVar.getText();
        IntExpression start = (IntExpression) ctx.start.accept(this);
        IntExpression end = (IntExpression) ctx.end.accept(this);
        IntExpression step = (IntExpression) ctx.step.accept(this);
        FunctionsWrapper functionsWrapper = (FunctionsWrapper) ctx.single_valued_functions().accept(this);

        return new ForLoop(variableName, start, end, step, functionsWrapper);
    }

    @Override
    public Object visitSingle_valued_function(FunctionsParser.Single_valued_functionContext ctx) {

        return new Function(Integer.parseInt(ctx.FUNC().getText().substring(2)) - 1, (Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitAbs(FunctionsParser.AbsContext ctx) {

        return new Abs((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitRound(FunctionsParser.RoundContext ctx) {

        return new Round((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitExp(FunctionsParser.ExpContext ctx) {

        return new Exp((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitLn(FunctionsParser.LnContext ctx) {

        return new Ln((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitSqrt(FunctionsParser.SqrtContext ctx) {

        return new Sqrt((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitSin(FunctionsParser.SinContext ctx) {

        return new Sin((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitCos(FunctionsParser.CosContext ctx) {

        return new Cos((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitTan(FunctionsParser.TanContext ctx) {

        return new Tan((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitArcsin(FunctionsParser.ArcsinContext ctx) {

        return new Arcsin((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitArccos(FunctionsParser.ArccosContext ctx) {

        return new Arccos((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitArctan(FunctionsParser.ArctanContext ctx) {

        return new Arctan((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitSinh(FunctionsParser.SinhContext ctx) {

        return new Sinh((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitCosh(FunctionsParser.CoshContext ctx) {

        return new Cosh((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitTanh(FunctionsParser.TanhContext ctx) {

        return new Tanh((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitArsinh(FunctionsParser.ArsinhContext ctx) {

        return new Arsinh((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitArcosh(FunctionsParser.ArcoshContext ctx) {

        return new Arcosh((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitArtanh(FunctionsParser.ArtanhContext ctx) {

        return new Artanh((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitNeg(FunctionsParser.NegContext ctx) {

        return new Neg((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitPos(FunctionsParser.PosContext ctx) {

        return new Pos((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitDivision(FunctionsParser.DivisionContext ctx) {

        return new Division((Expression) ctx.left.accept(this), (Expression) ctx.right.accept(this));
    }

    @Override
    public Object visitMultiplication(FunctionsParser.MultiplicationContext ctx) {

        return new Multiplication((Expression) ctx.left.accept(this), (Expression) ctx.right.accept(this));
    }

    @Override
    public Object visitSubtraction(FunctionsParser.SubtractionContext ctx) {

        return new Subtraction((Expression) ctx.left.accept(this), (Expression) ctx.right.accept(this));
    }

    @Override
    public Object visitAddition(FunctionsParser.AdditionContext ctx) {

        return new Addition((Expression) ctx.left.accept(this), (Expression) ctx.right.accept(this));
    }

    @Override
    public Object visitPower(FunctionsParser.PowerContext ctx) {

        return new Power((Expression) ctx.left.accept(this), (Expression) ctx.right.accept(this));
    }

    @Override
    public Object visitParenthesis(FunctionsParser.ParenthesisContext ctx) {

        return new Parenthesis((Expression) ctx.inner.accept(this));
    }

    @Override
    public Object visitDoub(FunctionsParser.DoubContext ctx) {

        return new Doub(Double.valueOf(ctx.value.getText()));
    }

    @Override
    public Object visitInt(FunctionsParser.IntContext ctx) {

        return new Int(Integer.valueOf(ctx.value.getText()));
    }

    @Override
    public Object visitConst(FunctionsParser.ConstContext ctx) {

        if (ctx.value.getText().equals("pi")) {
            return Constant.PI;
        } else if (ctx.value.getText().equals("e")) {
            return Constant.E;
        } else if (ctx.value.getText().equals("boltzmann")) {
            return Constant.BOLTZMANN;
        } else {
            throw new IllegalArgumentException(format("Unrecognized enum constant %s", ctx.value.getText()));
        }
    }

    @Override
    public Object visitIntVar(FunctionsParser.IntVarContext ctx) {
        return ctx.specialVariable().accept(this);
    }

    @Override
    public Object visitIintVar(FunctionsParser.IintVarContext ctx) {
        return ctx.specialVariable().accept(this);
    }

    @Override
    public Object visitIaddition(FunctionsParser.IadditionContext ctx) {

        return new Addition((IntExpression) ctx.left.accept(this), (IntExpression) ctx.right.accept(this));
    }

    @Override
    public Object visitIsubtraction(FunctionsParser.IsubtractionContext ctx) {

        return new Subtraction((IntExpression) ctx.left.accept(this), (IntExpression) ctx.right.accept(this));
    }

    @Override
    public Object visitImultiplication(FunctionsParser.ImultiplicationContext ctx) {

        return new Multiplication((IntExpression) ctx.left.accept(this), (IntExpression) ctx.right.accept(this));
    }

    @Override
    public Object visitIdivision(FunctionsParser.IdivisionContext ctx) {

        return new Division((IntExpression) ctx.left.accept(this), (IntExpression) ctx.right.accept(this));
    }

    @Override
    public Object visitIneg(FunctionsParser.InegContext ctx) {

        return new Neg((IntExpression) ctx.inner.accept(this));
    }

    @Override
    public Object visitIpos(FunctionsParser.IposContext ctx) {

        return new Pos((IntExpression) ctx.inner.accept(this));
    }

    @Override
    public Object visitIint(FunctionsParser.IintContext ctx) {

        return new Int(Integer.valueOf(ctx.value.getText()));
    }

    @Override
    public Object visitIpower(FunctionsParser.IpowerContext ctx) {

        return new Power((IntExpression) ctx.left.accept(this), (IntExpression) ctx.right.accept(this));
    }

    @Override
    public Object visitIparenthesis(FunctionsParser.IparenthesisContext ctx) {

        return new Parenthesis((IntExpression) ctx.inner.accept(this));
    }

    @Override
    public Object visitIfaculty(FunctionsParser.IfacultyContext ctx) {

        return new Faculty((IntExpression) ctx.inner.accept(this));
    }

    @Override
    public Object visitIbinomial(FunctionsParser.IbinomialContext ctx) {

        return new Binomial((IntExpression) ctx.n.accept(this), (IntExpression) ctx.k.accept(this));
    }

    @Override
    public Object visitVariable(FunctionsParser.VariableContext ctx) {

        if (ctx.intExpression() != null) {
            return new Variable((IntExpression) ctx.intExpression().accept(this));
        } else {
            return new Variable(Integer.parseInt(ctx.VARIABLE().getText().substring(2)) - 1);
        }
    }

    @Override
    public Object visitParameter(FunctionsParser.ParameterContext ctx) {

        if (ctx.intExpression() != null) {
            return new Parameter((IntExpression) ctx.intExpression().accept(this));
        } else {
            return new Parameter(Integer.parseInt(ctx.PARAMETER().getText().substring(2)) - 1);
        }
    }

    @Override
    public Object visitSum(FunctionsParser.SumContext ctx) {

        Expression inner = (Expression) ctx.inner.accept(this);
        String variableName = ctx.variableName.getText();
        IntExpression start = (IntExpression) ctx.start.accept(this);
        IntExpression end = (IntExpression) ctx.end.accept(this);

        return new Sum(inner, variableName, start, end);
    }

    @Override
    public Object visitIsum(FunctionsParser.IsumContext ctx) {

        IntExpression inner = (IntExpression) ctx.inner.accept(this);
        String variableName = ctx.variableName.getText();
        IntExpression start = (IntExpression) ctx.start.accept(this);
        IntExpression end = (IntExpression) ctx.end.accept(this);

        return new Sum(inner, variableName, start, end);
    }

    @Override
    public Object visitProd(FunctionsParser.ProdContext ctx) {

        Expression inner = (Expression) ctx.inner.accept(this);
        String variableName = ctx.variableName.getText();
        IntExpression start = (IntExpression) ctx.start.accept(this);
        IntExpression end = (IntExpression) ctx.end.accept(this);

        return new Prod(inner, variableName, start, end);
    }

    @Override
    public Object visitIprod(FunctionsParser.IprodContext ctx) {

        IntExpression inner = (IntExpression) ctx.inner.accept(this);
        String variableName = ctx.variableName.getText();
        IntExpression start = (IntExpression) ctx.start.accept(this);
        IntExpression end = (IntExpression) ctx.end.accept(this);

        return new Prod(inner, variableName, start, end);
    }

    @Override
    public Object visitAuxiliary_variable(FunctionsParser.Auxiliary_variableContext ctx) {

        AuxVar auxVar = (AuxVar) ctx.specialVariable().accept(this);

        return new AuxiliaryVariable(auxVar, (Expression) ctx.expression().accept(this));
    }

    @Override
    public Object visitSpecialVariable(FunctionsParser.SpecialVariableContext ctx) {

        String varName = ctx.IDENTIFIER().getText();

        if (varName.substring(0, 1).equals(varName.substring(0, 1).toLowerCase())) {
            return new IntVar(varName);
        } else {
            return new AuxVar(varName);
        }
    }

}
