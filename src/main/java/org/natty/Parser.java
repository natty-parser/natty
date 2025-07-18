package org.natty;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.antlr.runtime.tree.Tree;
import org.natty.generated.DateLexer;
import org.natty.generated.DateParser;
import org.natty.generated.DateWalker;
import org.natty.generated.TreeRewrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Joe Stelmach
 */
public class Parser implements Serializable {

  private static final long serialVersionUID = 233282586086252203L;
  private static final Logger _logger = LoggerFactory.getLogger(Parser.class);

  private final TimeZone _defaultTimeZone;



  /**
   * Tokens that should be removed from the end any list of tokens before parsing. These are
   * valid tokens, but could never add any meaningful parsing information when located at the
   * end of a token stream.
   */
  private static final Set<Integer> IGNORED_TRAILING_TOKENS =
      new HashSet<Integer>(Arrays.asList(new Integer[] {
          DateLexer.DOT,
          DateLexer.COLON,
          DateLexer.COMMA,
          DateLexer.DASH,
          DateLexer.SLASH,
          DateLexer.PLUS,
          DateLexer.SINGLE_QUOTE
      }));

  /**
   * Creates a new parser using the given time zone as the default
   * @param defaultTimeZone the timezone
   */
  public Parser(TimeZone defaultTimeZone) {
    _defaultTimeZone = defaultTimeZone;
  }

  /**
   * Creates a new parser with no explicit default time zone (default will be US/Eastern)
   */
  public Parser() {
    _defaultTimeZone = TimeZone.getDefault();
  }

  /**
   * Parses the given input value for one or more groups of date alternatives
   *
   * @param value the date to parse
   * @return list of date alternatives
   */
  public List<DateGroup> parse(String value) {
    return parse(value, new Date());
  }

  /**
   * Parses the given input value for one or more groups of
   * date alternatives with relative dates resolved according
   * to referenceDate
   *
   * @param value the date string to parse
   * @param referenceDate the reference date for relative dates
   * @return list of date alternatives
   */
  public List<DateGroup> parse(String value, Date referenceDate) {

    // lex the input value to obtain our global token stream
    ANTLRInputStream input = null;
    try {
      input = new ANTLRNoCaseInputStream(new ByteArrayInputStream(value.getBytes()));

    } catch (IOException e) {
      _logger.error("could not lex input", e);
    }
    DateLexer lexer = new DateLexer(input);

    // collect all sub-token streams that may include date information
    List<TokenStream> streams = collectTokenStreams(new CommonTokenStream(lexer));

    // and parse each of them
    List<DateGroup> groups = new ArrayList<DateGroup>();
    TokenStream lastStream = null;
    for(TokenStream stream:streams) {
      lastStream = stream;
      List<Token> tokens = ((NattyTokenSource) stream.getTokenSource()).getTokens();
      DateGroup group = singleParse(stream, value, referenceDate);
      while((group == null || group.getDates().size() == 0) && tokens.size() > 0) {
        if(group == null || group.getDates().size() == 0) {

          // we have two options:
          // 1. Continuously remove tokens from the end of the stream and re-parse.  This will
          //    recover from the case of an extraneous token at the end of the token stream.
          //    For example: 'june 20th on'
          List<Token> endRemovedTokens = new ArrayList<Token>(tokens);
          while((group == null || group.getDates().isEmpty()) && !endRemovedTokens.isEmpty()) {
            endRemovedTokens = endRemovedTokens.subList(0, endRemovedTokens.size() - 1);
            TokenStream newStream = new CommonTokenStream(new NattyTokenSource(endRemovedTokens));
            group = singleParse(newStream, value, referenceDate);
            lastStream = newStream;
          }

          // 2. Continuously look for another possible starting point in the token
          //    stream and re-parse.
          while((group == null || group.getDates().isEmpty()) && tokens.size() >= 1) {
            tokens = tokens.subList(1, tokens.size());
            Iterator<Token> iter = tokens.iterator();
            while(iter.hasNext()) {
              Token token = iter.next();
              if(!DateParser.FOLLOW_empty_in_parse186.member(token.getType())) {
                iter.remove();
              }
              else {
                break;
              }
            }
            TokenStream newStream = new CommonTokenStream(new NattyTokenSource(tokens));
            group = singleParse(newStream, value, referenceDate);
            lastStream = newStream;
          }
        }
      }

      // If a group with at least one date was found, we'll most likely want to add it to our list,
      // but not if multiple streams were found and the group contains only numeric time information.
      // For example: A full text string of '1' should parse to 1 o'clock, but 'I need 1 hard drive'
      // should result in no groups found.
      if(group != null && !group.getDates().isEmpty() &&
          (streams.size() == 1 || !group.isDateInferred() || !isAllNumeric(lastStream))) {

        // Additionally, we'll only accept this group if the associated text does not have an
        // alphabetic character to the immediate left or right, which would indicate a portion
        // of a word was tokenized. For example, 'nightingale' will result in a 'NIGHT' token,
        // but there's clearly no datetime information there.
        group.setFullText(value);
        String prefix = group.getPrefix(1);
        String suffix = group.getSuffix(1);
        if((prefix.isEmpty() || !Character.isLetter(prefix.charAt(0))) &&
           (suffix.isEmpty() || !Character.isLetter(suffix.charAt(0)))) {

          groups.add(group);
        }
      }
    }
    return groups;
  }

  /**
   * Determines if a token stream contains only numeric tokens
   * @param stream
   * @return true if all tokens in the given stream can be parsed as an integer
   */
  private boolean isAllNumeric(TokenStream stream) {
    List<Token> tokens = ((NattyTokenSource) stream.getTokenSource()).getTokens();
    for(Token token:tokens) {
      try {
        Integer.parseInt(token.getText());
      } catch(NumberFormatException e) {
        return false;
      }
    }
    return true;
  }

