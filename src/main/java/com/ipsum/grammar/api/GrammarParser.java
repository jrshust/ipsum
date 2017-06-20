package com.ipsum.grammar.api;

import java.util.Set;

public interface GrammarParser {

	Set<String> getUndefinedVariables(String code);

	Set<String> getDefinedVariables(String code);

}
