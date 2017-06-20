package com.ipsum.grammar.r;

import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import com.google.common.collect.Sets;
import com.ipsum.grammar.api.GrammarParser;
import com.jrshust.grammar.RFilter;
import com.jrshust.grammar.RLexer;
import com.jrshust.grammar.RParser;

/**
 * Legitimately hott parser for the R grammar.
 */
public final class RGrammarParser implements GrammarParser {

    private ParseTreeWalker walker;

    public RGrammarParser() {
        this.walker = new ParseTreeWalker();
    }

    public Set<String> getUndefinedVariables(String code) {
        ANTLRInputStream input = new ANTLRInputStream(code);
        RLexer lexer = new RLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        RFilter filter = new RFilter(tokens);
        filter.stream();
        tokens.reset();

        RParser parser = new RParser(tokens);
        parser.setBuildParseTree(true);
        RuleContext tree = parser.prog();

        RGrammarListener listener = new RGrammarListener();

        walker.walk(listener, tree);

        Set<Token> undefined = listener.getUndefinedSymbols();
        Set<String> undefinedIds = Sets.newHashSet();

        for (Token token : undefined) {
            undefinedIds.add(token.getText());
        }

        return undefinedIds;
    }

    public Set<String> getDefinedVariables(String code) {
        ANTLRInputStream input = new ANTLRInputStream(code);
        RLexer lexer = new RLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        RFilter filter = new RFilter(tokens);
        filter.stream();
        tokens.reset();

        RParser parser = new RParser(tokens);
        parser.setBuildParseTree(true);
        RuleContext tree = parser.prog();

        RGrammarListener listener = new RGrammarListener();

        walker.walk(listener, tree);

        return listener.getDefinedSymbols();
    }

}
