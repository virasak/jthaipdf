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
package com.googlecode.jthaipdf.itext;

import com.googlecode.jthaipdf.util.ThaiDisplayUtils;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;


public class ThaiChunk extends Chunk {

	public ThaiChunk(char c, Font font) {
		super(c, font);
		manageContent();
	}

	public ThaiChunk(String content, Font font) {
		super(content, font);
		manageContent();
	}
	
	public ThaiChunk(char c) {
		super(c);
		manageContent();
	}

	public ThaiChunk(Chunk ck) {
		super(ck);
		manageContent();
	}


	public ThaiChunk(String content) {
		super(content);
		manageContent();
	}

	private void manageContent() {
		ThaiDisplayUtils.toDisplayString(content);
	}

}
