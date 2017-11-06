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

    private final StreamFactory streamFactory;
    private final String objectMapping;

    private final Object[] reportHeaders;

    public ASCIITableReportBuilder(StreamFactory factory, String objectMapping, Object[] reportHeaders) {
        this.streamFactory = factory;
        this.objectMapping = objectMapping;

        this.reportHeaders = reportHeaders;
    }

    public String create(List<T> reportRecords) {
        AsciiTable at = new AsciiTable();

        at.setTextAlignment(TextAlignment.LEFT);
        at.getRenderer().setCWC(new CWC_LongestLine());

        at.addRule(); at.addRow(reportHeaders); at.addRule();
        Marshaller marshaller = streamFactory.createMarshaller(objectMapping);

        reportRecords.forEach(it->{
            at.addRow((Object[]) marshaller.marshal(it).toString().split(","));
            at.addRule();
        });
        at.setPaddingLeftRight(1);

        return at.render();
    }
}
