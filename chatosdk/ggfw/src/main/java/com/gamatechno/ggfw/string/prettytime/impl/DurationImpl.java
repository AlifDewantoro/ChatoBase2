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
package com.gamatechno.ggfw.string.prettytime.impl;

import com.gamatechno.ggfw.string.prettytime.Duration;
import com.gamatechno.ggfw.string.prettytime.TimeUnit;

public class DurationImpl implements Duration
{

   private long quantity;
   private long delta;
   private TimeUnit unit;

   @Override
   public long getQuantity()
   {
      return quantity;
   }

   public void setQuantity(final long quantity)
   {
      this.quantity = quantity;
   }

   @Override
   public TimeUnit getUnit()
   {
      return unit;
   }

   public void setUnit(final TimeUnit unit)
   {
      this.unit = unit;
   }

   @Override
   public long getDelta()
   {
      return delta;
   }

   public void setDelta(final long delta)
   {
      this.delta = delta;
   }

   @Override
   public boolean isInPast()
   {
      return getQuantity() < 0;
   }

   @Override
   public boolean isInFuture()
   {
      return !isInPast();
   }

   @Override
   public long getQuantityRounded(int tolerance)
   {
      long quantity = Math.abs(getQuantity());

      if (getDelta() != 0)
      {
         double threshold = Math
                  .abs(((double) getDelta() / (double) getUnit().getMillisPerUnit()) * 100);
         if (threshold > tolerance)
         {
            quantity = quantity + 1;
         }
      }
      return quantity;
   }

   @Override
   public String toString()
   {
      return "DurationImpl [" + quantity + " " + unit + ", delta=" + delta + "]";
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (delta ^ (delta >>> 32));
      result = prime * result + (int) (quantity ^ (quantity >>> 32));
      result = prime * result + ((unit == null) ? 0 : unit.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      DurationImpl other = (DurationImpl) obj;
      if (delta != other.delta)
         return false;
      if (quantity != other.quantity)
         return false;
      if (unit == null) {
         if (other.unit != null)
            return false;
      }
      else if (!unit.equals(other.unit))
         return false;
      return true;
   }
}
