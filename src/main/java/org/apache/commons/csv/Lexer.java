/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.csv;

import static org.apache.commons.io.IOUtils.EOF;
import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;

/**
 * Lexical analyzer.
 */
final class Lexer implements Closeable {

    private static final String CR_STRING = Character.toString(Constants.CR);

    private static final String LF_STRING = Character.toString(Constants.LF);

    private final char[] delimiter;

    private final char[] delimiterBuf;

    private final char[] escapeDelimiterBuf;

    private final int escape;

    private final int quoteChar;

    private final int commentStart;

    private final boolean ignoreSurroundingSpaces;

    private final boolean ignoreEmptyLines;

    private final boolean lenientEof;

    private final boolean trailingData;

    /**
     * The buffered reader.
     */
    private final ExtendedBufferedReader reader;

    private String firstEol;

    private boolean isLastTokenDelimiter;

    Lexer(final CSVFormat format, final ExtendedBufferedReader reader) {
        this.reader = reader;
        this.delimiter = format.getDelimiterCharArray();
        this.escape = nullToDisabled(format.getEscapeCharacter());
        this.quoteChar = nullToDisabled(format.getQuoteCharacter());
        this.commentStart = nullToDisabled(format.getCommentMarker());
        this.ignoreSurroundingSpaces = format.getIgnoreSurroundingSpaces();
        this.ignoreEmptyLines = format.getIgnoreEmptyLines();
        this.lenientEof = format.getLenientEof();
        this.trailingData = format.getTrailingData();
        this.delimiterBuf = new char[delimiter.length - 1];
        this.escapeDelimiterBuf = new char[2 * delimiter.length - 1];
    }

    /**
     * Appends the next escaped character to the token's content.
     *
     * @param token the current token.
     * @throws IOException  on stream access error.
     * @throws CSVException Thrown on invalid input.
     */
    private void appendNextEscapedCharacterToToken(final Token token) throws IOException {
        if (isEscapeDelimiter()) {
            token.content.append(delimiter);
        } else {
            final int unescaped = readEscape();
            if (unescaped == EOF) {
                // unexpected char after escape
                token.content.append((char) escape).append((char) reader.getLastChar());
            } else {
                token.content.append((char) unescaped);
            }
        }
    }

