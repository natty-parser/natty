package org.natty;


import org.junit.Assert;
import org.junit.Test;

import java.util.stream.Collectors;

public class RangeTest {


  @Test
  public void stream() {

    Assert.assertEquals("[10, 9, 8, 7, 6, 5, 4, 3, 2, 1]", Range.stream(Range.ofYears(10, 1)).collect(Collectors.toList()).toString());

    Assert.assertEquals("[-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]", Range.stream(Range.ofYears(-1, 10)).collect(Collectors.toList()).toString());


    Assert.assertEquals("[2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025, 2026]", Range.stream(Range.fromYear(2007)).limit(20).collect(Collectors.toList()).toString());

  }
}
