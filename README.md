<!----------------------------------------------------------------------------

   Copyright (c) 2019 Stefan Friedrich

   This program and the accompanying materials are made available under the
   terms of the Eclipse Public License 2.0 which is available at
   https://www.eclipse.org/legal/epl-2.0/

   SPDX-License-Identifier: EPL-2.0

 ------------------------------------------------------------------------------>

# F2BLib &mdash; Function to Bytecode Library

[![Language Java](https://img.shields.io/static/v1.svg?label=Language&message=Java&color=fbff8a&style=plastic)](https://openjdk.java.net/)
[![Build Gradle](https://img.shields.io/static/v1.svg?label=Build&message=Gradle&color=ffe780&style=plastic)](https://gradle.org/)
[![License EPL v2.0](https://img.shields.io/static/v1.svg?label=License&message=EPL&nbsp;v2.0&color=ffcf85&style=plastic)](https://www.eclipse.org/legal/epl-2.0/)
[![Version 1.0.2](https://img.shields.io/static/v1.svg?label=Nexus&message=1.0.2&color=ffb994&style=plastic)](https://repo1.maven.org/maven2/com/github/drstefanfriedrich/f2blib/f2blib/1.0.2/)
<br />
[![Travis CI](https://travis-ci.org/DrStefanFriedrich/f2blib.svg?branch=master)](https://travis-ci.org/DrStefanFriedrich/f2blib)
[![SonarQube](https://img.shields.io/static/v1.svg?label=SonarQube&message=analyse&color=green&style=plastic)](https://sonarcloud.io/dashboard?id=DrStefanFriedrich_f2blib)
[![Javadocs](https://www.javadoc.io/badge/com.github.drstefanfriedrich.f2blib/f2blib.svg)](https://www.javadoc.io/doc/com.github.drstefanfriedrich.f2blib/f2blib)

## TL;TR

Parse mathematical function expressions, convert them to Java bytecode, and evaluate
them _very_ quickly.


## Introduction

F2BLib &ndash; Function to Bytecode Library &ndash; defines a [Grammar](src/main/antlr/com/github/drstefanfriedrich/f2blib/antlr/Functions.g4)
for real-valued mathematical function expressions. It parses an input source using [Antlr4](https://www.antlr.org/)
and converts the resulting abstract syntax tree (AST) to Java bytecode using [ASM](https://asm.ow2.io/). The
functions can then be evaluated _very_ fast.

Example of an abstract syntax tree:

![Example of an Abstract Syntax Tree](src/main/docs/ast.png)

By real-valued mathematical functions we mean mappings of the form

<center>
<!-- Generated with https://www.mathtowebonline.com/ -->
<!--
<math xmlns="http://www.w3.org/1998/Math/MathML">
<mrow>
	<mi>f</mi>
	<mo>:</mo>
	<msup>
		<mi>&#x0211D;</mi>
		<mi>k</mi>
	</msup>
	<mo>&#x000D7;</mo>
	<msup>
		<mi>&#x0211D;</mi>
		<mi>n</mi>
	</msup>
	<mo>&#x02192;</mo>
	<msup>
		<mi>&#x0211D;</mi>
		<mi>m</mi>
	</msup>
	<mo>,</mo>
	<mrow>
		<mo form="prefix">(</mo>
		<mi>p</mi>
		<mo>,</mo>
		<mi>x</mi>
		<mo form="postfix">)</mo>
	</mrow>
	<mo>&#x021A6;</mo>
	<mi>y</mi>
	<mo>:</mo>
	<mo>=</mo>
	<msub>
		<mi>f</mi>
		<mi>p</mi>
	</msub>
	<mrow>
		<mo form="prefix">(</mo>
		<mi>x</mi>
		<mo form="postfix">)</mo>
	</mrow>
</mrow>
</math>
-->
<img src="src/main/docs/formula.png" />
</center>


## Getting Started

Suppose you want to evaluate a real-valued function with two variables and two parameters:

<center>
<!--
<math xmlns="http://www.w3.org/1998/Math/MathML">
<mrow>
	<msub>
		<mi>f</mi>
		<mi>p</mi>
	</msub>
	<mo>:</mo>
	<msup>
		<mi>&#x0211D;</mi>
		<mn>2</mn>
	</msup>
	<mo>&#x02192;</mo>
	<msup>
		<mi>&#x0211D;</mi>
		<mn>2</mn>
	</msup>
	<mo>,</mo>
	<mrow>
		<mo form="prefix">(</mo>
		<mi>x</mi>
		<mo>,</mo>
		<mi>p</mi>
		<mo form="postfix">)</mo>
	</mrow>
	<mo>&#x021A6;</mo>
	<mi>f</mi>
	<mo>:</mo>
	<mo>=</mo>
	<mrow>
		<mo rspace="0.3em" lspace="0em" stretchy="true" fence="true" form="prefix">(</mo>
		<mtable class="m-pmatrix">
			<mtr>
				<mtd>
					<msub>
						<mi>f</mi>
						<mn>1</mn>
					</msub>
				</mtd>
			</mtr>
			<mtr>
				<mtd>
					<msub>
						<mi>f</mi>
						<mn>2</mn>
					</msub>
				</mtd>
			</mtr>
		</mtable>
		<mo rspace="0em" lspace="0.3em" stretchy="true" fence="true" form="postfix">)</mo>
	</mrow>
	<mo>:</mo>
	<mo>=</mo>
	<mrow>
		<mo rspace="0.3em" lspace="0em" stretchy="true" fence="true" form="prefix">(</mo>
		<mtable class="m-pmatrix">
			<mtr>
				<mtd>
					<msub>
						<mi>p</mi>
						<mn>1</mn>
					</msub>
					<mi>sin</mi>
					<msub>
						<mi>x</mi>
						<mn>1</mn>
					</msub>
					<mo>+</mo>
					<msub>
						<mi>x</mi>
						<mn>2</mn>
					</msub>
				</mtd>
			</mtr>
			<mtr>
				<mtd>
					<mi>ln</mi>
					<msub>
						<mi>p</mi>
						<mn>2</mn>
					</msub>
					<mo>+</mo>
					<msup>
						<mi>e</mi>
						<mrow>
							<msub>
								<mi>x</mi>
								<mn>2</mn>
							</msub>
						</mrow>
					</msup>
				</mtd>
			</mtr>
		</mtable>
		<mo rspace="0em" lspace="0.3em" stretchy="true" fence="true" form="postfix">)</mo>
	</mrow>
</mrow>
</math>
-->
<img src="src/main/docs/sample_formula.png" />
</center>

Introduce the dependency

```
com.github.drstefanfriedrich.f2blib:f2blib:${f2blibVersion}
```

with the correct version of F2BLib you want to use into your Gradle `build.gradle`.

Then define a function like in

```java
private static final String FUNCTION = "function some.packagename.SomeClassName;" +
                                       "begin" +
                                       "    f_1 := p_1 * sin(x_1) + x_2;" +
                                       "    f_2 := ln(p_2) + exp(x_2);" +
                                       "end";
```

and obtain a reference to the Function Evaluation Kernel and load the function into the kernel:

```java
FunctionEvaluationKernel kernel = new FunctionEvaluationFactory().get().create();
kernel.load(FUNCTION);
```

Now you can start evaluating the function:

```java
double[] x = new double[]{ 2.51, 1.28 };
double[] p = new double[]{ -1.45, 8.27 };
double[] y = new double[2];

kernel.eval("some.packagename.SomeClassName", p, x, y);
```

We refer to [IntegrationTest.java](src/test/java/com/github/drstefanfriedrich/f2blib/IntegrationTest.java).


## Architecture

The main components of the system interact as follows:

![Overview Architecture](src/main/docs/arch.jpg)


## Build

To build the project and start developing, do

```
$ git clone git@github.com/DrStefanFriedrich/f2blib
$ cd f2blib
$ ./gradlew
```

To run the performance tests, do

```
$ ./gradlew -Dcom.github.drstefanfriedrich.f2blib.performancetest.enabled=true
```


## Limitations

* Right now variables must be named x_i, where i is an integer and parameters must be named p_j, where j is
  an integer.
* The maximum number of parameters and variables is 128, respectively.


## References

### Parser Generators

Here is a list of parser generators. We used the list during evaluation of the different frameworks.

* [Antlr4](https://www.antlr.org/)
* [JavaCC](https://javacc.org/)
* [Xtext](https://www.eclipse.org/Xtext/)

We decided to use Antlr, because it is very well known and popular. In contrast, Xtext is much more powerful
and has a lot of features we don't need.


### Bytecode Generation

Here is a list of Java bytecode generation frameworks. We used the list during evaluation of the different frameworks.

* [ASM](https://asm.ow2.io/)
* [Commons BCEL](https://commons.apache.org/proper/commons-bcel/)
* [cglib](https://github.com/cglib/cglib)
* [ByteBuddy](https://bytebuddy.net/#/)

ByteBuddy as well as cglib extend ASM. ByteBuddy is optimized for runtime speed. ASM is very small,
very fast, has no dependencies to other libraries, and is a low-level bytecode generation system.
It also has a class called ASMifier which can transform arbitrary .class files to .java files
containing all the ASM statements needed to reproduce the class. For the last reason we chose ASM.


### Arithmetic Expression Evaluation

Here is a list of mathematical expression evaluation frameworks similar to this one:

* [exp4j](https://www.objecthunter.net/exp4j/)
* [mXparser](http://mathparser.org/)
* [EvalEx](https://github.com/uklimaschewski/EvalEx)
* [JFormula](http://www.japisoft.com/formula/)
* [Jep Java](http://www.singularsys.com/jep/)
* [expr](https://github.com/darius/expr)
* [java-expr-eval](https://github.com/gianluca-nitti/java-expr-eval)
* [ExprK](https://github.com/Keelar/ExprK)


### Finance Mathematics

* Volkert Paulsen: [Versicherungsmathematik](https://wwwmath.uni-muenster.de/statistik/paulsen/WeiterePublikationen/Versicherungsmathematik.pdf) (only available in German)
* Michael Koller: [Lebensversicherungsmathematik](https://www.actuaries.ch/de/downloads/aid!b4ae4834-66cd-464b-bd27-1497194efc96/id!101/Koller_LV_2013.pdf) (only available in German)
* Michael Koller: Stochastische Modelle in der Lebensversicherung, Springer, ISBN 978-3-642-11252-2
* Hartmut Milbrodt, Manfred Helbig: Mathematische Methoden der Personenversicherung, de Gruyter, 1999.


## License

The project is published under the [Eclipse Public License v2.0](LICENSE.txt), which is online available at
[this](https://www.eclipse.org/legal/epl-2.0/) URL.
 