package org.natty.grammar;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ParserRuleReturnScope;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.Tree;
import org.natty.ANTLRNoCaseInputStream;
import org.natty.ParseListener;
import org.natty.generated.DateLexer;
import org.natty.generated.DateParser;
import org.natty.generated.TreeRewrite;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractGrammarTest {
    protected String ruleName;

    protected void assertAST(String input, String ast) throws Exception {
        assertThat(buildAST(input)).isEqualTo(ast);
    }

    protected String buildAST(String value) throws Exception {
        DateParser parser = buildParser(value);
        Class<?> klass = Class.forName("org.natty.generated.DateParser");
        Method meth = klass.getMethod(ruleName, (Class<?>[]) null);
        ParserRuleReturnScope ret = (ParserRuleReturnScope) meth.invoke(parser, (Object[]) null);

        Tree tree = (Tree) ret.getTree();
        // rewrite the tree (temporary fix for http://www.antlr.org/jira/browse/ANTLR-427)
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
        TreeRewrite s = new TreeRewrite(nodes);
        tree = (CommonTree) s.downup(tree);

        return tree.toStringTree();
    }

    private DateParser buildParser(String value) throws Exception {
        // lex
        ANTLRInputStream input = new ANTLRNoCaseInputStream(new ByteArrayInputStream(value.getBytes()));
        DateLexer lexer = new DateLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // parse
        ParseListener listener = new ParseListener();
        return new DateParser(tokens, listener);
    }
}
