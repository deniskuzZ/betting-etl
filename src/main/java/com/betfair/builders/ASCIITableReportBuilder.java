package com.betfair.builders;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.beanio.Marshaller;
import org.beanio.StreamFactory;

import java.util.List;

/**
 * Created by kuzmende on 11/1/17.
 */
public class ASCIITableReportBuilder<T> {

    private static final Object[] REPORT_HEADERS =
            {"Selection Name", "Currency", "No Of Bets", "Total Stakes", "Total Payout"};

    private StreamFactory streamFactory;
    private String objectMapping;

    public ASCIITableReportBuilder(StreamFactory factory, String objectMapping) {
        this.streamFactory = factory;
        this.objectMapping = objectMapping;
    }

    public String create(List<T> postsPerTypeAndAuthor) {
        AsciiTable at = new AsciiTable();

        at.setTextAlignment(TextAlignment.LEFT);
        at.getRenderer().setCWC(new CWC_LongestLine());

        at.addRule(); at.addRow(REPORT_HEADERS); at.addRule();
        Marshaller marshaller = streamFactory.createMarshaller(objectMapping);

        postsPerTypeAndAuthor.forEach(it->{
            at.addRow((Object[]) marshaller.marshal(it).toString().split(","));
            at.addRule();
        });
        at.setPaddingLeftRight(1);

        return at.render();
    }
}
