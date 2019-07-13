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

        List<Function> result = new ArrayList<>();

        for (FunctionsParser.Single_valued_functionContext function : functions) {
            result.add((Function) function.accept(this));
        }

        if (result.isEmpty()) {
            return null;
        }

        if (ctx.offset != null) {
            return new FunctionsWrapper(result, new MarkovShift(Integer.parseInt(ctx.offset.getText())));
        }

        return new FunctionsWrapper(result);
    }

    @Override
    public Object visitFor_loop(FunctionsParser.For_loopContext ctx) {

        String variableName = ctx.forVar.getText();
        int startParameterIndex = Integer.parseInt(ctx.start.getText().substring(2)) - 1;
        int endParameterIndex = Integer.parseInt(ctx.end.getText().substring(2)) - 1;
        int stepParameterIndex = Integer.parseInt(ctx.step.getText().substring(2)) - 1;
        FunctionsWrapper functionsWrapper = (FunctionsWrapper) ctx.single_valued_functions().accept(this);

        return new ForLoop(variableName, startParameterIndex, endParameterIndex, stepParameterIndex,
                functionsWrapper);
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
    public Object visitBinomial(FunctionsParser.BinomialContext ctx) {

        return new Binomial((IntExpression) ctx.n.accept(this), (IntExpression) ctx.k.accept(this));
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
    public Object visitFaculty(FunctionsParser.FacultyContext ctx) {

        return new Faculty((IntExpression) ctx.inner.accept(this));
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
    public Object visitInteger(FunctionsParser.IntegerContext ctx) {
        return new Int(Integer.parseInt(ctx.INDEX().getText()));
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
    public Object visitParam(FunctionsParser.ParamContext ctx) {

        return new Parameter(Integer.parseInt(ctx.param.getText().substring(2)) - 1);
    }

    @Override
    public Object visitVar(FunctionsParser.VarContext ctx) {

        return new Variable(Integer.parseInt(ctx.var.getText().substring(2)) - 1);
    }

    @Override
    public Object visitForVar(FunctionsParser.ForVarContext ctx) {
        return new ForVar(ctx.variableName.getText());
    }

}
