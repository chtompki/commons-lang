/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3;

/**
 * Operations on {@link java.lang.String} that are known to contain Unicode
 * code points, or surrogate pairs. Note that the javadoc for {@link java.lang.Character} contains
 * a brief preface on 
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Character.html#unicode">
 *   Unicode Character Representations
 * </a>. Our motivation for splitting these operations of from
 * {@link StringUtils} result from the fact that these operations can considerably
 * impact the runtime of methods such as {@link StringUtils#substring(String, int, int)}. So we have
 * also included a predicate whose runtime is {@code O(n)} runtime on a string of length
 * {@code n}, {@link UnicodeStringUtils#containsSurrogatePair(String)}, whose job it is to determine
 * if one should utilize this class over the standard methods in {@link StringUtils}.
 *
 * <p>More specifically, the set of characters from U+0000 to U+FFFF is
 * sometimes referred to as the <em>Basic Multilingual Plane (BMP)</em>.
 * Characters whose code points are greater than U+FFFF are called
 * <em>supplementary character</em>s.  The Java platform uses the
 * UTF-16 representation in {@code char} arrays and in the
 * {@code String} and {@code StringBuffer} classes. In this
 * representation, supplementary characters are represented as a
 * <em>surrogate</em> pair of {@code char} values, the first from
 * the <em>high-surrogates</em> range, (&#92;uD800-&#92;uDBFF), the
 * second from the <em>low-surrogates</em> range (&#92;uDC00-&#92;uDFFF).
 *
 * To determine if an individual character is either a <em>high-surrogate</em> or
 * a <em>low-surrogate</em> respectively, we suggest that the user rely upon the
 * {@link java.lang.Character} api. Specifically we suggest the user
 * rely upon {@link java.lang.Character#isHighSurrogate(char)} and
 * {@link java.lang.Character#isLowSurrogate(char)} respectively.
 *
 * @since 3.10
 */
public class UnicodeStringUtils {

    /**
     * <p>{@code UnicodeStringUtils} instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * {@code UnicodeStringUtils.containsSurrogatePair(" foo ");}.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public UnicodeStringUtils() {
        super();
    }

    /**
     * This method iterates over the individual characters of a string, which
     * if we retrieve via {@link String#toCharArray()} yields an array with the surrogate-pairs
     * individually listed out, and determines if any of the characters comprise a surrogate pair.
     *
     * @param str the {@link java.lang.String} that we wish to check for a surrogate pair.
     * @return {@code true} if we find such a surrogate pair and {@code false} otherwise.
     *         <em>Note</em>, we stop iterating over the at the first found surrogate pair
     *         as to maximize performance.
     */
    public static boolean containsSurrogatePair(String str) {
        if (str == null) {
            return Boolean.FALSE;
        }
        Character lastCharacter = null;
        Character currentCharacter;
        for (char c: str.toCharArray()) {
            currentCharacter = c;
            if (lastCharacter != null
                && Character.isSurrogatePair(lastCharacter, currentCharacter)) {
                return Boolean.TRUE;
            }
            lastCharacter = c;
        }
        return Boolean.FALSE;
    }

    /**
     * <p>Gets a substring from the specified String avoiding exceptions.</p>
     *
     * <p>A negative start position can be used to start/end {@code n}
     * characters from the end of the String.</p>
     *
     * <p>The returned substring starts with the character in the {@code start}
     * position and ends before the {@code end} position. All position counting is
     * zero-based -- i.e., to start at the beginning of the string use
     * {@code start = 0}. Negative start and end positions can be used to
     * specify offsets relative to the end of the String.</p>
     *
     * <p>If {@code start} is not strictly to the left of {@code end}, ""
     * is returned.</p>
     *
     * <pre>
     * UnicodeStringUtils.substring(null, *, *)    = null
     * UnicodeStringUtils.substring("", * ,  *)    = "";
     * UnicodeStringUtils.substring("&#92;uD83D&#92;uDC66&#92;uD83D&#92;uDC69&#92;uD83D&#92;uDC6A&#92;uD83D&#92;uDC6B", 0, 2) = "&#92;uD83D&#92;uDC66&#92;uD83D&#92;uDC69"
     * UnicodeStringUtils.substring(
     *     "&#92;uD83D&#92;uDC66&#92;uD83D&#92;uDC69&#92;uD83D&#92;uDC6A&#92;uD83D&#92;uDC6B"
     *     , 2
     *     , 0) =  ""
     * UnicodeStringUtils.substring(
     *     "&#92;uD83D&#92;uDC66&#92;uD83D&#92;uDC69&#92;uD83D&#92;uDC6A&#92;uD83D&#92;uDC6B"
     *     , 2
     *     , 4) = "&#92;uD83D&#92;uDC6A&#92;uD83D&#92;uDC6B"
     * UnicodeStringUtils.substring(
     *     "&#92;uD83D&#92;uDC66&#92;uD83D&#92;uDC69&#92;uD83D&#92;uDC6A&#92;uD83D&#92;uDC6B"
     *     , 2
     *     , 2) = ""
     * UnicodeStringUtils.substring(
     *     "&#92;uD83D&#92;uDC66&#92;uD83D&#92;uDC69&#92;uD83D&#92;uDC6A&#92;uD83D&#92;uDC6B"
     *     , -4
     *     , 2)  = ""
     * </pre>
     *
     * @param str  the String to get the substring from, may be null
     * @param start  the position to start from, negative means
     *  count back from the end of the String by this many characters
     * @param end  the position to end at (exclusive), negative means
     *  count back from the end of the String by this many characters
     * @return substring from start position to end position,
     *  {@code null} if null String input
     */
    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return null;
        }

        // handle negatives
        if (end < 0) {
            end = str.length() + end; // remember end is negative
        }
        if (start < 0) {
            start = str.length() + start; // remember start is negative
        }

        // check length next
        if (end > str.length()) {
            end = str.length();
        }

        // if start is greater than end, return ""
        if (start > end) {
            return StringUtils.EMPTY;
        }

        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        int realUtf16Start = str.offsetByCodePoints(0, start);
        int realUtf16End = str.offsetByCodePoints(0, end);
        return str.substring(realUtf16Start, realUtf16End);
    }
}
