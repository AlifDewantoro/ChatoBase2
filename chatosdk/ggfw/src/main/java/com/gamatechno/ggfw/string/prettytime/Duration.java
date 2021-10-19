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
 * Represents a quantity of any given {@link TimeUnit}
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public interface Duration
{
   /**
    * Return the calculated quantity of {@link TimeUnit} instances.
    */
   public long getQuantity();

   /**
    * Return the calculated quantity of {@link TimeUnit} instances, rounded up if {@link #getDelta()} is greater than or
    * equal to the given tolerance.
    */
   public long getQuantityRounded(int tolerance);

   /**
    * Return the {@link TimeUnit} represented by this {@link Duration}
    */
   public TimeUnit getUnit();

   /**
    * Return the number of milliseconds left over when calculating the number of {@link TimeUnit}'s that fit into the
    * given time range.
    */
   public long getDelta();

   /**
    * Return true if this {@link Duration} represents a number of {@link TimeUnit} instances in the past.
    */
   public boolean isInPast();

   /**
    * Return true if this {@link Duration} represents a number of {@link TimeUnit} instances in the future.
    */
   public boolean isInFuture();
}
