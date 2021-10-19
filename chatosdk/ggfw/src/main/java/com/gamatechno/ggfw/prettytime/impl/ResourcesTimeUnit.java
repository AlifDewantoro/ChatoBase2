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
package com.gamatechno.ggfw.prettytime.impl;


import com.gamatechno.ggfw.prettytime.TimeUnit;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public abstract class ResourcesTimeUnit implements TimeUnit
{
   private long maxQuantity = 0;
   private long millisPerUnit = 1;

   /**
    * Return the name of the resource bundle from which this unit's format should be loaded.
    */
   abstract protected String getResourceKeyPrefix();

   protected String getResourceBundleName()
   {
      return "org.ocpsoft.prettytime.i18n.Resources";
   }

   @Override
   public long getMaxQuantity()
   {
      return maxQuantity;
   }

   public void setMaxQuantity(long maxQuantity)
   {
      this.maxQuantity = maxQuantity;
   }

   @Override
   public long getMillisPerUnit()
   {
      return millisPerUnit;
   }

   public void setMillisPerUnit(long millisPerUnit)
   {
      this.millisPerUnit = millisPerUnit;
   }

   @Override
   public boolean isPrecise()
   {
      return true;
   }

   @Override
   public String toString()
   {
      return getResourceKeyPrefix();
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + (int) (maxQuantity ^ (maxQuantity >>> 32));
      result = prime * result + (int) (millisPerUnit ^ (millisPerUnit >>> 32));
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
      ResourcesTimeUnit other = (ResourcesTimeUnit) obj;
      if (maxQuantity != other.maxQuantity)
         return false;
      if (millisPerUnit != other.millisPerUnit)
         return false;
      return true;
   }
}
