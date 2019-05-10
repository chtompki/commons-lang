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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.UnicodeStringUtils}.
 */
public class UnicodeStringUtilsTest {

    private static final String STRING_WITH_NO_SURROGATE_PAIRS = "abcd\uD799\uD123";
    private static final String STRING_WITH_ONE_SURROGATE_PAIR = "ab\uD83D\uDE42d";
    private static final String EMOJI_BOY_WOMAN_FAMILY_WOMAN_AND_MAN_HOLDING_HANDS_STRING
        = "\uD83D\uDC66\uD83D\uDC69\uD83D\uDC6A\uD83D\uDC6B";
    private static final String CAPITAL_I_WITH_DOT_THEN_A = "?a";

    @Test
    public void test() {
        System.out.println(UnicodeStringUtils.substring(EMOJI_BOY_WOMAN_FAMILY_WOMAN_AND_MAN_HOLDING_HANDS_STRING, 0, 2));
        System.out.println(UnicodeStringUtils.substring(EMOJI_BOY_WOMAN_FAMILY_WOMAN_AND_MAN_HOLDING_HANDS_STRING, 2, 0));
        System.out.println(UnicodeStringUtils.substring(EMOJI_BOY_WOMAN_FAMILY_WOMAN_AND_MAN_HOLDING_HANDS_STRING, 2, 4));
        System.out.println(UnicodeStringUtils.substring(EMOJI_BOY_WOMAN_FAMILY_WOMAN_AND_MAN_HOLDING_HANDS_STRING, 2, 2));
        System.out.println(UnicodeStringUtils.substring(EMOJI_BOY_WOMAN_FAMILY_WOMAN_AND_MAN_HOLDING_HANDS_STRING, -4, 2));
    }

    @Test
    public void testIsUnicodeString() {
        System.out.println(STRING_WITH_NO_SURROGATE_PAIRS);
        System.out.println(STRING_WITH_ONE_SURROGATE_PAIR.length());
        System.out.println(UnicodeStringUtils.containsSurrogatePair(CAPITAL_I_WITH_DOT_THEN_A));
        assertFalse(UnicodeStringUtils.containsSurrogatePair(STRING_WITH_NO_SURROGATE_PAIRS));
        assertTrue(UnicodeStringUtils.containsSurrogatePair(STRING_WITH_ONE_SURROGATE_PAIR));
        assertTrue(UnicodeStringUtils.containsSurrogatePair(
            EMOJI_BOY_WOMAN_FAMILY_WOMAN_AND_MAN_HOLDING_HANDS_STRING)
        );
    }

}
