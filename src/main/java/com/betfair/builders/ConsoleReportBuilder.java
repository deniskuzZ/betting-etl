package com.betfair.builders;

import com.betfair.domain.ReportRecord;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.beanio.Marshaller;
import org.beanio.StreamFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by kuzmende on 11/1/17.
 */
@Service
public class ConsoleReportBuilder {

    private static final Object[] REPORT_HEADERS =
            {"Selection Name", "Currency", "No Of Bets", "Total Stakes", "Total Payout"};

    @Autowired
    private StreamFactory factory;

    public String create(List<ReportRecord> postsPerTypeAndAuthor) {
        AsciiTable at = new AsciiTable();

        at.setTextAlignment(TextAlignment.LEFT);
        at.getRenderer().setCWC(new CWC_LongestLine());

        at.addRule(); at.addRow(REPORT_HEADERS); at.addRule();
        Marshaller marshaller = factory.createMarshaller("betting-report-records");

        postsPerTypeAndAuthor.forEach(it->{
            at.addRow((Object[]) marshaller.marshal(it).toString().split(","));
            at.addRule();
        });
        at.setPaddingLeftRight(1);

        return at.render();
    }
}
