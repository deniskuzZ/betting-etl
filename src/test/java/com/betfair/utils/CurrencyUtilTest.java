package com.betfair.utils;

import com.betfair.domain.Currency;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.DecimalFormat;
import java.util.Collections;

import static com.betfair.utils.CurrencyUtil.DECIMAL_FORMAT;

/**
 * Created by kuzmende on 11/6/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class CurrencyUtilTest {

    private final DecimalFormat decimalFormat = new DecimalFormat(DECIMAL_FORMAT);

    @Test
    public void formatWithEUR() {
        Assert.assertEquals("€1.25",
                CurrencyUtil.formatWithCurrency(1.25, Currency.EUR));
    }

    @Test
    public void formatWithGBP() {
        Assert.assertEquals("£1.45",
                CurrencyUtil.formatWithCurrency(1.45, Currency.GBP));
    }

    @Test
    public void convertFromGBP() {
        CurrencyUtil.exchrate_to_EUR = Collections.singletonMap("GBP", 1.14);

        Assert.assertEquals(decimalFormat.format(5.7),
                decimalFormat.format(CurrencyUtil.toEUR(5.0, Currency.GBP)));
    }

    @Test
    public void convertFromEUR() {
        Assert.assertEquals(decimalFormat.format(5.0),
                decimalFormat.format(CurrencyUtil.toEUR(5.0, Currency.EUR)));
    }
}
