/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2018. Shendy Aditya Syamsudin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.gamatechno.ggfw.string.prettytime;

/**
 * Format a Duration object.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface TimeFormat
{
   /**
    * Given a populated {@link Duration} object. Apply formatting (with rounding) and output the result.
    * 
    * @param {@link Duration} instance from which the time string should be decorated.
    */
   public abstract String format(final Duration duration);

   /**
    * Given a populated {@link Duration} object. Apply formatting (without rounding) and output the result.
    * 
    * @param {@link Duration} instance from which the time string should be decorated.
    */
   public String formatUnrounded(Duration duration);

   /**
    * Decorate with past or future prefix/suffix (with rounding)
    * 
    * @param duration The original {@link Duration} instance from which the time string should be decorated.
    * @param time The formatted time string.
    */
   public String decorate(Duration duration, String time);

   /**
    * Decorate with past or future prefix/suffix (without rounding)
    * 
    * @param duration The original {@link Duration} instance from which the time string should be decorated.
    * @param time The formatted time string.
    */
   public String decorateUnrounded(Duration duration, String time);

}