  /**
   * Parses the token stream for a SINGLE date time alternative.  This
   * method assumes that the entire token stream represents date and or
   * time information (no extraneous tokens)
   *
   * @param stream
   * @return
   */
  private DateGroup singleParse(TokenStream stream, String fullText, Date referenceDate) {
	DateGroup group = null;
	List<Token> tokens = ((NattyTokenSource) stream.getTokenSource()).getTokens();
	if(tokens.isEmpty()) return group;

    StringBuilder tokenString = new StringBuilder();
    for(Token token:tokens) {
      tokenString.append(DateParser.tokenNames[token.getType()]);
      tokenString.append(" ");
    }

    try {
      // parse
      ParseListener listener = new ParseListener();
      DateParser parser = new DateParser(stream, listener);
      DateParser.parse_return parseReturn = parser.parse();

      Tree tree = (Tree) parseReturn.getTree();

      // we only continue if a meaningful syntax tree has been built
      if(tree.getChildCount() > 0) {
        _logger.debug("PARSE: {}", tokenString);

        // rewrite the tree (temporary fix for http://www.antlr.org/jira/browse/ANTLR-427)
        CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
        TreeRewrite s = new TreeRewrite(nodes);
        tree = (CommonTree)s.downup(tree);

        // and walk it
        nodes = new CommonTreeNodeStream(tree);
        nodes.setTokenStream(stream);
        DateWalker walker = new DateWalker(nodes);
        walker.setReferenceDate(referenceDate);
        walker.getState().setDefaultTimeZone(_defaultTimeZone);
        walker.parse();
        _logger.debug("AST: {}", tree.toStringTree());

        // run through the results and append the parse information
        group = walker.getState().getDateGroup();
        ParseLocation location = listener.getDateGroupLocation();
        group.setLine(location.getLine());
        group.setText(location.getText());
        group.setPosition(location.getStart());
        group.setSyntaxTree(tree);
        group.setParseLocations(listener.getLocations());
        group.setFullText(fullText);

        // if the group's matching text has an immediate alphabetic prefix or suffix,
        // we ignore this result
        String prefix = group.getPrefix(1);
        String suffix = group.getSuffix(1);
        if((!prefix.isEmpty() && Character.isLetter(prefix.charAt(0))) ||
            (!suffix.isEmpty() && Character.isLetter(suffix.charAt(0)))) {

          group = null;
        }

      }

    } catch(RecognitionException e) {
      _logger.debug("Could not parse input", e);
    }

    return group;
  }

  /**
   * Scans the given token global token stream for a list of sub-token
   * streams representing those portions of the global stream that
   * may contain date time information
   *
   * @param stream
   * @return
   */
  private List<TokenStream> collectTokenStreams(TokenStream stream) {

    // walk through the token stream and build a collection
    // of sub token streams that represent possible date locations
    List<Token> currentGroup = null;
    List<List<Token>> groups = new ArrayList<List<Token>>();
    Token currentToken;
    int currentTokenType;
    StringBuilder tokenString = new StringBuilder();
    while((currentToken = stream.getTokenSource().nextToken()).getType() != DateLexer.EOF) {
      currentTokenType = currentToken.getType();
      tokenString.append(DateParser.tokenNames[currentTokenType]).append(" ");

      // we're currently NOT collecting for a possible date group
      if(currentGroup == null) {
        // skip over white space and known tokens that cannot be the start of a date
        if(currentTokenType != DateLexer.WHITE_SPACE &&
            DateParser.FOLLOW_empty_in_parse186.member(currentTokenType)) {

          currentGroup = new ArrayList<Token>();
          currentGroup.add(currentToken);
        }
      }

      // we're currently collecting
      else {
        // preserve white space
        if(currentTokenType == DateLexer.WHITE_SPACE) {
          currentGroup.add(currentToken);
        }

        else {
          // if this is an unknown token, we'll close out the current group
          if(currentTokenType == DateLexer.UNKNOWN) {
            addGroup(currentGroup, groups);
            currentGroup = null;
          }
          // otherwise, the token is known and we're currently collecting for
          // a group, so we'll add it to the current group
          else {
            currentGroup.add(currentToken);
          }
        }
      }
    }

    if(currentGroup != null) {
      addGroup(currentGroup, groups);
    }

    _logger.debug("STREAM: {}", tokenString);
    List<TokenStream> streams = new ArrayList<TokenStream>();
    for(List<Token> group:groups) {
      if(!group.isEmpty()) {
        StringBuilder builder = new StringBuilder();
        builder.append("GROUP: ");
        for (Token token : group) {
          builder.append(DateParser.tokenNames[token.getType()]).append(" ");
        }
        _logger.debug(builder.toString());

        streams.add(new CommonTokenStream(new NattyTokenSource(group)));
      }
    }

    return streams;
  }

  /**
   * Cleans up the given group and adds it to the list of groups if still valid
   * @param group
   * @param groups
   */
  private void addGroup(List<Token> group, List<List<Token>> groups) {

    if(group.isEmpty()) return;

    // remove trailing tokens that should be ignored
    while(!group.isEmpty() && IGNORED_TRAILING_TOKENS.contains(
        group.get(group.size() - 1).getType())) {
      group.remove(group.size() - 1);
    }

    // if the group still has some tokens left, we'll add it to our list of groups
    if(!group.isEmpty()) {
      groups.add(group);
    }
  }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof Parser)) return false;

    Parser parser = (Parser) o;
    return Objects.equals(_defaultTimeZone, parser._defaultTimeZone);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(_defaultTimeZone);
  }
}
