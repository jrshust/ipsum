package com.ipsum.grammar.r;

import java.util.Set;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.google.common.collect.Sets;
import com.jrshust.grammar.RBaseListener;
import com.jrshust.grammar.RParser;
import com.jrshust.grammar.RParser.ExprContext;
import com.jrshust.grammar.RParser.FormContext;
import com.jrshust.grammar.RParser.SubContext;

public final class RGrammarListener extends RBaseListener {

    private Set<String> definedSymbols;
    private Set<Token> undefinedSymbols;

    private Set<String> scopeDefinitions;
    private Set<String> ignoreTokens;

    public RGrammarListener() {
        this.definedSymbols = Sets.newHashSet();
        this.undefinedSymbols = Sets.newHashSet();

        this.scopeDefinitions = Sets.newHashSet();
        this.ignoreTokens = Sets.newHashSet();
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        super.visitTerminal(node);

        // System.out.println("TERMINAL " + node.getText());
        // System.out.println(node.getSymbol().getType());

        if (node.getSymbol().getType() == RParser.ID) {
            if (!definedSymbols.contains(node.getSymbol().getText())
                    && !scopeDefinitions.contains(node.getSymbol().getText())
                    && !ignoreTokens.contains(node.getSymbol().getText())) {
                if (!node.getSymbol().getText().equals("return")) {
                    undefinedSymbols.add(node.getSymbol());
                }
            }
        }
    }

    @Override
    public void enterForm(FormContext ctx) {
        super.enterForm(ctx);

        // System.out.println("FORM: " + ctx.getText());

        Token formToken = ctx.getStart();
        if (formToken.getType() == RParser.ID) {
            this.definedSymbols.add(formToken.getText());
        }
    }

    @Override
    public void enterExpr(ExprContext ctx) {
        super.enterExpr(ctx);

        // System.out.println("EXPR: " + ctx.getText());

        TerminalNode node = ctx.getToken(RParser.LEFT_ASSIGNMENT, 0);

        TerminalNode dollarNode = ctx.getToken(RParser.DOLLAR_SIGN, 0);

        if (node != null) {
            ExprContext lol = ctx.expr().get(0);
            definedSymbols.add(lol.getText());
        }

        if (dollarNode != null) {
            ignoreTokens.add(ctx.expr().get(1).getText());
        }
    }

    @Override
    public void exitExpr(ExprContext ctx) {
        super.exitExpr(ctx);

        TerminalNode dollarNode = ctx.getToken(RParser.DOLLAR_SIGN, 0);
        if (dollarNode != null) {
            ignoreTokens.remove(ctx.expr().get(1).getText());
        }
    }

    @Override
    public void enterSub(SubContext ctx) {
        super.enterSub(ctx);

        // In the grammar if an ID exists on a Sub rule, it must be assignment
        TerminalNode node = ctx.ID();

        if (node != null) {
            // System.out.println("DEFINING " + node.getText() + " FOR SCOPE");
            scopeDefinitions.add(node.getText());
        }
    }

    @Override
    public void exitSub(SubContext ctx) {
        super.exitSub(ctx);

        TerminalNode node = ctx.ID();

        if (node != null) {
            // System.out.println("REMOVING SCOPE VAR: " + node.getText());
            scopeDefinitions.remove(node.getText());
        }
    }

    public Set<Token> getUndefinedSymbols() {
        return undefinedSymbols;
    }

    public Set<String> getDefinedSymbols() {
        return definedSymbols;
    }

}