    /**
     * Closes resources.
     *
     * @throws IOException
     *             If an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Gets the number of bytes read.
     *
     * @return the number of bytes read.
     */
    long getBytesRead() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Gets the current character position.
     *
     * @return the current character position.
     */
    long getCharacterPosition() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Gets the current line number.
     *
     * @return the current line number.
     */
    long getCurrentLineNumber() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    String getFirstEol() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    boolean isClosed() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    boolean isCommentStart(final int ch) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Tests whether the next characters constitute a delimiter through {@link ExtendedBufferedReader#peek(char[])}.
     *
     * @param ch
     *             the current character.
     * @return true if the next characters constitute a delimiter.
     * @throws IOException If an I/O error occurs.
     */
    boolean isDelimiter(final int ch) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Tests if the given character indicates the end of the file.
     *
     * @return true if the given character indicates the end of the file.
     */
    boolean isEndOfFile(final int ch) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Tests if the given character is the escape character.
     *
     * @return true if the given character is the escape character.
     */
    boolean isEscape(final int ch) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Tests if the next characters constitute a escape delimiter through {@link ExtendedBufferedReader#peek(char[])}.
     *
     * For example, for delimiter "[|]" and escape '!', return true if the next characters constitute "![!|!]".
     *
     * @return true if the next characters constitute an escape delimiter.
     * @throws IOException If an I/O error occurs.
     */
    boolean isEscapeDelimiter() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private boolean isMetaChar(final int ch) {
        return ch == escape || ch == quoteChar || ch == commentStart;
    }

    boolean isQuoteChar(final int ch) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Tests if the current character represents the start of a line: a CR, LF, or is at the start of the file.
     *
     * @param ch the character to check.
     * @return true if the character is at the start of a line.
     */
    boolean isStartOfLine(final int ch) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns the next token.
     * <p>
     * A token corresponds to a term, a record change or an end-of-file indicator.
     * </p>
     *
     * @param token an existing Token object to reuse. The caller is responsible for initializing the Token.
     * @return the next token found.
     * @throws IOException  on stream access error.
     * @throws CSVException Thrown on invalid input.
     */
    Token nextToken(final Token token) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private int nullToDisabled(final Character c) {
        // Explicit unboxing
        return c == null ? Constants.UNDEFINED : c.charValue();
    }

    /**
     * Parses an encapsulated token.
     * <p>
     * Encapsulated tokens are surrounded by the given encapsulating string. The encapsulator itself might be included
     * in the token using a doubling syntax (as "", '') or using escaping (as in \", \'). Whitespaces before and after
     * an encapsulated token is ignored. The token is finished when one of the following conditions becomes true:
     * </p>
     * <ul>
     * <li>An unescaped encapsulator has been reached and is followed by optional whitespace then:</li>
     * <ul>
     * <li>delimiter (TOKEN)</li>
     * <li>end of line (EORECORD)</li>
     * </ul>
     * <li>end of stream has been reached (EOF)</li> </ul>
     *
     * @param token
     *            the current token
     * @return a valid token object
     * @throws IOException
     *             Thrown when in an invalid state: EOF before closing encapsulator or invalid character before
     *             delimiter or EOL.
     * @throws CSVException Thrown on invalid input.
     */
    private Token parseEncapsulatedToken(final Token token) throws IOException {
        token.isQuoted = true;
        // Save current line number in case needed for IOE
        final long startLineNumber = getCurrentLineNumber();
        int c;
        while (true) {
            c = reader.read();
            if (isQuoteChar(c)) {
                if (isQuoteChar(reader.peek())) {
                    // double or escaped encapsulator -> add single encapsulator to token
                    c = reader.read();
                    token.content.append((char) c);
                } else {
                    // token finish mark (encapsulator) reached: ignore whitespace till delimiter
                    while (true) {
                        c = reader.read();
                        if (isDelimiter(c)) {
                            token.type = Token.Type.TOKEN;
                            return token;
                        }
                        if (isEndOfFile(c)) {
                            token.type = Token.Type.EOF;
                            // There is data at EOF
                            token.isReady = true;
                            return token;
                        }
                        if (readEndOfLine(c)) {
                            token.type = Token.Type.EORECORD;
                            return token;
                        }
                        if (trailingData) {
                            token.content.append((char) c);
                        } else if (!Character.isWhitespace((char) c)) {
                            // error invalid char between token and next delimiter
                            throw new CSVException("Invalid character between encapsulated token and delimiter at line: %,d, position: %,d", getCurrentLineNumber(), getCharacterPosition());
                        }
                    }
                }
            } else if (isEscape(c)) {
                appendNextEscapedCharacterToToken(token);
            } else if (isEndOfFile(c)) {
                if (lenientEof) {
                    token.type = Token.Type.EOF;
                    // There is data at EOF
                    token.isReady = true;
                    return token;
                }
                // error condition (end of file before end of token)
                throw new CSVException("(startline %,d) EOF reached before encapsulated token finished", startLineNumber);
            } else {
                // consume character
                token.content.append((char) c);
            }
        }
    }

    /**
     * Parses a simple token.
     * <p>
     * Simple tokens are tokens that are not surrounded by encapsulators. A simple token might contain escaped delimiters (as \, or \;). The token is finished
     * when one of the following conditions becomes true:
     * </p>
     * <ul>
     * <li>The end of line has been reached (EORECORD)</li>
     * <li>The end of stream has been reached (EOF)</li>
     * <li>An unescaped delimiter has been reached (TOKEN)</li>
     * </ul>
     *
     * @param token the current token.
     * @param ch     the current character.
     * @return the filled token.
     * @throws IOException  on stream access error.
     * @throws CSVException Thrown on invalid input.
     */
    private Token parseSimpleToken(final Token token, final int ch) throws IOException {
        // Faster to use while(true)+break than while(token.type == INVALID)
        int cur = ch;
        while (true) {
            if (readEndOfLine(cur)) {
                token.type = Token.Type.EORECORD;
                break;
            }
            if (isEndOfFile(cur)) {
                token.type = Token.Type.EOF;
                // There is data at EOF
                token.isReady = true;
                break;
            }
            if (isDelimiter(cur)) {
                token.type = Token.Type.TOKEN;
                break;
            }
            // continue
            if (isEscape(cur)) {
                appendNextEscapedCharacterToToken(token);
            } else {
                token.content.append((char) cur);
            }
            // continue
            cur = reader.read();
        }
        if (ignoreSurroundingSpaces) {
            trimTrailingSpaces(token.content);
        }
        return token;
    }

    /**
     * Greedily accepts \n, \r and \r\n This checker consumes silently the second control-character...
     *
     * @return true if the given or next character is a line-terminator.
     */
    boolean readEndOfLine(final int ch) throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    // TODO escape handling needs more work
    /**
     * Handle an escape sequence. The current character must be the escape character. On return, the next character is available by calling
     * {@link ExtendedBufferedReader#getLastChar()} on the input stream.
     *
     * @return the unescaped character (as an int) or {@link IOUtils#EOF} if char following the escape is invalid.
     * @throws IOException  if there is a problem reading the stream or the end of stream is detected: the escape character is not allowed at end of stream
     * @throws CSVException Thrown on invalid input.
     */
    int readEscape() throws IOException {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    void trimTrailingSpaces(final StringBuilder buffer) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
