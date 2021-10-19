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
 * Defines a Unit of time (e.g. seconds, minutes, hours) and its conversion to milliseconds.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface TimeUnit
{

   /**
    * The number of milliseconds represented by each instance of this TimeUnit. Must be a positive number greater than
    * zero.
    */
   public long getMillisPerUnit();

   /**
    * The maximum quantity of this Unit to be used as a threshold for the next largest Unit (e.g. if one
    * <code>Second</code> represents 1000ms, and <code>Second</code> has a maxQuantity of 5, then if the difference
    * between compared timestamps is larger than 5000ms, TimeConversion will move on to the next smallest TimeUnit for
    * calculation; <code>Minute</code>, by default)
    * <p>
    * millisPerUnit * maxQuantity = maxAllowedMs
    * <p>
    * If maxQuantity is zero, it will be equal to the next highest <code>TimeUnit.getMillisPerUnit() /
    * this.getMillisPerUnit()</code> or infinity if there are no greater TimeUnits
    */
   public long getMaxQuantity();

   /**
    * Whether or not this {@link TimeUnit} represents a price measurement of time, or a general concept of time. E.g:
    * "minute" as opposed to "moment".
    */
   public boolean isPrecise();
}
