package org.natty;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.antlr.runtime.tree.Tree;

/**
 * @author Joe Stelmach
 */
public class DateGroup {
  private final List<Temporal> _dates;
  private String _text;
  private String _fullText;
  private int _line;
  private int _position;
  private boolean _isRecurring;
  private boolean _isDateInferred;
  private boolean _isTimeInferred;
  private Date _recurringUntil;
  private Map<String, List<ParseLocation>> _parseLocations;
  private Tree _syntaxTree;

  public DateGroup() {
    _dates = new ArrayList<>();
    _isDateInferred = true;
    _isTimeInferred = true;
  }


  /**
   * Adds a date to this group
   * @param date the date
   */
  public void addDate(Date date) {
    _dates.add(date.toInstant());
  }

  public void addZonedDateTime(ZonedDateTime date) {
    _dates.add(date);
  }

  /**
   * @return The absolute position of this date group within fullText
   */
  public int getAbsolutePosition() {
    int lineCount = 1;
    int columnCount = 1;
    for(int i=0; i<_fullText.length(); i++) {

      // return current position if we've arrived at the requested character
      if(lineCount == _line && columnCount == _position) {
        return i;
      }

      if(_fullText.charAt(i) == '\n') {
        lineCount++;
        columnCount = 1;
      }
      else {
        columnCount++;
      }
    }
    return -1;
  }

  /**
   * @return The list of dates in this group
   * @deprecated
   */
  @Deprecated
  public List<Date> getDates() {
    return Collections.unmodifiableList(_dates.stream().
      map(i -> Date.from(Instant.from(i)))
      .collect(Collectors.toList()));
  }

  /**
   *
   * @since 1.2
   */
  public List<Instant> getInstants() {
    return Collections.unmodifiableList(_dates.stream().
      map(Instant::from)
      .collect(Collectors.toList()));
  }

  /**
   *
   * @since 1.2
   */
  public List<Temporal> getTemporals() {
    return Collections.unmodifiableList(_dates);
  }


  /**
   *
   * @since 1.2
   */
  public Temporal get(int i) {
    return _dates.get(i);
  }

  public boolean isEmpty() {
    return _dates.isEmpty();
  }


  /**
   *
   * @since 1.2
   */
  void set(int i, Temporal date) {
    _dates.set(i, date);
  }

  /**
   * @return The fullText this date group was found within
   */
  public String getFullText() {
    return _fullText;
  }

  /**
   * The line within the fullText this date group is found on
   * @return line number
   */
  public int getLine() {
    return _line;
  }

  /**
   * @return A map of all matching parse rules to their locations for this group
   */
  public Map<String, List<ParseLocation>> getParseLocations() {
    return _parseLocations;
  }

  /**
   * @return The character position of the matching text within the line of fullText
   */
  public int getPosition() {
    return _position;
  }

  /**
   * @param length The maximum number of characters to include
   * @return The immediate prefix within fullText to this group's text within the fullText
   */
  public String getPrefix(int length) {
    return _fullText.substring(Math.max(0, getAbsolutePosition() - length), getAbsolutePosition());
  }

  /**
   * @return the end date of any recurrence found
   */
  public Date getRecursUntil() {
    return _recurringUntil;
  }

  /**
   * @param length The maximum number of characters to include
   * @return The immediate suffix within fullText to this group's text within the fullText
   */
  public String getSuffix(int length) {
    int endPosition = getAbsolutePosition() + _text.length() + 1;
    return _fullText.substring(
        Math.min(endPosition - 1, _fullText.length()),
        Math.min(endPosition + length - 1, _fullText.length()));
  }

  /**
   * @return The abstract syntax tree built for this group
   */
  public Tree getSyntaxTree() {
    return _syntaxTree;
  }

  /**
   * @return The text within fullText this group represents
   */
  public String getText() {
    return _text;
  }

  /**
   * @return true if the date information in this group has been inferred,
   * as opposed to being explicitly defined in the input
   */
  public boolean isDateInferred() {
    return _isDateInferred;
  }

  /**
   * @return true if some recurrence information was found
   */
  public boolean isRecurring() {
    return _isRecurring;
  }

  /**
   * @return true if the time information in this group has been inferred,
   * as opposed to being explicitly defined in the input
   */
  public boolean isTimeInferred() {
    return _isTimeInferred;
  }

  public void setDateInferred(boolean isDateInferred) {
    this._isDateInferred = isDateInferred;
  }

  public void setFullText(String fullText) {
    this._fullText = fullText;
  }

  public void setIsTimeInferred(boolean isTimeInferred) {
    this._isTimeInferred = isTimeInferred;
  }

  public void setLine(int line) {
    _line = line;
  }

  public void setParseLocations(Map<String, List<ParseLocation>> parseLocations) {
    _parseLocations = parseLocations;
  }

  public void setPosition(int position) {
    _position = position;
  }

  public void setRecurring(boolean isRecurring) {
    _isRecurring = isRecurring;
  }

  public void setRecurringUntil(Date recurringUntil) {
    _recurringUntil = recurringUntil;
  }

  public void setSyntaxTree(Tree syntaxTree) {
    _syntaxTree = syntaxTree;
  }

  public void setText(String text) {
    _text = text;
  }

}
