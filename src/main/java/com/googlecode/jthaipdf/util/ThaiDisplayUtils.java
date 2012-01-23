/*
 * The MIT License
 *
 * Copyright (c) 2008 Virasak Dungsrikaew (virasak@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.googlecode.jthaipdf.util;


public class ThaiDisplayUtils {

	/**
	 * Rearrange Thai glyph
	 * @param value
	 * @return string for display
	 */
	public static String toDisplayString(String value) {
		return new String(toDisplayString(value.toCharArray())); 
	}

	/**
	 * Rearrange Thai glyph
	 * @param value
	 * @return string for display
	 */
	public static void toDisplayString(StringBuffer value) {
		char[] content = new char[value.length()];
		value.getChars(0, value.length(), content, 0);
		content = toDisplayString(content);
		value.setLength(0);
		value.append(content);
	}

	/**
	 * Rearrange Thai glyph
	 * @param content
	 * @return string for display
	 */
	public static char[] toDisplayString(char[] content) {
		
		content = explodeSaraAm(content);

		int length = content.length;
		char pch = 'a'; //previous character start with dummy value

		// Replace upper and lower character with un-overlapped character
		for (int i=0; i < length; i++) {
			char ch = content[i];

			if (isUpperLevel1(ch) && isUpTail(pch)) { // Level 1 and up-tail
				content[i] = shiftLeft(ch);

			} else if (isUpperLevel2(ch)) {
				// Level 2
				if (isLowerLevel(pch)) {
					pch = content[i-2];
				}

				if (isUpTail(pch)) {
					content[i] = pullDownAndShiftLeft(ch);
				} else if (isLeftShiftUpperLevel1(pch)) {
					content[i] = shiftLeft(ch);
				} else if (!isUpperLevel1(pch)) {
					content[i] = pullDown(ch);
				}

			} else if (isLowerLevel(ch) && isDownTail(pch)) { // Lower level and down-tail
				char cutch = cutTail(pch); 
				if (pch != cutch) {
					content[i-1] =cutch;
				} else {
					content[i] = pullDown(ch);
				}
			}
			pch = content[i];
		}
		
		return content;
	}

	private static char[] explodeSaraAm(char[] content) {
		int count = countSaraAm(content);
		
		if (count == 0) {
			return content.clone();
		}
		
		
		char[] newContent = new char[content.length + count]; // other chars length + 2*(SARA_AM length)

		int j = 0;

		// Exploded SARA_AM to NIKHAHIT + SARA_AA
		for (int i=0; i < content.length; i++) { 
			char ch = content[i];
			if (i < content.length - 1  && content[i + 1] == SARA_AM) {
				if (isUpperLevel2(ch)) {
					newContent[j++] = NIKHAHIT;
					newContent[j++] = ch;
				} else {
					newContent[j++] = ch;
					newContent[j++] = NIKHAHIT;
				}
			} else if (ch == SARA_AM){
				newContent[j++] =SARA_AA;
			} else {
				newContent[j++] = ch;
			}
		}
		
		return newContent;
	}
	
	private static int countSaraAm(char[] content) {
		int count = 0;
		for (int i = 0; i < content.length; i++) {
			if (content[i] == SARA_AM) {
				count++;
			}
		}
		return count;
	}


	private static boolean isUpTail(char ch) {

		return ch == PO_PLA || ch == FO_FA || ch == FO_FAN || ch == LO_CHULA;
	}
	
	private static boolean isDownTail(char ch) {
		return ch == THO_THAN || ch == YO_YING || ch == DO_CHADA 
				|| ch == TO_PATAK || ch == RU || ch == LU;
	}

	private static boolean isUpperLevel1(char ch) {
		return ch == MAI_HAN_AKAT || ch == SARA_I || ch == SARA_Ii
				|| ch == SARA_Ue || ch == SARA_Uee || ch == MAI_TAI_KHU
				|| ch == NIKHAHIT;
	}

	private static boolean isLeftShiftUpperLevel1(char ch) {
		return ch == MAI_HAN_AKAT_LEFT_SHIFT || ch == SARA_I_LEFT_SHIFT || ch == SARA_Ii_LEFT_SHIFT
		|| ch == SARA_Ue_LEFT_SHIFT || ch == SARA_Uee_LEFT_SHIFT || ch == MAI_TAI_KHU_LEFT_SHIFT
		|| ch == NIKHAHIT_LEFT_SHIFT;
	}

	
	private static boolean isUpperLevel2(char ch) {
		return ch == MAI_EK	|| ch == MAI_THO || ch == MAI_TRI
				|| ch == MAI_CHATTAWA || ch == THANTHAKHAT;
	}

	public static boolean isLowerLevel(char ch) {
		return ch == SARA_U || ch == SARA_UU || ch == PHINTHU;
	}

	public static char pullDownAndShiftLeft(char ch) {
		switch (ch) {
		case MAI_EK:
			return MAI_EK_PULL_DOWN_AND_LEFT_SHIFT;
		case MAI_THO:
			return MAI_THO_PULL_DOWN_AND_LEFT_SHIFT;
		case MAI_TRI:
			return MAI_TRI_PULL_DOWN_AND_LEFT_SHIFT;
		case MAI_CHATTAWA:
			return MAI_CHATTAWA_PULL_DOWN_AND_LEFT_SHIFT;
		case MAI_HAN_AKAT:
			return MAI_HAN_AKAT_LEFT_SHIFT;
		case THANTHAKHAT:
			return THANTHAKHAT_PULL_DOWN_AND_LEFT_SHIFT;
		default:
			return ch;
		}
	}

	public static char shiftLeft(char ch) {
		switch (ch) {
		case MAI_EK:
			return MAI_EK_LEFT_SHIFT;
		case MAI_THO:
			return MAI_THO_LEFT_SHIFT;
		case MAI_TRI:
			return MAI_TRI_LEFT_SHIFT;
		case MAI_CHATTAWA:
			return MAI_CHATTAWA_LEFT_SHIFT;
		case MAI_HAN_AKAT:
			return MAI_HAN_AKAT_LEFT_SHIFT;
		case SARA_I:
			return SARA_I_LEFT_SHIFT;
		case SARA_Ii:
			return SARA_Ii_LEFT_SHIFT;
		case SARA_Ue:
			return SARA_Ue_LEFT_SHIFT;
		case SARA_Uee:
			return SARA_Uee_LEFT_SHIFT;
		case MAI_TAI_KHU:
			return MAI_TAI_KHU_LEFT_SHIFT;
		case NIKHAHIT:
			return NIKHAHIT_LEFT_SHIFT;
		default:
			return ch;
		}
	}
	private static char pullDown(char ch) {
		switch (ch) {
		case MAI_EK:
			return MAI_EK_DOWN;
		case MAI_THO:
			return MAI_THO_DOWN;
		case MAI_TRI:
			return MAI_TRI_DOWN;
		case MAI_CHATTAWA:
			return MAI_CHATTAWA_DOWN;
		case THANTHAKHAT:
			return THANTHAKHAT_DOWN;
		case SARA_U:
			return SARA_U_DOWN;
		case SARA_UU:
			return SARA_UU_DOWN;
		case PHINTHU:
			return PHINTHU_DOWN;
		default:
			return ch;
		}
	}
	
	
	private static char cutTail(char ch) {
		switch(ch) {
		case THO_THAN:
			return THO_THAN_CUT_TAIL;
		case YO_YING:
			return YO_YING_CUT_TAIL;
		default:
			return ch;
		}
	}	

	// Lower level characters
	public static final char SARA_U = 0xE38;
	public static final char SARA_UU = 0xE39;
	public static final char PHINTHU = 0xE3A;
	
	// Lower level characters after pullDown
	public static final char SARA_U_DOWN = 0xF718;
	public static final char SARA_UU_DOWN = 0xF719;
	public static final char PHINTHU_DOWN = 0xF71A;

	// Upper level 1 characters
	public static final char MAI_HAN_AKAT = 0xE31;
	public static final char SARA_AM = 0xE33;
	public static final char SARA_I = 0xE34;
	public static final char SARA_Ii = 0xE35;
	public static final char SARA_Ue = 0xE36;
	public static final char SARA_Uee = 0xE37;
	public static final char MAI_TAI_KHU = 0xE47;

	// Upper level 1 characters after shift left
	public static final char MAI_HAN_AKAT_LEFT_SHIFT = 0xF710;
	public static final char SARA_I_LEFT_SHIFT = 0xF701;
	public static final char SARA_Ii_LEFT_SHIFT = 0xF702;
	public static final char SARA_Ue_LEFT_SHIFT = 0xF703;
	public static final char SARA_Uee_LEFT_SHIFT = 0xF704;
	public static final char MAI_TAI_KHU_LEFT_SHIFT = 0xF712;

	// Upper level 2 characters
	public static final char MAI_EK = 0xE48;
	public static final char MAI_THO = 0xE49;
	public static final char MAI_TRI = 0xE4A;
	public static final char MAI_CHATTAWA = 0xE4B;
	public static final char THANTHAKHAT = 0xE4C;
	public static final char NIKHAHIT = 0xE4D;

	// Upper level 2 characters after pull down
	public static final char MAI_EK_DOWN = 0xF70A;
	public static final char MAI_THO_DOWN = 0xF70B;
	public static final char MAI_TRI_DOWN = 0xF70C;
	public static final char MAI_CHATTAWA_DOWN = 0xF70D;
	public static final char THANTHAKHAT_DOWN = 0xF70E;
	
	// Upper level 2 characters after pull down and shift left
	public static final char MAI_EK_PULL_DOWN_AND_LEFT_SHIFT = 0xF705;
	public static final char MAI_THO_PULL_DOWN_AND_LEFT_SHIFT = 0xF706;
	public static final char MAI_TRI_PULL_DOWN_AND_LEFT_SHIFT = 0xF707;
	public static final char MAI_CHATTAWA_PULL_DOWN_AND_LEFT_SHIFT = 0xF708;
	public static final char THANTHAKHAT_PULL_DOWN_AND_LEFT_SHIFT = 0xF709;

	// Upper level 2 characters after shift left
	public static final char MAI_EK_LEFT_SHIFT = 0xF713;
	public static final char MAI_THO_LEFT_SHIFT = 0xF714;
	public static final char MAI_TRI_LEFT_SHIFT = 0xF715;
	public static final char MAI_CHATTAWA_LEFT_SHIFT = 0xF716;
	public static final char THANTHAKHAT_LEFT_SHIFT = 0xF717;
	public static final char NIKHAHIT_LEFT_SHIFT = 0xF711;

	// Up tail characters
	public static final char PO_PLA = 0x0E1B;
	public static final char FO_FA = 0x0E1D;
	public static final char FO_FAN = 0x0E1F;
	public static final char LO_CHULA = 0x0E2C;	

	// Down tail characters
	public static final char THO_THAN = 0xE10;
	public static final char YO_YING = 0xE0D;
	public static final char DO_CHADA = 0xE0E;
	public static final char TO_PATAK = 0xE0F;
	public static final char RU = 0xE24;
	public static final char LU = 0xE26;

	// Cut tail characters
	public static final char THO_THAN_CUT_TAIL = 0xF700;
	public static final char YO_YING_CUT_TAIL = 0xF70F;
	
	// for exploded SARA_AM (NIKHAHIT + SARA_AA)
	public static final char SARA_AA = 0xE32;

}
